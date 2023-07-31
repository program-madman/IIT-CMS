<template>
  <v-container fluid>
    <v-row no-gutters pa-0 ma-0>
      <v-col sm="12">
        <search-bar @onSearch="onSearch" @onReset="onReset" :departments="departments" />
        <loading v-if="loadingview" />
        <empty-view v-if="emptyViewShow" />
        <v-row align="center" dense class="pa-0 ma-0">
          <v-col v-for="(item, i) in items" :key="i" cols="12">
            <div
              @click.prevent="toDetail(item.id)"
              :style="item.readStatus ==0 ? borderStatus : ''"
              class="card pt-4"
            >
              <span class="article-time-text ma-0 pa-0 ml-4">
                {{ item.lastUpdateTime }}
              </span>

              <div class="pa-0 ma-0 ml-4 mt-2">
                <p
                  class="article-title-text mt-0 pa-0"
                  :style="item.readStatus ==0 ? 'color:#1867C0' : ''"
                  v-html="$options.filters.lenghth(item.title, 140)"
                ></p>
                <p
                  class="article-intro-text pa-0 pt-1 html-content"
                  v-html="$options.filters.lenghth(item.intro, 180)"
                ></p>
                <div
                  v-if="item.attachmentCount || item.articleNo"
                  class="article-report-text mt-2 mr-3 ml-0 pa-0"
                >
                  <span v-if="item.attachmentCount" class="mr-6"
                    >Attachments({{ item.attachmentCount }})</span
                  >
                  <span v-if="item.articleNo"
                    >Category:{{ item.articleNo }}</span
                  >
                </div>
              </div>
              
              <div class="mt-2 mb-1 ml-4 pa-0 pb-3 caption">
                  <template> 
                      <button
                      v-bind="attrs"
                      v-on="on"
                      class="article-operator-text"
                      @click.stop="collect(item)"
                    >
                      <v-icon class="article-operator-icon">
                        {{
                          item.favorite > 0 ? "mdi-star" : "mdi-star-outline"
                        }}
                      </v-icon>
                      {{ item.favorite > 0 ? "favorite" : "favorite" }}
                    </button>

                <button
                  class="article-operator-text ml-4">
                  <v-icon class="article-operator-icon">
                    mdi-thumb-up
                  </v-icon>
                  {{item.like}}
                </button>

                  </template> 
              </div>
            </div>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import "@/assets/css/article.css";
import {  removeCollection,getAllArticle } from "@/api/getData.js";
import { EventBus } from "@/utils/event-bus";
import {
  highLightText,
  formatHtml,
} from "@/utils/articleUtils.js";
import { mapGetters } from "vuex";
import { aesEncrypt } from "@/utils/encryption.js";
export default {
  components: {
    searchBar: () => import("./articleList/searchBar.vue"),
    loading: () => import("@/components/base/loading.vue"),
    emptyView: () => import("@/components/base/empty-view.vue"),
  },
  data() {
    return {
      data: null,
      loading: false,
      loadingview: true,
      emptyViewShow: false,
      departments: ["All"],
      borderStatus: "border-left:3px solid #1867C0 !important;",
      items: [],
    };
  },
  computed: {
    ...mapGetters({
      hasEditPermission: "user/hasEditPermission",
    }),
  },

  methods: {

    onSearch(data) {
      if (this.loading == true) return;
      this.items = [];
      this.data = data;
      this.searchArticles(this.data);
      this.emptyViewShow = false;
      this.loadingview = true;
    },

    onReset(data) {
      if (this.loading == true) return;
      this.onSearch(data);
    },

    searchArticles(data) {      
       console.log(data);
      this.loading = true;
      getAllArticle(data)
        .then((res) => {
          if (res == null || typeof res == "undefined" || res.code == null || 
              typeof res.code == "undefined" || res.data.length == 0) {
            this.showEmptyViewIfNeeded();
            return;
          }
         
          this.data.pageNum++;
          let aTemp = [];

          res.data.forEach((item) => {
            let dTemp = {};
            dTemp["id"] = item.articleId;
            dTemp["title"] = formatHtml(item.articleTitle);
            dTemp["intro"] = formatHtml(item.articleContent);
            dTemp["lastUpdateTime"] = item.publishTime;
            dTemp["deptment"] = item.authorDeptName
            //附件数量
            dTemp["attachmentCount"] = item.attachmentTotalCount
            //阅读数量
            dTemp["readNum"] = 2;
            //文件编号
            dTemp["articleNo"] = item.articleCategory
            //是否已读
            dTemp["readStatus"] = item.isRead;
            //收藏状态
            dTemp["favorite"] = item.isFav;
            //点赞数
            dTemp["like"] = item.likes
            //收藏操作选择状态寄存
            dTemp["favoriteSelect"] = null;
            aTemp.push(dTemp);
          });
          this.items = this.items.concat(aTemp);
          console.debug("add list" + aTemp);
          highLightText(this.items, this.data.searchContent);
        })
        .catch((res) => {
          console.log(res);
          this.showEmptyViewIfNeeded();
        })
        .finally(() => {
          EventBus.$emit("articleListComplete", "init");
          this.loading = false;
          this.loadingview = false;
        });
    },
    showEmptyViewIfNeeded() {
      if (this.data==null || this.data.pageNum == 1) {
        this.loading = false;
        this.emptyViewShow = true;
      }
    },

    //跳转详情页
    toDetail(id) {
      this.$router.push({
        path: "/artcleDetail",
        query: { id: aesEncrypt(id + "") },
      });
    },
  
    //文章收藏
    collect(item) {
      item.favorite = !item.favorite
    },
  },
  
  //文章收藏
  removeFavourite(item) {
    //选择收藏的文章的指针
    // this.items.splice(0, 1)
    //let item = this.items[index];
    let data = {
      msgId: item.id,
      archiveId: item.favorite,
    };
    removeCollection(data)
      .then((res) => {
        console.debug(res);
        if(res && res.code ==0) {
          item.favorite = 0;
          this.items = JSON.parse(JSON.stringify(this.items))
        }
        // var i = this.items.indexOf(item);
        // if (i >= 0) {
        //   this.items.splice(i, 1);
        // }
      })
      .catch();
    //选择的文件夹
  },

  //过滤文字
  filters: {
    lenghth(text, length, suffix = "...") {
      if (!text) return;
      if (text.length <= length) {
        return text;
      }
      let hightLightEle = '<font color="red">';
      if (text.indexOf(hightLightEle) >= 0) {
        length += 25;
      }

      return (text + "").substring(0, length) + suffix;
    },
  },
};
</script>
<style>
.html-content p {
  margin-bottom: 0px;
}
</style>
<template>
  <v-container fluid>

    <v-row no-gutters pa-0 ma-0>
      <v-col sm="12">
        <search-bar
          @onSearch="onSearch"
          @onReset="onReset"
          :departments="departments"
        />
        
        <loading v-if="loadingview " />
        <empty-view v-if="emptyViewShow" />
        

        <v-row align="center" dense class="pa-0 ma-0">
          <v-col v-for="(item, i) in items" :key="i" cols="12">
            <div
              @click.prevent="toDetail(item.id)"
              :style="item.readStatus ==0 ? borderStatus : ''"
              class="card pt-4"
            >
              <div class="mt-0 mb-0 ml-4 pa-0" style="display: flex">
                <span class="deptment">{{ item.deptment }}</span>
                <v-spacer></v-spacer>
                <v-img
                  max-height="20"
                  max-width="57"
                  :src="item.articleTypeImg"
                >
                  <template>
                    <v-row
                      class="fill-height ma-0"
                      align="center"
                      justify="center"
                    >
                      <span class="article-type-text">{{
                        item.articleType
                      }}</span>
                    </v-row>
                  </template>
                </v-img>
              </div>
              <span class="article-time-text ma-0 pa-0 ml-4">
                {{ item.lastUpdateTime }}
              </span>

              <div style="margin: 0; padding: 0" class="ml-4 mt-2">
                <p
                  class="article-title-text mt-0 pa-0"
                  :style="item.readStatus ==0 ? 'color:#1867C0' : ''"
                  v-html="$options.filters.lenghth(item.title, 50)"
                ></p>
                <p
                  class="article-intro-text pa-0 pt-1 html-content"
                  v-html="$options.filters.lenghth(item.intro, 90)"
                ></p>
                <div
                  v-if="item.attachmentCount || item.articleNo"
                  class="article-report-text mt-2 mr-3 ml-0 pa-0"
                >
                  <span v-if="item.attachmentCount" class="mr-6"
                    >附件({{ item.attachmentCount }})</span
                  >
                  <span v-if="item.articleNo"
                    >文章编号:{{ item.articleNo }}</span
                  >
                </div>
              </div>
              <div class="mt-2 mb-1 ml-4 pa-0 pb-3 caption">
                <button
                  v-if="hasEditPermission"
                  class="article-operator-text mr-2"
                  @click.stop="saveAs(item.id)"
                >
                  <v-icon class="article-operator-icon">
                    mdi-file-plus-outline
                  </v-icon>
                  另存为
                </button>
                <button
                  class="article-operator-text"
                  @click.stop="collect(i)"
                >
                  <v-icon class="article-operator-icon"> mdi-star </v-icon>
                  取消收藏
                </button>
              </div>
            </div>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <page-loader @onLoadMore="onLoadMore" />
  </v-container>
</template>

<script>
import "@/assets/css/article.css";
import {
  getArticleByArchiveCategory,
  removeCollection,
} from "@/api/getData.js";
import {
  getMsgStatusDesc,
  highLightText,
  getMsgStatusImg,
  formatHtml,
} from "@/utils/articleUtils.js";
import { mapGetters } from "vuex";
import { aesEncrypt } from "@/utils/encryption.js";
export default {
  props: {
    archiveId: Number,
  },

  components: {
    searchBar: () => import("./searchBar.vue"),
    pageLoader: () => import("./pageLoader.vue"),
    loading: () => import("@/components/base/loading.vue"),
    emptyView: () => import("@/components/base/empty-view.vue"),
    
  },
  computed: {
    ...mapGetters({
      // 把 `this.doneCount` 映射为 `this.$store.getters.doneTodosCount`
      hasEditPermission: "user/hasEditPermission",
    }),
  },
  data() {
    return {
      dialog:true,
      allowSaveAs:false,
      data: null,
      loading: false,
      loadingview: true,
      emptyViewShow: false,
      departments: ["全部"],
      borderStatus: "border-left:3px solid #1867C0 !important;",
      items: [],
    };
  },
  methods: {
    onSearch(data) {
      if (this.loading == true) return;
      this.items = [];
      this.data = data;
      this.data.archiveId = this.archiveId;
      this.loadingview = true;
      this.emptyViewShow = false;
      this.searchArticles(this.data);
    },
    onReset(data) {
      if (this.loading == true) return;
      this.onSearch(data);
    },

    onLoadMore() {
      if (this.loading == true) return;
      this.searchArticles(this.data);
    },
    searchArticles(data) {
      if (this.loading == true) return;
      this.loading = true;
      getArticleByArchiveCategory(data)
        .then((res) => {
          console.debug(res);
          if (
            res == null ||
            typeof res == "undefined" ||
            res.code == null ||
            typeof res.code == "undefined" ||
            res.code != 0 ||
            res.data.messageList.length == 0
          ) {
            this.showEmptyViewIfNeeded();
            return;
          }

          if (this.data.pageNum == 1) {
            let departments = ["全部"];
            this.departments = departments.concat(res.data.departments);
          }

          this.data.pageNum++;
          let aTemp = [];
          res.data.messageList.forEach((item) => {
            let dTemp = {};
            dTemp["id"] = item.id;
            dTemp["title"] = formatHtml(item.title);
            dTemp["intro"] = formatHtml(item.content);
            dTemp["lastUpdateTime"] = item.releaseTime;
            dTemp["deptment"] = item.creatorCompanyname;

            //文章状态:0:草稿 1:已发布  4:已失效
            dTemp["articleType"] = getMsgStatusDesc(item.msgStatus);
            dTemp["articleTypeImg"] = getMsgStatusImg(item.msgStatus);
            //附件数量
            dTemp["attachmentCount"] = item.attachmentCount;
            //阅读数量
            dTemp["readNum"] = item.readCount;
            //文件编号
            dTemp["articleNo"] = item.msgNumber;
            //是否已读
            dTemp["readStatus"] = item.readStatus;
            //收藏状态
            dTemp["favorite"] = item.archiveStatus;
            aTemp.push(dTemp);
          });
          this.items = this.items.concat(aTemp);
          highLightText(this.items, this.data.searchContent);
        })
        .catch((res) => {
          console.log(res);
          this.showEmptyViewIfNeeded();
        })
        .finally(() => {
          this.loading = false;
          this.loadingview = false;
        });
    },
    showEmptyViewIfNeeded() {
      if (this.data.pageNum == 1) {
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
    //跳转发布文章
    saveAs(id) {
      this.$router.push({
        name: "publishArticle",
        params: { id: id },
      });
    },

    //文章收藏
    collect(index) {
      //选择收藏的文章的指针
      // this.items.splice(0, 1)
      let item = this.items[index];
      let data = {
        msgId: item.id,
        archiveId: this.archiveId,
      };
      removeCollection(data)
        .then((res) => {
          console.debug(res);
          var i = this.items.indexOf(item);
          if (i >= 0) {
            this.items.splice(i, 1);
          }
        })
        .catch();
      //选择的文件夹
    },

    //撤回文章
    withdraw(index) {
      //选择收藏的文章的指针
      console.log(index);
      //选择的文件夹
      this.items[index].dlog2 = false;
      console.log(this.tempItems[index]);
    },
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
<style scoped>
.un-read-line {
  color: #1867c0ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 18px;
  line-height: 24px;
  letter-spacing: 0px;
  text-align: left;
}
</style>
<style>
.html-content p {
  margin-bottom: 0px;
}
</style>
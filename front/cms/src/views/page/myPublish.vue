<template>
  <v-container fluid>
    <v-row no-gutters pa-0 ma-0>
      <v-col sm="12">
        <search-bar
          @onSearch="onSearch"
          @onReset="onReset"
          :departments="departments"
        />
        <loading v-if="loadingview" />
        <empty-view v-if="emptyViewShow" />
        <v-row align="center" dense>
          <v-col v-for="(item, i) in items" :key="i" cols="12">
            <div @click="toDetail(item.id)" class="card pt-4">
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

              <div class="pa-0 ma-0 ml-4 mt-2">
                <p
                  class="article-title-text mt-0 pa-0"
                  :style="item.readStatus ? 'color:#1867C0' : ''"
                  v-html="$options.filters.lenghth(item.title, 50)"
                ></p>
                <p
                  class="article-intro-text pa-0 pt-1 html-content"
                  v-html="$options.filters.lenghth(item.intro, 90)"
                ></p>

                <div class="article-report-text ma-0 mt-2 mr-3 ml-0 pa-0">
                  <span v-if="item.attachmentCount" class="mr-6"
                    >附件({{ item.attachmentCount }})</span
                  >
                  <span class="mr-6">阅读量({{ item.readCount }})</span>
                  <span class="mr-6">回复({{ item.feedbackCount }})</span>
                  <span v-if="item.articleNo" class="mr-6"
                    >文章编号:{{ item.articleNo }}</span
                  >
                </div>
              </div>

              <div class="mt-2 mb-1 ml-4 pa-0 pb-3 caption">
                <v-dialog v-model="item.dlog2" max-width="400">
                  <template v-slot:activator="{ on, attrs }">
                    <button
                      class="article-operator-text"
                      v-bind="attrs"
                      v-on="on"
                    >
                      <v-icon class="article-operator-icon">
                        mdi-arrow-left-top
                      </v-icon>
                      撤回
                    </button>
                  </template>
                  <v-card>
                    <v-card-title justify-center>确认要撤回吗</v-card-title>
                    <v-card-text
                      >如需重新编辑，请先将文章另存。是否现在撤回这篇文章？</v-card-text
                    >
                    <v-card-actions>
                      <v-btn
                        color="blue darken-1"
                        text
                        @click="item.dlog2 = false"
                      >
                        关闭
                      </v-btn>
                      <v-spacer></v-spacer>
                      <v-btn
                        color="blue darken-1"
                        text
                        @click="withdraw(item, i)"
                      >
                        确认
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
                <button
                  class="article-operator-text ml-6"
                  v-if="hasEditPermission"
                  @click.stop="saveAs(item.id)"
                >
                  <v-icon class="article-operator-icon">
                    mdi-file-plus-outline
                  </v-icon>
                  另存为
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
import {
  getMyArticle,
  withdrawMessage,
  collectionOperation,
} from "@/api/getData.js";
import { ThrottleItem } from "@/utils/tools.js";
import {
  getMsgStatusDesc,
  highLightText,
  getMsgStatusImg,
  formatHtml,
} from "@/utils/articleUtils.js";
import { mapGetters } from "vuex";
import { aesEncrypt } from "@/utils/encryption.js";
export default {
  components: {
    searchBar: () => import("./articleList/searchBar.vue"),
    pageLoader: () => import("./articleList/pageLoader.vue"),
    loading: () => import("@/components/base/loading.vue"),
    emptyView: () => import("@/components/base/empty-view.vue"),
  },
  data() {
    return {
      data: null,
      loading: false,
      loadingview: true,
      emptyViewShow: false,
      departments: ["全部"],
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
      getMyArticle(data)
        .then((res) => {
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
            //部门
            dTemp["deptment"] = item.creatorCompanyname;
            //更新时间
            dTemp["lastUpdateTime"] = item.releaseTime;
            //文章状态:0:草稿 1:已发布 2:召回 3:已修改 4:已失效
            dTemp["articleType"] = getMsgStatusDesc(item.msgStatus);
            dTemp["articleTypeImg"] = getMsgStatusImg(item.msgStatus);
            //附件数量
            dTemp["attachmentCount"] = item.attachmentCount;
            //回复数
            dTemp["feedbackCount"] = !item.feedbackCount ? 0 : item.feedbackCount;
            //阅读数
            dTemp["readCount"] = !item.readCount? 0: item.readCount;
            //文件编号
            dTemp["articleNo"] = item.msgNumber;

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
      if (this.data==null || this.data.pageNum == 1) {
        this.loading = false;
        this.emptyViewShow = true;
      }
    },

    //跳转详情页
    toDetail(id) {
      this.$router.push({
        path: "/artcleDetail",
        query: {
          id: aesEncrypt(id + ""),
          isMyPublish: true,
        },
      });
    },
    //跳转另存为发布文章
    saveAs(id) {
      this.$router.push({
        name: "publishArticle",
        params: { id: id },
      });
    },
    //文章收藏
    collect(index) {
      //选择收藏的文章的指针
      let item = this.items[index];
      let type = Number(item.favoriteSelect);
      let params = {
        msgId: Number(item.id),
        archiveId: type,
      };
      console.debug("收藏类型=======>" + type);
      item.favoriteSelect = null;

      collectionOperation(params)
        .then((res) => {
          console.debug("修改文章类型" + res);
          this.items[index].favorite = type;
        })
        .catch()
        .finally(() => (this.items[index].dlog = false));
    },

    withdraw: ThrottleItem(function (item, index) {
      this.items[index].dlog2 = false;
      withdrawMessage(item.id)
        .then((res) => {
          if ((!res && typeof res != "undefined") || res.code != 0) return;
          var i = this.items.indexOf(item);
          if (i >= 0) {
            this.items.splice(i, 1);
          }
        })
        .catch();
    }),
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
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
        <v-row align="center" dense class="pa-0 ma-0">
          <v-col v-for="(item, i) in items" :key="i" cols="12">
            <div @click="toDetail(item.id)" class="card pt-4">
              <div class="mt-0 mb-0 ml-4 pa-0" style="display: flex">
                <span class="deptment">{{ item.deptment }}</span>
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
                <div class="article-report-text mt-2 mr-3 ml-0 pa-0">
                  <span v-if="item.attachmentCount" class="mr-6"
                    >附件({{ item.attachmentCount }})</span
                  >
                  <span class="mr-6">阅读量({{ item.readNum }})</span>
                  <span class="mr-6">回复({{ item.replyNum }})</span>
                  <span v-if="item.articleNo" class="mr-6"
                    >文章编号:{{ item.articleNo }}</span
                  >
                </div>
              </div>

              <div class="mt-2 mb-1 ml-4 pa-0 pb-3 caption">
                <button
                  class="article-operator-text"
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
import "@/assets/css/article.css";
import { getArticleByStatusCategory } from "@/api/getData.js";
import { highLightText, formatHtml } from "@/utils/articleUtils.js";
import { aesEncrypt } from "@/utils/encryption.js";
import { mapGetters } from "vuex";
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
      this.data.msgStatus = 2;
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
      getArticleByStatusCategory(data)
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
            //附件数量
            dTemp["attachmentCount"] = item.attachmentCount;
            //回复数
            dTemp["replyNum"] = !item.feedbackCount ? 0 : item.feedbackCount;
            //阅读数
            dTemp["readNum"] = !item.readCount
              ? 0
              : item.readCount;
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
  },
  //过滤文字
  filters: {
    lenghth(text, length, suffix = "...") {
      if (!text) return;
      text = text.replace(/<[^>]+>/g, "");
      if (text.length <= length) {
        return text;
      }
      return (text + "").replace(/\n/g, "<br/>").substring(0, length) + suffix;
    },
  },
};
</script>
<style>
.html-content p {
  margin-bottom: 0px;
}
</style>
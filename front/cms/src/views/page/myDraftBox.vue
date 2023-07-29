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
            <div
              @click="toPublish(item.id)"
              class="card pt-4"
              :style="item.readStatus ? borderStatus : ''"
            >
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

                <div
                  v-if="item.attachmentCount"
                  class="article-report-text mt-2 mr-3 ml-0 pa-0"
                >
                  <span>附件({{ item.attachmentCount }})</span>
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
                        mdi-trash-can-outline
                      </v-icon>
                      删除
                    </button>
                  </template>
                  <v-card>
                    <v-card-title justify-center>确认要删除吗</v-card-title>
                    <v-card-text
                      >草稿删除后无法恢复，确认删除吗？</v-card-text
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
                        @click="onDelete(item, i)"
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
import "@/assets/css/article.css";
import { aesEncrypt } from "@/utils/encryption.js";
import { getArticleByStatusCategory , deleteMessage} from "@/api/getData.js";
import { highLightText, formatHtml } from "@/utils/articleUtils.js";
import { mapGetters } from "vuex";
import { ThrottleItem } from "@/utils/tools.js";
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
      this.data.msgStatus = 0;
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
            //部门
            dTemp["deptment"] = item.creatorCompanyname;
            //更新时间
            dTemp["lastUpdateTime"] = item.lastUpdateTime;
            //附件数量
            dTemp["attachmentCount"] = item.attachmentCount;
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

    //跳转另存为发布文章
    toPublish(id) {
      this.$router.push({
        path: "/publishArticle",
        query: { id: aesEncrypt(id+"") },
      });
    },
    onDelete: ThrottleItem(function (item, index) {
      this.items[index].dlog2 = false;
      deleteMessage(item.id)
        .then((res) => {
          if ((!res && typeof res != "undefined") || res.code != 0) return;
          var i = this.items.indexOf(item);
          if (i >= 0) {
            this.items.splice(i, 1);
          }
        })
        .catch();
    }),

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
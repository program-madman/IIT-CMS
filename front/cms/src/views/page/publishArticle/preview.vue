<template>
  <v-row justify="center">
    <v-dialog
      :persistent="true"
      v-model="dialog"
      v-bind="$attrs"
      v-on="$listeners"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="close">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>预览文章</v-toolbar-title>
        </v-toolbar>

        <v-card class="pa-6" elevation="0">
          <div style="border: 1px solid #e4e8eb" class="pa-6 ma-0">
            <v-row align="center" class="article-title ma-0">
              <span>
                {{ title }}
              </span>
            </v-row>
            

            <v-row class="mt-2 ma-0 aritcle-desc">
              <span>
                {{ user.operateDepartment }}
              </span>
              <span class="ml-6">{{ createTime }}</span>
            </v-row>

            <v-row class="mt-4 ma-0" align="center">
              <span class="aritcle-desc"> 本文发送给: </span>
              <div class="chip-wrapper ml-2">
                <span class="chip-text">XX主管</span>
              </div>
              <div class="chip-wrapper ml-2">
                <span class="chip-text">XX经理</span>
              </div>
            </v-row>
            <v-row class="ma-0 mt-4">
              <span class="html-content" v-html="content"> </span>
            </v-row>
            <!-- 投票显示区 -->
            <vote
              :vote="this.vote"
              :isPreview="true"
              v-if="this.vote.enable"
              class="mt-4"
            />
        
            <div class="mt-4">
              <div v-if="uploadFileNormal.length > 0">
                <v-row class="ma-0 pa-0">
                  <span class="files-category mr-4"> 普通附件 </span>
                  <span class="files-category-btn">全部下载</span>
                </v-row>
                <v-row no-gutters class="pa-0 ma-0 mt-2">
                  <file-item
                    v-for="(item, hindex) in uploadFileNormal"
                    :key="hindex"
                    :isPreview="true"
                    class="pa-0 mr-4 file-item"
                    :apFile="item"
                  ></file-item>
                </v-row>
              </div>
              <div v-if="uploadFileDirect.length > 0">
                <v-row class="ma-0 mt-4 pa-0" align="center">
                  <span class="files-category mr-4">定向附件</span>
                  <span class="files-category-btn">全部下载</span>
                </v-row>
                <v-row no-gutters class="pa-0 ma-0 mt-2">
                  <file-item
                    v-for="(item, hindex) in uploadFileDirect"
                    :key="hindex"
                    :isPreview="true"
                    class="pa-0 mr-4 file-item"
                    :apFile="item"
                  ></file-item>
                </v-row>
              </div>
            </div>
           
            <v-row class="ma-0 mt-4" align="center">
              <span class="aritcle-desc">
                阅读：0/0
              </span>
              <span class="aritcle-desc ml-6">
                回复：0/0
              </span>
            </v-row>
          </div>
        </v-card>
        <v-divider></v-divider>
      </v-card>

      <div style="height: 62px; background: #ffffff"></div>
      <v-footer fixed padless style="height: 64px" class="footer">
        <v-spacer></v-spacer>
        <v-btn
          class="mr-6"
          min-width="105px"
          outlined
          depressed
          dark
          color="#1867C0FF"
          @click="close"
        >
          返回编辑文章
        </v-btn>
      </v-footer>
    </v-dialog>
  </v-row>
</template>
<script>
import "@/utils/timeFormate.js";
export default {
  props: {
    open: {
      type: Boolean,
      default: false,
    },

    uploadFileDirect: {
      type:[Object,Array,String] ,
      default: "",
    },
    uploadFileNormal: {
      type: [Object,Array,String],
      default: "",
    },
    vote:{
      type: [String, Object],
      default: "",
    },
    title: {
      type: String,
      default: "",
    },
    content: {
      type: String,
      default: "",
    },
  },
  components: {
    vote: () => import("./vote.vue"),
    fileItem: () => import("./fileOption.vue"),
  },
  data() {
    return {
      dialog: this.open,

      createTime: new Date().format("yyyy-MM-dd hh:mm:ss"),
      user: {
        operateDepartment: "集团物流部",
      },
    };
  },
  methods: {
    close() {
      //this.dialog = false;
      this.$emit("closeDialog");
    },

    getRandom(min, max) {
      //根据最小值和最大值产生一个随机数
      return Math.floor(Math.random() * (max - min) + min);
    },
  },
  watch: {
    open() {
      this.dialog = this.open;
    },
  },
};
</script>
<style>
.article-title {
  color: #212223ff;
  font-family: PingFang SC;
  font-weight: medium;
  font-size: 20px;
  line-height: 30px;
  letter-spacing: 0px;
  text-align: left;
}
.article-title .v-icon.v-icon {
  font-size: 16px;
  height: 24px;
  width: 24px;
  color: #212223ff !important;
}
.aritcle-desc {
  opacity: 0.800000011920929;
  color: #909191ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: 20px;
  letter-spacing: 0px;
  text-align: left;
}
.chip-wrapper {
  border-radius: 10px;
  background: #f0f0f0ff;
  height: 20px;
  padding-left: 8px;
  padding-right: 8px;
}
.chip-text {
  opacity: 0.800000011920929;
  color: #212223ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: 20px;
  letter-spacing: 0px;
  text-align: left;
  display: block;
}

.files-category {
  opacity: 0.800000011920929;
  color: #333333ff;
  font-family: PingFang SC;
  font-weight: semibold;
  font-size: 14px;
  line-height: 20px;
  letter-spacing: 0px;
  text-align: left;
}
.files-category-btn {
  color: #1867c0ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: 18px;
  letter-spacing: 0px;
  text-align: left;
}

.file-item {
  margin-bottom: 1px;
}

.html-content p {
  margin-bottom: 0px;
}
.footer {
  background: #f5f6f7ff;
  height: 64px;
  bottom: 0;
  right: 0;
  left: 0;
}
</style>


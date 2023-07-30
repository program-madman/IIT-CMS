<template>
  <v-container fluid class="pa-0 publish_content">
    <!-- 文章标题 -->
    <title-editor v-model="title" :error-messages="titleErrorMessage" />

    <!-- 正文编辑器 -->
    <app-editor ref="editor" v-model="content" />

    <div class="append-content">
      <div class="appendix mt-4 ml-5 mr-5" height="18px">
        <v-btn
          plain
          text
          color="#1867c0ff"
          :ripple="false"
          height="18px"
          class="pa-0 ma-0"
          @click="uploadFileN"
        >
          <v-icon height="16px" width="16px" left> mdi-paperclip </v-icon>
          Add Attachments
          <input
            ref="uploadFileN"
            type="file"
            @change="fileUpload($event, false)"
            @click="resetUploaderN"
            multiple="multiple"
            :accept="inputFileAccept"
            hidden
          />
        </v-btn>
        <div class="ml-4 mr-4 divider" vertical></div>
      </div>
      <!-- 文件附件部分 开始 -->
      <div class="ml-5 mt-4 mr-5">
        <div v-if="uploadFileNormal.length > 0">
          <v-row class="ma-0 pa-0">
            <span class="files-category mr-6">
              Attachments({{ uploadFileNormal.length }})
            </span>
            <v-btn
              depressed
              plain
              text
              color="#1867c0ff"
              :ripple="false"
              height="20px"
              min-width="24px"
              class="pa-0 ma-0"
              @click="removeAllUploadFileNormal"
            >
              Delete All
            </v-btn>
          </v-row>
          <v-row no-gutters class="pa-0 ma-0 mt-2">
            <file-item
              v-for="(item, hindex) in uploadFileNormal"
              :key="hindex"
              @onDelete="onDeleteFile(hindex, false)"
              class="pa-0 mr-4 file-item"
              :apFile="item"
            ></file-item>
          </v-row>
        </div>
      </div>
    </div>

    <div style="height: 128px; display: block"></div>

    <!-- 底部按钮 -->
    <v-footer padless fixed height="64">
      <v-spacer></v-spacer>
      <v-btn
        class="mr-6"
        width="105px"
        height="36px"
        outlined
        depressed
        dark
        color="#1867C0FF"
        @click="
          openPreview();
          closeSnack();
        "
      >
        Preview
      </v-btn>
      <v-btn
        class="mr-8"
        min-width="105px"
        depressed
        dark
        color="blue"
        @click="
          publishArticle();
          closeSnack();
        "
      >
        Publish
      </v-btn>
    </v-footer>
    <preview
      :open="preview && $route.path == '/publishArticle'"
      @closeDialog="preview = false"
      :uploadFileDirect="uploadFileDirect"
      :uploadFileNormal="uploadFileNormal"
      :vote="vote"
      :title="title"
      :content="content"
    />
    <publish
      :open="publish && $route.path == '/publishArticle'"
      :id="id"
      :voteEnable="vote.enable"
      :dealer="delearList"
      @closeDialog="publish = false"
    />
    <confirm ref="confirm" />
  </v-container>
</template>
<script>
import {
  saveMessage,
  getArticleDetail,
  uploadFile,
} from "@/api/getData.js";
import { aesEncrypt, aesDecrypt } from "@/utils/encryption.js";
import { highLightAllText } from "@/utils/articleUtils.js";
import { Debounce, Throttle, deepClone } from "@/utils/tools.js";
import { showSnackbar, closeSnackbar } from "@/utils/snackbar.js";
import { isNull } from "@/utils/stringUtils.js";
import { offsetMonthTime } from "@/utils/timeFormate.js";
import { EventBus } from "@/utils/event-bus";
import axios from 'axios';
import {
  checkUploadFileType,
  INPUT_FILE_ACCEPT,
  readFileSizeUnit,
  getDistributorNumberByFileName,
} from "@/utils/filesUtils.js";
const voteDefault = {
  id: null,
  enable: false,
  topic: "",
  voteType: false,
  voteOptions: [{}, {}],
  endTime: offsetMonthTime(1).format("yyyy-MM-dd hh:mm:ss"),
};
export default {
  data() {
    return {
      id: null,
      title: "",
      content: "",
      titleErrorMessage: "",
      inputFileAccept:INPUT_FILE_ACCEPT,  
      maxTime: offsetMonthTime(1),
      vote: deepClone(voteDefault),
      voteTemp: deepClone(voteDefault),
      voteDialog: false,
      uploadFileNormal: [],
      uploadFileDirect: [],
      delearList: [],
      preview: false,
      publish: false,
      needSave: false,
      autoSaving: false,
      //普通附件上传句柄集合
      normalReqeuestHandles:[],
      //定向附件上传句柄集合
      directionReqeuestHandles:[],
      
      cancelToken : axios.CancelToken,
    };
  },
  components: {
    appEditor: () => import("./editor.vue"),
    titleEditor: () => import("./title.vue"),
    
    
    fileItem: () => import("./fileOption.vue"),
    
    preview: () => import("./preview.vue"),
    publish: () => import("./publish.vue"),
    confirm: () => import("@/components/base/conform.vue"),
  },
  // 路由更新处理
  beforeRouteUpdate(to, from, next) {    
    if (this.needSave) {
      this.$refs.confirm
        .confirm(
          "您当前有修改的信息未保存，离开当前页面您输入的信息将不会被保存。"
        )
        .then(() => {
          next(true);
          this.reEditIfNeed(to.query.id);
        })
        .catch(() => next(false));
    } else {
      next(true);

      if (this.reEditIfNeed(to.query.id)) return;
      this.saveAsNewMessage(to.params);
    }
  },

  //路由离开处理
  beforeRouteLeave(to, from, next) {
    this.closeSnack();
    if (this.needSave) {
      this.$refs.confirm
        .confirm(
          "您当前有修改的信息未保存，离开当前页面您输入的信息将不会被保存。"
        )
        .then(() => {          
          next(true);
        })
        .catch(() => next(false));
    } else {
      next(true);
    }
  },

  mounted() {
    if (!this.reEditIfNeed(this.$route.query.id)) {
      this.saveAsNewMessage(this.$route.params);
    }
    let _that = this;
    console.log("mounted:register wantClosePublicarticle");

    EventBus.$off('wantClosePublicarticle')
    EventBus.$on("wantClosePublicarticle", () => {
        console.log("needSave:"+_that.needSave);
       if (_that.needSave) {
            _that.$refs.confirm
             .confirm(
               "您当前有修改的信息未保存，离开当前页面您输入的信息将不会被保存。"
             )
             .then(() => {          
               _that.needSave = false;
               EventBus.$emit("closeTab","publishArticle");
             }).catch(()=>{
               console.log("cancel");
             });
        } else {
            EventBus.$emit("closeTab","publishArticle");
        }
    });

    window.addEventListener("beforeunload", this.beforeunloadHandler);
  },
  destroyed() {
    window.removeEventListener("beforeunload", this.beforeunloadHandler);
  },

  methods: {
    //全部删除所有普通附件
    removeAllUploadFileNormal(){
      this.uploadFileNormal = []

      this.normalReqeuestHandles.forEach((source)=>{
          source.cancel("canclefile");
      });      
      this.normalReqeuestHandles = [];
    },
    //移除所有定向附件
    removeAllDirectionFileNormal(){
      this.uploadFileDirect = []

      this.directionReqeuestHandles.forEach((source)=>{
          source.cancel("canclefile");
      });      
      this.directionReqeuestHandles = [];
    },

    beforeunloadHandler(e) {
      if (e) {
        e.returnValue = "关闭提示";
      }
      return "关闭提示";
    },
    resetUploaderN() {
      this.$refs.uploadFileN.value = "";
    },

    resetUploaderD() {
      this.$refs.uploadFileD.value = "";
    },
    //另存为创建新消息
    saveAsNewMessage(params) {
      console.debug("public======>" + params);
      if (params == null || typeof params == "undefined") return;
      let id = params.id;
      if (!id || typeof id == "undefined" || isNaN(id)) return;

      this.id = null;
      this.restoreMessage(id, true);
    },
    //草稿重新编辑
    reEditIfNeed(id) {
      if (!id || typeof id == "undefined") return false;

      id = Number(aesDecrypt(id));
      if (id == null || isNaN(id) || this.id == id) return false;
      this.id = id;
      this.restoreMessage(id);
      return true;
    },
    //重设消息内容
    restoreMessage(id, isNew = false) {
      this.clearContent();
      //请求后台获取消息内容
      getArticleDetail(id)
        .then((res) => {
          if (res == null || typeof res == undefined) return;
          let data = res.data;
          this.title = data.title;
          this.content = data.content;
          if (
            data.voteInfoDTO != null &&
            typeof data.voteInfoDTO != undefined &&
            data.voteInfoDTO.options.length >= 2
          ) {
            let vote = {
              id: isNew ? null : data.voteInfoDTO.voteId,
              enable: true,
              topic: data.voteInfoDTO.topic,
              voteOptions: data.voteInfoDTO.options.map((item) => {
                return { value: item.optionName };
              }),
              voteType: false,
              endTime: offsetMonthTime(1).format("yyyy-MM-dd hh:mm:ss"),
            };
            this.voteTemp = deepClone(vote);

            if (isNew) {
              this.voteSave(true);
              return;
            } else {
              this.vote = deepClone(vote);
            }
          }

          if (
            !data.attachments ||
            typeof data.attachments == "undefined" ||
            data.attachments.length > 0
          ) {
            console.debug("attachments:" + JSON.stringify(data.attachments));
            data.attachments.forEach((item) => {
              let fileName = item.attachmentName;
              let temp = {
                res: true,
                text: fileName,
                fileType: checkUploadFileType(fileName),
                path: item.url,
                progress: 100,
                orientation: item.orientation,
                uploadSize: item.attachmentSize,
                fileSize: readFileSizeUnit(item.attachmentSize),
              };
              if (item.orientation == null) {
                this.uploadFileNormal.push(temp);
              } else {
                this.uploadFileDirect.push(temp);
              }
            });
          }
        })
        .catch();
    },
    //清空消息内容
    clearContent() {
      this.title = "";
      this.content = "";
      this.vote = deepClone(voteDefault);
      this.voteTemp = deepClone(voteDefault);
      this.uploadFileNormal = [];
      this.uploadFileDirect = [];
    },
    // *-------------------投票操作---------------------------*/
    //取消保存
    cancleVoteEdit() {
      this.voteDialog = false;
      this.voteTemp = deepClone(this.vote);
    },

    /*-------------------附件上传操作---------------------------*/
    uploadFileN() {
      //this.$refs.uploadFileN.dispatchEvent(new MouseEvent("click"));
      this.$refs.uploadFileN.click();
    },
    uploadFileD() {
      this.$refs.uploadFileD.click();
    },

    onDeleteFile(index, isDirectFile) {
      if (isDirectFile) {
        this.uploadFileDirect.splice(index, 1);
        this.directionReqeuestHandles[index].cancel("canclefile:"+index);
        this.directionReqeuestHandles.splice(index,1);
      } else {
        console.log("remove index:"+index);
        this.uploadFileNormal.splice(index, 1);
        this.normalReqeuestHandles[index].cancel("canclefile:"+index);
        this.normalReqeuestHandles.splice(index, 1);
      }
    },

    fileUpload(event, direct) {
      let upFiles = direct ? this.uploadFileDirect : this.uploadFileNormal;
      let file = event.target.files;

      if (!direct && file.length + this.uploadFileNormal.length > 10) {
        this.showSnack("每次最多只能上传10个文件");
        return;
      }
      for (let i = 0; i < file.length; i++) {
        let item = file[i];
        item.direct = direct;
        console.log(file[i]);
        if (item.size / 1024 / 1024 >= 100) {
          this.showSnack("您有文件上传失败,附件最大限制100M");
          continue;
        }
        //上传文件去重
        if (upFiles.findIndex((t) => t.text == item.name) >= 0) {
          continue;
        }

        //上传类型判断
        let ext = checkUploadFileType(item.name);
        if (ext == null) {
          this.showSnack("您上传的文件格式不支持");
          continue;
        }

        let temp = {};
        temp.text = item.name;
        temp.fileType = ext;
        temp.res = false;
        temp.fileSize = readFileSizeUnit(item.size);
        temp.uploadSize = item.size;
        if (direct) {
          temp.orientation = getDistributorNumberByFileName(item.name);
          console.log("========>" + temp.orientation);
          if (
            temp.orientation == null ||
            typeof temp.orientation == "undefined" ||
            temp.orientation == ""
          ) {
            temp.error = "文件名不符合要求";
            upFiles.push(temp);
            continue;
          }
        }
        upFiles.push(temp);

        if (direct) {
          this.uploadDirectImp(item, temp);
        } else {
          this.uploadNormalImp(item);
        }
      }
    },
  
    //上传普通附件
    uploadNormalImp(item) {
      const source = this.cancelToken.source();

      this.normalReqeuestHandles.push(source);

      uploadFile(item,source.token, (progress, fileName) => {
        var index = this.uploadFileNormal.findIndex((t) => t.text == fileName);
        if (index < 0) return;
        this.uploadFileNormal[index].progress = progress;

        this.uploadFileNormal = JSON.parse(
          JSON.stringify(this.uploadFileNormal)
        );

        console.log("uploadFileNormal:"+JSON.stringify(this.uploadFileNormal));
      })
        .then((res) => {
          let index = this.uploadFileNormal.findIndex(
            (t) => t.text == item.name
          );
          let update = this.uploadFileNormal[index];
          if (!res || typeof res == "undefined" || res.code != 0) {
            update[index].error = "上传失败";
          } else {
            update.path = res.data.url;
            update.res = true;
          }
        })
        .catch(()=>{
            let index = this.uploadFileNormal.findIndex(
              (t) => t.text == item.name
            );
            if(index>=0){
                this.uploadFileNormal[index].error = "上传失败";
            }
        })
        .finally(
          () =>
            (this.uploadFileNormal = JSON.parse(
              JSON.stringify(this.uploadFileNormal)
            ))
        );
    },
    
    /*-------------------保存草稿操作---------------------------*/

    saveArticle: Throttle(
      function () {
        this.saveArticleImp();
      },
      function () {
        this.showSnack("您提交的太频繁了，稍等一会试试吧");
      }
    ),
    autoSave: Debounce(function () {
      if (this.needSave) {
        this.autoSaving = true;
        this.saveArticleImp();
      }
    }, 10000),

    saveArticleImp() {
      if (!this.precondition()) {
        this.autoSaving = false;
        return;
      }
      let attachmentLists = [];
      this.uploadFileNormal.forEach((item) => {
        let temp = {};
        temp.path = item.path;
        temp.attachmentSize = item.uploadSize;
        attachmentLists.push(temp);
      });
      this.uploadFileDirect.forEach((item) => {
        let temp = {};
        temp.path = item.path;
        temp.orientation = item.orientation;
        temp.attachmentSize = item.uploadSize;
        attachmentLists.push(temp);
      });
      let data = {
        id: this.id,
        title: this.title,
        content: this.content,
        voteId: this.vote.id,
        attachmentList: attachmentLists,
      };

      saveMessage(data)
        .then((res) => {
          console.log(res);
          if (res.code == 0) {
            this.needSave = false;
            this.showSnack(this.autoSaving ? "自动保存成功" : "保存成功");

            if (this.id != res.data) {
              this.id = res.data;

              this.$router.replace({
                query: { id: aesEncrypt(this.id + "") },
              });
            }
          } else {
            if (!this.autoSaving) this.showSnack("保存失败");
          }
        })
        .catch((error) => {
          if (error.response == null || typeof error.response == "undefined") {
            if (!this.autoSaving) this.showSnack("保存失败");
            return;
          }
          let res = error.response;
          if (res.data == null || typeof res.data == "undefined") {
            if (!this.autoSaving) this.showSnack("保存失败");
            return;
          }
          let data = res.data;
          if (
            data.code == null ||
            typeof data.code == "undefined" ||
            data.code != 4
          ) {
            if (!this.autoSaving) this.showSnack("保存失败");
            return;
          }

          let sensitiveWords = data.data;
          if (
            sensitiveWords == null ||
            typeof sensitiveWords == "undefined" ||
            sensitiveWords.length == 0
          ) {
            if (!this.autoSaving) this.showSnack("保存失败");
            return;
          } else {
            if (this.autoSaving) return;
            this.showSnack("您提交的内容中有敏感词，请进行修改后重新提交");
            console.debug("error" + JSON.stringify(sensitiveWords));
            let titleError = [];
            sensitiveWords.forEach((word) => {
              if (this.title.indexOf(word) >= 0) {
                titleError.push(word);
              }
              this.content = highLightAllText(this.content, word);
            });
            if (titleError.length > 0) {
              this.titleErrorMessage = "包含敏感词:";
              titleError.forEach((word) => {
                this.titleErrorMessage += word + " ";
              });
            }
          }
        })
        .finally(() => (this.autoSaving = false));
    },
    caculateDelearList() {
      this.delearList = [];
      console.log(JSON.stringify(this.uploadFileDirect));
      this.uploadFileDirect.forEach((item) => {
        if (
          item.res &&
          (item.error == null || typeof item.error == "undefined")
        ) {
          this.delearList.push(item.orientation);
        }
      });
      console.log(this.delearList);
    },
    openPreview() {
      this.preview = this.precondition();
    },

    publishArticle() {
      this.publish = this.precondition();
    },

    precondition() {
      let error = null;
      if (isNull(this.title)) {
        if (!this.autoSaving) {
          error = this.titleErrorMessage = "title cannot empty";
          this.showSnack(error);
        }

        return false;
      }
      
      if (isNull(this.content)) {
        if (!this.autoSaving) this.showSnack("content cannot empty");
        return false;
      } else if (this.sumLetter().num > 5000) {
        this.showSnack("正文内容不能超过5000字");
        return false;
      }

      this.uploadFileDirect.findIndex((item) => {
        if (item.error != null) {
          error = item.error;
          return true;
        }
        if (item.res == false) {
          error = "请等待文件上传完成";
          return true;
        }
      });
      if (error != null && !this.autoSaving) {
        this.showSnack(error);
        return false;
      }
      this.uploadFileNormal.forEach((item) => {
        if (item.error != null) {
          error = item.error;
          return true;
        }
        if (item.res == false) {
          error = "请等待文件上传完成";
          return true;
        }
      });
      if (error != null && !this.autoSaving) {
        this.showSnack(error);
        return false;
      }
      return true;
    },
    sumLetter() {
      var html = this.content;
      var re1 = new RegExp("<.+?>", "g");
      var txt = html.replace(re1, "");
      txt = txt.replace(/\n/g, "");
      txt = txt.replace(/&nbsp;/g, " ");
      var num = txt.length;
      return { txt: txt, num: num };
    },
    showSnack(message) {
      if (
        this.preview ||
        this.publish ||
        this.$route.path != "/publishArticle"
      ) {
        return;
      }
      showSnackbar(message);
    },
    closeSnack() {
      closeSnackbar();
    },

    changeSaveStatus() {
      this.needSave = true;
      this.autoSave();
    },
  },
  watch: {
    content() {
      this.changeSaveStatus();
    },
    title() {
      this.titleErrorMessage = "";
      this.changeSaveStatus();
    },
    uploadFileDirect() {
      this.caculateDelearList();
      this.changeSaveStatus();
    },
    uploadFileNormal() {
      this.changeSaveStatus();
    },
  },
};
</script>
<style>
.publish_content {
  display: block;
  overflow: scroll;
}
.append-content .v-btn {
  color: #1867c0ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: 18px;
  letter-spacing: 0px;
  text-align: left;
  vertical-align: top;
}

.append-content
  .v-btn--plain:not(.v-btn--active):not(.v-btn--loading):not(:focus):not(:hover)
  .v-btn__content {
  opacity: 1;
}

.appendix .v-icon {
  margin-left: 0px !important;
  margin-right: 4px !important;
}

.appendix .divider {
  display: inline-block;
  width: 1px;
  height: 12px;
  background: #e4e8ebff;
  margin-bottom: 3px;
}

.appendix-hint {
  display: inline-block;
  color: #999999ff;
  height: 18px;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
  margin-left: 8px;
  vertical-align: top;
}

/* vote dialog */
.vote-option .v-btn {
  color: #1867c0ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 14px;
  line-height: 18px;
  letter-spacing: 0px;
  text-align: left;
  vertical-align: top;
}

.vote-option
  .v-btn--plain:not(.v-btn--active):not(.v-btn--loading):not(:focus):not(:hover)
  .v-btn__content {
  opacity: 1;
}

.vote-checkbox-label {
  color: #333333ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 14px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
  margin-right: 8px;
}

.vote-checkbox {
  display: inline-flex;
  font-size: 14px;
  margin: 0 !important;
  padding: 0 !important;
  margin-top: 3px !important;
}

.vote-checkbox .v-input__slot {
  margin-bottom: 0 !important;
  padding-right: 0;
}
.vote-checkbox .v-input__control {
  height: 14px !important;
}

.vote-checkbox .v-input--selection-controls__input {
  padding: 0;
  margin: 0;
  height: 14px !important;
  width: 14px !important;
}
/* vote dialog */

.files-category {
  opacity: 0.800000011920929;
  color: #333333ff;
  font-family: PingFang SC;
  font-weight: semibold;
  font-size: 14px;
  line-height: 20px;
  letter-spacing: 0px;
}

.file-item {
  margin-bottom: 1px;
}

.time_input .v-input__slot {
  padding-right: 0px !important;
}
</style>

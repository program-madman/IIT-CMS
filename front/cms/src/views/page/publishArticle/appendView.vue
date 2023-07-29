<template>
  <div class="append-content">
    <!-- 添加附件按钮栏 开始 -->
    <div class="appendix mt-4 ml-5 mr-5" height="18px">
      <v-btn
        plain
        text
        :disabled="vote.enable"
        :ripple="false"
        @click="dialog = !dialog"
        height="18px"
        class="pa-0 ma-0"
      >
        <v-icon height="16px" width="16px" left> mdi-vote-outline </v-icon>
        添加投票
      </v-btn>

      <div class="ml-4 mr-4 divider" vertical></div>
      <v-btn
        plain
        text
        :ripple="false"
        height="18px"
        class="pa-0 ma-0"
        @click="uploadFileN"
      >
        <v-icon height="16px" width="16px" left> mdi-paperclip </v-icon>
        添加附件
        <input
          ref="uploadFileN"
          type="file"
          @click="resetUploaderN"
          @change="fileUpload($event, false)"
          multiple="multiple"
          accept=".docx,.doc,.pdf,.jpg,.xls,.xlsx,.txt"
          hidden
        />
      </v-btn>
      <div class="ml-4 mr-4 divider" vertical></div>
      <v-btn
        plain
        text
        :ripple="false"
        height="18px"
        class="pa-0 ma-0"
        @click="uploadFileD"
      >
        <v-icon height="16px" width="16px" left> mdi-paperclip </v-icon>
        添加定向附件
        <input
          ref="uploadFileD"
          type="file"
          @click="resetUploaderD"
          @change="fileUpload($event, true)"
          multiple="multiple"
          accept=".docx,.doc,.pdf,.jpg,.xls,.xlsx,.txt"
          hidden
        />
      </v-btn>

      <span class="appendix-hint"
        >定向附件文件名格式如下：[70101010]******************，中括号中为发送经销商代码</span
      >
    </div>
    <!-- 添加附件按钮栏 结束 -->
    <!-- 添加投票dialog 开始 -->
    <v-dialog width="546" v-model="dialog" :disabled="vote.enable">
      <v-card>
        <v-card-title>
          <span>添加投票</span>
        </v-card-title>
        <v-form ref="form" lazy-validation>
          <vote-option
            placeholder="请输入投票主题"
            errorMessage="投票主题不能为空"
            class="ml-7 mr-7"
            v-model="voteTemp.topic"
          />

          <div class="mt-6">
            <vote-option
              v-for="(item, hindex) in voteTemp.voteOptions"
              :key="hindex"
              placeholder="请输入投票内容"
              :errorMessage="hindex > 1 ? null : '投票内容不能为空'"
              v-model="item.value"
              :enableDelete="hindex > 1"
              @onDelete="deleteItem(hindex)"
            />
          </div>
          <div class="vote-option">
            <v-btn
              plain
              text
              :ripple="false"
              height="18px"
              class="pa-0 ma-0 ml-8"
              @click="addItem"
            >
              <v-icon height="16px" width="16px" left>
                mdi-plus-circle-outline
              </v-icon>
              添加投票选项
            </v-btn>
          </div>

          <time-picker v-model="timeSelect" :minTime="new Date()">
            <v-text-field
              placeholder="投票结束时间"
              hide-details="auto"
              single-line
              maxlength="40"
              v-model="voteTemp.endTime"
              :rules="[(value) => !!value || '投票结束时间不能为空']"
              required
              outlined
              append-icon="mdi-menu-down"
              class="title time_input ml-7 mt-5"
              style="display: inline-flex; width: 455px"
              readonly
            />
            <span
              style="height: 16px; width: 16px; color: red; margin-left: 13px"
            >
              *
            </span>
          </time-picker>
          <!-- <div class="mt-6 ml-7" style="display: flex">
            <span class="vote-checkbox-label">匿名投票</span>
            <v-checkbox
              class="vote-checkbox"
              :ripple="false"
              v-model="voteTemp.voteType"
            ></v-checkbox>
          </div> -->
          <div style="margin-top: 24px; height: 60px">
            <v-btn
              min-width="105px"
              height="36px"
              depressed
              dark
              outlined
              color="#1867C0FF"
              style="margin-left: 157px; margin-right: 24px"
              @click="dialogCancle"
            >
              取消
            </v-btn>
            <v-btn
              min-width="105px"
              height="36px"
              depressed
              dark
              color="blue"
              @click="dialogOk"
            >
              确认
            </v-btn>
          </div>
        </v-form>
      </v-card>
    </v-dialog>
    <!-- 添加投票dialog 结束 -->
    <!-- 投票显示区域 -->
    <vote
      :vote="vote"
      @onDelete="onVoteDelete"
      @onUpdate="dialog = true"
      v-if="vote.enable"
      class="ml-5 mt-4"
    />
    <!-- 文件附件部分 开始 -->
    <div class="ml-5 mt-4 mr-5">
      <div v-if="uploadFileNormal.length > 0">
        <v-row class="ma-0 pa-0">
          <span class="files-category mr-6"> 普通附件 </span>
          <v-btn
            depressed
            plain
            text
            :ripple="false"
            height="20px"
            min-width="24px"
            class="pa-0 ma-0"
            @click="uploadFileNormal = []"
          >
            全部删除
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
      <div v-if="uploadFileDirect.length > 0">
        <v-row class="ma-0 mt-4 pa-0" align="center">
          <span class="files-category mr-6">定向附件</span>
          <v-btn
            depressed
            plain
            text
            :ripple="false"
            height="20px"
            min-width="24px"
            class="pa-0 ma-0"
            @click="uploadFileDirect = []"
          >
            全部删除
          </v-btn>
        </v-row>
        <v-row no-gutters class="pa-0 ma-0 mt-2">
          <file-item
            v-for="(item, hindex) in uploadFileDirect"
            :key="hindex"
            @onDelete="onDeleteFile(hindex, true)"
            class="pa-0 mr-4 file-item"
            :apFile="item"
          ></file-item>
        </v-row>
      </div>
    </div>
    <!-- 文件附件部分 结束 -->
  </div>
</template>

<script>
import { saveVote, uploadFile } from "@/api/getData.js";
import { offsetMonthTime } from "@/utils/timeFormate.js";
import {
  getUploadRealFileNameByPath,
  checkUploadFileType,
  readFileSizeUnit,
  getDistributorNumberByFileName,
} from "@/utils/filesUtils.js";
export default {
  model: {
    prop: "content",
    event: "change",
  },
  props: {
    content: {
      type: [String, Object],
      default: "",
    },
  },
  data() {
    return {
      maxTime: offsetMonthTime(1),
      vote: {},
      timeSelect: "",
      voteTemp: {
        id: null,
        enable: false,
        topic: "",
        voteType: false,
        voteOptions: [{}, {}],
        endTime: offsetMonthTime(1).format("yyyy-MM-dd hh:mm:ss"),
      },
      dialog: false,
      uploadFileNormal: this.content.uploadFileNormal,
      uploadFileDirect: this.content.uploadFileDirect,
      distributorNumber: ["70101010", "70101011"],
    };
  },
  components: {
    vote: () => import("./vote"),
    voteOption: () => import("./voteOption.vue"),
    fileItem: () => import("./fileOption.vue"),
    timePicker: () => import("./timePicker.vue"),
  },

  methods: {
    //取消投票修改
    dialogCancle() {
      this.dialog = false;
    },
    //确认保存投票
    dialogOk: function () {
      if (!this.$refs.form.validate()) return;

      let temp = this.voteTemp;
      temp.enable = true;
      temp.voteOptions = temp.voteOptions.filter(
        (t) => t.value != null && t.value != ""
      );

      let upload = {};
      upload.id = temp.id;
      upload.topic = temp.topic;
      upload.voteType = temp.voteType ? 1 : 0;
      upload.voteOptions = temp.voteOptions.map((item) => item.value);
      upload.endTime = temp.endTime;

      saveVote(upload)
        .then((res) => {
          console.debug("save vote ==>" + JSON.stringify(res));
          if (res.code == 0) {
            this.dialog = false;
            console.debug("save id" + res.data.voteId);
            temp.id = res.data.voteId;
            this.vote = this.voteTemp = JSON.parse(JSON.stringify(temp));
            this.onChange();
          }
        })
        .catch(() => {});
    },
    //添加投票选项
    addItem() {
      this.voteTemp.voteOptions.push({ value: null });
    },
    //删除投票选项
    deleteItem: function (index) {
      //  not allow to delete the first
      if (index > 1) {
        this.voteTemp.voteOptions.splice(index, 1);
      }
    },
    onVoteDelete() {
      this.vote.enable = false;
    },
    uploadFileN() {
      this.$refs.uploadFileN.dispatchEvent(new MouseEvent("click"));
    },
    uploadFileD() {
      this.$refs.uploadFileD.dispatchEvent(new MouseEvent("click"));
    },

    onDeleteFile(index, isDirectFile) {
      console.log("ondelete1");
      if (isDirectFile) {
        this.uploadFileDirect.splice(index, 1);
      } else {
        this.uploadFileNormal.splice(index, 1);
      }
    },

    resetUploaderN(){
        this.$refs.uploadFileN.value="";
    },

    resetUploaderD(){
        this.$refs.uploadFileD.value="";
    },

    fileUpload(event, direct) {
      let upFiles = direct ? this.uploadFileDirect : this.uploadFileNormal;
      let file = event.target.files;

      for (let i = 0; i < file.length; i++) {
        let item = file[i];
        item.direct = direct;

        //上传文件去重
        if (upFiles.findIndex((t) => t.text == item.name) == 0) {
          continue;
        }

        //上传类型判断
        let ext = checkUploadFileType(item.name);
        if (ext == null) continue;

        let temp = {};
        temp.text = item.name;
        temp.fileType = ext;
        temp.fileSize = readFileSizeUnit(item.size);

        //定向文件编码
        if (direct) {
          let code = getDistributorNumberByFileName(item.name);
          if (this.distributorNumber.findIndex((t) => t == code) != 0) {
            temp.error = "文件名不符合要求";
            upFiles.push(temp);
            continue;
          } else {
            temp.orientation = code;
          }
        }

        //添加上传文件列表
        upFiles.push(temp);

        //上传服务器
        uploadFile(item,null, (progress, fileName) => {
          if (item.direct) {
            this.uploadFileDirect
              .filter((t) => t.text == fileName)
              .map((update) => {
                update.progress = progress;
                //this.$set(this.uploadFileDirect, index, update);
              });
            this.uploadFileDirect = JSON.parse(
              JSON.stringify(this.uploadFileDirect)
            );
          } else {
            this.uploadFileNormal
              .filter((t) => t.text == fileName)
              .map((update) => {
                update.progress = progress;
                //this.$set(this.uploadFileNormal, index, update);
              });
            this.uploadFileNormal = JSON.parse(
              JSON.stringify(this.uploadFileNormal)
            );
          }
        })
          .then((res) => {
            console.log("upload" + JSON.stringify(res) + res);
            console.log("local");
            if (res.code == 0) {
              //更新上传信息
              let files = item.direct
                ? this.uploadFileDirect
                : this.uploadFileNormal;
              files
                .filter((t) => t.text == getUploadRealFileNameByPath(res.data))
                .map((update) => (update.path = res.data));
              console.log("upload file message:" + JSON.stringify(files));
            }
          })
          .catch(() => {});
      }
    },

    onChange() {
      let content = {};
      content.vote = this.vote;
      content.uploadFileDirect = this.uploadFileDirect;
      content.uploadFileNormal = this.uploadFileNormal;
      this.$emit("change", content);
    },
    voteEndDateAllowed(val) {
      return (
        new Date(val).getTime() >= new Date().getTime() - 24 * 60 * 60 * 1000
      );
    },
    voteEndHoursAllowed(val) {
      var cur = new Date();
      return (
        new Date(this.timeSelect).getDate() > cur.getDate() ||
        val >= cur.getHours()
      );
    },
    voteEndMinutesAllowed(val) {
      let select = new Date(this.timeSelect);
      var cur = new Date();
      return (
        select.getDate() > cur.getDate() ||
        select.getHours() > cur.getHours() ||
        val > cur.getMinutes()
      );
    },
  },
  watch: {
    content(val, oldVal) {
      if (val === oldVal) return;
      this.vote = this.content.vote;
      this.voteTemp = this.content.vote;
      this.uploadFileNormal = this.content.uploadFileNormal;
      this.uploadFileDirect = this.content.uploadFileDirect;
    },
    vote() {
      this.onChange();
    },
    uploadFileNormal() {
      this.onChange();
    },
    uploadFileDirect() {
      this.onChange();
    },
    timeSelect() {
      this.voteTemp.endTime = this.timeSelect;
    },
  },
  created() {
    if (this.content.vote != null && this.content.vote.voteOptions != null) {
      this.voteTemp = this.vote = this.content.vote;
    }
  },
};
</script>

<style>
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

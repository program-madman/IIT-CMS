<template>
  <v-progress-linear
    :value="progress"
    color="#1867C0"
    background-color="#F5F6F7FF"
    rounded
    height="32"
    class="progress-linear mt-1"
    style="width: 546px"
  >
    <v-sheet
      class="d-flex justify-start pt-1 pb-1"
      width="546"
      v-ripple
      :height="isPreview ? 32 : 30"
      color="#F5F6F7FF"
    >
      <v-icon small class="ml-4"> mdi-paperclip </v-icon>
      <!-- <v-icon class="ml-2" :color="showIconColor(apFile.fileType)">
        {{ showIcon(apFile.fileType) }}</v-icon
      > -->
      <v-img max-height="24" max-width="24" :src="showIcon(apFile.fileType)" class="ml-2"/>
      <span
        class="d-inline-block ml-2 file-name text-truncate align-self-center"
        >{{ apFile.text }}</span
      >
      <div
        class="ml-2"
        style="
          display: flex;
          align-items: center;
          justify-content: center;
          flex-direction: column;
        "
      >
        <span class="caption grey--text text--darken-1"
          >({{ apFile.fileSize }})</span
        >
      </div>
      <span class="error-message" v-if="apFile.error != null"
        ><v-icon>mdi-alert-circle-outline </v-icon>Upload Error
        {{ apFile.progressbar }}</span
      >
      <v-spacer></v-spacer>
      <div
        class="mr-4"
        style="
          display: flex;
          align-items: center;
          justify-content: center;
          flex-direction: column;
        "
      >
        <button class="caption blue--text text--darken-3" @click="onDelete">
          {{ isPreview ? "Download" : "Delete" }}
        </button>
      </div>
    </v-sheet>
  </v-progress-linear>
</template>

<script>
export default {
  model: {
    event: "change",
  },
  props: {
    apFile: {
      typeof: Object,
      default: () => {
        return {
          fileType: "",
          text: "",
          fileSize: "",
          progress: 100,
          error: null,
        };
      },
    },
    isPreview: {
      type: Boolean,
      default: false,
    },
  },

  data() {
    return {
      //压缩文件夹zip,pdf,jpg,excel,doc,默认文件
      borderStatus:
        "border-left:4px solid blue !important;border-radius: 4px 4px 4px 4px",
      progress: this.isPreview ? 0 : this.apFile.progress,
    };
  },
  watch: {
    apFile() {
      if (!this.isPreview) {
        this.progress = this.apFile.progress;
      }
    },
  },

  methods: {
    //根据类型显示对应icon
    showIcon(type) {
      switch (type) {
        case "zip":
        case "rar":
          return require("@/assets/filestyle/rar.png");
        case "pdf":
          return require("@/assets/filestyle/pdf.png");
        case "ppt":
          return require("@/assets/filestyle/ppt.png");
        case "jpg":
          return require("@/assets/filestyle/jpg.png");
        case "jpeg":
          return require("@/assets/filestyle/jpeg.png");
        case "png":
          return require("@/assets/filestyle/png.png");
        case "gif":
          return require("@/assets/filestyle/gif.png");
        case "excel":
        case "xls":
        case "xlsx":
          return require("@/assets/filestyle/xls.png");
        case "document":
        case "doc":
        case "docx":
          return require("@/assets/filestyle/doc.png");
        case "txt":
          return require("@/assets/filestyle/txt.png");
        case "avi":
          return require("@/assets/filestyle/avi.png");
        case "mp3":
          return require("@/assets/filestyle/mp3.png");
        case "mp4":
          return require("@/assets/filestyle/mp4.png");
        case "rmvb":
          return require("@/assets/filestyle/rmvb.png");
        case "html":
          return require("@/assets/filestyle/html.png");
        default:
          return require("@/assets/filestyle/other.png");
      }
    },
    onDelete() {
      this.$emit("onDelete");
    },
  },
};
</script>

<style>
.error-message {
  color: #f44336ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: 18px;
  letter-spacing: 0px;
  text-align: left;
  margin-top: 2px;
  margin-left: 16px;
}

.error-message .v-icon.v-icon {
  font-size: 14px;
  color: #f44336ff;
  margin-right: 2px;
}

.file-name {
  color: #333333ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: 18px;
  letter-spacing: 0px;
  text-align: left;
  max-width: 170px;
  height: 18px;
}
.progress-linear .v-progress-linear__content {
  display: block;
}
</style>
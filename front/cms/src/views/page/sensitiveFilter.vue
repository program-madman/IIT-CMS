<template>
  <v-card>
    <v-card-title>敏感词管理</v-card-title>
    <v-row no-gutters>
      <v-btn class="ml-4 primary" @click="addAppend">上传配置文件</v-btn>
      <p class="caption align-center ml-2 mt-2">
        *支持.txt文件，在文件中请用逗号将敏感词隔开
      </p>
      <input
        ref="uploadFile"
        type="file"
        @change="fileUpload($event)"
        @click="resetUploader"
        multiple="multiple"
        accept=".txt"
        hidden
      />
    </v-row>
    <!-- 
      <v-sheet class="d-flex justify-start ml-4 pl-3 pr-3 pt-1 pb-1" height="32" width="564" v-ripple outlined color="#F5F6F7FF"  >
        <v-icon  large class="ml-2" :color="showIconColor(fileType)"> {{ showIcon(fileType) }}</v-icon>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="400">
          
          <template v-slot:activator="{ on, attrs }">
            <v-btn text depressed  x-small v-bind="attrs" v-on="on" class="mr-6" color="primary">
              删除
            </v-btn>
          </template>
          
          <v-card>
            <v-card-title align="center">确认要删除吗?</v-card-title>
            <v-card-text>敏感词删除后将从它的所有观看者列表中消失,确认要删除吗？</v-card-text>
            <v-card-actions>
              <v-btn color="blue darken-1" text @click="dialog = false">
                取消
              </v-btn>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="deleteSensitive">
                确认
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
    </v-sheet>
    -->
    <div
    v-if="loading"
    class="ml-5"
    style="width: 560px"
    > 
      <v-progress-linear
            
            indeterminate
            color="blue"
          />
    </div>
     
    <div
      style="width: 564px; border: 1px solid #e4e8ebff; border-radius: 4px"
      class="ml-4 mt-0"
    >
      <v-virtual-scroll
        :items="replyText"
        height="622"
        item-height="20"
        bench="1"
      >
        <template v-slot:default="{ item, index }">
          <v-list-item :key="index">
            <span class="body-2 grey--text text--darken-4">{{ item }}</span>
          </v-list-item>
        </template>
      </v-virtual-scroll>
    </div>
  </v-card>
</template>

<script>
import { showSnackbar } from "@/utils/snackbar.js";
import { isNull } from "@/utils/common.js";
import { getSensitive, saveSensitive } from "@/api/getData.js";
export default {
  name: "sensitiveFilter",

  data() {
    return {
      //压缩文件夹zip,pdf,jpg,excel,doc,默认文件
      icons: [
        "mdi-folder-zip",
        "mdi-file-pdf-box",
        "mdi-file-jpg-box",
        "mdi-file-excel",
        "mdi-file-document",
        "mdi-application",
        "mdi-text-box-outline",
      ],

      //附件下载报告title
      title: "附件下载报告",
      loading:true,
      uploadSensitiveError: "提交敏感词失败，请稍再试",
      getSensitiveError: "获取敏感词失败，请稍后再试",
      //上传文件
      upFiles: [],
      replyText: [],
      fileType: "txt",
      dialog: false,
    };
  },

  activated() {
    this.getSensitiveList();
  },

  methods: {
    //根据类型显示对应icon
    //according to file type show corresponding icon
    showIcon(type) {
      let that = this;
      switch (type) {
        case "zip":
          return that.icons[0];
        case "pdf":
          return that.icons[1];
        case "jpg":
          return that.icons[2];
        case "xls":
        case "xlsx":
          return that.icons[3];
        case "doc":
        case "docx":
          return that.icons[4];
        default:
          return that.icons[6];
      }
    },

    resetUploader(){
        this.$refs.uploadFile.value="";
    },

    //show icon color
    showIconColor(type) {
      switch (type) {
        case "zip":
          return "blue-grey";
        case "pdf":
          return "red";
        case "jpg":
          return "purple lighten-1";
        case "xls":
        case "xlsx":
          return "light-green";
        case "doc":
        case "docx":
          return "teal lighten-1";
        default:
          return "";
      }
    },

    getSensitiveList() {
      this.loading = true
      let that = this;
      getSensitive({})
        .then((res) => {
          this.loading = false;
          console.log(JSON.stringify(res));
          if (
            !isNull(res) &&
            !isNull(res.data) &&
            !isNull(res.code) &&
            res.code === 0
          ) {
            this.replyText = res.data;
          } else {
            that.loading = false;
            showSnackbar("获取敏感词错误");
          }
        })
        .catch((e) => {
          that.loading = false;
          console.log(JSON.stringify(e));
          showSnackbar("获取敏感词错误");
        });
    },
    submitError(text) {
      showSnackbar(text);
    },
    //file upload
    fileUpload(event) {
      this.upFiles = [];
      let file = event.target.files;
      for (let i = 0; i < file.length; i++) {
        //上传类型判断
        let imgName = file[i].name;
        let idx = imgName.lastIndexOf(".");
        if (idx !== -1) {
          let ext = imgName.substr(idx + 1).toUpperCase();
          ext = ext.toLowerCase();
          if (
            ext !== "pdf" &&
            ext !== "doc" &&
            ext !== "txt" &&
            ext !== "docx" &&
            ext !== "jpg" &&
            ext !== "jpeg" &&
            ext !== "xls" &&
            ext !== "xlsx"
          ) {
            console.log("文件类型错误");
          } else {
            console.log("文件类型正确");
            this.fileType = ext;
            console.log(ext);
            this.upFiles.push(file[i]);
          }
        }
      }
      console.log("==========");
      console.log(file);
      this.readFile(file[0]);
      //call backend interface
    },

    //add attachment
    addAppend() {
      console.log("添加附件");
      this.$refs.uploadFile.dispatchEvent(new MouseEvent("click"));
    },

    //read upload file and display on page
    readFile(file) {
      this.loading = true;
      let reader = new FileReader();
      let that = this;
      reader.readAsText(file);
      reader.onload = function () {
        //that.replyText = reader.result;
        let replyArray = reader.result.split(",");
        console.log("split string " + JSON.stringify(replyArray));
        let words = { words: replyArray };
        saveSensitive(words)
          .then((res) => {
            console.log("on set response ===> " + JSON.stringify(res));
            if (!isNull(res) && !isNull(res.code) && res.code === 0) {
              that.getSensitiveList();
            } else {
              that.loading = false;
              //that.replyText = []
              showSnackbar("上传敏感词错误");
            }            

          }).catch((e) => {
            that.loading = false;
            //that.replyText = []
            showSnackbar("上传敏感词错误");
            console.log(JSON.stringify(e));
          });
      };
    },

    //get file size(b,kb,mb,gb)
    change(limit) {
      let size = "";
      if (limit < 0.1 * 1024) {
        //小于0.1KB，则转化成B
        size = limit.toFixed(2) + "B";
      } else if (limit < 0.1 * 1024 * 1024) {
        //小于0.1MB，则转化成KB
        size = (limit / 1024).toFixed(2) + "KB";
      } else if (limit < 0.1 * 1024 * 1024 * 1024) {
        //小于0.1GB，则转化成MB
        size = (limit / (1024 * 1024)).toFixed(2) + "MB";
      } else {
        //其他转化成GB
        size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";
      }

      let sizeStr = size + ""; //转成字符串
      let index = sizeStr.indexOf("."); //获取小数点处的索引
      let dou = sizeStr.substr(index + 1, 2); //获取小数点后两位的值
      if (dou == "00") {
        //判断后两位是否为00，如果是则删除00
        return sizeStr.substring(0, index) + sizeStr.substr(index + 3, 2);
      }
      return size;
    },

    //delete sensitive
    deleteSensitive() {
      console.log("delete sensitive");
      this.dialog = false;
      this.replyText = "";
      this.upFiles = [];
    },
  },
};
</script>



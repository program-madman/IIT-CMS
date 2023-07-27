<template>
  <v-card class="mx-auto">
    <v-toolbar dense color="#1867C0">
      <v-btn icon>
        <v-icon color="white" @click="bt_close"> mdi-close </v-icon>
      </v-btn>
      <v-toolbar-title class="body-1 white--text">统计报告</v-toolbar-title>
    </v-toolbar>
    <div class="pl-8 pr-8 pt-4">
      <span class="body-1 grey--text text--darken-3"> 人员 </span>
      <template>
        <loading v-if="loadingview" /> 
        <v-data-table
          :headers="headers"
          :items="desserts_area"
          item-key="name"
          v-else
          :search="search_sele"
          class="mt-8 elevation-1"
          hide-default-footer
        >
          <template v-slot:body="{ items }">
            <tbody v-if="items.length > 0">
              <tr v-for="(item, iindex) in desserts_area" :key="item.userId">
                <td>
                  <template
                    v-if="statisticsStatusParam == STATISTICSSTATUS_TYPE_READ&& readStatusParam==STATUS_UNREAD"
                  >
                    <v-checkbox
                      v-if="iindex === 0"
                      v-model="checkedAll"
                      :label="item.name"
                      @change="changeAllChecked"
                    ></v-checkbox>
                    <v-checkbox
                      v-else
                      v-model="checkedNames"
                      :value="item.userId"
                      :label="item.name"
                    ></v-checkbox>
                  </template>
                  <template v-else>
                    {{ item.name }}
                  </template>
                </td>
                <td>{{ item.position }}</td>
                <td>{{ item.reading }}</td>
                <td v-if="feebackBackType == 2">  
                    <template v-if="iindex!=0&&item.reply!=''">                    
                      <v-progress-circular v-if="isRequestDownloadUrl" class="ml-8"  :indeterminate="isRequestDownloadUrl" color="primary"/>                     
                      <v-btn v-else class="text-caption blue--text text--darken-3" @click="downloadfile(item.replyAttachmentId)">
                        {{ item.reply }}
                      </v-btn>
                    </template>
                    <template v-else-if="iindex==0">{{item.reply}}</template>
                    <template v-else>未回复</template>
                </td>
                <td v-else-if="feebackBackType!=4">{{ item.reply }}</td>

                <td
                  v-for="attachment in item.attachDataList"
                  :key="attachment.id"
                >
                  {{ attachment.value }}
                </td>
              </tr>
            </tbody>
            <tbody v-else>
              <tr>
                <td :colspan="headers.length" style="text-align: center">
                  No Results Here!
                </td>
              </tr>
            </tbody>
          </template>
        </v-data-table>

        <v-divider class="#E4E8EB"></v-divider>
      </template>
    </div>
  </v-card>
</template>

<script>
import { getReportPosition,getDownloadUrlInADView } from "@/api/getData.js";
import { composeStatisticsShowText,textEllipsis } from "@/utils/stringUtils.js";
import { downloadFileByUrl } from "@/utils/download.js"
import { showSnackbar } from "@/utils/snackbar.js";

export default {
  name: "statisticalReportPersonView",
  components: {    
    loading: () => import("@/components/base/loading.vue"),
  },
  props: {
    openStatus: {
      type: Boolean,
    },
    areaparam: {
      type: Array,
    },
    msgIDParam: {
      type: String,
    },
    readStatusParam: {
      type: Number,
    },
    statisticsStatusParam: {
      type: Number,
    },
    attachmentIdParam: {
      type: Number,
    },
    statisticsSelectParam: {
      type: Object,
    },
    selectTypeValueParam:{
      type:String,
    },
    companyCodeParam: {
      type: String,
    },
    unSelectCompanyInfos: {
      type: Map,
    },
    searchContentParam:{
      type:String
    },
    //反馈类型
    feebackBackType: {
      type: Number,
    },
  },

  data() {
    return {
      //状态：已读、未读
      STATUS_READ: 0,
      STATUS_UNREAD: 1,

      //统计查询类型 0: 阅读类型查询 1: 附件类型查询 2: 回复类型查询
      STATISTICSSTATUS_TYPE_READ: 0,
      STATISTICSSTATUS_TYPE_ATTACH: 1,
      STATISTICSSTATUS_TYPE_REPLY: 2,

      loadingview:true,

      remindDialog: false,
      search_sele: "",
      selected_area: [],
      expanded: [],
      headers: [],

      desserts_area: [],

      //start 全选参数
      checkedAll: true,
      //所有选中的userId
      checkedNames: [],
      //所有用户 userid: userInfo
      checkedArr: [],
      //所有用户 companyCode:List<userInfo>
      allUserInfoByCompanyCode : new Map(),
      //end 全选参数
      //是否正在请求url
      isRequestDownloadUrl:false,
    };
  },

  mounted() {
    this.getDealerReportService();
  },

  watch: {
    openStatus(newV) {
      console.log("openStatus:" + newV);
      //由不可见到可见
      if (newV) {
        this.getDealerReportService();
      }
    },
    checkedNames: function () {
      if (this.checkedNames.length == this.checkedArr.length) {
        this.checkedAll = true;
      } else {
        this.checkedAll = false;
      }

      console.log("checkedNames checkedAll:" + this.checkedAll+" checkNames["+JSON.stringify(this.checkedNames)+"]  this.checkedArr:"+JSON.stringify(this.checkedArr));
    },
  },

  methods: {
    //查用户
    getDealerReportService() {
      this.headers = [];
      this.desserts_area = [];
      this.loadingview  = true;
      this.checkedArr = [];
      this.allUserInfoByCompanyCode.clear();

      console.log("statisticsStatusParam:"+this.statisticsStatusParam);

      getReportPosition(this.msgIDParam,this.areaparam,this.selectTypeValueParam,this.searchContentParam,this.companyCodeParam,this.readStatusParam,this.statisticsStatusParam,this.attachmentIdParam)
      .then((res) => {
          console.log("getSuppilerListData receive:" + JSON.stringify(res));

          if (res.data == null) {
            console.log("data size is 0");
            return;
          }

          this.headers = this.initHeaderData(res.data.reports);
          this.desserts_area = this.initBodyData(res.data.reports);
          this.checkedAll = this.checkedNames.length == this.checkedArr.length;
        })
        .catch((res) => {
          console.log(res);
        }).finally(()=>{
            this.loadingview = false;
        });
    },

    //关闭
    bt_close() {
      console.log(
        "......关闭按钮....companyCodeParam:" +
          this.companyCodeParam +
          " checkedNames:" +
          JSON.stringify(this.checkedNames)
      );

      let companyCode = this.companyCodeParam;            
      let unSelectUsers = [];

      this.checkedArr.forEach((userInfo)=>{
          if(this.checkedNames.indexOf(userInfo.userId)<0){
              unSelectUsers.push(userInfo.userId);
          }
      });

      console.log(        
          "unSelectUsers:" +
          JSON.stringify(unSelectUsers)
      );

      //合计内容返回
      if(companyCode==''){
          this.$emit("closeParentInAll",this.filterUnselectInfo());                  
      }else{//具体某个公司的操作数据返回
          this.$emit("closedChild", unSelectUsers, companyCode,unSelectUsers.length==this.checkedArr.length);
      }
    },
    filterUnselectInfo(){
        let unSelectCompanyMapping  = new Map();
        let resultSelectCompnayMapping = new Map();
        
        //未选中的用户信息
        this.checkedArr.forEach((userInfo)=>{

            if(this.checkedNames.indexOf(userInfo.userId)>=0){
                return;
            }

            let userList = unSelectCompanyMapping.get(userInfo.companyCode);
            if(!userList){
                userList = new Array()
                unSelectCompanyMapping.set(userInfo.companyCode,userList);
            }

            userList.push(userInfo.userId);          
        });

        console.log("unSelectCompanyMapping:"+JSON.stringify(unSelectCompanyMapping));

        for (let [key, value] of unSelectCompanyMapping.entries()) {
            let sourceUserList = this.allUserInfoByCompanyCode.get(key);
            //所有用户未选中,将该公司代码加入
            if(sourceUserList.length==value.length){
              resultSelectCompnayMapping.set(key,[]);
            }else{//部分用户选入
              resultSelectCompnayMapping.set(key,value);
            }
        }

        return resultSelectCompnayMapping;
    },
    //构建单个header头信息
    buildHeaderBean(text, value) {
      let headerBean = {};
      headerBean.text = textEllipsis(text,20);
      headerBean.value = value;
      headerBean.width = "10rem";
      headerBean.align = "left";
      return headerBean;
    },
    //初始化Header的数据
    initHeaderData(data) {
      let headerDataArray = [];

      headerDataArray.push(this.buildHeaderBean("姓名", "userName"));
      headerDataArray.push(this.buildHeaderBean("职务", "position"));
      if(this.readStatusParam==this.STATUS_UNREAD){
          headerDataArray.push(this.buildHeaderBean("未读统计", "reading"));
      }else{
          headerDataArray.push(this.buildHeaderBean("已读统计", "reading"));
      } 
      //无需回复，不需要展示已回复统计   
      if(this.feebackBackType!=4){
        headerDataArray.push(this.buildHeaderBean("已回复统计", "reply"));
      }

      if (data[0].attachmentReports==null||data[0].attachmentReports.length == 0) {
        console.log("initHeaderData:attachmentStatistics is 0");
        return headerDataArray;
      }

      data[0].attachmentReports.forEach((attachmentStatistics) => {
        let attachmentName = attachmentStatistics.attachmentName.slice(
          attachmentStatistics.attachmentName.lastIndexOf("/") + 1
        );

        headerDataArray.push(
          this.buildHeaderBean(
            "已下载统计(" + attachmentName + ")",
            attachmentStatistics.attachmentId
          )
        );
      });
      return headerDataArray;
    },
    
    /**
     * 下载文件
     */
    downloadfile(replyAttachmentId){

      this.isRequestDownloadUrl = true;

      getDownloadUrlInADView(this.msgIDParam,replyAttachmentId)
          .then((res) => { 
              if(typeof res !== "undefined" && res !== null && res.code === 0 && typeof res.data !== "undefined" && res.data !== null && res.data !== "") {
                  downloadFileByUrl(res.data)   
              }else {
                  showSnackbar('下载失败，请稍候重试');
              }
          })
          .catch(() => {
              showSnackbar('下载失败，请稍候重试');
          })
          .finally(() => {
              this.isRequestDownloadUrl = false;
          });  
    },

    /**
     * 构造本地数据映射，方便查找
     */
    constructLocalMapping(personData){      
        let localUserInfoDefine={userId:personData.userId,companyCode:personData.companyCode};

        this.checkedArr.push(localUserInfoDefine);
        
        let userList = this.allUserInfoByCompanyCode.get(personData.companyCode);
        if(userList==null||userList==undefined){
            userList = new Array()
            this.allUserInfoByCompanyCode.set(personData.companyCode,userList);
        }

        userList.push(localUserInfoDefine);
    },

    initBodyData(data) {
      this.checkedNames = [];
      let dataArray = [];
      let totalinfo = {};
      let totalAttachmentStatistics = [];

      let readStatisticsCount = 0;
      let readStatisticsTotalCount = 0;
      let feedbackStatisticsCount = 0, feedbackStatisticsTotalCount = 0;     

      for (let [key, value] of this.unSelectCompanyInfos.entries()) {
          console.log("initHeaderData --- : companyCode:"+key+" unSelectCompanyInfos "+JSON.stringify(value));
      }

      console.log("initHeaderData:feebackBackType is "+this.feebackBackType);

      data.forEach((personData) => {
          let bodyBean = {};

          this.constructLocalMapping(personData);

          let unselectUsersInCompany = this.unSelectCompanyInfos.get(personData.companyCode);

          if (
            unselectUsersInCompany == undefined ||
            (unselectUsersInCompany.length>0 && unselectUsersInCompany.indexOf(personData.userId) < 0)
          ) {
              this.checkedNames.push(personData.userId);
          }

          bodyBean.userId = personData.userId;
          bodyBean.name = personData.userName;
          bodyBean.position = personData.position;
          bodyBean.selected = true;
          bodyBean.replyAttachmentId = personData.replyAttachmentId;       

          bodyBean.reading =
            personData.haveReadStatus == 1 ? "已读" : "未读";
          
          //回执类:已读、未读
          if (this.feebackBackType == 0) {
            bodyBean.reply =
              personData.haveFeedbackStatus == 1
                ? "已读"
                : "未读";
            //文本回复:读者回复的文本内容
          } else if (this.feebackBackType == 1 || this.feebackBackType == 3) {
            if(personData.replyContent==undefined){
                bodyBean.reply = "未回复";               
            }else{
                bodyBean.reply = personData.replyContent;
            }
            // //附件回复
          } else if (this.feebackBackType == 2) {
            if(personData.replyContent==undefined){
                bodyBean.reply = '';   
            }else{
                bodyBean.reply = personData.replyContent.slice(personData.replyContent.lastIndexOf("/") + 1);
                bodyBean.reply = textEllipsis(bodyBean.reply,20);
            }

          } else {
            bodyBean.reply = "";
          }

          readStatisticsCount += personData.haveReadStatus;
          readStatisticsTotalCount += 1;

          feedbackStatisticsCount +=personData.haveFeedbackStatus;
          feedbackStatisticsTotalCount += 1;

          let attachDataList = [];     

          personData.attachmentReports.forEach(
            (attachmentStatistics, index) => {

              let attachData = {};
              if (totalAttachmentStatistics[index] == undefined) {
                totalAttachmentStatistics[index] = { id: "",statisticsCount: 0,totalCount: 0,};
              }              

              attachData.value = attachmentStatistics.haveDownloadStatus == 1 ? "已下载": "未下载";

              totalAttachmentStatistics[index].statisticsCount +=attachmentStatistics.haveDownloadStatus;
              totalAttachmentStatistics[index].totalCount += 1;

              attachDataList.push(attachData);
            }
          );

          bodyBean.attachDataList = attachDataList;

          dataArray.push(bodyBean);
      });

      totalinfo.userId = "-1";
      totalinfo.name = "合计";
      totalinfo.position = "-";
      totalinfo.selected = true;

      if(this.readStatusParam==this.STATUS_UNREAD){
          totalinfo.reading = composeStatisticsShowText(readStatisticsTotalCount-readStatisticsCount,readStatisticsTotalCount,100);
      }else{
          totalinfo.reading = composeStatisticsShowText(readStatisticsCount,readStatisticsTotalCount,100);
      }
      totalinfo.reply = composeStatisticsShowText(feedbackStatisticsCount,feedbackStatisticsTotalCount,(feedbackStatisticsCount * 100) / feedbackStatisticsTotalCount);

      totalinfo.attachDataList = [];
      totalAttachmentStatistics.forEach((attachmentStatistics) => {
        let attachData = {};

        attachData.id = attachmentStatistics.attachmentId;
        attachData.value = composeStatisticsShowText(attachmentStatistics.statisticsCount,attachmentStatistics.totalCount,(attachmentStatistics.statisticsCount * 100) /attachmentStatistics.totalCount);

        totalinfo.attachDataList.push(attachData);
      });

      dataArray.unshift(totalinfo);

      return dataArray;
    },
    changeAllChecked: function () {
      console.log(
        "checkedAll:" +
          this.checkedAll +
          " checkedNames:" +
          JSON.stringify(this.checkedNames) +
          "  checkedArr:" +
          JSON.stringify(this.checkedArr)
      );

      if (this.checkedAll) {
        this.checkedArr.forEach((userInfo)=>{
            this.checkedNames.push(userInfo.userId);
        });
      } else {
        this.checkedNames = [];
      }
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
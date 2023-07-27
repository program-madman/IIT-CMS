<template>
  <v-card class="mx-auto">
    <v-toolbar dense color="#1867C0">
      <v-btn icon>
        <v-icon color="white" @click="bt_close"> mdi-close </v-icon>
      </v-btn>
      <v-toolbar-title class="body-1 white--text">统计报告</v-toolbar-title>
    </v-toolbar>
    <div class="pl-8 pr-8 pt-4">
      <span class="body-1 grey--text text--darken-3"> 经销商 </span>
      <v-row align="center" class="mt-6 ml-1 mb-4">        
        <v-dialog v-model="remindDialog" max-width="720px" height="630px" scrollable>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              class="white--text"
              color="#1867C0"
              small
              depress
              v-bind="attrs"
              v-on="on"
              @click="showReminderDialog"
            >
              发送提醒
            </v-btn>
          </template>
          <v-card>
            <v-card-title style="font-size: 20px;">将向以下经销商对应的人员发送提醒</v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height: 500px;">  
            <loading v-if="reminderLoading" />     
            <template v-else>
                <template v-if="this.reminderCompanyList.length>0">
                  <v-data-table
                    :headers="reminderheaders"
                    :items="reminderCompanyList"
                    item-key="id"                
                    class="mt-8 elevation-1"
                    hide-default-footer
                    disable-pagination
                    dense
                  >
                  </v-data-table>      
                </template>
                <template v-else>
                  <v-row class="fill-height">
                      <v-col class="fill-height d-flex flex-column justify-center align-center">
                          <h3 class="gray--text"><div>未找到需要提醒的经销商</div></h3>
                      </v-col>
                  </v-row>            
                </template>
            </template>   
          </v-card-text>

            <v-divider></v-divider>
            <v-card-actions style="height:70px">
              <v-row justify="center">
                <v-btn min-width="105px" height="36px" dark outlined color="#1867C0FF" style="margin-right: 24px" @click="closeReminder">
                  取消
                </v-btn>
                <v-btn :disabled="this.reminderCompanyList.length==0" class="white--text" min-width="105px" height="36px" color="#1867C0" @click="sendReminder">
                  发送
                </v-btn>
              </v-row>
            </v-card-actions>
          </v-card>          
        </v-dialog>
        <v-dialog v-model="isSending" max-width="720px">
          <v-card color="grey lighten-4" dark>
              <v-card-text style=" color:black;">
                  正在发送，请稍候
                <v-progress-linear
                  indeterminate
                ></v-progress-linear>
              </v-card-text>
          </v-card>
        </v-dialog>
        <!-- 已读回执、附件、文本回复，三种类型需要支持导出 -->
        <template v-if="this.feebackBackType != 4">
            <v-btn     
              v-if="!this.isDownloadUrlRequesting"     
              class="ml-4"
              color="#1867C0"
              small
              text
              depress
              @click="exportReply">
              <v-icon small>mdi-export</v-icon>
              回复附件导出
            </v-btn>
            <v-progress-circular
               class="ml-4"
               v-if="this.isDownloadUrlRequesting"
               :indeterminate="this.isDownloadUrlRequesting"
               color="primary">
            </v-progress-circular>
            
        </template>      
      </v-row>

      <v-row class="pl-4 pt-3 pb-5 pr-4" justify="center">

        <v-combobox
          v-model="select_status"
          :items="items_status"
          height="36px"
          style="width: 60px"
          class="pa-0 mr-4 mr-sm-2"
          label="状态"
          @change="selectStatusChange"
          outlined
          dense
        ></v-combobox>

        <v-combobox
          v-model="select_area"
          :items="items_area"
          height="36px"
          style="width: 60px"
          class="pa-0 mr-4 mr-sm-2"
          label="区域"
          @change="selectAreaChange"
          outlined
          dense
        ></v-combobox>

        <v-combobox
          v-model="select_type"
          :items="items_type"
          item-value="id"
          item-text="text"
          height="36px"
          style="width: 60px"
          label="类型"
          class="pa-0 mr-4 mr-sm-2"
          @change="selectTypeChange"
          outlined
          dense
        ></v-combobox>

        <v-text-field
          v-model="search_sele"
          flat
          style="height: 36px; min-width: 140px;width: 320px;"
          hide-details
          clearable        
          label="按照经销商名称和编码查找"
          placeholder="按照经销商名称和编码查找"
          maxlength="60"
          counter="60"
          append-icon="mdi-magnify"
          outlined
          dense
          class="ml-sm-0 ml-md-2 mr-4"
        ></v-text-field>

        <v-btn
          outlined
          class="blue--text text--darken-2 mr-4"
          height="38px"
          width="97px"
          @click="bt_search"
        >
          查找
        </v-btn>
        <v-btn
          outlined
          width="97px"
          class="blue--text text--darken-2"
          height="38px"
          @click="bt_reset"
        >
          还原
        </v-btn>
      </v-row>

      <template>
        <loading v-if="loadingview" />
        <v-data-table
          v-else
          :headers="headers"
          :items="desserts_area"
          item-key="area"
          class="mt-4 elevation-1"
          hide-default-footer
        >
          <template v-slot:body="{ items }">
            <tbody v-if="items.length > 0">
              <tr v-for="(item, index) in items" :key="item.area" style="width: 200px">
                <td style="text-align: left">{{ item.area }}</td>
                <td
                  style="text-align: left"
                  @click="
                    clickRow(STATISTICSSTATUS_TYPE_READ, item.area, -1, item)
                  "
                >
                  <span style="color: #1867c0" v-if="index>0">{{item.readingStatisticsCount}}</span>
                  <span v-else>{{item.readingStatisticsCount}}</span
                  >/{{ item.readingTotalCount }} {{ item.readingPercentage }}%
                  

                </td>
                <td
                  style="text-align: left"
                  v-if="feebackBackType!=4"
                  @click="
                    clickRow(STATISTICSSTATUS_TYPE_REPLY, item.area, -1, item)
                  "
                >
                  <span style="color: #1867c0" v-if="index>0">{{item.replyStatisticsCount}}</span>
                  <span v-else>{{item.replyStatisticsCount}}</span>
                  /{{ item.replyTotalCount }} {{ item.replyPercentage }}%
                </td>

                <td
                  v-for="attachment in item.attachDataList"
                  :key="attachment.attachmentName"
                  @click="
                    clickRow(
                      STATISTICSSTATUS_TYPE_ATTACH,
                      item.area,
                      attachment.attachmentId,
                      item
                    )
                  "
                >
                  <span style="color: #1867c0" v-if="index>0">{{attachment.valueStatisticsCount}}</span>
                  <span v-else>{{attachment.valueStatisticsCount}}</span>
                  /{{ attachment.valueTotalCount }}
                  {{ attachment.valuePercentage }}%
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

        <v-dialog
          v-model="dialog_supplier"
          fullscreen
          hide-overlay
          transition="dialog-bottom-transition"
        >
          <SupplierReportView
            @closedChild="closeParent"
            :areaparam="clickAreaName"
            :msgIDParam="msgID"
            :openStatus="dialog_supplier"
            :readStatusParam="selectReadValue"
            :statisticsStatusParam="statisticsStatus"
            :attachmentIdParam="attachmentId"
            :selectTypeValueParam="selectTypeValue"
            :feebackBackType="feebackBackType"
            :searchContentParam = "this.search_sele"
          ></SupplierReportView>
        </v-dialog>
      </template>

      <v-snackbar
          v-model="snackbar">
          {{ showSnackbarText }}

          <template v-slot:action="{ attrs }">
            <v-btn
              text
              v-bind="attrs"
              @click="snackbar = false"
            >
              关闭
            </v-btn>
          </template>
    </v-snackbar>
    </div>
  </v-card>
</template>

<script>
import SupplierReportView from "@/components/statisticalReportSupplierView.vue";
import { 
  getReminderDealers,
  sendMessageRemind,
  getArticleAreas,
  getAllDealerReport,
  getAllDownloadUrlInReportView
} from "@/api/getData.js";
import { downloadFileByUrl } from "@/utils/download.js";
import {roundFun,textEllipsis} from "@/utils/stringUtils.js";

export default {
  name: "statisticalReportView",  

  props: ['msgIdParam','feebackBackType'],

  components: {
    SupplierReportView,
    loading: () => import("@/components/base/loading.vue"),
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
      //加载状态
      loadingview: true,
      reminderLoading:false,

      snackbar:false,

      DEALERTYPE: [
        { text: "全部", id: "" },
        { text: "4S店", id: "1" },
        { text: "独立售后", id: "0" },
        { text: "二级网点", id: "2" },
      ],

      remindDialog: false,
      search_sele: "",
      selected_area: [],
      expanded: [],
      clickAreaName: [],
      statisticsStatus: 0,
      attachmentId: 0,

      reminderCompanyList: [],
      unSelectedReminderUserId: [],
      unSelectReminderCompanyIds: [],

      dialog_supplier: false,
      msgID: this.msgIdParam,

      headers: [],
      desserts_area: [],

      isSending:false,

      items_status: ["未读","已读"],
      article_areas:[],
      items_area: [],
      items_type: [],


      selectReadValue: this.STATUS_UNREAD,
      selectTypeValue: "",

      select_status: "未读",
      select_area: "全部",
      select_type: [],
      //是否正在请求生成下载url
      isDownloadUrlRequesting:false,
      
      unSelectComanyInfoMapInfo: new Map(),

      reminderheaders: [
        { text: "区域", value: "area" },
        { text: "经销商", value: "companyName" },
      ],
    };
  },

  mounted() {
    console.log(
      "mounted --- msgID:" +
        this.msgID +
        " feebackBackType:" +
        this.feebackBackType
    );

    this.items_type = this.DEALERTYPE;
    this.select_type = { text: "全部", id: "" };
    this.selectReadValue = this.STATUS_UNREAD;

    //获取文章区域    
    this.getArticleAreasService();
  },

  methods: {
    
    /**
     * 获取文章发布时所选区域
     */
    getArticleAreasService(){
        getArticleAreas(this.msgID)
       .then((res) => {

          console.log("getArticleAreas receive:" + JSON.stringify(res));

          if (res.data == null) {
            console.log("data size is 0");
            return;
          }

          this.processAreaResponse(res.data);

        })
        .catch((res)=>{
            console.log(res);
        })       
    },       
    
    /**
     * 初始化区域数据响应
     */
    processAreaResponse(data){
        //初始化区域
        this.initItemsArea(data);          

        //开始查询报告
        this.getDealerReportService();
    },

    /**
     * 获取查询条件对应的区域
     */
    getQueryArea(selectArea){
      console.log('getQueryArea:'+selectArea);      
      if(selectArea == "全部"){
          return this.article_areas;
      }else{
          return [selectArea];           
      }
    },

    /**
     * 获取统计报告
     */
    getDealerReportService() {
        this.resetData();
        this.loadingview = true;

        getAllDealerReport(this.msgID,this.getQueryArea(this.select_area),this.selectTypeValue,this.search_sele)
        .then((res) => {
            console.log("getDealerReport receive:" + JSON.stringify(res));

            if (res.data == null) {
              console.log("data size is 0");
              return;
            }

            this.headers = this.initHeaderData(res.data.reports);
            if(res.data.reports.length==0){
              this.desserts_area = [];
            }else{
              this.desserts_area = this.initBodyData(res.data.reports);
            }
        })
        .catch((res) => {
            console.log(res);
        })
        .finally(() => {
            this.loadingview = false;
        });
    },

    resetData() {
      this.headers = [];
      this.desserts_area = [];
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

    /**
     * 初始化选择区域
     */
    initItemsArea(data) {
      this.article_areas = [];
      this.items_area = ['全部'];

      data.forEach((areaName) => {
          this.items_area.push(areaName);
          this.article_areas.push(areaName);
      });
    },

    //初始化Header的数据
    initHeaderData(data) {
      let headerDataArray = [];

      headerDataArray.push(this.buildHeaderBean("名称", "area"));   
      if(this.selectReadValue == this.STATUS_UNREAD){
          headerDataArray.push(this.buildHeaderBean("未读统计", "reading"));
      }else{
          headerDataArray.push(this.buildHeaderBean("已读统计", "reading"));
      }   
      //如果是无需要回复的文章，此列不需要展示
      if(this.feebackBackType!=4){
          headerDataArray.push(this.buildHeaderBean("已回复统计", "reply"));
      }
      
      if (data.length==0) {
        console.log("initHeaderData:attachmentStatistics is 0");
        return headerDataArray;
      }

      console.log("initHeaderData:data "+JSON.stringify(data));

      let attachNameList = [];

      data.forEach((supplierData)=>{
          if(supplierData.attachmentReports!=null&&supplierData.attachmentReports.length>0
            &&attachNameList.length==0){            
              supplierData.attachmentReports.forEach((attachmentStatistics) => {
              let attachmentName = attachmentStatistics.attachmentName.slice(
                  attachmentStatistics.attachmentName.lastIndexOf("/") + 1
              );

              attachNameList.push(
                this.buildHeaderBean(
                  "已下载统计(" + attachmentName + ")",
                  attachmentStatistics.attachmentName
                )
              );
            });          
          }
      });        
      
      attachNameList.forEach((headerBean)=>{
          headerDataArray.push(headerBean);
      });
      
      return headerDataArray;
    },

    /**
     * 阅读统计赋值
     */
    composeReadStatisticsData(bodyBean,haveReadCount,readCount){
          if(this.selectReadValue == this.STATUS_READ){
              bodyBean.readingStatisticsCount = haveReadCount;
          }else{
              bodyBean.readingStatisticsCount = readCount - haveReadCount;
          }          
          bodyBean.readingTotalCount = readCount;
          bodyBean.readingPercentage = roundFun(bodyBean.readingStatisticsCount,bodyBean.readingTotalCount);        
    },

    /**
     * 阅读统计反馈
     */
    composeFeedbackData(bodyBean,haveFeedbackCount,feedbackCount){      
          bodyBean.replyStatisticsCount = haveFeedbackCount;            
          bodyBean.replyTotalCount = feedbackCount;
          bodyBean.replyPercentage = roundFun(bodyBean.replyStatisticsCount,bodyBean.replyTotalCount);
    },

    /**
     * 组装附件文件
     */
    composeAttachFile(attachmentStatistics){
        let attachData = {};
        
        attachData.attachmentId = attachmentStatistics.attachmentId;
        attachData.valueStatisticsCount = attachmentStatistics.haveDownloadCount;        
        attachData.valueTotalCount = attachmentStatistics.downloadCount;
        attachData.valuePercentage = roundFun(attachData.valueStatisticsCount,attachData.valueTotalCount);

        return attachData;
    },

    /**
     * 新建一个附件对象
     */
    buildNewAttachmentData(){
        return {
            id: "",
            statisticsCount: 0,
            totalCount: 0,          
        };
    },
   
    /**
     * 组装表格内容
     */
    initBodyData(data) {
        let dataArray = [];
        let totalinfo = {};
        let totalAttachmentStatistics = [];

        totalinfo.area = "合计";

        let readStatisticsCount = 0,
          readStatisticsTotalCount = 0;
        let feedbackStatisticsCount = 0,
          feedbackStatisticsTotalCount = 0;

        data.forEach((statisticsReport) => {

              if(statisticsReport.area==null){ 
                return;                     
              }

              let bodyBean = {};

              bodyBean.area = statisticsReport.area;

              //阅读统计赋值
              this.composeReadStatisticsData(bodyBean,statisticsReport.haveReadCount,statisticsReport.readCount);
              readStatisticsCount += bodyBean.readingStatisticsCount;
              readStatisticsTotalCount += bodyBean.readingTotalCount;

              //回复统计赋值
              this.composeFeedbackData(bodyBean,statisticsReport.haveFeedbackCount,statisticsReport.feedbackCount);
              feedbackStatisticsCount += bodyBean.replyStatisticsCount;
              feedbackStatisticsTotalCount += bodyBean.replyTotalCount;   

              //附件下载统计
              let attachDataList = [];

              statisticsReport.attachmentReports.forEach(
                (attachmentStatistics, index) => {            

                  if (totalAttachmentStatistics[index] == undefined) {
                      totalAttachmentStatistics[index] = this.buildNewAttachmentData();
                  }

                  //设置附件数据
                  let attachData = this.composeAttachFile(attachmentStatistics);
                  
                  totalAttachmentStatistics[index].attachmentId = attachmentStatistics.attachmentId;
                  totalAttachmentStatistics[index].statisticsCount += attachData.valueStatisticsCount;
                  totalAttachmentStatistics[index].totalCount += attachData.valueTotalCount;

                  attachDataList.push(attachData);
                }
              );

              bodyBean.attachDataList = attachDataList;

              dataArray.push(bodyBean);
            });

            totalinfo.readingStatisticsCount = readStatisticsCount;
            totalinfo.readingTotalCount = readStatisticsTotalCount;
            totalinfo.readingPercentage = roundFun(readStatisticsCount,readStatisticsTotalCount);

            totalinfo.replyStatisticsCount = feedbackStatisticsCount;
            totalinfo.replyTotalCount = feedbackStatisticsTotalCount;
            totalinfo.replyPercentage = roundFun(feedbackStatisticsCount,feedbackStatisticsTotalCount);

            totalinfo.attachDataList = [];
            totalAttachmentStatistics.forEach((attachmentStatistics) => {
              let attachData = {};

            attachData.attachmentId = attachmentStatistics.attachmentId;
            attachData.valueStatisticsCount = attachmentStatistics.statisticsCount;
            attachData.valueTotalCount = attachmentStatistics.totalCount;
            attachData.valuePercentage = roundFun(attachmentStatistics.statisticsCount,attachmentStatistics.totalCount);

            totalinfo.attachDataList.push(attachData);
        });

        console.log(
          "totalAttachmentStatistics:" + JSON.stringify(totalinfo.attachDataList)
        );

        dataArray.unshift(totalinfo);

        return dataArray;
    },    

    closeReminder() {
      console.log("......关闭提醒按钮.......");
      this.remindDialog = false;
    },

    //关闭
    bt_close() {
      console.log("......关闭按钮.......");
      this.$emit("closedChild", false);
    },
    //关闭dialog
    closeParent(unSelectComanyInfoMapInfo) {
      console.log("-----------" + this.nowDialog);
      this.dialog_supplier = false;

      this.unSelectComanyInfoMapInfo.clear();
      for (let [key, value] of unSelectComanyInfoMapInfo.entries()) {
        this.unSelectComanyInfoMapInfo.set(key, value);
      }
    },

    /**
     * 获取需要排除的公司列表
     */
    getUnSelectCompanyCodeList(){
        let excludeCompanyCode = [];

        for (let [key, value] of this.unSelectComanyInfoMapInfo.entries()) {
            if(value.length==0){              
                excludeCompanyCode.push(key);
            }
        }

        console.log('getUnSelectCompanyCodeList:'+JSON.stringify(excludeCompanyCode));

        return excludeCompanyCode;      
    },

    /**
     * 获取需要排除的用户列表
     */
    getUnselectUserList(){
        let excludeUserList = [];

        for (let [key, value] of this.unSelectComanyInfoMapInfo.entries()) {
            console.log("key:"+key+" value:"+JSON.stringify(value));
            if(value.length>0){       
                value.forEach((item)=>{
                  excludeUserList.push(item);
                });                       
            }
        }

        console.log('getUnselectUserList:'+JSON.stringify(excludeUserList));

        return excludeUserList;
    },  

    //显示Reminder弹出框
    showReminderDialog(){
        this.remindDialog = true;
        this.reminderLoading = true;

        getReminderDealers(this.msgID,this.getUnSelectCompanyCodeList())
        .then((res) => {            
            if(res.code=='0'){
                this.reminderCompanyList = res.data;
            }else{
                this.reminderCompanyList = [];
            }            
            console.log('reminderCompanyList:'+JSON.stringify(this.reminderCompanyList));
        }).finally(()=>{
          this.reminderLoading = false;
        });

    },
    //发送提醒
    sendReminder() {
      console.log("......发送提醒.......");

      this.isSending = true;
      this.remindDialog = false;      

      sendMessageRemind(
        this.msgID,
        this.getUnSelectCompanyCodeList(),
        this.getUnselectUserList()
        )
        .then((res) => {
            if(res.code==0){
              this.showSnackbarText = "发送成功";
              this.snackbar = true;
            }
        })
        .catch((error) => {
          console.log(error);
          if (error.response == null || typeof error.response == "undefined") {
            return;
          }
          let res = error.response;
          if (res.data == null || typeof res.data == "undefined") {
            return;
          }
          let data = res.data;
          if (
            data.code != null &&data.code=='28'
          ) {
              this.showSnackbarText = "1小时内只能发送1次提醒，请稍后再试试";           
              this.snackbar = true;
          }

        }).finally(()=>{            
            this.isSending = false;
        });
    },
    //查找
    bt_search() {
      console.log("......查找.......:" + this.search_sele);
      if (this.search_sele != "") {
          this.getDealerReportService();
      }
    },
    //还原
    bt_reset() {
      console.log("......还原.......");
      this.search_sele = "";
      (this.select_status = "未读"),
        (this.select_type = { text: "全部", id: "" });
      this.selectReadValue = this.STATUS_UNREAD;
      this.select_area = "全部";

      this.getDealerReportService();
    },
    //状态选择的值改变
    selectStatusChange() {
      console.log("....状态选择.... :" + this.select_status);
      if (this.select_status == "未读") {
        this.selectReadValue = this.STATUS_UNREAD;
      } else {
        this.selectReadValue = this.STATUS_READ;
      }

      this.getDealerReportService();
    },
    //区域选择的值改变
    selectAreaChange() {
      console.log("....区域选择.... :" + this.select_area);

      this.getDealerReportService();
    },
    exportReply() {
      console.log("....导出回复附件.... :" + this.select_area);
    
      this.isDownloadUrlRequesting = true;

      getAllDownloadUrlInReportView(this.msgID)
        .then((res) => { 
          if(typeof res !== "undefined" && res !== null && res.code === 0 && typeof res.data !== "undefined" && res.data !== null && res.data !== "") {
            downloadFileByUrl(res.data)   
          }else {
              this.showSnackbarText = "下载失败，请稍候重试";
              this.snackbar = true;
          }
        })
        .catch(() => {
            this.showSnackbarText = "下载失败，请稍候重试";
            this.snackbar = true;
        })
        .finally(() => {
            this.isDownloadUrlRequesting = false;
        });
    },
    //类型选择的值改变
    selectTypeChange() {
      this.selectTypeValue = this.select_type.id;

      console.log("....类型选择.... selectTypeValue:" + this.selectTypeValue);

      this.getDealerReportService();
    },
    //当table row click时触发
    clickRow: function (type, area, attachId) {    
      if(area=='合计'){        
        //  this.clickAreaName = this.getQueryArea('全部');
        return;
      } else{
         this.clickAreaName = this.getQueryArea(area);
      }
      this.statisticsStatus = type;
      this.attachmentId = attachId;

      console.log(
        "....rowClick.... statisticsStatus:" +
          this.statisticsStatus +
          "  clickAreaName:" +
          JSON.stringify(this.clickAreaName) +
          " attachmentId:" +
          this.attachmentId
      );

      this.dialog_supplier = true;
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
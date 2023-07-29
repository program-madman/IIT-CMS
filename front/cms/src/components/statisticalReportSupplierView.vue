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
      <template>
        <loading v-if="loadingview" /> 
        <v-data-table
          v-else
          :headers="headers"
          :items="desserts_area"
          item-key="area"
          :search="search_sele"
          class="mt-8 elevation-1"
          hide-default-footer
        >
          <template v-slot:body="{ items }">
            <tbody v-if="items.length > 0">
              <tr v-for="(item, iindex) in desserts_area" :key="item.supnumber">
                <td>{{item.supnumber }}</td>
                <td>
                  <template
                    v-if="statisticsStatusParam == STATISTICSSTATUS_TYPE_READ && readStatusParam==STATUS_UNREAD"
                  >
                    <v-checkbox
                      v-if="iindex === 0"
                      v-model="checkedAll"
                      :label="item.supname"
                      @change="changeAllChecked"
                    ></v-checkbox>
                    <v-checkbox
                      v-else
                      v-model="checkedNames"
                      :value="item.supnumber"
                      :label="item.supname"
                    ></v-checkbox>
                  </template>
                  <template v-else>
                    {{ item.supname }}
                  </template>
                </td>
                <td
                  @click="clickRow(STATISTICSSTATUS_TYPE_READ, item.supnumber,0)"
                >
                   <span style="color: #1867c0" v-if="iindex > 0">{{item.readingStatisticsCount}}</span>
                   <span v-else>{{item.readingStatisticsCount}}</span>                   
                  /{{ item.readingTotalCount }} {{ item.readingPercentage}}%
                </td>
                <td
                  v-if="feebackBackType!=4"
                  @click="clickRow(STATISTICSSTATUS_TYPE_REPLY, item.supnumber,0)"
                >
                  <span style="color: #1867c0" v-if="iindex > 0">{{item.replyStatisticsCount}}</span>
                  <span v-else>{{item.replyStatisticsCount}}</span>                  
                  /{{ item.replyTotalCount }} {{ item.replyPercentage }}%
                </td>

                <td
                  v-for="attachment in item.attachDataList"
                  :key="attachment.attachmentId"
                  @click="clickRow(STATISTICSSTATUS_TYPE_ATTACH,item.supnumber, attachment.attachmentId)"
                >
                  <span style="color: #1867c0" v-if="iindex > 0">{{attachment.valueStatisticsCount}}</span>
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

        <v-dialog
          v-model="dialog_person"
          fullscreen
          hide-overlay
          transition="dialog-bottom-transition"
        >
          <ReportPersonView
            @closedChild="closeParent"
            @closeParentInAll = "closeParentInAll"
            :areaparam="this.areaparam"
            :msgIDParam="msgIDParam"
            :readStatusParam="readStatusParam"
            :statisticsStatusParam="selectStatisticsStatus"
            :attachmentIdParam="clickAttachmentId"
            :openStatus="dialog_person"
            :selectTypeValueParam="this.selectTypeValueParam"
            :companyCodeParam="selectCompanyCode"
            :unSelectCompanyInfos="unSelectComanyInfoMapInfo"
            :feebackBackType = "feebackBackType"
            :searchContentParam = "this.searchContentParam"
          ></ReportPersonView>
        </v-dialog>

        <v-divider class="#E4E8EB"></v-divider>
      </template>
    </div>
  </v-card>
</template>

<script>
import ReportPersonView from "@/components/statisticalReportPersonView.vue";
import { getCompanybyarea } from "@/api/getData.js";
import { roundFun,textEllipsis} from "@/utils/stringUtils.js";

export default {
  name: "statisticalReportSupplierView",
  props: {
    openStatus: {
      type: Boolean,
    },
    areaparam: {
      type: Array,
    },

    searchContentParam:{
      type:String,
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
    //反馈类型
    feebackBackType:{
      type:Number
    },
    attachmentIdParam: {
      type: Number,
    },
    selectTypeValueParam: {
      type: String,
    },
  },
  components: {
    ReportPersonView,
    loading: () => import("@/components/base/loading.vue"),
  },

  data() {
    return {
      //统计查询类型 0: 阅读类型查询 1: 附件类型查询 2: 回复类型查询
      STATISTICSSTATUS_TYPE_READ: 0,
      STATISTICSSTATUS_TYPE_ATTACH: 1,
      STATISTICSSTATUS_TYPE_REPLY: 2,
      dialog: this.open,
      dialog_person: false,
      search_sele: "",
      selected_area: [],
      expanded: [],
      headers: [],
      desserts_area: [],
      selectCompanyCode: "",
      selectStatisticsStatus: 0,

        //加载状态
      loadingview: true,

      //状态：已读、未读
      STATUS_READ:0,
      STATUS_UNREAD: 1,

      unSelectComanyInfoMapInfo: new Map(),

      //start 全选参数
      checkedAll: true,
      //所有选中的companyCode
      checkedNames: [],
      checkedArr: [],
      //end 全选参数

      //点击的附件ID
      clickAttachmentId : "",
    };
  },

  mounted() {
    console.log("---- mounted ----");

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

      //更新选择数组
      this.updateSelectComanyInfoMapInfo();
    },
  },

  methods: {
    updateSelectComanyInfoMapInfo() {
      this.checkedArr.forEach((companyCode=>{
          //如果选中,取消该公司定义
          if(this.checkedNames.indexOf(companyCode)>=0){              
              if(this.unSelectComanyInfoMapInfo.has(companyCode)){
                  let unselectUserList = this.unSelectComanyInfoMapInfo.get(companyCode);
                  if(unselectUserList.length==0){
                      console.log("has companyCode:"+companyCode+" remove:"+this.unSelectComanyInfoMapInfo.size);
                      this.unSelectComanyInfoMapInfo.delete(companyCode); 
                      console.log("after remove:"+this.unSelectComanyInfoMapInfo.size);               
                  }
              }
          }else{//如果都不在选择内
              this.unSelectComanyInfoMapInfo.set(companyCode,[]);
              console.log("unSelectComanyInfoMapInfo set :"+companyCode +" to empty");   
          }
      }));
    },

    getDealerReportService() {
      this.headers = [];
      this.desserts_area = [];
      this.loadingview =true;

      getCompanybyarea(this.msgIDParam,this.areaparam,this.selectTypeValueParam,this.searchContentParam,this.readStatusParam,this.statisticsStatusParam)
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

    /**
     * 构造阅读数据
     */
    composeReadStatisticsData(bodyBean,haveReadCount,readCount){
        if(this.readStatusParam == this.STATUS_READ){
            bodyBean.readingStatisticsCount = haveReadCount;        
        }else{
            bodyBean.readingStatisticsCount = readCount - haveReadCount;        
        }
        bodyBean.readingTotalCount= readCount;
        bodyBean.readingPercentage = roundFun(bodyBean.readingStatisticsCount,bodyBean.readingTotalCount);        
    },

    /**
     * 构造阅读数据
     */
    composeFeedbackData(bodyBean,haveFeedbackCount,feedbackCount){
          bodyBean.replyStatisticsCount = haveFeedbackCount;
          bodyBean.replyTotalCount = feedbackCount;
          bodyBean.replyPercentage = roundFun(bodyBean.replyStatisticsCount,bodyBean.replyTotalCount);        
    },

    /**
     * 组装附件文件数据
     */
    composeAttachFile(attachmentStatistics){
        let attachData = {};
                
        attachData.valueStatisticsCount = attachmentStatistics.haveDownloadCount;
        attachData.valueTotalCount = attachmentStatistics.downloadCount;
        attachData.valuePercentage = roundFun(attachData.valueStatisticsCount,attachData.valueTotalCount);                  
        attachData.attachmentId = attachmentStatistics.attachmentId;
        return attachData;
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

      headerDataArray.push(this.buildHeaderBean("经销商编号", "supnumber"));
      headerDataArray.push(this.buildHeaderBean("经销商名称", "supname"));
      if(this.readStatusParam == this.STATUS_UNREAD){
          headerDataArray.push(this.buildHeaderBean("未读统计", "reading"));
      }else{
          headerDataArray.push(this.buildHeaderBean("已读统计", "reading"));
      }   
      //无需回复
      if(this.feebackBackType!=4){
        headerDataArray.push(this.buildHeaderBean("已回复统计", "reply"));
      }

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
     * 是否过滤掉，不显示此条数据
     */
    isFilterPersonData(supplierData){
        //如果是点击已读、未读这一列进来的
        if(this.statisticsStatusParam == this.STATISTICSSTATUS_TYPE_READ){
          if(this.readStatusParam == this.STATUS_READ){ //查询已读，如果公司下面已读数为0，就不显示
              if(supplierData.haveReadCount==0){
                return true;
              }
            }
            else{ //如果点击的是阅读数一列，并且查询状态是未读，如果公司下面有已读的，就不显示
              if(supplierData.haveReadCount>0){
                return true;
              }
            }
        }

        //如果是从已回复点击进来的，只展示回复数大于0的数据
        if(this.statisticsStatusParam == this.STATISTICSSTATUS_TYPE_REPLY){
           if(supplierData.haveFeedbackCount==0){
            return true;
           }
        }

        return false;
    },

    initBodyData(data) {
      this.checkedArr = [];
      this.checkedNames = [];

      let dataArray = [];      

      data.forEach((supplierData) => {      
        
        if(this.isFilterPersonData(supplierData)){
            return;
        }
            
        let bodyBean = {};
        
        bodyBean.supnumber = supplierData.companyCode;
        bodyBean.supname = supplierData.companyName;
        bodyBean.selected = true;

        this.checkedArr.push(bodyBean.supnumber);
        
        let userInfoList= this.unSelectComanyInfoMapInfo.get(bodyBean.supnumber);
        if(userInfoList==undefined || userInfoList.length>0){
            this.checkedNames.push(bodyBean.supnumber);
        }

        /**
         * 解析已读数据
         */
        this.composeReadStatisticsData(bodyBean,supplierData.haveReadCount,supplierData.count);        

        /**
         * 解析回复数据
         */
        this.composeFeedbackData(bodyBean,supplierData.haveFeedbackCount,supplierData.count);                    
        
        let attachDataList = [];
        let isFilterAttach = false;

        if(supplierData.attachmentReports!=null){
            supplierData.attachmentReports.forEach(
              (attachmentStatistics) => {
                
                console.log("statisticsStatusParam:"+this.statisticsStatusParam+"  attachmentIdParam:"+this.attachmentIdParam+" attachmentId:"+attachmentStatistics.attachmentId);

                if(this.statisticsStatusParam == this.STATISTICSSTATUS_TYPE_ATTACH
                    &&this.attachmentIdParam==attachmentStatistics.attachmentId){//如果点击的是已下载附件回复
                    isFilterAttach = attachmentStatistics.haveDownloadCount == 0;
                }

                console.log("attachmentStatistics:"+JSON.stringify(attachmentStatistics)+" isFilterAttach:"+isFilterAttach);

                let attachData = this.composeAttachFile(attachmentStatistics);

                attachDataList.push(attachData);
              }
            );            
        }

        /**
         * 如果从已下载附件进来，需要将未下载附件的经销商过滤掉
         */
        if(isFilterAttach){
            return;
        }

        bodyBean.attachDataList = attachDataList;

        dataArray.push(bodyBean);

      });

      let totalInfo = this.createTotalElement(dataArray);

      dataArray.unshift(totalInfo);

      return dataArray;
    },

    createTotalElement(dataArray){
        
        let totalinfo = {};  
        let totalAttachmentStatistics = [];    
        let readStatisticsCount = 0,readStatisticsTotalCount = 0;
        let feedbackStatisticsCount = 0, feedbackStatisticsTotalCount = 0;

        dataArray.forEach(bodyBean => {

            readStatisticsCount += bodyBean.readingStatisticsCount;
            readStatisticsTotalCount += bodyBean.readingTotalCount;

            feedbackStatisticsCount += bodyBean.replyStatisticsCount;
            feedbackStatisticsTotalCount += bodyBean.replyTotalCount;

            if(bodyBean.attachDataList!=null){
                 bodyBean.attachDataList.forEach((attachData, index)=>{                    
                    if (totalAttachmentStatistics[index] == undefined) {                  
                        totalAttachmentStatistics[index] = {attachmentId: "",statisticsCount: 0,totalCount: 0};
                    }
                        
                    totalAttachmentStatistics[index].attachmentId = attachData.attachmentId;
                    totalAttachmentStatistics[index].statisticsCount += attachData.valueStatisticsCount;
                    totalAttachmentStatistics[index].totalCount += attachData.valueTotalCount;
               })
            }                     
        });

        totalinfo.supnumber = "-";
        totalinfo.supname = "合计";
        totalinfo.selected = true;

        totalinfo.readingStatisticsCount = readStatisticsCount;
        totalinfo.readingTotalCount = readStatisticsTotalCount;
        totalinfo.readingPercentage = roundFun(readStatisticsCount ,readStatisticsTotalCount);

        totalinfo.replyStatisticsCount = feedbackStatisticsCount;
        totalinfo.replyTotalCount = feedbackStatisticsTotalCount;
        totalinfo.replyPercentage = roundFun(feedbackStatisticsCount,feedbackStatisticsTotalCount);

        totalinfo.attachDataList = [];
        totalAttachmentStatistics.forEach((attachmentStatistics) => {
          let attachData = {};

          attachData.valueStatisticsCount = attachmentStatistics.statisticsCount;
          attachData.valueTotalCount = attachmentStatistics.totalCount;
          attachData.valuePercentage = roundFun(attachmentStatistics.statisticsCount,attachmentStatistics.totalCount);

          totalinfo.attachDataList.push(attachData);
        });

        return totalinfo;
    },

    /**
     * 合计的关闭窗口
     */
    closeParentInAll(unSelectCompanyMap){
       console.log(
        "......closeParentInAll..." +
          " unSelectUsers:" +
          JSON.stringify(unSelectCompanyMap)
      );

      this.checkedArr.forEach(companyCode => {
          if(unSelectCompanyMap!=undefined && unSelectCompanyMap.has(companyCode)){
              let userList = unSelectCompanyMap.get(companyCode);
              this.unSelectComanyInfoMapInfo.set(companyCode,userList);
              if(userList.length==0){//如果全不选
                  this.checkedNames.splice(this.checkedNames.indexOf(companyCode), 1);
              }else{
                  if(this.unSelectComanyInfoMapInfo.has(companyCode)){
                    this.unSelectComanyInfoMapInfo.delete(companyCode);          
                  }
                  if(this.checkedNames.indexOf(companyCode)<0){
                    this.checkedNames.push(companyCode);
                  }                
              } 
          }else{
            this.unSelectComanyInfoMapInfo.delete(companyCode);
            if(this.checkedNames.indexOf(companyCode)<0){
                  this.checkedNames.push(companyCode);
            }
          }
      });

      this.dialog_person = false;
    },

    closeParent(unSelectUsers, companyCode,isNoneSelected) {
      console.log(
        "......closeParent....companyCode" +
          companyCode +
          " unSelectUsers:" +
          JSON.stringify(unSelectUsers)
      );

      this.dialog_person = false;

      //没有选择1个人
      if (isNoneSelected) {
        this.checkedNames.splice(this.checkedNames.indexOf(companyCode), 1);
        this.unSelectComanyInfoMapInfo.set(companyCode,[]);
        console.log("checkedNames splice:" + companyCode);
      }else{//选择了其中几个人
        if(unSelectUsers.length>0){
            this.unSelectComanyInfoMapInfo.set(companyCode,unSelectUsers);
        }else{
          if(this.unSelectComanyInfoMapInfo.has(companyCode)){
            this.unSelectComanyInfoMapInfo.delete(companyCode);          
          }        
        }
        if(this.checkedNames.indexOf(companyCode)<0){
            this.checkedNames.push(companyCode);
        }
      }

      console.log("unSelectCompanyInfo:" + JSON.stringify(this.unSelectComanyInfoMapInfo));
      
    },
    //关闭
    bt_close() {
      console.log("......关闭按钮.......");
      let tmpSelectComanyInfoMapInfo = this.unSelectComanyInfoMapInfo;
      this.$emit("closedChild", tmpSelectComanyInfoMapInfo);
    },
    //当table row click时触发 - 表示全部，此时的传值
    clickRow: function (type,companyCode, attachmentId) {
      console.log("....clickRow....type:" + type + " companyCode:" + companyCode+" attachMentId:"+attachmentId);
      if(companyCode=='-'){
        return;
      }
      this.selectCompanyCode = companyCode == "-" ? "" : companyCode;
      this.selectStatisticsStatus = type;
      this.clickAttachmentId = attachmentId;
      this.dialog_person = true;
    },
    changeAllChecked: function () {
      if (this.checkedAll) {
        this.checkedArr.forEach((info) => {
          this.checkedNames.push(info);
        });

        console.log("checkedArr:" + JSON.stringify(this.checkedArr));
        console.log("checkedNames:" + JSON.stringify(this.checkedNames));
      } else {
        this.checkedNames = [];
      }
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
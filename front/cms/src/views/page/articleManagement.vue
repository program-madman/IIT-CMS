<template>
  <v-container fluid>
    <template>
      <v-row class="pl-4 pt-3 pb-5 pr-4 search-bar">
    <v-col
      cols="12"
      sm="12"
      md="3"
      class="pa-0 pt-4"
      style="display: inline-flex"
    >
      <v-combobox
        style="height: 40px;"
        v-model="selectDept"
        :items="distributeDepartItems"
        label="发布部门"
        outlined
        hide-details="auto"
        dense
        class="pa-0 mr-4 mr-sm-2"
      ></v-combobox>
      <v-combobox
        style="height: 40px;"
        v-model="selectStatus"
        :items="distributeStatusItems"
        label="发布状态"
        outlined
        flat
        hide-details
        dense
        class="pa-0"
      ></v-combobox>
    </v-col>
    <v-col
      class="pa-0 pt-4"
      cols="12"
      sm="12"
      :md="9"
      style="display: inline-flex"
    >
      <v-spacer>
        <v-text-field
          ref="search"
          v-model="search"
          flat
          style="height: 40px; min-width: 140px"
          hide-details
          clearable
          label="按照文章编号，标题，正文内容查找"
          placeholder="按照文章编号，标题，正文内容查找"
          maxlength="70"
          counter="70"
          append-icon="mdi-magnify"
          outlined
          dense
          class="ml-sm-0 pa-0 ml-md-4 mr-2"
        ></v-text-field>
      </v-spacer>

      <v-btn
        height="40"
        class="mr-2"
        depressed
        button
        color="blue"
        min-width="105px"
        dark
        style="z-index:1"
        @click="onSearchArticle"
      >
        <v-icon left> mdi-magnify </v-icon>
              查找
      </v-btn>

      <v-btn
        height="40"
        min-width="105px"
        outlined
        depressed
        dark
        color="#333333"
        style="z-index:1"
        @click="resetSearch"
      >
         <v-icon left> mdi-autorenew </v-icon>
              还原
      </v-btn>
    </v-col>
  </v-row>
  </template>
       <v-data-table
      :loading="loadingData"
      loading-text="加载数据中,请稍后..."
      :headers="headers"
      :items="articleItems"
      class="elevation-1"
      :items-per-page="10"
      hide-default-footer
      :footer-props="{
        'items-per-page-text': '每页显示条数',
        'items-per-page-options': [10],
        pageText: '{0}-{1} of {2}',
        'items-per-page-all-text': 'all',
      }"
    >
      <template v-slot:[`item.title`]="{ item }">
         <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
        <a v-bind="attrs"
          v-on="on" @click="toArticleDetail(item)" style="color: #1867c0"> {{ lenghth(item.title,36) }} </a>
        </template>
        <span>{{item.title}}</span>
         </v-tooltip>
      </template>

      <template v-slot:[`item.readCount`]="{ item }">
        <span blue> {{ item.readCount }}/{{item.receiveUserCount}} </span>
      </template>

       <template v-slot:[`item.feedbackCount`]="{ item }">
        <span> {{ item.feedbackCount }}/{{item.receiveUserCount}} </span>
      </template>
      
      <template v-slot:[`item.msgStatus`]="{ item }">
        <span :style="{ color: getColor(item.msgStatus) }">
          {{ convert(item.msgStatus) }}
        </span>
      </template>
     
      <template v-slot:[`item.actions`]="{ item }">
        <v-col>
        <a :disabled="item.msgStatus === 0" color="#595959FF" style="color: #1867c0;font-size:12px;display: inline-block;"  small @click="toChangeLog(item)" plain> 变更记录 </a>
        <a :disabled="item.msgStatus === 2 || item.msgStatus === 0" color="#595959FF" style="color: #1867c0;font-size:12px;display: inline-block;" class="ml-2" small @click="withdraw(item)" plain> 撤回 </a>
        </v-col>
      </template>

     
      <template v-slot:no-results>
        <p>未找到任何文章，请换个内容查找试试吧</p>
      </template>
      <template v-slot:no-data>
        <p>未找到任何文章，请换个内容查找试试吧</p>
      </template>
      
      <template v-slot:top>
        <v-dialog v-model="withdrawDialog" max-width="400">
          <v-card>
            <v-card-title justify-center>确认要撤回吗</v-card-title>
            <v-card-text
              >文章撤回后将从它的所有观看者列表中消失，确认要撤回吗？</v-card-text
            >
            <v-card-actions>
              <v-btn color="blue darken-1" text @click="withdrawDialog = false">
                关闭
              </v-btn>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="withdrawConfirm()">
                确认
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
    </v-data-table>

        <v-col cols="12">
          <template v-if="articleItems.length > 0">
           <div class="text-center">
            <v-container class="ml-0 mx-auto">
              <v-row>
                <v-col cols="12">
                  <v-container class="max-width">
                    <v-spacer></v-spacer>
                    <v-pagination
                      @input="onPage" 
                      @next="onPage"
                      @prevoius="onPage"
                      v-model="currentPage"
                      class="my-4"
                      :length="totalPage"
                      :total-visible="7"
                    ></v-pagination>
                  </v-container>
                </v-col>
              </v-row>
            </v-container>
          </div>
        </template>      
       </v-col>
  </v-container>
</template>

<script>
import { jumpToDetail,jumpToDetailWithParams } from "@/utils/common.js";
import { getAllList, withdrawMessageByAdmin } from "@/api/getData.js";
import { getMsgStatusDesc,getMsgStatusCodeByName } from "@/utils/articleUtils.js";

export default {
  data: () => ({
    name: "article Management",
    data: null,
    selectDept: "全部",
    distributeDepartItems: ["全部"],
    selectStatus: "全部",
    distributeStatusItems: [
      "全部",
      "已发布",
      "已召回",
      "已失效",
    ],
    search: "",
    headers: [
      {
        text: "标题",
        width:"300px",
        align: "start",
        value: "title",
      },
      { text: "发布部门",width:"160px", value: "creatorCompanyname" },
      { text: "附件", width:"80px",value: "attachmentCount" },
      { text: "阅读",width:"80px", value: "readCount" },
      { text: "回复", width:"80px",value: "feedbackCount" },
      { text: "发布状态",width:"80px", value: "msgStatus" },
      { text: "发布时间", align: "start",width:"180px",value: "lastUpdateTime"},
      { text: "操作", align: "center",width:"120px",value: "actions" },
    ],
    articleItems: [],
    withdrawDialog: false,
    loadingData:false,
    currentPage:1,
    lastPage:1,
    pageSize:10,
    hasNextPage:true,
    reset:true,
    totalage:1,
    searchParam:{},
    withdrawItem: {
      id: 0,
      title: "",
      distributeDepart: "",
      attachment: 0,
      readingNum: 0,
      replyNum: 0,
      distributeStatus: "",
      distributeTime: "",
    },
  }),

  //监视对话框
  watch: {
    dialog(val) {
      val || this.close();
    },

    input(page) {
      alert(page)
    },
    next(page) {
      alert(page)
    },
    previous(page) {
      alert(page)
    },
  },
  //页面第一次加载时,调用初始化方法
  created() {},

  mounted() {
    this.searchParam.pageSize = this.pageSize
    this.searchParam.pageNum = 1;
    this.getAllArticle(this.searchParam);
  },

  methods: {

    getHeader() {
      return "header.title"
    },

    toArticleDetail(item) {
      //草稿就别跳了。跳了肯定出事....
      if(item.msgStatus === 0) {
        return;
      }  
      console.log(">>>>> jump to detail id = "+item.id)
      jumpToDetail(this.$router,item.id)
    },

    toChangeLog(item){ 
      jumpToDetailWithParams(this.$router,item.id,true)
    },

    onPage() {
      console.log(">>> load prepare load page ["+this.currentPage+"] <<< ")
      if(this.lastPage === this.currentPage) {
        console.log(">>>  the two page number are the same, so we abandon load page current page = ["+this.currentPage+"]  last page = ["+this.lastPage+"]<<< ")
        return;
      }else {
        console.log(">>> begin load page ["+this.currentPage+"] <<< ")
        this.lastPage = this.currentPage;
        this.searchParam.pageNum = this.currentPage;
        this.searchParam.pageSize = this.pageSize;
        this.getAllArticle(this.searchParam)
      }       
    },

    convert(status) {
      return getMsgStatusDesc(status)
    },
    
    getColor(msgStatus) {
      if (msgStatus === 1) return "#11BA3E";
      else if (msgStatus === 2) return "#F44336";
      else if (msgStatus === 5) return "#1867C0";
      else if (msgStatus === 4) return "#999999";
      else return "#1867C0";
    },
    
    resetSearch() {
      if(this.reset) {
        console.log(">>>> already had reset <<<<")
        return;
      }
      console.log(">>>>  reset search <<<<")
      this.selectDept = "全部";
      this.selectStatus = "全部";
      this.search = "";
      this.currentPage = 1;
      this.lastPage = 1;
      this.searchParam = {};
      this.reset = true;
      this.getAllArticle()
    },

    onSearchArticle() {
      //点击确定按钮就重新搜一次,并且存储搜索条件
      this.currentPage = 1;
      this.searchParam = {}
      
      if(this.selectDept !== "全部" ) {
        this.searchParam.department = this.selectDept;
      }

      if(this.selectStatus !== "全部" ) {
        this.searchParam.msgStatus = getMsgStatusCodeByName(this.selectStatus)
      }

      if(typeof this.search !== "undefined" && this.search != null && this.search.trim() !== "") {
          this.searchParam.searchContent = this.search;
      }
      //看来不需要做没输入条件的处理
      // if(JSON.stringify(this.searchParam) === "{}") {
      //   console.log(">>> search option has not changed <<< ")
      //   return;
      // }
      this.reset = false;
      this.searchParam.pageNum = 1;
      this.searchParam.pageSize = this.pageSize;
      console.log(">>> on search <<<")
      this.getAllArticle(this.searchParam)
    },

    getAllArticle(param) {
      this.loadingData = true,
      console.log(" >>>>> prepare get all articles >>>>>>>> params >>>>>> : "+JSON.stringify(param));   
        getAllList(param).then((response) => {
          this.loadingData = false
        if((!response && typeof response != "undefined") || response.code != 0 || response.data.messageList.length == 0){
          //需要清空现在的数据,但是搜索条件不建议清空
          this.articleItems = []
          return;
          }
          
        //let articles = [];
        console.debug("response json======>" + JSON.stringify(response));        
        this.articleItems = response.data.messageList;
        this.totalPage = Math.ceil(response.data.total/this.pageSize)
        this.distributeDepartItems = ["全部"];
        this.distributeDepartItems = this.distributeDepartItems.concat(response.data.departments);
      }).catch(e => {
        this.articleItems = [];
        this.loadingData = false;
        console.log("on get article list error ======>"+JSON.stringify(e))
        });
    },

    withdraw(item) {
      this.withdrawItem = Object.assign({}, item);
      this.withdrawDialog = true;
    },

    //关闭撤回对话框
    close() {
      this.withdrawDialog = false;
    },

    lenghth(text, length, suffix = "...") {
      if (!text) return;
      if (text.length <= length) {
        return text;
      }
      return (text + "").substring(0, length) + suffix;
    },

    //撤回确认
    withdrawConfirm() {
      this.withdrawDialog = false;
      withdrawMessageByAdmin(this.withdrawItem.id)
        .then((res) => {
          if ((!res && typeof res != "undefined") || res.code != 0) return;
          console.debug(JSON.stringify(res));
          var i = -1;  
          this.articleItems.some((item, index) => {
            if (item.id == this.withdrawItem.id) {
              i = index;
              item.msgStatus = 2;
              item.distributeStatus = getMsgStatusDesc(item.msgStatus);
            }
          });
          if (i >= 0) {
            this.articleItems = JSON.parse(JSON.stringify(this.articleItems))
          }
        })
        .catch();
    },
  },
};
</script>
<style>
.search-bar .v-input {
  color: #999999ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 14px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
  z-index:2;
  background-color: white;
}
</style>
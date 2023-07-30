<template
>
    <v-card class="mx-auto pa-5 ma-0">
         <v-progress-linear
         v-if="!backData.data"
            indeterminate
            color="blue"
            class="mb-1"
          ></v-progress-linear>
        <div style="border: 1px solid #E4E8EB;" class="pa-0 ma-0" v-if="backData.data">
            <v-row align="center" class="ml-4 mr-0 mt-4 mb-0">
                
                <span class="text-h6 grey--text text--darken-4">
                    {{ backData.data.title }}
                </span>
            </v-row>
            <v-row class="mt-2 ml-4 mr-4 mb-0">
                <span class="text-caption grey--text text--darken-1 mr-6" >
                   From: {{ backData.data.creatorCompanyname }}
                </span>
                <span class="text-caption grey--text text--darken-1">Publish Time:</span>
                <span class="text-caption grey--text text--darken-1">{{ backData.data.releaseTime }}</span>
                <span class="text-caption grey--text text--darken-1 ml-5">Read:100</span>
                <span class="text-caption grey--text text--darken-1 ml-5"> Reply:100</span>
                <span class="text-caption grey--text text--darken-1 ml-5"> Browsed:100</span>
                <button
                  class="article-operator-text ml-5">
                  <v-icon class="article-operator-icon">
                    mdi-thumb-up
                  </v-icon>
                  100
                </button>
                <v-dialog v-model="closeDialog_report" 
                fullscreen hide-overlay transition="dialog-bottom-transition">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn class="text-caption blue--text text--darken-1 ml-5" text x-small @click="reportDialog"
                            v-bind="attrs" v-on="on">
                            {{$t("statisticsReport")}}
                        </v-btn>
                    </template>
                    <ReportView @closedChild="closeParent" :msgIdParam="msgId" :feebackBackType="this.feebackBackType" ></ReportView>
                </v-dialog>
            </v-row>
            <v-row class="ml-4 mt-4 mr-4 mb-0" align="center">
                <span class="text-caption grey--text text--darken-1">{{$t("articleSendTo")}}:</span>
                 <v-chip
                    v-for="(position, index) in backData.data.sendPositions"
                    color="#F0F0F0" 
                    :key="index"
                    small
                    class="text-caption grey--text text--darken-3 ml-2 pl-2 pr-2 ma-1"
                    >
                    {{ position}}
                    </v-chip>
            </v-row>
            
            <v-row  class="ma-0 mt-2 ml-5 mr-5">
                
                <span class="html-content" v-html="backData.data.content"> </span>
                
            </v-row>
        
            <div class="ml-7 mt-16 mr-7 mb-0 pa-0" v-if="commonFiles.length>0 && this.needShowDownloadFile()">
                <v-row  align="center" class="mb-4">
                    <span class="text-body-2 grey--text text--darken-3"> {{$t("commonFile")}} </span>
                    <v-btn small text v-if="!this.isCommonFilesDownloadLoading"                 
                    class="text-body-2 blue--text text--darken-3"
                    @click="downloadCommonFiles"> {{$t("allDownload")}} </v-btn>
                    <v-progress-circular
                        class="ml-8"
                        v-if="isCommonFilesDownloadLoading"
                        :indeterminate="isCommonFilesDownloadLoading"
                        color="primary">
                    </v-progress-circular>
                    <span 
                        v-if="isCommonFilesDownloadLoading"
                        class="text-body-2 grey--text text--darken-3">
                        正在为您打包压缩，请稍后！
                    </span>
                </v-row>
                <v-row  align="center" class="mb-4">
                    <template v-for="(item, hindex) in commonFiles">
                        <div
                        :key="`fc-${hindex}`" 
                        cols="6"
                        class="pa-2 ">
                            <AppendView :apFile="item" :articleId="msgId"></AppendView>
                        </div>
                        
                        <v-responsive 
                            v-if="n === 2" 
                            :key="`fs-${hindex}`" 
                            :cols="6"
                            class="pa-2"></v-responsive>
                    </template>
                </v-row>
            </div>
        </div>
        <!-- 添加文字回复 -->
        <div style="border-left: 1px solid #E4E8EB;border-right: 1px solid #E4E8EB;border-bottom: 1px solid #E4E8EB;"
            class="pa-6 ma-0" >
            <v-row>
                <span class="text-body-2 grey--text text--darken-4">需要您文字回复</span>
            </v-row>
            <div class="d-flex flex-row ma-0 pa-0 mt-8 ml-n3">
                <v-text-field outlined dense class="ma-0" v-model="reportCotent" maxlength="100" counter="100"></v-text-field>
                <v-btn :disabled="reportCotent==''" depressed color="#1867C0FF" class="white--text ma-0 ml-2" width="105" @click="toReportContent">
                    回复
                </v-btn>
            </div>
        </div>
        <!-- 回复信息 -->
        <div style="border-left: 1px solid #E4E8EB;border-right: 1px solid #E4E8EB;border-bottom: 1px solid #E4E8EB;"
            class="pa-6 ma-0" 
            v-if="this.textReply && this.textReply.length > 0 ">
            <v-row class="mt-2 mb-2">
                <span class="text-h8 grey--text text--darken-4"> 回复列表</span>
            </v-row>
            <div class="pa-0 ma-0" v-show="textReply.length > 0">
                <v-card min-height="60" outlined v-for="(item, iindex) in textReply" :key="'info2-'+iindex" class="mt-4 mx-auto">
                    <v-row class="pa-0 ma-0 ml-2 mt-1">
                        <div class="text-body-2">
                            {{item.replyName}}
                        </div>
                        <div class="text-body-2 grey--text text-darken-10 ms-2">
                            {{item.replyCompanyname}}
                        </div>
                        <div class="text-body-2 grey--text text-darken-10 ms-2">
                            {{item.replyPosition}}
                        </div>
                        <v-spacer></v-spacer>
                        <div class="text-body-2 grey--text text-darken-10 mr-2">
                            {{item.createTime}}
                        </div>
                    </v-row>
                    <v-row class="pa-0 ma-0 ml-2">
                        <span class="text-body-2 font-weight-medium"> {{ item.content }}</span>
                    </v-row>   
                </v-card>
            </div>
            <page-loader @onLoadMore="onLoadMore" />
        </div>
        <div ref="changelog" style="overflow:scroll;border-left: 1px solid #E4E8EB;border-right: 1px solid #E4E8EB;border-bottom: 1px solid #E4E8EB;"
            class="pt-1 ma-0 pb-1" >
            <v-row class="ml-4 mt-3">
                <span class="text-body-2 grey--text text--darken-4">变更记录</span>
            </v-row>
            <div class="mt-4 ml-4 mr-4 mb-4 ">
                <div v-for="(item,jindex) in changHistory" :key="jindex">
                    <v-divider v-if="item.divider" :key="jindex" ></v-divider>
                    <v-list-item v-else :key="jindex" dense class="mt-2">
                        <v-row>
                            <span class="text-body-2 grey--text text--darken-2">{{ item.createTime }}</span>
                            <span class="text-body-2 grey--text text--darken-2 ml-2">{{ item.operateUsername }}</span>
                            <span class="text-body-2 grey--text text--darken-2 ml-2">{{ item.operateDepartment }}</span>
                            <span class="text-body-2 grey--text text--darken-4 ml-2">{{ item.operateType }}</span>
                        </v-row>
                    </v-list-item>
                </div>
            </div>
        </div>
        <!-- <v-btn id="to-top-btn" style="position:fixed;top:50%;transform: translateY(-50%);right:0px;display:none;" fab
            dark small color="primary" @click="backtop">
            <v-icon>
                mdi-arrow-up
            </v-icon>
        </v-btn> -->
        <v-snackbar
            v-model="caution"
            :timeout="cautionTimeout"
            :centened="true"
            :width="240"
          >
            {{cautionText}}

            <template v-slot:action="{ attrs }">
              <v-btn color="blue" text v-bind="attrs" @click="caution = false">
                关闭
              </v-btn>
            </template>
          </v-snackbar>
        <confirm ref="confirm" />
    </v-card>
</template>
<script>
    import AppendView from '@/components/appendView.vue'
    import ReportView from '@/components/statisticalReportView.vue'
    import {isNull} from "@/utils/common.js";
    import { showSnackbar } from "@/utils/snackbar.js";
    import store from '@/store';
    import { Throttle } from "@/utils/tools.js";
    import {  
    getArticleLog, 
    uploadFile, 
    //getDetailAppend, 
    //getArticleReportAuthor,
    //getArticleReportViewer,
    getDetailToReport,
    getAllDownloadUrlInADView,
    messageRead} from "@/api/getData.js"
    import { getMsgStatusDesc} from "@/utils/articleUtils.js"
    import {downloadFileByUrl} from "@/utils/download.js"
    import {        
        checkUploadFileType,
        INPUT_FILE_ACCEPT,
        readFileSizeUnit,
    } from "@/utils/filesUtils.js";
    import { aesDecrypt  } from "@/utils/encryption.js";

    export default {
        
        components: {
            AppendView,
            ReportView,
            //fileItem: () => import("./publishArticle/fileOption.vue"),
            confirm: () => import("@/components/base/conform.vue"),
            pageLoader: () => import("./articleList/pageLoader.vue"),
        },
        data: () => ({
            inputFileAccept:INPUT_FILE_ACCEPT,
            loadMore:false,
            currentPage:1,
            caution:false,
            cautionText:"回复消息失败，请稍后再试",
            cautionTimeout:1500,
            backData: {},
            appendBackData: {},
            changHistory: [],
            //文本回复
            textReply:[],
            //附件回复
            attachmentReply:[],
            
            textRule: {
                message: '',
                rules: [v => v.length <= 25 || 'Max 25 characters'],
            },
            //是否是admin用户
            isAdmin: false,
            //关闭dialog
            closeDialog_report: false,
            closeDialog_see: false,
            //当前选中的dialog 默认0 未选择dialog, 1.可见范围，2.统计报告
            nowDialog: 0,
            //文章状态
            msgText: '',
            //普通附件
            feebackBackType:-1,            
            commonFiles: [],
            //回复内容
            reportCotent:"",
            //文章id
            msgId: '',
            //是否从我的发布进入的
            isMyPublish:false,
            //获取的回复信息,先赋默认值。防止渲染出错
            reports:{},
            //定时器
            timeout:null,
            isCommonFilesDownloadLoading:false,
            isDirectionalFilesDownloadLoading:false,
        }),

        watch:{
            radioGroup:function(oldVal,newVal) {
                console.log('radioGroup---'+newVal+" oldVal:"+oldVal+"  current:"+this.radioGroup);
                //未选择投票选项
                if(this.radioGroup==0){
                    this.voteDisable = true;
                }else{
                    this.voteDisable = false;
                }
            }
        },
        beforeRouteUpdate(to,from,next) {
            next();
            this.initialize();
        },
        
        //keepAlive生命周期 多次执行 activated 只执行一次mounted
        mounted() {
            this.initialize()
        },
        beforeDestroy() {
            this.nowDialog = 0;
        },
        deactivated(){
            this.timeout.clearImmediate();
        },
        destroyed() {
            // 销毁事件
            // window.removeEventListener('scroll', this.scrollToTop);
        },
        methods: {
            onLoadMore() {
              if (this.loadMore == true || !this.isMyPublish) return;
              this.loadMore = true;
                let data = {
                    msgId: this.msgId,
                    pageNum:++this.currentPage,
                }
             this.getReports(data)
            },

            reset() {
                this.loadMore = false
                this.currentPage=1
                this.caution
                this.backData={}
                this.appendBackData = {}
                this.changHistory = []
                this.textReply = []
                this.attachmentReply=[]
                this.isAdmin = false;
                this.closeDialog_report = false
                this.closeDialog_see = false
                this.nowDialog = 0
                this.msgText=''
                this.radioGroup=0
                this.feebackBackType=-1       
                this.commonFiles= []
                this.directionalFiles=[]
                this.voteDisable = true
                this.votes=[]
                this.reportCotent=""
                this.msgId= ''
                this.isMyPublish=false
                this.reports={}
                this.uploadFileNormal=[]
                this.timeout=null
                this.isCommonFilesDownloadLoading=false
                this.isDirectionalFilesDownloadLoading=false
            },

            initialize(){
                console.log("[[[[[[[[ ==>"+JSON.stringify(store.state.user.authorization))
                this.reset();
                
                this.isMyPublish = this.$route.query.isMyPublish;
                this.msgId = aesDecrypt(this.$route.query.id);
                //顶部回滚事件监听
                // window.addEventListener('scroll', this.scrollToTop)
                //let that = this;
                
                this.timeout = setTimeout(this.timeOutReport,10000);
                // let data = {
                //     msgId: this.msgId,
                //     pageNum:this.currentPage,
                // }
                //获取文章详情
                this.getDetails(this.msgId)
                //获取回复信息
                this.getComments();
                 let attachment = {"id":1,url:"abc.pdf","downloaded":100,"count":99,"attachmentName":"myAttachment"} 
                 let attachment1 = {"id":1,url:"abc.pdf","downloaded":100,"count":99,"attachmentName":"myAttachment"}  
                 this.commonFiles.push(attachment,attachment1) 
            },
            //获取文章变更历史
            getArticleLog() {
                let that = this;
                getArticleLog(this.msgId).then(res => {
                    console.log(">>>>>>>>>>>获取文章变更历史>>>>>>>>>>>>>>>");
                    console.log(JSON.stringify(res));
                    that.articleChangeData = res;
                    that.articleChangeData.data.list.forEach(item => {
                        item.operateType = getMsgStatusDesc(item.operateType)
                        that.changHistory.push(item);
                        that.changHistory.push({ divider: true });
                    });
                    setTimeout(() => {
                        this.toChangeLog();
                        }, 800);
                }).catch(res => {
                    console.log(res);
                });
            },

             toChangeLog() { 
                 
                let query = this.$route.query;
                console.log("===========>to log params "+JSON.stringify(query));
                if (query == null || typeof query == "undefined") return;
                let needToLog = query.param;
                console.log("===========>to log need to log "+needToLog);
                if (!needToLog || typeof needToLog == "undefined") return;
                this.$refs.changelog.scrollIntoView(true);
                },
            //是否显示文章内容
            needShowContent() {
            //v-if="backData.data &&!isMyPublish && reports && reports.data && reports.data.feedBackType === 0 && backData.data.feedbackStatus !== 1"
                //是已读回执的消息
                if(this.backData.data &&!this.isMyPublish && this.reports && this.reports.data && this.reports.data.feedBackType === 0){
                    if(this.backData.data.feedbackStatus !== 1) {
                        return false;
                    }else {
                        return true;
                    }
                }else {
                    return true;
                } 
            },
            
            /**
             * 是否显示附件下载
             */
            needShowDownloadFile(){

                let feedbackStatus = this.backData && this.backData.data && this.backData.data.feedbackStatus === 1;

                console.log("needShowDownloadFile:"+feedbackStatus+" feebackBackType:"+this.feebackBackType);

                //如果是已读回执类型，并且未回复
                if(!this.isMyPublish&&this.feebackBackType==0&& !feedbackStatus ){
                    return false;
                }else{
                    return true;
                }
            },

            reportDialog() {
                this.nowDialog = 1;
            },
            seeDialog() {
                this.nowDialog = 2;
            },
            //关闭dialog
            closeParent(childState) {
                //console.log("-----------" + this.nowDialog);
                //console.log("-----------" + childState);
                let that = this;
                switch (this.nowDialog) {
                    case 1:
                        that.closeDialog_report = childState;
                        break;
                    case 2:
                        that.closeDialog_see = childState;
                        break;
                    default:
                        break;
                }
            },
            //获取文章信息
            getDetails(msgId){
                console.log(msgId);
                this.backData.data = {"title":"title","content":"aabbccdd","msgStatus":1}
                //获取文章详情
                // getArticleDetail(msgId).then(res => {
                //     console.log(">>>>>>>>>>>获取文章详情>>>>>>>>>>>>>>>");
                //     console.log(JSON.stringify(res));
                //     this.backData = res;
                //     this.msgText = getMsgStatusDesc(this.backData.data.msgStatus);
                //     this.feebackBackType =  this.backData.data.feedbackType ;
                //     //获取文章记录
                //     this.getArticleLog();

                // }).catch(res => {
                //     console.log(res);
                // });
            },
            getComments(data) {
                console.log(data);
                // this.loadMore = true;
                // if(this.isMyPublish) {
                //     this.getAuthorReports(data)
                // }else {
                //     this.getViewerReports(data)
                // }
                let comment = {"replyName":"lgh","replyCompanyname":"replyCompanyname","replyPosition":"student","createTime":"2023-07-22","content":"this is comment content"}
                this.textReply.push(comment)
            },
           getAttachment() {

            // getDetailAppend(data).then(res => {
                //     console.log(">>>>>>>>>>>获取文章附件>>>>>>>>>>>>>>>");
                //     console.log(JSON.stringify(res));
                //     that.appendBackData = res;
                //     that.directionalFiles = new Array();
                //     that.commonFiles = new Array();
                //     if(that.appendBackData && that.appendBackData.data.length > 0){
                //         for(let i=0;i<that.appendBackData.data.length;i++){
                //             var currentFile = that.appendBackData.data[i];
                //             if(!that.isMyPublish){
                //                 currentFile.downloaded = 0;
                //                 currentFile.count = 0;
                //             }
                //             if(currentFile.orientation){
                //                 that.directionalFiles.push(currentFile);
                //             }else{
                //                 that.commonFiles.push(currentFile); 
                //             }
                //         }
                //     }
                //     console.log("commonFiles:"+JSON.stringify(this.commonFiles));
                // });
           },
            onGetReportResult(result) {
                this.reports = result;
                if(isNull(result) || result.code != 0 || isNull(result.data)) {
                    return;
                }
                
                if(!isNull(result.data.contents) && result.data.contents.length > 0) {
                    this.textReply = this.textReply.concat(result.data.contents)  
                    return;
                }

                // if(!isNull(result.data.attachments) && result.data.attachments.length > 0) {
                //     this.attachmentReply = this.attachmentReply.concat(result.data.attachments)
                //     return;
                // }

                // if(this.reports && this.reports.data && this.reports.data.voteInfo){
                //     if(this.reports.data.voteInfo.optionId==undefined){
                //         this.radioGroup = 0;                                  
                //     }else{
                //         this.radioGroup = this.reports.data.voteInfo.optionId;                                  
                //     }
                // }
            },

            downloadCommonFiles(){
                // let that = this;
                //let filename = this.backData.data.title + "-普通附件.zip";
                this.isCommonFilesDownloadLoading = true; 

                getAllDownloadUrlInADView(this.msgId,0)
                    .then((res) => { 
                        if(!isNull(res) && res.code === 0 && !isNull(res.data) && res.data !== "") {
                            downloadFileByUrl(res.data)   
                        }else {
                            showSnackbar('下载失败，请稍候重试');
                        }
                        
                    })
                    .catch(() => {
                        showSnackbar('下载失败，请稍候重试');
                    })
                    .finally(() => {
                        this.isCommonFilesDownloadLoading = false;
                    });    
            },
            downloadDirectionalFiles(){
                // let that = this;
                //let filename = this.backData.data.title + "-定向附件.zip";
                this.isDirectionalFilesDownloadLoading = true; 

                getAllDownloadUrlInADView(this.msgId,1)

                    .then((res) => { 
                         if(!isNull(res) && res.code === 0 && !isNull(res.data) && res.data !== "") {
                            downloadFileByUrl(res.data)   
                        }else {
                            showSnackbar('下载失败，请稍候重试');
                        }
                    })
                    .catch(() => {
                        showSnackbar('下载失败，请稍候重试');
                    })
                    .finally(() => {
                        this.isDirectionalFilesDownloadLoading = false;
                    });
            },            
            //回复内容
            toReportContent:Throttle(
                function(){
                    //二次确认
                    this.$refs.confirm
                    .confirm(
                        "只能回复一次，不能更改，确认回复吗?"
                    )
                    .then(() => {
                        this.toReport(1,this.reportCotent);
                    })
                    .catch(() => {});   
                },
                function(){
                    showSnackbar("您提交的太频繁了，稍等一会试试吧");
                }                
            ),
           
            //回复附件
            toReportFile(){              
                //二次确认
                this.$refs.confirm
                .confirm(
                    "只能回复一次，不能更改，确认回复吗?"
                )
                .then(() => {
                    this.submitReport();
                })
                .catch(() => {});                
            },

            submitReport(){
                let attachmentList = [];
                for(let i=0;i<this.uploadFileNormal.length;i++){
                    let attachment = {};
                    attachment.path = this.uploadFileNormal[i].path;
                    attachment.attachmentSize = this.uploadFileNormal[i].fileSize;
                    attachmentList.push(attachment);
                }
                console.log("save attachment:" + JSON.stringify(attachmentList));
                this.toReport(2,"",attachmentList);
                // let data = {
                //     msgId: this.msgId,
                // }
                //this.getReports(data);
                //this.getDetails(this.msgId)
            },
        
            onDeleteFile(index) {
                console.log("ondelete file:"+index);
                this.uploadFileNormal.splice(index, 1);
            },
            uploadFileN() {
                console.log("uploadFileN");
                if(this.uploadFileNormal.length==1){
                    showSnackbar("每次最多只能上传1个文件");
                    return;
                }
                this.$refs.uploadFileN.dispatchEvent(new MouseEvent("click"));
            },
            fileUpload(event) {
                let upFiles = this.uploadFileNormal;
                let selectedFiles = event.target.files;

                 if (selectedFiles.length >1) {
                    showSnackbar("每次最多只能上传1个文件");
                    return;
                }
                                
                for (let i = 0; i < selectedFiles.length; i++) {
                    let item = selectedFiles[i];

                    if (item.size /1024 /1024 >= 1) {
                        showSnackbar("您有文件上传失败,附件最大限制1M");
                        continue;
                    }
                    //上传文件去重
                    if (upFiles.findIndex((t) => t.text == item.name) >= 0) {
                        continue;
                    }
                    //上传类型判断
                    //上传类型判断
                    let ext = checkUploadFileType(item.name);
                    if (ext == null) {
                    this.showSnack("您上传的文件格式不支持");
                    continue;
                    }
                    let temp = {};
                    temp.text = item.name;
                    temp.fileType = ext;
                    temp.fileSize = readFileSizeUnit(item.size);
                    console.log(temp);
                    //添加上传文件列表
                    
                    upFiles.push(temp);

                    //上传服务器
                    uploadFile(item,null, (progress, fileName) => {
                        this.uploadFileNormal
                        .filter((t) => t.text == fileName)
                        .map((update) => {
                            update.progress = progress;
                        });
                        this.uploadFileNormal = JSON.parse(
                            JSON.stringify(this.uploadFileNormal)
                        );
                    }).then((res) => {
                        console.log("upload" + JSON.stringify(res));
                        if (res.code == 0) {
                            //更新上传信息
                            let files = this.uploadFileNormal;
                             files
                            .filter((t) => (t.text==res.data.attachmentName))
                            .map((update) => (update.path = res.data.url));  

                            console.log("upload file message2:" + JSON.stringify(files));
                            
                        }
                    }).catch(() => {});
                }
            },

            resetUploader(){
                this.$refs.uploadFileN.value="";
            },
            goVoteConfirm(){
                this.$refs.confirm
                .confirm(
                    "只能回复一次，不能更改，确认回复吗?"
                )
                .then(() => {
                    this.goVote();
                })
                .catch(() => {});  
            },
            //回复信息
            toReport(feedbackType,content,attachmentList){
                let reportDatas = {
                    msgId: this.msgId,
                    feedbackType: feedbackType,
                }
                if(content){
                    reportDatas.content = content;
                }
                if(attachmentList){
                    reportDatas.attachmentList = attachmentList;
                }
                console.log(JSON.stringify(reportDatas))
                getDetailToReport(reportDatas).then(res => { 
                    console.log(res); 
                    let data = {
                        msgId: this.msgId,
                    }
                    this.getReports(data);
                    this.getDetails(this.msgId);
                }).catch(e => {
                    console.log("------------>====="+ JSON.stringify(e.response))
                    this.cautionText = "回复消息失败，请稍后再试"
                    let resp = e.response
                    
                    if (resp === null || typeof resp === "undefined") {
                         console.log(">>>>> response is null <<<<<<<<")
                        this.caution = true;
                        return;
                    }

                    if (resp.data === null || typeof resp.data === "undefined") {
                        console.log(">>>>> data is null <<<<<<<<")
                        this.caution = true;
                        return;
                    }
                    
                    let data = resp.data;
                    
                    if (data.code === null || typeof data.code === "undefined" || data.code !== 4) {
                        console.log(">>>>> error <<<<<<<<")
                        this.caution = true;
                        return;
                    }

                    let sensitiveWords = data.data;
                    if (sensitiveWords === null ||typeof sensitiveWords === "undefined" || sensitiveWords.length === 0) {
                        console.log(">>>>> sensitive Words are null <<<<<<<<")
                        this.caution = true;
                        return;
                    }

                     this.$refs.confirm.confirm("您回复的内容包含敏感词，请去掉这些敏感词:" +JSON.stringify(sensitiveWords)
                        ).catch();  
                });
            },
            toMessageRead(){
                let requestDatas = {
                    id: this.msgId,
                }
                messageRead(requestDatas).then(res => {
                    console.log(res); 
                })
            },
            //倒计时，将文章状态变为已读
            timeOutReport() {
                console.log("倒计时结束，把状态置为已读");
                this.toMessageRead();
            }
            
        },
    }
</script>
<style>
.file-item {
  margin-bottom: 1px;
}
.html-content p {
  margin-bottom: 0px;
}
</style>
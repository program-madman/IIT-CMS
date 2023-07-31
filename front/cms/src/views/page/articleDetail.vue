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
                    {{ backData.data.articleTitle }}
                </span>
            </v-row>
            <v-row class="mt-2 ml-4 mr-4 mb-0">
                <span class="text-caption grey--text text--darken-1 mr-6" >
                   From: <span style="color: #1867c0">{{ backData.data.department }}</span>
                </span>

                <span class="text-caption grey--text text--darken-1">Publish Time:<span style="color: #1867c0">{{ backData.data.publishTime }}</span></span>
                <span class="text-caption grey--text text--darken-1 ml-5">Read: <span style="color: #1867c0">{{backData.data.readCount}}</span></span>
                <span class="text-caption grey--text text--darken-1 ml-5"> Reply:<span style="color: #1867c0">{{ backData.data.replyCount }}</span></span>
                <span class="text-caption grey--text text--darken-1 ml-5"> Browsed:<span style="color: #1867c0">{{backData.data.browseCount}}</span></span>
                <span class="text-caption grey--text text--darken-1 ml-5"> Browsed:<span style="color: #1867c0">{{backData.data.likeCount}}</span></span>
            </v-row>
            <v-row class="ml-4 mt-4 mr-4 mb-0" align="center">
                <span class="text-caption grey--text text--darken-1">Article Send To:</span>
                 <v-chip
                    v-for="(value, index) in backData.data.targetDeptName"
                    color="#F0F0F0" 
                    :key="index"
                    small
                    class="text-caption grey--text text--darken-3 ml-2 pl-2 pr-2 ma-1"
                    >
                    {{ value}}
                    </v-chip>
            </v-row>
            
            <v-row  class="ma-0 mt-2 ml-5 mr-5">
                
                <span class="html-content" v-html="backData.data.articleContent"> </span>
                
            </v-row>
        
            <div class="ml-7 mt-16 mr-7 mb-0 pa-0" v-if="backData.data.attachmentList && backData.data.attachmentList.length>0">
                <v-row  align="center" class="mb-4">
                    <span class="text-body-2" style="color: #1867c0"> Attachments </span>
                </v-row>
                <v-row  align="center" class="mb-4">
                    <template v-for="(item, hindex) in backData.data.attachmentList">
                        <div
                        :key="`fc-${hindex}`" 
                        cols="6"
                        class="pa-2 ">
                            <AppendView :apFile="item" :articleId="msgId"></AppendView>
                        </div>
                        
                        <v-responsive 
                            v-if="hindex === 2" 
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
                
            </v-row>
            <div class="d-flex flex-row ma-0 pa-0 mt-8 ml-n3">
                <v-text-field outlined dense class="ma-0" v-model="reportCotent" maxlength="100" counter="100"></v-text-field>
                <v-btn :disabled="reportCotent==''" depressed color="#1867C0FF" class="white--text ma-0 ml-2" width="105" @click="submitComment">
                    Reply
                </v-btn>
            </div>
        </div>
        <!-- 回复信息 -->
        <div style="border-left: 1px solid #E4E8EB;border-right: 1px solid #E4E8EB;border-bottom: 1px solid #E4E8EB;"
            class="pa-6 ma-0" 
            v-if="this.backData.data.commentList && this.backData.data.commentList.length > 0 ">
            <v-row class="mt-2 mb-2">
                <span class="text-h8 grey--text text--darken-4"> Comment List</span>
            </v-row>
            <div class="pa-0 ma-0" v-show="backData.data.commentList.length > 0">
                <v-card min-height="60" outlined v-for="(item, index) in backData.data.commentList" :key="'info2-'+index" class="mt-4 mx-auto">
                    <v-row class="pa-0 ma-0 ml-2 mt-1">
                        <div class="text-body-2">
                            {{item.commenterName}}
                        </div>
                        <div class="text-body-2 grey--text text-darken-10 ms-2">
                            {{item.commenterDept}}
                        </div>
                        <div class="text-body-2 grey--text text-darken-10 ms-2">
                            |  {{item.commenterType}}
                        </div>
                        <v-spacer></v-spacer>
                        <div class="text-body-2 grey--text text-darken-10 mr-2">
                            {{item.commentTime}}
                        </div>
                    </v-row>
                    <v-row class="pa-0 ma-0 ml-2">
                        <span class="text-body-2 font-weight-medium"> {{ item.commentContent }}</span>
                    </v-row>   
                </v-card>
            </div>
        </div>
        <div ref="changelog" style="overflow:scroll;border-left: 1px solid #E4E8EB;border-right: 1px solid #E4E8EB;border-bottom: 1px solid #E4E8EB;"
            class="pt-1 ma-0 pb-1" >
            <v-row class="ml-4 mt-3">
                <span class="text-body-2 grey--text text--darken-4">Change Log</span>
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
        <v-snackbar v-model="commentSuccess" timeout="3000" color="success" top="top"  >
            Successfully posted a comment
            <template v-slot:action>
            <v-btn text color="white" @click="commentSuccess = false">
                Close
            </v-btn>
            </template>
        </v-snackbar>


        <v-snackbar
            v-model="caution"
            :timeout="cautionTimeout"
            :centened="true"
            :width="240"
          >
            {{cautionText}}

            <template v-slot:action="{ attrs }">
              <v-btn color="blue" text v-bind="attrs" @click="caution = false">
                Close
              </v-btn>
            </template>
          </v-snackbar>
        <confirm ref="confirm" />
    </v-card>
</template>
<script>
    import AppendView from '@/components/appendView.vue'
    import {isNull} from "@/utils/common.js";
    import { showSnackbar } from "@/utils/snackbar.js";
    import store from '@/store';
    //import { deepClone } from "@/utils/tools.js";
    import {
    getArticleDetail,  
    publishComment,
    getArticleLog,     
    getAllDownloadUrlInADView,
    messageRead} from "@/api/getData.js"
    import { getMsgStatusDesc} from "@/utils/articleUtils.js"
    import {downloadFileByUrl} from "@/utils/download.js"
    import {        
        INPUT_FILE_ACCEPT,
    } from "@/utils/filesUtils.js";
    import { aesDecrypt  } from "@/utils/encryption.js";

    export default {
        
        components: {
            AppendView,
            //fileItem: () => import("./publishArticle/fileOption.vue"),
            confirm: () => import("@/components/base/conform.vue"),
        },
        data: () => ({
            commentSuccess:false,
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
                this.timeout = setTimeout(this.timeOutReport,10000);
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
                return true;
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
                //获取文章详情
                getArticleDetail(msgId).then(res => {
                    console.log(">>>>>>>>>>>获取文章详情>>>>>>>>>>>>>>>");
                    console.log(JSON.stringify(res));
                    this.backData.data = res.data;
                    this.$forceUpdate();
                }).catch(res => {
                    console.log(res);
                });
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
                 
            submitComment(){
                var comment = {}
                comment.articleId = this.msgId
                comment.content = this.reportCotent
               
                publishComment(comment)
                    .then((res) => { 
                        if(res.code === '200') {
                            this.commentSuccess = true 
                             comment.commenterName = "test"
                             comment.commenterDept = "test dept"
                             comment.commentTime = "2023-07-25 11:58:33"
                            this.backData.data.commentList.unshift(comment)
                            this.reportCotent = ""
                            this.$forceUpdate();
                        }else {
                            showSnackbar('Failed to post comment');
                        }  
                    })
                    .catch(() => {
                        showSnackbar('Failed to post comment');
                    })
                    
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
            

            resetUploader(){
                this.$refs.uploadFileN.value="";
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
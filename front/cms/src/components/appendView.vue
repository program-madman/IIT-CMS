<template>
    <v-sheet class="d-flex justify-start pl-3 pr-3 pt-1 pb-1" height="32" min-width="546" v-ripple rounded
        color="#F5F6F7FF" @click="download(apFile)">
        <v-icon small>
            mdi-paperclip
        </v-icon>
        <v-img max-height="24" max-width="24" :src="showIcon(apFile.url)" class="ml-2">
        </v-img>
        <div class="ml-2" style="display: flex;align-items: center;justify-content: center;flex-direction: column;">
            <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">

            <span v-bind="attrs" v-on="on" class="text-caption d-inline-block text-truncate" style="max-width: 300px;">{{ showFileName(apFile) }}</span>
            </template>
            <span>{{showFileName(apFile)}}</span>
            </v-tooltip>
        
        </div> 
        <v-spacer></v-spacer>
        <div class="mr-2" 
            v-if="apFile.downloaded && apFile.count"
        style="display: flex;align-items: center;justify-content: center;flex-direction: column;">
            <span class="text-caption grey--text text--darken-1">{{$t("downloadNum")}}: {{ showDownloadTxt(apFile.downloaded,apFile.count)
                }}</span></div>
        <div class="mr-2" style="display: flex;align-items: center;justify-content: center;flex-direction: column;">
            <v-progress-circular v-if="this.isRequestDownloadUrl" class="ml-8"  :indeterminate="isRequestDownloadUrl" color="primary"></v-progress-circular>
            <v-btn v-else text class="text-caption blue--text text--darken-3">{{$t("download")}}</v-btn>            
        </div>
    </v-sheet>
</template>
<script>
    import { downloadFileByUrl } from "@/utils/download.js"
    import { getDownloadUrlInADView } from "@/api/getData.js"  
    import { showSnackbar} from "@/utils/snackbar.js";  
    export default {
        name: 'appendview',
        props: {
            apFile: {
                typeof: Object,
                default: function () {
                    return {
                        "id": 0,
                        "url": "",
                        "orientation": "",
                        "downloaded": 0,
                        "count": 0,
                        "attachmentName": ""
                    };
                }
            },
            articleId:{
                typeof: Number,
            }
        },
        data() {
            return {
                //压缩文件夹zip,pdf,jpg,excel,doc,默认文件 
                icons: ['mdi-folder-zip', 'mdi-file-pdf-box', 'mdi-file-jpg-box', 'mdi-file-excel', 'mdi-file-document', 'mdi-application'],
                // 关闭dialog
                closeDialog_down: false,
                //附件下载报告title
                title: '附件下载报告',
                //是否在请求url地址
                isRequestDownloadUrl : false,
            };
        },
        mounted() {
        },
        methods: {
            //根据类型显示对应icon
            showIcon(url) {
                let fileName = url.slice(url.lastIndexOf("/") + 1);
                let type = fileName.slice(fileName.lastIndexOf(".")+1);
                switch (type) {
                    case 'zip':
                    case 'rar':    
                        return require('@/assets/filestyle/rar.png');
                    case 'pdf':
                        return require('@/assets/filestyle/pdf.png');
                    case 'ppt':
                        return require('@/assets/filestyle/ppt.png');                        
                    case 'jpg':
                        return require('@/assets/filestyle/jpg.png');
                    case 'jpeg':
                        return require('@/assets/filestyle/jpeg.png');
                    case 'png':
                        return require('@/assets/filestyle/png.png');
                    case 'gif':
                        return require('@/assets/filestyle/gif.png');    
                    case 'excel':
                    case 'xls':
                    case 'xlsx':        
                        return require('@/assets/filestyle/xls.png');
                    case 'document':
                    case 'doc':
                    case 'docx':        
                        return require('@/assets/filestyle/doc.png');
                    case 'txt':
                        return require('@/assets/filestyle/txt.png');  
                    case 'avi':
                        return require('@/assets/filestyle/avi.png');                         
                    case 'mp3':
                        return require('@/assets/filestyle/mp3.png'); 
                    case 'mp4':
                        return require('@/assets/filestyle/mp4.png');
                    case 'rmvb':
                        return require('@/assets/filestyle/rmvb.png');       
                    case 'html':
                        return require('@/assets/filestyle/html.png');                                                      
                    default:
                        return require('@/assets/filestyle/other.png');
                }
            },
            //文件名
            showFileName(file){
                if(file.attachmentName) {
                    return file.attachmentName
                }else { 
                    return file.url.slice(file.url.lastIndexOf("/") + 1);
                }
                
            },
            //显示下载的人数文字 
            showDownloadTxt(i, total) {
                return i + "/" + total;
            },
            //关闭dialog;
            closeParent(childState) {
                this.closeDialog_down = childState;
            },
            download(apFile){        

                console.log("download apFile:" + JSON.stringify(apFile)+" articleId:"+this.articleId);
                let attachmentId = apFile.attachmentId ? apFile.attachmentId : apFile.id
                this.isRequestDownloadUrl = true;

                getDownloadUrlInADView(this.articleId,attachmentId)
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
            }
            
        },
    };
</script>
<style lang="scss" scoped>
</style>
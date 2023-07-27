<template>
  <v-data-table
    :loading="loadingData"
    calculate-widths="true"
    loading-text="加载模板中,请稍后..."
    :headers="headers"
    :items="templateList"
    class="elevation-1"
    :items-per-page="10"
    :footer-props="{
      'items-per-page-text': '每页显示条数',
      pageText: '{0}-{1} of {2}',
      'items-per-page-all-text': 'all',
    }"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>权限管理</v-toolbar-title>
        <v-btn
          color="primary"
          dark
          class="mb-1 ml-4"
          @click="editAuthority = true"
        >
          创建新模板
        </v-btn>
        
        <edit-authority
          :open="editAuthority"
          :template="template"
          :department="department"
          @closeDialog="editAuthority = false"
          @onEditAuthorityTemplateResult="onEditAuthorityTemplateResult"
        />

        <!-- 提交成功的alert -->    
           <v-alert
              :value="alert"
              :timeout="timeout"
              border="left"
              color="green"
              type="success"
              transition="scale-transition"
            >{{alertMessage}}</v-alert>
            <v-spacer></v-spacer>
        <!--删除dialog-->
        <deldialog ref="deldialog" />
        <!-- 提示 -->
        <v-snackbar
            v-model="caution"
            :timeout="timeout"
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

        <!--提交进度-->
        <v-dialog
              v-model="submiting"
              persistent
              width="300"
            >
              <v-card
                color="primary"
                dark
              >
                <v-card-text>
                  删除权限中,请稍后...
                  <v-progress-linear
                    indeterminate
                    color="white"
                    class="mb-0"
                  ></v-progress-linear>
                </v-card-text>
              </v-card>
           </v-dialog>

      </v-toolbar>
    </template>

    <template v-slot:[`item.templateName`]="{ item }">
       <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
        <span  v-bind="attrs"
          v-on="on"> {{lenghth(item.templateName,12)}} </span>
        </template>
        <span>{{item.templateName}}</span>
         </v-tooltip>
    </template>

    <template v-slot:[`item.oemList`]="{ item }">
     
        <v-chip
          small
          v-for="(dept, index) in item.oemList"
          class="ma-1"
          :key="index"
          color="primary"
          text-color="white"
        >
          {{ dept.companyName}}
        </v-chip>  
    </template>

    <template v-slot:[`item.actions`]="{ item }">
      <v-row>
      <a style="color: #1867c0"  class=" mr-2" @click="editItem(item)" plain > 编辑 </a>  
      <a style="color: #1867c0" class=" mr-4" @click="notifiyUser(item)"> 删除 </a>
      </v-row>    
      </template>
    <template v-slot:no-data>
      <v-subheader dense>还没有权限模板，请添加模板.</v-subheader>
    </template>
  </v-data-table>
</template>

<script>
import { getTemplateList,getDeptListByAdmin,deleteTemplate } from "@/api/getData";
import "@/utils/timeFormate.js";
import {transformationAuthority} from "@/utils/templateUtils.js";
export default {
  name: "authorityManagement",
  components: {
    editAuthority: () => import("./editAuthority"),
    deldialog: () => import("@/components/base/delete.vue"),
  },

  mounted() {
    this.loadingData = true;
    getTemplateList().then((response) => {
      this.loadingData = false;
      console.log("######## tremplate =========> ####### " + JSON.stringify(response));
        if(response.data.length > 0) {
          this.templateList = response.data;
          //把权限字段的menuName名字换成name
           this.templateList.forEach(temp => {
             if(temp !== null && typeof temp !== "undefined") {   
               temp.time = new Date(temp.create_time).format("yyyy-MM-dd hh:mm:ss") 
               transformationAuthority(temp.targetList)
             }    
          });
        }
      }).catch((e) => {
        this.loadingData = false;
        console.log("on load authority error ===>"+e)
      });
    
    //从网络获取部门
     getDeptListByAdmin().then((response) => {
        console.log("######## department list =========> ####### " + JSON.stringify(response));
         if(response.data.length > 0) {
           this.department = response.data
          }
      }).catch();

    //this.department = getDeptList() 
  },

  data() {
    return {
      alert:false,
      alertMessage:"",
      addSuccessText:"添加模板成功",
      updateSuccessText:"更新模板成功",
      deleteSuccessText:"删除模板成功",
      deleteSuccessFailedText:"删除模板失败,请稍后再试...",
      cautionText:"",
      timeout:3000,
      caution:false,
      dialog: false,
      dialogDelete: false,
      editAuthority: false,
      submiting:false,
      loadingData:false,
      headers: [
        {
          text: "权限名称",
          align: "start",
          width: "240",
          value: "templateName",
        },
        { text: "权限描述",width: "300",align: "start",value: "desc" },
        // { text: '包含权限', value: 'authority' },
        { text: "包含部门", align: "start", value: "oemList" },
        { text: "发布时间",width: "180", sortable: false, value: "time" },
        { text: "操作", width: "90",value: "actions", sortable: true },
      ],

      templateList: [],
      template:{},
      department:[],
      //======  分割线=======
      editedIndex: -1,
      editedItem: {
        name: "",
        description: "",
        authority: [],
        roles: "",
      },
      selection: [],
    };
  },

  //加载页面时设置添加和编辑标题
  computed: {

  },

  //监视对话框
  watch: {
    editAuthority() {
      //编辑对话框已关闭。清理数据
      if(this.editAuthority === false) {
        //清理资源。
          this.template = {}
          // alert("result = "+result+" v "+v + "  "+h)
      }
    },
  },

  //页面第一次加载时,调用初始化方法
  created() {
    
  },

  methods: {
    lenghth(text, length, suffix = "...") {
      if (!text) return;
      if (text.length <= length) {
        return text;
      }
      return (text + "").substring(0, length) + suffix;
    },

    notifiyUser(item) {
      this.$refs.deldialog.confirm("确定删除此模板吗?")
        .then(() => this.onDeleteTemplate(item))
        .catch(() => {});      
    },
    
    onDeleteTemplate(item) {
      this.submiting = true;
      deleteTemplate(item).then((response) => {
        this.submiting = false;
        console.log("delete template result ==> " + JSON.stringify(response));
        if(response != null && typeof response.code !== "undefined" && response.code === 0) {
          this.deleteItem(item.templateId)
          this.alert = true;
          this.alertMessage = this.deleteSuccessText;
          setTimeout(() => {this.alert = false }, 1500);
        }else {
        this.submiting = false;
        this.cautionText = this.deleteSuccessFailedText;
        this.caution = true;
        }
      }).catch(e => {
        console.log("on delete error "+e)
        this.submiting = false;
        this.cautionText = this.deleteSuccessFailedText;
        this.caution = true;
      });
    },

    //编辑权限
    editItem(item) {
      this.template = item;
      this.editAuthority=true; 
      console.log(JSON.stringify(item))
    },

    onEditAuthorityTemplateResult(result,type){   
      if(type === "add") {
        result.oemList = result.oemList.filter(v => v !== null)
        result.targetList = result.targetList.filter(v => v !== null)
             this.templateList.unshift(result)
      }else if(type === "update") {
        //1.找到数组里这个元素
        //2.删除这个元素
        //3.在把新的数据插入
        // 有更好的javascript方法吗？
        let delSucc = this.deleteItem(result.templateId)      
        if(delSucc) {
          this.templateList.unshift(result)
        }     
      }
      this.alertMessage = type === "add" ? this.addSuccessText : this.updateSuccessText
      this.alert = true;
      setTimeout(() => {this.alert = false }, 1500);
    },

    deleteItem(targetTemplateId) {
      console.log("delete local template ID : ["+targetTemplateId+"]")
      let index = this.templateList.findIndex(v => v.templateId === targetTemplateId);     
      // for(let i=0; i<this.templateList.length; i++) {
      //   this.templateList[i].templateId === targetTemplateId;
      //   index = i;
      //   break;
      // }
      //alert("finnd index is "+index)
      if(index !== -1) {
          this.templateList.splice(index,1)
          return true;
      }
    },
  },
};
</script>

<style scoped>
</style>
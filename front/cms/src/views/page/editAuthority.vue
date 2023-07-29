<template
sytle="background-color: #ffffff;"
>
  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      v-bind="$attrs"
      v-on="$listeners"
      fullscreen
      hide-overlay
      :persistent="true"
      transition="dialog-bottom-transition"
    >
      <v-toolbar dark color="primary">
        <v-btn icon dark @click="onCancel">
          <v-icon>mdi-close</v-icon>
        </v-btn>
        <v-toolbar-title>编辑权限</v-toolbar-title>
      </v-toolbar>

      <v-container
        class="grey lighten-5"
        style="max-width: 100%;"
      >
        <v-row style="min-width: 100px; max-width: 100%; max-height: 100%;margin-bottom:10px"> 
          <confirm ref="confirm" />

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
          <!-- 加载的dialog-->
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
                  提交请求中,请稍后...
                  <v-progress-linear
                    indeterminate
                    color="white"
                    class="mb-0"
                  ></v-progress-linear>
                </v-card-text>
              </v-card>
           </v-dialog>
          <v-col
            style="min-width: 100px; max-width: 50%; max-height: 100%"
            class="flex-grow-1 flex-shrink-0"
          >
            <v-row>
            <v-subheader
            style="font-size:1.05rem;height: 30px;"
            class="subtitle-6 mt-2"
            v-text="'模板名称'"
          />
            </v-row>

            <v-row>
              <v-text-field
              style="margin-left:10px;margin-right:13px;"
                maxlength="45"
                v-model="templateName"
                label="模板名称"
                placeholder="输入您的模板名称"
                outlined
                dense
                ></v-text-field
              >
            </v-row>
             <v-subheader
            style="font-size:1.05rem;height: 12px;padding: 0;"
            class="subtitle-6"
            v-text="'权限编辑'"
          />
            <v-card
              class="pa-2 mt-2"
              outlined
              tile
              style="height: 316px; overflow: scroll"
            >
              <v-treeview
                v-model="selection"
                :open="openTree"
                :items="authorityMenu"
                selection-type="leaf"
                selectable
                item-disabled="locked"
                return-object       
              >
              </v-treeview>
            </v-card>


            <v-subheader
            style="font-size:1.05rem;height: 30px;padding: 0;"
            class="subtitle-6 mt-1"
            v-text="'模板描述'"
          />

            <v-textarea
              v-model="summary"
              outlined
              auto-grow
              clearable
              maxlength="65"
              clear-icon="mdi-close-circle"
              counter
              :rules="rules"
              name="summary"
              background-color="#ffffff"
              label="请输入权限模板的说明"
              style="height=200px"
            ></v-textarea>
          </v-col>
          <v-col
            cols="6"
            style="max-width: 50%; max-height: 100%;"
            class="flex-grow-0 flex-shrink-1"
          >
            <v-row justify="left"
            >
             <v-subheader
            style="font-size:1.05rem;height: 45px;margin-left:0px"
            class="subtitle-6 mt-0"
            v-text="'包含角色列表'"
          />
            </v-row>

            <v-card
              class="pa-2 mt-3"
              outlined
              tile
              style="height: 240px; max-width: 100%; max-height: 100%"
            >
              <v-chip
                v-for="(dept, index) in withinDepts"
                :key="index"
                class="ma-2"
                close
                color="primary"
                text-color="white"
                @click:close="moveToWait(index)"
              >
                {{ dept.companyName }}
              </v-chip>
            </v-card>
             <v-subheader
            style="font-size:1.05rem;height: 45px;"
            class="subtitle-6"
            v-text="'点击下列部门添加权限'"
          />

            <v-chip
              v-for="(dept, index) in withoutDepts"
              :key="index"
              class="ma-2"
              color="secondary"
              text-color="white"
              @click="moveToSelected(index)"
            >
              {{ dept.companyName }}
              <v-icon right> mdi-plus </v-icon>
            </v-chip>
          </v-col>
        </v-row> 
        <v-footer fixed style="height:65px;background-color: #ffffff; !important" class="footer">
          <v-spacer></v-spacer>
          <v-btn
            class="mr-6"
            min-width="105px"
            outlined
            depressed
            dark
            color="#1867C0FF"
            @click="onCancel"
          >
            取消
          </v-btn>

          <v-btn
            class="mr-6"
            min-width="105px"
            outlined
            depressed
            dark
            color="#1867C0FF"
            @click="onConfirm"
          >
            保存
          </v-btn>
        </v-footer>
      </v-container>

      <!-- <v-card class="pa-6" elevation="0"> </v-card> -->
      
    </v-dialog>
  </v-row>
</template>

<script>
import { getAuthorityMenu,addTemplate,updateTemplate} from "@/api/getData.js";
import "@/utils/timeFormate.js";
import {transformationAuthority} from "@/utils/templateUtils.js";
export default {
  props: {
    open: {
      type: Boolean,
      default: false,
    },
    template: {
      type: Object,
      default: function () {},
    },

    department: {
      type: [],
      default: [],
    },
  },
  components: {
    confirm: () => import("@/components/base/conform.vue"),
  },
  data() {
    return {
      openTree:[],
      submiting: false,
      typeAdd:"add",
      typeUpdate:"update",
      leaveNotify: "您当前有修改的信息未保存，离开当前页面您输入的信息将不会被保存。",
      addErrorText:"添加权限模板失败,请稍后再试...",
      updateErrorText:"更新权限模板失败,请稍后再试...",
      timeout: 2000,
      caution: false,
      //deleteTemplate:false,
      cautionText:"",
      //被授权的部门
      withinDepts: [],
      //待授权的部门
      withoutDepts: [],
      //模板名称
      templateName: "",
      //模板描述
      summary: "",
      //模板描述字符限制
      rules: [(v) => v.length <= 65 || "最多输入65字描述"],
      dialog: this.open,
      //权限列表
      authorityMenu: [],
      //默认选中状态
      selection: [],
      //============ 初始化的值，需要做对比判断，是不是所有字段都更改过 ================
      //被授权的部门
      originalWithinDepts: [],
      //模板名称
      originalTemplateName: "",
      //模板描述
      originalSummary: "",
      //默认选中状态
      originalSelection: [],
    };
  },


  methods: {
  
    notifiyUser(content) {
      this.$refs.confirm
        .confirm(content)
        .then(() => this.close())
        .catch(() => {});      
    },

    close() {
      this.$emit("closeDialog");
    },

    onConfirm() {
     
      let empty = this.checkEmpty()
      if (empty[0]) {
        this.cautionText="您需要填写模板名称"
        this.caution = true;
        return;
      }

      if (empty[1]) {
        this.cautionText="您需要填写模板描述";
        this.caution = true;
        return;
      }

      if (empty[2]) {
        this.cautionText="您需要添加授权部门";
        this.caution = true;
        return;
      }

      if (this.selection.length === 0) {
        this.cautionText="请至少添加一个权限";
        this.caution = true;
        return;
      }

      if(JSON.stringify(this.template) === "{}") {
        //添加一个新的模板
         this.submitTemplate(this.typeAdd); 
      }else {
        if(this.checkChage()) {
          //提交变化
           this.submitTemplate(this.typeUpdate); 
        }else {
          //没有任何改变
          this.cautionText="您没有做任何改变";
          this.caution = true;
        }
      }
    },

    onCancel() { 
      //新创建一个模板，用户可能会放弃，所以我们需要判断下，是不是已经写了部分字段了
      if (JSON.stringify(this.template) === "{}") {
        
        //检查字段是否有变化
        if(this.checkChage()) {
          this.notifiyUser(this.leaveNotify)
          return;
        }
        //没有任何变化，关闭就好
        this.close()
      } else {
        //这是一个编辑行为......检查参数是否有变化
        if(this.checkChage()) {
          this.notifiyUser(this.leaveNotify)
          return;
        }
        //没有任何变化，关闭就好
        this.close() 
      } 
    },

    checkEmpty() {
      let withinDeptsChange = this.withinDepts.length === 0;
      let templateNameChange = this.templateName.trim() === "";
      let summaryChange = JSON.stringify(this.summary) === "null" ? true:this.summary.trim() === "";
      return [templateNameChange,summaryChange,withinDeptsChange] 
    },

    checkChage() {
      if(this.templateName.trim() !== this.originalTemplateName.trim()) {
        console.log("templateName had changed");
        return true;
      }

      if(JSON.stringify(this.summary) === "null" ? "":this.summary.trim() !== this.originalSummary) {
        console.log("summary had changed");
        return true;
      }

      console.log("withindepts length "+this.withinDepts.length + " originalWithinDepts length "+this.originalWithinDepts.length);
      if(this.withinDepts.length !== this.originalWithinDepts.length) {
        console.log("withinDepts length not match");
        return true;
      }
      //检查授权部门是否有变化，不考虑数组顺序进行比较
    let resultDepts = this.originalWithinDepts.concat([])
          .filter( (i) =>!this.withinDepts.find((j) => j.companyCode === i.companyCode)
            ).concat(this.withinDepts.filter( (i) =>!this.withinDepts.find((j) => j.companyCode === i.companyCode)));
    
    if(resultDepts.length > 0) {
        console.log("withinDepts had changed");
        return true;
      }

    console.log("selection length "+this.selection.length + " originalSelection length "+this.originalSelection.length);
      if(this.selection.length !== this.originalSelection.length) {
        console.log("selection length not match");
        return true;
      }

      let resultSelection = this.originalSelection.concat([])
          .filter( (i) =>!this.selection.find((j) => j.id === i.id)
            ).concat(this.selection.filter( (i) =>!this.selection.find((j) => j.id === i.id)));
      //把发生改变的数组内容剔除，只保留没改变的。
      //let result = this.originalSelection.filter((v1) => this.selection.find((v2) => v1.id === v2.id))
      console.log("result = "+JSON.stringify(resultSelection));
      if(resultSelection.length > 0) {
        //已经发生改变
        console.log("selection had changed");
        return true;
      }
       console.log("nothing change");
        return false;
    },
    
    submitTemplate(type) {
        let resultTemplate = {}
        resultTemplate.templateName = this.templateName;
        resultTemplate.desc = this.summary;
        resultTemplate.oemList = this.withinDepts;
        resultTemplate.targetList = this.selection;
        
        //提交到服务器
        this.submiting = true;
        
        if(type == this.typeAdd) {
          this.onAddTemplate(resultTemplate)
        } else if(type == this.typeUpdate) {
           this.onUpdateTemplate(resultTemplate)
        }
    },

    onAddTemplate(newTemplate){
       addTemplate(newTemplate).then((response) => {
          console.log("add template result "+JSON.stringify(response))
          this.submiting = false;   
          if(this.checkResponse(response)){
            let result = response.data;
            result.time = new Date(result.create_time).format("yyyy-MM-dd hh:mm:ss") 
            this.$emit("onEditAuthorityTemplateResult",result,this.typeAdd);
            this.close()
          }else {
            this.onSubmitError(this.addErrorText);
            return;
          }}).catch(e => {
          this.submiting = false;   
          this.onSubmitError(this.addErrorText)
          console.log("add template error "+e)
        });
    },

    onUpdateTemplate(newTemplate){
      newTemplate.templateId = this.template.templateId;
      newTemplate.time = new Date().format("yyyy-MM-dd hh:mm:ss") 
       updateTemplate(newTemplate).then((response) => {
         this.submiting = false; 
         console.log("update template result "+JSON.stringify(response))
          //如果后端接口足够标准，这块是可以使用通用验证标准的
          if(response != null && typeof response.code !== "undefined" && response.code === 0){
            //不用返回值了。。。莫名其妙的接口
            //let result = response.data;
            this.$emit("onEditAuthorityTemplateResult",newTemplate,this.typeUpdate);
            this.close()
          }else {
            this.onSubmitError(this.addErrorText);
            return;
          }}).catch(e => {
            this.submiting = false; 
            this.onSubmitError(this.addErrorText)
            console.log("update template error "+e)
        });
    },
    //因为接口返回值不标准，所以不能通用，等后端好好改一下接口，别各种值乱飞，标准化就好了。
    checkResponse(response) {
      if(response != null && 
              typeof response.code !== "undefined" && 
              response.data != null && 
              typeof response.data !== "undefined") {
        if(response.code != 0) {
          return false;
        }else {
          return true;
        }
       } else {
         return false;
       }    
    },

    onSubmitError(msg) {
      this.cautionText=msg;
      this.caution = true;
    },

    moveToWait(index) {
      for (let i = 0; i < this.withinDepts.length; i++) {
        if (i == index) {
          this.withoutDepts.push(this.withinDepts[i]);
          this.withinDepts.splice(index, 1);
        }
      }
      //change(this.waitForSelectDept, this.selectDept, index);
    },
    moveToSelected(index) {
      for (let i = 0; i < this.withoutDepts.length; i++) {
        if (i == index) {
          this.withinDepts.push(this.withoutDepts[i]);
          this.withoutDepts.splice(index, 1);
        }
      }
      //change(this.selectDept, this.waitForSelectDept, index);
    },

    // change(source, dest, index) {
    //   for (let i = 0; i < source.length; i++) {
    //     if (i == index) {
    //       dest.push(source[i]);
    //       source.splice(index, 1);
    //     }
    //   }
    // },

    onCreateNewTemplate() {
      this.withoutDepts = this.department.concat([]);        
    },

    onEditTemplate() {
      //#################   这是编辑原有模板，需要把原模板数据提出来    ####################
      //初始化检测条件，备份原始数据
      this.templateName = this.template.templateName;
      this.originalTemplateName = this.templateName;
      let desc = this.template.desc;
      this.summary = (desc === null ||  typeof desc === "undefined" || JSON.stringify(desc)) === "null" ? "" : desc;
      this.originalSummary = this.summary;

      //this.authorityMenu = this.template.targetList;
      
      this.withinDepts = this.template.oemList.concat([]);
      this.originalWithinDepts = this.withinDepts.concat([]);
      
      console.log("merge result" + JSON.stringify(this.children));

      //有模板信息，模板里包含了已获得权限的部门。
      //需要提取已经授权的部门
      if (this.department.length > 0) {
        //首先 a.filter((i) => !b.find((j) => j.id === i.id))筛选出a中与b中不同的元素，之后
        //b.filter((i) => !b.find((j) => j.id === i.id))筛选出b中与a中不同的元素，之后concat合并两个结果
        this.withoutDepts = this.department.concat([])
          .filter( (i) =>!this.withinDepts.find((j) => j.companyCode === i.companyCode)
            ).concat(this.withinDepts.filter( (i) =>!this.withinDepts.find((j) => j.companyCode === i.companyCode)));
      }
    }
  },

  watch: {
    open() {
      this.dialog = this.open;
    },
    //打开和关闭
    dialog(isOpen) {
      // on dialog has opened
      if (isOpen) {
        //初始化
        this.withinDepts = [];
        this.withoutDepts = [];
        this.originalWithinDepts = [];
        
        this.summary = "";
        this.templateName = "";
        this.originalTemplateName = ""
        this.originalSummary = ""
        // this.authorityMenu = []
        // this.selection = []
        // this.originalSelection = []
       
      //需要拉取默认权限信息
      //获取权限必须成功，如果失败，则关闭页面
      getAuthorityMenu().then((response) => {
        console.log("######## getAuthorityMenu =========> ####### " + JSON.stringify(response));
        if(response.data != null && typeof response.data !== "undefined") {
          this.authorityMenu = response.data;           
          //把所有子权限合并成一个数组
          let children = [];        
        //#################   新建权限模板    ####################
        if (typeof this.template === "undefined" || JSON.stringify(this.template) === "{}") {
          //把名字进行赋值，因为是创建权限模板，我们直接拉取所有权限即可            
         children = transformationAuthority(this.authorityMenu)
         this.onCreateNewTemplate()
        } else {
          //把名字进行赋值，因为是编辑模板，我们需要把已经选中的权限在赋值上            
          children = transformationAuthority(this.authorityMenu,this.template.targetList)
          this.onEditTemplate()
        }
         //把选中的子权限提出来
          this.selection = children.filter((v) => v.isCheck); 
          this.originalSelection = this.selection.concat([]);

      }
      }).catch(e => {
              console.log("get authority list error "+e)
              this.cautionText = "获取权限列表出现异常，请稍后再试"
              this.caution = true;
              this.close()
              }
            );
      }
    },
  },
};
</script>
<style>
.title{
 color: #666666;
font-family: PingFang SC;
font-weight: medium;
font-size: 14px;
line-height: normal;
letter-spacing: 0px;
text-align: left;

}
</style>

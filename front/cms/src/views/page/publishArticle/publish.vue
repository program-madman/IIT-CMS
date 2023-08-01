<template>
  <v-row justify="center">
    <v-dialog
      :persistent="true"
      v-model="dialog"
      v-bind="$attrs"
      v-on="$listeners"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="close">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>Publish Article</v-toolbar-title>
        </v-toolbar>
        <div class="pl-10 pr-10">
          <span class="label_text mt-6"
            >Audience <span style="color: red"> * </span></span>
          <v-sheet
            class="mt-4 pa-0"
            rounded
            outlined
            min-width="1320"
          >
            <div
              class="d-flex flex-row"
              style="
                background-color: #f5f6f7;
                height: 40px;
                border-bottom: 1px solid #e4e8eb;
              "
            >
              <v-checkbox
                v-model="allDepartment"
                label="To Department"
                hide-details="false"
                class="pl-4"
                style="width: 33%; min-height: 28px; height: 28px"
                @change="selectAllDepartment"
                dense
              >
              </v-checkbox>
              <div style="width: 1px; height: 100%; background: #e4e8eb"></div>
              <v-checkbox
                v-model="allUsers"
                label="To User"
                hide-details="false"
                class="pl-4"
                style="width: 33%"
                @change="selectAllUser"
                dense
              >
              </v-checkbox>
              <div style="width: 1px; height: 100%; background: #e4e8eb"></div>
              <span  dense class="pl-4" style="width: 33%; margin-top: 10px;margin-left: 8px;">Category</span>
              <!-- <v-checkbox
                label="Category"
                hide-details="true"
                class="pl-4"
                style="width: 34%"
                disabled="true"
                dense
              >
              </v-checkbox> -->
            </div>
            <div class="d-flex flex-row pa-0">
              <div
                class="pa-0 ma-0"
                style="
                  height: 446px;
                  width: 33%;
                  border-left: 1px solid #e4e8eb;
                  border-right: 1px solid #e4e8eb;
                "
              >
                <v-virtual-scroll
                  :items="department"
                  height="446"
                  item-height="30"
                  bench="1"
                  class="pa-0 ma-0"
                >
                  <template v-slot:default="{ item, index }">
                    <v-list-item
                      :key="index"
                      style="min-height: 28px; height: 30px"
                    >
                      <v-checkbox
                        class="mt-0"
                        v-model="item.isCheck"
                        :label="item.deptName"
                        hide-details="false"
                        dense
                      ></v-checkbox>
                      <v-spacer></v-spacer>
                    </v-list-item>
                  </template>
                </v-virtual-scroll>
              </div>
              <div
                class="pa-0 ma-0"
                style="
                  height: 446px;
                  width: 33%;
                  border-left: 1px solid #e4e8eb;
                  border-right: 1px solid #e4e8eb;
                "
                >
                <v-virtual-scroll
                    :items="userType"
                    height="446"
                    item-height="30"
                    bench="1"
                    class="pa-0 ma-0"
                  >
                    <template v-slot:default="{ item }">
                      
                    <v-checkbox
                      v-model="item.isCheck"
                      :label="item.typeName"
                      hide-details="false"
                      dense
                      style="width: 197px; min-height: 28px; height: 30px"
                      class="pl-4 mt-0"
                    />
                    </template>
                  </v-virtual-scroll>
              </div>
              
              <div
                class="pa-0 ma-0"
                style="
                  height: 446px;
                  width: 33%;
                  border-left: 1px solid #e4e8eb;
                  border-right: 1px solid #e4e8eb;">
                <v-radio-group
                      v-model="category"
                      height="446"
                      item-height="30"
                      bench="1"
                      class="pa-0 ma-0"
                    >
                  
                    <v-virtual-scroll
                      :items="articlecategory"
                      height="446"
                      item-height="30"
                      bench="1"
                      class="pa-0 ma-0 ml-4 mt-4"
                    >
                  
                    <template v-slot:default="{ item }">
                    <v-radio
                        :key="item.categoryName"
                        :label=item.categoryName
                        :value=item.categoryName
                        ></v-radio>
                      </template>
                      
                  </v-virtual-scroll>
                </v-radio-group>
              </div>
            </div>
          </v-sheet>        
          <div style="height: 128px; display: block"></div>
        </div>
      </v-card>
      <v-footer fixed padless style="height: 64px; background: #f5f6f7ff">
        <v-spacer></v-spacer>
        <v-btn
          class="mr-6"
          min-width="105px"
          outlined
          depressed
          dark
          color="#1867C0FF"
          @click="close"
        >
          Edit Article
        </v-btn>
        <v-btn
          class="mr-8"
          min-width="105px"
          depressed
          dark
          color="blue"
          @click="publish"
        >
          Confirm
        </v-btn>
      </v-footer>
      <confirm ref="confirm" />
      <alert-time ref="alerTime" />
      <loading-alert :open="uploading" value="Publishing in progress, please wait" />
    </v-dialog>
  </v-row>
</template>
<script>
import {
  publishArticle,
  getAllDepartments,
  getAllCategorys,
  getAllUserTypes,
} from "@/api/getData.js";
import "@/utils/timeFormate.js";
import { EventBus } from "@/utils/event-bus";
import { showSnackbar, closeSnackbar } from "@/utils/snackbar.js";
import { Throttle, deepClone } from "@/utils/tools.js";
export default {
  props: {
    open: {
      type: Boolean,
      default: false,
    },
    id: {
      type: Number,
      default: null,
    },
    title: {
      type: String,
      default: "",
    },
    content: {
      type: String,
      default: "",
    },
  },
  components: {
    confirm: () => import("./publishConfirm.vue"),
    alertTime: () => import("@/components/base/alertTime"),
    loadingAlert: () => import("@/components/base/loadingAlert"),
  },
  data() {
    return {
      dialog: this.open,
      msgNumber: null,
      releaseTime: "",
      effectiveTime: "",
      invalidTime: "",
      feedbackType: 4,
      allDepartment:false,
      allUsers:false,
      category:null,
      uploading: false,
      articlecategory: [],
      userType: [],
      department:[]
    };
  },

  mounted() {
    this.initDepartment();
    this.initCategory();
    this.initUserType();
  },

  methods: {
    resetPublishTime() {
      this.releaseTime = "";
    },

    resetPolicyTime() {
      this.effectiveTime = "";
      this.invalidTime = "";
    },

    close() {
      this.$emit("closeDialog");
      closeSnackbar();
    },

    initDepartment() {
      getAllDepartments()
        .then((res) => {
          this.department = res.data
        })
        .catch((res) => {
          console.log(res);
          
        })
    },
    
    initCategory() {
      
      getAllCategorys()
        .then((res) => {
          this.articlecategory = res.data
          this.category = this.articlecategory[0].categoryName
        }).catch((res) => {
          console.log(res);
        })
    },

    initUserType() {
      getAllUserTypes()
        .then((res) => {
          this.userType = res.data
        })
        .catch((res) => {
          console.log(res);
          
        })
    },

    handleSelectionChange(newCategory) {
      alert(newCategory)
    },

    selectAllDepartment() {
      this.department.forEach((item) => {
        item.isCheck = this.allDepartment;
      });

      this.department = deepClone(this.department);
    },

    selectAllUser() {
      this.userType.forEach((item) => {
        item.isCheck = this.allUsers;
      });
      this.userType = deepClone(this.userType);
    },

    publish: Throttle(
      function () {
        this.publishArticle();
      },
      function () {
        showSnackbar("submitted too frequently. try it later");
      }
    ),

    publishArticle() {
      var article = {}
      article.title = this.title
      article.content = this.content
      article.categoryName = this.category
      article.targetDeptList = []
      article.userTypeList = []
      article.attachmentList = []
      

      this.department.forEach((item) => {
        if (item.isCheck) {
          item.departmentId = item.deptId
          item.departmentName = item.deptName
          article.targetDeptList.push(item);
        }
      })

      this.userType.forEach((item) => {
        if (item.isCheck) {
          article.userTypeList.push(item.typeName);
        }
      })

      if (article.targetDeptList.length == 0) {
          showSnackbar("please select department");
          return;
      }

      if (article.userType == 0) {
          showSnackbar("please select user");
          return;
      }
  
      this.$refs.confirm
        .confirm(article)
        .then(() => {
          this.uploading = true;  
          publishArticle(article)
            .then((res) => {
              console.debug("publish message:" + JSON.stringify(res));
              if (res.code == 200) {
                this.$refs.alerTime
                  .alert("The article has been successfully submitted and will redirect you to the homepage of the article.", 10)
                  .then(() => EventBus.$emit("publishSucessful"));
              } else {
                showSnackbar("Publishing failed. Please try again later");
              }
            })
            .catch(() => {
              showSnackbar("Publishing failed. Please try again later");
            })
            .finally(() => {
              this.uploading = false;
            });
        })
        .catch(()=> (console.debug("Cancel")));

    },
   
  },
  watch: {
    open() {
      console.log("===========> on open change" + this.open);
      this.dialog = this.open;
      if (this.open) {
        this.caculateDirectFileDelear();
      }
    },
    voteEnable() {
      if (this.voteEnable) {
        this.feedbackType = 3;
      } else {
        this.feedbackType = 4;
      }
    },
  },
  computed: {
    effectiveTimeMax() {
      return new Date(this.invalidTime);
    },
    invalidTimeMin() {
      return new Date(Date.parse(this.effectiveTime));
    },
  },
};
</script>
<style>
.label_text {
  color: #666666ff;
  display: block;
  font-family: PingFang SC;
  font-weight: medium;
  font-size: 14px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}
.replay-radio .v-label {
  color: #666666ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 14px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}

.replay-radio .v-icon.v-icon {
  font-size: 14px;
}
.replay-radio .v-icon__svg,
.replay-radio .v-input--selection-controls__input {
  width: 14px;
  height: 14px;
}

.time_input {
  width: 200px;
}
.time_input .v-input__slot {
  padding-right: 0px !important;
}
.dealer-lab {
  width: 18px;
  height: 18px;
  border-radius: 2px;
  background: #f0f0f0;
  border: 1px solid #e4e8eb;
  color: #9e9e9e;
  font-family: PingFang SC;
  font-weight: medium;
  font-size: 12px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: center;
}
</style>
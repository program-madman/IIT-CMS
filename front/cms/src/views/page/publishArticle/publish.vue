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
                        :label="item.departmentName"
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
                  border-right: 1px solid #e4e8eb;
                "
              >
              <v-radio-group
                    :v-model="category"
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
                      :key="item.typeId"
                      :label=item.typeName
                      :value=item.typeId
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
      <loading-alert :open="uploading" value="发布中，请稍等..." />
    </v-dialog>
  </v-row>
</template>
<script>
import {
  publishMessage,
  getOemDepList,
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
    voteEnable: {
      type: Boolean,
      default: false,
    },
    dealer: {
      type: [Object, Array, String],
      default: function () {
        return [];
      },
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
      ch_delear_disable: false,
      ch_delear: false,
      ch_area_supp: [],
      ch_com_supp: [],
      ch_delear_select: [],
      ch_delear_deplist_all: false,
      ch_delear_deplist: [],
      ch_oem: false,
      ch_oem_deplist: [],

          articlecategory: [
                  {
                    "typeId":1,
                    "typeName":"News"
                  },
                  {
                    "typeId":2,
                    "typeName":"Deep Learning"
                  },
                  {
                    "typeId":3,
                    "typeName":"Database"
                  },

                ],

                userType: [
                  {
                    "typeId":1,
                    "typeName":"Student"
                  },
                  {
                    "typeId":2,
                    "typeName":"Professor"
                  },
                  {
                    "typeId":3,
                    "typeName":"Office Assisant"
                  },

                ],
                department:[
                  {
                      "departmentName": "Computer Science",
                      "departmentId": "1",
                      
                  },
                  {
                      "departmentName": "Computer Engineering",
                      "departmentId": "5",
                     
                  },
                  {
                      "departmentName": "Business and Finance",
                      "departmentId": "3",
                      
                  },
                  {
                      "departmentName": "Medicine and Health Sciences",
                      "departmentId": "8",
                     
                  },
                  {
                      "departmentName": "Psychology",
                      "departmentId": "2",
                      
                  },
                  {
                      "departmentName": "Engineering",
                      "departmentId": "4",
                      
                  },
                  {
                      "departmentName": "Economics",
                      "departmentId": "6",
                     
                  },
                  {
                      "departmentName": "Environmental Science",
                      "departmentId": "7",
                      
                  }
              ]
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
      
    },
    
    initCategory() {
      this.category = this.articlecategory[0]
    },

    initUserType() {

    },

    selectAllDepartment() {
      this.department.forEach((item) => {
        item.isCheck = this.allDepartment;;
      });

      this.department = deepClone(this.department);
    },

    selectAllUser() {
      this.userType.forEach((item) => {
        item.isCheck = this.allUsers;;
      });
      this.userType = deepClone(this.userType);
    },

  
    //选择全部
    selectAllDelear() {
      var isSelected = this.ch_delear;
      this.ch_area_supp.forEach((item) => {
        item.isCheck = isSelected;
      });

      this.ch_com_supp.forEach((item) => {
        item.isCheck = isSelected;
      });

      this.ch_delear_select.forEach((item) => {
        item.isCheck = isSelected;
      });
      this.ch_delear_select = deepClone(this.ch_delear_select);
    },
    selectAllDeplist() {
      var isSelected = this.ch_delear_deplist_all;
      this.ch_delear_deplist.forEach((item) => {
        item.isCheck = isSelected;
      });

      this.ch_delear_deplist = deepClone(this.ch_delear_deplist);
    },
    selectAllOem() {
      var isSelected = this.ch_oem;
      this.ch_oem_deplist.forEach((item) => {
        item.isCheck = isSelected;
      });
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
      article.targetDeptList = []
      article.userTypeList = []
      this.department.forEach((item) => {
        if (item.isCheck) {
          article.targetDeptList.push(item);
        }
      })

      this.userType.forEach((item) => {
        if (item.isCheck) {
          article.userTypeList.push(item);
        }
      })

      article.categoryName = this.category.typeName

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
          publishMessage(article)
            .then((res) => {
              console.debug("publish message:" + JSON.stringify(res));
              if (res.code == 0) {
                this.$refs.alerTime
                  .alert("文章提交成功，将为您跳转文章首页。", 10)
                  .then(() => EventBus.$emit("publishSucessful"));
              } else {
                showSnackbar("发布失败,请稍后重试");
              }
            })
            .catch(() => {
              showSnackbar("发布失败,请稍后重试");
            })
            .finally(() => {
              this.uploading = false;
            });
        })
        .catch(()=> (console.debug("取消")));

    },

    publishImp() {
      
      this.$refs.confirm
        .confirm(dealers)
        .then(() => {
          this.uploading = true;
          publishMessage(data)
            .then((res) => {
              console.debug("publish message:" + JSON.stringify(res));
              if (res.code == 0) {
                this.$refs.alerTime
                  .alert("文章提交成功，将为您跳转文章首页。", 10)
                  .then(() => EventBus.$emit("publishSucessful"));
              } else {
                showSnackbar("发布失败,请稍后重试");
              }
            })
            .catch(() => {
              showSnackbar("发布失败,请稍后重试");
            })
            .finally(() => {
              this.uploading = false;
            });
        })
        .catch(()=> (console.debug("取消")));
    },
    caculateDirectFileDelear() {
      console.debug("dealer ==========>" + JSON.stringify(this.dealer));
      if (this.ch_delear_select.length == 0) {
        return;
      }
      if (this.dealer.length == 0) {
        this.ch_delear_disable = false;
        this.ch_area_supp.forEach((it) => {
          it.disable = false;
        });
        this.ch_area_supp = deepClone(this.ch_area_supp);

        this.ch_com_supp.forEach((it) => {
          it.disable = false;
        });
        this.ch_com_supp = deepClone(this.ch_com_supp);

        this.ch_delear_select.forEach((it) => {
          it.disable = false;
        });
      } else {
        this.ch_delear_disable = true;
        this.ch_delear = true;
        this.dealer.forEach((code) => {
          var index = this.ch_delear_select.findIndex(
            (dealer) => dealer.companyCode === code
          );
          if (index < 0) return;
          var com = this.ch_delear_select[index];
          com.disable = true;
          com.isCheck = true;

          var areaIndex = this.ch_area_supp.findIndex(
            (it) => com.area == it.lable
          );
          if (areaIndex < 0) return;
          this.ch_area_supp[areaIndex].disable = true;
          this.ch_area_supp[areaIndex].isCheck = true;
          var typeIndex = this.ch_com_supp.findIndex(
            (it) => com.dealerType == it.type
          );
          if (typeIndex < 0) return;
          this.ch_com_supp[typeIndex].disable = true;
          this.ch_com_supp[typeIndex].isCheck = true;
        });
      }
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
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
                v-model="ch_delear"
                :disabled="ch_delear_disable"
                label="To Department"
                hide-details="false"
                class="pl-4"
                style="width: 33%; min-height: 28px; height: 28px"
                @change="selectAllDelear()"
                dense
              >
              </v-checkbox>
              <div style="width: 1px; height: 100%; background: #e4e8eb"></div>
              <v-checkbox
                v-model="ch_delear_deplist_all"
                label="To User"
                hide-details="false"
                class="pl-4"
                style="width: 33%"
                @change="selectAllDeplist()"
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
                  :items="ch_delear_select"
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
                        :label="item.companyName"
                        :disabled="item.disable"
                        hide-details="false"
                        @change="onDealerSelectChange(item)"
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
                    :items="ch_delear_deplist"
                    height="446"
                    item-height="30"
                    bench="1"
                    class="pa-0 ma-0"
                  >
                    <template v-slot:default="{ item }">
                      
                    <v-checkbox
                      v-model="item.isCheck"
                      :label="item.value"
                      hide-details="false"
                      :disabled="item.isable"
                      @change="onDealerDeplistChange(item)"
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
                    :v-model="ch_delear_deplist"
                    height="446"
                    item-height="30"
                    bench="1"
                    class="pa-0 ma-0"
                  >
                    
                      <v-radio
                      v-for="item in ch_delear_deplist"
                      :key="item.typeId"
                      :label=item.item.typeName
                      :value=item.item.typeId
                      ></v-radio>
                   
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
                userTypeMoke: [
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
                mokeData:[
                  {
                      "companyName": "Computer Science",
                      "companyCode": "1",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  },
                  {
                      "companyName": "Computer Engineering",
                      "companyCode": "5",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  },
                  {
                      "companyName": "Business and Finance",
                      "companyCode": "3",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  },
                  {
                      "companyName": "Medicine and Health Sciences",
                      "companyCode": "8",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  },
                  {
                      "companyName": "Psychology",
                      "companyCode": "2",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  },
                  {
                      "companyName": "Engineering",
                      "companyCode": "4",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  },
                  {
                      "companyName": "Economics",
                      "companyCode": "6",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  },
                  {
                      "companyName": "Environmental Science",
                      "companyCode": "7",
                      "companyType": null,
                      "dealerType": null,
                      "area": null,
                      "positions": null,
                      "selected": null,
                      "readCount": null,
                      "feedbackCount": null,
                      "receiveUserCount": null,
                      "attachmentReplyCount": null
                  }
              ]
    };
  },

  mounted() {
    this.initDealerList();
    this.initDealerDep();
    this.initOemDep();
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
    initDealerList() {
          //let data = res.data;
          let data = this.mokeData
          if (!data || typeof data == "undefined" || data.length == 0) {
            return;
          }
          this.ch_delear_select = [];
          data.forEach((item, index) => {
            let temp = {
              id: index,
              isCheck: false,
              disable: false,
              companyName: item.companyName,
              companyCode: item.companyCode,
              companyType: item.companyType,
              dealerType: item.dealerType,
              area: item.area,
              arealab: item.area == null ? null : item.area.substring(0, 1),
            };
            this.ch_delear_select.push(temp);
          });
          this.ch_delear_select_view = deepClone(this.ch_delear_select);
      // getDealerList()
      //   .then((res) => {
      //     //console.debug(JSON.stringify(res));
      //     if (!res || typeof res == "undefined" || res.code != 0) {
      //       return;
      //     }

      //     //let data = res.data;
      //     let data = this.mokeData
      //     if (!data || typeof data == "undefined" || data.length == 0) {
      //       return;
      //     }
      //     var areaSet = new Set();
      //     var dealerTypeSet = new Set();
      //     this.ch_delear_select = [];
      //     data.forEach((item, index) => {
      //       let area = item.area;
      //       if (area != null && typeof area != undefined && area != "") {
      //         areaSet.add(area);
      //       }
      //       let type = item.dealerType;
      //       if (type != null && typeof type != undefined) {
      //         if (type == 0) type += 3;
      //         dealerTypeSet.add(type);
      //       }

      //       let temp = {
      //         id: index,
      //         isCheck: false,
      //         disable: false,
      //         companyName: item.companyName,
      //         companyCode: item.companyCode,
      //         companyType: item.companyType,
      //         dealerType: item.dealerType,
      //         area: item.area,
      //         arealab: item.area == null ? null : item.area.substring(0, 1),
      //       };
      //       this.ch_delear_select.push(temp);
      //     });
      //     this.ch_delear_select_view = deepClone(this.ch_delear_select);

      //     this.ch_area_supp = [];
      //     Array.from(areaSet).forEach((item, index) => {
      //       let temp = {
      //         id: index,
      //         isCheck: false,
      //         lable: item,
      //       };
      //       this.ch_area_supp.push(temp);
      //     });

      //     this.ch_com_supp = [];

      //     Array.from(dealerTypeSet)
      //       .sort()
      //       .forEach((item, index) => {
      //         let temp = {
      //           id: index,
      //           isCheck: false,
      //           lable: this.getDealerTypeDesc(item),
      //           type: item % 3,
      //         };
      //         this.ch_com_supp.push(temp);
      //       });
      //   })
      //   .catch();
    },

    initDealerDep() {
      this.userTypeMoke.forEach(item => {
        let temp = {
              id: item.typeId,
              isCheck: false,
              value: item.typeName,
            };
            this.ch_delear_deplist.push(temp);
      }) 
      // getDealerDepList()
      //   .then((res) => {
      //     if (!res || typeof res == "undefined" || res.code != 0) {
      //       return;
      //     }

      //     let data = res.data;
      //     if (!data || typeof data == "undefined" || data.length == 0) {
      //       return;
      //     }
      //     this.ch_delear_deplist = [];
      //     Object.keys(data).forEach((key) => {
      //       let temp = {
      //         id: key,
      //         isCheck: false,
      //         value: data[key],
      //       };
      //       this.ch_delear_deplist.push(temp);
      //     });
      //   })
      //   .catch();
    },
    initOemDep() {
      getOemDepList()
        .then((res) => {
          if (!res || typeof res == "undefined" || res.code != 0) {
            return;
          }

          let data = res.data;
          if (!data || typeof data == "undefined" || data.length == 0) {
            return;
          }

          this.ch_oem_deplist = [];
          data.forEach((item, index) => {
            let temp = {
              id: index,
              isCheck: false,
              companyName: item.companyName,
              companyCode: item.companyCode,
              companyType: item.companyType,
              dealerType: item.dealerType,
              area: item.area,
            };
            this.ch_oem_deplist.push(temp);
          });
          console.log(JSON.stringify(this.ch_oem_deplist));
        })
        .catch();
    },
    getDealerTypeDesc(type) {
      switch (type) {
        case 1:
          return "4s店";
        case 2:
          return "二级网点";
        case 3:
          return "独立售后";
        default:
          return "未分类";
      }
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

    onAreaSelectChange(item) {
      if (!item.isCheck) {
        this.ch_delear_select.forEach((it) => {
          if (it.area == item.lable) {
            it.isCheck = false;
          }
        });
        this.setDealerUnCheckedIfNeed();
      } else {
        this.ch_delear = true;
        let selecType = [];
        this.ch_com_supp.forEach((it) => {
          if (it.isCheck) {
            selecType.push(it.type);
          }
        });
        if (selecType.length == 0) return;
        this.ch_delear_select.forEach((it) => {
          var typeSelected = selecType.indexOf(it.dealerType) >= 0;
          if (typeSelected && it.area == item.lable) {
            it.isCheck = true;
          }
        });
      }
    },
    onTypeSelectChange(item) {
      if (!item.isCheck) {
        this.ch_delear_select.forEach((it) => {
          if (it.dealerType == item.type) {
            it.isCheck = false;
          }
        });
        this.setDealerUnCheckedIfNeed();
      } else {
        this.ch_delear = true;
        let selectArea = [];
        this.ch_area_supp.forEach((it) => {
          if (it.isCheck) {
            selectArea.push(it.lable);
          }
        });

        if (selectArea.length == 0) return;
        this.ch_delear_select.forEach((it) => {
          var areaSelected = selectArea.indexOf(it.area) >= 0;
          if (areaSelected && it.dealerType == item.type) {
            it.isCheck = true;
          }
        });
      }
    },
    onDealerSelectChange(item) {
      if (item.isCheck) {
        this.ch_delear = true;
        this.ch_com_supp.forEach((it) => {
          if (it.type == item.dealerType) {
            it.isCheck = true;
          }
        });
        this.ch_area_supp.forEach((it) => {
          if (it.lable == item.area) {
            it.isCheck = true;
          }
        });
      } else {
        // 只刷新当前条目相关
        // if(this.ch_delear_select.findIndex((it)=> it.isCheck && it.dealerType == item.dealerType) < 0) {
        //   this.ch_com_supp.forEach((it) => it.type == item.dealerType && (it.isCheck = false))
        //   this.setDealerUnCheckedIfNeed();
        // }

        // if(this.ch_delear_select.findIndex((it) => it.isCheck && it.area == item.area)<0){
        //   this.ch_area_supp.forEach((it) => it.lable == item.area && (it.isCheck = false))
        //   this.setDealerUnCheckedIfNeed();
        // }
        // 全部刷新
        let areaSet = new Set();
        let dealerTypeSet = new Set();
        this.ch_delear_select.forEach((it) => {
          if (it.isCheck) {
            areaSet.add(it.area);
            dealerTypeSet.add(it.dealerType);
          }
        });
        let area = Array.from(areaSet);
        let dealerType = Array.from(dealerTypeSet);
        this.ch_com_supp.forEach(
          (it) => dealerType.indexOf(it.type) < 0 && (it.isCheck = false)
        );
        this.ch_area_supp.forEach(
          (it) => area.indexOf(it.lable) < 0 && (it.isCheck = false)
        );
        this.setDealerUnCheckedIfNeed();
      }
    },
    setDealerUnCheckedIfNeed() {
      this.ch_com_supp.findIndex((it) => it.isCheck) < 0 &&
        this.ch_area_supp.findIndex((it) => it.isCheck) < 0 &&
        (this.ch_delear = false);
    },
    onDealerDeplistChange(item) {
      if (item.isCheck) {
        this.ch_delear_deplist_all = true;
      } else if (this.ch_delear_deplist.findIndex((it) => it.isCheck) < 0) {
        this.ch_delear_deplist_all = false;
      }
    },
    onOemSelectChange(item) {
      if (item.isCheck) {
        this.ch_oem = true;
      } else if (
        this.ch_oem_deplist.findIndex((it) => it.isCheck == true) < 0
      ) {
        this.ch_oem = false;
      }
    },

    publish: Throttle(
      function () {
        this.publishImp();
      },
      function () {
        showSnackbar("您提交的太频繁了，稍等一会试试吧");
      }
    ),

    publishImp() {
      if (!/^[A-Za-z0-9]*$/.test(this.msgNumber)) {
        showSnackbar("文章编号请输入20位数字与字母组合（选填）");
        return;
      }
      let dealers = [];
      this.ch_delear_select.forEach((item) => {
        if (item.isCheck) {
          let temp = {
            companyName: item.companyName,
            companyCode: item.companyCode,
            companyType: item.companyType,
            dealerType: item.dealerType,
            area: item.area,
          };
          dealers.push(temp);
        }
      });

      let dep = Object.create(null);

      if (dealers.length > 0) {
        let hasdep = false;
        this.ch_delear_deplist.forEach((item) => {
          if (item.isCheck) {
            dep[item.id] = item.value;
            hasdep = true;
          }
        });
        console.log(dep)
        if (!hasdep) {
          showSnackbar("请选择发布职位后重试");
          return;
        }
      }

      this.ch_oem_deplist.forEach((item) => {
        if (item.isCheck) {
          let temp = {
            companyName: item.companyName,
            companyCode: item.companyCode,
            companyType: item.companyType,
            dealerType: item.dealerType,
            area: item.area,
          };
          dealers.push(temp);
        }
      });
      if (dealers.length == 0) {
        showSnackbar("未选择发布对象,请选择发布对象后重试");
        return;
      }
      let releaseTime =
        this.releaseTime == ""
          ? new Date().format("yyyy-MM-dd hh:mm:ss")
          : this.releaseTime;

      let invalidTime = this.invalidTime == "" ? null : this.invalidTime;

      let data = {
        id: this.id,
        msgNumber: this.msgNumber,
        dealerObjList: dealers,
        positions: dep,
        releaseTime: releaseTime,
        invalidTime: invalidTime,
        effectiveTime: this.effectiveTime,
        feedbackType: this.feedbackType,
        msgStatus: 1,
      };
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
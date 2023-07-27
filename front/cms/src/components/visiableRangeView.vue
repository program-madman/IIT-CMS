<template>
  <v-card>
    <v-toolbar dense color="#1867C0">
      <v-btn icon>
        <v-icon color="white" @click="bt_close"> mdi-close </v-icon>
      </v-btn>
      <v-toolbar-title class="body-1 white--text">可见范围</v-toolbar-title>
    </v-toolbar>
    <span class="body-2 grey--text text--darken-2 mt-12 ml-15 mr-15"
      >发布对象</span
    >
  
    <v-sheet class="mt-4 ml-15 mr-15 pa-0" rounded outlined v-if="regions.length > 0">
      <div
        class="d-flex flex-row pl-4"
        style="background-color: #f5f6f7; height: 40px; width: 100%"
      >
        <v-subheader>发送给经销商</v-subheader>
      </div>
      <div class="d-flex flex-row pa-0">
        <div style="height: 216px; width: 25%">
          <div style="border-bottom: 1px solid #e4e8eb" class="pl-4">
            <v-virtual-scroll
              :items="regions"
              height="108"
              item-height="30"
              bench="1"
              class="pa-0 ma-0"
            >
              <template v-slot:default="{ item, index }">
                <v-list-item :key="index">
                    <v-subheader dense>{{item}}</v-subheader>
                </v-list-item>
              </template>
            </v-virtual-scroll>
          </div>
          <div class="pl-4">
            <v-virtual-scroll
              :items="dealerType"
              height="108"
              item-height="30"
              bench="1"
              class="pa-0 ma-0"
            >
              <template v-slot:default="{ item, index }">
                <v-list-item :key="index">
                    <v-subheader dense>{{item}}</v-subheader>
                </v-list-item>
              </template>
            </v-virtual-scroll>
          </div>
        </div>
        <div
          class="pa-0 ma-0"
          style="
            height: 216px;
            width: 50%;
            border-left: 1px solid #e4e8eb;
            border-right: 1px solid #e4e8eb;
          "
        >
          <v-virtual-scroll
            :items="dealers"
            height="198"
            item-height="30"
            bench="1"
          >
            <template v-slot:default="{ item, index }">
              <v-list-item :key="index">
                  <v-subheader dense>{{item.companyCode +" "+item.companyName}}</v-subheader>
              </v-list-item>
            </template>
          </v-virtual-scroll>
        </div>
        <div class="pa-0 ma-0" style="height: 216px; width: 25%">
          <v-virtual-scroll
            :items="roles"
            height="198"
            item-height="30"
            bench="1"
          >
            <template v-slot:default="{ item, l}">
              <v-list-item :key="l">
                  <v-subheader dense>{{item}}</v-subheader>
              </v-list-item>
            </template>
          </v-virtual-scroll>
        </div>
      </div>
    </v-sheet>
    <v-sheet class="mt-4 ml-15 mr-15 pa-0" rounded outlined v-if="oems.length > 0">
      <div
        class="d-flex flex-row pl-4"
        style="background-color: #f5f6f7; height: 40px; width: 100%"
      >
        <v-subheader dense>发送给大众内部</v-subheader>
      </div>
      <div class="d-flex flex-row pa-0">
        <div style="height: 180px; width: 25%">
          <v-virtual-scroll
            :items="oems"
            height="170"
            item-height="30"
            bench="1"
            class="pa-0 ma-0"
          >
            <template v-slot:default="{ item, n }">
              <v-list-item :key="n">
                <v-subheader dense>{{item.companyName}}</v-subheader>
              </v-list-item>
            </template>
          </v-virtual-scroll>
        </div>
      </div>
    </v-sheet>
  </v-card>
</template>

<script>
import { getDealerRangeList } from "@/api/getData.js";

export default {
  name: "visiableRangeView",

  props: {
    articleId: {
      type: -1,
      default: -1,
    }
  },

  data() {
    return {
      regions: [],
      dealers: [],
      dealerType:[],
      roles: [],
      oems: [],
    };
  },
  activated() {
    
  },
  mounted() {},

  created() {
    this.getDealerRange();
  },

  methods: {
    //关闭
    bt_close() {
      console.log("......关闭按钮.......");
      this.$emit("closedChild", false);
    },

    getDealerRange() {
      getDealerRangeList(this.articleId)
        .then((response) => {
            console.log("on response articleId = "+this.articleId+ " " + JSON.stringify(response));
            if(response.data != null && typeof response.data !== "undefined") {
                //先提取oem和dealers
                this.fetchDealersAndOEM(response.data.dealerList)
                //获取角色,把dealer的数据传过去
                this.roles = response.data.roles;
                console.log("areas ==> "+JSON.stringify(this.regions))
                console.log("dealerType ==> "+JSON.stringify(this.dealerType))
                console.log("oems ==> "+JSON.stringify(this.oems))
            }
        })
        .catch((e) => {
          console.log(JSON.stringify(e));
        });
    },
    fetchDealersAndOEM(data) {
        data.forEach(v => {
            if(v.companyType == 1){
                this.dealers.push(v)
                //在获取dealerType
                let type = this.convertDealerType(v.dealerType)
                if(!this.dealerType.includes(type)){
                    this.dealerType.push(type)
                } 
                //获取区域
                if(!this.regions.includes(v.area)){
                    this.regions.push(v.area)
                }  
            } else if(v.companyType == 0) {
                this.oems.push(v)
            }
        })
    },
    convertDealerType(type) {
        switch (type) {
            case 0:
                return "独立售后";
            case 1:
                return "4S店";
            case 2:
                return "二级网点";
        
            default:
                return "";
        }
    },
  },
};
</script>
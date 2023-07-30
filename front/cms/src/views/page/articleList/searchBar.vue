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
        v-model="select"
        :items="departments"
        :label="$t('from_department')"
        outlined
        hide-details="auto"
        dense
        class="pa-0 mr-4 mr-sm-2"
      ></v-combobox>
      <v-combobox
        style="height: 40px;"
        v-model="distributeSelect"
        :items="distributeDate"
        :label="$t('publish_date')"
        outlined
        flat
        hide-details
        dense
        @change="selectDate"
        class="pa-0"
      ></v-combobox>
    </v-col>

    <v-col
      cols="12"
      sm="12"
      md="3"
      class="pa-0 pt-4"
      v-show="distributeSelect != 'All'"
      style="display: inline-flex"
    >
      <v-menu
        ref="menu"
        v-model="menu"
        :close-on-content-click="false"
        :return-value.sync="startDate"
        transition="scale-transition"
        offset-y
        min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            outlined
            hide-details
            flat
            dense
            class="ml-sm-0 ml-md-2"
            :disabled="distributeSelect != 'Custom Date'"
            ref="startDate"
            v-model="startDate"
            label="Publish start time"
            readonly
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
          v-model="startDate"
          no-title
          scrollable
          :allowed-dates="allowed_start_dates"
          @change="$refs.menu.save(startDate)"
        >
        </v-date-picker>
      </v-menu>
      <v-menu
        ref="menu1"
        v-model="menu1"
        :close-on-content-click="false"
        :return-value.sync="endDate"
        transition="scale-transition"
        offset-y
        min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            outlined
            hide-details
            flat
            dense
            class="ml-2"
            style="width: 144px"
            :disabled="distributeSelect != 'Custom Date'"
            ref="endDate"
            v-model="endDate"
            label="Publish end time"
            readonly
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
          v-model="endDate"
          no-title
          scrollable
          :allowed-dates="allowed_end_dates"
          @change="$refs.menu1.save(endDate)"
        >
          <v-spacer></v-spacer>
        </v-date-picker>
      </v-menu>
    </v-col>
    <v-col
      class="pa-0 pt-4"
      cols="12"
      sm="12"
      :md="distributeSelect != 'All' ? 6 : 9"
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
          label="Search by article title or body content text"
          placeholder="Search by article title or body content text"
          maxlength="70"
          counter="70"
          append-icon="mdi-magnify"
          @click:append="toSearch()"
          @keyup.enter="toSearch()"
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
        @click="onSearch"
      >
      <v-icon left> mdi-magnify </v-icon>
        Search
      </v-btn>

      <v-btn
        height="40"
        min-width="105px"
        outlined
        depressed
        dark
        color="#333333"
        style="z-index:1"
        @click="btnReset"
      >
      <v-icon left> mdi-autorenew </v-icon>
        Revert
      </v-btn>
    </v-col>
  </v-row>
</template>
<script>
import { offsetDayTime,offsetMonthTime,isValidDate} from "@/utils/timeFormate.js"
export default {
  props: {
    departments: {
      typeof: Array,
      default: () => {
        return ["All"];
      },
    },
  },
  data() {
    return {
      select: "All",
      distributeSelect: "All",
      distributeDate: [
        "All",
        "Past seven days",
        "Past 15 days",
        "Last Month",
        "Past three months",
        "Custom Date",
      ],
      menu: false,
      menu1: false,
      startDate: new Date().format("yyyy-MM-dd"),
      endDate: new Date().format("yyyy-MM-dd"),
      search:""
    };
  },
  mounted(){
    this.onSearch();
  },
  methods:{
    selectDate() {
      switch (this.distributeSelect) {
        case "Past seven days":
          this.endDate = new Date().format("yyyy-MM-dd");
          this.startDate =   offsetDayTime(-7).format("yyyy-MM-dd");
          break;
        case "Past 15 days":
          this.endDate = new Date().format("yyyy-MM-dd");
          this.startDate =   offsetDayTime(-15).format("yyyy-MM-dd");
          break;
        case "Last Month":
          this.endDate = new Date().format("yyyy-MM-dd");
          this.startDate = offsetMonthTime(-1).format("yyyy-MM-dd");
          break;
        case "Past three months":
          this.endDate = new Date().format("yyyy-MM-dd");
          this.startDate = offsetMonthTime(-3).format("yyyy-MM-dd");
          break;
      }
    },

    allowed_start_dates(val){
      var res = true;
        var max = new Date(this.endDate);
        if(isValidDate(max)){
          res = res && new Date(val).getTime() <= max.getTime();
        }
        return res;
       
    },
    allowed_end_dates(val){
        var res = true;
        var min = new Date(this.startDate)
        if(isValidDate(min)){
          console.log("offset"+min.getTimezoneOffset())
          res = new Date(val).getTime() > min.getTime()-(24+min.getTimezoneOffset()/60) * 60 * 60 * 1000;
        }   
        return res;
    },

    btnReset() {
      this.select = "All";
      this.distributeSelect = "All";
      this.search = "";
      let data = {
        pageNum: 1,
        pageSize: 5,
        department:this.select,
        searchContent: this.search,
      };
      this.$emit("onReset",data);
    },

    onSearch(){
      let data = {
        pageNum: 1,
        pageSize: 10,
        department:this.select,
        searchContent: this.search,
      };
      var timeSearch = this.distributeSelect != "All";
      if (timeSearch) {
        data.startTime = this.startDate;
        data.endTime = this.endDate;
      }
      this.$emit("onSearch",data);
    }
  }
  
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
<template>
  <v-menu
    ref="menu1"
    :close-on-content-click="false"
    transition="scale-transition"
    offset-y
    v-model="showDatePicker"
    min-width="auto"
  >
    <template v-slot:activator="{ on, attrs }">
      <div v-bind="attrs" v-on="on" style="display: inline-block">
        <slot></slot>
      </div>
    </template>

    <v-date-picker
      v-model="selectDate"
      :persistent="true"
      scrollable
      no-title
      :allowed-dates="allowed_dates"
      v-if="showDatePicker && !showTimePicker"
      @change="
        showTimePicker = true;
        setTimeRule();
        setTime();
      "
    >
      <v-spacer></v-spacer>
    </v-date-picker>
    <v-time-picker
      format="24hr"
      no-title
      :persistent="true"
      v-model="selectTime"
      v-if="showTimePicker"
      :max="maxTimeRule"
      :min="minTimeRule"
      click:hour="setTime()"
      @change="
        showTimePicker = false;
        showDatePicker = false;
        setTime();
      "
    ></v-time-picker>
  </v-menu>
</template>
<script>
export default {
  model: {
    prop: "time",
    event: "change",
  },
  props: {
    time: {
      type: [String, Object],
      default: "",
    },
    minTime: {
      type: [Date, String],
      default: "",
    },
    maxTime: {
      type: [Date, String],
      default: "",
    },
  },
  data() {
    return {
      showTimePicker: false,
      showDatePicker: false,
      selectDate: "",
      selectTime: "00:00:00",
      maxTimeRule: null,
      minTimeRule: null,
    };
  },
  watch: {
    showDatePicker(){
      if(!this.showDatePicker){
        this.showTimePicker = false;
      }
    }
  },
  methods: {
    setTime() {
      let time = this.selectDate + " " + this.selectTime;
      if (this.selectTime.length > 0 && this.selectTime.length < 6) {
        time += ":00";
      }
      this.$emit("change", time);
    },

    setTimeRule() {
      if (this.isValidDate(this.minTime)) {
        if (new Date(this.selectDate).getTime() < this.minTime.getTime()) {
          this.minTimeRule = this.minTime.format("hh:mm");
        } else {
          this.maxTimeRule = null;
        }
      } else {
        var minval = new Date(this.minTime);
        if (this.isValidDate(minval)) {
          if (new Date(this.selectDate).getTime() < minval.getTime()+ 8 * 60 * 60 * 1000) {
            this.minTimeRule = minval.format("hh:mm");
          } else {
            this.maxTimeRule = null;
          }
        }
      }

      if (this.isValidDate(this.maxTime)) {
        if (new Date(this.selectDate).getTime() > this.maxTime.getTime()) {
          this.maxTimeRule = this.maxTime.format("hh:mm");
        } else {
          this.maxTimeRule = null;
        }
      } else {
        var maxval = new Date(this.maxTime);
        if (this.isValidDate(maxval)) {
          if (
            new Date(this.selectDate).getTime() >
            maxval.getTime() - 16 * 60 * 60 * 1000
          ) {
            this.maxTimeRule = maxval.format("hh:mm");
          } else {
            this.maxTimeRule = null;
          }
        }
      }
    },
    isValidDate(date) {
      return date instanceof Date && !isNaN(date.getTime());
    },
    allowed_dates(val) {
      var res = true;
      if (this.isValidDate(this.minTime)) {
        res =
          new Date(val).getTime() >
          this.minTime.getTime() - 16 * 60 * 60 * 1000;
      } else {
        var min = new Date(this.minTime);
        if (this.isValidDate(min)) {
          res = new Date(val).getTime() > min.getTime() - 16 * 60 * 60 * 1000;
        }
      }

      if (this.isValidDate(this.maxTime)) {
        console.log(new Date(val).getTime() - this.maxTime.getTime());
        res = res && new Date(val).getTime() < this.maxTime.getTime();
      } else {
        var max = new Date(this.maxTime);
        if (this.isValidDate(max)) {
          res =
            res && new Date(val).getTime() < max.getTime() + 8 * 60 * 60 * 1000;
        }
      }
      return res;
    },
  },
};
</script>
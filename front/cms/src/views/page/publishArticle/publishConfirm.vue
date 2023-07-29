<template>
  <v-dialog width="728" height="631" style="max-height: 631px" v-model="dialog" :persistent="true">
    <v-card class="pt-6 pl-7 pr-7">
      <span class="publish-confirm-title"
        >将向以下经销商对应的人员发布文章</span
      >

      <div class="mt-6 publish-confirm-header-text">
        <span class="ml-2">区域</span><span style="margin-left: 116px">经销商</span>
      </div>
      <v-divider class="mt-3"></v-divider>

      <v-virtual-scroll
        :items="content"
        height="443"
        item-height="40"
        bench="1"
        class="pa-0 ma-0"
      >
        <template v-slot:default="{ item, index }">
          <div
            :key="index"
            class="publish-confirm-content"
          >
            <span class="pl-2" style="width:148px;display: inline-flex;">{{item.companyType ==1 ? item.area : "大众内部"}}</span><span>{{item.companyName}}</span>
          </div>
        </template>
      </v-virtual-scroll>

      <div style="margin-top: 24px; height: 60px">
        <v-btn
          min-width="105px"
          height="36px"
          depressed
          dark
          outlined
          color="#1867C0FF"
          style="margin-left: 220px; margin-right: 24px"
          @click="onCancele"
        >
          取消
        </v-btn>
        <v-btn
          min-width="105px"
          height="36px"
          depressed
          dark
          color="blue"
          @click="onConfirm"
        >
          确认
        </v-btn>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: {
    value: {
      type: Array,
      default: function () {
        return [];
      },
    },
    open: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dialog: false,
      content: this.value,
    };
  },

  methods: {
    confirm(content) {
      this.dialog = true;
      return new Promise((resolve, reject) => {
        this.content = JSON.parse(JSON.stringify(content));
        this.onConfirm = (e) => {
          this.dialog = false;
          resolve(e);
        };
        this.onCancele = (error) => {
          this.dialog = false;
          this.content=[];
          reject(error);
        };
      });
    },
    onConfirm() {},
    onCancele() {},
    close() {
      //this.dialog = false;
      this.$emit("closeDialog");
    },
  },
  watch: {
    open() {
      this.dialog = this.open;
    },
  },
};
</script>
<style scoped>
.publish-confirm-title {
  color: #212121;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 20px;
  line-height: 26px;
  letter-spacing: 0px;
  text-align: left;
}
.publish-confirm-header-text {
  color: #666666;
  font-family: PingFang SC;
  font-weight: medium;
  font-size: 12px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}
.publish-confirm-content {
  height: 40px;
  padding-top: 13px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e8eb;
  color: #666666;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}
</style>
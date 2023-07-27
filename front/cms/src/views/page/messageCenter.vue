<template>
  <v-container fluid class="ml-0 mx-auto">
    <v-card class="mx-auto blue">
      <v-list two-line>
        <v-card-actions>
          <v-btn
            v-if="messageDates.length > 0"
            min-width="105px"
            height="36px"
            depressed
            dark
            outlined
            color="#1867C0FF"
            style="margin-left: 8px; margin-top: 1px"
            @click="markAllRead"
            >全部已读</v-btn
          >
        </v-card-actions>
        <v-subheader
          class="subtitle-1 my-n2"
          v-text="'还没有任何消息'"
          v-if="messageDates.length === 0"
        />

        <template v-for="date in messageDates">
          <v-subheader
            class="subtitle-2 my-n2"
            :key="date"
            v-text="date"
          ></v-subheader>
          <template v-for="msg in messageMap.get(date)">
            <v-row :key="msg.id">
              <v-list-item class="item">
                <v-list-item-content @click="toDetail(msg)">
                  <v-row class="message-list-text">
                    <span class="mt-n2" v-if="msg.isRead === false">
                      <v-icon large color="red darken-3"
                        >mdi-circle-small</v-icon
                      >
                    </span>
                    <span class="ml-9" v-else></span>
                    <span class="ml-2 message-list-releasetime-text"
                      >{{ msg.releaseTime }}
                    </span>
                    <span class="ml-2 message-list-time-text">{{
                      msg.time
                    }}</span>
                    <span class="ml-2 message-list-time-text">{{
                      msg.operator
                    }}</span>
                    <span class="ml-2 message-list-operate-type-text"
                      >{{ msg.typeText }} </span
                    ><a class="ml-3 message-list-title-text"
                      >{{ msg.messageTitle }}
                    </a>
                  </v-row>
                  <v-divider class="my-sm-auto"></v-divider>
                </v-list-item-content>
              </v-list-item>
            </v-row>
          </template>
        </template>
      </v-list>
    </v-card>
    <page-loader @onLoadMore="onLoadMore" />
  </v-container>
</template>

<script>
import {
  getUserMessageList,
  markAllMessageRead,
  markMessageReadOne,
} from "@/api/getData.js";
import { jumpToDetail } from "@/utils/common.js";
import { getMsgReminderStatusDesc } from "@/utils/articleUtils.js";
import { EventBus } from "@/utils/event-bus";
import { Debounce } from "@/utils/tools.js";
export default {
  mounted() {
    //初始化..... 需要吗？
    this.messageMap = new Map();
    this.messageDates = [];
    this.currentPage = 1;
    this.loadUserMessage(this.currentPage, this.pageNumber);
    EventBus.$emit("refreshUnread", "refresh");
  },
  components: {
    pageLoader: () => import("./articleList/pageLoader.vue"),
  },
  data: () => ({
    messageMap: new Map(),
    messageDates: [],
    pageNumber: 10,
    currentPage: 1,
  }),
  methods: {
    reset() {
      this.messageMap = new Map();
      this.messageDates = [];
      this.currentPage = 1;
    },

    getColor(msgStatus) {
      if (msgStatus === "0") return "#999999";
      else if (msgStatus === "4") return "#F44336";
      else if (msgStatus === "-1") return "#1867C0";
      else return "#1867C0";
    },

    //go to article detail page
    toDetail(message) {
      jumpToDetail(this.$router, message.messageId);
      if (message.isRead === false) {
        this.markOnceRead(message);
      }
    },

    onLoadMore() {
      if (this.loading == true) return;
      this.loading = true;
      this.loadUserMessage(this.currentPage, this.pageNumber);
    },

    markAllRead: Debounce(function () {


      markAllMessageRead()
        .then((response) => {
          if (
            response != null &&
            response.code === 0 &&
            response.message === "success"
          ) {
            this.reset();
            this.loadUserMessage(this.currentPage, this.pageNumber);
            EventBus.$emit("makeAllMsgRead", "all message readed");
          }
        })
        .catch();
    }, 500),

    markOnceRead(message) {
      markMessageReadOne(message.id)
        .then((response) => {
          if (
            response != null &&
            response.code === 0 &&
            response.message === "success"
          ) {
            EventBus.$emit("markSingleRead", "one message readed");
            message.isRead = true;
          }
        })
        .catch();
    },

    loadUserMessage(pageNum, pageSize) {
      getUserMessageList(pageNum, pageSize)
        .then((response) => {
          this.fillDatas(response);
        })
        .catch();
    },

    fillDatas(response) {
      console.debug("respone" + JSON.stringify(response));
      if (
        response == null ||
        response.data == null ||
        response.data.length == 0
      ) {
        return;
      }
      this.loading = false;
      this.currentPage++;
      for (let i = 0; i < response.data.length; i++) {
        let message = response.data[i];
        let date = message.operationTime.split(" ");
        let key = "";
        console.log("######## date =========> ####### " + JSON.stringify(date));
        if (date != null && typeof key != undefined && date.length >= 2) {
          key = date[0];
          message.time = date[1];
          message.prefixTime = key;
        } else {
          key = message.operationTime;
        }
        if (key != null && typeof key != undefined && key != "") {
          let list = this.messageMap.get(key);
          if (list == null || typeof list == undefined) {
            list = [];
          }
          message.color = message.isRead ? "grey" : "red";
          message.cardColor = message.isRead ? "grey" : "blue-grey";
          //----- mock effect ----
          //message.color = i % 2 === 0 ? "grey" : "red";
          //message.cardColor = i % 2 === 0 ? "grey" : "indigo";
          //message.isRead = i % 2 === 0 ? true : false;
          //----- mock effect ----
          message.typeText = getMsgReminderStatusDesc(message.operation);
          console.log("typeText ==========>" + message.color);
          list.push(message);
          this.messageMap.set(key, list);
        }
      }
      this.messageDates = Array.from(this.messageMap.keys());
      //alert("on response length >>>> "+Array.from(this.messageDates).length)
    },
  },

  //Set the length of a paragraph
  filters: {
    length(text, length, suffix = "...") {
      if (!text) {
        return;
      }
      if (text.length <= length) {
        return text;
      }
      return (text + "").replace(/\n/g, "<br/>").substring(0, length) + suffix;
    },
  },
};
</script>
<style scoped>
.message-list-text {
  font-family: PingFang SC;
  font-weight: semibold;
  font-size: 14px !important;
  line-height: 20px;
  letter-spacing: 0px;
  text-align: left;
}
.message-list-releasetime-text {
  opacity: 0.800000011920929;
  color: #333333;
}
.message-list-time-text {
  color: #666666;
  font-family: Roboto;
  font-weight: regular;
}
.message-list-operate-type-text {
  color: #333333;
  font-family: Roboto;
  font-weight: regular;
}
.message-list-title-text {
  color: #1867c0;
  font-family: Roboto;
  font-weight: regular;
}
</style>
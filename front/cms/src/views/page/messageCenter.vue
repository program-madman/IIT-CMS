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
            @click="newMessage"
            >New Message</v-btn
          >
        </v-card-actions>
        <v-subheader
          class="subtitle-1 my-n2"
          v-text="'Empty Message'"
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
                    <span class="ml-2 message-list-time-text">{{ msg.time }}</span>
                    <span class="ml-4 message-list-time-text">{{ msg.fromUser }}</span>
                    <a class="ml-3 message-list-title-text">{{ msg.title }}</a>
                  </v-row>
                  <v-divider class="my-sm-auto"></v-divider>
                </v-list-item-content>
              </v-list-item>
            </v-row>
          </template>
        </template>
      </v-list>
    </v-card>

    <v-dialog
        v-model="dialog"
        activator="parent"
        width="600"
      >
        <v-card>
          <v-card-title class="text-h5">
            {{selectMsg.title}}
           </v-card-title>
          <v-card-text>
            {{selectMsg.content}}
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" block @click="dialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog
        v-model="sendDialog"
        activator="parent"
        width="600"
      >
        <v-card> 
          
          <v-col cols="12">
          <v-text-field
            v-model="toUser.toUserId"
            label="To User"
            required
          />
        </v-col>
          

          <v-col cols="12">
          <v-text-field
            v-model="toUser.title"
            label="Title"
            maxlength="70"
            required
          />
        </v-col>
        <v-col cols="12">
          <v-textarea maxlength="500" v-model="toUser.content" label="Content" variant="outlined"></v-textarea>
        </v-col>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn  
            @click="sendDialog = false"
          >
            Cancle
          </v-btn>
          <v-btn
            color="primary"
            @click="postMessage"
          >
              Send Message 
          </v-btn>
        </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog
      v-model="sending"
      :scrim="false"
      persistent
      width="auto"
    >
      <v-card
        color="primary"
      >
        <v-card-text>
          Please stand by
          <v-progress-linear
            indeterminate
            color="white"
            class="mb-0"
          ></v-progress-linear>
        </v-card-text>
      </v-card>
    </v-dialog>

      <v-snackbar color="success" v-model="snackbarVisible"   top="top"  >
       Send Message Success!
    <template v-slot:action>
      <v-btn text color="white" @click="snackbarVisible = false">
        Close
      </v-btn>
    </template>
  </v-snackbar>

  </v-container>
</template>

<script>
import {
  getMyMessages,
  sendMessage,
  markMessageReadOne,
} from "@/api/getData.js";
//import { getMsgReminderStatusDesc } from "@/utils/articleUtils.js";
import { EventBus } from "@/utils/event-bus";

export default {
  mounted() {
    this.messageMap = new Map();
    this.messageDates = [];
    this.currentPage = 1;
    this.loadUserMessage();
    EventBus.$emit("refreshUnread", "refresh");
  },
  components: {
    
  },
  data: () => ({
    sending:false,
    dialog:false,
    sendDialog:false,
    messageMap: new Map(),
    messageDates: [],
    pageNumber: 10,
    currentPage: 1,
    snackbarVisible:false,
    selectMsg:{"title":"","content":""},
    toUser:{"toUserId":"","title":"","content":""}
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
      if (message.isRead === false) {
        this.markOnceRead(message);
      }
      this.selectMsg = message
      this.dialog = true
    },

    newMessage() {
      this.sendDialog = true
    },

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
        }).catch();
    },

    loadUserMessage() {
      // var items = []
      // for (let i = 0; i < 10; i++) {
      //     let msg = {}
      //     msg.title = "title "+ i;
      //     msg.content = "content" + i
      //     msg.isRead = false
      //     msg.sendtime = "2023-08-03 12:12:12"
      //     items.push(msg)
      // }
      // var resp = {}
      // resp.data = items

      getMyMessages().then((response) => {
          if (response.code === '200') {
            this.fillDatas(response.data)
          }
        }).catch();
    },

    postMessage() {
      this.sending = true
      sendMessage(this.toUser).then((resp) => {
          if (resp.code === '200') {
            this.snackbarVisible = true
            this.sendDialog = false
          }
        }).catch().finally(() => { this.sending = false});   
      
    },

    fillDatas(response) {
      console.debug("respone >>>>>>" + JSON.stringify(response));
      this.loading = false;
      for (let i = 0; i < response.length; i++) {
        let message = response[i];
        let date = message.sendTime.split("31T");
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
          console.log("typeText ==========>" + message.color);
          list.push(message);
          this.messageMap.set(key, list);
        }
      }
      this.messageDates = Array.from(this.messageMap.keys());
      this.$forceUpdate()
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
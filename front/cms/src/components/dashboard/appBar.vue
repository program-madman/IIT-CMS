<template>
  <v-app-bar id="app-bar" app flat link height="64" class="app-bar-style">
    <v-app-bar-nav-icon @click="onDrawerClick"> </v-app-bar-nav-icon>

    <v-spacer></v-spacer>
    <v-btn
      color="primary"
      dark
      class="pl-2"
      height="36"
      min-width="105"
      v-if="hasEditPermission"
      @click="newArticle"
    >
      {{ $t("new_message") }}
    </v-btn>
    <!-- 消息通知 -->
    <v-menu :nudge-width="220" offset-y open-on-hover>
      <template v-slot:activator="{ on, attrs }">
        <v-btn class="ml-2" min-width="0" text v-bind="attrs" v-on="on">
          <v-badge v-if="msgCount > 0" color="red" overlap bordered>
            <template v-slot:badge>
              <span>{{ msgCount > 99 ? 99 : msgCount }}</span>
            </template>
            <v-icon>mdi-bell</v-icon>
          </v-badge>
          <v-icon v-if="msgCount <= 0">mdi-bell</v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-text>
          <div @click="markOnceRead(item)" disabled v-for="(item, cindex) in msgItems" :key="'message'+cindex">
            
            <div class="pa-0 ma-0 " style="min-height:45px">
              
              <span class="ml-n2" v-if="item.isRead === false">
                <v-icon dense large color="red darken-3"
                  >mdi-circle-small</v-icon>
              </span>
              <span class="ml-n2"  v-else>
                <v-icon dense large color="white darken-3"
                  >mdi-circle-small</v-icon>
              </span>
              <span class="ml-n2">{{ item.title }}</span>
              <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
              <a
                v-bind="attrs"
                v-on="on"
                small
                :style="{color:'#1867C0'}"
                color="blue lighten-2"
                @click="selectMsg(cindex)"
              >
                {{ item.messageTitle | lenghth(20) }}</a>
               </template>
            <span>{{item.messageTitle}}</span>
         </v-tooltip>
         
         <v-divider />
        
            </div>
          </div>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            align="center"
            color="blue darken-1"
            text
            @click="toMessageCenter"
          >
            All Message
          </v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-menu>
    <!-- 语言选择 -->
    <v-menu
      
      bottom
      left
      offset-y
      origin="top right"
      min-width="0"
      transition="scale-transition"
    >
      <template v-slot:activator="{ attrs, on }">
        <v-btn class="ml-2" min-width="110px" text v-bind="attrs" v-on="on" v-if="false">
          <v-icon>mdi-translate </v-icon>{{ lanval }}
        </v-btn>
      </template>
      <v-list v-model="lanitems.val">
        <v-list-item
          v-for="(item, index) in lanitems"
          :key="index"
          :val="lanitems.val"
          @click="changeLanguage(item)"
        >
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
    <!-- <v-btn> 退出 </v-btn> -->

    <div class="mr-1 ml-6">
      <div class="app-bar-user">{{ user.name }}</div>
      <span class="app-bar-user-post mr-2" v-if="user.department != null">{{ user.department }}</span>
      <span class="app-bar-user-post" v-if='user.positionName !="MG"'>{{ user.positionName }}</span>
    </div>

    
  </v-app-bar>
</template>
<script>
// import mySearch from "../store/modules/mySearch";
import { mapState, mapGetters } from "vuex";
import { getUserMessageList, getMessageUnreadCount,markMessageReadOne } from "@/api/getData.js";
import { getMsgReminderStatusDesc} from "@/utils/articleUtils.js";
import { jumpToDetail } from "@/utils/common.js";
import { EventBus } from "@/utils/event-bus";

export default {

  mounted() {

    this.getUserUnreadArticleCount()
    this.getUserMessage();

    EventBus.$on("makeAllMsgRead", (msg) => {
      // 所有消息标记已读，把标记去掉
      console.log("######## receive message ####### " + msg);
      this.msgCount = 0;
      this.getUserMessage()
    }),

     EventBus.$on("markSingleRead", (msg) => {
      // 我们不需要在获取一次未读消息数量，如果再次刷新的话，可能看上去会有未读消息可能还会多的结果
      console.log("######## receive message ####### " + msg);
      this.msgCount--;
    }),

    EventBus.$on("refreshUnread", (msg) => {
      // 接到此消息会刷新一下未读数。
      console.log("######## refresh message ####### " + msg);
      this.getUserUnreadArticleCount()
    }),

    EventBus.$on("articleListComplete", (msg) => {
      console.log("######## donothing for now ####### " + msg);
      //this.getUserUnreadArticleCount();
      //this.getUserMessage();
    })
  },

  data: () => ({
    lanval: "简体中文",
    drawer: true,
    msgCount: 0,
    lanitems: [
      { title: "简体中文", val: "zh" },
      { title: "English", val: "en" },
    ],
    //default message
    msgItems: [
      {
        id:-1,
        operator: "",
        optText: "没有新消息",
        title: "",
      },
    ],
  }),

  computed: {
    ...mapState({
      user: (state) => state.user.userInfo,
    }),
    ...mapGetters({
      // 把 `this.doneCount` 映射为 `this.$store.getters.doneTodosCount`
      hasEditPermission: "user/hasEditPermission",
    }),
  },

  methods: {

    getUserMessage() {
      //  getUserMessageList(1, 10)
      // .then((response) => {
      //   //showMsgText("1")
      //   console.log("######## unread message =========> ####### " + JSON.stringify(response));
      //   if (response != null && response.data != null && response.data.length > 0) {
      //     this.msgItems = response.data;
      //     for (let i = 0; i < this.msgItems.length; i++) {
      //       let msg = this.msgItems[i];
      //       msg.optText = getMsgReminderStatusDesc(msg.operation);
      //     }
      //   }
      // }).catch();
      this.msgItems = []
      for (let i = 0; i < 10; i++) {
            var msg = {}
            msg.title = "message title "+i
            msg.content = "message content "+i
            msg.isRead = false
            this.msgItems.push(msg)
          }
    },

    getUserUnreadArticleCount() {
      getMessageUnreadCount()
        .then((response) => {
            //console.log("######## unread counnt =========> ####### " + JSON.stringify(response));
            if(response.data != null && response.data > 0) {
              this.msgCount = response.data
            }
        }).catch()
    },

    markOnceRead(message) {
      if(message.id === -1) {
        return;
      }      
      markMessageReadOne(message.id)
        .then((response) => {
          if(response != null && response.code ===0 && response.message === "success"){
              message.isRead = true;
              this.msgCount--;
          }  
        })
        .catch();

        jumpToDetail(this.$router,message.messageId);
    },

    onDrawerClick() {
      this.drawer = !this.drawer;
      this.$emit("change", this.drawer);
    },
    changeLanguage(item) {
      this.lanval = item.title;
      this.$i18n.locale = item.val;
    },

    toMessageCenter() {
      this.$router.push({ name: "messageCenter" });
    },
    // 选中文章跳转详情
    selectMsg(index) {
      console.log("==========selectMsg=========" + index+" messageID:"+this.msgItems[index].messageId); 
        jumpToDetail(this.$router,this.msgItems[index].messageId);
    },
    newArticle() {
      this.$router.push({ name: "publishArticle" });
    },
  },
  created() {
    this.$i18n.locale = "zh";
  },
  //过滤文字
  filters: {
    lenghth(text, length, suffix = "...") {
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
.app-bar-style {
  background: #ffffffff !important;
  box-shadow: inset 0px -1px 0px 0px #e4e8ebff !important;
}
.app-bar-user {
  color: #333333ff;
  font-family: Noto Sans S Chinese;
  font-weight: bold;
  font-size: 16px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}
.app-bar-user-post {
  color: #999999ff;
  font-family: Noto Sans S Chinese;
  font-weight: regular;
  font-size: 12px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}
</style>

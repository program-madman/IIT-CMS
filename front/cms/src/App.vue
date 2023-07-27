<template>
  <div>
    <!-- <keep-alive>
      <router-view v-if="$route.meta.keepAlive"> </router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive"></router-view> -->
    <keep-alive>
      <router-view></router-view>
    </keep-alive>
    <v-snackbar v-model="snackbarShow" class="mb-2">
      {{ snackbarMessage }}
      <button style="float: right" @click.stop="snackbarShow = false">
        关闭
      </button>
    </v-snackbar>
  <template>
    <div class="text-center">
    <v-dialog
      width="500"
      transition="scale-transition"
      border="top"
      persistent
      v-model="showAlert"
      class="relogin-alert"
      :prominent="showAlert"
      type="error"
    >
     <v-card>
        <v-card-title class="text-h6 grey lighten-2">
          <v-icon class="mr-2">mdi-alert</v-icon>
           登录已超时
        </v-card-title>

        <v-card-text class="mt-2">
          登录信息已过期,请前往主页进行重新登录
        </v-card-text>
         <v-card-text>
           您未发布的文章会保存到草稿箱
        </v-card-text>

        <v-divider></v-divider>

         <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click.stop="reLogin()"
          >
            重新登录
          </v-btn>
        </v-card-actions>
      </v-card>
     </v-dialog>
     </div>
    </template>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  // data(){
  //   return{
  //     show:false,
  //   }
  // },
  watch: {
    showAlert() {
      console.log("=========>alert :" + this.showAlert);
    },
  },
  methods: {
    reLogin() {
      this.showAlert = false;
      window.location.href = "../login";
    },
  },
  computed: {
    snackbarShow: {
      get() {
        return this.$store.state.snackBar.show;
      },
      set(v) {
        if (!v) {
          this.$store.commit("snackBar/close");
        }
      },
    },
    showAlert: {
      get() {
        return this.$store.state.snackBar.alert;
      },
      set(v) {
        if (!v) {
          this.$store.commit("snackBar/closeAlert");
        }
      },
    },

    ...mapState({
      snackbarMessage: (state) => state.snackBar.message,
    }),
  },
};
</script>
<style scoped>
.relogin-alert {
  top: 10%;
  left: 50%;
  margin-left: -315px;
  width: 630px;
  position: fixed;
  z-index: 1000;
  float: left;
}
</style>

<template>
  <v-app>
    <loading v-if="false" />
    <login-error v-if="loadingError" />
    <template>
  <div class="login-container">
    
    <div class="login-center-content">
      <div class="login-form-card">

        <v-layout wrap>
        <v-flex >
          <v-card elevation="4" light tag="section">
            <v-card-title>
              <v-layout align-center justify-space-between>
                <h1 class="headline">
                  <v-img class="ml-12"  width="200px" height="200px" src="../assets/images/hawks.jpeg" />
                  {{ platformName }}
                </h1>
                <v-flex>
                  <v-img :alt="platformName" class="ml-3" contain height="48px" position="center right" src="https://www.mobygames.com/images/i/12/25/1435075.png"></v-img>
                </v-flex>
              </v-layout>
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text>
              <p>Sign in with your username and password:</p>
              <v-form>
                <v-text-field
                              outline
                              label="Username"
                              type="text"
                              :rules="[requiredEmail]"
                              v-model="username"></v-text-field>
                <v-text-field
                              outline
                              hide-details
                              :rules="[requiredPassword]"
                              label="Password"
                              type="password"
                              v-model="password"></v-text-field>
              </v-form>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions :class="{ 'pa-3': $vuetify.breakpoint.smAndUp }">
              <v-spacer></v-spacer>
              <v-btn @click="loginCMS" style="width: 128px;" :loading="loading" color="info" :large="$vuetify.breakpoint.smAndUp">Login</v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
      </v-layout>
       
      </div>
    </div>
    <div class="login-foot" />

    <v-snackbar v-model="snackbar"  color="#444444" top="top"  >
     {{verifyError}}
    <template v-slot:action>
      <v-btn text color="red" @click="snackbar = false">
        Close
      </v-btn>
    </template>
  </v-snackbar>
  </div>
</template>
</v-app>
</template>

<script>
import { login,myProfile } from "@/api/getData.js";
export default {
  data() {
    return {
      verifyError:"",
      emailRegex : /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
      userSelectPosition:{},
      carryoverPosition:[],
      dialog:false,
      loading: false,
      loadingError: false,
      viewer:[11,12],
      author:[11,12,13,21,22],
      admin:[11,12,13,20,21,22],
      darkTheme: true,
      platformName: 'IIT CS425 Project - CMS',
      password: null,
      username: null,
      snackbar:false,
      verifyUserName:false,
      verifyPassword:false,
    };
  },
  components: {
    Loading: () => import("./loginLoading.vue"),
    LoginError: () => import("@/components/base/error-view.vue"),
  },
  mounted() {
    let authorization = this.$route.query;
    console.log("authorization ====> "+JSON.stringify(authorization))
    console.log(authorization)
    // let user = {"authorization":"1234","menuList":this.author}
    // this.$store.commit("user/setUser", user);
    // console.log("user ====> "+JSON.stringify(this.$store.state.user.userInfo))
    //this.toIndexPage();
    // return;

    // if (authorization == null || typeof authorization == "undefined") {
    //   if (this.$store.state.user.loginStatus) {
    //     this.toIndexPage();
    //   } else {
    //     this.loading = false;
    //     this.loadingError = true;
    //   }
    //   return;
    // }

    // this.$store.commit("user/setAuthorization",JSON.stringify(authorization));
    // getUserInfo()
    //   .then((res) => {
    //     console.log("userinfo=====>" + JSON.stringify(res));
    //     if (res == null || typeof res == "undefined" || res.code != 0) {
    //       console.log(" code mybay null "+res.code)
    //       this.loadingError = true;
    //     } else if(res.data.positionList === null || typeof res.data.positionList === "undefined" || res.data.positionList.length === 0) { 
    //       console.log("user position is null === >"+JSON.stringify(res.data.userInfo.positionList))
    //       this.loadingError = true;
    //     } else {
    //       console.log("=== enter main page ===")
    //       this.$store.commit("user/setUser", res.data);  
    //       let position = res.data.positionList;
    //       console.log("user positions ===> "+JSON.stringify(position))
    //       if(position.length === 1) {
    //         console.log("=== user has onle one position ===")
    //         this.dialog = false;
    //         this.userSelectPosition = position[0].code;
    //         this.onSelectPosition();     
    //       }else {
    //         console.log("=== user has many position === ")
    //         this.carryoverPosition = position;
    //         this.dialog = true;
    //       } 
    //     }
    //   })
    //   .catch((e) => {
    //     console.log(" on error "+JSON.stringify(e))
    //     this.loadingError = true;
    //   })
    //   .finally(() => {
    //     this.loading = false;
    //   });
  },

  methods: {

    requiredEmail (v) {
      this.verifyUserName = this.emailRegex.test(v);
        return this.verifyUserName || 'Invalid email address'
      },

    requiredPassword (v) {
      this.verifyPassword = !!v
      return this.verifyPassword || 'Invalid password'
    },

    toIndexPage(){
      var index = this.$store.getters["user/getIndexPage"]
      if(index != null && typeof index != undefined){
          this.$router.push(index.path);
      } 
    },

    loginCMS() {
      login(this.username,this.password)
       .then((res) => {
        console.log("userinfo=====>" + JSON.stringify(res));
        if (res == null || typeof res == "undefined" || res.code != '200') {
          console.log(" code mybay null "+res.code)
          this.verifyError = "login failed"
          this.snackbar = true
        } else {
          console.log("=== enter main page ===")
          let user = res.data;
          //user.menuList = this.author
          //console.log("login success ====> "+JSON.stringify(user))
          this.$store.commit("user/setUser", user);
          //this.toIndexPage()
          this.getUserInfo(res.data.token)
        }
      })
      .catch((e) => {
        console.log(" on error "+JSON.stringify(e))
        this.verifyError = "login failed"
        this.snackbar = true
      })
    },
    getUserInfo(token) {
      myProfile()
       .then((res) => {
        console.log("userinfo=====>" + JSON.stringify(res));
        if (res == null || typeof res == "undefined" || res.code != '200') {
          console.log(" code mybay null "+res.code)
          this.verifyError = "login failed"
          this.snackbar = true
        } else {
          console.log("=== enter main page ===")
          let user = res.data;
          user.menuList = this.author
          user.token = token
          console.log("getinfo success ====> "+JSON.stringify(user))
          this.$store.commit("user/setUser", user);
          this.toIndexPage()
        }
      })
      .catch((e) => {
        console.log(" on error "+JSON.stringify(e))
        this.verifyError = "login failed"
        this.snackbar = true
      })
    }
  },

};
</script>


<style lang="scss" scoped>
$dark: #000;
$dark_gray: #889aa4;
$light_gray: #eee;
$cursor: #fff;

.login-container {
  height: 100%;
  width: 100%;
  background: url("../assets/images/login/background.png") no-repeat;
  background-size: cover;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .login-head{
    display: flex;
    justify-content:space-between;
  }
  .login-foot{
    height: 172px;
  }
  .login-title {
    margin-top: 100px;
    margin-left: 48px;
  }

  .login-audi{
    margin-top: 100px;
    margin-right: 48px;
  }

  .login-title-label {
    height: 20px;
    font-size: 12px;
    margin-top: 12px;
    font-weight: bold;
    letter-spacing: 2px;
    color: white;
  }

  .login-center-content {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }

  .login-form-card {
    margin-right: 48px;
    width: 416px;
  }

  .login-form {
    font-family: Audi Type VF, DFP King Gothic GB;
    position: relative;
    background-color: #ffffff;
    opacity: 0.8;
    width: 416px;
    padding: 48px 32px 56px 32px;
    margin: 0 auto;
    overflow: hidden;
  }

  .title-container{
    height: 60px;
    font-size: 40px;
    font-weight: 700;
  }

  .username-item {
    margin-top: 24px;
  }

  .login-label {
    font-size: 12px;
    color: #666666;
    margin-top:16px;
  }

  .login-input {
    font-size: 16px;
    color: #333333;
  }

  .login-submit {
    font-family: Audi Type VF, DFP King Gothic GB;
    width: 100%;
    height: 56px;
    margin-top: 24px;
    font-size: 16px;
  }

  .error-msg {
    margin-bottom: 20px;
    color: red;
  }

  .caution-icon {
    display: inline-block;
    width: 16px;
    height: 16px;
    background: url(../assets/images/login/caution.png) no-repeat;
  }

  .password-icon-view {
    display: inline-block;
    width: 24px;
    height: 24px;
    background: url(../assets/images/login/eye_open.svg) no-repeat;
  }

  .password-icon-minus {
    display: inline-block;
    width: 24px;
    height: 24px;
    background: url(../assets/images/login/eye_close.svg) no-repeat;
  }
  .get-verify-code{
    color: #556EFE;
    font-size: 14px;
  }
}
.forget-password {
  color: #556EFE;
  font-size: 14px;
  width: 100%;
  margin-top: 12px;
}
.dialog-footer{
  display: flex;
  justify-content: flex-end;
  padding-top: 8px;
}
::v-deep .el-dialog__title{
  font-size: 16px;
  font-weight: bold;
}

::v-deep .el-dialog__body{
  padding: 20px 20px;
}

</style>
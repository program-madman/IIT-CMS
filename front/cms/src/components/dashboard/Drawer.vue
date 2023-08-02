<template>
  <v-navigation-drawer v-model="drawerOpen" app>
    <div class="drawer-title-wrapper">
      <span class="drawer-title" @click.stop="onTitleClick" style="cursor:pointer">MyMessage</span>
    </div>
    <v-list>
      <v-list-item-group
        class="pt-0 mt-0"
        v-model="selectedItem"
        color="primary"
      >
        <v-list-item
          class="pa-0"
          v-for="(item, i) in items"
          :key="i"
          :to="item.path"
          v-on:click="item.message = 0"
        >
          <v-list-item-icon class="mr-5">
            <div class="indicator"></div>
            <v-icon v-text="item.icon" style="margin-left: 27px"></v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title class="lab-text" v-text="$t(item.text)"></v-list-item-title>
          </v-list-item-content>
          <div class="message_background" v-show="item.message != 0">
            {{ item.message }}
          </div>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import {menus} from "@/store/modules/menu.js"
export default {
  name: "navigationDrawer",
  model: {
    prop: "drawer",
  },
  props: {
    drawer: Boolean,
  },

  data() {
    return {
      drawerOpen: true,
      selectedItem: 0,
    };
  },
  computed: {
    items() {
      let res = [];
      let menu = this.$store.state.user.userInfo.menuList;
      if (menu == null || typeof menu == "undefined" || menu.length == 0)
        return res;
      menu.sort();
      menu.forEach((item) => {
        if (item > 10 && item < 23) {
          res.push(menus[item]);
        }
      });
      return res;
    },
  },
  methods:{
    onTitleClick(){
      this.$router.push(this.items[0].path)
    }
  },
  watch: {
    drawer() {
      this.drawerOpen = this.drawer;
    },
  },
};
</script>

<style scoped>
.drawer-title-wrapper {
  height: 84px;
  padding-top: 15px;
}
.drawer-title {
  color: #333333ff;
  font-family: Noto Sans;
  font-weight: bold;
  font-size: 26px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
  margin-top: 15px;
  margin-left: 30px;
}
.v-list .v-list-item--active .indicator {
  background: #1867c0ff;
  display: block;
  height: 40px;
  width: 3px;
}

.v-list .indicator {
  background: #fff;
  display: block;
  height: 40px;
  width: 3px;
}

.v-list-item {
  align-items: center;

  letter-spacing: normal;
  height: 40px;
  min-height: 40px;
  outline: none;
  padding: 0px;
  position: relative;
  text-decoration: none;
}

.v-list-item__icon {
  margin: 0;

  align-self: inherit;
}
.lab-text{
  font-family: PingFang SC;
  font-weight: bold;
  font-size: 14px !important;
  line-height: normal;
  letter-spacing: 0px !important;
  text-align: left;
}
.message_background {
  border-radius: 10px;
  background: #e0e0e0ff;
  width: 25px;
  height: 20px;
  text-align: center;
  font-family: PingFang SC;
  font-weight: bold;
  font-size: 14px;
  line-height: normal;
  letter-spacing: 0px;
  margin-right: 16px;
}
</style>

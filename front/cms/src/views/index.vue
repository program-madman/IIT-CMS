<template>
  <v-app>
    <app-bar @change="onDrawerClick"/>
    <!-- navigation drawer -->
    <web-dashboard-core-drawer v-model="drawer" app />
    <dashboard-core-view />
  </v-app>
</template>

<script>
export default {
  data: () => ({
    drawer: true,
  }),
  mounted() {
    window.addEventListener("beforeunload", this.beforeunloadHandler);
  },
  destroyed() {
    window.removeEventListener("beforeunload", this.beforeunloadHandler);
  },
  components: {
    WebDashboardCoreDrawer: () => import("@/components/dashboard/Drawer"),
    DashboardCoreView: () => import("@/components/dashboard/View"),
    AppBar:() => import("@/components/dashboard/appBar"),
   
  },
  methods: {
    onDrawerClick(open){
      this.drawer = open;
    },
    beforeunloadHandler(){
      this.$store.commit('user/storeSession')
    },
  },
 
};
</script>



<template>
  <v-main>
    <v-expand-transition>
      <v-tabs
        v-show="tabsView && tabList.length"
        style="position: sticky; top: 64px; z-index: 5"
        class="tabs"
        show-arrows
      >
        <v-tab
          v-for="(item, i) in tabList"
          :key="item.name"
          :name="i"
          :to="{ path: item.path, query: item.query }"
          style="text-transform: none"
        >
          <p>{{ $t(item.title) }}</p>
          <v-icon
            v-if="tabList.length != 1"
            size="12"
            class="ml-4 closetab"
            @click.stop.prevent="onTableIconClick(i)"
            @contextmenu.stop.prevent=""
          >
            mdi-close
          </v-icon>
        </v-tab>
      </v-tabs>
    </v-expand-transition>

    <iframe-router-view />
  </v-main>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import { mapState } from "vuex";
import { EventBus } from "@/utils/event-bus";
export default {
  name: "DashboardCoreView",
  components: {
    IframeRouterView: () => import("@/components/base/iframe-router-view"),
  },
  data() {
    return {
      tabList: [],
      tabsView: true,
      tree: true,
      dataCount: this.$store.state.count,
    };
  },
  watch: {
    $route(to) {
      // eslint-disable-next-line no-unused-vars
      var tema = this.tabList.some((val, index) => {
        // eslint-disable-next-line eqeqeq

        if (
          val.path == to.path ||
          (typeof to.meta.group != "undefined" && val.group == to.meta.group)
        ) {
          this.tabList.splice(index, 1, {
            name: to.name,
            path: to.path,
            title: to.name,
            group: to.meta.group,
            query: to.query,
          });
          return true;
        }
      });

      // eslint-disable-next-line eqeqeq
      if (tema == false) {
        this.tabList.push({
          name: to.name,
          path: to.path,
          title: to.name,
          group: to.meta.group,
          iframe: to.iframeComponent,
          query: to.query,
        });
      }
    },
  },

  mounted() {

     EventBus.$on("closeTab", (name) => {
        console.log("closeTab:"+name);
        this.closeTabByName(name)
    }),

    EventBus.$on("publishSucessful", () => {
      var index = this.tabList.findIndex((t) => {
        return t.path == "/publishArticle";
      });
      if (index >= 0) {
        this.tabList.splice(index, 1);
        var indexPage = this.$store.getters["user/getIndexPage"];
        if (indexPage != null && typeof indexPage != undefined) {
          this.$router.push("/" + indexPage.path);
        }
        return;
      }
    });
    this.tabList.push({
      name: this.$route.name,
      path: this.$route.path,
      title: this.$route.name,
      group: this.$route.meta.group,
      query: this.$route.query,
    });

    this.tabsView = JSON.parse(localStorage.getItem("tabsView") || true);
  },
  methods: {
    closeTab(index) {  
        console.log("closeTab:"+index);        
        this.tabList.splice(index, 1);
        let target = this.tabList[this.tabList.length - 1];
        this.$router.push({ path: target.path, query: target.query });
    },

    onTableIconClick(index){   
      console.log("onTableIconClick:"+index);      

      if("publishArticle"==this.tabList[index].name){
          console.log("close publishArticle");
          EventBus.$emit("wantClosePublicarticle")
      }else{
          this.closeTab(index);
      }    
    },

    closeTabByName(name) {
      var index = this.tabList.findIndex((t) => {
        return t.name == name;
      });
      if(index>=0){
        this.closeTab(index);
      }
    },

  },
};
</script>
<style>
.tabs.theme--light.v-tabs > .v-tabs-bar {
  background-color: #f5f6f7ff !important;
  box-shadow: inset 0px -1px 0px 0px #e4e8ebff;
}
.tabs .v-tabs-bar {
  border-radius: inherit;
  height: 34px;
}
.tabs .v-tabs-slider-wrapper {
  display: none;
}

.tabs.theme--light .v-tab {
  border-radius: 4px 4px 0px 0px;
  padding: 0 8px;
  margin: 7px 0 0 8px;
  background: #f5f6f7ff;
  border: 1px solid #e4e8ebff;
  box-shadow: inset -1px -1px 0px 0px #e4e8ebff;
  height: 28px;
}

.tabs.theme--light .v-tab--active {
  border-radius: 4px 4px 0px 0px;
  background: #ffffffff;
  border: 1px solid #e4e8ebff;
  border-bottom: 0;
  box-shadow: inset 0px -1px 0px 0px #e4e8ebff;
}

.v-tab p {
  margin: 0;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  letter-spacing: 0px;
  max-width: 60px;
  height: 100%;
  line-height: 28px;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 1;
  white-space: nowrap;
}

.v-tab--active .v-icon.v-icon.closetab {
  display: inline-flex;
}

.v-icon.v-icon.closetab {
  display: none;
}
</style>

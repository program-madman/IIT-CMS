<template>
  <v-card>
    <v-tabs
      v-model="currentTab"
      color="deep-purple-accent-4"
      align-tabs="center">
      <v-tab
          v-for="tabItem in tabItems"
          :key="'tab_' + tabItem.value">
        {{ $t(tabItem.tabName) }}
      </v-tab>

    </v-tabs>
    <v-tabs-items v-model="currentTab">
      <v-tab-item
          v-for="tabItem in tabItems"
          :key="'tab_' + tabItem.value">
        <v-card flat>
<!--          <v-card-text>{{ tabItem.value }}</v-card-text>-->
                    <recordDetails :table-data="tabItem.data" :head-data="tabItem.head" />
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </v-card>
</template>
<script>
import recordTableTabData from './recordTableTabData.json'
import {
  listArticleStatistic,
  listAttachment,
  listBrowse,
  listComment, listLike,
  listMessage,
  listOperate, listRead,
  listSend,
  listUserStatistic
} from "@/api/recordApi";
export default {
  components: {
    recordDetails: () => import("./recordDetails.vue")
  },
  data: () => ({
    currentTab: null,
    tabItems: [],
  }),
  created() {
    this.initTabItems();
  },
  methods: {
    initTabItems(){
      this.tabItems = recordTableTabData;
      listSend().then(res => {
        this.tabItems[0].data = res.data;
      })
      listBrowse().then(res => {
        this.tabItems[1].data = res.data;
      })
      listComment().then(res => {
        this.tabItems[2].data = res.data;
      })
      listMessage().then(res => {
        this.tabItems[3].data = res.data;
      })
      listOperate().then(res => {
        this.tabItems[4].data = res.data;
      })
      listUserStatistic().then(res => {
        this.tabItems[5].data = res.data;
      })
      listRead().then(res => {
        this.tabItems[6].data = res.data;
      })
      listAttachment().then(res => {
        this.tabItems[7].data = res.data;
      })
      listLike().then(res => {
        this.tabItems[8].data = res.data;
      })
      listArticleStatistic().then(res => {
        this.tabItems[9].data = res.data;
      })
    }
  }
}
</script>


<style scoped>

</style>
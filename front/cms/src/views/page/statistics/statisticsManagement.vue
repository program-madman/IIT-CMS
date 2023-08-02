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
          <tableDetails :table-data="tabItem.data" :head-data="tabItem.head" />
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </v-card>
</template>
<script>
import tableTabData from './statisticsTableTabData.json'
import {
  listArticleStatistics,
  listNoBrowsedUserYear,
  listRecentMonthCount,
  listTopThreeArticleMonth
} from "@/api/statisticsAPI";

export default {
  components: {
    tableDetails: () => import("../record/tableDetails.vue")
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
      this.tabItems = tableTabData;
      listRecentMonthCount().then(res => {
        this.tabItems[0].data = res.data;
      })
      listArticleStatistics().then(res => {
        this.tabItems[1].data = res.data;
      })
      listTopThreeArticleMonth().then(res => {
        this.tabItems[2].data = res.result;
      })
      listNoBrowsedUserYear().then(res => {
        this.tabItems[3].data = res.result;
      })
    }
  }
}
</script>


<style scoped>

</style>
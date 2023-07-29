<template>
  <v-container fluid class="pa-0 ma-0">
    <archive-list :archiveId="3"> </archive-list>
  </v-container>
</template>

<script>
export default {
  name: "myImportantNotice",
  components: { archiveList: () => import("./articleList/archiveList.vue") },
};
</script>
<template>
  <v-data-table
      :headers="headData"
      :items="tableData"
      :disable-pagination="true"
      :hide-default-footer="true"
      class="elevation-1"
  ></v-data-table>
</template>
<script>
export default {
  props: {
    headData: {
      typeof: Array,
      default: () => {
        return [];
      },
    },
    tableData: {
      typeof: Array,
      default: () => {
        return [];
      },
    },
  },
}

</script>
<style scoped>

</style>
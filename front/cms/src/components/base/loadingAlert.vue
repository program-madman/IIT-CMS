<template>
  <v-dialog width="546" v-model="dialog" persistent>
    <!-- <v-card color="primary" dark>
      <v-card-text>
        {{content}} -->
        <v-progress-linear
          indeterminate
          color="primary"
          class="mb-0"
        ></v-progress-linear>
      <!-- </v-card-text>
    </v-card> -->
  </v-dialog>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: "",
    },
    open: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dialog: this.open,
      content: this.value,
    };
  },

  methods: {
    show(content) {
      this.content = content;
      this.dialog = true;
      
    },
  },
  watch: {
    open() {
      this.dialog = this.open;
    },
  },

};
</script>
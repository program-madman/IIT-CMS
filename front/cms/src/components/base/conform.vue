<template>
  <v-dialog width="546" v-model="dialog">
    <v-card>
      <v-card-title>
        <span>提示</span>
      </v-card-title>
      <v-card-text>{{ content }}</v-card-text>

      <div style="margin-top: 24px; height: 60px">
        <v-btn
          min-width="105px"
          height="36px"
          depressed
          dark
          outlined
          color="#1867C0FF"
          style="margin-left: 157px; margin-right: 24px"
          @click="onCancele"
        >
          取消
        </v-btn>
        <v-btn
          min-width="105px"
          height="36px"
          depressed
          dark
          color="blue"
          @click="onConfirm"
        >
          确认
        </v-btn>
      </div>
    </v-card>
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
      dialog: false,
      content: this.value,
    };
  },

  methods: {
    confirm(content) {
      this.dialog = true;
      return new Promise((resolve, reject) => {
        this.content = content;
        this.onConfirm = (e) => {
          this.dialog = false;
          resolve(e);
        };
        this.onCancele = (error) => {
          this.dialog = false;
          reject(error);
        };
      });
    },
    onConfirm() {},
    onCancele() {},
    close() {
      //this.dialog = false;
      this.$emit("closeDialog");
    },
  },
  watch: {
    open() {
      this.dialog = this.open;
    },
  },
};
</script>
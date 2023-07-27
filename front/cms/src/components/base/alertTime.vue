<template>
  <v-dialog width="546" v-model="dialog" :persistent="true">
    <v-card>
      <v-card-title>
        <span>提示</span>
      </v-card-title>
      <v-card-text>{{ content }}</v-card-text>

      <div style="margin: 24px auto 0px auto; width: 105px; height: 60px">
        <v-btn
          min-width="105px"
          height="36px"
          depressed
          dark
          color="blue"
          class="text-capitalize"
          @click="onConfirm"
        >
          确认 {{ times == 0 ? "" : "(" + times + "s)" }}
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
  },
  data() {
    return {
      dialog: false,
      content: this.value,
      times: 0,
      timer: null,
    };
  },

  methods: {
    alert(content, times = 0) {
      if (this.timer != null) {
        this.times = 0;
        clearInterval(this.timer);
      }
      if (times != 0) {
        this.times = times;
        this.timer = setInterval(() => {
          this.times--;
          if (this.times === 0) {
            this.onConfirm();
            clearInterval(this.timer);
          }
        }, 1000);
      }
      this.dialog = true;
      return new Promise((resolve) => {
        this.content = content;

        this.onConfirm = (e) => {
          this.dialog = false;
          resolve(e);
        };
      });
    },
    onConfirm() {},
  },
};
</script>
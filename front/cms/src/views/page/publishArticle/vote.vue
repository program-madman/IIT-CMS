<template>
  <v-sheet
    class="vote pl-5 pt-4 pb-5 pr-4"
    color="#F5F6F7"
    width="546"
    rounded
  >
    <v-container>
      <v-row height="20px">
        <v-icon height="16px" width="16px"> mdi-vote-outline </v-icon>
        <span class="vote-title ml-2">{{ vote.topic }}</span>
      </v-row>
      <v-row>
        <v-radio-group v-model="radioGroup" class="mt-2">
          <v-radio
            v-for="(item, raindex) in vote.voteOptions"
            :key="raindex"
            :ripple="false"
            :label="item.value"
            on-icon="mdi-check-circle"
            class="vote-radio caption"
          ></v-radio>
        </v-radio-group>
      </v-row>
      <v-row class="mt-0 pa-0">
        <v-btn
          depressed
          color="#1867C0"
          class="white--text"
          width="90"
          height="27"
        >
          投票
        </v-btn>
        <v-spacer></v-spacer>

        <v-btn
          depressed
          plain
          text
          color="#1867c0ff"
          :ripple="false"
          height="27px"
          min-width="24px"
          class="pa-0 ma-0 mr-2"
          @click="onUpdate"
          v-if="!isPreview"
        >
          修改
        </v-btn>

        <v-btn
          depressed
          plain
          text
          color="#1867c0ff"
          :ripple="false"
          height="27px"
          min-width="24px"
          class="pa-0 ma-0"
          @click="onDelete"
          v-if="!isPreview"
        >
          删除
        </v-btn>
      </v-row>
    </v-container>
  </v-sheet>
</template>
<script>
export default {
  props: {
    vote: {
      type: [String, Object],
      default: "",
    },
    isPreview:{
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      radioGroup: 0,
    };
  },

  methods: {
    onDelete() {
      // 父组件 回调传参
      this.$emit("onDelete");
    },
    onUpdate() {
      this.$emit("onUpdate");
    },
  },
};
</script>
<style>
.vote .vote-title {
  color: #333333ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 14px;
  line-height: 20px;
  letter-spacing: 0px;
  text-align: left;
  align-self: center;
}

.vote-radio .v-label {
  color: #333333ff;
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: 18px;
  letter-spacing: 0px;
  text-align: left;
}
.vote .vote-radio {
  height: 14px;
}
.vote .v-icon.v-icon {
  font-size: 14px;
}
.vote .v-icon__svg,
.vote .v-input--selection-controls__input {
  width: 14px;
  height: 14px;
}

.vote .v-btn.v-size--default {
  font-family: PingFang SC;
  font-weight: regular;
  font-size: 12px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}
</style>

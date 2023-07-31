<template>
  <v-text-field
    placeholder="Input title here(no more than 120 words)"
    hide-details="auto"
    single-line
    clearable
    autofocus
    maxlength="70"
    required
    outlined
    v-bind="$attrs"
    v-on="$listeners"
    class="title ml-5 mt-5 mr-5"
  />
</template>

<script>
export default {

};
</script>

<style>
.title {
  min-height: 40px;
}
.title.v-text-field--outlined fieldset {
  border-radius: 4px;
  border: 1px solid #e4e8ebff;
  top: 0px;
}

.title.v-text-field--outlined legend {
  display: none;
}

.title.theme--light.v-input input::-moz-placeholder {
  color: #404040 !important;
  font-family: PingFang SC;
  font-size: 14px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}


.title.v-text-field--outlined > .v-input__control > .v-input__slot {
  height: 40px;
  min-height: 40px;
  padding-left: 16px !important;
}

.title.theme--light.v-input input,
.title.v-text-field input {
  color: #333333ff;
  font-family: PingFang SC;
  font-size: 14px;
  line-height: normal;
  letter-spacing: 0px;
  text-align: left;
}

.title.v-text-field--enclosed .v-input__append-inner {
  margin-top: 8px;
}
</style>
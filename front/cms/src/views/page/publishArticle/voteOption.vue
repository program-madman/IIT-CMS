<template>
  <div class="ml-7 mb-2 mr-7" style="display: inline-flex;">
  <v-text-field
            :placeholder="placeholder"
            hide-details="auto"
            single-line
            :maxlength="maxlength"
            required
            outlined
            :rules="errorMessage?[rules.required] :[]"
            v-model="content"
            class="title text-field"
          />
          
    <v-icon v-if="enableDelete" class="enable-move" @click="onDelete"> mdi-delete </v-icon>
    <span v-if="!enableDelete" class="unenable-move"> * </span>
  </div>
</template>
<script>
import { isNull } from "@/utils/stringUtils.js"

export default {

  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      type: [String, Object],
      default: "",
    },
    placeholder:{
      type: [String, Object],
      default: "",
    },
    errorMessage:{
      type: [String, Object],
      default: "",
    },
    maxlength:{
      type:Number,
      default:30,
    },
    enableDelete: Boolean
  
  },
  data() {
    return {
      rules: {
        required: value => !isNull(value) || this.errorMessage,
      },
      content:this.value,
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
  watch:{
    content(){
      this.$emit("change",this.content)
    },
    value(){
      this.content = this.value;
    }
  }
  
 
};
</script>
<style scoped>
  .text-field{
    display: inline-flex;
    width: 455px;
    margin-right: 8px;
  }
  .enable-move,.unenable-move{
    height:"16px";
    width:"16px";
    color:red;
  }
  .unenable-move{
    margin-left: 5px;
    display: inline-flex;
  }
</style>

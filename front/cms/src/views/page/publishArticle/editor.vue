<template>
  <div class="ml-5 mr-5 mt-2">
    <vue-tinymce
      class="html-content"
      ref="tinymce"
      v-bind="$attrs"
      v-on="$listeners"
      :setting="setting"
      :setup="setup"
    />
  </div>
</template>

<script>
import { showSnackbar } from "@/utils/snackbar.js";
export default {
  model: {
    prop: "content",
    event: "change",
  },
  // props: {
  //   content: {
  //     type: [String, Object],
  //     default: "",
  //   },
  // },
  inheritAttrs: false,
  data() {
    return {
      setting: {
        height: 320,
        branding: false,
        statusbar: false,
        menubar: false,
        auto_focus: false,
        skin: "custom",
        content_css: "custom",
        toolbar:
          " bold italic | bullist numlist | forecolor | fontselect fontsizeselect ",
        toolbar_drawer: "sliding",
        placeholder: "input content here, support html text (no more than 5000 words)",
        font_formats:
          'VWAG TheSans=VWAGTheSans;Arial=arial;Helvetica=helvetica;Times New Roman=Times New Roman;宋体="SimSun";黑体="SimHei";楷体="KaiTi"',

        plugins: "lists ax_wordlimit",
        language: "zh_CN", //本地化设置
        ax_wordlimit_num:5000,
        ax_wordlimit_callback:function(){
          showSnackbar("no more than 5000 words")
        }
      },
      setup: (ed) => {
        ed.on("init", function() {
          
          
          //this.execCommand("fontName", false, "VWAGTheSans");
          //this.getDoc().body.style.fontSize = '12pt';
          //this.getDoc().body.style.fontFamily = "VWAG TheSans";
          //this.getDoc().body.style.lineHeight = "1"
        });
      },
    };
  },
  watch: {
    // message: function () {
    //   this.$emit("change", this.message);
    // },
    content: function () {
      // console.log("++++++>"+this.content)
    },
  },
};
</script>
<style>
#tinymce p {
    margin-top:0px;
    margin-bottom: 0px;
}
</style>

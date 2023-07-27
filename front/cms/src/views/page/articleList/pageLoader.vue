<template>
  <v-btn id="to-top-btn" style="position:fixed;top:90%;right:50px;" fab dark
            small color="primary" @click="backTop">
            <v-icon>
              mdi-arrow-up
            </v-icon>
          </v-btn>
</template>
<script>
import { Debounce } from "@/utils/tools.js";
export default {
  mounted(){
    let topBtn = document.getElementById('to-top-btn');
    topBtn.style.display = 'none';
    window.addEventListener("scroll", this.onScroll);
  },
  methods:{
    onScroll:Debounce(function() {
      console.log("<================================================>")
      let top = document.documentElement.scrollTop || document.body.scrollTop; // 滚动条在Y轴上的滚动距离
      let vh =
        document.compatMode == "CSS1Compat"
          ? document.documentElement.clientHeight
          : document.body.clientHeight; // 浏览器视口的高度
      let height = Math.max(
        document.body.scrollHeight,
        document.documentElement.scrollHeight
      ); // 文档的总高度
      
      let topBtn = document.getElementById('to-top-btn');
      if(top > window.innerHeight){
        topBtn.style.display = 'block';
      }else{
        topBtn.style.display = 'none';
      }
      if (top + vh >= height-500) {
        // 滚动到底部 this.getData(); // 如果已经滚到底了 获取数据
        this.$emit("onLoadMore");
      }
    }),
    backTop() {
        var timer = setInterval(function () {
          let osTop = document.documentElement.scrollTop || document.body.scrollTop;
          let ispeed = Math.floor(-osTop / 5);
          document.documentElement.scrollTop = document.body.scrollTop = osTop + ispeed;
          this.isTop = true;
          if (osTop === 0) {
            clearInterval(timer);
          }
        }, 30)
    },
  },
  destroyed() {
     window.removeEventListener('scroll', this.onScroll);
  },

}
</script>

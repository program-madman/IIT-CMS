import Vue from 'vue'
import App from './App.vue'
import TinymceEditor from './index.js';
Vue.use(TinymceEditor)
new Vue({
  el: '#app',
  render: h => h(App)
})

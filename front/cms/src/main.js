import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import '@/router/permission'

import '@/assets/css/public.css'
import axios from './config/myAxios'
Vue.prototype.$axios = axios
Vue.prototype.$EventBus = new Vue()
import tinymce from 'tinymce'
import VueTinymce from '@packy-tang/vue-tinymce'
import i18n from './i18n'

    

Vue.prototype.$tinymce = tinymce // 将全局tinymce对象指向给Vue作用域下
Vue.use(VueTinymce)              // 安装vue的tinymce组件
Vue.config.productionTip = false
new Vue({
  vuetify,
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app')

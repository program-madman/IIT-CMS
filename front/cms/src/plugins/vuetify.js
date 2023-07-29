import Vue from 'vue';
import i18n from '@/i18n'
import Vuetify from 'vuetify/lib/framework';


Vue.use(Vuetify);

export default new Vuetify({
    lang: {
      t: (key, ...params) => i18n.t(key, params),
    },
    icons: {
        iconfont: 'mdiSvg', // 'mdi' || 'mdiSvg' || 'md' || 'fa' || 'fa4' || 'faSvg'
    }
});

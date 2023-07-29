import Vue from 'vue'
import Vuex from 'vuex'
import reply from './modules/reply'
import user from "./modules/user"
import snackBar from "./modules/snackBar"

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    reply,
    user,
    snackBar,
  }
})

const state = {
  show: false,
  message: "",
  alert:false,
  //alertMessage,
};

const mutations = {
  open(state, content) {
    if (state.show) {
      state.show = false;
      setTimeout(() => {
        state.show = true;
      }, 50);
    } else {
      state.show = true;
    }
    state.message = content;

  },
  close(state) {
    state.show = false;
    state.message = "";
  },
  openAlert(state) {
    if (state.alert) {
      state.alert = false;
    } else {
      state.alert = true;
    }

  },
  closeAlert(state) {
    state.alert = false;
    state.message = "";
  }
};


const actions = {

};
const getters = {

};


//把state, mutations等暴露出去。
export default {
  //需要命名空间
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
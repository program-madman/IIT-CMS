import { menus } from "./menu.js"
const state = {
  authorization: null,
  loginStatus: false,
  userInfo: {
    token: null,
    name: "",
    role: null,
    userId: "",
    positionName: "",
    menuList: [],
    department: "",
    
  }
};

const mutations = {
  setUser(state, user) {
    // 变更状态
    state.userInfo = user;
    state.loginStatus = true;
    //this.storeSession(state);
  },
  setAuthorization(state, authorization) {
    state.authorization = authorization;
  },
  setPosition(state, position){
    state.position = position
  },
  setTicket(state, ticket){
    state.ticket = ticket
  },
  setTicketExpireTime(state, expireTime){
    state.expireTime = expireTime
  },
  storeSession(state) {
    sessionStorage.setItem("authorization", escape(state.authorization))
    sessionStorage.setItem("userInfo", escape(JSON.stringify(state.userInfo)));
    sessionStorage.setItem("position", escape(state.position));
    sessionStorage.setItem("ticket", escape(state.ticket));
    sessionStorage.setItem("expiretime", escape(state.expireTime));

  },
  resetDataFromSession(state) {
    let userInfo = sessionStorage.getItem("userInfo")
    // console.debug("=================>"+userInfo)
    if (userInfo == null || typeof (userInfo) == "undefined") return

    state.loginStatus = true;
    state.authorization = unescape(sessionStorage.getItem("authorization"));
    state.position = unescape(sessionStorage.getItem("position"))
    state.ticket = unescape(sessionStorage.getItem("ticket"))
    state.expireTime = unescape(sessionStorage.getItem("expiretime"))
    state.userInfo = JSON.parse(unescape(userInfo));
  },
};


const actions = {

};
const getters = {
  hasEditPermission: state => {
    return state.userInfo.menuList.indexOf(21) >= 0;
  },
  permissionList: state => {
    return state.userInfo.menuList;
  },
  getIndexPage: state => {
    let menu = state.userInfo.menuList
    menu.sort();
    let index = menu.findIndex(item => item > 10 && item < 21);
    if (index < 0) return null;
    return menus[menu[index]];
  }
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
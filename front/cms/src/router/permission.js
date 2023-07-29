import router from './index'
import store from '@/store';
router.beforeEach(async (to, from, next) => {

  // set page title
  document.title = to.meta.title === undefined ? 'My Message' : to.meta.title
  //console.debug("=============>router path:"+to.path+"    login status:"+store.state.user.loginStatus)
  if (!store.state.user.loginStatus) {
    store.commit('user/resetDataFromSession')
  }

  if (to.path == "/" || to.path == "/login") {
    next();
    return;
  }

  if (!store.state.user.loginStatus) {
    next("/");
    return
  }

  let permission = to.meta.permission;

  if (permission != null && typeof permission != "undefined") {
    if (store.getters["user/permissionList"].indexOf(permission) < 0) {
      next("/")
      return
    }
  }
  next();

})

router.afterEach(() => {

})

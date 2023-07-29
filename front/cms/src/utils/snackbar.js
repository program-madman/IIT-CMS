import store from '@/store';
import { Debounce,Throttle } from "./tools.js"


export const showSnackbar = Debounce(function (content) {
  store.commit("snackBar/open", content);
}, 200)

export const closeSnackbar = () => {
  store.commit("snackBar/close");
}

export const showLoginAlert = Throttle(function () {
  store.commit("snackBar/openAlert");
}, null,30000)

export const closeLoginAlert = () => {
  store.commit("snackBar/closeAlert");
}
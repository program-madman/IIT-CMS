export const Debounce = (fn, delay = 500) => {
  let timer;
  return function () {
    let args = arguments;
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      timer = null;
      fn.apply(this, args);
    }, delay);
  };
};

export const Throttle = (fn, reject = null, interval = 10000) => {
  let last;
  return function () {
    let thisArg = this;
    let args = arguments;
    let now = new Date();
    console.log("data" + last + now + (now - last))
    if (!last || now - last > interval) {
      last = now;
      fn.apply(thisArg, args);

    } else if (typeof (reject) == "function") {

      reject.apply(thisArg, args)
    }
  };
};
export const ThrottleItem = (fn, reject = null, interval = 5000) => {
  //let last;
  return function (item) {

    let thisArg = this;
    let args = arguments;
    let now = new Date();
    console.log(arguments, item)

    if (!item.last || now - item.last > interval) {
      item.last = now;
      fn.apply(thisArg, args);
    } else if (typeof (reject) == "function") {
      reject.apply(thisArg, args)
    }
  };
};

export const deepClone = (target)=>{
  return JSON.parse(JSON.stringify(target))
};
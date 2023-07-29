
import { aesEncrypt } from "@/utils/encryption.js";

//A deep copy function to receive the target parameter
// 定义一个深拷贝函数  接收目标target参数
export const deepClone = (target) => {

    // 定义一个变量
    let result;
    // 如果当前需要深拷贝的是一个对象的话
    if (typeof target === 'object') {
        // 如果是一个数组的话
        if (Array.isArray(target)) {
            result = []; // 将result赋值为一个数组，并且执行遍历
            for (let i in target) {
                // 递归克隆数组中的每一项
                result.push(deepClone(target[i]))
            }
            // 判断如果当前的值是null的话；直接赋值为null
        } else if (target === null) {
            result = null;
            // 判断如果当前的值是一个RegExp对象的话，直接赋值
        } else if (target.constructor === RegExp) {
            result = target;
        } else {
            // 否则是普通对象，直接for in循环，递归赋值对象的所有值
            result = {};
            for (let i in target) {
                result[i] = deepClone(target[i]);
            }
        }
    } else {
        // 如果不是对象的话，就是基本数据类型，那么直接赋值
        result = target;
    }
    // 返回最终结果
    return result;
}

 //文件上传
 export const fileUpload = (event) =>{
    let file = event.target.files;
    for (let i = 0; i < file.length; i++) {
      //上传类型判断
      let imgName = file[i].name;
      let idx = imgName.lastIndexOf(".");
      if (idx != -1) {
        let ext = imgName.substr(idx + 1).toUpperCase();
        ext = ext.toLowerCase();
        if (ext != 'pdf' && ext != 'doc' && ext != 'docx' && ext != 'jpg' && ext != 'jpeg' && ext != 'xls' && ext != 'xlsx') {
          console.log("文件类型错误")
          return null;
        } else {
          console.log("文件类型正确")    
        }
      }
    }
    console.log(file);
    return file;
  }

  //跳转到文章详情界面
  export const jumpToDetail = (router,id) =>{
    console.log("jump to detail article id is =>>>>>>>>>>>> "+id)
    router.push({
      path: "/artcleDetail",
      query: { id: aesEncrypt(id + "") },
    });
  }

  //跳转到文章详情界面带着参数
  export const jumpToDetailWithParams = (router,id,value) =>{
    router.push({
      path: "/artcleDetail",
      query: { id: aesEncrypt(id + ""),param:value},
    });
  }

  export const isNull = (obj) => {
    if(obj === null || JSON.stringify(obj) === "{}") {
      return true;
    }

    if(typeof obj === "undefined") {
      return true;
    }
    return false;
  }

  export const splitString = (text, length, suffix = "...") => {
    if (!text) return;
      if (text.length <= length) {
        return text;
      }
      return (text + "").substring(0, length) + suffix;
  }
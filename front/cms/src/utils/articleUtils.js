export const getFeedbackTypeDesc = (type) => {
  var desc = "";
  switch (type) {
    case 0:
      desc = "不需要回复";
      break;
    case 1:
      desc = "投票";
      break;
    case 2:
      desc = "文字回复";
      break;
    case 3:
      desc = "带附件回复";
      break;
    case 4:
      desc = "需要已回执";
      break;
  }
  return desc;
}

export const getMsgStatusDesc = (type) => {
  switch (type) {
    case 0:
      return "草稿"; 
    case 1:
      return "已发布";
    case 2:
      return "已撤回";
    case 3:
      return "已修改";
    case 4:
      return "已失效";
    case 5:
      return "待发布";
    default:
      return "未知状态";
  }
}

export const getMsgReminderStatusDesc = (type) => {
  switch (type) {
    case "0":
      return "保存了草稿";
    case "1":
      return "发布了文章";
    case "2":
      return "已撤回文章";
    case "3":
      return "重新发布了文章";
    case "4":
      return "提醒您查看";
    default:
      return "未知状态";
  }
}
export const formatHtml = (text) => {
  if (!text) return;
  return text.replace(/<[^>]+>/g, "");
}

export const getMsgStatusCodeByName = (name) => {
  switch (name) {
    case "草稿":
      return  0;
    case "已发布":
      return 1;
    case "已撤回":
      return 2;
    case "已修改":
      return 3;
    case "已失效":
      return 4;
    case "待发布":
      return 5;
    default:
      return -1;
  }
}

export const highLightText = (items, search) => {
  items.filter(function (item) {
    if (search && typeof search !== "undefined") {
      if (
        item.title.indexOf(search) === -1 &&
        item.intro.indexOf(search) === -1
      ) {
        return;
      } else {
        item.title = item.title.replace(
          search,
          search.fontcolor("red")
        );
        item.intro = item.intro.replace(
          search,
          search.fontcolor("red")
        );
      }
    }
    return items;
  });
}

export const highLightAllText = (str, hightStr) => {
  if (!str || !hightStr) return str;
  str = str.replaceAll(hightStr, hightStr.fontcolor("red"));
  return str;
}

export const getDistributeStatus = (type) => {
  var status = "";
  switch (type) {
    case 0:
      status = "草稿";
      break;
    case 1:
      status = "已发布";
      break;
    case 2:
      status = "撤回";
      break;
      // case 3:
      //   status = "已修改";
      // break;
    case 4:
      status = "已失效";
      break;
    case 5:
      status = "未发布";
      break;
    default:
      status = "已发布";
  }
  return status;
}

export const getMsgStatusImg=(status)=> {
  let imgsrc;
  switch (status) {
      case 0:
          imgsrc = require('@/assets/daifabu.png');
          break;
      case 1:
          imgsrc = require('@/assets/yifabu.png');
          break;
      case 2:
      case 3:
          imgsrc = require('@/assets/yichexiao.png');
          break;
      case 4:
          imgsrc = require('@/assets/yishixiao.png');
          break;
      case 5:
          imgsrc = require('@/assets/daifabu.png');
          break;
      default:
          break;
  }
  return imgsrc;
}
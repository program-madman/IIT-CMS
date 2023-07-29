export const isNull = (val) => {
  if (!val) return true;
  //会移除当前图片，所以先过滤掉
  // val = val.replace(/<[^>]+>/g, "");
  // console.log("val1:"+val);
  val = val.replace(/&nbsp;/ig, "");
  var str = val.replace(/(^\s*)|(\s*$)/g, '');//去除空格;
  if (str == '' || str == undefined || str == null) {
      return true;
  } else {
      return false;
  }
}

//保留n位小数
export const roundFun = (numerator,denominator,n = 0) => {
  if(numerator==0){
    return 0;
  }
  
  if(denominator==0){
      return 0;
  }

  let value = numerator*100/denominator;

  return Math.round(value*Math.pow(10,n))/Math.pow(10,n);
}

export const composeStatisticsShowText = (count, totalCount) => {
  return count + "/" + totalCount + "   " + roundFun(count,totalCount) + "%";
}


export const textEllipsis = (text, length, suffix = "...") => {
  if (!text) return;
  if (text.length <= length) {
    return text;
  }
  return (text + "").substring(0, length) + suffix;
}

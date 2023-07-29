// export const timeFormate = (timeStamp)=> {
//   let time = new Date(timeStamp);
//   let year = time.getFullYear();
//   let month =time.getMonth() + 1 < 10? "0" + (time.getMonth() + 1): time.getMonth() + 1;
//   let date =time.getDate() < 10? "0" + new Date(timeStamp).getDate(): new Date(timeStamp).getDate();
//   let hh =time.getHours() < 10? "0" + new Date(timeStamp).getHours(): new Date(timeStamp).getHours();
//   let mm =time.getMinutes() < 10? "0" + new Date(timeStamp).getMinutes(): new Date(timeStamp).getMinutes();
//   let ss =time.getSeconds() < 10? "0" + new Date(timeStamp).getSeconds(): new Date(timeStamp).getSeconds();
//   return year + "-" + month + "-" + date +" "+" "+hh+":"+mm+':'+ss ;
// }

Date.prototype.format = function (format) {
  var args = {
    "M+": this.getMonth() + 1,
    "d+": this.getDate(),
    "h+": this.getHours(),
    "m+": this.getMinutes(),
    "s+": this.getSeconds(),
    "q+": Math.floor((this.getMonth() + 3) / 3), //quarter

    "S": this.getMilliseconds()
  };
  if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var i in args) {
    var n = args[i];

    if (new RegExp("(" + i + ")").test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
  }
  return format;
};

export const isValidDate = (date)=> {
  return date instanceof Date && !isNaN(date.getTime())
}

export const stringToDate = (dataString) =>{
  var pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
  return new Date(dataString.replace(pattern, '$1/$2/$3 $4:$5:$6'));
}

export const offsetDayTime = (offset) => {
  return (new Date(new Date().getTime() + offset * 24 * 60 * 60 * 1000));
}

export const offsetMonthTime = (offset) => {
  let cur = new Date();
  var y = cur.getFullYear();
  var m = cur.getMonth()+offset;
  var dy = m / 12;
  m %= 12;
  if(m < 0 ){
    m += 12;
  }
  y = y +dy;
  return new Date(y, m, cur.getDate(), cur.getHours(), cur.getMinutes(), cur.getSeconds());
}
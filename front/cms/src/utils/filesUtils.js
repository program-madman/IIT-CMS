exports.getFileNameByPath = (path) => {
  var index = path.lastIndexOf("/"); // lastIndexOf("/")  找到最后一个  /  的位置
  var fileName = path.substr(index + 1); // substr() 截取剩余的字符，即得文件名xxx.doc

  return fileName;
};

exports.getUploadRealFileNameByPath = (path) =>{
  var fileName = this.getFileNameByPath(path);
  var timeStampIndex = fileName.lastIndexOf("_");
  var extIndex = fileName.lastIndexOf(".");
  return fileName.substr(0,timeStampIndex)+fileName.substr(extIndex)

}

exports.getDistributorNumberByFileName = (name) =>{
  if(!name || name[0]!= "["){
    return null;
  }
  return name.substr(1,name.indexOf("]")-1)
}

const FILE_TYPE = ['zip','rar','pdf','txt','mp4','jpg','jpeg','png','xps','wmv','7z',
                    'doc','docx','dotx','docm','dotm',
                    'ppt','pot','pps','ppa','pptx','ppsx','ppam','pptm','potm','ppsm',
                    'xls','xlt','xla','xlsx','xltx','xlsm','xltm','xlam','xlsb',
                    '001','002','003','004','005','006','007','008','009','010',
                    '01','02','03','04','05','06','07','08','09','10',
                    '1','2','3','4','5','6','7','8','9'];

exports.INPUT_FILE_ACCEPT = //压缩
                    "application/x-rar-compressed," +
                    ".rar,"+
                    "application/zip," +
                    "application/x-7z-compressed,"+
                    //如果7z 压缩包识别不出来，后缀名可能是
                    ".001,.002,.003,.004,.005,.006,.007,.008,.009,.010,"+
                    ".01,.02,.03,.04,.05,.06,.07,.08,.09,.10,"+
                    ".1,.2,.3,.4,.5,.6,.7,.8,.9,"+
                    //pdf
                    "application/pdf," +
                    //txt
                    "text/plain," +
                    //jpg
                    "image/jpeg," +
                    //png
                    "image/png," +
                    //mp4
                    "video/mp4," +
                    //wmv
                    "video/x-ms-wmv,"+
                    //xps
                    "application/vnd.ms-xpsdocument,"+
                    ".xps,"+
                    //word
                    // ".doc,.docx"+
                    "application/msword," +
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.template," +
                    "application/vnd.ms-word.document.macroEnabled.12," +
                    "application/vnd.ms-word.template.macroEnabled.12," +
                    //excel
                    "application/vnd.ms-excel," +
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet," +
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.template," +
                    "application/vnd.ms-excel.sheet.macroEnabled.12," +
                    "application/vnd.ms-excel.template.macroEnabled.12," +
                    "application/vnd.ms-excel.addin.macroEnabled.12," +
                    "application/vnd.ms-excel.sheet.binary.macroEnabled.12," +
                    "application/vnd.ms-excel.sheet.macroenabled.12,"+
                    //ppt
                    "application/vnd.ms-powerpoint," +
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation," +
                    "application/vnd.openxmlformats-officedocument.presentationml.template," +
                    "application/vnd.openxmlformats-officedocument.presentationml.slideshow," +
                    "application/vnd.ms-powerpoint.addin.macroEnabled.12," +
                    "application/vnd.ms-powerpoint.presentation.macroEnabled.12," +
                    "application/vnd.ms-powerpoint.template.macroEnabled.12," +
                    "application/vnd.ms-powerpoint.slideshow.macroEnabled.12";
                    
                    


exports.checkUploadFileType = (name) =>{
  var idx = name.lastIndexOf(".");
  if (idx != -1) {
    let ext = name.substr(idx + 1).toLowerCase();
    if(FILE_TYPE.indexOf(ext) < 0) {
      return null;
    } else {
      return ext;    
    }
  }
}

const UNITS = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
const STEP =  1024;
exports.readFileSizeUnit = (value) => {
  if(!value || typeof(value) == "undefined") return "";
  value = parseFloat(value, 10);
  for (var i = 0; i < UNITS.length; i++) {
    if (value < Math.pow(STEP, i)) {
      if (UNITS[i - 1]) {
        return format(value, i - 1);
      }
      return value + UNITS[i];
    }
  }
  return this.format(value, i - 1);
}

function format(value, power) {
  return (
    (value / Math.pow(STEP, power)).toFixed(2) + UNITS[power]
  );
}


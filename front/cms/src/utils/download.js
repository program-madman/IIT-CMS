import JSZip from "jszip";
import FileSaver from "file-saver";
import {downFile} from "../api/getData.js";
export const downloadFile = (apFile) => {
    console.log("apFile:"+JSON.stringify(apFile)); 

    let name = apFile.attachmentName  === "" ? apFile.url.slice(apFile.url.lastIndexOf("/") + 1) : apFile.attachmentName;
    let data = {
      "attachmentId": apFile.attachmentId ? apFile.attachmentId : apFile.id
    }
    
    downFile(data).then((res) => {
      FileSaver.saveAs(res, name) 
    })
}

/**
 * 通过URL下载
 * 
 */
export const downloadFileByUrl = (url) => {
    console.log("downloadFileByUrl:"+url);
    let a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    //a.setAttribute('download',fileName);
    document.body.appendChild(a);
    a.click(); //执行下载
    window.URL.revokeObjectURL(a.href);
    document.body.removeChild(a);
}

/**
 * 下载全部
 * @param {*} files 
 * @param {*} fileName 
 * @param {*} callback 0为开始分个下载文件，1为开始打包，2为打包压缩完成，3为下载完成
 */
export const downloadAll = (files, fileName,callback) => {
    console.log("打包压缩以下文件:"+JSON.stringify(files));
    callback(0)
    const zip = new JSZip();
    const promises = [];  //用于存储多个promise
    files.forEach((apFile) => {
        let name = apFile.attachmentName  === "" ? apFile.url.slice(apFile.url.lastIndexOf("/") + 1) : apFile.attachmentName;
        let data = {
          "attachmentId": apFile.attachmentId ? apFile.attachmentId : apFile.id
        }
        const promise = downFile(data).then((res) => {
            zip.file(name, res ,{binary: true});
        })
        promises.push(promise)
    })
    
    Promise.all(promises).then(() => {
        callback(1)
        zip.generateAsync({
          type: "blob",
          compression: "DEFLATE",  // STORE：默认不压缩 DEFLATE：需要压缩
          compressionOptions: {
            level: 9               // 压缩等级1~9    1压缩速度最快，9最优压缩方式
          }
        }).then((res) => {
          callback(2)
          FileSaver.saveAs(res, fileName ? fileName : "压缩包.zip") // 利用file-saver保存文件
          callback(3)
        })
    })
    
}
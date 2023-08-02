import request from "../config/myAxios"
import store from '@/store';


//web端-0 获取用户信息
export const login = (userName,password) => {
    return request({
        url: "user/login",
        headers: {
            "Content-Type": "application/json",
        },
        method: 'post',
        data: {"username":userName,"password":password},
    })
}


//web端-2.1.获取全部文章列表
export const getAllArticle = (query) => {
    console.log(query);
    return request({
        url: 'article/getAllArticles',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'get',
        params: query,
    })
}

//web端-2.1.获取全部文章列表
export const like = (articleId) => {
    console.log(articleId);
    return request({
        url: 'article/likeArticle',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data: {"articleId":articleId},
    })
}


//web端-2.2.获取我的发布文章列表
export const getMyArticle = (query) => {
    return request({
        url: 'article/getAllArticlesPublishedByMe',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'get',
        params: query
    })
}


export const getMyFavArticles = (query) => {
    return request({
        url: 'article/getMyFavArticles',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'get',
        params: query
    })
}


//web端-1 发布文章接口
export const createArticle = (article) => {

    return request({
        url: "article/createArticle",
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data: article,
    })
}


//web端-3.1获取文章详情
export const getArticleDetail = (id) => {
    let newUrl = 'article/getArticleById';
    return request({
        url: newUrl,
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data: {"articleId":id},
    })

}

//web端-1.2 发布文章接口
export const publishArticle = (article) => {
    return request({
        url: "article/createArticle",
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data: article,
    })
}

//web端-1.2 发布文章接口
export const publishComment = (comment) => {
    return request({
        url: "article/addComment",
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data: comment,
    })
}

export const addOrRemoveFavorite = (articleId) => {
    return request({
        url: 'article/addToMyFav',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data: {"articleId":articleId}
    })
}


export const getAllDepartments = () => {
    return request({
        url: 'user/getAllDepartments',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
    })
}


export const getAllUserTypes = () => {
    return request({
        url: 'user/getAllUserTypes',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
    })
}


export const getAllCategorys = () => {
    return request({
        url: 'article/getAllArticleCategories',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
    })
}


export const myProfile = () => {
    return request({
        url: 'user/myProfile',
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
    })
}







//web端-4.获取文章变更日志
export const getArticleLog = (id) => {
    let newUrl = 'message/history';
    return request({
        url: newUrl,
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params:{id:id},
    })
}



export const getMyMessages = () => {
    return request({
        url: "message/getAllMessagesSendToMe",
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
    })
}


export const sendMessage = (message) => {
    return request({
        url: "message/sendMessageToUser",
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data:message
    })
}


export const getSensitive = () => {
    let newUrl = 'sensitiveWord/list';
    return request({
        url: newUrl,
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })
}

export const saveSensitive = (requestParams) => {
    return request({
        url: 'sensitiveWord/update',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: requestParams,
    })
}


//web端-8.获取所有权限
export const getAllPermission = () => {
    let newUrl = 'permission/list';
    return request({
        url: newUrl,
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })

}

//web端-9 上传附件
export const uploadFile = (file,cancelToken=null,onUploadProgress = null) => {
    let format = new FormData();
    format.append('file', file);
    
    return request({
        timeout: 10 * 60 * 1000,
        url: "upload",
        cancelToken:cancelToken,
        headers: {
            "Content-Type": "multipart/form-data",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: format,
        onUploadProgress(progressEvent) {
            if (null != onUploadProgress) {
                if (progressEvent.lengthComputable) {
                    let val = (progressEvent.loaded / progressEvent.total * 100).toFixed(0);
                    // that.formData.showProgress = true;
                    // that.formData.startValue = parseInt(val)
                    console.log(val)
                    onUploadProgress(val, file.name)
                }
            }

        }
    })
}

//web端 下载附件
export const downFile = (query) => {
    return request({
        url: 'download',
        responseType: 'blob',
        headers: {
            'Content-Type': 'application/json; application/octet-stream',
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: query
    })
}



// get user message list
export const getUserMessageList = (pageNumber, size) => {

    return request({
        url: 'message/center/list',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params:  {pageNum:pageNumber,pageSize:size}
    })
}


export const markMessageRead = (id) => {
    return request({
        url: "message/markMessageAsRead",
        headers: {
            "Content-Type": "application/json",
            "token": store.state.user.userInfo.token,
        },
        method: 'post',
        data: {"messageId":id}
    })

}

export const getAuthorityMenu = () => {
    return request({
        url: "permission/menu",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })
    //return resp;
}


export const getDeptList = () => {
    return request({
        url: "oem/list",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })
    //return dept
}

export const getDeptListByAdmin = () => {
    return request({
        url: "oem/noauth/list",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })
    //return dept
}

export const getTemplateList = () => {
    return request({
        url: "role/template/list",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })
    //return template;
}
export const addTemplate = (template) => {
    console.log("add template "+JSON.stringify(template))
        return request({
            url: "role/template/add",
            headers: {
                "Content-Type": "application/json",
                "authorization": store.state.user.authorization,
                "position":store.state.user.position,
                "ticket":store.state.user.ticket,
            },
            method: 'post',
            data:template,
        })
        
    //return mockResult;

}
export const updateTemplate = (template) => {
    console.log("update template "+JSON.stringify(template))
    return request({
        url: "role/template/update",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:template,
    })
    
//return mockResult;
}

export const deleteTemplate = (template) => {
    console.log("delete template "+JSON.stringify(template))
    return request({
        url: "role/template/delete",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:template,
    })    
}

//web端  文章已读
export const messageRead = (data) => {
    return request({
        url: 'message/read',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: data
    })
}

//web端-4.获取文章可见范围
export const getDealerRangeList = (aid) => {
    let newUrl = 'message/dealerList';
    return request({
        url: newUrl,
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: {id:aid},
    })
}

export const getAllList = (query) => {
    console.log("submit server ===> "+JSON.stringify(query))
    return request({
        url: 'message/allList',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: query
    })
}

//获取统计报告列表
export const getDealerReport = (requestParams) => {
    console.log('getDealerReport request:'+JSON.stringify(requestParams))
    return request({
        url: 'message/dealerReport',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },

        method: 'post',
        data: requestParams,
    })
}

//发送提醒
export const sendReportReminder = (requestParams) => {
    console.log('sendReportReminder request:'+JSON.stringify(requestParams))
    return request({
        url: 'message/center/publish',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },

        method: 'post',
        data: requestParams,
    })
}

//发送提醒
export const exportReply = (msgId) => {
    console.log('exportReply request:'+msgId)
    return request({
        url: 'message/exportReply',
        responseType: 'blob',
        method: 'get',
        headers: {
            'Content-Type': 'application/json; application/octet-stream',
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        params: {msgId:msgId}
    })
}


export const getReminderDealers = (msgId,companyCodes) => {
    console.log(msgId,companyCodes);
    return request({
        url: "message/dealers",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:{msgId:msgId,companyCodes:companyCodes}
    })    
}


export const sendMessageRemind = (msgId,companyCodes,userIds) => {
    return request({
        url: "message/remind",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:{msgId:msgId,companyCodes:companyCodes,userIds:userIds},
    })    
}


export const getArticleAreas = (msgId) => {
    return request({
        url: 'report/area',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: {msgId:msgId},
    }) 
}


export const getAllDealerReport = (msgId,areas,dealerType,searchContent) => {
    return request({
        url: "message/allDealerReport",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:{
            msgId:msgId,
            areas:areas,
            dealerType:dealerType,
            searchContent:searchContent
        }
    })    
}


export const getCompanybyarea = (msgId,areas,dealerType,searchContent,readStatus,statisticsStatus) => {
    return request({
        url: "report/companybyarea",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:{
            msgId:msgId,
            areas:areas,
            dealerType:dealerType,
            searchContent:searchContent,
            readStatus:readStatus,
            statisticsStatus: statisticsStatus
        }
    })    
}


export const getReportPosition = (msgId,areas,dealerType,searchContent,companyCode,readStatus,statisticsStatus,attachmentIdParam) => {
    console.log("dealerType:"+dealerType);
    return request({
        url: "report/position",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:{
            msgId:msgId,
            areas:areas,
            dealerType:dealerType,
            searchContent:searchContent,
            companyCode:companyCode,
            readStatus:readStatus,
            statisticsStatus: statisticsStatus,
            attachmentId:attachmentIdParam
        }
    })    
}

export const refreshTicketIfNeed = () => {
    console.log("[[[request refresh ticket ]]]")
    return request({
        url: 'ticket/refresh',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data:{ticket:store.state.user.ticket}
    }) 
}

//文章详情，获取单个附件下载地址
export const getDownloadUrlInADView = (articleId,attachmentId) => {
    return request({
        url: 'download',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: {articleId:articleId,attachmentId:attachmentId},
    }) 
}

//文章详情，获取全部下载-普通附件(0)和定向附件(1)
export const getAllDownloadUrlInADView = (msgId,attachmentType) => {
    return request({
        url: '/download/all',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: {articleId:msgId,attachmentType:attachmentType},
    })
}

//统计报告，回复附件导出
export const getAllDownloadUrlInReportView = (msgId) => {
    return request({
        url: '/message/exportReply',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: {msgId:msgId},
    })
}
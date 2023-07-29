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



//web端-2.2.获取我的发布文章列表
export const getMyArticle = (query) => {
    return request({
        url: 'message/list/mine',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,

        },
        method: 'get',
        params: query
    })
}


//web端-1 发布文章接口
export const saveMessage = (requestParams) => {

    return request({
        url: "message/save",
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
//web端-1.1 保存投票接口
export const saveVote = (requestParams) => {

    return request({
        url: "vote/save",
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
//web端-1.1.1 定向文件检验
export const dealerCodeValid = (code) => {

    return request({
        url: "dealer/code/valid",
        headers: {

            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: {code:code},
    })
}

//web端-1.2 发布文章接口
export const publishMessage = (requestParams) => {
    return request({
        url: "message/push",
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
//web端-1.3 获取发布对象list
export const getDealerList = () => {
    return request({
        url: "dealer/list",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })
}
//web端-1.5 获取发布对象全部部门list
export const getDealerDepList = () => {
    return request({
        url: "dealer/job/list",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
    })
}

//web端-1.5 获取发布对象oem部门list
export const getOemDepList = () => {
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
}

//web端-2.3.获取 草稿箱/已撤回 文章列表
export const getArticleByStatusCategory = (query) => {
    console.debug(JSON.stringify(query))
    return request({
        url: 'message/list/status',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: query
    })
}

//web端-2.3.获取 收藏/重要通知/政策发布 文章列表
export const getArticleByArchiveCategory = (query) => {
    return request({
        url: 'message/list/archive',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: query
    })
}

//web端-2.4 收藏 0：取消 1：我的收藏 2：重要通知 3：政策发布
export const collectionOperation = (requestParams) => {
    return request({
        url: 'message/archive',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: requestParams
    })
}


export const removeCollection = (requestParams) => {
    return request({
        url: 'message/unarchive',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: requestParams
    })
}


//web端-2.5 取消发布
export const withdrawMessage = (id) => {
    return request({
        url: '/message/undo',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: { msgId: id }
    })
}


//web端-2.5 取消发布2
export const withdrawMessageByAdmin = (id) => {
    return request({
        url: '/message/undo/admin',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: { msgId: id }
    })
}
//web端-2.6 删除草稿
export const deleteMessage = (id) => {
    return request({
        url: '/message/delete',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: { msgId: id }
    })
}

//web端-3.1获取文章详情
export const getArticleDetail = (id) => {
    let newUrl = 'message/detail';
    return request({
        url: newUrl,
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        params: {id:id},
    })

}

//web端-3.2文章详情中获取该文章的回复--admin and author
export const getArticleReportAuthor = (data) => {
    console.log("get author report =====>"+JSON.stringify(data))
    return request({
        url: 'feedback',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: data,
    })
}
//web端-3.2文章详情中获取该文章的回复--viewer
export const getArticleReportViewer = (msgId) => {
    return request({
        url: 'feedback/viewer',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: msgId,
    })
}

//web-3.3 获取详情页附件/定向附件
export const getDetailAppend = (msgId) => {
    return request({
        url: 'attachment',
        headers: {
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get',
        params: msgId,
    })
}

//web端-3.4 位置详情回复信息
export const getDetailToReport = (requestParams) => {
    return request({
        url: "message/reply",
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



//web端-6.获取敏感词
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

//web端-7.保存敏感词
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

export const getMessageUnreadCount = () => {
    return request({
        url: 'message/center/unreadcount',
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'get'
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


export const markAllMessageRead = () => {
    return request({
        url: "message/center/markallread",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post'
    })
}

export const markMessageRead = (ids) => {
    return request({
        url: "message/center/readlist",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: {messageIds:ids}
    })

}

export const markMessageReadOne = (id) => {
    console.log("======> read one id == "+id)
    return request({
        url: "message/center/readone",
        headers: {
            "Content-Type": "application/json",
            "authorization": store.state.user.authorization,
            "position":store.state.user.position,
            "ticket":store.state.user.ticket,
        },
        method: 'post',
        data: {id:id}
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

//获取发送提醒公司信息
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

//获取发送提醒公司信息
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

//获取该文章对应区域
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

//获取发送提醒公司信息
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

//获取区域下面的经销商信息
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

//获取公司对应下面的人的内容
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
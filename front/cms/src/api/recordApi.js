import request from "../config/myAxios"
import store from '@/store';

export const listUser = () => {
  return request({
    url: 'query/getUsers',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listBrowse = () => {
  return request({
    url: 'query/getUserBrowsedHistory',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listComment = () => {
  return request({
    url: 'query/getUserComments',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listMessage = () => {
  return request({
    url: 'query/getMessages',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listOperate = () => {
  return request({
    url: 'query/getOperateHistory',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listUserStatistic = () => {
  return request({
    url: 'query/getTotalUserStatistics',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listRead = () => {
  return request({
    url: 'query/getArticleReadStatusDetails',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listAttachment = () => {
  return request({
    url: 'query/getAttachmentDetails',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listLike = () => {
  return request({
    url: 'query/getLikeDetails',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listArticleStatistic = () => {
  return request({
    url: 'query/getArticleStatistics',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}
import request from "../config/myAxios"
import store from '@/store';

export const listSend = () => {
  return request({
    url: 'message/record/send',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listBrowse = () => {
  return request({
    url: 'message/record/browse',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listComment = () => {
  return request({
    url: 'message/record/comment',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listMessage = () => {
  return request({
    url: 'message/record/message',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listOperate = () => {
  return request({
    url: 'message/record/operate',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listUserStatistic = () => {
  return request({
    url: 'message/record/userStatistic',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listRead = () => {
  return request({
    url: 'message/record/read',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listAttachment = () => {
  return request({
    url: 'message/record/attachment',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listLike = () => {
  return request({
    url: 'message/record/like',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}

export const listArticleStatistic = () => {
  return request({
    url: 'message/record/articleStatistic',
    headers: {
      "authorization": store.state.user.authorization,
      "position":store.state.user.position,
      "ticket":store.state.user.ticket,
    },
    method: 'get'
  })
}
import request from "../config/myAxios"
import store from '@/store';

export const listRecentMonthCount = () => {
  return request({
    url: '/statistics/author-recentmonth-count-statistics',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listArticleStatistics = () => {
  return request({
    url: '/statistics/article-statistics',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listTopThreeArticleMonth = () => {
  return request({
    url: '/statistics/queryTopThreeArticleMonth',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}

export const listNoBrowsedUserYear = () => {
  return request({
    url: '/statistics/queryNoBrowsedUserYear',
    headers: {
      "Content-Type": "application/json",
      "token": store.state.user.userInfo.token,
    },
    method: 'get'
  })
}
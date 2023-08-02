import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {

    path: '/',
    name: "index",

    redirect: '/login',
    component: () => import('../views/index.vue'),
    meta: {
      keepAlive: true,
    },

    children: [
      {
        path: 'allArticle',
        name: 'allArticle',
        group:1,
        component: () => import('../views/page/allArticle.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:11,
        }
      },
      {
        path: 'myFavorite',
        name: 'myFavorite',
        group:1,
        component: () => import('../views/page/myFavorite.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:12,
        }
      },
      {
        path: 'myPublish',
        name: 'myPublish',
        component: () => import('../views/page/myPublish.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:13,
        }
      },
      {
        path: 'myWithdraw',
        name: 'myWithdraw',
        component: () => import('../views/page/myWithdraw.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:14,
        }
      },
      {
        path: 'myDraftBox',
        name: 'myDraftBox',
        component: () => import('../views/page/myDraftBox.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:15,
        }
      },
      {
        path: 'myImportantNotice',
        name: 'myImportantNotice',
        component: () => import('../views/page/myImportantNotice.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:16,
        }
      },
      {
        path: 'myPolicyRelease',
        name: 'myPolicyRelease',
        component: () => import('../views/page/myPolicyRelease.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:17,
        }
      },
      {
        path: 'publishArticle',
        name: 'publishArticle',
        //iframeComponent: () => import('../views/page/publishArticle/index.vue'),
        component: () => import('../views/page/publishArticle/index.vue'),
        meta: {
          keepAlive: false,
        }
      },
      {
        path: 'publishRedirect',
        name: 'publishRedirect',
      },
      {
        path: 'artcleDetail',
        name: 'artcleDetail',
        component: () => import('../views/page/articleDetail.vue'),
        meta: {
          keepAlive: false,
        }
      },
      {
        path: 'messageCenter',
        name: 'messageCenter',
        component: () => import('../views/page/messageCenter.vue'),
        meta: {
          keepAlive: false,
        }
      },
      {
        path: 'authorityManagement',
        name: 'authorityManagement',
        component: () => import('../views/page/authorityManagement.vue'),
        meta: {
          keepAlive: true,
          permission:18,
        }
      },
      {
        path: 'sensitiveFilter',
        name: 'sensitiveFilter',
        component: () => import('../views/page/sensitiveFilter.vue'),
        meta: {
          keepAlive: true,
          permission:19,
        }

      },
      {
        path: 'articleManagement',
        name: 'articleManagement',
        component: () => import('../views/page/articleManagement.vue'),
        meta: {
          keepAlive: true,
          permission:20,
        }
      },
      {
        path: 'recordManagement',
        name: 'recordManagement',
        group:1,
        component: () => import('../views/page/record/recordManagement.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:21,
        }
      },
      {
        path: 'statisticsManagement',
        name: 'statisticsManagement',
        group:1,
        component: () => import('../views/page/statistics/statisticsManagement.vue'),
        meta: {
          keepAlive: false,
          group: 1,
          permission:22,
        }
      }
    ],
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login.vue'),
    meta: {
      keepAlive: false,
    }
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  // base: "/mymessage/",//部署的子路径
  scrollBehavior: () => ({ y: 0 }),
  routes
})

//防止重复跳转
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router

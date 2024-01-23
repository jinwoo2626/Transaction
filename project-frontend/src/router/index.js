import { createWebHistory, createRouter } from 'vue-router'
// import { useStore } from 'vuex';
// import Redirect from "@/views/RedirectSet";
// import App from '@/App.vue'
// import {store} from "@/vuex/store";


// const requireAuth = () => (from, to, next) => {
//   // const store = useStore();
//   const token = localStorage.getItem('user_token')
//   if (token) {
//       //  store.commit('setislogin', true)
//     return next()
//   } // isLogin === true면 페이지 이동
//    next('/login') // isLogin === false면 다시 로그인 화면으로 이동
// }

const routes = [
  {
    path: '/',
    name: 'HelloWorld',
    component: () => import("../views/Main.vue"),
  },
  {
    path: '/main',
    name: 'HelloWorld2',
    component: () => import("../views/Main.vue"),
  },
  {
    path: '/login',
    name: 'login',
    component: () => import("../views/Login.vue"),
  },
  {
    path: '/join',
    name: 'join',
    component: () => import("../views/Join.vue"),
  },
  {
    path: '/userinfo',
    name: 'userinfo',
    component: () => import("../views/Userinfo.vue"),
  },
  {
    path: '/addboard',
    name: 'addboard',
    component: () => import("../views/AddBoard.vue"),
    // beforeEnter: requireAuth()
  },
  {
    path: '/transaction',
    name: 'transaction',
    component: () => import("../views/transaction.vue"),
    // beforeEnter: requireAuth()
  },
  {
    path: '/board/:bnum',
    name: 'board',
    component: () => import("../views/Board.vue"),
    // beforeEnter: requireAuth()
    props: true
  },
  {
    path: '/boardupdate/:bnum',
    name: 'boardupdate',
    component: () => import("../views/Boardupdate.vue"),
    // beforeEnter: requireAuth(),
    props: true
  },
  {
    path: '/addaccount',
    name: 'addaccount',
    component: () => import("../views/Addaccount.vue"),
    // beforeEnter: requireAuth()
  },
  // {path: '/oauth2/redirect', component: Redirect},
]

const router = new createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(){
    return{top:0}
  }
});

export default router;
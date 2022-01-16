import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../views/Login";
import Layout from '../layout/Layout'
import User from "../views/User";
import Register from "../views/Register";
import Person from "../views/Person";
import Books from "../views/Books";
import News from "../views/News";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: "/user", // 重定向，訪問/自動跳轉home
    children: [
        {
            path: '/user',
            name: 'User',
            component: User
            // component: () => import("@/views/Home"), // vue3的另一種引入方式

        },
        {
            path: '/person',
            name: 'Person',
            component: Person
        },
        {
            path: '/books',
            name: 'Books',
            component: Books
        },
        {
            path: '/news',
            name: 'News',
            component: News
        }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 在刷新页面的时候重置当前路由
// activeRouter()

// function activeRouter() {
//     const userStr = sessionStorage.getItem("user")
//     if (userStr) {
//         const user = JSON.parse(userStr)
//         let root = {
//             path: '/',
//             name: 'Layout',
//             component: Layout,
//             redirect: "/home",
//             children: []
//         }
//         user.permissions.forEach(p => {
//             let obj = {
//                 path: p.path,
//                 name: p.name,
//                 component: () => import("@/views/" + p.name)
//             };
//             root.children.push(obj)
//         })
//         if (router) {
//             router.addRoute(root)
//         }
//     }
// }
//
// router.beforeEach((to, from, next) => {
//     if (to.path === '/login' || to.path === '/register') {
//         next()
//         return
//     }
//     let user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
//     if (!user.permissions || !user.permissions.length) {
//         next('/login')
//     } else if (!user.permissions.find(p => p.path === to.path)) {
//         next('/login')
//     } else {
//         next()
//     }
// })

export default router

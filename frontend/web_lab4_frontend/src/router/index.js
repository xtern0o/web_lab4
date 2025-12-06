import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/Home.vue'
import Points from '../components/Points.vue'
import Auth from '../components/Auth.vue'
import Admin from '../components/Admin.vue'
import Callback from '../components/Callback.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/points', name: 'Points', component: Points },
  { path: '/auth', name: 'Auth', component: Auth },
  { path: '/admin', name: 'Admin', component: Admin },
  { path: '/callback', name: 'Callback', component: Callback }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

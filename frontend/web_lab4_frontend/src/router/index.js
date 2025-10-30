import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/Home.vue'
import Points from '../components/Points.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/points', name: 'Points', component: Points },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

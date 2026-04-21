import { createRouter, createWebHistory } from 'vue-router'

import DefaultLayout from '../layouts/DefaultLayout.vue'

import Home from '../views/Home.vue'
import Orders from '../views/Orders.vue'
import Planning from '../views/Planning.vue'

const routes = [
  {
    path: '/',
    component: DefaultLayout,
    children: [
      { path: '', component: Home },
      { path: 'orders', component: Orders },
      { path: 'planning', component: Planning }
    ]
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
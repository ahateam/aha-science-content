import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/icon/iconfont.css'
import constData from './commen/constData'
import util from 'ahaapi'
import api from './commen/api/api.js'

import Distpicker from 'v-distpicker'



Vue.use(ElementUI)
Vue.config.productionTip = false
Vue.prototype.$constData = constData
Vue.prototype.$util = util
Vue.prototype.$api = api
Vue.component('v-distpicker', Distpicker)


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

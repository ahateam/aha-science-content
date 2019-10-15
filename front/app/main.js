import Vue from 'vue'
import App from './App'

import api from './commen/api.js'
import util from 'ahaapi'
import constData from './commen/constData.js'
import commen from './commen/commen.js'
import './static/icon/iconfont.css'

Vue.prototype.$api = api
Vue.prototype.$util = util
Vue.prototype.$commen = commen
Vue.prototype.$constData = constData

Vue.config.productionTip = false

App.mpType = 'app'

const app = new Vue({
	...App
})
app.$mount()

import Vue from 'vue'
import Axios from 'axios'

import router from './router'
import App from './App.vue'

import './plugins/vueamap.js' //  vue-amap 高德地图
import './plugins/element.js' //  elemnt组件
import {post,get,put,deletes,toJson,isEmpty} from './plugins/http.js' // 请求方式中间件
import {api} from './plugins/api.js'  // API 链接
import {formatDate,formatGoods} from './plugins/function.js' // 公共方法
import '../public/fonts/iconfont.css' // 阿里图标
import '../public/style.css' // 公共CSS

import skeleton from 'vue-skeleton-component' // 骨架


import md5 from 'js-md5'; // md5
import store from './store' // VUEX

//定义全局变量
Vue.prototype.$api=api;
Vue.prototype.$post=post;
Vue.prototype.$get=get;
Vue.prototype.$put=put;
Vue.prototype.$delete=deletes;
Vue.prototype.$toJson=toJson;
Vue.prototype.$isEmpty=isEmpty;
Vue.prototype.$md5=md5;
Vue.prototype.$formatGoods=formatGoods;


// 时间格式化
Vue.filter('formatDate', function (time) {
    var date = new Date(time*1000);
    return formatDate(date, 'yyyy-MM-dd hh:mm');
});

Vue.filter('formatDateAuto', function (time,str) {
    var date = new Date(time*1000);
    return formatDate(date, str);
});

Vue.config.productionTip = false
Vue.prototype.$axios = Axios
Axios.defaults.baseURL = '/api' // <<<<<<<------此处将所有经过axios的请求加上了/api
Axios.defaults.headers.post['Content-Type'] = 'application/json';


Vue.use(skeleton); // 骨架

// 跳转后返回顶部
router.afterEach(() => {
    window.scrollTo(0,0);
})

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')

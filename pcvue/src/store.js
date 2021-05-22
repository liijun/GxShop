import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isLogin:false,
    userInfo:{},
    permission:[],
    goods_class:[],
  },
  mutations: {
    isLogin(state,userInfo,permission){
      state.isLogin = true;
      state.userInfo = userInfo;
      state.permission = permission;
    },
    set_goods_class (state, goods_class) {
      state.goods_class = goods_class
    },
  },
  actions: {
    set_goods_class ({commit}, goods_class) {
      commit('set_goods_class', goods_class)
    },
  }
})

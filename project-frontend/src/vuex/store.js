// store.js
import { createStore } from 'vuex'
import getters from "./getters"
 import mutations from "./mutation"
 import actions from "./actions"; 

const store = createStore({
    state: {
        user: null,
        isLogin: null,
        isNickname: null,
        token: null,
        tokenExpiry: null // 토큰 만료 시간을 추적하는 변수
    },
    mutations,
    getters,
    actions
})

export default store;
// src/vuex/getters.js
export default {
    getUserId: state => state.userId,
    getErrorState: state => state.errorState,
    getIsAuth: state => state.isAuth,
    getIsLogin: state => state.isLogin,
    getIsNickname: state => state.isNickname,
    loggedIn(state) {
      return !!state.user
    }
  }
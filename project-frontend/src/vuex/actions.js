// src/vuex/actions.js
import {USER_ID, IS_AUTH, ERROR_STATE, IS_LOGIN, IS_NICKNAME} from './mutation_types'
import loginAPI from '../service/loginAPI'

let setUserId = ({commit}, data) => {
  commit(USER_ID, data)
}

let setErrorState = ({commit}, data) => {
  commit(ERROR_STATE, data)
}

let setIsAuth = ({commit}, data) => {
  commit(IS_AUTH, data)
}

let setIsLogin = ({commit}, data) => {
  commit(IS_LOGIN, data)
}

let setNickname = ({commit}, data) => {
  commit(IS_NICKNAME, data)
}

// 백엔드에서 반환한 결과값을 가지고 로그인 성공 실패 여부를 vuex에 넣어준다.
let processResponse = (store, loginResponse) => {
  switch (loginResponse) {
    case 'notFound':
      setErrorState(store, 'Wrong ID or Password')
      setIsAuth(store, false)
      break
    default:
      setUserId(store, loginResponse.user_id)
      setErrorState(store, '')
      setIsAuth(store, true)
      setIsLogin(store, true)
      setNickname(store, loginResponse.data.nickname)
  }
}

export default {
  async login (store, {user_id, user_pw}) {
    let loginResponse = await loginAPI.doLogin(user_id, user_pw)
    processResponse(store, loginResponse)
    return store.getters.getIsAuth  // 로그인 결과를 리턴한다
  }
}
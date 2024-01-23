// src/service/loginAPI.js
import axios from 'axios'
// import store from '../vuex/store'


const getUserInfo = (userId, userPw) => {
  const reqData = {
    'username': userId,
    'password': userPw
  }

  let serverUrl = '//localhost:8080'

  return axios.post(serverUrl + '/api/authenticate', reqData, {
    headers: {
      'Content-type': 'application/json'
    }
  })
}

export default {
  async doLogin(userId, userPw) {
    try {
      const getUserInfoPromise = getUserInfo(userId, userPw)
      const [userInfoResponse] = await Promise.all([getUserInfoPromise])
      if (userInfoResponse.data.length === 0) {
        return 'notFound'
      } else {
        localStorage.setItem('user_token', userInfoResponse.data.token)
        localStorage.setItem('user_role', userInfoResponse.data.authentication.replace(/\[|\]/g, ''))
        localStorage.setItem('user_id', userInfoResponse.data.userid)
        localStorage.setItem('nickname', userInfoResponse.data.nickname)
        return userInfoResponse
      }
    } catch (err) {
      console.error(err)
    }
  }
}
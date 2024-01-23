<template>
  <div>
    <div>
      <h2>로그인</h2>
      <div id="loginForm">
        <form @submit.prevent="fnLogin">
          <div class="loginarea">
            <input class="w3-input" name="uid" placeholder="아이디" v-model="user_id" autocomplete="username"><br>
            <input name="password" class="w3-input" placeholder="비밀번호" v-model="user_pw" type="password" autocomplete="current-password">
          </div>
          <div class="loginbtn">
            <!-- <button type="submit" class="w3-button w3-green w3-round">Login</button> -->
            <button type="submit" class="loginbtn">로그인</button>
            <button type="button" class="historybtn" onclick="history.back()">뒤로가기</button>
            <br>
            <span class="login" @click="join()">회원가입</span>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {mapActions, mapGetters} from 'vuex'   //vuex 추가
import axios from 'axios';

export default {
  name: 'MainPage',
  data() {
    return {
      loginstatus: '',
      user_id: '',
      user_pw: ''
    }
  },
  methods: {
    ...mapActions(['login']),     //vuex/actions에 있는 login 함수

    async fnLogin() {       //async 함수로 변경
      if (this.user_id === '') {
        alert('ID를 입력하세요.')
        return
      }

      if (this.user_pw === '') {
        alert('비밀번호를 입력하세요.')
        return
      }

      //로그인 API 호출 
      try {
        await this.login({user_id: this.user_id, user_pw: this.user_pw}) // await를 사용하여 로그인 API를 호출
        this.$router.push({ path: "/main" });
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('로그인 정보를 확인할 수 없습니다.')
          this.user_id = ''
          this.user_pw = ''
        }
      }
    },
    join(){   //회원가입페이지 이동
        var url = 'http://localhost:8080/join';
        axios.get(url, {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => {
            this.$router.push({ path: "/join" });
        })
        .catch(error => {
            console.log(error);
        })
  }
  },
  computed: {
    ...mapGetters({
      errorState: 'getErrorState'
    })
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Dongle&family=Nanum+Gothic&display=swap');

h2{
  margin: 90px 0;
  font-family: 'Nanum Gothic', sans-serif;
  font-weight: bold;
}
#loginForm {
  margin: 0 auto;
  width: 800px;
  height: 453px;
  margin-top: 50px;
  margin-bottom: 100px;
}
.loginarea{
  height: 100px;
  margin-bottom: 50px;
}
.loginarea input{
  width: 240px;
  margin-bottom: 10px;
}
.loginbtn{
  margin: 0 auto;
}
.loginbtn button{
  margin-left: 5px;
  background-color: #123951;
  width: 120px;
  height: 30px;
  padding: 5px;
  font-size: 15px;
  color: #fdf6e3;
  border-style: double;
  border-width: 1px;
  border-radius: 3px;
  border-color: #123951;
  cursor: pointer;
  margin-bottom: 16px;
}
.loginbtn span{
  font-family: 'Nanum Gothic', sans-serif;
  font-weight: bold;
  cursor: pointer;
}
</style>
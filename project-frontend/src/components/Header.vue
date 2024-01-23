<template>
  <div class="mainarea">
    <div class="maintop">
      <!-- <img src = "@/assets/images/dbridge.png" /> -->
      <span>{{msg}}</span>
    </div>
    <div class="link">
      <span class="mainpage" @click="main()">메인</span>
      <span class="userinfo" @click="userinfo()">회원정보</span>
      <span class="transaction" @click="transaction()">중고거래</span>
    </div>
    <div class="link2">
      <span class="login" v-if="!this.$store.state.isLogin" @click="login()">로 그 인</span>
      <span class="logout"  v-if="this.$store.state.isLogin" @click="logout()">로그아웃</span>
      <span class="nickname" v-if="$store.state.isLogin">{{ $store.state.isNickname }}님</span>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import store from '../vuex/store'

export default {
  name: 'HeaderPage',
  data(){
    return{
        userid:'',
        userpw:'',
        Login: '',
    }
  },
  computed: {
    dynamicNickname() {
      console.log(localStorage.getItem('nickname') )
      return localStorage.getItem('nickname'); //
    }
  },
  props:{
    msg: String
  },
  created(){
    this.isLogin();
  },
  methods:{
    isLogin(){
      this.Login = localStorage.getItem("login");
    },
    main(){
        var url = 'http://localhost:8080/main';
        axios.get(url,{
    })
    .then(() => {
        this.$router.push({ path: "/main" });
    })
    .catch(() => {
        this.$router.push({ path: "/login" });
    })
  },
    userinfo(){
        var url = 'http://localhost:8080/userinfo';
        axios.get(url)
    .then(() => {
        this.$router.push({ path: "/userinfo" });
    })
    .catch(() => {
            alert("로그인을 진행해주세요")
          this.$router.push({ path: "/login" });
          this.localStorageset();
          
    })
  },
    transaction(){
        var url = 'http://localhost:8080/transaction';
        axios.get(url,{
    })
    .then(() => {
        this.$router.push({ path: "/transaction" });
    })
    .catch(() => {
        alert("로그인을 진행해주세요")
        this.$router.push({ path: "/login" });
        this.localStorageset();
    })
  },
  login(){
        var url = 'http://localhost:8080/login';
        axios.get(url,{
        params: {
            // id : this.userid,
            // pw : this.userpw
        }
    })
    .then(() => {
        this.$router.push({ path: "/login" });
    })
    .catch(() => {
        this.$router.push({ path: "/login" });
    })
  },
  logout(){
        var url = 'http://localhost:8080/logout'; //url수정하기
        axios.get(url, null, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    withCredentials: true // 쿠키를 전송하기 위해 필요한 설정
  })
    .then(() => {
        this.$router.push({ path: "/login" });
        this.localStorageset();
    })
    .catch(() => {
        this.$router.push({ path: "/login" });
        this.localStorageset();
    })
  },
  localStorageset(){
      store.commit('IS_LOGIN', false) // 로그인정보, 토큰정보 삭제
      localStorage.removeItem('user_token'); //토큰정보, 권한, 유저아이디 삭제
      localStorage.removeItem('db_token'); //토큰정보, 권한, 유저아이디 삭제
      localStorage.removeItem('user_role');
      localStorage.removeItem('user_id');
  }
}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.maintop{
  display: block;
  height: 50px;
}
.maintop img{
  /* float: left; */
}
.maintop span{
  font-size: 35px;
  font-weight: bold;
}
.link{
  width: 75%;
  height: 40px;
  margin-top: 25px;
  padding-top: 5px;
  background-color: #002b36;
  display: inline-block;
  float: left;
}
.link2{
  width: 25%;
  height: 40px;
  margin-top: 25px;
  padding-top: 5px;
  background-color: #002b36;
  display: inline-block;
}
.mainpage{
  text-decoration-line: none;
  margin-right: 50px;
  margin-left: 38%;
  font-size: 20px;
  color: white;
  cursor: pointer;
}
.userinfo{
  text-decoration-line: none;
  margin-right: 50px;
  font-size: 18px;
  color: white;
  cursor: pointer;
}
.transaction{
  text-decoration-line: none;
  margin-right: 50px;
  font-size: 18px;
  color: white;
  cursor: pointer;
}
.nickname{
  text-decoration-line: none;
  margin-right: 50px;
  font-size: 18px;
  color: rgb(255, 255, 255);
  cursor: pointer;
  float: right;
}
.login{
  text-decoration-line: none;
  margin-right: 50px;
  font-size: 18px;
  color: white;
  cursor: pointer;
  float: right;
}
.logout{
  text-decoration-line: none;
  margin-right: 50px;
  font-size: 18px;
  color: white;
  cursor: pointer;
  float: right;
}
</style>

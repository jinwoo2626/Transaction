<template>
  <div class="joinarea">
    <div class="join_text">
        <h2>회원가입</h2>
    </div>
    <div class="joininput">
      <input type="text" placeholder="아이디" id="username" v-model="username" @input="handleInputChange">
      <button type="button" class="check_duplicate" @click="check_duplicate()">중복체크</button><br>
      <input type="password" placeholder="비밀번호(영문/숫자포함 8자이상)" id="userpw" v-model="password" @input="checkPassword"><br>
      <span class="pwvalid" v-if="!(password && !isValidPassword)"></span>
      <span class="pwvalid" v-if="password && !isValidPassword">영문자와 숫자를 포함한 8자 이상이어야 합니다.</span>
      <input type="password" placeholder="비밀번호확인" id="userpwck" v-model="ckpassword" @input="checksamePassword"><br>
      <span class="pwck" v-if="!(ckpassword && !isSamePassword)"></span>
      <span class="pwck" v-if="ckpassword && !isSamePassword">비밀번호가 서로 다릅니다.</span>
      <input type="text" placeholder="닉네임" id="nickname" v-model="nickname"><br>
      <input type="text" placeholder="이메일" id="email" v-model="emailPart1" @change="validateEmail">@
      <select id="emaillast" v-model="emailPart2">
        <option value="gmail.com">gmail.com</option>
        <option value="naver.com">naver.com</option>
        <option value="daum.net">daum.net</option>
        <option value="nate.net">nate.net</option>
      </select>
   </div>
   <div class="joinbtn">
      <button type="button" class="joinbtn" @click="joinlater()">회원가입</button>
      <button type="button" class="historybtn" onclick="history.back()">뒤로가기</button>
   </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MainPage',
  data(){
    return{
      username:'',
      password:'',
      ckpassword:'',
      nickname: '',
      emailPart1: '', // @ 앞부분
      emailPart2: 'gmail.com', // @ 뒷부분 중 초기 선택값
      ch_duplicate: false,
      isValidPassword: '',
      isSamePassword: false,
      isEmailValid: false,
    }
  },
  methods:{
    check_duplicate(){
      if (this.username === '') {
        alert('아이디를 입력하세요.')
      }else{
          var url = 'http://localhost:8080/check_duplicate';
          axios.get(url, { params: { username: this.username } })
          .then((response) => {
              if(response.data == "사용가능"){
                alert("사용 가능한 아이디입니다.")
                this.ch_duplicate = true;
              }else{
                alert("사용중인 아이디입니다.")
                this.ch_duplicate = false;
              }
          })
          .catch((error) => {
              console.log(error);
          });
      }
    },
    handleInputChange() {
      this.ch_duplicate = false;
    },
    checkPassword() {
      const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/; //비밀번호 유효성 검증 / 영문/숫자포함 8자이상
      this.isValidPassword = passwordRegex.test(this.password);
      this.checksamePassword();
    },

    checksamePassword(){
      if(this.password == this.ckpassword){
        this.isSamePassword = true;
      }else{
        this.isSamePassword = false;
      }
    },

    validateEmail() {
      // 이메일 부분1이 영어와 숫자로만 이루어져 있는지 확인
      const isPart1Valid = /^[a-zA-Z0-9]+$/.test(this.emailPart1);

      // 선택된 이메일 도메인이 유효한지 확인
      const validEmailDomains = ['gmail.com', 'naver.com', 'daum.net', 'nate.net'];
      const isPart2Valid = validEmailDomains.includes(this.emailPart2);

      // 유효성 검사 결과에 따라 상태 업데이트
      this.isEmailValid = isPart1Valid && isPart2Valid;
    },
    joinlater(){  //회원가입
      if (this.username === '') {
        alert('아이디를 입력하세요.')
      }
      else if (this.password === '') {
        alert('비밀번호를 입력하세요.')
      }
      else if (this.nickname === '') {
        alert('이름을 입력하세요.')
      }
      else if (this.emailPart1 === '') {
        alert('이메일을 입력하세요.')
      }else if(this.ch_duplicate == false){
        alert("아이디 중복체크를 진행해주세요")
      }else if(this.isValidPassword == false){
        alert("비밀번호를 알맞게 적어주세요")
      }else if(this.isSamePassword == false){
        alert("비밀번호가 서로 다릅니다.")
      }else if (this.isEmailValid == false) {
        alert('유효한 이메일을 입력해주세요.')
      }else{
        var url = 'http://localhost:8080/api/join';
        var data = {
          username: this.username,
          password : this.password,
          nickname : this.nickname,
          email : this.emailPart1 + "@" + this.emailPart2
        }
        axios.post(url, JSON.stringify(data), {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => {
            alert("회원가입이 완료되었습니다.")
            this.$router.push({ path: "/main" });
        })
        .catch(error => {
            console.log(error);
        })
      }
  },
}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Dongle&family=Nanum+Gothic&display=swap');

  .joinarea{
    margin: 0 auto;
    width: 500px;
  }
  h2{
    margin: 90px 0;
    font-family: 'Nanum Gothic', sans-serif;
    font-weight: bold;
  }
  .join_text span{
    font-size: 30px;
    font-weight: bold;
    color: #002b36;
  }
  .joininput{
    margin: 0;
    margin-bottom: 35px;
  }
  .joininput input{
    width: 245px;
    margin-bottom: 30px;
  }
  #userpw{
    width: 245px;
    margin-bottom: 0px;
  }
  #userpwck{
    width: 245px;
    margin-bottom: 0px;
  }
  .pwvalid{
    margin-left: 130px;
    text-align: left;
    height: 30px;
    display: block;
  }
  .pwck{
    margin-left: 130px;
    text-align: left;
    height: 30px;
    display: block;
  }
  #username{
    width: 160px;
  }

  #email{
    width: 113px;
    margin-right: 5px;
  }

  #emaillast{
    width: 100px;
    height: 30px;
    margin-left: 5px;
  }
  .joininput button{
    margin-left: 5px;
    background-color: #123951;
    width: 80px;
    height: 30px;
    padding: 5px;
    font-size: 15px;
    color: #fdf6e3;
    border-style: double;
    border-width: 1px;
    border-radius: 3px;
    border-color: #123951;
    cursor: pointer
  }
  .joinbtn button{
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
    margin-bottom: 85px;
  }
</style>

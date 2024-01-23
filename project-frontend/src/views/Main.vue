<template>
  <div class="mainarea">
    <div class="main_maintext">
        <span>중고거래</span>
    </div>
    <div class="main">
      <div class="main_des">
        <div class="des_area1">
            <img src = "@/assets/images/user.png" /><br>
            <span class="des_header">회원</span>
            <span class="des_content">간편한 회원가입과 탈퇴기능</span>
        </div>
        <div class="des_area2">
            <img src = "@/assets/images/box.png" /><br>
            <span class="des_header">중고물품</span>
            <span class="des_content">사용하지 않는 중고물품을 거래</span>
        </div>
        <div class="des_area3">
            <img src = "@/assets/images/money.png" /><br>
            <span class="des_header">거래</span>
            <span class="des_content">입금, 출금, 이체</span>
        </div>
      </div>
     </div>
  </div>
</template>

<script>
// import axios from 'axios';

export default {
  name: 'MainPage',
  data(){
    return{
        userid:'',
        userpw:'',
        username:''
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    img {
      width: 80px;
      height: 80px;
    }
    .main_maintext{
        height: 150px;
        display: block;
        margin: 0 auto;
        margin-top: 50px;
        margin-bottom: 100px;
        text-align: center;
    }
    .main_maintext span{
        font-size: 20px;
        font-weight: bold;
        color: #002b36;
    }
    .main{
        width: 780px;
        height: 371px;
        margin: 0 auto;
        margin-bottom: 40px;
    }
    .main .des_area1{
        width: 250px;
        margin: 0 5px;
        float: left;
    }
    .main .des_area2{
        width: 250px;
        margin: 0 5px;
        float: left;
    }
    .main .des_area3{
        width: 250px;
        margin: 0 5px;
        float: left;
    }
    .main .des_header{
        width: 250px;
        margin-bottom: 10px;
        float: left;
        color: 	#1C1C1C;
        font-weight: bold;
        font-size: 16px;
        text-align: center;
    }
    .main .des_content{
        color: #6E6E6E;
        font-weight: bold;
        text-align: left;
    }
</style>

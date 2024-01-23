<template>
  <div class="addaccountarea">
    <div class="add_text">
        <span>계좌등록</span>
    </div>
    <div class="addaccountmain">
      <div class="addaccounttext">
          <span class="subject">계좌번호</span>
          <span class="content">은행명</span>
          <span class="name">보유금액</span>
        </div>
      <div class="addaccountinput">
        <input type="text" placeholder="10자 이상이어야 하며, 숫자와 하이픈(-)만 허용됩니다" id="accountnum" maxlength='30' v-model="accountnum"><br>
        <select id="bankselect" v-model="bankinfo" @change="validateBankInfo">
          <option value="" disabled selected>은행을 선택하세요</option>
          <option value="국민은행">국민은행</option>
          <option value="하나은행">하나은행</option>
          <option value="우리은행">우리은행</option>
          <option value="신한은행">신한은행</option>
        </select>
        <input type="number" placeholder="보유금액" id="amount" v-model="amount"><br>
    </div>
  </div>
   <div class="addaccountbtn">
      <button type="button" class="addaccount" @click="validateAccountNumber()">계좌등록</button>
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
      accountnum:'',
      bankinfo: '',
      amount: '',
    }
  },
  methods:{
    getuser_token(){  //계좌 등록 / client_credentials > 토큰 발급
      if(localStorage.getItem('db_token') == null){
                this.loadstatus = false;
                var url = 'http://localhost:8080/getuser_token';
                axios.get(url, { params: { userid: localStorage.getItem("user_id") } })
                .then((response) => {
                    localStorage.setItem('db_token', response.data.access_token);
                    this.addaccount(localStorage.getItem('db_token'));
                })
                .catch((error) => {
                    console.log(error);
                });
            }else{
                this.addaccount(localStorage.getItem('db_token'));
            }
    },
    validateAccountNumber() {
        // 유효성 검사
        var isValidacnum = /^[0-9-]{10,}$/.test(this.accountnum);

        if(isValidacnum  == false){
          alert("계좌번호가 유효하지 않습니다. 10자 이상이어야 하며, 숫자와 하이픈(-)만 허용됩니다.");
        }else if(this.bankinfo == ''){
          alert("은행명을 선택해주세요.")
        }else if(this.amount < 0 || this.amount == ''){
          alert("보유금액을 올바르게 작성해주세요.")
        }else{
          this.getuser_token()
        }

    },
    addaccount(dbToken){
      var url = 'http://localhost:8080/addaccount';
      var data = {
          dbtoken: dbToken,
          accountnum : this.accountnum,
          bankinfo : this.bankinfo,
          amount : this.amount
        }
        axios.post(url, JSON.stringify(data), {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => {
            alert("계좌 등록이 완료되었습니다.")
            this.$router.push({ path: "/userinfo" });
        })
        .catch((error) => {
            console.log(error);
        });
    }
}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .addaccountarea{
      margin: 0 auto;
      width: 800px;
    }
    .add_text{
      height: 150px;
      display: block;
      margin: 0 auto;
      margin-top: 50px;
      text-align: center;
    }
    .add_text span{
      font-size: 30px;
      font-weight: bold;
      color: #002b36;
    }
    .addaccountmain{
      width: 800px;
      height: 300px;
    }
    .addaccounttext{
      width: 150px;
      padding-top: 10px;
      display: inline-block;
    }
    .addaccountinput{
      margin: 0 auto;
      width: 650px;
      float: right;
      display: inline-block;
    }
    .addaccounttext span{
      width: 150px;
      display: block;
      margin-bottom: 20px;
      font-family: 'Nanum Gothic', sans-serif;
      font-weight: bold;
    }
    .addaccountinput input{
      width: 620px;
      height: 40px;
      margin-bottom: 5px;
    }
    input{
      font-size: 13px;
      margin-bottom: 5px;
      border-radius: 5px;
      border-width: 3px;
      border-color: #001216;
    }
    #bankselect{
      width: 620px;
      height: 40px;
      margin-bottom: 5px;
      border-radius: 5px;
      border-width: 3px;
      border-color: #001216;
    }

    .addaccountbtn button{
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
      margin-bottom: 141px;
    }
</style>

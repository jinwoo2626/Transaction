<template>
    <div class="infoarea">
      <div class="info_maintext">
          <!-- <span v-text="nickname"></span>
          <span v-if="this.amount != null">보유금액 = {{ this.amount }} 원</span>
          <span v-if="this.amount == null">계좌 연결을 진행해주세요</span> -->
      </div>
      <div class="main">

        <div class="tablearea">
        <div class="myinfo_head">
            <span>나의 계좌정보</span>
        </div>
        <div class="myinfo_headbtn">
            <button type="button" class="addcountbtn" v-if="this.accountnum != null" @click="addaccount()">계좌 추가</button>
            <button type="button" class="addcountbtnnon" v-if="this.accountnum == null" @click="addaccount()">계좌 추가</button>
        </div>
        <div class="nontable" v-if="this.accountnum == null">
                <span>계좌 연결을 진행해주세요.</span>
            </div>
        <table class="myinfo" v-if="this.accountnum != null">    
            <thead>
                <tr>
                <th class="myinfo-sel1">계좌번호</th>
                <th class="myinfo-sel3">은행명</th>
                <th class="myinfo-sel3">잔액</th>
                <th class="myinfo-sel4">입금 / 출금</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                <td v-if="this.accountnum != null">{{ this.accountnum }}</td>
                <td v-if="this.bankinfo != null">{{ this.bankinfo }}</td>
                <td v-if="this.amount != null">{{ this.amount }}원</td>
                <td v-if="this.amount != null">
                    <button class="depositbtn" @click="checkdeposit(this.accountnum)">입금</button>
                    <button class="withdrawalbtn" @click="checkwithdrawal(this.accountnum)">출금</button></td>
                </tr>
            </tbody>
        </table>
        </div>

        <div class="tablearea">
            <div class="myinfo_head">
                <span>받은 예약요청</span>
            </div>
            <div class="nontable" v-if="!(board && board.length > 0)">
                <span>받은 예약 요청이 없습니다.</span>
            </div>
            <table class="myinfo" v-if="board && board.length > 0">
            <thead>
                <tr>
                <th class="myinfo-sel1">게시글</th>
                <th class="myinfo-sel2">회원명</th>
                <th class="myinfo-sel3">요청수량</th>
                <th class="myinfo-sel4">상품금액</th>
                <th class="myinfo-sel5">거래하기</th>
                <th class="myinfo-sel6">예약거절</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="group in board" :key="group">
                <td v-if="group.boardsub != null">{{ group.boardsub }}</td>
                <td v-if="group.nickname != null">{{ group.nickname }}</td>
                <td v-if="group.quantity != null">{{ group.quantity }}개</td>
                <td v-if="group.productprice != null">{{ group.productprice }}원</td>
                <!-- <td v-if="group.quantity != null"><button class="transbtn" @click="getuser_token('trans')">거래하기</button></td> -->
                <td v-if="group.productprice != null"><button class="transbtn" @click="trans(group.boardnum, group.userid, group.quantity, group.reserveid)">거래</button></td>
                <td v-if="group.productprice != null"><button class="refusebtn" @click="refuse(group.reserveid, 'refuse')">거절</button></td>
                </tr>
            </tbody>
        </table>
        </div>  

        <div class="tablearea">
        <div class="myinfo_head">
            <span>보낸 예약요청</span>
        </div>
        <div class="nontable" v-if="!(myboard && myboard.length > 0)">
            <span>보낸 예약 요청이 없습니다.</span>
        </div>
        <table class="myinfo" v-if="myboard && myboard.length > 0">   
            <thead>
                <tr>
                <th class="myinfo-sel1">게시글</th>
                <th class="myinfo-sel2">요청수량</th>
                <th class="myinfo-sel3">상품금액</th>
                <th class="myinfo-sel4">예약취소</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="group in myboard" :key="group">
                <td v-if="group.boardsub != null">{{ group.boardsub }}</td>
                <td v-if="group.quantity != null">{{ group.quantity }}개</td>
                <td v-if="group.productprice != null">{{ group.productprice }}원</td>
                <td v-if="group.productprice != null"><button class="refusebtn" @click="refuse(group.reserveid, 'cancle')">취소</button></td>
                </tr>
            </tbody>
        </table>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'MainPage',
    data(){
      return{
          board: [],
          myboard: [],
          accountnum: '',
          bankinfo: '',
          amount: '',
          loadstatus: false,
          nickname: ''
      }
    },
    created(){
        // this.getuserboard();
        this.reservationlist();
        this.myreservationlist(localStorage.getItem("user_id"))
        this.getuser_token("select")
        this.nickname = localStorage.getItem("nickname")
    },
    methods:{
        getuser_token(status){  //client_credentials > 토큰 발급
            this.loadstatus = false;

            if(localStorage.getItem('db_token') == null){
                var url = 'http://localhost:8080/getuser_token';
                axios.get(url, { params: { userid: localStorage.getItem("user_id") } })
                .then((response) => {
                    localStorage.setItem('db_token', response.data.access_token);

                    if(status == "select"){
                        this.getuseramount();
                    }
                })
                .catch((error) => {
                    console.log(error);
                });
            } else{
                if(status == "select"){
                    this.getuseramount();
                }
            }
            
        },
        getuseramount(){ //보유 금액 조회

            var url = 'http://localhost:8080/getuseramount';
            axios.get(url, { 
                params: { dbToken: localStorage.getItem('db_token') } 
                }
            )
            .then((response) => {
                if(response.data.response[0].AMOUNT != -1){
                    this.accountnum = response.data.response[0].ACCOUNTNUM;
                    this.bankinfo = response.data.response[0].BANKINFO;
                    this.amount = response.data.response[0].AMOUNT;
                    this.regularexpression();
                    this.loadstatus = true;
                } else{
                    this.accountnum = null;
                    this.bankinfo = null;
                    this.amount = null;
                    this.loadstatus = true;
                }
            })
            .catch((error) => {
                console.log(error);
            });
        },
        checkdeposit(){ //입금 금액 체크
         var depositamount = prompt("입금하시려는 금액을 입력해주세요: ");

            // 입력된 값이 null 또는 빈 문자열인 경우 처리
          if (depositamount === null) {
              return;
          }
          else if (depositamount === "") {
              alert("입금하시려는 금액을 입력해주세요.");
          }
          else if(depositamount < 1000){
            alert("입금은 1000원 이상부터 가능합니다.");
          }
          else if(isNaN(depositamount)) {  // 입력된 값이 숫자가 아닌 경우 처리
              alert("유효한 금액을 입력해주세요.");
          }else{
            depositamount = parseInt(depositamount);

            this.deposit(depositamount);
          }
        },
        deposit(depositamount){ //입금

            this.getuser_token();

            var url = 'http://localhost:8080/deposit';
            var data = {
                dbtoken: localStorage.getItem('db_token'),
                amount: depositamount
            }
            axios.post(url, JSON.stringify(data), {
            headers: {
                "Content-Type": `application/json`
            }}
            )
            .then((response) => {
                    alert(response.data.message);
                    this.amount = response.data.amount;
                    this.regularexpression();
                })
            .catch((error) => {
                alert(error.data.message);
            });
        },
        checkwithdrawal(){ //출금 금액 체크
            var withdrawalamount = prompt("출금하시려는 금액을 입력해주세요: ");

                // 입력된 값이 null 또는 빈 문자열인 경우 처리
            if (withdrawalamount === null) {
                return;
            }
            else if (withdrawalamount === "") {
                alert("출금하시려는 금액을 입력해주세요.");
            }
            else if(withdrawalamount < 1000){
                alert("출금은 1000원 이상부터 가능합니다.");
            }
            else if(isNaN(withdrawalamount)) {  // 입력된 값이 숫자가 아닌 경우 처리
                alert("유효한 금액을 입력해주세요.");
            }else{
                withdrawalamount = parseInt(withdrawalamount);

                this.withdrawal(withdrawalamount);
            }
        },
        withdrawal(withdrawalamount){ //출금

            this.getuser_token();

            var url = 'http://localhost:8080/withdrawal';
            var data = {
                dbtoken: localStorage.getItem('db_token'),
                amount: withdrawalamount
            }
            axios.post(url, JSON.stringify(data), {
            headers: {
                "Content-Type": `application/json`
            }}
            )
            .then((response) => {
                    alert(response.data.message);
                    this.amount = response.data.amount;
                    this.regularexpression();
                })
            .catch((error) => {
                alert(error.data.message);
            });

        },
        addaccount(){
            if(this.amount != null){
                alert("이미 등록된 계좌가 있습니다.")
            }else if(this.loadstatus == false){
                alert("잠시후에 다시 시도해주세요.")
            }
            else{
                var url = 'http://localhost:8080/addaccount';
                axios.get(url)
                .then(() => {
                    this.$router.push({ path: "/addaccount" });
                })
                .catch((error) => {
                    console.log(error);
            });
            }
        },
        // getuserboard(){     //회원이 작성한 게시글(상품게시글)번호 가져오기
        //     var vm = this;
        //     var url = 'http://localhost:8080/userboard';
        //     axios.get(url, { params: { userid: localStorage.getItem("user_id") } })
        //     .then((response) => {
        //         vm.board = response.data;

        //         for(var i = 0; i < response.data.length; i++){
        //             this.reservationlist(response.data[i]);
        //         }
        //     })
        //     .catch((error) => {
        //         console.log(error);
        //     });
        // },
        reservationlist(){  //예약정보를 불러오기
            var vm = this;
            var url = 'http://localhost:8080/reservationlist';
            axios.get(url, { params: { hostuserid: localStorage.getItem("user_id") } })
            .then((response) => {
                vm.board.push(...response.data);
            })
            .catch((error) => {
                console.log(error);
            });
        },
        myreservationlist(userid){  //나의 거래 예약 정보 불러오기
            var vm = this;
            var url = 'http://localhost:8080/myreservationlist';
            axios.get(url, { params: { userid: userid } })
            .then((response) => {
                vm.myboard.push(...response.data);
            })
            .catch((error) => {
                console.log(error);
            });
        },
        trans(bnum, userid, quantity, reserveid){  
            this.getuser_token()

            //  중고거래
            var url = 'http://localhost:8080/trans';
            var data = {
                dbtoken: localStorage.getItem('db_token'),
                boardnum : bnum,    //게시글 번호
                hostuserid : localStorage.getItem("user_id"), //판매할 회원
                userid : userid,    //구매할 회원
                quantity : quantity, //구매할 수량
                reserveid : reserveid
            }
            axios.post(url, JSON.stringify(data), {
            headers: {
            "Content-Type": `application/json`
            }}
            )
            .then((response) => {
                    if(response.data.message == "거래가 완료되었습니다."){
                        alert(response.data.message);
                        this.amount = response.data.amount;
                        this.regularexpression();
                        this.board = this.board.filter(reservation => reservation.reserveid !== reserveid);
                    }else if(response.data.message == "거래가 미완료되었습니다."){
                        alert("거래가 미완료되었습니다.")
                    }else if(response.data.message == "구매자의 잔액이 부족합니다."){
                        alert("구매자의 잔액이 부족합니다.")
                    }
                })
            .catch((error) => {
                alert(error.data.message);
            });
        },
        refuse(reserveid, state){  //예약 거절 / 취소
            var url = 'http://localhost:8080/refuse';
            var data = {
                reserveid : reserveid
            }
            axios.post(url, JSON.stringify(data), {
            headers: {
            "Content-Type": `application/json`
            }}
            )
            .then(() => {
                if(state == "refuse"){
                    alert("예약을 거절했습니다.");
                    this.board = this.board.filter(reservation => reservation.reserveid !== reserveid);
                }else if(state == "cancle"){
                    alert("예약을 취소했습니다.");
                    this.myboard = this.myboard.filter(reservation => reservation.reserveid !== reserveid);
                }
            })
            .catch(() => {
                alert("다시 시도해주세요.")
            });
        },
        regularexpression(){  //정규식으로 변환(숫자 단위)
            this.amount = this.amount.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
        }
   }
}
  </script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
   @import url('https://fonts.googleapis.com/css2?family=Dongle&family=Nanum+Gothic&display=swap');

      .info_maintext{
        height: 100px;
        margin-top: 50px;
        font-family: 'Nanum Gothic', sans-serif;
        font-weight: bold;
        font-size: 20px;
      }
      .info_maintext span{
        display: block;
        margin-bottom: 50px;
      }
      .main{
        width: 800px;
        margin: 0 auto;
      }
      .myinfo_head{
        width: 800px;
        height: 40px;
        font-family: 'Nanum Gothic', sans-serif;
        font-weight: bold;
        font-size: 20px;
      }
      .myinfo_headbtn{
        width: 800px;
        height: 30px;
        margin-bottom: 10px;
      }
      .nontable{
        margin-top: 30px;
        font-family: 'Nanum Gothic', sans-serif;
        font-weight: bold;
        font-size: 15px;
      }
      .myinfo_headbtn .addcountbtn{
        margin-left: 5px;
        background-color: #123951c0;
        width: 80px;
        height: 30px;
        padding: 5px;
        font-size: 15px;
        color: #fdf6e3;
        border-style: double;
        border-width: 1px;
        border-radius: 3px;
        border-color: #123951b9;
        cursor: pointer;
        float: right;
      }
      .myinfo_headbtn .addcountbtnnon{
        margin-left: 5px;
        background-color: #123951c0;
        width: 80px;
        height: 30px;
        padding: 5px;
        font-size: 15px;
        color: #fdf6e3;
        border-style: double;
        border-width: 1px;
        border-radius: 3px;
        border-color: #123951b9;
        cursor: pointer;
      }
      .tablearea{
        min-height: 200px;
        margin-bottom: 30px;
      }
      .myinfo {
        width: 800px;
        border-collapse: collapse;
        overflow: hidden;
        box-shadow: 0 0 20px rgba(0,0,0,0.3);
        margin-bottom: 30px;
      }
      th,
        td {
        padding: 15px;
        background-color: rgba(255,255,255,0.2);
        color: #fff;
      }
      th {
        text-align: center;
      }
      td button {
        margin-left: 5px;
        background-color:  rgba(255,255,255,0.2);
        width: 50px;
        height: 30px;
        padding: 5px;
        font-size: 15px;
        color: #fdf6e3;
        border-style: double;
        border-width: 1px;
        border-radius: 3px;
        border-color: rgba(255,255,255,0.2);
        cursor: pointer;
      }

      thead {
        background-color: #123951;
      }
      tbody{
        background-color: #19567c;
      }
      tr :hover {
        background-color: rgba(255,255,255,0.3);
      }
      td :hover{
        background-color: rgba(255,255,255,0.3);
      }
      .myinfo-sel1{
        width: 250px;
        vertical-align:top
      }
      .myinfo-sel2{
        width: 150px;
        vertical-align:top
      }
      .myinfo-sel3{
        width: 120px;
        vertical-align:top
      }
      .myinfo-sel4{
        width: 140px;
        vertical-align:top
      }
      .myinfo-sel5{
        width: 120px;
        vertical-align:top
      }
      .myinfo-sel6{
        width: 120px;
        vertical-align:top
      }
  </style>
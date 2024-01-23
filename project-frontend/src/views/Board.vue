<template>
  <div class="boardarea">
    <div class="board_text">
        <span>거래정보</span>
    </div>
    <!-- <div class="boardinfo"> -->
   <div class="boardinfo">
      <div class="boardinfoimg" v-for="group in boardimg" :key="group">
        <img v-if="group.filepath != null" :src="getSrc(group.filepath)" class="area4">
        <span v-if="group.filepath == null" class="area5"></span>
      </div>
      <div class="boardinfotop" v-for="group in board" :key="group">
        <span class="nickname">{{ group.nickname }}</span>
        <span class="emptyarea"></span>
        <span class="status">{{ group.productstatus }}</span>
        <hr>
        <span class="boardsubject">{{ group.boardsub }}</span>
        <span class="productprice">가격 : {{ group.productprice }}원</span>
        <span class="productinfo">상품명 : {{ group.productinfo }}</span>
        <span class="productcount">수량 : {{ group.productcount }}</span>
      </div>
      <div class="boardinfomain" v-for="group in board" :key="group">
        <pre><span class="boardcontent">{{ group.boardcontent }}</span></pre>
        <div class="boardbtn">
          <button type="button" v-if="showButton(group.userid) && !(group.productstatus == '거래완료')" class="updatebtn" @click="reservationcheck()">수정하기</button>
          <button type="button" v-if="!showButton(group.userid) && !(group.productstatus == '거래완료')" class="reservebtn" @click="checkreservation(group.userid)">거래예약</button>
          <button type="button" v-if="group.productstatus == '거래완료'" class="transbtn" disabled='disabled'>거래완료</button>
          <button type="button" class="historybtn" onclick="history.back()">뒤로가기</button>
      </div>
     </div>
     <hr>
     <div class="commentarea">
      <div class="divcomment" v-for="group in comment" :key="group">
        <span class="cnickname" v-if="group.nickname">{{group.nickname}}</span>
        <span class="ccontentnon" v-if="group.commentcontent && !(group.userid == this.myuserid)">{{group.commentcontent}}</span>
        <span class="ccontent" v-if="group.commentcontent && (group.userid == this.myuserid)">{{group.commentcontent}}</span>
        <button type="button" v-if="(group.userid == this.myuserid)" class="deletecomment" @click="deletecomment(group.commentnum)">X</button>
      </div>
     </div>
     <div class="addcommentarea">
      <input type="text" id="commentcontent" v-model="commentcontent">
      <button type="button" class="addcomment" @click="addcomment()">댓글등록</button>
    </div>
   </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MainPage',
  props: {
    bnum: {
      type: String
    },
  },
  data(){
    return{
      board: [],
      productprice: '',
      reservationcount: '',
      boardimg: [],
      comment: [],
      commentcontent: '',
      myuserid: ''
    }
  }, 
  created(){
    this.boardinfo(this.$route.params.bnum); // bnum값으로 boardinfo정보 불러오기
    this.boardimginfo(this.$route.params.bnum);
    this.getcomment();
    this.myuserid = localStorage.getItem('user_id');
  },
  methods:{
    boardinfo(bnum) {   //상품 상세정보
    var vm = this;
    var url = 'http://localhost:8080/boardinfo';
    axios.get(url, { params: { bnum: bnum } })
      .then((response) => {
        vm.board = response.data;
        vm.productprice = response.data[0].productprice;

        this.regularexpression()  //정규화
      })
      .catch((error) => {
        console.log(error);
      });

      //상품 예약정보(수량)
      var url2 = 'http://localhost:8080/checkreservation';
        axios.get(url2, { params: { bnum: this.$route.params.bnum } })
          .then((response) => {
              this.reservationcount = response.data;
          })
          .catch((error) => {
            console.log(error);
        });
    },
    boardimginfo(bnum) {  //상품 이미지 불러오기
    var vm = this;
    var url = 'http://localhost:8080/boardimginfo';
    axios.get(url, { params: { bnum: bnum } })
      .then((response) => {
        vm.boardimg = response.data;
      })
      .catch((error) => {
        console.log(error);
      });
    },
    getSrc(name){ //이미지 경로설정
        return require('../../../../../../boardupload/'+ name)
    },
    showButton(userid) {
      // 로컬 스토리지에 저장된 사용자 ID 가져오기
      const user_id = localStorage.getItem('user_id');
      // user_id와 board의 userid 비교 후 결과 반환
      return user_id === userid;
    },

    reservationcheck(){
      var url = 'http://localhost:8080/reservationcheck';
      axios.get(url, { params: { bnum: this.$route.params.bnum } })
      .then((response) => {
        if(response.data == true){
          alert("상품이 예약되어있어 수정이 불가합니다.")
        }else{
          this.updateproduct();
        }
        
      })
      .catch((error) => {
        console.log(error);
      });
    },
    updateproduct() { //게시글 수정페이지로 이동
      var url = 'http://localhost:8080/boardupdate';
      axios.get(url)
        .then(() => {
          this.$router.push({ name: "boardupdate", params:{bnum: this.$route.params.bnum} });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    checkreservation(hostuserid){   //예약 유뮤 체크
       
       if((this.board[0].productcount - this.reservationcount) == 0){
        alert("예약 가능한 수량이 없습니다.")
       }else{
          var purchaseQuantity = prompt("예약하시려는 수량을 입력해주세요: \n\n현재 예약된 수량 = " + this.reservationcount + "\n현재 예약 가능수량 = " + (this.board[0].productcount - this.reservationcount));

          // 입력된 값이 null 또는 빈 문자열인 경우 처리
          if (purchaseQuantity === null) {
              return;
          }
          else if (purchaseQuantity === "") {
              alert("예약하시려는 수량을 입력해주세요.");
          }
          else if(purchaseQuantity > (this.board[0].productcount - this.reservationcount)){
            alert("예약하시려는 상품의 수량을 " + (this.board[0].productcount - this.reservationcount) + "이하로 입력해주세요.");
          }
          else if(purchaseQuantity <= 0){
            alert("예약하시려는 상품의 수량을 올바르게 입력해주세요.");
          }
          else if(isNaN(purchaseQuantity)) {  // 입력된 값이 숫자가 아닌 경우 처리
              alert("유효한 수량을 입력해주세요.");
          }else{
            // 입력된 값을 숫자로 변환하여 확인
            purchaseQuantity = parseInt(purchaseQuantity);

            this.reservation(purchaseQuantity, hostuserid);

          }
        }
      },
    reservation(purchaseQuantity, hostuserid){  //거래 예약
        var url = 'http://localhost:8080/reservation';
        var data = {
          boardnum : this.$route.params.bnum,
          userid : localStorage.getItem("user_id"),
          hostuserid : hostuserid,
          quantity : purchaseQuantity
        }
        axios.post(url, JSON.stringify(data), {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => {
          alert("거래 예약되었습니다.")
          this.boardinfo(this.$route.params.bnum );
          // this.$router.push({ path: "/transaction" });
        })
        .catch(() => {
          alert("거래 예약이 되지 않았습니다.")
        });
    },
    getcomment(){ //댓글 조회
      var url = 'http://localhost:8080/getcomment';
        axios.get(url, { params: { bnum: this.$route.params.bnum } })
          .then((response) => {
              this.comment = response.data;
          })
          .catch((error) => {
            console.log(error);
        });
    },
    addcomment(){ //댓글 등록

      if(this.commentcontent == ''){
        alert("댓글을 입력해주세요.")
      }else{
        var url = 'http://localhost:8080/addcomment';
        var data = {
          commentcontent: this.commentcontent,
          boardnum : this.$route.params.bnum,
          userid : localStorage.getItem("user_id"),
        }
        axios.post(url, JSON.stringify(data), {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => {
          alert("댓글이 등록되었습니다.")
          this.commentcontent = '';
          this.getcomment();
        })
        .catch(() => {
          alert("댓글이 등록되지 않았습니다.")
        });
      }
    },
    deletecomment(commentnum){

      var url = 'http://localhost:8080/deletecomment';
        var data = {
          commentnum : commentnum,
        }
        axios.post(url, JSON.stringify(data), {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => {
          alert("댓글이 삭제되었습니다.")
          this.comment = this.comment.filter(comment => comment.commentnum !== commentnum);
        })
        .catch(() => {
          alert("댓글이 삭제되지 않았습니다.")
        });
    },
    regularexpression(){  //정규식으로 변환(숫자 단위)
       this.board[0].productprice = this.board[0].productprice.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
    },
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
 @import url('https://fonts.googleapis.com/css2?family=Dongle&family=Nanum+Gothic&display=swap');
    .board_text{
      height: 100px;
      display: block;
      margin: 0 auto;
      margin-top: 50px;
      text-align: center;
    }
    .board_text span{
      font-size: 50px;
      font-family: 'Dongle', sans-serif;
      font-weight: bold;
      color: #002b36;
    }
    .boardinfo{
      margin: 0 auto;
      width: 800px;
      min-height: 700px;
      max-height: 3000px;
      font-family: 'Nanum Gothic', sans-serif;
      font-weight: bold;
      display: block;
    }
    .boardinfotop{
      margin: 0 auto;
      width: 800px;
      display: block;
    }
    .nickname{
      display: inline-block;
      float: left;
      width: 100px;
      height: 50px;
      line-height: 50px;
    }
    .emptyarea{
      display: inline-block;
      width: 600px;
      height: 50px;
      line-height: 50px;
    }
    .status{
      display: inline-block;
      float: right;
      width: 100px;
      height: 50px;
      line-height: 50px;
    }
    .boardsubject{
      display: block;
      /* float: left; */
      width: 800px;
      height: 50px;
      line-height: 50px;
      font-size: 20px;
      text-align: left;
    }
    .productprice{
      display: block;
      /* float: left; */
      width: 200px;
      height: 50px;
      line-height: 50px;
      font-size: 15px;
      text-align: left;
    }
    .productinfo{
      display: block;
      /* float: left; */
      width: 200px;
      height: 50px;
      line-height: 50px;
      font-size: 15px;
      text-align: left;
    }
    .productcount{
      display: block;
      /* float: left; */
      width: 200px;
      height: 50px;
      line-height: 50px;
      font-size: 15px;
      text-align: left;
    }
    .area4{
      display: block;
      margin: 0 auto;
      margin-bottom: 10px;
      padding: 15px;
      width: 800px;
      height: 600px;
    }
    .area5{
      display: block;
      margin: 0 auto;
      width: 650px;
      height: 100px;
    }
    pre{
      display: block;
      width: 800px;
      min-height: 150px;
      max-height: 1500px;
      margin-top: 10px;
      text-align: left;
      font-family: 'Nanum Gothic', sans-serif;
      font-weight: normal;
    }
    .board_emptyarea{
      height: 5px;
      background-color: #002b36;
    }
    .boardbtn button{
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
      margin-bottom: 92px;
    }
    .divcomment{
      margin-bottom: 10px;
      
    }
    .cnickname{
      width: 80px;
      margin-right: 20px;
      display: inline-block;
      background-color: #0b3d4d;
      color: white;
      border: 1px solid #002b36; /* 아래쪽 테두리 1px로 지정 */
      border-radius: 7px;
    }
    .ccontentnon{
      width: 665px;
      display: inline-block;
      text-align: left;
      white-space: nowrap;      /* 줄 바꿈 방지 */
      overflow: hidden;         /* 넘치는 부분 숨김 */
      text-overflow: ellipsis;  /* 생략 부호 (...) 표시 */
      vertical-align: bottom; /* 기준선을 기본 위치로 조정 */
    }
    .ccontent{
      width: 630px;
      display: inline-block;
      text-align: left;
      white-space: nowrap;      /* 줄 바꿈 방지 */
      overflow: hidden;         /* 넘치는 부분 숨김 */
      text-overflow: ellipsis;  /* 생략 부호 (...) 표시 */
      vertical-align: bottom; /* 기준선을 기본 위치로 조정 */
    }
    .addcommentarea{
      margin-top: 40px;
    }
    .addcommentarea input{
      width: 550px;
      display: inline-block;
    }
    .addcommentarea button{
      display: inline-block;
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
    }
    .deletecomment{
      display: inline-block;
      margin-left: 5px;
      background-color: #123951;
      width: 30px;
      height: 30px;
      font-size: 15px;
      color: #fdf6e3;
      border-style: double;
      border-width: 1px;
      border-radius: 3px;
      border-color: #123951;
      cursor: pointer;
    }
</style>

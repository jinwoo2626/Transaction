<template>
  <div class="transactionarea">
    <div class="transaction_text">
        <span>중고거래</span>
        <button type="button" class="btn btn-secondary" v-on:click="addboard">상품등록</button>
    </div>
      <div class="board_area" v-for="group in board" :key="group">
        <div class="board_area_top"></div>
        <span class="area1">{{ group.nickname }}</span>
        <span class="area2">{{ group.boardsub }}</span>
        <span class="area3">{{ group.productstatus }}</span>
        <span class="emptyarea"></span>
        <span class="area4">{{ group.productprice }}원</span>
        <img v-if="group.filepath != null" :src="getSrc(group.filepath)" class="area5" v-on:click="detail(group.boardnum)">
        <span v-if="group.filepath == null" class="area6" v-on:click="detail(group.boardnum)"></span>
      </div>
      <div class="paging" id="pagingarea"> <!-- 미검색 -->
            <button class="beforebtn" @click="beforebtn()">이전</button>
            <span class="pagelist"  v-for="j in pagelist.slice((curpageset*10), (curpageset + 1) *10)" :key="j" @click="movepage($event)" :id=j>{{ j }}</span>
            <button class="nextbtn" @click="nextbtn()">다음</button>
      </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
    name: 'MainPage',
    data() {
        return {
            board: [],
            bnum: '',
            totalCount: '', //총 게시글 수
            totalPage: 0,  //총 페이지 수
            curpage: 1,    //현재 페이지 정보
            bepage: 1,      //이전 페이지 정보
            curpageset: 0, //현재 페이지 set(1~10페이지면 0 / 11~20페이지면 1..)
            numOfRows: 5, //한 페이지에 보여주는 게시글 수
            pagelist: [],
        };
    },
    created() {
        this.gettotalCount();
    },
    methods: {
        gettotalCount(){    //총 게시물 개수
          var vm = this;
          var url = 'http://localhost:8080/boardcnt';
            axios.get(url)
            .then(response => {
              vm.totalCount = response.data;

              this.boardlist(1);
              vm.totalPage = Math.ceil(vm.totalCount/vm.numOfRows); //총 페이지수 구하기
              for (let i = 1; i <= vm.totalPage; i++) {   //초기 페이지리스트 생성
                vm.pagelist.push(i)
              }
            })
            .catch(error => {
            console.log(error);
            });
        },
        boardlist(curpage) {  //게시판 리스트 불러오기
            var vm = this;
            var url = 'http://localhost:8080/board';
            var data = {
                numOfRows : "5",
                pageNo : curpage,
            }
            axios.get(url,{
                params: {
                    numOfRows : data.numOfRows,
                    pageNo : data.pageNo,
                }
            })
            .then(response => {
              vm.board = response.data;
              this.regularexpression()  //정규화
              vm.curpagebold()
            })
            .catch(error => {
            console.log(error);
            });
        },
        getSrc(name){ //이미지 경로설정
          return require('../../../../../../boardupload/'+ name)
        },
        addboard(){   //상품추가 페이지로 이동
          var url = 'http://localhost:8080/addboard';

           axios.get(url)
            .then(() => {
              this.$router.push({ path: "/addboard" });
            })
            .catch(() => {
            });
        },
        detail(bnum) {  //상세페이지로 이동
          var vm = this;
          vm.bnum = bnum;
          var url2 = 'http://localhost:8080/boarddetail';

           axios.get(url2)
            .then(() => {
                this.$router.push({ name: "board", params:{bnum: vm.bnum} });
            })
            .catch(() => {
              
            });
        },
        regularexpression(){  //정규식으로 변환(숫자 단위)
          for(var i = 0; i < this.board.length; i++){
            this.board[i].productprice = this.board[i].productprice.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
          }
        },
        movepage(event){ //페이지 클릭시 화면이동
          this.bepage = this.curpage  //이전페이지 정보 저장
          this.curpage = event.currentTarget.id;
          this.curpageset = Math.ceil((this.curpage) / 10) -1  //curpageset 구하기
          window.scrollTo(0, 0);
          this.boardlist(this.curpage)
        },
        beforebtn(){ //페이징 이전버튼
          this.bepage = this.curpage  //이전페이지 정보 저장
          if(this.curpage == 1){
            this.curpage == 1   //1페이지인경우 이전버튼 클릭시 1페이지로 고정
          }else{
            this.curpage--;
            window.scrollTo(0, 0);
          }
          this.curpageset = Math.ceil((this.curpage) / 10) -1  //curpageset 구하기
          this.boardlist(this.curpage)

        },
        nextbtn(){ //페이징 다음버튼
          this.bepage = this.curpage  //이전페이지 정보 저장
          if(this.curpage >= this.totalPage){
            this.curpage == this.totalPage  //현재페이지가 마지막페이지인경우 다음버튼 클릭시 현재페이지로 고정
          }else{
            this.curpage++;
            window.scrollTo(0, 0);
          }
          this.curpageset = Math.ceil((this.curpage) / 10) -1  //curpageset 구하기
          this.boardlist(this.curpage)
          
        },
        curpagebold(){
          if(this.curpage == 1 & this.bepage == 1){
            document.getElementById(1).style.fontWeight = "bold" //현재 페이지 bold처리 후 이전페이지 normal 처리
          }else{
            document.getElementById(this.curpage).style.fontWeight = "bold" //현재 페이지 bold처리 후 이전페이지 normal 처리
            document.getElementById(this.bepage).style.fontWeight = "normal"
          }
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
 @import url('https://fonts.googleapis.com/css2?family=Dongle&family=Nanum+Gothic&display=swap');
    .transaction_text{
      width: 700px;
      height: 140px;
      display: block;
      margin: 0 auto;
      margin-top: 50px;
      text-align: center;
    }
    .transaction_text button{
      height: 35px;
      background-color: #123951;
      border-style: double;
      border-width: 1px;
      border-radius: 3px;
      border-color: #123951;
      float: right;
    }
    .transaction_text span{
      display: block;
      font-size: 50px;
      font-family: 'Dongle', sans-serif;
      font-weight: bold;
      color: #002b36;
    }
    .board_area{
      margin: 0 auto;
      margin-bottom: 20px;
      width: 700px;
      font-family: 'Nanum Gothic', sans-serif;
      font-weight: bold;
      /* height: 500px; */
			border-style:solid;
      border-width: 3px;
      border-color: #001216;
      border-radius: 7px;
    }
    .board_area .board_area_top{
      height: 5px;
      background-color: #002b36;
    }
    .board_area .area1{
      display: inline-block;
      width: 50px;
      height: 50px;
      line-height: 50px;
    }
    .board_area .area2{
      display: inline-block;
      width: 500px;
      height: 50px;
      line-height: 50px;
    }
    .board_area .area3{
      display: inline-block;
      width: 100px;
      height: 50px;
      line-height: 50px;
    }
    .board_area .emptyarea{
      display: inline-block;
      width: 550px;
      line-height: 50px;
    }
    .board_area .area4{
      display: inline-block;
      width: 100px;
      height: 50px;
      line-height: 50px;
    }
    .board_area .area5{
      display: block;
      margin: 0 auto;
      margin-top: 30px;
      margin-bottom: 50px;
      width: 650px;
      height: 410px;
      background-color: #002b36;
    }
    .board_area .area6{
      display: block;
      margin: 0 auto;
      width: 650px;
      height: 100px;
    }
    .paging{
      padding: 10px;
    }
    .pagelist{
      padding: 10px 20px;
      cursor: pointer;
    }
    .beforebtn{
      margin-left: 10px;
      background-color: #123951;
      width: 70px;
      height: 30px;
      margin: 0 10px;
      padding: 5px;
      font-size: 15px;
      color: #fdf6e3;
      border-style: double;
      border-width: 1px;
      border-radius: 3px;
      border-color: #123951;
      cursor: pointer;
    }
    .nextbtn{
      margin-left: 10px;
      background-color: #123951;
      width: 70px;
      height: 30px;
      margin: 0 10px;
      padding: 5px;
      font-size: 15px;
      color: #fdf6e3;
      border-style: double;
      border-width: 1px;
      border-radius: 3px;
      border-color: #123951;
      cursor: pointer;
    }
</style>

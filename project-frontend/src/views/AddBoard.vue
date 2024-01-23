<template>
  <div class="addboardarea">
    <div class="add_text">
        <span>상품등록</span>
    </div>
    <div class="boardmain">
      <div class="addboardtext">
          <span class="subject">게시글 제목</span>
          <span class="content">게시글 내용</span>
          <span class="name">판매상품명</span>
          <span class="price">판매가격</span>
          <span class="quantity">판매수량</span>
        </div>
      <div class="addboardinput">
        <input type="text" placeholder="게시글 제목" id="boardsub" maxlength='30' v-model="boardsub"><br>
        <textarea placeholder="게시글 내용" v-model="boardcontent"></textarea><br>
        <input type="text" placeholder="판매상품명" id="productinfo" v-model="productinfo"><br>
        <input type="number" placeholder="판매가격" id="productprice" v-model="productprice"><br>
        <input type="number" placeholder="판매수량" id="productcount" v-model="productcount"><br>
        <input multiple @change="imageUpload()" ref="images" type="file" id="productimg"/>
    </div>
  </div>
   <div class="addboardbtn">
      <button type="button" class="addbtn" @click="addproduct()">상품등록</button>
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
        boardsub:'',
        boardcontent: '',
        productinfo: '',
        productcount: '',
        productprice:'',
        input: {
          image: []
        }
    }
  },
  methods:{
    addproduct(){   //상품추가

      if (this.boardsub === '') {
        alert('게시글 제목을 입력해주세요')
      }else if(this.boardcontent === ''){
        alert('게시글 내용을 입력해주세요')
      }else if(this.productinfo === ''){
        alert('상품명을 입력해주세요')
      }else if(this.productprice === ''){
        alert('상품가격을 입력해주세요')
      }else if(this.productprice < 0){
        alert('상품가격은 0원 이상이여야 합니다')
      }else if(this.productcount === ''){
        alert('상품수량을 입력해주세요')
      }else if(this.productcount <= 0){
        alert('판매하려는 상품은 1개 이상이어야 합니다.')
      }else if(this.input.image == ''){
        alert('사진을 선택해주세요')
      }
      else{
        var vm = this;
        var url = 'http://localhost:8080/addproduct';
        var url2 = 'http://localhost:8080/addproductimg';
        var data = {
          boardsub : this.boardsub,
          boardcontent : this.boardcontent,
          productinfo : this.productinfo,
          productcount : this.productcount,
          productprice: this.productprice,
          userid : localStorage.getItem("user_id")
        }
        axios.post(url, JSON.stringify(data), {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => { //게시판 등록에 성공한 후 이미지 업로드
              var formData = new FormData();
              for (let i = 0; i < vm.input.image.length; i++) {
                formData.append('productimage', this.input.image[i]);
              }
              formData.append('userid', localStorage.getItem("user_id"))

              axios.post(url2, formData,{headers:{
                'Content-Type': 'multipart/form-data'
              }})
              .then(() => {
                alert("게시글 등록이 완료되었습니다.")
                this.$router.push({ path: "/main" });
              })
              .catch(error => {
                  console.log(error);
              })
        })
        .catch(error => {
            console.log(error);
        })
      } 
  },
  imageUpload() {
      this.input.image = []
      // refs 속성을 이용해 input 태그에 접근함
      var file = this.$refs.images.files;

      for (let i = 0; i < file.length; i++) {
        // 선택한 각 파일에 접근하여 배열에 추가
        this.input.image.push(file[i]);
      }
    }
}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .addboardarea{
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
    .boardmain{
      width: 800px;
      height: 580px;
    }
    .addboardtext{
      width: 150px;
      padding-top: 10px;
      display: inline-block;
    }
    .addboardinput{
      margin: 0 auto;
      width: 650px;
      float: right;
      display: inline-block;
    }
    .addboardtext span{
      width: 150px;
      display: block;
      margin-bottom: 20px;
      font-family: 'Nanum Gothic', sans-serif;
      font-weight: bold;
    }
    .addboardtext .content{
      margin-bottom: 288px;
    }
    .addboardinput input{
      width: 620px;
      height: 40px;
      margin-bottom: 5px;
    }
    .addboardinput textarea{
      width: 620px;
      height: 300px;
      resize: none;
    }
    input, textarea {
      font-size: 13px;
      margin-bottom: 5px;
      border-radius: 5px;
      border-width: 3px;
      border-color: #001216;
    }
    input[type=file]::file-selector-button {
      width: 150px;
      height: 30px;
      margin-top: 10px;
      background: #fff;
      border: 2px solid #002b36;
      border-radius: 10px;
      cursor: pointer;
    }
    .addboardbtn button{
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
</style>

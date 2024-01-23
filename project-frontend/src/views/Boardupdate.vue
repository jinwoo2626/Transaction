<template>
  <div class="boardupdatearea">
    <div class="update_text">
        <span>상품수정</span>
    </div>
    <div class="boardmain">
      <div class="boardupdatetext">
        <span class="subject">게시글 제목</span>
        <span class="content">게시글 내용</span>
        <span class="name">판매상품명</span>
        <span class="price">판매가격</span>
        <span class="quantity">판매수량</span>
      </div>
      <div class="boardupdateinput">
        <input type="text" placeholder="게시글 제목" id="boardsub" maxlength='30' v-model="boardsub"><br>
        <textarea placeholder="게시글 내용" v-model="boardcontent"></textarea><br>
        <input type="text" placeholder="판매상품명" id="productinfo" v-model="productinfo"><br>
        <input type="number" placeholder="판매가격" id="productprice" v-model="productprice"><br>
        <input type="number" placeholder="판매수량" id="productcount" v-model="productcount"><br>
        <div class="filearea">
          <div class="fileareain"  v-for="group in filename" :key="group">
            <span class="file" v-if="filename" :id="group.filenum">{{ group.filename }}</span>
            <button type="button" class="delete" @click="deletefile(group.filenum, $event)" :id="group.filenum">삭제</button>
          </div>
        </div>
        <input multiple @change="imageUpload()" ref="images" type="file" id="productimg"/>
    </div>
  </div>
   <div class="boardupdatebtn">
      <button type="button" class="updatebtn" @click="updateproduct()">제품수정</button>
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
      board: [],
      boardsub:'',
      boardcontent: '',
      productinfo: '',
      productprice: '',
      productcount: '',
      filename: [],
      input: {
        image: []
      }
    }
  },
  created(){
    this.boardupdateinfo(this.$route.params.bnum);
    this.boardupdateimginfo(this.$route.params.bnum);
  },
  methods:{
    boardupdateinfo(bnum) {   //수정할 게시글 정보 불러오기
    var vm = this;
    var url = 'http://localhost:8080/boardinfo';
    axios.get(url, { params: { bnum: bnum } })
      .then((response) => {
        vm.boardsub = response.data[0].boardsub;
        vm.boardcontent = response.data[0].boardcontent;
        vm.productinfo = response.data[0].productinfo;
        vm.productprice = response.data[0].productprice;
        vm.productcount = response.data[0].productcount;
      })
      .catch((error) => {
        console.log(error);
      });
    },
    boardupdateimginfo(bnum) {    //수정할 이미지 정보 불러오기
    var vm = this;
    var url = 'http://localhost:8080/boardimginfo';
    axios.get(url, { params: { bnum: bnum } })
      .then((response) => {
        vm.filename = response.data;
      })
      .catch((error) => {
        console.log(error);
      });
    },
    deletefile(btnid, event){   //이미지 삭제
       let like = confirm("이미지를 삭제하시겠습니까?");

      if(like){
        var url = 'http://localhost:8080/deleteimg';
        var data = {
              filenum : btnid,
          }
          axios.post(url, JSON.stringify(data), {
          headers: {
            "Content-Type": `application/json`
          }}
          )
          .then(() => {
                this.filename = this.filename.filter(item => item.filenum !== btnid);
                event.target.previousElementSibling.remove(); // span 태그 삭제
                event.target.remove(); // button 삭제
            })
            .catch(error => {
                console.log(error);
            })
      }
    }, 
    updateproduct(){ // 상품정보 수정

      if (this.boardsub === '') {
        alert('게시글 제목을 입력해주세요')
      }else if(this.boardcontent === ''){
        alert('게시글 내용을 입력해주세요')
      }else if(this.productinfo === ''){
        alert('상품명을 입력해주세요')
      }else if(this.productcount === ''){
        alert('상품수량을 입력해주세요')
      }else if(this.filename.length === 0 && this.input.image == ''){
        alert('사진을 선택해주세요')
      }else{
        var vm = this;
        var url = 'http://localhost:8080/updateproduct';
        var url2 = 'http://localhost:8080/updateproductimg';
        var data = {
          boardsub : this.boardsub,
          boardcontent : this.boardcontent,
          productinfo : this.productinfo,
          productprice : this.productprice,
          productcount : this.productcount,
          boardnum : this.$route.params.bnum,
          userid : localStorage.getItem("user_id")
        }
        axios.post(url, JSON.stringify(data), {
        headers: {
          "Content-Type": `application/json`
        }}
        )
        .then(() => {  
                if(this.input.image == ''){   //이미지를 등록하지 않았을 때 메인으로 이동
                  alert("게시글 수정이 완료되었습니다.")
                    this.$router.push({ path: "/main" });
                }else{  //게시판 등록에 성공한 후 이미지 업로드
                  var formData = new FormData();
                  for (let i = 0; i < vm.input.image.length; i++) {
                    formData.append('productimage', this.input.image[i]);
                  }
                  formData.append('userid', localStorage.getItem("user_id"))
                  formData.append('boardnum', this.$route.params.bnum,)

                  axios.post(url2, formData,{headers:{
                    'Content-Type': 'multipart/form-data'
                  }})
                  .then(() => {
                    alert("게시글 수정이 완료되었습니다.")
                    this.$router.push({ path: "/main" });
                  })
                  .catch(error => {
                      console.log(error);
                  })
                }
          }
        )
        .catch(error => {
            console.log(error);
        })
      }
        
  },
  imageUpload() {
      var file = this.$refs.images.files;

      for (let i = 0; i < file.length; i++) {
        this.input.image.push(file[i]);
      }
    }
}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Dongle&family=Nanum+Gothic&display=swap');

    .boardupdatearea{
      margin: 0 auto;
      width: 800px;
    }
    .update_text{
      height: 150px;
      display: block;
      margin: 0 auto;
      margin-top: 50px;
      text-align: center;
    }
    .update_text span{
      font-size: 30px;
      font-weight: bold;
      color: #002b36;
    }
    .boardmain{
      width: 800px;
      height: 600px;
    }
    .boardupdatetext{
      width: 150px;
      padding-top: 10px;
      display: inline-block;
    }
    .boardupdateinput{
      margin: 0 auto;
      width: 650px;
      float: right;
      display: inline-block;
    }
    .boardupdatetext span{
      width: 150px;
      display: block;
      margin-bottom: 20px;
      font-family: 'Nanum Gothic', sans-serif;
      font-weight: bold;
    }

    .boardupdatetext .content{
      margin-bottom: 288px;
    }

    .boardupdateinput input{
      width: 620px;
      height: 40px;
      margin-bottom: 5px;
    }
    .boardupdateinput textarea{
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
    .filearea{
      width: 400px;
      margin: 0 auto;
      margin-bottom: 15px;
      float: right;
      height: auto;
    }
    .filearea span{
      width: 300px;
      display: inline-block;
      text-align: right;
    }
    .filearea button{
      background-color: #123951;
      width: 70px;
      height: 25px;
      line-height: 20px;
      margin: 0 10px;
      padding: 5px;
      font-size: 15px;
      color: white;
      border-style: double;
      border-width: 1px;
      border-radius: 3px;
      border-color: #123951;
      cursor: pointer;
    }
    input[type=file]::file-selector-button {
      width: 150px;
      height: 30px;
      background: #fff;
      border: 2px solid #002b36;
      border-radius: 10px;
      cursor: pointer;
    }
    .boardupdatebtn button{
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

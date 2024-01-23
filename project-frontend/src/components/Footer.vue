<template>
  <div class="footer">
    <h2>D-Bridge</h2>
    <h5>DBridge v2.8.0-SNAPSHOT( 20230823063750 )
Copyrightⓒ 2015. All rights reserved by CyberImagination, Inc.</h5>
  </div>
</template>

<script>
export default {
  name: 'FooterArea',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.footer{
  margin-top: 100px;
  background-color: #002b36;
  height: 120px;
}
h2{
  margin: 0;
  padding-top: 20px;
}
</style>

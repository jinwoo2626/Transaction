<template>
  <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
  <Header msg="중고거래"/>
  <router-view></router-view>
  <Footer/>
  <!-- <MainPage/> -->
</template>

<script>
  import Header from './components/Header.vue'
  import Footer from './components/Footer.vue'

export default {
  name: 'App',
  components: {
    Header,
    Footer
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>

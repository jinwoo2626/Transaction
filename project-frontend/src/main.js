import { createApp } from 'vue'
import App from './App.vue'
import axios from './utils/axios' 
import router from './router'
import store from './vuex/store'
import loginAPI from './service/loginAPI'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'

const app = createApp(App)
app.config.globalProperties.$axios = axios  //전역변수로 설정 컴포넌트에서 this.$axios.js 호출할 수 있음
app.config.globalProperties.$serverUrl = '//localhost:8081' //api server
app.config.globalProperties.$store = store

app
  .use(router)
  .use(store)
  .use(loginAPI)
  .mount('#app')

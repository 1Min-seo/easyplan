import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import axios from 'axios';
import VueCookies from 'vue-cookies';
import '@fortawesome/fontawesome-free/css/all.css';

axios.defaults.withCredentials = true;
Vue.use(VueCookies);

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');

axios.interceptors.request.use(
    config => {
        const token = Vue.$cookies.get("access_token");
        console.log("token: " + token);
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        } else {

            if (config.url.includes('/hearts')) {
                alert("로그인이 필요합니다");
                location.href = "/";
            }
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);
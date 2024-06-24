import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';
import Cookies from 'js-cookie';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        isAuthenticated: !!Cookies.get('token'),
    },
    mutations: {
        setAuthentication(state, status) {
            state.isAuthenticated = status;
        },
    },
    actions: {
        async checkAuth({commit}) {
            try {
                const response = await axios.get('http://localhost:8090/login', {withCredentials: true});
                commit('setAuthentication', response.data.isAuthenticated);
            } catch (error) {
                console.error("인증 상태 확인 실패:", error);
                commit('setAuthentication', false);
            }
        },
    }
});

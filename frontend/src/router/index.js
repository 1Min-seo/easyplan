
import Vue from 'vue';
import Router from 'vue-router';


import Index from "@/components/Index.vue";
import Mypage from "@/components/Mypage.vue";
import CreateReview from "@/components/CreateReview.vue";
import ReviewDetails from "@/components/ReviewDetails.vue";
import UpdateReview from "@/components/UpdateReview.vue";
import Schedule from "@/components/Schedule.vue";
import CreateReserve from "@/components/CreateReserve.vue";
import ChatRoom from "@/components/ChatRoom.vue";
import MyAllReview from "@/components/MyAllReview.vue";


Vue.use(Router);

const router = new Router({
    mode: 'history',
    routes: [
        {
            path:'/',
            name: "Index",
            component: Index,
        },
        {
            path: '/mypage',
            name: 'Mypage',
            component: Mypage,
            meta: { requiresAuth: true }
        },
        {
            path: '/reviews/form',
            name: 'CreateReview',
            component: CreateReview,
            meta: { requiresAuth: true }
        },
        {
            path: '/reviews/:id/details',
            name: 'ReviewDetails',
            component: ReviewDetails,
            props: true

        },
        {
            path: '/reviews/:id/edit',
            name: 'UpdateReview',
            component: UpdateReview,
            props: true
        },
        {
            path: '/schedule',
            name: 'Schedule',
            component: Schedule,
            meta: { requiresAuth: true }
        },
        {
            path: '/reserve/form',
            name: 'CreateReserve',
            component: CreateReserve,
            meta: { requiresAuth: true }
        },
        {
            path: '/chat/:roomId',
            name: 'ChatRoom',
            component: ChatRoom,
            props: route => ({ roomId: route.params.roomId, roomName: route.query.roomName })
        },
        {
            path: '/reviews/all',
            name: 'AllReviews',
            component: MyAllReview
        }
    ],
});

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        const token = Vue.$cookies.get("access_token");
        console.log(token);
        if (!token) {
            alert('로그인이 필요합니다.');
            window.location.href = 'http://localhost:8090/login';
        } else {
            next();
        }
    } else {
        next();
    }
});

/*router.beforeEach(async (to, from, next) => {
    /!*if (to.matched.some(record => record.meta.requiresAuth)) {
        const isAuthenticated = store.state.isAuthenticated;
        console.log("ds")
        if (!isAuthenticated) {
            alert('로그인이 필요합니다.');
            window.location.href = 'http://localhost:8081/login';
        } else {
            next();
        }
    } else {
        next();
    }*!/
    if(to.matched.some(record => record.meta.requiresAuth)) {
        Vue.use(VueCookies);
        const token = Vue.$cookies.get("access_token")
        console.log(token)
        if (!token) {
            alert('로그인이 필요합니다.');
            window.location.href = 'http://localhost:8081/login';
        } else {
            next();
        }
    }
});*/

export default router;
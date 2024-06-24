<template>
  <div>
    <nav :class="['navbar', 'navbar-expand-lg', 'navbar-light', 'bg-light', { 'hidden': !isAtTop }]" ref="navbar">
      <div class="container">
        <router-link to="/" class="navbar-brand">
          <span style="font-weight: bold; margin-right: 5px;">Easy</span><span style="font-weight: bold;">Plan</span>
        </router-link>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
          <ul class="navbar-nav ml-auto">
            <li class="nav-item" v-if="token == null">
              <a href="http://localhost:8090/login" class="nav-link" style="font-weight: normal;">Login</a>
            </li>
            <li class="nav-item" id="logout" v-if="token != null">
              <a v-on:click="logout" class="nav-link" style="cursor:pointer; font-weight: normal;">Logout</a>
            </li>
            <li class="nav-item">
              <router-link to="/reviews/form" class="nav-link" style="font-weight: normal;">Review</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/Mypage" class="nav-link" style="font-weight: normal;">Mypage</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/Mypage" class="nav-link" @click.prevent="scrollToChatSection" style="font-weight: normal;">Chat</router-link>
<!--              <a href="#" class="nav-link" @click.prevent="scrollToChatSection" style="font-weight: normal;">Chat</a>-->
            </li>
            <li class="nav-item">
              <router-link to="/Schedule" class="nav-link" style="font-weight: normal;">Schedule</router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>
import Vue from 'vue';
import $ from 'jquery';

export default {
  data() {
    return {
      token: Vue.$cookies.get("access_token"),
      isAtTop: true,
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    logout() {
      this.$cookies.remove("access_token");
      this.$cookies.remove("refresh_token");
      location.href = "/";
    },
    scrollToChatSection() {
      this.$router.push("/Mypage").then(() => {
        Vue.nextTick(() => {
          const chatSection = $("#chat-section");
          if (chatSection.length) {
            $('html, body').animate({
              scrollTop: chatSection.offset().top
            }, 1000);
          }
        });
      });
    },
    handleScroll() {
      this.isAtTop = window.scrollY === 0;
    }
  }
}
</script>

<style>
@import '@/assets/css/index.css';

.navbar {
  z-index: 1030;
  transition: top 0.3s ease-in-out;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  font-family: 'Arial', sans-serif;
}

.navbar.hidden {
  top: -100px;
}

.navbar-brand {
  display: flex;
  align-items: center;
}

.navbar-brand span {
  display: block;
}

.navbar-brand span:first-child {
  font-weight: bold;
}

.navbar-nav .nav-link {
  font-weight: normal;
}

.navbar-nav .nav-item {
  margin-top: 5px;
}

.navbar-toggler {
  font-size: 1.25rem;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 0.375rem;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%280, 0, 0, 0.55%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}
</style>

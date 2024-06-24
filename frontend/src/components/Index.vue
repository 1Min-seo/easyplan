<template>
  <div>
    <navigation-bar></navigation-bar>
    <br><br>
    <header class="masthead fade-in">
      <video autoplay loop muted style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; z-index: -1;">
        <source :src="require('@/assets/img/index_video.mp4')" type="video/mp4">
      </video>
      <div class="container h-100" style="position: relative; z-index: 1;">
        <div class="row h-100 align-items-center justify-content-center text-center">
          <div class="col-lg-10 align-self-end fade-in">
            <h1 class="text-uppercase text-white font-weight-bold">Are you dreaming of a perfect journey?</h1>
            <hr class="divider my-4" />
          </div>
          <div class="col-lg-8 align-self-baseline fade-in">
            <p class="text-white-75 font-weight-light mb-5" style="color: white">Travel is a time for creating new experiences and memories. We are here to plan and embark on your journey together.
              Journey with us from start to finish will be even more special and meaningful</p>
            <a class="btn btn-primary btn-xl js-scroll-trigger" href="#about">Find Out More</a>
          </div>
        </div>
      </div>
    </header>
    <section class="features-icons bg-light text-center fade-in">
      <div class="loader fade-in" style="margin-top: -100px;">
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
      </div>
      <p style="font-family: 'Arial', sans-serif; font-weight: 300; color: #555; text-align: center; margin-top: -5px;">
        여행은 새로운 경험과 추억을 만들어내는 시간입니다.<br>
        우리는 여러분과 함께 여행을 계획하고 떠나는 여정에 참여할 준비가 되어 있습니다.<br>
        시작부터 끝까지 함께하는 여행이 되어드릴게요
      </p>
      <br>
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-lg-3 hover-container fade-in">
            <div class="image-wrapper">
              <img src="@/assets/img/index01.jpg" class="img-fluid">
              <div class="hover-text">
                <p>TRAVEL</p>
                <p>"Travel is the only thing you buy that makes you richer."</p>
              </div>
            </div>
          </div>
          <div class="col-lg-3 hover-container fade-in">
            <div class="image-wrapper">
              <img src="@/assets/img/index03.jpg" class="img-fluid">
              <div class="hover-text">
                <p>RELAX</p>
                <p>"Sometimes the most productive thing you can do is relax."</p>
              </div>
            </div>
          </div>
          <div class="col-lg-3 hover-container fade-in">
            <div class="image-wrapper">
              <img src="@/assets/img/index04.jpg" class="img-fluid">
              <div class="hover-text">
                <p>EAT</p>
                <p>"One cannot think well, love well, sleep well, if one has not dined well."</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="about fade-in" id="ranking" style="margin-top: -100px">
      <div class="loader fade-in" style="margin-top: -30px">
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
        <div class="circle" tabindex="0"></div>
      </div>
      <div class="inner">
        <div class="ranking-content">
          <div class="desc">
            <div class="items" id="reviews" style="margin-top: 20px; margin-bottom: 60px">
              <template v-if="reviews.length > 0">
                <div v-for="(review, index) in reviews" :key="review.id" class="review-box fade-in">
                  <div class="review-content">
                    <div class="up-image" v-if="review.photos && review.photos.length > 0">
                      <img :src="`/reviews/${review.id}/${review.photos[0].fileName}`" alt="Review Image">
                    </div>
                    <div class="up-image" v-else>
                      not upload image
                    </div>
                    <div class="down-desc">
                      <h3>{{ review.title }}</h3>
                      <p>{{ truncate(review.content, 100) }}</p>
                      <router-link :to="'/reviews/' + review.id + '/details'" class="btn-explore">
                        Details <img src="@/assets/img/icon-arrow.png" alt="Arrow Icon" style="width: 30px">
                      </router-link>
                      <div class="heart-button">
                        <button @click="toggleHeart(review.id)" class="btn btn-link p-0">
                          <i class="bi" :class="review.userHasHearted ? 'bi-heart-fill text-danger' : 'bi-heart'"></i>
                        </button>
                        <span>{{ review.heartCount }}</span>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- Paging -->
                <nav aria-label="Page navigation example" class="fade-in">
                  <button class="page-button" :class="{ disabled: currentPage === 1 }">
                    <a class="page-link" @click="changePage(currentPage - 1)" aria-label="Previous">
                      <span aria-hidden="true">&laquo;</span>
                    </a>
                  </button>
                  <button v-for="page in totalPages" :key="page" class="page-button" :class="{ active: page === currentPage }">
                    <a class="page-link" @click="changePage(page)">{{ page }}</a>
                  </button>
                  <button class="page-button" :class="{ disabled: currentPage === totalPages }">
                    <a class="page-link" @click="changePage(currentPage + 1)" aria-label="Next">
                      <span aria-hidden="true">&raquo;</span>
                    </a>
                  </button>
                </nav>
              </template>
              <template v-else>
                <div class="card text-center thin-font rounded-circle p-5 fade-in" style="background-color: #f8f9fa; color: #212529;">
                  <div class="card-body">
                    <br>
                    <h5 class="card-title">There is no review yet</h5>
                    <p class="card-text">Let's write the first review!</p>
                    <router-link to="/reviews/form" class="btn btn-primary">Go!</router-link>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>
      </div>
    </section>
    <div class="container-footer fade-in" style="margin-bottom: 30px">
      <div class="heart">&hearts;</div>
      <blockquote>"Travel is the act of remembering yourself,<br> Traveling is about leaving where I was and seeing where I was"</blockquote>
    </div>
    <div class="footer">
      <img src="@/assets/img/7.png" style="position: absolute; botton: 0; left: 0; width: 100%; height: 55%; object-fit: cover; z-index: -1; margin-top: -50px;">
    </div>
  </div>
</template>
<script>
import axios from 'axios';
import NavigationBar from "@/components/NavigationBar.vue";

export default {
  components: { NavigationBar },
  name: 'Index',
  data() {
    return {
      reviews: [],
      totalPages: 0,
      currentPage: 1,
      pageSize: 3,
      observer: null
    };
  },
  mounted() {
    this.fetchAllReviews();
    this.initIntersectionObserver();
  },
  methods: {
    fetchAllReviews(page = 1, size = 3) {
      axios.get(`http://localhost:8090/api2/?page=${page - 1}&size=${size}`)
          .then(response => {
            this.reviews = response.data.content;
            this.totalPages = response.data.totalPages;
            this.reviews.forEach(review => {
              this.$set(review, 'heartCount', review.heartCount || 0);
              this.$set(review, 'userHasHearted', false);
            });
            this.updateUserHeartStatus();
            this.$nextTick(() => {
              this.observeElements();
            });
          })
          .catch(error => {
            console.error(error);
          });
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchAllReviews(page, this.pageSize);
      }
    },
    toggleHeart(reviewId) {
      const token = this.$cookies.get('access_token');
      if (!token) {
        return alert('로그인이 필요합니다.');
      }

      axios.post(`http://localhost:8090/api/reviews/${reviewId}/hearts`, {}, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      })
          .then(() => {
            this.updateHeartStatus(reviewId);
          })
          .catch(error => {
            console.log(error);
          });
    },
    updateHeartStatus(reviewId) {
      axios.get(`http://localhost:8090/api/reviews/${reviewId}/hearts/count`)
          .then(countResponse => {
            const review = this.reviews.find(r => r.id === reviewId);
            if (review) {
              this.$set(review, 'heartCount', countResponse.data);
            }
          })
          .catch(error => {
            console.log(error);
          });

      const token = this.$cookies.get('access_token');
      if (!token) return;

      axios.get(`http://localhost:8090/api/reviews/${reviewId}/user-heart`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      })
          .then(userHeartResponse => {
            const review = this.reviews.find(r => r.id === reviewId);
            if (review) {
              this.$set(review, 'userHasHearted', userHeartResponse.data);
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    updateUserHeartStatus() {
      const token = this.$cookies.get('access_token');
      if (!token) return;

      this.reviews.forEach(review => {
        this.updateHeartStatus(review.id);
      });
    },
    truncate(text, length) {
      if (text.length <= length) {
        return text;
      } else {
        return text.substring(0, length) + '...';
      }
    },
    initIntersectionObserver() {
      this.observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            entry.target.classList.add('show');
          }
        });
      }, {
        threshold: 0.1
      });
    },
    observeElements() {
      const elements = document.querySelectorAll('.fade-in');
      elements.forEach(element => {
        this.observer.observe(element);
      });
    }
  }
};
</script>
<style scoped>
@import '@/assets/css/home.css';
a{
  text-decoration-line: none;
}
.features-icons,
.about {
  background-color: #f8f9fa;
}

.fade-in {
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.6s ease-out, transform 0.6s ease-out;
}

.fade-in.show {
  opacity: 1;
  transform: translateY(0);
}


.hover-container {
  position: relative;
  overflow: hidden;
}


.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: opacity 0.3s ease-in-out;
}

.hover-container .hover-text {
  position: absolute;
  top: 0;
  left: 0;
  width: 93%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
}

.hover-container:hover .hover-text {
  opacity: 1;
}

.hover-text p {
  margin: 0;
  padding: 10px;
  text-align: center;
}


.hover-container .hover-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
  color: white;
}

.hover-text p {
  margin-bottom: 0;
}

.about {
  padding: 80px 0;
}

.about .inner {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.about .ranking-content .desc {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.about .ranking-content .desc {
  flex: 1;
}

.about .ranking-content .items {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 20px;
  margin-top: 30px;
}

.about .ranking-content .items .review-content {
  width: 900px;
  background: #fff;
  box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: box-shadow 0.3s ease-in-out;
}

.about .ranking-content .items .review-content:hover {
  box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.2);
}

.about .ranking-content .items .review-content .up-image {
  width: 900px;
  height: 500px;
  text-align: center;
  margin-bottom: 10px;
}

.about .ranking-content .items .review-content .up-image img {
  max-width: 100%;
  height: auto;
}

.about .ranking-content .items .review-content .down-desc {
  padding: 10px 0;
}

.about .ranking-content .items .review-content .down-desc h3 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.about .ranking-content .items .review-content .down-desc p {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  margin-bottom: 10px;
}

.about .ranking-content .items .review-content .down-desc .btn-explore {
  font-size: 14px;
  color: #c25a3a;
  display: inline-block;
  margin-right: 10px;
}

.about .ranking-content .items .review-content .down-desc .btn-explore img {
  width: 10px;
  vertical-align: middle;
}

.about .ranking-content .items .review-content .down-desc .heart-button {
  float: right;
  font-size: 14px;
  color: #999;
  display: inline-block;
  margin-left: 10px;
}

.about .ranking-content .items .review-content .down-desc .heart-button i {
  margin-right: 5px;
  vertical-align: middle;
}

.about .ranking-content .items .review-content .down-desc .heart-button span {
  vertical-align: middle;
}

.about .ranking-content .items .review-content .down-desc .heart-button:hover {
  color: #dc3545;
}

.about .ranking-content .items .review-content .down-desc .heart-button:hover i {
  color: #dc3545;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  list-style: none;
  padding: 0;
  margin-top: 20px;
}

.pagination .page-item {
  margin: 0 5px;
}

.pagination .page-item.disabled .page-link {
  color: #999;
  pointer-events: none;
  background-color: #fff;
  border-color: #dee2e6;
}

.pagination .page-item.active .page-link {
  z-index: 3;
  color: #fff;
  background-color: #007bff;
  border-color: #007bff;
}

.pagination .page-link {
  color: #007bff;
  text-decoration: none;
  background-color: #fff;
  border: 1px solid #dee2e6;
  padding: 8px 12px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.pagination .page-link:hover {
  background-color: #e9ecef;
  border-color: #dee2e6;
}

.review-box {
  max-width: 1000px;
  margin: 0 auto;
}

.review-box .review-content {
  width: 100%;
}

.review-box .review-content .up-image img {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
}

.review-box .review-content .down-desc {
  text-align: left;
}

.review-box .review-content .down-desc h3 {
  font-size: 24px;
  margin-bottom: 15px;
}

.review-box .review-content .down-desc p {
  font-size: 16px;
  margin-bottom: 15px;
}

.review-box .review-content .down-desc .btn-explore {
  margin-top: 10px;
  font-size: 16px;
}

.review-box .review-content .down-desc .heart-button {
  float: none;
  display: inline-flex;
  align-items: center;
  margin-top: 10px;
  font-size: 16px;
  color: #999;
}

.review-box .review-content .down-desc .heart-button i {
  margin-right: 5px;
}

.review-box .review-content .down-desc .heart-button span {
  margin-left: 5px;
}

.review-box .review-content .down-desc .heart-button:hover {
  color: #dc3545;
}

.review-box .review-content .down-desc .heart-button:hover i {
  color: #dc3545;
}

.page-button {
  padding: 5px 7px;
  margin: 1px;
  border:none;
  color: white;
  font-weight: 600;
  cursor: pointer;
  border-radius: 2px;
  transition: all .3s cubic-bezier(0.08, -0.05, 0.05, 0.05);
  background-color: lightskyblue;
}

.page-button:hover {
  background-color: rgb(37, 115, 218);
  color: #fff;
  border: none;
}

.loader {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 10em;
}


.circle {
  width: 1em;
  height: 1em;
  margin: 0 0.25em;
  border-radius: 50%;
  background-color: #a8d5e2;
  animation: wave 1.5s infinite ease-in-out;
  transition: background-color 0.3s ease;
}


@keyframes wave {
  0%, 100% {
    transform: translateY(0);
  }

  50% {
    transform: translateY(-1em);
  }
}


.circle:nth-child(1) {
  animation-delay: 0s;
}

.circle:nth-child(2) {
  animation-delay: 0.2s;
}

.circle:nth-child(3) {
  animation-delay: 0.4s;
}

.circle:nth-child(4) {
  animation-delay: 0.6s;
}

.circle:nth-child(5) {
  animation-delay: 0.8s;
}

.container-footer {
  font-family: 'Playfair Display', serif;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: -110px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.heart {
  color: #FF6F61;
  font-size: 24px;
  margin-bottom: 20px;
}

blockquote {
  font-style: italic;
  font-size: 24px;
  text-align: center;
  margin: 0;
}

.thin-font {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-weight: 300;

}
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounceIn {
  from, 20%, 40%, 60%, 80%, to {
    -webkit-animation-timing-function: cubic-bezier(0.215, 0.61, 0.355, 1);
    animation-timing-function: cubic-bezier(0.215, 0.61, 0.355, 1);
  }

  from {
    opacity: 0;
    transform: scale3d(.3, .3, .3);
  }

  20% {
    transform: scale3d(1.1, 1.1, 1.1);
  }

  40% {
    transform: scale3d(.9, .9, .9);
  }

  60% {
    opacity: 1;
    transform: scale3d(1.03, 1.03, 1.03);
  }

  80% {
    transform: scale3d(.97, .97, .97);
  }

  to {
    opacity: 1;
    transform: scale3d(1, 1, 1);
  }
}

.fade-in {
  opacity: 0;
  transition: opacity 0.5s ease-out, transform 0.5s ease-out;
}

.fade-in.show {
  opacity: 1;
  transform: translateY(0);
  animation: fadeInUp 1s ease-in-out;
}

</style>


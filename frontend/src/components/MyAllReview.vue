<template>
  <div class="container">
    <h2>모든 리뷰</h2>
    <div class="row">
      <div v-for="review in userReviews" :key="review.id" class="col-md-12 mb-4">
        <div class="card review-card">
          <div class="row no-gutters">
            <div class="col-md-6">
              <img v-if="review.photos && review.photos.length > 0" :src="review.photos[0].fileName" class="card-img" alt="Review Image">
              <img v-else src="@/assets/img/no-img.png" class="card-img" alt="Default Image">
            </div>
            <div class="col-md-6">
              <div class="card-body">
                <h5 class="card-title">{{ review.title }}</h5>
                <p class="card-text">{{ review.content }}</p>
                <router-link :to="'/reviews/' + review.id + '/details'" class="btn btn-primary">자세히 보기</router-link>
                <div class="heart-container">
                  <button @click="toggleHeart(review.id)" class="btn btn-link p-0">
                    <i class="bi" :class="review.userHasHearted ? 'bi-heart-fill text-danger' : 'bi-heart'"></i>
                  </button>
                  <span>{{ review.heartCount }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AllReviews',
  data() {
    return {
      token: this.$cookies.get("access_token"),
      userReviews: []
    }
  },
  mounted() {
    this.fetchUserReviews();
  },
  methods: {
    fetchUserReviews() {
      axios.get('http://localhost:8090/api/mypage/reviews', {
        headers: {
          Authorization: `Bearer ${this.token}`,
        }
      })
          .then(response => {
            if (Array.isArray(response.data)) {
              this.userReviews = response.data.reverse();
            } else {
              this.userReviews = [];
            }
            this.userReviews.forEach(review => {
              this.fetchHeartCount(review.id);
              this.fetchUserHeartStatus(review.id);
            });
          })
          .catch(error => {
            console.error(error);
          });
    },
    fetchHeartCount(reviewId) {
      axios.get(`http://localhost:8090/api/reviews/${reviewId}/hearts/count`)
          .then(response => {
            const review = this.userReviews.find(r => r.id === reviewId);
            if (review) {
              this.$set(review, 'heartCount', response.data);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    toggleHeart(reviewId) {
      axios.post(`http://localhost:8090/api/reviews/${reviewId}/hearts`, {}, {
        headers: {
          Authorization: `Bearer ${this.token}`,
        }
      })
          .then(() => {
            const review = this.userReviews.find(r => r.id === reviewId);
            if (review) {
              review.userHasHearted = !review.userHasHearted;
              review.userHasHearted ? review.heartCount++ : review.heartCount--;
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    fetchUserHeartStatus(reviewId) {
      axios.get(`http://localhost:8090/api/reviews/${reviewId}/hearts/user`, {
        headers: {
          Authorization: `Bearer ${this.token}`,
        }
      })
          .then(response => {
            const review = this.userReviews.find(r => r.id === reviewId);
            if (review) {
              this.$set(review, 'userHasHearted', response.data);
            }
          })
          .catch(error => {
            console.error(error);
          });
    }
  }
}
</script>

<style scoped>
.container {
  margin-top: 20px;
}
.review-card {
  border: none;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.card-img {
  height: 100%;
  object-fit: cover;
}
.card-body {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.card-title {
  font-size: 1.5rem;
  font-weight: bold;
}
.card-text {
  font-size: 1rem;
  margin-bottom: 20px;
}
.btn-primary {
  background-color: #007bff;
  border: none;
}
.heart-container {
  display: flex;
  align-items: center;
}
.heart-container .bi {
  font-size: 1.5rem;
}
</style>

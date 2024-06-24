<template>
  <div>
    <navigation-bar></navigation-bar>
  <div class="review-details">

    <div class="background">
      <video autoplay loop muted style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; z-index: -1;">
        <source :src="require('@/assets/img/tree.mp4')" type="video/mp4">
      </video>
      <div class="container mt-5">
        <div class="form-container" v-if="reviewDetails.id">
          <form id="reviewForm" @submit.prevent="submitForm" enctype="multipart/form-data">
            <h2 class="form-title">REVIEW DETAILS</h2>
            <br>
            <div class="description">HERE ARE THE DETAILS OF YOUR REVIEW.</div>

            <div class="form-group">
              <label for="title">제목</label>
              <input type="text" id="title" v-model="reviewDetails.title" class="form-control" readonly>
            </div>

            <div class="form-group">
              <label for="content">글 작성</label>
              <textarea id="content" v-model="reviewDetails.content" class="form-control" readonly></textarea>
            </div>

            <div class="form-group">
              <label>첨부 사진</label>
              <div class="photo-preview">
                <div v-for="photo in photos" class="preview-image-wrapper">
                  <img :src="photo" class="preview-image">
                </div>
              </div>
            </div>

            <div class="form-group">
              <form @submit.prevent="clickComment">
                <input type="textarea" id="comment" v-model="commentContent" class="form-control">
                <button v-if="isLoggedIn" type="submit" class="btn btn-info">등록</button>
              </form>
            </div><br><br>

            <div>
              <div v-if="commentList.length > 0">
                <div v-for="comment in commentList" :key="comment.id">
                  <div class="comment">
                    <div class="email-container">
                      <div class="email">
                        {{comment.email}}
                      </div>
                      <div>
                        <button v-if="isWriter" class="btn-delete btn btn-warning" @click="delete_comment(comment.id)">x</button>
                      </div>
                    </div>
                    <div class="content-container">
                      <div class="content">
                        {{comment.content}}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="button-group">
              <a v-if="isWriter" :href="`/reviews/${reviewDetails.id}/edit`" class="set-button">SET</a>
              <button v-if="isWriter" type="button" @click="confirmDelete" class="delete-button">DELETE</button>
            </div>
          </form>
          <div class="contact-info">
            <p>tel: +82 (0)123-456-789</p>
          </div>
        </div>
        <div v-else>
          <p>Loading...</p>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import axios from 'axios';
import NavigationBar from "@/components/NavigationBar.vue";
import Vue from "vue";

export default {
  components: { NavigationBar },
  name: 'ReviewDetails',
  data() {
    return {
      commentContent: '',
      photos: [],
      token: Vue.$cookies.get("access_token"),
      reviewDetails: {
        id: null,
        title: '',
        content: '',
        photoFiles: [],
        photoUrls: [],
        commentList: []
      },
      commentList: [],
      isLoggedIn: !!Vue.$cookies.get("access_token"),
      isWriter: false
    };
  },
  props: ['id'],
  created() {
    this.fetchReviewDetails(this.id);
    this.checkWriter(this.id);
  },
  methods: {
    delete_comment(commentId){
      const token = this.token;
      console.log(commentId)
      axios({
        url: 'http://localhost:8090/api/comment/' + commentId,
        method: "delete",
        headers: {
          Authorization: "Bearer " + token,
        }
      }).then(response => {
        alert("삭제되었습니다")
        location.reload();
      })
          .catch(error => {
            console.log(error);
          });
    },
    checkWriter(reviewId){
      let token = this.$cookies.get("access_token");
      let data = {
        reviewId: this.id
      }
      if(token != null){
        axios.get(`http://localhost:8090/api/reviews/${reviewId}/writer`,{
          headers:{
            Authorization: "Bearer " + token,
          }
        })
            .then(response => {
              if(response.data === 1){
                console.log(response.data)
                this.isWriter = true;
              }
            })
            .catch(error => {
              console.log("details of reviewId Error", error);
            });
      }
    },
    clickComment(){
      console.log(this.id)
      let data ={
        reviewId: this.id,
        content: this.commentContent
      }
      console.log(this.commentContent)
      let token = this.$cookies.get("access_token");
      axios(`http://localhost:8090/api/comment`, {
        data,
        method: 'post',
        headers: {
          "Content-Type": `application/json`,
          Authorization: "Bearer " + token,
        }
      })
          .then(response => {
            location.reload();
            console.log("성공")
          })
          .catch(error => {
            console.log("details of reviewId Error", error);
          });
    },
    fetchReviewDetails(reviewId) {
      let token = this.$cookies.get("access_token");
      axios.get(`http://localhost:8090/api2/reviews/${reviewId}/details`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      })
          .then(response => {
            this.reviewDetails = response.data;
            console.log(response.data)
            for (let i = 0; i < response.data.photos.length; i++) {
              this.photos.push(response.data.photos[i].fileName);
            }
            /*this.photos.push(response.data.photos);*/
            console.log(this.photos)
            if (Array.isArray(this.reviewDetails.photoFiles)) {
              this.reviewDetails.photoUrls = this.reviewDetails.photoFiles.map(file => `http://localhost:8090/api/files/${file}`);
            } else {
              this.reviewDetails.photoUrls = [];
            }
            this.commentList = response.data.commentList;
            /*console.log("review detail: ", response.data);*/
          })
          .catch(error => {
            console.log("리뷰 세부 정보를 가져오는 중에 오류가 발생했습니다.", error);
          });
    },
    submitForm() {
    },
    confirmDelete() {
      if (confirm("정말로 삭제하시겠습니까?")) {
        this.deleteReview(this.reviewDetails.id);
      }
    },
    deleteReview(reviewId) {
      let token = this.$cookies.get("access_token");
      axios.delete(`http://localhost:8090/api/reviews/${reviewId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      })
          .then(response => {
            alert("리뷰가 삭제되었습니다.");
            this.$router.push('/mypage');
          })
          .catch(error => {
            console.error("리뷰 삭제 중 오류가 발생했습니다.", error);
            alert("리뷰 삭제 중 오류가 발생했습니다.");
          });
    },
    promptLogin() {
      alert("로그인이 필요합니다.");
    }
  }
};
</script>

<style>
@import '@/assets/css/reviewDetails.css';
.review {
  margin-bottom: 20px;
}

.comment{
  border: solid 1px white;
  border-radius: 5px;
}
.email-container{
  height: 50px;
  border-bottom: solid 1px white;
  display: flex;
}
.email{
  margin: 15px;
  width: 93%;
}
.content{
  margin: 15px;
}
.btn-delete button{
  padding: 3px;
  width: 40px;
  height: 40px;
  margin: 5%;
}
</style>
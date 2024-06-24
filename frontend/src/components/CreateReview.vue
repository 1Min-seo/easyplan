<template>
  <div>
    <navigation-bar></navigation-bar>
    <br>
    <div class="background">
      <video autoplay loop muted style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; z-index: -1;">
        <source :src="require('@/assets/img/tree.mp4')" type="video/mp4">
      </video>
      <div class="container mt-5">
        <div class="form-container">
          <form role="form" id="reviewForm" @submit.prevent="submitReview" enctype="multipart/form-data">
            <div class="title-container">
              <h2 class="form-title">EASY-REVIEW</h2>
              <span class="title-icon"><i class="fa-solid fa-pencil"></i></span>
            </div>
            <div class="form-group">
              <input type="text" id="title" v-model="title" class="form-control" :class="{ fieldError: errors.title }" placeholder="Title*">
              <div v-if="errors.title" class="error-message">Enter Your Title.</div>
            </div>

            <div class="form-group">
              <textarea id="content" v-model="content" class="form-control large-textarea" :class="{ fieldError: errors.content }" placeholder="Content*"></textarea>
              <div v-if="errors.content" class="error-message">Enter Your Content.</div>
            </div>

            <div class="form-group">
              <input type="file" ref="fileInput" id="photoFiles" class="form-control" multiple @change="handleFileUpload">
              <div v-if="fileList.length > 0" class="photo-preview">
                <div v-for="(photoUrl, index) in photoUrls" :key="index" class="preview-image-wrapper">
                  <img :src="photoUrl" alt="Preview" class="preview-image">
                </div>
                <div class="photo-count">{{ fileList.length }} 장의 사진이 선택되었습니다.</div>
              </div>
            </div>

            <div class="form-group text-center">
              <button type="submit" class="button">
                <p class="submit">submit</p>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import NavigationBar from "@/components/NavigationBar.vue";

export default {
  components: {
    NavigationBar
  },
  data() {
    return {
      title: "",
      content: "",
      fileList: [],
      photoUrls: [],
      errors: {}
    };
  },
  methods: {
    submitReview() {
      this.errors = {};
      if (!this.title) {
        this.errors.title = true;
      }
      if (!this.content) {
        this.errors.content = true;
      }
      if (Object.keys(this.errors).length > 0) {
        return;
      }
      let formData = new FormData();
      formData.append('title', this.title);
      formData.append('content', this.content);
      this.fileList.forEach(file => {
        formData.append('photoFiles', file);
      });
      let token = this.$cookies.get("access_token");
      axios.post('http://localhost:8090/api/reviews', formData, {
        headers: {
          Authorization: "Bearer " + token,
        }
      })
          .then(response => {
            alert('리뷰가 성공적으로 작성되었습니다.');
            this.$router.push('/mypage');
          })
          .catch(error => {
            console.log(error);
            if (error.response && error.response.data) {
              this.errors = error.response.data;
            }
            alert('리뷰 제출에 실패하였습니다.');
          });
    },
    handleFileUpload() {
      this.fileList = Array.from(this.$refs.fileInput.files);
      this.photoUrls = this.fileList.map(file => URL.createObjectURL(file));
    }
  }
};
</script>

<style scoped>
@import '@/assets/css/createReview.css';
</style>

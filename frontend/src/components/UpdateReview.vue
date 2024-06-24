<template>
  <div>
    <navigation-bar></navigation-bar>
    <br>
    <div class="background">
      <video autoplay loop muted style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; z-index: -1;">
        <source :src="require('@/assets/img/balloon.mp4')" type="video/mp4">
      </video>
      <div class="container mt-5">
        <div class="form-container">
          <form id="reviewForm" @submit.prevent="submitReviewEdit" enctype="multipart/form-data">
            <h2 class="form-title">EDIT REVIEW</h2>
            <br>
            <div class="description">PLEASE UPDATE YOUR REVIEW DETAILS BELOW.</div>

            <div class="form-group">
              <label for="title">Title</label>
              <input type="text" id="title" v-model="reviewEdit.title" class="form-control">
            </div>

            <div class="form-group">
              <label for="content">Content</label>
              <textarea id="content" v-model="reviewEdit.content" class="form-control"></textarea>
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

            <button type="submit" class="btn btn-primary">submit</button>
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
  name: 'UpdateReview',
  data() {
    return {
      reviewEdit: {
        title: '',
        content: '',
        fileList: [],
        errors: {}
      },
      title: "",
      content: "",
      fileList: [],
      photoUrls: [],
      errors: {}
    };
  },
  props: ['id'],
  created() {
    this.loadReviewData(this.id);
  },
  methods: {
    loadReviewData(reviewId) {
      let token = this.$cookies.get("access_token");
      axios.get(`http://localhost:8090/api/reviews/${reviewId}/editForm`, {
        headers: {
          Authorization: "Bearer " + token,
        }
      })
          .then(response => {
            this.reviewEdit.title = response.data.title;
            this.reviewEdit.content = response.data.content;
            this.reviewEdit.fileList = response.data.fileUrls || [];
          })
          .catch(error => {
            console.log(error);
          });
    },
    submitReviewEdit() {
      let token = this.$cookies.get("access_token");
      console.log("UpdateReview token: " + token);

      const formData = new FormData();
      formData.append('title', this.reviewEdit.title);
      formData.append('content', this.reviewEdit.content);
      this.reviewEdit.fileList.forEach(file => {
        formData.append('photoFiles', file);
      });

      axios.post(`http://localhost:8090/api/reviews/${this.id}/edit`, formData, {
        headers: {
          Authorization: "Bearer " + token,
        }
      })
          .then(response => {
            alert('리뷰가 성공적으로 수정되었습니다.');
            this.$router.push('/mypage');
          })
          .catch(error => {
            console.log(error);
            if (error.response && error.response.data) {
              this.reviewEdit.errors = error.response.data;
            }
            alert('리뷰 수정을 실패했습니다.');
          });
    },
    handleFileUpload() {
      this.reviewEdit.fileList = Array.from(this.$refs.fileInput.files);
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Didot:wght@400&family=Poppins:wght@500&display=swap');

body {
  font-family: 'Poppins', sans-serif;
  background-color: #F9FAFA;
  color: #333;
  margin: 0;
  padding: 0;
}

.background {
  position: relative;
  overflow: hidden;
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 90%;
}

.form-container {
  padding: 20px;
  width: 70%;
  max-width: 820px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #eaeaea;
}

.form-title {
  margin-bottom: 20px;
  font-size: 36px;
  font-family: 'Didot', serif;
  text-align: center;
}

.description {
  margin-bottom: 20px;
  font-size: 16px;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

.form-control {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: none;
  border-bottom: 1px solid #ddd;
  transition: border-color 0.3s ease;
  background-color: transparent;
}

.form-control::placeholder {
  color: #aaa;
  font-weight: 500;
}

.form-control:focus {
  outline: none;
  border-bottom: 2px solid #000;
}

textarea.form-control {
  resize: none;
  height: 150px;
}

.form-control:focus-visible {
  outline: none;
}

.btn {
  width: 100%;
  padding: 14px;
  font-size: 18px;
  color: #fff;
  background-color: #FF9166;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn:hover {
  background-color: #FF7043;
}

.btn:active {
  transform: scale(0.98);
}

.error-message {
  color: red;
  font-size: 14px;
  margin-top: 5px;
}

.fieldError {
  border-color: red;
}

.contact-info {
  text-align: center;
  margin-top: 20px;
  color: #333;
}

.photo-preview {
  text-align: center;
}

.preview-image-wrapper {
  display: inline-block;
  margin-right: 10px;
}

.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.photo-count {
  margin-top: 10px;
  font-size: 14px;
  color: #888;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
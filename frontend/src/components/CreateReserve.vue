<template>
  <div>
    <navigation-bar></navigation-bar>
    <br><br><br>
    <div class="container">
      <form role="form" id="reviewForm" @submit.prevent="submitReserve" enctype="multipart/form-data">
        <div class="form-group">
          <label for="content">글 작성</label>
          <input type="text" id="content" v-model="content" class="form-control" :class="{ 'fieldError': errors.content }">
          <span v-if="errors.content" class="error-message">{{ errors.content }}</span>
        </div>

        <button type="submit" class="btn btn-primary">작성완료</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import NavigationBar from "@/components/NavigationBar.vue";

export default {
  components: {NavigationBar},
  data() {
    return {
      content: "",
      fileList: [],
      errors: {}
    };
  },
  methods: {
    submitReserve() {

      let token=this.$cookies.get("access_token");
      console.log("token: " + token)
      console.log("content: " + this.content)
      axios({
        url: 'http://localhost:8090/api/reserve',
        method: "post",
        data: {
          content: this.content,
        },
        headers: {
          Authorization: "Bearer " + token,
        }
      }).then(response => {
        alert('리뷰가 성공적으로 제출되었습니다.');
        this.$router.push('/schedule')
      })
          .catch(error => {
            console.log(error);
            if (error.response && error.response.data) {
              this.errors = error.response.data;
            }
            alert('리뷰 제출에 실패했습니다.');
          });
    }
  }
};
</script>


<style scoped>
.error-message {
  color: red;
}
.fieldError {
  border-color: red;
}
</style>
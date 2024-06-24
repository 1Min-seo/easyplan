<template>
  <div v-if="token != null">
    <navigation-bar></navigation-bar>
    <br>
    <br>
    <br>
    <br>
    <br>
    <div id="page-top">
      <section class="about" id="ranking" style="margin-left:15%; margin-right: 18%">
        <div class="inner">
          <div class="ranking-content">
            <div class="desc">
              <h2 class="thin-font">Plan</h2>
            </div><br>
            <div class="itemss">
              <template v-if="reserves.length > 0">
                <div v-for="reserve in reserves" :key="reserve.id" class="item" id="reviews">
                  <div class="down-desc">
                    <p>{{ reserve.content }}</p>
                    <div v-if="reserves_date.includes(reserve.id) === true">
                      <div style="margin-right: 0; justify-content: flex-end; display: flex">
                        <div v-if="reserve.type === 0">
                          <button @click="click_date(reserve.id)" class="set-button">confirm</button>
                          <button style="margin-left: 5px" @click="delete_reserve(reserve.id)" class="delete-button">delete</button>
                        </div>
                        <div v-if="reserve.type === 1">
                          <button style="margin-left: 5px" @click="delete_schedule(reserve.id)" class="delete-button">cancel</button>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-show="reserves_date.includes(reserve.id) === false">
                    <div style="display: flex;">
                      <input type="text" v-model="year" class="input-date form-control" style="height:30px">-
                      <input type="text" v-model="months" class="input-date form-control" style="height:30px">-
                      <input type="text" v-model="day" class="input-date form-control" style="height:30px">
                    </div>
                    <button @click="schedule(reserve.id)" class="set-button">receive</button>
                  </div>
                  <div v-show="reserves_date.includes(reserve.id) === false">
                  </div>
                </div>
              </template>
              <template v-else>
                <div class="card text-center" style="margin-left: 290px; padding: 50px; width: 400px">
                  <div class="card-body">
                    <h5 class="card-title thin-font">일정이 아직 없습니다.</h5>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>
<script>
import axios from 'axios';
import NavigationBar from "@/components/NavigationBar.vue";
import Vue from 'vue';

export default{
  components: {NavigationBar},
  name: 'Schedule',
  data() {
    return {
      token: Vue.$cookies.get("access_token"),
      userInfo: null,
      reserves: [],
      year: '',
      months: '',
      day: '',
      reserves_date: []
    }
  },
  mounted() {
    this.userReserves();
    this.click_date();
  },
  created() {
    this.click_date();
  },
  methods: {
    delete_reserve(id){
      const token = this.token;
      console.log(id)
      axios({
        url: 'http://localhost:8090/api/reserve/' + id,
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
    delete_schedule(id){
      const token = this.token;
      console.log(id)
      axios({
        url: 'http://localhost:8090/api/schedule/' + id,
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
    click_date(id){
      console.log(id);
      console.log(this.reserves_date)
      this.reserves_date = this.reserves_date.filter((element)=> element !== id)
    },
    userReserves() {
      const token = this.token;
      axios.get('http://localhost:8090/api/reserve', {
        headers: {
          Authorization: "Bearer " + token,
        }
      })
          .then(response => {
            console.log(response.data)
            this.reserves = response.data;
            for (let i = 0; i < response.data.length; i++) {
              this.reserves_date.push(response.data[i].id)
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    schedule(id) {
      const token = this.token;
      console.log(id)
      axios({
        url: 'http://localhost:8090/api/schedule',
        method: "post",
        data: {
          id: id,
          date: this.year + "-" + this.months + "-" + this.day,
        },
        headers: {
          Authorization: "Bearer " + token,
        }
      }).then(response => {
        console.log(response.data)
        alert("예약 발송에 성공했습니다")
        location.reload();
      })
          .catch(error => {
            console.log(error);
            alert("이미 예약 발송된 일정입니다")
            location.reload();
          });
      axios({
        url: 'http://localhost:8090/api/reserve/' + id,
        method: "put",
        headers: {
          Authorization: "Bearer " + token,
        }
      }).then(response => {
        console.log(response)
      })
          .catch(error => {
            console.log(error);
            alert("이미 예약 발송된 일정입니다")
            location.reload();
          });
    }

  }
}
</script>
<style scoped>
@import '@/assets/css/mypage.css';
body {
  background-color: #f0f0f0;
}
.itemss{
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  grid-auto-flow: dense;
}

.input-date{
  width: 100px;
}

button{
  text-decoration: none;
  height: 2.5em;
  width: 6em;
  border: none;
  border-radius: 1em;
  font-size: 16px;
  color: #ffffff;
  font-family: 'Roboto', sans-serif;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;

  justify-content: center;
  align-items: center;
}

.delete-button {
  background: #D90429;
}

.set-button {
  background: #016DD9;
}

.thin-font {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-weight: 300;

}

.desc h2.thin-font {
  text-align: center;
  margin-bottom: 20px;
}

.card.text-center .card-body {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
}

</style>
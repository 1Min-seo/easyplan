<template>
  <div>
    <navigation-bar></navigation-bar>
    <br><br><br><br>
    <div id="page-top">
      <nav class="navbar navbar-expand-lg navbar-dark fixed-top" id="sideNav">
        <a class="navbar-brand js-scroll-trigger" href="#page-top">
          <span class="d-block d-lg-none">ms-h</span>
          <span class="d-none d-lg-block">
            <!-- 카톡 이미지 -->
          </span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
          <ul class="navbar-nav">
            <li class="nav-item"><router-link to="/reviews/form" class="nav-link thin-font">Create Review</router-link></li>
            <li class="nav-item"><a class="nav-link js-scroll-trigger thin-font" href="#">My Review</a></li>
            <li class="nav-item"><a class="nav-link js-scroll-trigger thin-font" href="#" @click="scrollToChat">My Chat</a></li>
          </ul>
        </div>
      </nav>
      <section class="content-section" id="content-section">
        <div class="inner" >
          <div class="ranking-content" style="background-color: rgba(90, 98, 112, 0.05); width: 1050px;">
            <h2 class="thin-font">My Review</h2>
            <div class="items" id="reviews">
              <template v-if="userReviews && userReviews.length > 0">
                <div v-for="(review) in userReviews.slice(0, 6)" :key="review.id" class="item">
                  <div class="up-image" v-if="review.photos && review.photos.length > 0">
                    <img :src="`/reviews/${review.id}/${review.photos[0].fileName}`" alt="Review Image">
                    <!--                    <img :src="review.photos[0].fileName" alt="Review Image" >-->
                  </div>
                  <div class="up-image" v-else>
                    <img src="@/assets/img/no-img.png" alt="Default Image">
                  </div>
                  <div class="down-desc">
                    <h3>{{ review.title }}</h3>
                    <p class="thin-font">{{ truncate(review.content, 20) }}</p>
                    <router-link :to="'/reviews/' + review.id + '/details'" class="btn-explore thin-font">
                      Details <img src="@/assets/img/icon-arrow.png" alt="Arrow Icon">
                    </router-link>
                    <div>
                      <button @click="toggleHeart(review.id)" class="btn btn-link p-0">
                        <i class="bi" :class="review.userHasHearted ? 'bi-heart-fill text-danger' : 'bi-heart'"></i>
                      </button>
                      <span class="thin-font">{{ review.heartCount }}</span>
                    </div>
                  </div>
                </div>
                <div v-if="userReviews.length > 6" class="text-center">
                  <router-link to="/reviews/all" class="btn btn-secondary read-more thin-font button2">Read More</router-link>
                </div>
              </template>
              <template v-else>
                <div class="no-review-card card text-center">
                  <div class="card-body">
                    <h5 class="card-title thin-font" style="font-weight: bold">There is no review yet</h5>
                    <p class="card-text thin-font">Let's write the first review!</p>
                    <router-link to="/reviews/form" class="btn btn-primary cta button2 thin-font">Go</router-link>
                  </div>
                </div>
              </template>
            </div>

            <!-- Chat -->
            <div class="chat-section" id="chat-section">
              <h2 class="thin-font" style="display: inline-block; margin-right: 10px;">Chat Rooms</h2>
              <button @click="toggleShowCreateRoom" class="chat-btn thin-font button2" >
                <span style="margin-bottom: 30px; font-size: 15px">{{ showCreateRoom ? '<--' : 'List' }}</span>
              </button>
              <div v-if="showCreateRoom">
                <div class="form-group">
                  <label for="roomName" class="thin-font">room name</label>
                  <input type="text" v-model="roomName" class="form-control" id="roomName" placeholder="채팅방 이름">
                </div>
                <div class="form-group d-flex align-items-center">
                  <label for="inviteEmail" class="mb-0 thin-font">counterpart e-mail</label>
                  <button type="button" class="btn btn-link ms-2 p-0" @click="addInviteEmail">
                    <i class="fas fa-plus-circle"></i>
                  </button>
                </div>
                <div class="form-group">
                  <input type="email" v-model="inviteEmails[0]" class="form-control" id="inviteEmail" placeholder="상대 이메일 주소">
                </div>
                <div class="form-group" v-for="(email, index) in inviteEmails.slice(1)" :key="index">
                  <input type="email" v-model="inviteEmails[index + 1]" class="form-control" placeholder="추가 상대 이메일 주소">
                </div>
                <button type="button" class="btn btn-primary thin-font button2" @click="createChatRoom">Create</button>
              </div>
              <div v-else>
                <ul v-if="chatRooms.length > 0" class="chat-room-list">
                  <li v-for="room in chatRooms" :key="room.id">
                    <span class="loader"></span>
                    <router-link :to="{ path: `/chat/${room.id}`, query: { roomName: room.name } }" class="chat-room-link">
                      {{ room.name }}
                    </router-link>
                    <button @click="deleteChatRoom(room.id)" class="btn btn-link">
                      <i class="fa-solid fa-trash-can"></i>
                    </button>
                  </li>
                </ul>
                <p v-else class="thin-font">No chat rooms found.</p>
              </div>
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

export default {
  components: {NavigationBar},
  name: 'MyPage',
  data() {
    return {
      token: this.$cookies.get("access_token"),
      userReviews: [],
      roomName: '',
      inviteEmails: [''],
      showCreateRoom: false,
      chatRooms: []
    };
  },
  mounted() {
    this.fetchUserReviews();
    this.fetchChatRooms();
  },
  methods: {
    truncate(text, length) {
      if (text.length <= length) return text;
      return text.substring(0, length) + '...';
    },
    scrollToChat() {
      const chatSection = document.getElementById('chat-section');
      if (chatSection) {
        chatSection.scrollIntoView({ behavior: 'smooth' });
      }
    },

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
      axios.get(`http://localhost:8090/api/reviews/${reviewId}/hearts/count`, {
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
    },
    fetchChatRooms() {
      axios.get('http://localhost:8090/api/chatRoom/roomList', {
        headers: {
          Authorization: `Bearer ${this.token}`,
        }
      })
          .then(response => {
            if (Array.isArray(response.data)) {
              console.log(response.data)
              this.chatRooms = response.data;
            } else {
              this.chatRooms = [];
            }
          })
          .catch(error => {
            console.error('fail to fetch chat room:', error);
          });
    },
    createChatRoom() {
      const roomName = this.roomName.trim();

      if (!roomName) {
        alert('채팅방 이름을 설정해주세요.');
        return;
      }

      axios.post('http://localhost:8090/api/chatRoom/createRoom', this.inviteEmails.filter(email => email.trim()), {
        params: {name: roomName},
        headers: {
          Authorization: `Bearer ${this.token}`,
        }
      })
          .then(response => {
            const newRoomId = response.data.roomId;

            if (!newRoomId) {
              console.error('room ID 못 찾음:', response.data);
              return;
            }

            this.roomName = '';
            this.inviteEmails = [''];
            this.showCreateRoom = false;

            let chatRoom = this.chatRooms.find(room => room.id === newRoomId);
            if (!chatRoom) {
              chatRoom = {
                id: newRoomId,
                name: roomName
              };
              this.chatRooms.push(chatRoom);
            }

            this.$router.push({path: `/chat/${newRoomId}`, query: {roomName: roomName}});
          })
          .catch(error => {
            console.error('fail to create chat room', error);
            if (error.response && error.response.data && error.response.data.error) {
              alert(error.response.data.error);
            } else {
              alert('채팅방 생성에 실패했습니다.');
            }
          });
    },
    addInviteEmail() {
      this.inviteEmails.push('');
    },
    toggleShowCreateRoom() {
      this.showCreateRoom = !this.showCreateRoom;
    },
    deleteChatRoom(roomId) {
      if (confirm("정말 이 채팅방을 삭제하시겠습니까?")) {
        axios.delete(`http://localhost:8090/api/chatRoom/${roomId}`, {
          headers: {
            Authorization: `Bearer ${this.token}`,
          }
        })
            .then(() => {
              this.chatRooms = this.chatRooms.filter(room => room.id !== roomId);
              console.log(`Chat room ${roomId}: 삭제 성공`);
            })
            .catch(error => {
              console.error(`Failed to delete chat room: ${roomId}`, error);
              alert('채팅방 삭제에 실패했습니다.');
            });
      }
    }
  }
}
</script>

<style scoped>
body {
  background-color: #f0f0f0;
}

#sideNav {
  position: fixed;
  top: 0;
  left: 0;
  width: 280px;
  height: 100%;
  overflow-x: hidden;
  padding-top: 20px;
  z-index: 1;
  background-color: #5a6270;
  text-decoration-line: none;
  color: white;
}

.navbar-brand {
  text-align: center;
  margin-bottom: 30px;
}


.navbar-nav {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.nav-item {
  margin-bottom: 15px;
  width: 100%;
  display: flex;
  justify-content: center;
}

.nav-link {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  text-align: center;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 0;
  color: white;
}

.nav-link:hover {
  width: 200px;
  background-color: #494f5c;
}

.content-section {
  margin-left: 360px;
  padding: 20px;
}

.ranking-content {
  background-color: #f8f8f8;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 40px;
}

.items {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  grid-auto-flow: dense;
}

.item {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  width: 1000px;
}

.table-content {
  padding: 20px;
  border-radius: 8px;
  background-color: #f8f8f8;
  margin-bottom: 40px;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th, .table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.table tr:nth-child(even) {
  background-color: #f9f9f9;
}


.btn-explore {
  display: flex;
  align-items: center;
  text-decoration: none;
  font-weight: bold;
}

.btn-explore img {
  margin-left: 5px;
}

@media screen and (max-width: 1200px) {
  .content-section {
    margin-left: 300px;
  }

  .item {
    width: 500px;
  }
}

@media screen and (max-width: 992px) {
  .content-section {
    margin-left: 280px;
    padding: 15px;
  }

  .items {
    grid-template-columns: 1fr;
  }
}

@media screen and (max-width: 768px) {
  .content-section {
    margin-left: 0;
    padding: 10px;
  }

  .items {
    grid-template-columns: 1fr;
  }
}


.chat-section {
  background-color: #f8f8f8;
  padding: 20px;
  border-radius: 8px;
}

.chat-btn {
  background-color: #007bff;
  color: #fff;
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}


.chat-room-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
  text-align: left;
}

.chat-room-list li {
  background-color: #fff;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-room-link {
  text-decoration: none;
  color: #007bff;
  font-weight: bold;
  flex-grow: 1;
  margin-left: 10px;
}

.chat-room-link:hover {
  text-decoration: underline;
}

.loader {
  border: 4px solid #f3f3f3;
  border-radius: 50%;
  border-top: 4px solid #3498db;
  width: 16px;
  height: 16px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.btn-link {
  margin-left: auto;
}

.no-review-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.cta {
  position: relative;
  display: inline-block;
  padding: 12px 18px;
  transition: all 0.2s ease;
  border: none;
  background: none;
  cursor: pointer;
  text-decoration: none;
  color: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.cta span {
  font-family: "Ubuntu", sans-serif;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.05em;
}

.cta svg {
  position: relative;
  top: 0;
  margin-left: 10px;
  fill: none;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke: #fff;
  stroke-width: 2;
  transform: translateX(-5px);
  transition: all 0.3s ease;
}


.cta:hover svg {
  transform: translateX(0);
}

.cta:active {
  transform: scale(0.95);
}

.thin-font {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-weight: 300;

}

.button2 {
  height: 40px;
  display: inline-block;
  transition: all 0.2s ease-in;
  position: relative;
  overflow: hidden;
  z-index: 1;
  color: #090909;
  padding: 0.7em 1.7em;
  cursor: pointer;
  font-size: 18px;
  border-radius: 0.5em;
  background: #e8e8e8;
  border: 1px solid #e8e8e8;
  box-shadow: 6px 6px 12px #c5c5c5, -6px -6px 12px #ffffff;
}

.button2:active {
  color: #666;
  box-shadow: inset 4px 4px 12px #c5c5c5, inset -4px -4px 12px #ffffff;
}

.button2:before {
  content: "";
  position: absolute;
  left: 50%;
  transform: translateX(-50%) scaleY(1) scaleX(1.25);
  top: 100%;
  width: 140%;
  height: 180%;
  background-color: rgba(0, 0, 0, 0.05);
  border-radius: 50%;
  display: block;
  transition: all 0.5s 0.1s cubic-bezier(0.55, 0, 0.1, 1);
  z-index: -1;
}

.button2:after {
  content: "";
  position: absolute;
  left: 55%;
  transform: translateX(-50%) scaleY(1) scaleX(1.45);
  top: 180%;
  width: 160%;
  height: 190%;
  background-color: #009087;
  border-radius: 50%;
  display: block;
  transition: all 0.5s 0.1s cubic-bezier(0.55, 0, 0.1, 1);
  z-index: -1;
}

.button2:hover {
  color: #ffffff;
  border: 1px solid #009087;
}

.button2:hover:before {
  top: -35%;
  background-color: #009087;
  transform: translateX(-50%) scaleY(1.3) scaleX(0.8);
}

.button2:hover:after {
  top: -45%;
  background-color: #009087;
  transform: translateX(-50%) scaleY(1.3) scaleX(0.8);
}

.read-more{
  margin-left: -340px;
}

.up-image {
  width: 100%;
  height: 300px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.up-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.down-desc {
  padding: 10px;
}

.action-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.heart-section {
  display: flex;
  align-items: center;
  gap: 5px;
}

.heart-section button {
  border: none;
  background: none;
}

.heart-section i {
  font-size: 20px;
}

.heart-section span {
  font-size: 16px;
}


</style>

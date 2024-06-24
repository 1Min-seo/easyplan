<template>
  <div class="chat-room">
    <navigation-bar></navigation-bar>
    <br><br><br>
    <div class="chat-room-header" style="background-color: white;">
      <h1 class="thin-font" ><p class="thin-font" style="margin-left: 160px; margin-bottom:10px; font-size: 15px;" >{{ roomName }} 방에 오신 것을 환영합니다!</p></h1>
    </div>
    <div ref="messages" class="messages-container">
      <ul>
        <transition-group name="message" tag="div">
          <li v-for="message in messages" :key="message.id" :class="{'message-right': message.senderEmail === userEmail && message.type !== 0, 'message-left': message.senderEmail !== userEmail && message.type !== 0, 'message-middle': message.type === 0}">
            <div v-if="message.type === 1">
              <span class="message-content message-bubble">{{ message.content }}</span>
            </div>
            <div v-if="message.type === 0" style="margin: 0 auto">
              <span class="message-content thin-font">GPT</span><br>
              <span class="message-content">{{ message.content }}</span><br>
              <button class="btn" @click="reserve(message.content)">예비 일정으로 등록</button>
            </div>
          </li>
        </transition-group>
      </ul>
    </div>
    <div class="change-btn-container">
      <div v-if="this.msgType==='normal'" class="change-btn" >
        <button class="btn-chatType thin-font" @click="changeGPT()" style="color: purple">
          Chat
          GPT
        </button>
      </div>
      <div v-if="this.msgType==='gpt'" class="change-btn">
        <button class="btn btn-chatType" @click="changeChat()">
          Chat
        </button>
      </div>
    </div>
    <div v-if="this.msgType==='normal'" class="message-input-container">
      <input type="text" v-model="newMessage" @keyup.enter="sendMessage" class="message-input">
      <button @click="sendMessage" class="send-button"> Send </button>
    </div>
    <div v-if="this.msgType==='gpt'" class="message-input-container">
      <input type="text" v-model="newGPT" @keyup.enter="sendGPT()" class="message-input">
      <button @click="sendGPT()" class="send-button">GPT</button>
    </div>
  </div>
</template>

<script>
import NavigationBar from "@/components/NavigationBar.vue";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import axios from "axios";

export default {
  components: { NavigationBar },
  name: 'ChatRoom',
  props: {
    roomId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      msgType: 'normal',
      newGPT: '',
      messages: [],
      newMessage: '',
      roomName: this.$route.query.roomName || '',
      token: this.$cookies.get("access_token"),
      stompClient: null,
      userEmail: '',
      messageIds: new Set()
    };
  },
  mounted() {
    this.fetchUserEmail();
    this.connectWebSocket();
    this.fetchMessages();
  },
  methods: {
    reserve(content){
      console.log(content)
      let token = this.$cookies.get("access_token");
      axios(`http://localhost:8090/api/reserve/${this.roomId}`, {
        data:{
          content
        },
        method: 'post',
        headers: {
          "Content-Type": `application/json`,
          Authorization: "Bearer " + token,
        }
      })
          .then(response => {
            console.log("성공")
            alert("예비 일정에 등록되었습니다")
          })
          .catch(error => {
            console.log("details of reviewId Error", error);
          });

    },
    changeGPT(){
      this.msgType = 'gpt'
    },
    changeChat(){
      this.msgType = 'normal'
    },
    sendGPT() {
      if (!this.newGPT.trim()) {
        return;
      }
      const message = {
        senderEmail: this.userEmail,
        content: this.newGPT,
      };
      console.log('Sending message:', message);

      this.stompClient.send(`/app/room/${this.roomId}/gpt`, JSON.stringify(message), {
        Authorization: `Bearer ${this.token}`
      });
      this.newGPT = '';
    },
    connectWebSocket() {
      const socket = new SockJS('http://localhost:8090/ws');
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({Authorization: `Bearer ${this.token}`}, frame => {
        console.log('Connected: ' + frame);
        this.stompClient.subscribe(`/topic/room/${this.roomId}`, message => {
          if (message.body) {
            const parsedMessage = JSON.parse(message.body);
            if (!this.messageIds.has(parsedMessage.id)) { // 중복 메시지 체크
              this.messages.push(parsedMessage);
              this.messageIds.add(parsedMessage.id);
              this.scrollToBottom();
            }
          }
        });
      }, error => {
        console.error('Error on connecting WebSocket', error);
      });
    },

    fetchUserEmail() {
      axios.get(`http://localhost:8090/api/chatRoom/sender/info`, {
        headers: { Authorization: `Bearer ${this.token}` }
      })
          .then(response => {
            this.userEmail = response.data;
            console.log('userEmail: ', this.userEmail);
            window.localStorage.setItem('sender email', this.userEmail);
          })
          .catch(error => {
            console.error('Failed to fetch user email:', error);
          });
    },

    sendMessage() {
      if (!this.newMessage.trim()) {
        return;
      }
      const message = {
        senderEmail: this.userEmail,
        content: this.newMessage,
      };
      console.log('Sending message:', message);

      this.stompClient.send(`/app/room/${this.roomId}/send`, JSON.stringify(message), {
        Authorization: `Bearer ${this.token}`
      });
      this.newMessage = '';
    },

    fetchMessages() {
      axios.get(`http://localhost:8090/api/chatRoom/${this.roomId}`, {
        headers: { Authorization: `Bearer ${this.token}` }
      })
          .then(response => {
            this.messages = response.data;
            // 메시지 ID들을 Set에 추가
            this.messages.forEach(message => {
              this.messageIds.add(message.id);
            });
            this.scrollToBottom();
          })
          .catch(error => {
            console.error('Failed to fetch messages:', error);
          });
    },

    scrollToBottom() {
      this.$nextTick(() => {
        this.$refs.messages.scrollTop = this.$refs.messages.scrollHeight;
      });
    }
  }
};
</script>

<style scoped>
@import '@/assets/css/chatRoom.css';
.btn-chatType{
  height: 50px;
  width: 50px;
  padding: 5px;
  background-color: white;
  text-align: center;
  font-size: 80%;
  border-radius: 50%;
  border: solid 1px black;
}
.change-btn{
  background-color: #ffffff;
  justify-content: flex-end;
  align-self: flex-end;
  margin-right: 1%;
  margin-bottom: 1%;
}
.change-btn-container{
  background-color: #ffffff;
  height: 80px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.btn{
  margin-top: 8px;
  margin-bottom: 8px;
}

.thin-font {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-weight: 300;

}


.message-right .message-bubble {
  background-color: #d1e7dd;
  margin-left: auto;
}

.message-left .message-bubble {
  background-color: #f8d7da;
  margin-right: auto;
}
</style>
import {checkSession} from "./auth.js";
import {httpRequestGet} from "./http.js";

Vue.component('cross-icon', {
  template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
</svg>`
});

Vue.component('online-icon', {
  template: `<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="#26b54c">
  <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-6-3a2 2 0 11-4 0 2 2 0 014 0zm-2 4a5 5 0 00-4.546 2.916A5.986 5.986 0 0010 16a5.986 5.986 0 004.546-2.084A5 5 0 0010 11z" clip-rule="evenodd" />
</svg>`
});

new Vue({
  el: '#template',
  data: {
    sender: '',
    receiver: '',
    message: '',
    ws: null,
    messagesList: [],
    inboxes: [],
    friends: [],
    invalidMessage: false,
    isTyping: false,
    canMessage: false
  },
  beforeMount() {
    this.authenticate()
    this.setReceiver()

    this.setInboxes()
  },
  beforeDestroy() {
    this.ws.close();
  },
  methods: {
    authenticate() {
      checkSession().then((res) => {
        if (res == 401) {
          return window.location.href = '/cw1'
        }
        this.sender = res.username;
      }).then(() => {
        this.connectWebsocket()
      })
    },
    setReceiver(fu) {
      if (fu !== undefined) {
        // window.location.href = `?u=${fu}`
        this.receiver = fu;
        this.canMessage = true;
        this.getMessages();
        return;
      }

    },
    connectWebsocket() {
      this.ws = new WebSocket(`ws://localhost:8080/cw1/chat/${this.sender}`);
      this.ws.onopen = () => {

        /**
         * Send a login event and tell people I have just logged in.
         */
        this.ws.send(JSON.stringify({
          type: 'login',
          event: 'this is login event',
          username: this.sender
        }));

        /**
         * Send a connect event to the server to reply me with whoever is online
         */
        this.ws.send(JSON.stringify({
          type: 'connect',
          username: this.sender
        }))
      }

      this.ws.onmessage = (e) => {
        let data = JSON.parse(e.data)
        if (data.type === 'message') {
          console.log(data)
          this.inboxes.map(i => {
            if (i.user === data.sender) {
              i.messages.push(data);
              this.friends.map(f => {
                if (f.username === i.user) {
                  f.newMessage = true;
                }
              });
            }
          });

          Vue.nextTick(function () {
            const messagesArea = document.getElementById('messagesArea');
            messagesArea.scrollTop = messagesArea.scrollHeight;

            document.title = 'New message...'
          });
        }

        if (data.type === 'connect') {
          data.users.map(u => {
            let friend = {
              username: u.username,
              isOnline: true,
              isTyping: false,
              newMessage: false
            }
            if (!this.friends.find(f => f.username === u.username))
              this.friends.push(friend);
          });
          this.setInboxes();
        }

        if (data.type === 'login') {
          let friend = {
            username: data.username,
            isOnline: true,
            isTyping: false,
            newMessage: false
          }
          if (!this.friends.find(f => f.username === friend.username))
            this.friends.push(friend)
        }

        if (data.type === 'isTyping') {
          this.friends.forEach(f => {
            if (f.username === data.sender) {
              f.isTyping = data.state;
            }
          })
        }
      }

      this.ws.onclose = (e) => {
        this.sendMessage()
      }

      // this.ws.onclose
    },
    sendMessage(event) {
      if (event.key === 'Enter') {
        if (this.message === '') {
          return this.invalidMessage = true;
        }
        let msg = {
          'type': 'message',
          'sender': this.sender,
          'receiver': this.receiver,
          'message': this.message,
          'sentAt': new Date(),
        }
        this.ws.send(JSON.stringify(msg)); // send
        this.messagesList.push(msg) // add to the list

        this.message = ''; // reset message box

        setTimeout(() => {
          const messagesArea = document.getElementById('messagesArea');
          messagesArea.scrollTop = messagesArea.scrollHeight;
        }, 200)
      }

      if (event === 'close') {
        return this.ws.send(JSON.stringify({
          'type': 'close',
          'username': this.sender
        }));
      }

      this.invalidMessage = false;
      document.title = `Chatting with ${this.receiver}`
    },
    startTyping() {
      this.ws.send(JSON.stringify({
        'type': 'isTyping',
        'sender': this.sender,
        'receiver': this.receiver,
        'state': true
      }))
    },
    stopTyping() {
      this.ws.send(JSON.stringify({
        'type': 'isTyping',
        'sender': this.sender,
        'receiver': this.receiver,
        'state': false
      }))
    },
    setInboxes() {
      this.friends.forEach(f => {
        let i = {
          user: f.username,
          messages: []
        }
        if (!this.inboxes.find(inbox => inbox.user === i.user))
          this.inboxes.push(i)
      })
    },
    getMessages() {
      this.inboxes.map(i => {
        if (i.user === this.receiver) {
          this.messagesList = i.messages;
          this.friends.map(f => {
            if (f.username === i.user) {
              f.newMessage = false;
            }
          })
        }
      })
    },
    resetReceiver() {
      this.receiver = '';
      this.canMessage = false;
      this.messagesList = [];
    },
    async fetchMessages() {
      // fetch messages here
      let data = await httpRequestGet("messages/get");
      console.log(JSON.parse(data))
    }
  }
})
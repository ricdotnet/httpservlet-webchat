import {checkSession} from "./auth.js";

let messageBox = document.getElementById("message");
let messagesArea = document.getElementById("messagesArea");
// let usernameBox = document.getElementById('username');
// let receiverBox = document.getElementById('receiver');
let currentUser;
let receiver;

checkSession().then((data) => {
  if (data === '401') {
    localStorage.removeItem('token')
    return window.location.href = '/cw1'
  }
  webSocket(data.username);
  setReceiver();
})

function webSocket(username) {
  let ws = new WebSocket("ws://localhost:8080/cw1/chat");
  ws.onopen = () => {
    currentUser = username;
    ws.send(JSON.stringify({
      type: 'connect',
      username: username
    }))
  }

  ws.onmessage = (e) => {

    // let message = document.createElement('div');
    // message.classList.add('px-5', 'py-3', 'rounded-md');
    //
    // let data = JSON.parse(e.data)
    // if (data.sender === username) {
    //   message.classList.add('text-right', 'w-full', 'bg-blue-200');
    // } else {
    //   message.classList.add('text-left', 'w-full', 'bg-white');
    // }
    // message.innerText = ((data.sender) ? data.sender + ": " : "") + data.message;
    // messagesArea.appendChild(message);

    let data = JSON.parse(e.data);
    let origin;
    if (data.sender !== currentUser) {
      origin = 'remote';
    }
    let message = createMessageElement(origin, data.message, '11-11-21 22:30');
    messagesArea.appendChild(message);

    updateScroll();
  }

  window.send = function send() {
    let msg = messageBox.value;
    if (msg === "" || msg === undefined)
      return alert("please enter a message....");

    ws.send(JSON.stringify({
      'type': 'message',
      'sender': username,
      'receiver': receiver,
      'message': msg
    }));

    messagesArea.scrollTop = messagesArea.scrollHeight;
  }
}

function setReceiver() {
  let url = window.location.href.split('?u=');
  receiver = url[url.length - 1];
  document.getElementById('receiver').innerText = receiver;
}

messageBox.addEventListener('keydown', (e) => {
  if (e.key === 'Enter') {
    send();
    messageBox.value = '';
  }
});

let remoteTemplate = `
<div class="w-full flex justify-start">
        <div class="bg-gray-100 rounded px-5 py-2 my-2 text-gray-700 relative max-w-sm">
          <span class="block" id="remoteMessage"></span>
          <span class="block text-xs text-right">10:30pm</span>
        </div>
      </div>
`;

function createMessageElement(origin, message, time) {
  let messageEl = document.createElement('div');
  messageEl.classList.add('w-full', 'flex');
  let messageChild = document.createElement('div');
  messageChild.classList.add('bg-gray-100', 'rounded', 'px-5', 'py-2', 'my-2', 'text-gray-700', 'relative', 'max-w-sm');
  let messageContent = document.createElement('span');
  messageContent.classList.add('block');
  let messageTime = document.createElement('span');
  messageTime.classList.add('block', 'text-xs', 'text-right');

  messageContent.innerText = message;
  messageTime.innerText = time;

  if (origin === 'remote') {
    messageEl.classList.add('justify-start');
  } else {
    messageEl.classList.add('justify-end');
  }

  messageChild.appendChild(messageContent).appendChild(messageTime);
  messageEl.appendChild(messageChild);

  return messageEl;
}

function updateScroll() {
  let area = document.getElementById('messages');
  area.scrollTop = area.scrollHeight;
}
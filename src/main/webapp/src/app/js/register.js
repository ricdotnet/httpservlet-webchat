import {httpRequestPost} from "./http.js";
import {checkSession} from "./auth.js";

checkSession().then(data => {
  if (data != '401') {
    localStorage.removeItem('token')
    return window.location.href = '/cw1/chat'
  }

  // redirect();
});

document.title = "Register"

let responseBox = document.getElementById('message');

window.doRegister = async function doRegister(event) {
  if (event) {
    event.preventDefault();
  }
  let res = await httpRequestPost('auth/register', 'POST', prepareBody())
  let data = await res.json();

  console.log(data);

  if (!data.token) {
    let msg = data.message.charAt(0).toUpperCase() + data.message.slice(1);
    responseBox.innerText = msg + '. Please try again.';
    responseBox.classList.remove('hidden');
    responseBox.classList.add('bg-red-400', 'border-red-800');
    removeToast();
  } else {
    localStorage.setItem('token', data.token);
    responseBox.innerText = 'Logged in. Redirecting...';
    responseBox.classList.remove('hidden');
    responseBox.classList.add('bg-green-400', 'border-green-800');
    removeToast();
    redirect();
  }
}

function removeToast() {
  setTimeout(() => {
    responseBox.classList.add('hidden');
  }, 5000)
}

function redirect() {
  setTimeout(() => {
    window.location.replace('/cw1/chat');
  }, 3000)
}

function prepareBody() {
  let username = document.getElementById('username').value;
  let password = document.getElementById('password').value;
  let email = document.getElementById('email').value;

  return {
    username: username,
    password: password,
    email: email
  }
}
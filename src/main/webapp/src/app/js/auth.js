import {httpRequestPost} from "./http.js";

/**
 * Check if the user is logged in with a token.
 */
export async function checkSession() {
  let token = localStorage.getItem('token');
  if (token) {
    let request = await httpRequestPost(`auth/token`, 'POST', {
      token: token
    })
    let data = await request.json();
    if (data.code !== '401')
      return data;
  }

  return '401';
}
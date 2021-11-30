/**
 * Main functions file
 *
 * @author Ricardo Rocha
 */

import {httpRequestPost} from './http.js';

window.init = async function init() {
  console.log('running this first yay')
}

window.doRegister = async function doRegister() {
  let res = await httpRequestPost('auth/register', 'POST')
  let data = await res.json();
  console.log(data)
}
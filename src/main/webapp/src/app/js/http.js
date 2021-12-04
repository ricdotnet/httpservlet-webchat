/**
 * Http functions file
 *
 * @author Ricardo Rocha
 */

let apiUrl = 'http://localhost:8080/cw1/';

// maybe refactor this and make it persist the post method
export async function httpRequestPost(endpoint, method, body) {
  return await fetch(apiUrl + endpoint, {
    method: method,
    mode: 'cors',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  })
}

export async function httpRequestGet(endpoint) {
  return await fetch(apiUrl + endpoint, {
    method: 'GET',
    mode: 'cors',
    headers: {
      'Accept': '*/*',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  })
}
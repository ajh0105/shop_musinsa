import axios from 'axios'

export const api = axios.create({
  baseURL: '/v1/api',
  withCredentials: true,
  timeout: 3000
})
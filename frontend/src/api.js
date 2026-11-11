import axios from 'axios';
export const api = axios.create({ baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api' });
api.interceptors.request.use(config => { const token = localStorage.getItem('placement_token'); if (token) config.headers.Authorization = `Bearer ${token}`; return config; });
api.interceptors.response.use(r => r, error => { if (error.response?.status === 401) { localStorage.removeItem('placement_token'); localStorage.removeItem('placement_user'); } return Promise.reject(error); });
export const messageOf = e => e.response?.data?.message || e.response?.data?.detail || e.response?.data?.error || e.message || 'Something went wrong. Please try again.';

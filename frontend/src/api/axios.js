import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
})

// Attach the stored token to every request.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// If the server says the token is invalid/expired, clear the session.
api.interceptors.response.use(
  (res) => res,
  (error) => {
    const url = error.config?.url || ''
    if (error.response?.status === 401 && !url.includes('/auth/login')) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      if (window.location.pathname !== '/login') window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export function errorMessage(error, fallback = 'Something went wrong') {
  const data = error.response?.data
  if (data?.fieldErrors) return Object.values(data.fieldErrors).join('. ')
  return data?.message || (error.request ? 'Cannot reach the server' : fallback)
}

export default api

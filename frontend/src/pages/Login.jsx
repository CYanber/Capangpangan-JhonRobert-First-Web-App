import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { errorMessage } from '../api/axios'

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const info = location.state?.message

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const onSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(form.username, form.password)
      navigate('/dashboard')
    } catch (err) {
      setError(errorMessage(err, 'Login failed'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card auth-card">
      <h2>Log in</h2>
      {info && <div className="alert alert-success">{info}</div>}
      {error && <div className="alert alert-error">{error}</div>}
      <form onSubmit={onSubmit}>
        <label>Username
          <input name="username" value={form.username} onChange={onChange} required />
        </label>
        <label>Password
          <input type="password" name="password" value={form.password} onChange={onChange} required />
        </label>
        <button className="btn" disabled={loading}>{loading ? 'Signing in...' : 'Login'}</button>
      </form>
      <p className="muted">No account yet? <Link to="/register">Register</Link></p>
    </div>
  )
}

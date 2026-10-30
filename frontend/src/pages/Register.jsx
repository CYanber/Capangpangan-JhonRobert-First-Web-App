import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import api, { errorMessage } from '../api/axios'

export default function Register() {
  const [form, setForm] = useState({ username: '', email: '', fullName: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const onSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await api.post('/auth/register', form)
      navigate('/login', { state: { message: 'Registration successful! Please log in.' } })
    } catch (err) {
      setError(errorMessage(err, 'Registration failed'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card auth-card">
      <h2>Create an account</h2>
      {error && <div className="alert alert-error">{error}</div>}
      <form onSubmit={onSubmit}>
        <label>Full name
          <input name="fullName" value={form.fullName} onChange={onChange} required />
        </label>
        <label>Username
          <input name="username" value={form.username} onChange={onChange} required minLength={3} />
        </label>
        <label>Email
          <input type="email" name="email" value={form.email} onChange={onChange} required />
        </label>
        <label>Password
          <input type="password" name="password" value={form.password} onChange={onChange} required minLength={6} />
        </label>
        <button className="btn" disabled={loading}>{loading ? 'Creating...' : 'Register'}</button>
      </form>
      <p className="muted">Already have an account? <Link to="/login">Log in</Link></p>
    </div>
  )
}

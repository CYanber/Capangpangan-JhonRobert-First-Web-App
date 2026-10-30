import { useEffect, useState } from 'react'
import api, { errorMessage } from '../api/axios'
import { useAuth } from '../context/AuthContext'

export default function Profile() {
  const { user, updateUser } = useAuth()
  const [fullName, setFullName] = useState(user?.fullName || '')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    api.get('/users/me').then((res) => setFullName(res.data.fullName)).catch(() => {})
  }, [])

  const onSubmit = async (e) => {
    e.preventDefault()
    setMessage('')
    setError('')
    setLoading(true)
    try {
      const { data } = await api.put('/users/me', { fullName })
      updateUser(data)
      setMessage('Profile updated successfully.')
    } catch (err) {
      setError(errorMessage(err, 'Update failed'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card auth-card">
      <h2>Edit my profile</h2>
      {message && <div className="alert alert-success">{message}</div>}
      {error && <div className="alert alert-error">{error}</div>}
      <form onSubmit={onSubmit}>
        <label>Username
          <input value={user?.username || ''} disabled />
        </label>
        <label>Email
          <input value={user?.email || ''} disabled />
        </label>
        <label>Full name
          <input value={fullName} onChange={(e) => setFullName(e.target.value)} required maxLength={100} />
        </label>
        <button className="btn" disabled={loading}>{loading ? 'Saving...' : 'Save changes'}</button>
      </form>
    </div>
  )
}

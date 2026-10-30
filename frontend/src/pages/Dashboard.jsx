import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api, { errorMessage } from '../api/axios'
import { useAuth } from '../context/AuthContext'

export default function Dashboard() {
  const { updateUser, isAdmin } = useAuth()
  const [profile, setProfile] = useState(null)
  const [welcome, setWelcome] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([api.get('/users/me'), api.get('/users/dashboard')])
      .then(([me, dash]) => {
        setProfile(me.data)
        updateUser(me.data)
        setWelcome(dash.data.message)
      })
      .catch((err) => setError(errorMessage(err, 'Failed to load dashboard')))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  if (error) return <div className="alert alert-error">{error}</div>
  if (!profile) return <p>Loading...</p>

  return (
    <div className="card">
      <h2>Dashboard</h2>
      <p className="welcome">{welcome}</p>
      <dl className="details">
        <dt>Full name</dt><dd>{profile.fullName}</dd>
        <dt>Username</dt><dd>{profile.username}</dd>
        <dt>Email</dt><dd>{profile.email}</dd>
        <dt>Role</dt><dd><span className={`badge badge-${profile.role.toLowerCase()}`}>{profile.role}</span></dd>
        <dt>Member since</dt><dd>{new Date(profile.createdAt).toLocaleString()}</dd>
      </dl>
      <div className="actions">
        <Link className="btn" to="/profile">Edit my profile</Link>
        {isAdmin && <Link className="btn btn-secondary" to="/admin/users">View all users</Link>}
      </div>
    </div>
  )
}

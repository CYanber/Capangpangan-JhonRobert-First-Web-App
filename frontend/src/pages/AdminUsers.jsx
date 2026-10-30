import { useEffect, useState } from 'react'
import api, { errorMessage } from '../api/axios'

export default function AdminUsers() {
  const [users, setUsers] = useState([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/admin/users')
      .then((res) => setUsers(res.data))
      .catch((err) => setError(errorMessage(err, 'Failed to load users')))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="card">
      <h2>All registered users</h2>
      {error && <div className="alert alert-error">{error}</div>}
      {loading ? <p>Loading...</p> : (
        <table className="table">
          <thead>
            <tr><th>ID</th><th>Full name</th><th>Username</th><th>Email</th><th>Role</th><th>Created</th></tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.fullName}</td>
                <td>{u.username}</td>
                <td>{u.email}</td>
                <td><span className={`badge badge-${u.role.toLowerCase()}`}>{u.role}</span></td>
                <td>{new Date(u.createdAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

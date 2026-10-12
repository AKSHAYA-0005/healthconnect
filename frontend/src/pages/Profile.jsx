import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import DisclaimerBox from '../components/DisclaimerBox';

const Profile = () => {
  const { user, updateUser } = useAuth();
  const [fullName, setFullName] = useState(user?.fullName || '');
  const [phone, setPhone] = useState(user?.phone || '');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    try {
      const res = await api.put('/users/profile', { fullName, phone });
      updateUser(res.data);
      setMessage('Profile updated successfully!');
    } catch (err) {
      setMessage('Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '550px', margin: '2rem auto' }}>
      <div className="glass-card" style={{ padding: '2.5rem' }}>
        <h2 style={{ fontSize: '1.6rem', fontWeight: '800', marginBottom: '0.4rem' }}>
          👤 User Profile Management
        </h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '1.5rem' }}>
          Manage your personal details and contact preferences
        </p>

        <DisclaimerBox />

        {message && (
          <div style={{ background: 'rgba(16, 185, 129, 0.15)', border: '1px solid #10b981', color: '#6ee7b7', padding: '0.8rem', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.88rem' }}>
            {message}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Email Address (Read-only)</label>
            <input type="email" className="form-input" value={user?.email || ''} disabled style={{ opacity: 0.7 }} />
          </div>

          <div className="form-group">
            <label className="form-label">Full Name</label>
            <input
              type="text"
              className="form-input"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Phone Number</label>
            <input
              type="tel"
              className="form-input"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label className="form-label">System Role</label>
            <div className="badge badge-low" style={{ fontSize: '0.9rem', display: 'inline-block' }}>
              {user?.role?.replace('ROLE_', '')}
            </div>
          </div>

          <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', marginTop: '1rem' }} disabled={loading}>
            {loading ? 'Saving Changes...' : 'Save Profile Changes'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Profile;

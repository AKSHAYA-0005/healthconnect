import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import DisclaimerBox from '../components/DisclaimerBox';

const Register = () => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [role, setRole] = useState('ROLE_PATIENT');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const user = await register({
        fullName,
        email,
        password,
        phone,
        role
      });
      if (user.role === 'ROLE_ADMIN') {
        navigate('/admin');
      } else if (user.role === 'ROLE_DOCTOR') {
        navigate('/doctor');
      } else {
        navigate('/patient');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '500px', margin: '2rem auto' }}>
      <div className="glass-card" style={{ padding: '2.5rem' }}>
        <h2 style={{ textAlign: 'center', marginBottom: '0.5rem', fontWeight: '800' }}>
          Create Account
        </h2>
        <p style={{ textAlign: 'center', color: 'var(--text-muted)', marginBottom: '1.5rem', fontSize: '0.9rem' }}>
          Register to access AI Triage & Medical Services
        </p>

        {/* MANDATORY HEALTHCARE SAFETY DISCLAIMER ON REGISTER PAGE */}
        <DisclaimerBox />

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.15)', border: '1px solid #ef4444', color: '#fca5a5', padding: '0.8rem', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.88rem' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Full Name</label>
            <input
              type="text"
              className="form-input"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              placeholder="e.g. Jane Doe"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Email Address</label>
            <input
              type="email"
              className="form-input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="jane@example.com"
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
              placeholder="+1 555-0199"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Password (Min 6 characters)</label>
            <input
              type="password"
              className="form-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              minLength={6}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Account Role</label>
            <select className="form-select" value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="ROLE_PATIENT">Patient Account</option>
              <option value="ROLE_DOCTOR">Doctor Account</option>
            </select>
          </div>

          <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', marginTop: '1rem' }} disabled={loading}>
            {loading ? 'Creating Account...' : 'Register Now'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.88rem', color: 'var(--text-muted)' }}>
          Already have an account? <Link to="/login" style={{ color: 'var(--primary)', fontWeight: '600' }}>Sign In</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;

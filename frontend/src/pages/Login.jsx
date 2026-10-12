import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import DisclaimerBox from '../components/DisclaimerBox';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const loggedUser = await login(email, password);
      if (loggedUser.role === 'ROLE_ADMIN') {
        navigate('/admin');
      } else if (loggedUser.role === 'ROLE_DOCTOR') {
        navigate('/doctor');
      } else {
        navigate('/patient');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid email or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '450px', margin: '3rem auto' }}>
      <div className="glass-card" style={{ padding: '2.5rem' }}>
        <h2 style={{ textAlign: 'center', marginBottom: '0.5rem', fontWeight: '800' }}>
          Welcome Back
        </h2>
        <p style={{ textAlign: 'center', color: 'var(--text-muted)', marginBottom: '1.5rem', fontSize: '0.9rem' }}>
          Sign in to access your HEALTHCONNECT portal
        </p>

        <DisclaimerBox />

        {error && (
          <div style={{ background: 'rgba(239, 68, 68, 0.15)', border: '1px solid #ef4444', color: '#fca5a5', padding: '0.8rem', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.88rem' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Email Address</label>
            <input
              type="email"
              className="form-input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="e.g. patient@example.com"
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
            />
          </div>

          <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', marginTop: '1rem' }} disabled={loading}>
            {loading ? 'Signing In...' : 'Sign In'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.88rem', color: 'var(--text-muted)' }}>
          Don't have an account? <Link to="/register" style={{ color: 'var(--primary)', fontWeight: '600' }}>Register here</Link>
        </div>

        <div style={{ marginTop: '1.5rem', paddingTop: '1rem', borderTop: '1px solid var(--card-border)', fontSize: '0.8rem', color: 'var(--text-subtle)' }}>
          <strong>Demo Credentials:</strong>
          <div>Admin: <code>admin@healthconnect.com</code> / <code>admin123</code></div>
          <div>Doctor: <code>doctor.jenkins@healthconnect.com</code> / <code>doc123</code></div>
        </div>
      </div>
    </div>
  );
};

export default Login;

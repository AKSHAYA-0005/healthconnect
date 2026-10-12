import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = ({ onOpenChatbot }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path) => location.pathname === path ? 'active' : '';

  return (
    <nav className="navbar">
      <Link to="/" className="nav-brand" id="brand-link">
        <span>🏥</span> HEALTHCONNECT
      </Link>

      <ul className="nav-links">
        {user ? (
          <>
            {user.role === 'ROLE_PATIENT' && (
              <>
                <li>
                  <Link to="/patient" className={`nav-link ${isActive('/patient')}`}>
                    Dashboard
                  </Link>
                </li>
                <li>
                  <Link to="/assessment" className={`nav-link ${isActive('/assessment')}`}>
                    Triage Form
                  </Link>
                </li>
                <li>
                  <button
                    onClick={onOpenChatbot}
                    className="nav-link"
                    style={{ background: 'none', border: 'none', color: '#818cf8', fontWeight: '700' }}
                  >
                    💬 AI Assistant
                  </button>
                </li>
                <li>
                  <Link to="/history" className={`nav-link ${isActive('/history')}`}>
                    My History
                  </Link>
                </li>
                <li>
                  <Link to="/appointments" className={`nav-link ${isActive('/appointments')}`}>
                    Appointments
                  </Link>
                </li>
              </>
            )}

            {user.role === 'ROLE_DOCTOR' && (
              <>
                <li>
                  <Link to="/doctor" className={`nav-link ${isActive('/doctor')}`}>
                    Doctor Portal
                  </Link>
                </li>
              </>
            )}

            {user.role === 'ROLE_ADMIN' && (
              <>
                <li>
                  <Link to="/admin" className={`nav-link ${isActive('/admin')}`}>
                    Admin Console
                  </Link>
                </li>
              </>
            )}

            <li>
              <Link to="/departments" className={`nav-link ${isActive('/departments')}`}>
                Departments
              </Link>
            </li>

            <div className="nav-user">
              <Link to="/profile" className="user-badge" id="user-profile-badge">
                👤 {user.fullName} ({user.role.replace('ROLE_', '')})
              </Link>
              <button onClick={handleLogout} className="btn-secondary" style={{ padding: '0.4rem 0.9rem', fontSize: '0.85rem' }}>
                Logout
              </button>
            </div>
          </>
        ) : (
          <>
            <li>
              <Link to="/departments" className={`nav-link ${isActive('/departments')}`}>
                Departments
              </Link>
            </li>
            <li>
              <Link to="/login" className="btn-secondary">
                Sign In
              </Link>
            </li>
            <li>
              <Link to="/register" className="btn-primary">
                Register
              </Link>
            </li>
          </>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;

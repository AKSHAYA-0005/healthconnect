import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import DisclaimerBox from '../components/DisclaimerBox';

const PatientDashboard = ({ onOpenChatbot }) => {
  const { user } = useAuth();

  return (
    <div>
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '1.8rem', fontWeight: '800', marginBottom: '0.5rem' }}>
          Welcome, {user?.fullName || 'Patient'}! 👋
        </h1>
        <p style={{ color: 'var(--text-muted)' }}>
          Assess your symptoms, get department recommendations, and connect with expert hospital specialists.
        </p>

        <DisclaimerBox />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1.5rem' }}>
        <div className="glass-card" style={{ padding: '1.8rem' }}>
          <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>📋</div>
          <h3 style={{ fontSize: '1.2rem', fontWeight: '700', marginBottom: '0.5rem' }}>
            Symptom Assessment Form
          </h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '1.5rem' }}>
            Fill out a quick guided form with your age, duration, and red-flag symptoms to receive instant triage.
          </p>
          <Link to="/assessment" className="btn-primary" style={{ width: '100%', justifyContent: 'center' }}>
            Start Triage Assessment
          </Link>
        </div>

        <div className="glass-card" style={{ padding: '1.8rem' }}>
          <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>💬</div>
          <h3 style={{ fontSize: '1.2rem', fontWeight: '700', marginBottom: '0.5rem' }}>
            Interactive AI Chatbot
          </h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '1.5rem' }}>
            Chat naturally with our AI assistant for step-by-step triage guidance and emergency checks.
          </p>
          <button onClick={onOpenChatbot} className="btn-primary" style={{ width: '100%', justifyContent: 'center', background: 'linear-gradient(135deg, #06b6d4 0%, #0284c7 100%)' }}>
            Launch Chatbot
          </button>
        </div>

        <div className="glass-card" style={{ padding: '1.8rem' }}>
          <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>📅</div>
          <h3 style={{ fontSize: '1.2rem', fontWeight: '700', marginBottom: '0.5rem' }}>
            Doctor Appointments
          </h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '1.5rem' }}>
            Book appointments with recommended hospital departments and manage your upcoming schedule.
          </p>
          <Link to="/appointments" className="btn-secondary" style={{ width: '100%', display: 'inline-block', textAlign: 'center' }}>
            View Appointments
          </Link>
        </div>

        <div className="glass-card" style={{ padding: '1.8rem' }}>
          <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>📜</div>
          <h3 style={{ fontSize: '1.2rem', fontWeight: '700', marginBottom: '0.5rem' }}>
            Triage History
          </h3>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '1.5rem' }}>
            Review past assessments, urgency levels, and explanations for previous health checkups.
          </p>
          <Link to="/history" className="btn-secondary" style={{ width: '100%', display: 'inline-block', textAlign: 'center' }}>
            View History
          </Link>
        </div>
      </div>
    </div>
  );
};

export default PatientDashboard;

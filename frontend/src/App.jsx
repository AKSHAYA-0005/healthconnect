import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ChatbotModal from './components/ChatbotModal';

import Login from './pages/Login';
import Register from './pages/Register';
import PatientDashboard from './pages/PatientDashboard';
import SymptomAssessment from './pages/SymptomAssessment';
import AssessmentHistory from './pages/AssessmentHistory';
import Departments from './pages/Departments';
import MyAppointments from './pages/MyAppointments';
import DoctorDashboard from './pages/DoctorDashboard';
import AdminDashboard from './pages/AdminDashboard';
import Profile from './pages/Profile';
import './App.css';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user, loading } = useAuth();

  if (loading) return <div style={{ padding: '3rem', textAlign: 'center' }}>Loading session...</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" replace />;
  }

  return children;
};

const RootRedirect = () => {
  const { user, loading } = useAuth();
  if (loading) return <div style={{ padding: '3rem', textAlign: 'center' }}>Loading...</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (user.role === 'ROLE_ADMIN') return <Navigate to="/admin" replace />;
  if (user.role === 'ROLE_DOCTOR') return <Navigate to="/doctor" replace />;
  return <Navigate to="/patient" replace />;
};

function AppContent() {
  const [isChatbotOpen, setIsChatbotOpen] = useState(false);

  return (
    <div className="app-container">
      <Navbar onOpenChatbot={() => setIsChatbotOpen(true)} />

      <main className="main-content">
        <Routes>
          <Route path="/" element={<RootRedirect />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/departments" element={<Departments />} />

          {/* Patient Routes */}
          <Route
            path="/patient"
            element={
              <ProtectedRoute allowedRoles={['ROLE_PATIENT']}>
                <PatientDashboard onOpenChatbot={() => setIsChatbotOpen(true)} />
              </ProtectedRoute>
            }
          />
          <Route
            path="/assessment"
            element={
              <ProtectedRoute allowedRoles={['ROLE_PATIENT']}>
                <SymptomAssessment />
              </ProtectedRoute>
            }
          />
          <Route
            path="/history"
            element={
              <ProtectedRoute allowedRoles={['ROLE_PATIENT']}>
                <AssessmentHistory />
              </ProtectedRoute>
            }
          />
          <Route
            path="/appointments"
            element={
              <ProtectedRoute allowedRoles={['ROLE_PATIENT']}>
                <MyAppointments />
              </ProtectedRoute>
            }
          />

          {/* Doctor Routes */}
          <Route
            path="/doctor"
            element={
              <ProtectedRoute allowedRoles={['ROLE_DOCTOR', 'ROLE_ADMIN']}>
                <DoctorDashboard />
              </ProtectedRoute>
            }
          />

          {/* Admin Routes */}
          <Route
            path="/admin"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          />

          {/* Shared Profile Route */}
          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            }
          />
        </Routes>
      </main>

      <ChatbotModal isOpen={isChatbotOpen} onClose={() => setIsChatbotOpen(false)} />
      <Footer />
    </div>
  );
}

export default function App() {
  return (
    <Router>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Router>
  );
}

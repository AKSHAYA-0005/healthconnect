import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import api from '../services/api';
import DisclaimerBox from '../components/DisclaimerBox';

const MyAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showBookModal, setShowBookModal] = useState(false);
  const [error, setError] = useState('');

  const location = useLocation();

  const [bookingForm, setBookingForm] = useState({
    doctorId: '',
    appointmentDate: '',
    reason: '',
    assessmentId: location.state?.assessmentId || null
  });

  useEffect(() => {
    fetchAppointments();
    fetchDoctors();
  }, []);

  useEffect(() => {
    if (location.state?.departmentId || location.state?.assessmentId) {
      setShowBookModal(true);
    }
  }, [location.state]);

  const fetchAppointments = async () => {
    try {
      const res = await api.get('/appointments/my');
      setAppointments(res.data);
    } catch (err) {
      console.error("Failed to fetch appointments", err);
    } finally {
      setLoading(false);
    }
  };

  const fetchDoctors = async () => {
    try {
      const res = await api.get('/doctors');
      setDoctors(res.data);
      if (res.data.length > 0 && !bookingForm.doctorId) {
        setBookingForm(prev => ({ ...prev, doctorId: res.data[0].id }));
      }
    } catch (err) {
      console.error("Failed to fetch doctors", err);
    }
  };

  const handleBookSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await api.post('/appointments', {
        doctorId: parseInt(bookingForm.doctorId),
        appointmentDate: bookingForm.appointmentDate,
        reason: bookingForm.reason,
        assessmentId: bookingForm.assessmentId
      });
      setShowBookModal(false);
      fetchAppointments();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to book appointment');
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm("Are you sure you want to cancel this appointment?")) return;
    try {
      await api.put(`/appointments/${id}/status`, { status: 'CANCELLED' });
      fetchAppointments();
    } catch (err) {
      alert("Failed to cancel appointment");
    }
  };

  return (
    <div>
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h2 style={{ fontSize: '1.6rem', fontWeight: '800' }}>📅 My Hospital Appointments</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.92rem' }}>
            Book, view, reschedule, or cancel your consultations with hospital doctors.
          </p>
        </div>
        <button onClick={() => setShowBookModal(true)} className="btn-primary">
          + Book New Appointment
        </button>
      </div>

      <DisclaimerBox />

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem' }}>Loading appointments...</div>
      ) : appointments.length === 0 ? (
        <div className="glass-card" style={{ padding: '3rem', textAlign: 'center' }}>
          <p style={{ color: 'var(--text-muted)' }}>You have no appointments scheduled.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gap: '1rem' }}>
          {appointments.map((app) => (
            <div key={app.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.8rem', marginBottom: '0.4rem' }}>
                  <span className={`badge ${app.status === 'CONFIRMED' ? 'badge-low' : app.status === 'CANCELLED' ? 'badge-high' : 'badge-medium'}`}>
                    {app.status}
                  </span>
                  <strong style={{ fontSize: '1.1rem' }}>{app.doctor?.user?.fullName || 'Doctor'}</strong>
                  <span style={{ color: 'var(--primary)', fontSize: '0.88rem', fontWeight: '600' }}>
                    ({app.department?.name || 'Department'})
                  </span>
                </div>
                <div style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>
                  🗓️ <strong>{new Date(app.appointmentDate).toLocaleString()}</strong>
                </div>
                <p style={{ fontSize: '0.88rem', color: 'var(--text-subtle)', marginTop: '0.3rem' }}>
                  Reason: {app.reason || 'General Triage Consultation'}
                </p>
              </div>

              {app.status !== 'CANCELLED' && app.status !== 'COMPLETED' && (
                <button onClick={() => handleCancel(app.id)} className="btn-danger" style={{ padding: '0.4rem 0.9rem', fontSize: '0.85rem' }}>
                  Cancel Appointment
                </button>
              )}
            </div>
          ))}
        </div>
      )}

      {showBookModal && (
        <div className="modal-overlay" style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(15, 23, 42, 0.8)', backdropFilter: 'blur(8px)', zIndex: 1000, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1rem' }}>
          <div className="glass-card" style={{ maxWidth: '550px', width: '100%', padding: '2rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.2rem' }}>
              <h3 style={{ fontSize: '1.3rem', fontWeight: '800' }}>Book Doctor Appointment</h3>
              <button onClick={() => setShowBookModal(false)} className="btn-secondary">✕</button>
            </div>

            {error && (
              <div style={{ background: 'rgba(239, 68, 68, 0.15)', border: '1px solid #ef4444', color: '#fca5a5', padding: '0.8rem', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.88rem' }}>
                {error}
              </div>
            )}

            <form onSubmit={handleBookSubmit}>
              <div className="form-group">
                <label className="form-label">Select Specialist Doctor</label>
                <select
                  className="form-select"
                  value={bookingForm.doctorId}
                  onChange={(e) => setBookingForm({ ...bookingForm, doctorId: e.target.value })}
                  required
                >
                  {doctors.map((doc) => (
                    <option key={doc.id} value={doc.id}>
                      {doc.user?.fullName} - {doc.department?.name} ({doc.qualification})
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label className="form-label">Appointment Date & Time</label>
                <input
                  type="datetime-local"
                  className="form-input"
                  value={bookingForm.appointmentDate}
                  onChange={(e) => setBookingForm({ ...bookingForm, appointmentDate: e.target.value })}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Reason for Visit</label>
                <textarea
                  className="form-textarea"
                  rows={3}
                  value={bookingForm.reason}
                  onChange={(e) => setBookingForm({ ...bookingForm, reason: e.target.value })}
                  placeholder="Describe your primary symptoms or attach triage notes..."
                  required
                />
              </div>

              <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', marginTop: '1rem' }}>
                Confirm Appointment Request
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyAppointments;

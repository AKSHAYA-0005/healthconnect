import React, { useState, useEffect } from 'react';
import api from '../services/api';
import DisclaimerBox from '../components/DisclaimerBox';

const DoctorDashboard = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedPatientAssessment, setSelectedPatientAssessment] = useState(null);
  const [assessmentLoading, setAssessmentLoading] = useState(false);
  const [assessmentError, setAssessmentError] = useState('');

  useEffect(() => {
    fetchDoctorAppointments();
  }, []);

  const fetchDoctorAppointments = async () => {
    try {
      const res = await api.get('/appointments/doctor');
      setAppointments(res.data);
    } catch (err) {
      console.error("Failed to load doctor appointments", err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateStatus = async (id, status) => {
    try {
      await api.put(`/appointments/${id}/status`, { status });
      fetchDoctorAppointments();
    } catch (err) {
      alert("Failed to update appointment status");
    }
  };

  const fetchPatientAssessment = async (assessmentId) => {
    setAssessmentLoading(true);
    setAssessmentError('');
    try {
      const res = await api.get(`/assessments/${assessmentId}`);
      setSelectedPatientAssessment(res.data);
    } catch (err) {
      setAssessmentError(err.response?.data?.message || 'Unauthorized or failed to view patient triage history');
    } finally {
      setAssessmentLoading(false);
    }
  };

  return (
    <div>
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h2 style={{ fontSize: '1.6rem', fontWeight: '800' }}>👨‍⚕️ Doctor Clinical Portal</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.92rem' }}>
          Manage your patient consultation schedule, review authorized patient symptom assessments, and update appointment outcomes.
        </p>

        <DisclaimerBox />
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem' }}>Loading appointment schedule...</div>
      ) : appointments.length === 0 ? (
        <div className="glass-card" style={{ padding: '3rem', textAlign: 'center' }}>
          <p style={{ color: 'var(--text-muted)' }}>No appointments currently scheduled for you.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gap: '1rem' }}>
          {appointments.map((app) => (
            <div key={app.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.8rem', marginBottom: '0.4rem' }}>
                  <span className={`badge ${app.status === 'CONFIRMED' ? 'badge-low' : app.status === 'COMPLETED' ? 'badge-low' : app.status === 'CANCELLED' ? 'badge-high' : 'badge-medium'}`}>
                    {app.status}
                  </span>
                  <strong style={{ fontSize: '1.15rem' }}>Patient: {app.patient?.fullName}</strong>
                  <span style={{ fontSize: '0.85rem', color: 'var(--text-subtle)' }}>({app.patient?.phone || app.patient?.email})</span>
                </div>

                <div style={{ fontSize: '0.9rem', color: 'var(--text-muted)', margin: '0.3rem 0' }}>
                  🗓️ Scheduled: <strong>{new Date(app.appointmentDate).toLocaleString()}</strong>
                </div>

                <p style={{ fontSize: '0.88rem', color: 'var(--text-main)', marginTop: '0.3rem' }}>
                  Chief Complaint: "{app.reason || 'N/A'}"
                </p>

                {app.assessmentId && (
                  <button
                    onClick={() => fetchPatientAssessment(app.assessmentId)}
                    className="btn-secondary"
                    style={{ marginTop: '0.6rem', padding: '0.3rem 0.8rem', fontSize: '0.82rem', color: '#a5b4fc' }}
                  >
                    🔍 View Patient Pre-Triage History
                  </button>
                )}
              </div>

              <div style={{ display: 'flex', gap: '0.5rem' }}>
                {app.status === 'REQUESTED' && (
                  <button onClick={() => handleUpdateStatus(app.id, 'CONFIRMED')} className="btn-primary" style={{ padding: '0.4rem 0.9rem', fontSize: '0.85rem' }}>
                    Confirm
                  </button>
                )}
                {app.status === 'CONFIRMED' && (
                  <button onClick={() => handleUpdateStatus(app.id, 'COMPLETED')} className="btn-primary" style={{ padding: '0.4rem 0.9rem', fontSize: '0.85rem', background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)' }}>
                    Mark Completed
                  </button>
                )}
                {app.status !== 'CANCELLED' && app.status !== 'COMPLETED' && (
                  <button onClick={() => handleUpdateStatus(app.id, 'CANCELLED')} className="btn-secondary" style={{ padding: '0.4rem 0.9rem', fontSize: '0.85rem', color: '#fca5a5' }}>
                    Cancel
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {selectedPatientAssessment && (
        <div className="modal-overlay" style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(15, 23, 42, 0.8)', backdropFilter: 'blur(8px)', zIndex: 1000, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1rem' }}>
          <div className="glass-card" style={{ maxWidth: '600px', width: '100%', padding: '2rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <h3 style={{ fontSize: '1.3rem', fontWeight: '800' }}>Patient Authorized Triage Record</h3>
              <button onClick={() => setSelectedPatientAssessment(null)} className="btn-secondary">✕</button>
            </div>

            <DisclaimerBox text={selectedPatientAssessment.disclaimer} />

            <div style={{ margin: '1rem 0', fontSize: '0.9rem' }}>
              <div><strong>Urgency Level:</strong> <span className={`badge badge-${selectedPatientAssessment.urgencyLevel.toLowerCase()}`}>{selectedPatientAssessment.urgencyLevel}</span></div>
              <div style={{ marginTop: '0.4rem' }}><strong>Target Dept:</strong> {selectedPatientAssessment.recommendedDepartment}</div>
              <div style={{ marginTop: '0.4rem' }}><strong>Symptoms:</strong> {selectedPatientAssessment.symptomsText}</div>
              <div style={{ marginTop: '0.4rem' }}><strong>Age / Gender:</strong> {selectedPatientAssessment.age} / {selectedPatientAssessment.gender}</div>
              <div style={{ marginTop: '0.4rem' }}><strong>Self Severity:</strong> {selectedPatientAssessment.severityScale}/10</div>
              <div style={{ marginTop: '0.4rem' }}><strong>Co-morbidities:</strong> {selectedPatientAssessment.preExistingConditions || 'None'}</div>
            </div>

            <div style={{ marginTop: '1.5rem', textAlign: 'right' }}>
              <button onClick={() => setSelectedPatientAssessment(null)} className="btn-primary">
                Done
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DoctorDashboard;

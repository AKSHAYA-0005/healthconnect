import React, { useState, useEffect } from 'react';
import api from '../services/api';
import DisclaimerBox from '../components/DisclaimerBox';

const AssessmentHistory = () => {
  const [assessments, setAssessments] = useState([]);
  const [selectedAssessment, setSelectedAssessment] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchHistory();
  }, []);

  const fetchHistory = async () => {
    try {
      const res = await api.get('/assessments');
      setAssessments(res.data);
    } catch (err) {
      console.error("Failed to load assessment history", err);
    } finally {
      setLoading(false);
    }
  };

  const parseReasons = (reasonsJson) => {
    try {
      return JSON.parse(reasonsJson);
    } catch (e) {
      return [];
    }
  };

  return (
    <div>
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h2 style={{ fontSize: '1.6rem', fontWeight: '800' }}>📜 My Triage Assessment History</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.92rem' }}>
          View your previously evaluated symptoms, recommendations, and clinical decision factors.
        </p>

        <DisclaimerBox />
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem' }}>Loading assessment history...</div>
      ) : assessments.length === 0 ? (
        <div className="glass-card" style={{ padding: '3rem', textAlign: 'center' }}>
          <p style={{ color: 'var(--text-muted)' }}>No previous assessments found. Complete your first triage form to see history here.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gap: '1rem' }}>
          {assessments.map((item) => (
            <div key={item.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.8rem', marginBottom: '0.4rem' }}>
                  <span className={`badge badge-${item.urgencyLevel.toLowerCase()}`}>{item.urgencyLevel}</span>
                  <strong style={{ fontSize: '1.05rem' }}>{item.recommendedDepartment}</strong>
                  <span style={{ fontSize: '0.8rem', color: 'var(--text-subtle)' }}>
                    {new Date(item.createdAt).toLocaleString()}
                  </span>
                </div>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', maxWidth: '650px' }}>
                  "{item.symptomsText}"
                </p>
              </div>

              <button onClick={() => setSelectedAssessment(item)} className="btn-secondary">
                View Details
              </button>
            </div>
          ))}
        </div>
      )}

      {selectedAssessment && (
        <div className="modal-overlay" style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(15, 23, 42, 0.8)', backdropFilter: 'blur(8px)', zIndex: 1000, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1rem' }}>
          <div className="glass-card" style={{ maxWidth: '600px', width: '100%', padding: '2rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <h3 style={{ fontSize: '1.3rem', fontWeight: '800' }}>Assessment Details #{selectedAssessment.id}</h3>
              <button onClick={() => setSelectedAssessment(null)} className="btn-secondary">✕</button>
            </div>

            <DisclaimerBox text={selectedAssessment.disclaimer} />

            <div style={{ margin: '1rem 0' }}>
              <div style={{ margin: '0.5rem 0' }}>
                <strong>Urgency Level:</strong> <span className={`badge badge-${selectedAssessment.urgencyLevel.toLowerCase()}`}>{selectedAssessment.urgencyLevel}</span>
              </div>
              <div style={{ margin: '0.5rem 0' }}>
                <strong>Recommended Department:</strong> {selectedAssessment.recommendedDepartment}
              </div>
              <div style={{ margin: '0.5rem 0' }}>
                <strong>Symptoms:</strong> {selectedAssessment.symptomsText}
              </div>
              <div style={{ margin: '0.5rem 0' }}>
                <strong>Patient Age / Gender:</strong> {selectedAssessment.age} yrs / {selectedAssessment.gender || 'Not specified'}
              </div>
              <div style={{ margin: '0.5rem 0' }}>
                <strong>Self-Reported Severity:</strong> {selectedAssessment.severityScale}/10
              </div>
            </div>

            <div style={{ background: 'rgba(15, 23, 42, 0.6)', padding: '1rem', borderRadius: '8px', marginTop: '1rem' }}>
              <h4 style={{ fontSize: '0.95rem', color: '#a5b4fc', marginBottom: '0.5rem' }}>Factors Raised:</h4>
              <ul style={{ paddingLeft: '1.2rem', fontSize: '0.88rem' }}>
                {parseReasons(selectedAssessment.reasonsJson).map((r, i) => (
                  <li key={i}>{r}</li>
                ))}
              </ul>
            </div>

            <div style={{ marginTop: '1.5rem', textAlign: 'right' }}>
              <button onClick={() => setSelectedAssessment(null)} className="btn-primary">
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AssessmentHistory;

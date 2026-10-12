import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import DisclaimerBox from '../components/DisclaimerBox';
import EmergencyBanner from '../components/EmergencyBanner';

const SymptomAssessment = () => {
  const [formData, setFormData] = useState({
    symptomsText: '',
    age: 30,
    gender: 'Male',
    severityScale: 5,
    durationDays: 1,
    preExistingConditions: '',
    fever: false,
    chestPain: false,
    shortnessOfBreath: false,
    suddenNumbness: false,
    severeBleeding: false,
    lossOfConsciousness: false
  });

  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const res = await api.post('/assessments', {
        ...formData,
        age: parseInt(formData.age),
        severityScale: parseInt(formData.severityScale),
        durationDays: parseInt(formData.durationDays)
      });
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit triage assessment.');
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
    <div style={{ maxWidth: '850px', margin: '0 auto' }}>
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h2 style={{ fontSize: '1.6rem', fontWeight: '800', marginBottom: '0.4rem' }}>
          🏥 Patient Symptom Assessment
        </h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.92rem' }}>
          Evaluate your symptoms in seconds to get transparent department guidance and emergency alerts.
        </p>

        <DisclaimerBox />
      </div>

      {result ? (
        <div className="glass-card" style={{ padding: '2.5rem', border: result.urgencyLevel === 'HIGH' ? '2px solid rgba(239, 68, 68, 0.5)' : '1px solid var(--card-border)' }}>
          {result.isEmergencyAlert && (
            <EmergencyBanner message="Possible emergency. Contact your local emergency number (112) or go to the nearest emergency department now." />
          )}

          <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
            <span style={{ fontSize: '0.9rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '1px' }}>
              Triage Urgency Level
            </span>
            <div style={{ margin: '0.8rem 0' }}>
              <span className={`badge badge-${result.urgencyLevel.toLowerCase()}`} style={{ fontSize: '1.4rem', padding: '0.6rem 1.8rem' }}>
                {result.urgencyLevel} URGENCY
              </span>
            </div>
            <h3 style={{ fontSize: '1.3rem', marginTop: '1rem', color: '#ffffff' }}>
              Recommended Department: <span style={{ color: 'var(--primary)', fontWeight: '800' }}>{result.recommendedDepartment}</span>
            </h3>
          </div>

          <div style={{ background: 'rgba(15, 23, 42, 0.6)', padding: '1.5rem', borderRadius: '12px', marginBottom: '2rem' }}>
            <h4 style={{ fontSize: '1rem', fontWeight: '700', marginBottom: '0.8rem', color: '#a5b4fc' }}>
              🔍 Why This Result? (Factor Breakdown)
            </h4>
            <ul style={{ paddingLeft: '1.2rem', color: 'var(--text-main)', fontSize: '0.92rem' }}>
              {parseReasons(result.reasonsJson).map((reason, idx) => (
                <li key={idx} style={{ marginBottom: '0.4rem' }}>{reason}</li>
              ))}
            </ul>
          </div>

          <DisclaimerBox text={result.disclaimer} />

          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', marginTop: '2rem' }}>
            <button
              onClick={() => navigate('/appointments', { state: { assessmentId: result.id, department: result.recommendedDepartment } })}
              className="btn-primary"
            >
              📅 Book Appointment with Specialist
            </button>
            <button onClick={() => setResult(null)} className="btn-secondary">
              Evaluate Another Assessment
            </button>
          </div>
        </div>
      ) : (
        <form onSubmit={handleSubmit} className="glass-card" style={{ padding: '2rem' }}>
          {error && (
            <div style={{ background: 'rgba(239, 68, 68, 0.15)', border: '1px solid #ef4444', color: '#fca5a5', padding: '0.8rem', borderRadius: '8px', marginBottom: '1rem' }}>
              {error}
            </div>
          )}

          <div className="form-group">
            <label className="form-label">Describe Your Symptoms *</label>
            <textarea
              className="form-textarea"
              rows={4}
              name="symptomsText"
              value={formData.symptomsText}
              onChange={handleChange}
              placeholder="Describe what you are feeling in detail (e.g. Sharp pain in upper stomach after meals, mild nausea for 2 days)..."
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '1rem' }}>
            <div className="form-group">
              <label className="form-label">Age (Years) *</label>
              <input
                type="number"
                className="form-input"
                name="age"
                min={0}
                max={120}
                value={formData.age}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label className="form-label">Gender</label>
              <select className="form-select" name="gender" value={formData.gender} onChange={handleChange}>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Duration (Days) *</label>
              <input
                type="number"
                className="form-input"
                name="durationDays"
                min={0}
                value={formData.durationDays}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">
              Self-Reported Symptom Severity: <strong style={{ color: 'var(--primary)' }}>{formData.severityScale} / 10</strong>
            </label>
            <input
              type="range"
              min={1}
              max={10}
              name="severityScale"
              value={formData.severityScale}
              onChange={handleChange}
              style={{ width: '100%', accentColor: 'var(--primary)' }}
            />
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.78rem', color: 'var(--text-subtle)' }}>
              <span>1 (Mild Discomfort)</span>
              <span>5 (Moderate)</span>
              <span>10 (Unbearable)</span>
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Pre-Existing Conditions (Optional)</label>
            <input
              type="text"
              className="form-input"
              name="preExistingConditions"
              value={formData.preExistingConditions}
              onChange={handleChange}
              placeholder="e.g. Asthma, High Blood Pressure, Diabetes..."
            />
          </div>

          <div className="form-group">
            <label className="form-label" style={{ color: '#fca5a5' }}>
              🚨 Red-Flag Check (Select any active severe symptoms):
            </label>
            <div className="checkbox-group">
              <label className="checkbox-label">
                <input type="checkbox" name="chestPain" checked={formData.chestPain} onChange={handleChange} />
                Chest Pain / Pressure
              </label>
              <label className="checkbox-label">
                <input type="checkbox" name="shortnessOfBreath" checked={formData.shortnessOfBreath} onChange={handleChange} />
                Shortness of Breath
              </label>
              <label className="checkbox-label">
                <input type="checkbox" name="suddenNumbness" checked={formData.suddenNumbness} onChange={handleChange} />
                Sudden Numbness / Slurred Speech
              </label>
              <label className="checkbox-label">
                <input type="checkbox" name="lossOfConsciousness" checked={formData.lossOfConsciousness} onChange={handleChange} />
                Loss of Consciousness / Fainting
              </label>
              <label className="checkbox-label">
                <input type="checkbox" name="severeBleeding" checked={formData.severeBleeding} onChange={handleChange} />
                Active Severe Bleeding
              </label>
              <label className="checkbox-label">
                <input type="checkbox" name="fever" checked={formData.fever} onChange={handleChange} />
                High Fever
              </label>
            </div>
          </div>

          <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', marginTop: '1.5rem' }} disabled={loading}>
            {loading ? 'Evaluating Triage...' : 'Calculate Urgency & Department'}
          </button>
        </form>
      )}
    </div>
  );
};

export default SymptomAssessment;

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import DisclaimerBox from '../components/DisclaimerBox';

const Departments = () => {
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchDepartments();
  }, []);

  const fetchDepartments = async () => {
    try {
      const res = await api.get('/departments');
      setDepartments(res.data);
    } catch (err) {
      console.error("Failed to load departments", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h2 style={{ fontSize: '1.6rem', fontWeight: '800' }}>🏥 Hospital Specialized Departments</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.92rem' }}>
          Explore specialized departments and consult verified medical experts.
        </p>
        <DisclaimerBox />
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem' }}>Loading departments...</div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '1.5rem' }}>
          {departments.map((dept) => (
            <div key={dept.id} className="glass-card" style={{ padding: '1.8rem' }}>
              <div style={{ fontSize: '2.5rem', marginBottom: '0.8rem' }}>{dept.icon || '🏥'}</div>
              <h3 style={{ fontSize: '1.25rem', fontWeight: '700', marginBottom: '0.4rem' }}>{dept.name}</h3>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '1.2rem', minHeight: '50px' }}>
                {dept.description}
              </p>
              <div style={{ fontSize: '0.85rem', color: '#fca5a5', marginBottom: '1.2rem' }}>
                Emergency Contact: <strong>{dept.emergencyPhone || '112'}</strong>
              </div>
              <button
                onClick={() => navigate('/appointments', { state: { department: dept.name, departmentId: dept.id } })}
                className="btn-primary"
                style={{ width: '100%', justifyContent: 'center' }}
              >
                Book Department Appointment
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Departments;

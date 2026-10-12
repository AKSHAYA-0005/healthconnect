import React, { useState, useEffect } from 'react';
import api from '../services/api';
import DisclaimerBox from '../components/DisclaimerBox';

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('analytics'); // analytics, users, doctors, departments
  const [analytics, setAnalytics] = useState(null);
  const [users, setUsers] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);

  // Form states for creating doctor / department
  const [newDept, setNewDept] = useState({ name: '', description: '', icon: '🏥', emergencyPhone: '112' });
  const [newDoctor, setNewDoctor] = useState({
    fullName: '', email: '', password: 'doc123', phone: '',
    departmentId: '', qualification: '', experienceYears: 5,
    consultationFee: 120.0, availableDays: 'Mon-Fri'
  });

  useEffect(() => {
    fetchAdminData();
  }, []);

  const fetchAdminData = async () => {
    setLoading(true);
    try {
      const [analyticsRes, usersRes, doctorsRes, deptRes] = await Promise.all([
        api.get('/admin/analytics'),
        api.get('/admin/users'),
        api.get('/doctors'),
        api.get('/departments')
      ]);
      setAnalytics(analyticsRes.data);
      setUsers(usersRes.data);
      setDoctors(doctorsRes.data);
      setDepartments(deptRes.data);
      if (deptRes.data.length > 0) {
        setNewDoctor(prev => ({ ...prev, departmentId: deptRes.data[0].id }));
      }
    } catch (err) {
      console.error("Failed to load admin console data", err);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleUserStatus = async (id, currentStatus) => {
    try {
      await api.put(`/admin/users/${id}/status`, { enabled: !currentStatus });
      fetchAdminData();
    } catch (err) {
      alert("Failed to update user status");
    }
  };

  const handleCreateDepartment = async (e) => {
    e.preventDefault();
    try {
      await api.post('/admin/departments', newDept);
      setNewDept({ name: '', description: '', icon: '🏥', emergencyPhone: '112' });
      fetchAdminData();
    } catch (err) {
      alert("Failed to create department");
    }
  };

  const handleCreateDoctor = async (e) => {
    e.preventDefault();
    try {
      await api.post('/admin/doctors', newDoctor);
      setNewDoctor({
        fullName: '', email: '', password: 'doc123', phone: '',
        departmentId: departments[0]?.id || '', qualification: '', experienceYears: 5,
        consultationFee: 120.0, availableDays: 'Mon-Fri'
      });
      fetchAdminData();
    } catch (err) {
      alert("Failed to create doctor account");
    }
  };

  return (
    <div>
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h2 style={{ fontSize: '1.6rem', fontWeight: '800' }}>⚡ Admin System Control Panel</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.92rem' }}>
          Manage hospital accounts, verified doctors, departments, and real-time triage statistics.
        </p>
        <DisclaimerBox />
      </div>

      {/* Tabs */}
      <div style={{ display: 'flex', gap: '0.8rem', marginBottom: '1.5rem' }}>
        {['analytics', 'users', 'doctors', 'departments'].map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`btn-secondary ${activeTab === tab ? 'active' : ''}`}
            style={{
              textTransform: 'capitalize',
              background: activeTab === tab ? 'var(--primary)' : 'rgba(255, 255, 255, 0.08)',
              color: '#ffffff'
            }}
          >
            {tab === 'analytics' ? '📊 Triage Statistics' : tab === 'users' ? '👥 User Management' : tab === 'doctors' ? '👨‍⚕️ Doctor Management' : '🏥 Department Control'}
          </button>
        ))}
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '3rem' }}>Loading Admin Console...</div>
      ) : (
        <>
          {/* Analytics Tab */}
          {activeTab === 'analytics' && analytics && (
            <div>
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.2rem', marginBottom: '2rem' }}>
                <div className="glass-card" style={{ padding: '1.5rem', textAlign: 'center' }}>
                  <div style={{ fontSize: '2rem', fontWeight: '800', color: '#818cf8' }}>{analytics.totalPatients}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Total Patients</div>
                </div>
                <div className="glass-card" style={{ padding: '1.5rem', textAlign: 'center' }}>
                  <div style={{ fontSize: '2rem', fontWeight: '800', color: '#38bdf8' }}>{analytics.totalDoctors}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Active Doctors</div>
                </div>
                <div className="glass-card" style={{ padding: '1.5rem', textAlign: 'center' }}>
                  <div style={{ fontSize: '2rem', fontWeight: '800', color: '#e2e8f0' }}>{analytics.totalAssessments}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Completed Assessments</div>
                </div>
                <div className="glass-card" style={{ padding: '1.5rem', textAlign: 'center' }}>
                  <div style={{ fontSize: '2rem', fontWeight: '800', color: '#34d399' }}>{analytics.totalAppointments}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Booked Appointments</div>
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
                <div className="glass-card" style={{ padding: '1.8rem' }}>
                  <h3 style={{ fontSize: '1.1rem', fontWeight: '700', marginBottom: '1rem' }}>Triage Urgency Breakdown</h3>
                  {Object.entries(analytics.urgencyBreakdown || {}).map(([level, count]) => (
                    <div key={level} style={{ marginBottom: '1rem' }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.3rem', fontSize: '0.9rem' }}>
                        <span><strong className={`badge badge-${level.toLowerCase()}`}>{level}</strong></span>
                        <span>{count} Assessments</span>
                      </div>
                      <div style={{ background: 'rgba(255,255,255,0.1)', height: '10px', borderRadius: '5px', overflow: 'hidden' }}>
                        <div style={{
                          width: `${(count / (analytics.totalAssessments || 1)) * 100}%`,
                          height: '100%',
                          background: level === 'HIGH' ? '#ef4444' : level === 'MEDIUM' ? '#f59e0b' : '#10b981'
                        }} />
                      </div>
                    </div>
                  ))}
                </div>

                <div className="glass-card" style={{ padding: '1.8rem' }}>
                  <h3 style={{ fontSize: '1.1rem', fontWeight: '700', marginBottom: '1rem' }}>Recommended Department Distribution</h3>
                  {Object.entries(analytics.departmentBreakdown || {}).map(([dept, count]) => (
                    <div key={dept} style={{ display: 'flex', justifyContent: 'space-between', padding: '0.6rem 0', borderBottom: '1px solid var(--card-border)', fontSize: '0.9rem' }}>
                      <span>{dept}</span>
                      <strong>{count} cases</strong>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* User Management */}
          {activeTab === 'users' && (
            <div className="glass-card" style={{ padding: '1.8rem' }}>
              <h3 style={{ fontSize: '1.2rem', fontWeight: '700', marginBottom: '1rem' }}>Registered Users</h3>
              <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.9rem' }}>
                <thead>
                  <tr style={{ borderBottom: '1px solid var(--card-border)', textAlign: 'left', color: 'var(--text-muted)' }}>
                    <th style={{ padding: '0.8rem' }}>ID</th>
                    <th style={{ padding: '0.8rem' }}>Full Name</th>
                    <th style={{ padding: '0.8rem' }}>Email</th>
                    <th style={{ padding: '0.8rem' }}>Role</th>
                    <th style={{ padding: '0.8rem' }}>Status</th>
                    <th style={{ padding: '0.8rem' }}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((u) => (
                    <tr key={u.id} style={{ borderBottom: '1px solid var(--card-border)' }}>
                      <td style={{ padding: '0.8rem' }}>#{u.id}</td>
                      <td style={{ padding: '0.8rem', fontWeight: '600' }}>{u.fullName}</td>
                      <td style={{ padding: '0.8rem' }}>{u.email}</td>
                      <td style={{ padding: '0.8rem' }}>{u.role.replace('ROLE_', '')}</td>
                      <td style={{ padding: '0.8rem' }}>
                        <span className={`badge ${u.enabled ? 'badge-low' : 'badge-high'}`}>
                          {u.enabled ? 'Active' : 'Disabled'}
                        </span>
                      </td>
                      <td style={{ padding: '0.8rem' }}>
                        <button
                          onClick={() => handleToggleUserStatus(u.id, u.enabled)}
                          className="btn-secondary"
                          style={{ padding: '0.3rem 0.7rem', fontSize: '0.8rem' }}
                        >
                          {u.enabled ? 'Disable' : 'Enable'}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* Doctor Management */}
          {activeTab === 'doctors' && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '1.5rem' }}>
              <form onSubmit={handleCreateDoctor} className="glass-card" style={{ padding: '1.5rem' }}>
                <h3 style={{ fontSize: '1.1rem', fontWeight: '700', marginBottom: '1rem' }}>+ Register New Doctor</h3>
                <div className="form-group">
                  <label className="form-label">Doctor Name</label>
                  <input type="text" className="form-input" value={newDoctor.fullName} onChange={(e) => setNewDoctor({ ...newDoctor, fullName: e.target.value })} required />
                </div>
                <div className="form-group">
                  <label className="form-label">Email</label>
                  <input type="email" className="form-input" value={newDoctor.email} onChange={(e) => setNewDoctor({ ...newDoctor, email: e.target.value })} required />
                </div>
                <div className="form-group">
                  <label className="form-label">Department</label>
                  <select className="form-select" value={newDoctor.departmentId} onChange={(e) => setNewDoctor({ ...newDoctor, departmentId: e.target.value })} required>
                    {departments.map((d) => (
                      <option key={d.id} value={d.id}>{d.name}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Qualification</label>
                  <input type="text" className="form-input" value={newDoctor.qualification} onChange={(e) => setNewDoctor({ ...newDoctor, qualification: e.target.value })} placeholder="e.g. MD Cardiology" required />
                </div>
                <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center' }}>Create Doctor Profile</button>
              </form>

              <div className="glass-card" style={{ padding: '1.5rem' }}>
                <h3 style={{ fontSize: '1.1rem', fontWeight: '700', marginBottom: '1rem' }}>Active Medical Staff</h3>
                {doctors.map((doc) => (
                  <div key={doc.id} style={{ padding: '1rem', borderBottom: '1px solid var(--card-border)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                      <strong style={{ fontSize: '1rem' }}>{doc.user?.fullName}</strong>
                      <div style={{ fontSize: '0.85rem', color: 'var(--primary)' }}>{doc.department?.name} | {doc.qualification}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Experience: {doc.experienceYears} yrs | Fee: ${doc.consultationFee}</div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Department Management */}
          {activeTab === 'departments' && (
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '1.5rem' }}>
              <form onSubmit={handleCreateDepartment} className="glass-card" style={{ padding: '1.5rem' }}>
                <h3 style={{ fontSize: '1.1rem', fontWeight: '700', marginBottom: '1rem' }}>+ Add Department</h3>
                <div className="form-group">
                  <label className="form-label">Department Name</label>
                  <input type="text" className="form-input" value={newDept.name} onChange={(e) => setNewDept({ ...newDept, name: e.target.value })} required />
                </div>
                <div className="form-group">
                  <label className="form-label">Description</label>
                  <textarea className="form-textarea" rows={3} value={newDept.description} onChange={(e) => setNewDept({ ...newDept, description: e.target.value })} required />
                </div>
                <div className="form-group">
                  <label className="form-label">Icon (Emoji)</label>
                  <input type="text" className="form-input" value={newDept.icon} onChange={(e) => setNewDept({ ...newDept, icon: e.target.value })} />
                </div>
                <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center' }}>Save Department</button>
              </form>

              <div className="glass-card" style={{ padding: '1.5rem' }}>
                <h3 style={{ fontSize: '1.1rem', fontWeight: '700', marginBottom: '1rem' }}>Hospital Departments</h3>
                {departments.map((d) => (
                  <div key={d.id} style={{ padding: '0.8rem 0', borderBottom: '1px solid var(--card-border)', display: 'flex', alignItems: 'center', gap: '1rem' }}>
                    <span style={{ fontSize: '1.8rem' }}>{d.icon || '🏥'}</span>
                    <div>
                      <strong>{d.name}</strong>
                      <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>{d.description}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default AdminDashboard;

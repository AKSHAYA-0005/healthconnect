import React, { useState, useEffect, useRef } from 'react';
import api from '../services/api';
import DisclaimerBox from './DisclaimerBox';
import EmergencyBanner from './EmergencyBanner';

const ChatbotModal = ({ isOpen, onClose, onAssessmentComplete }) => {
  const [session, setSession] = useState(null);
  const [inputMessage, setInputMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    if (isOpen && !session) {
      startChat();
    }
  }, [isOpen]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [session?.messages]);

  const startChat = async () => {
    setLoading(true);
    try {
      const res = await api.post('/chatbot/start');
      setSession(res.data);
    } catch (err) {
      console.error("Failed to start chatbot session", err);
    } finally {
      setLoading(false);
    }
  };

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!inputMessage.trim() || !session || loading) return;

    const userMsg = inputMessage;
    setInputMessage('');
    setLoading(true);

    try {
      const res = await api.post('/chatbot/message', {
        sessionId: session.sessionId,
        message: userMsg
      });
      setSession(res.data);
      if (res.data.finalTriageResult && onAssessmentComplete) {
        onAssessmentComplete(res.data.finalTriageResult);
      }
    } catch (err) {
      console.error("Failed to send chatbot message", err);
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" style={overlayStyle}>
      <div className="glass-card modal-content" style={modalContentStyle}>
        <div style={headerStyle}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <span>🤖</span> HEALTHCONNECT AI Assistant
          </h3>
          <button onClick={onClose} className="btn-secondary" style={{ padding: '0.2rem 0.6rem' }}>✕</button>
        </div>

        <DisclaimerBox />

        {session?.emergencyTriggered && (
          <EmergencyBanner emergencyNumber={session?.finalTriageResult?.emergencyNumber || "112"} />
        )}

        <div style={chatBodyStyle}>
          {session?.messages?.map((msg, index) => (
            <div
              key={index}
              style={{
                display: 'flex',
                justifyContent: msg.sender === 'USER' ? 'flex-end' : 'flex-start',
                marginBottom: '0.8rem'
              }}
            >
              <div
                style={{
                  maxWidth: '80%',
                  padding: '0.8rem 1.1rem',
                  borderRadius: '16px',
                  background: msg.sender === 'USER'
                    ? 'linear-gradient(135deg, #6366f1 0%, #4f46e5 100%)'
                    : msg.isEmergencyAlert
                    ? 'rgba(239, 68, 68, 0.25)'
                    : 'rgba(30, 41, 59, 0.9)',
                  border: msg.isEmergencyAlert ? '1px solid #ef4444' : '1px solid var(--card-border)',
                  color: '#ffffff',
                  fontSize: '0.92rem'
                }}
              >
                {msg.message}
              </div>
            </div>
          ))}
          {loading && <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>Thinking...</div>}
          <div ref={messagesEndRef} />
        </div>

        {session?.finalTriageResult ? (
          <div style={{ padding: '1rem', borderTop: '1px solid var(--card-border)', textAlign: 'center' }}>
            <div style={{ marginBottom: '0.5rem', fontWeight: '700' }}>
              Final Recommendation: <span className={`badge badge-${session.finalTriageResult.urgencyLevel.toLowerCase()}`}>{session.finalTriageResult.urgencyLevel}</span>
            </div>
            <p style={{ fontSize: '0.88rem', color: 'var(--text-muted)' }}>
              Recommended Department: <strong>{session.finalTriageResult.recommendedDepartment}</strong>
            </p>
            <button onClick={startChat} className="btn-primary" style={{ marginTop: '0.8rem' }}>
              Start New Triage
            </button>
          </div>
        ) : (
          <form onSubmit={sendMessage} style={inputFormStyle}>
            <input
              type="text"
              placeholder="Type your response or symptoms..."
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              className="form-input"
              style={{ flex: 1 }}
              disabled={loading || session?.step === 'COMPLETED'}
            />
            <button type="submit" className="btn-primary" disabled={loading || session?.step === 'COMPLETED'}>
              Send
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

const overlayStyle = {
  position: 'fixed',
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  background: 'rgba(15, 23, 42, 0.8)',
  backdropFilter: 'blur(8px)',
  zIndex: 1000,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  padding: '1rem'
};

const modalContentStyle = {
  width: '100%',
  maxWidth: '650px',
  maxHeight: '85vh',
  display: 'flex',
  flexDirection: 'column',
  padding: '1.5rem',
  overflow: 'hidden'
};

const headerStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  paddingBottom: '1rem',
  borderBottom: '1px solid var(--card-border)'
};

const chatBodyStyle = {
  flex: 1,
  overflowY: 'auto',
  padding: '1rem 0',
  display: 'flex',
  flexDirection: 'column'
};

const inputFormStyle = {
  display: 'flex',
  gap: '0.5rem',
  paddingTop: '1rem',
  borderTop: '1px solid var(--card-border)'
};

export default ChatbotModal;

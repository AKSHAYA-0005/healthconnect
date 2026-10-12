import React from 'react';

const EmergencyBanner = ({ emergencyNumber = "112", message }) => {
  return (
    <div className="emergency-banner" id="emergency-alert-banner">
      <div className="emergency-icon">🚨</div>
      <div>
        <div className="emergency-title">CRITICAL SAFETY ALERT</div>
        <p>
          {message || `Possible emergency. Contact your local emergency number (${emergencyNumber}) or go to the nearest emergency department now.`}
        </p>
      </div>
    </div>
  );
};

export default EmergencyBanner;

import React from 'react';

export const MANDATORY_DISCLAIMER =
  "This result is a preliminary automated triage recommendation and is not a medical diagnosis. If symptoms are severe, worsening, or concerning, seek professional medical care promptly.";

const DisclaimerBox = ({ text = MANDATORY_DISCLAIMER }) => {
  return (
    <div className="disclaimer-box" id="mandatory-disclaimer-box">
      <span style={{ fontSize: '1.2rem' }}>⚠️</span>
      <div>
        <strong>Healthcare Safety Notice:</strong>
        <p style={{ marginTop: '0.2rem' }}>{text}</p>
      </div>
    </div>
  );
};

export default DisclaimerBox;

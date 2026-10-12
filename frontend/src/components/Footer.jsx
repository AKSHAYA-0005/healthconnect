import React from 'react';
import { MANDATORY_DISCLAIMER } from './DisclaimerBox';

const Footer = () => {
  return (
    <footer className="footer" id="app-footer">
      <div className="footer-disclaimer">
        <strong>HEALTHCARE DISCLAIMER:</strong> {MANDATORY_DISCLAIMER}
      </div>
      <p style={{ fontSize: '0.85rem' }}>
        &copy; {new Date().getFullYear()} HEALTHCONNECT – AI-Powered Patient Triage & Decision-Support System. All rights reserved. Emergency Contact: <strong>112</strong>
      </p>
    </footer>
  );
};

export default Footer;

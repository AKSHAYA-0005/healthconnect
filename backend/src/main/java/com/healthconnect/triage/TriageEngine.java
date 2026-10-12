package com.healthconnect.triage;

import com.healthconnect.triage.dto.TriageInput;
import com.healthconnect.triage.dto.TriageResult;

public interface TriageEngine {
    TriageResult evaluate(TriageInput input);
}

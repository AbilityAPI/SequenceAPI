package com.abilityapi.sequenceapi.action.condition;

import com.abilityapi.sequenceapi.context.SequenceContext;

import java.util.Optional;

public class ConditionResult {

    private boolean accept = false;
    private SequenceContext sequenceContext = null;

    public ConditionResult() {}

    public boolean isAccepted() {
        return this.accept;
    }

    public void setAccepted(boolean accept) {
        this.accept = accept;
    }

    public Optional<SequenceContext> getSequenceContext() {
        return Optional.ofNullable(this.sequenceContext);
    }

    public void setSequenceContext(SequenceContext sequenceContext) {
        this.sequenceContext = sequenceContext;
    }

}

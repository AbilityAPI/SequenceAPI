package com.abilityapi.sequenceapi.action.condition;

import com.abilityapi.sequenceapi.origin.Origin;

import java.util.Optional;

public class ConditionResult {

    private boolean accept = false;
    private Origin origin = null;

    public ConditionResult() {}

    public boolean isAccepted() {
        return this.accept;
    }

    public void setAccepted(boolean accept) {
        this.accept = accept;
    }

    public Optional<Origin> getOrigin() {
        return Optional.ofNullable(this.origin);
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

}

package com.abilityapi.sequenceapi.action.condition;

import com.abilityapi.sequenceapi.util.MutableContext;

import java.util.Optional;

public class ConditionResult<E> {

    private boolean accept = false;
    private MutableContext<E> mutableContext = null;

    public ConditionResult() {}

    public boolean isAccepted() {
        return this.accept;
    }

    public void setAccepted(boolean accept) {
        this.accept = accept;
    }

    public Optional<MutableContext<E>> getMutableContext() {
        return Optional.ofNullable(this.mutableContext);
    }

    public void setMutableContext(MutableContext<E> mutableContext) {
        this.mutableContext = mutableContext;
    }

}

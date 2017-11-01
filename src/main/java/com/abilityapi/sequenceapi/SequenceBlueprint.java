package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.context.SequenceContext;

public interface SequenceBlueprint<T> {

    Sequence create(SequenceContext sequenceContext);

    Class<? extends T> getTriggerClass();

}

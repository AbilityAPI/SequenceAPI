package com.abilityapi.sequenceapi.sequence;

import com.abilityapi.sequenceapi.origin.Origin;

public interface SequenceBlueprint<T> {

    Sequence create(Origin origin);

    Class<? extends T> getTriggerClass();

}

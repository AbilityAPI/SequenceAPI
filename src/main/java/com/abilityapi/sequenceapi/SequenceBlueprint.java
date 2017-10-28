package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.origin.Origin;

public abstract class SequenceBlueprint<T> {

    public abstract Sequence create(Origin origin);

    public abstract Class<? extends T> getTriggerClass();

}

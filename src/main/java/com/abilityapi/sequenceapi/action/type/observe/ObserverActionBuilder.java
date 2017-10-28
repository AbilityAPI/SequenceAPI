package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.action.CommonActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBlueprint;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;

public class ObserverActionBuilder<T> implements CommonActionBuilder {

    public ObserverActionBuilder() {}

    @Override
    public ObserverActionBuilder observe(Class event) {
        return null;
    }

    @Override
    public ObserverActionBuilder observe(ObserverActionBlueprint actionBlueprint) {
        return null;
    }

    @Override
    public ObserverActionBuilder observe(ObserverAction action) {
        return null;
    }

    @Override
    public ScheduleActionBuilder schedule() {
        return null;
    }

    @Override
    public ScheduleActionBuilder schedule(ScheduleActionBlueprint actionBlueprint) {
        return null;
    }

    @Override
    public ScheduleActionBuilder schedule(ScheduleAction scheduleAction) {
        return null;
    }

}

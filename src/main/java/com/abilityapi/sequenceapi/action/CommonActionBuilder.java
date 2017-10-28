package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBlueprint;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;

public interface CommonActionBuilder<T> {

    ObserverActionBuilder<T> observe(Class<T> event);

    ObserverActionBuilder<T> observe(ObserverActionBlueprint<T> actionBlueprint);

    ObserverActionBuilder<T> observe(ObserverAction<T> action);

    ScheduleActionBuilder schedule();

    ScheduleActionBuilder schedule(ScheduleActionBlueprint actionBlueprint);

    ScheduleActionBuilder schedule(ScheduleAction scheduleAction);

}

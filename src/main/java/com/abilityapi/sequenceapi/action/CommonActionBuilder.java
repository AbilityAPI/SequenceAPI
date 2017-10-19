package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;

public interface CommonActionBuilder {

    <T> ObserverActionBuilder observe(Class<T> event);

    <T> ScheduleActionBuilder schedule();

    <T> ScheduleActionBuilder schedule(ScheduleAction scheduleAction);

}

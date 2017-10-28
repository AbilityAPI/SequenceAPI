package com.abilityapi.sequenceapi.action.type.schedule;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.origin.Origin;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAction implements Action {

    private List<Condition> conditions = new ArrayList<>();
    private int delay = 0;
    private int expire = 0;
    private int period = 0;

    public ScheduleAction() {}

    @Override
    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    @Override
    public void setDelay(int period) {
        this.delay = period;
    }

    @Override
    public void setExpire(int period) {
        this.expire = period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public int getDelay() {
        return this.delay;
    }

    @Override
    public int getExpire() {
        return this.expire;
    }

    public int getPeriod() {
        return this.period;
    }

    @Override
    public boolean apply(Origin origin) {
        return false;
    }

    @Override
    public boolean success(Origin origin) {
        return false;
    }

    @Override
    public boolean failure(Origin origin) {
        return false;
    }

}

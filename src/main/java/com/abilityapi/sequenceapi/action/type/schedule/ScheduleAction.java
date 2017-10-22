package com.abilityapi.sequenceapi.action.type.schedule;

import com.abilityapi.sequenceapi.action.ActionType;
import com.abilityapi.sequenceapi.action.condition.Condition;

public class ScheduleAction implements ActionType {

    private int delay = 0;
    private int expire = 0;
    private int period = 0;

    public ScheduleAction() {}

    @Override
    public void addCondition(Condition condition) {

    }

    @Override
    public void setDelay(int period) {

    }

    @Override
    public void setExpire(int period) {

    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public int getExpire() {
        return 0;
    }

    public int getPeriod() {
        return this.period;
    }

}

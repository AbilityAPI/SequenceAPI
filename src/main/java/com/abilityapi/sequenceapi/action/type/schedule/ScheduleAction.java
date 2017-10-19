package com.abilityapi.sequenceapi.action.type.schedule;

import com.abilityapi.sequenceapi.action.ActionType;

public class ScheduleAction implements ActionType {

    private int delay = 0;
    private int expire = 0;

    public ScheduleAction() {}

    @Override
    public void addCondition() {

    }

    @Override
    public void setDelay(int period) {

    }

    @Override
    public void setExpire(int period) {

    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public int getExpire() {
        return 0;
    }
}

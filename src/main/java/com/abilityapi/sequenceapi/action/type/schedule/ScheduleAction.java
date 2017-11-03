package com.abilityapi.sequenceapi.action.type.schedule;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionResult;
import com.abilityapi.sequenceapi.action.condition.ConditionTypes;
import com.abilityapi.sequenceapi.context.SequenceContext;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAction implements Action {

    private List<Condition> conditions = new ArrayList<>();
    private int delay = 0;
    private int expire = 0;
    private int period = 0;
    private int repeat = 0;

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

    public int getRepeat() {
        return this.repeat;
    }

    @Override
    public boolean apply(SequenceContext sequenceContext) {
        boolean applyResult = this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionTypes.UNDEFINED))
                .noneMatch(condition -> {
                    ConditionResult result = condition.apply(sequenceContext);

                    return !result.isAccepted();
                });

        if (applyResult && this.period != 0) this.repeat++;
        return applyResult;
    }

    @Override
    public boolean success(SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionTypes.SUCCESS))
                .noneMatch(condition -> {
                    ConditionResult result = condition.apply(sequenceContext);

                    return !result.isAccepted();
                });
    }

    @Override
    public boolean failure(SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionTypes.FAIL))
                .anyMatch(condition -> {
                    ConditionResult result = condition.apply(sequenceContext);

                    return result.isAccepted();
                });
    }

}

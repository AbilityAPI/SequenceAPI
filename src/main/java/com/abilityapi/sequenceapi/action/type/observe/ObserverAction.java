package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionResult;
import com.abilityapi.sequenceapi.action.condition.ConditionTypes;
import com.abilityapi.sequenceapi.context.SequenceContext;

import java.util.ArrayList;
import java.util.List;

public class ObserverAction<T> implements Action {

    private final Class<? extends T> eventClass;

    private List<Condition> conditions = new ArrayList<>();
    private int delay = 0;
    private int expire = 0;

    public ObserverAction(Class<? extends T> eventClass) {
        this.eventClass = eventClass;
    }

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

    @Override
    public int getDelay() {
        return this.delay;
    }

    @Override
    public int getExpire() {
        return this.expire;
    }

    @Override
    public boolean apply(SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionTypes.UNDEFINED))
                .noneMatch(condition -> {
                    ConditionResult result = condition.apply(sequenceContext);

                    return !result.isAccepted();
                });
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

    public final Class<? extends T> getEventClass() {
        return this.eventClass;
    }

}

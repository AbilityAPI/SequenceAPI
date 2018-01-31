package com.abilityapi.sequenceapi.action.type.after;

import com.abilityapi.sequenceapi.SequenceContext;
import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionType;

import java.util.ArrayList;
import java.util.List;

public class AfterAction implements Action {

    private List<Condition> conditions = new ArrayList<>();
    private int delay = 0;
    private int expire = 0;

    public AfterAction() {}

    @Override
    public void addCondition(final Condition condition) {
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
    public final boolean apply(final SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.NORMAL))
                .allMatch(condition -> condition.getSupplier().apply(sequenceContext));
    }

    @Override
    public final boolean success(final SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.SUCCESS))
                .allMatch(condition -> condition.getSupplier().apply(sequenceContext));
    }

    @Override
    public final boolean failure(final SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.FAIL))
                .anyMatch(condition -> condition.getSupplier().apply(sequenceContext));
    }
}

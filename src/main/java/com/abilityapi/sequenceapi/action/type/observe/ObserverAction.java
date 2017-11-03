package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.Sequence;
import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionType;
import com.abilityapi.sequenceapi.context.SequenceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event observer for a {@link Sequence}.
 *
 * <p>Events that that are called on this {@link Sequence}
 * where this action is in the current position, will have
 * the observer active.
 *
 * When it is active, events that aren't
 * the same type and are called on this {@link Sequence} will
 * execute the {@link ConditionType#FAIL} {@link Condition}s.
 *
 * Otherwise if the event is the same as the target event class
 * it will execute the {@link ConditionType#UNDEFINED}
 * {@link Condition}s. If these all pass it will then execute
 * the {@link ConditionType#SUCCESS} {@link Condition}s.</p>
 *
 * @param <T> the event type
 */
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
                .filter(condition -> condition.getType().equals(ConditionType.UNDEFINED))
                .allMatch(condition -> condition.apply(sequenceContext));
    }

    @Override
    public boolean success(SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.SUCCESS))
                .allMatch(condition -> condition.apply(sequenceContext));
    }

    @Override
    public boolean failure(SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.FAIL))
                .anyMatch(condition -> condition.apply(sequenceContext));
    }

    /**
     * Returns the event class for this {@link ObserverAction}.
     *
     * @return the event class
     */
    public final Class<? extends T> getEventClass() {
        return this.eventClass;
    }

}

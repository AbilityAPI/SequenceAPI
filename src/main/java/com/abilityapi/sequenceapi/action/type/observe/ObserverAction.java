package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.action.ActionType;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.origin.Origin;

public class ObserverAction<T> implements ActionType {

    private final Class<? extends T> eventClazz;

    private int delay = 0;
    private int expire = 0;

    public ObserverAction(Class<? extends T> eventClazz) {
        this.eventClazz = eventClazz;
    }

    @Override
    public void addCondition(Condition condition) {

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

    public final Origin apply(final Origin origin, final T event) {
        return null;
    }

    public final Origin success(final Origin origin, final T event) {
        return null;
    }

    public final Origin failure(final Origin origin, final T event) {
        return null;
    }

    public final Class<? extends T> getEventClass() {
        return null;
    }

}

package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.origin.Origin;

public class ObserverAction<T> implements Action {

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

    @Override
    public Origin apply(Origin origin) {
        return null;
    }

    @Override
    public Origin success(Origin origin) {
        return null;
    }

    @Override
    public Origin failure(Origin origin) {
        return null;
    }

    public final Class<? extends T> getEventClass() {
        return null;
    }

}
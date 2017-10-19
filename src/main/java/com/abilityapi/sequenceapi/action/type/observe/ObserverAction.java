package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.action.ActionType;
import com.abilityapi.sequenceapi.origin.Origin;

public class ObserverAction<T> implements ActionType {

    private int delay = 0;
    private int expire = 0;

    public ObserverAction() {}

    public void addCondition() {

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

    public Origin apply(Origin origin, T event) {
        return null;
    }

    public Origin success(Origin origin, T event) {
        return null;
    }

    public Origin failure(Origin origin, T event) {
        return null;
    }

   public  Class<? extends T> getEventClass() {
        return null;
    }

}

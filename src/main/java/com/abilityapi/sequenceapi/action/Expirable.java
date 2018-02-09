package com.abilityapi.sequenceapi.action;

/**
 * Represents an {@link Action} that can expire.
 */
public interface Expirable {

    /**
     * Sets an expire to this {@link Action}.
     *
     * @param period the period to expire
     */
    void setExpire(final int period);

    /**
     * Returns the expire period.
     *
     * @return the expire period
     */
    int getExpire();

}

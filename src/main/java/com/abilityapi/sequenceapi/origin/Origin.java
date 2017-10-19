package com.abilityapi.sequenceapi.origin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Origin {

    public static OriginKey SOURCE = new OriginKey("source", null);

    public static OriginKey OWNER = new OriginKey("owner", null);

    public static OriginKey STATE = new OriginKey("state", null);

    public static Builder builder() {
        return new Builder();
    }

    // This should be immutable.
    private Map<OriginKey, Object> origins = new HashMap<>();

    private Origin(Map<OriginKey, Object> origins) {
        this.origins = origins;
    }


    public static class Builder {

        Map<OriginKey, Object> origins = new HashMap<>();
        Set<String> keysUsed = new HashSet<>();

        Builder() {}

        Builder source(Object value) {
            if (!this.keysUsed.contains(Origin.SOURCE.getId())) {
                this.origins.put(Origin.SOURCE, value);
                this.keysUsed.add(Origin.SOURCE.getId());
            }
            return this;
        }

        Builder owner(Object value) {
            if (!this.keysUsed.contains(Origin.OWNER.getId())) {
                this.origins.put(Origin.OWNER, value);
                this.keysUsed.add(Origin.OWNER.getId());
            }
            return this;
        }

        Builder state(Object value) {
            if (!this.keysUsed.contains(Origin.STATE.getId())) {
                this.origins.put(Origin.STATE, value);
                this.keysUsed.add(Origin.STATE.getId());
            }
            return this;
        }

        Builder custom(OriginKey key, Object value) {
            if (!this.keysUsed.contains(key.getId())) {
                this.origins.put(key, value);
                this.keysUsed.add(key.getId());
            }
            return this;
        }

        Origin build() {
            return new Origin(this.origins);
        }

    }

}

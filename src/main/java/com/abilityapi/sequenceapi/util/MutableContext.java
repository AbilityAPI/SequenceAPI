package com.abilityapi.sequenceapi.util;

import java.util.Optional;

public interface MutableContext<E> {

    Optional<E> getValue();

    E getDefaultValue();

    void setValue(E value);

}

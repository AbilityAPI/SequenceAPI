package com.abilityapi.sequenceapi.util;

import java.util.Optional;

public interface ImmutableContext<E> {

    Optional<E> getValue();

}

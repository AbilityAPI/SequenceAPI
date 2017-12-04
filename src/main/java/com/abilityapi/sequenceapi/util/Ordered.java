package com.abilityapi.sequenceapi.util;

import java.util.Objects;

public class Ordered<E> {

    private final long index;

    private E element;

    public Ordered(long index) {
        this(index, null);
    }

    public Ordered(long index, E element) {
        this.index = index;
        this.element = element;
    }

    // order

    public Ordered<E> next(E element) {
        return new Ordered<>(this.index + 1, element);
    }

    public Ordered<E> nextEmpty() {
        return new Ordered<>(this.index + 1);
    }

    public long getNext() {
        return this.index + 1;
    }

    // specific

    public long getIndex() {
        return this.index;
    }

    public E getElement() {
        return this.element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.index, this.element);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Ordered<?>)) return false;
        final Ordered<?> that = (Ordered<?>) obj;
        return Objects.equals(this.index, that.index)
                && Objects.equals(this.element, that.element);
    }
}

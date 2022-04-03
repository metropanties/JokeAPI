package me.metropanties.jokeapi.cache;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public interface CacheSet<E> {

    void add(@NotNull E element);

    void remove(@NotNull E element);

    void clear();

    Iterator<E> iterator();

    Collection<E> values();

    boolean contains(@NotNull E element);

}

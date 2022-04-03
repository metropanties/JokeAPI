package me.metropanties.jokeapi.cache;

import me.metropanties.jokeapi.Server;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ExpiringSet<E> implements CacheSet<E> {

    private final Set<E> set = new HashSet<>();

    private void evict(@NotNull E element) {
        Server.getInstance().getExecutor().schedule(() -> {
            if (!set.contains(element))
                return;

            set.remove(element);
        }, 24, TimeUnit.HOURS);
    }

    @Override
    public void add(@NotNull E element) {
        if (set.contains(element))
            return;

        set.add(element);
        evict(element);
    }

    @Override
    public void remove(@NotNull E element) {
        if (!set.contains(element))
            return;

        set.remove(element);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return set.iterator();
    }

    @Override
    public Collection<E> values() {
        return new ArrayList<>(set);
    }

    @Override
    public boolean contains(@NotNull E element) {
        return set.contains(element);
    }

}

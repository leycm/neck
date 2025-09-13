package org.leycm.neck.share.shared;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.leycm.neck.share.config.SharingDefaults;
import java.time.LocalDateTime;
import java.util.*;

public class SharedHistory<T> {
    public static class HistoryEntry<T> {
        private final T value;
        private final LocalDateTime timestamp;
        private final String modifiedBy;

        public HistoryEntry(T value, String modifiedBy) {
            this.value = value;
            this.timestamp = LocalDateTime.now();
            this.modifiedBy = modifiedBy;
        }

        public T getValue() { return value; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getModifiedBy() { return modifiedBy; }
    }

    private final LinkedList<HistoryEntry<T>> history;
    private final int maxSize;

    private SharedHistory(int maxSize) {
        this.maxSize = maxSize;
        this.history = new LinkedList<>();
    }

    @Contract(value = " -> new", pure = true)
    public static <T> @NotNull SharedHistory<T> empty() {
        return new SharedHistory<>(SharingDefaults.DEFAULT_HISTORY_SIZE);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull SharedHistory<T> empty(int maxSize) {
        return new SharedHistory<>(maxSize);
    }

    public static <T> @NotNull SharedHistory<T> of(T value) {
        SharedHistory<T> sh = new SharedHistory<>(SharingDefaults.DEFAULT_HISTORY_SIZE);
        sh.set(value);
        return sh;
    }

    public Optional<T> get() {
        return history.isEmpty() ? Optional.empty() : Optional.ofNullable(history.peekFirst().getValue());
    }

    public void set(T value) {
        set(value, "system");
    }

    public void set(T value, String modifiedBy) {
        history.addFirst(new HistoryEntry<>(value, modifiedBy));
        if (history.size() > maxSize) {
            history.removeLast();
        }
    }

    public List<HistoryEntry<T>> getHistory() {
        return new ArrayList<>(history);
    }

    public List<HistoryEntry<T>> getHistory(int limit) {
        return history.stream().limit(limit).toList();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public int size() {
        return history.size();
    }
}


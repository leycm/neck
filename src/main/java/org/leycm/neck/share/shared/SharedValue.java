package org.leycm.neck.share.shared;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public class SharedValue<T> {
    private T value;
    private LocalDateTime lastModified;
    private String modifiedBy;

    private SharedValue(T value) {
        this.value = value;
        this.lastModified = LocalDateTime.now();
        this.modifiedBy = "system";
    }

    @Contract(" -> new")
    public static <T> @NotNull SharedValue<T> empty() {
        return new SharedValue<>(null);
    }

    @Contract("_ -> new")
    public static <T> @NotNull SharedValue<T> of(T value) {
        return new SharedValue<>(value);
    }

    public Optional<T> get() {
        return Optional.ofNullable(value);
    }

    public void set(T value) {
        set(value, "system");
    }

    public void set(T value, String modifiedBy) {
        this.value = value;
        this.lastModified = LocalDateTime.now();
        this.modifiedBy = modifiedBy;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isPresent() {
        return value != null;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public String toString() {
        return "SharedValue{" +
                "value=" + value +
                ", lastModified=" + lastModified +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
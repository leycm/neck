package de.leycm.neck.result;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class Result<T> {
    private static final Result<?> EMPTY = new Result<>(null, null);

    private final Throwable throwable;
    private final T result;

    @Contract(value = "_ -> new", pure = true)
    public static @NonNull Result<?> throwing(final Throwable throwable) {
        return new Result<>(null, throwable);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NonNull Result<?> of(final @NotNull("Value cannot be null use Result#empty() instead") Object value) {
        return new Result<>(value, null);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NonNull Result<?> ofNullable(final Object value) {
        return value == null ? empty() : new Result<>(value, null);
    }

    @Contract(value = "-> new", pure = true)
    public static @NonNull Result<?> empty() {
        return EMPTY;
    }

    private Result(final T result, final Throwable throwable) {
        this.result = result;
        this.throwable = throwable;
    }

    public T unwarp() throws UnwrapException {
        return unwarp("Unwrap a Result with an Exception");
    }

    public T unwarp(final @NonNull String message) throws UnwrapException{
        if (throwable != null)
            throw new UnwrapException(message, throwable);

        return result;
    }

    public @NonNull T expect(final @NonNull String message)
            throws NoSuchElementException {

        if (throwable != null)
            throw new NoSuchElementException(message + ": " + throwable.getMessage());

        if (result == null)
            throw new NoSuchElementException(message + ": Result is empty");

        return result;
    }

    public @NonNull T get() throws NoSuchElementException {
        if (throwable != null)
            throw new NoSuchElementException("Result contains an exception: " + throwable.getMessage());

        if (result == null)
            throw new NoSuchElementException("Result is empty");

        return result;
    }

    public @NonNull T recover(Function<Throwable, T> handler) {
        if (result == null)
            return handler.apply(new NoSuchElementException("Result is empty"));

        if (throwable != null)
            return handler.apply(throwable);

        return result;
    }

    public @NonNull T orElse(Supplier<T> supplier) {
        return recover(ignored -> supplier.get());
    }

    public @NonNull T orElse(T value) {
        return recover(ignored -> value);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isSuccessful() {
        return throwable == null;
    }

    public @NonNull String asString() {
        if (this == EMPTY) return "Result{empty}";

        if (throwable != null)
            return "Result{throwable=" + throwable + "}";

        return "Result{result=" + result + "}";
    }

    public @NonNull String toString() {
        return asString();
    }

    // NOTE: May make this throw exception in future versions
    @ApiStatus.Experimental
    public @NonNull Optional<T> asOptional() {
        if (throwable != null) return Optional.empty();
        return Optional.ofNullable(result);
    }

}

package org.mbari.jcommons.fn;

import java.util.Optional;
import java.util.function.Function;

public record Left<L, R>(L value) implements Either<L, R> {

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public Optional<L> left() {
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<R> right() {
        return Optional.empty();
    }

    @Override
    public <T> T fold(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper) {
        return leftMapper.apply(value);
    }

    @Override
    public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
        return Either.left(value);
    }
}

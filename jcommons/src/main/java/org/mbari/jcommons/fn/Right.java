package org.mbari.jcommons.fn;

import java.util.Optional;
import java.util.function.Function;

public record Right<L, R>(R value) implements Either<L, R> {

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public Optional<L> left() {
        return Optional.empty();
    }

    @Override
    public Optional<R> right() {
        return Optional.ofNullable(value);
    }

    @Override
    public <T> T fold(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper) {
        return rightMapper.apply(value);
    }

    @Override
    public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
        return Either.right(mapper.apply(value));
    }
}

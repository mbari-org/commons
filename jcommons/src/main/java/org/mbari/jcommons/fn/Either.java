package org.mbari.expd.etc.jdk;

import java.util.Optional;
import java.util.function.Function;

public sealed interface Either<L, R> permits Left, Right {

    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    boolean isLeft();

    boolean isRight();

    Optional<L> left();

    Optional<R> right();

    <T> T fold(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper);

    <U> Either<L, U> map(Function<? super R, ? extends U> mapper);
}

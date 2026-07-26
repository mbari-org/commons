package org.mbari.jcommons.fn;

public record Failure<T, E>(E error) implements Result<T, E> {}

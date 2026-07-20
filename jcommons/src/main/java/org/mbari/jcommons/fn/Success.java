package org.mbari.jcommons.fn;

public record Success<T, E>(T value) implements Result<T, E> {}

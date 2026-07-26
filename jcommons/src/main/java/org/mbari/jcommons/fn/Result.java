package org.mbari.jcommons.fn;

public sealed interface Result<T, E>
    permits Success, Failure {}

package org.mbari.scommons.etc.jdk

import java.lang.Iterable as JIterable

/**
  * From https://august.nagro.us/scala-for-loop.html
  */
object Loops:

    /** Java-style for-of loop (ex, for(x : myList) {...}) */
    inline def loop[A](inline iterable: Iterable[A])(inline loopBody: A => Any): Unit =
        val it = iterable.iterator
        while it.hasNext do loopBody(it.next())


    /** Java-style for-of loop (ex, for(x : myList) {...}) */
    inline def loop[A](inline iterable: JIterable[A])(inline loopBody: A => Any): Unit =
        val it = iterable.iterator
        while it.hasNext do loopBody(it.next())

    /** for-of loop, for Arrays */
    inline def loop[A](arr: Array[A])(inline loopBody: A => Any): Unit =
        var i = 0
        while i < arr.length do
            loopBody(arr(i))
            i += 1

    /** 
     * simple loop for ranges 
     * 
     * ```scala
     * loop(0, _ < 9, _ + 1) { i =>
     *     println("number: " + i)
     * }
     * ```
     * */
    inline def loop(
        start: Int,
        until: Int,
        inline advance: Int => Int = _ + 1
    )(inline loopBody: Int => Any): Unit =
        var i = start
        while i < until do
            loopBody(i)
            i = advance(i)

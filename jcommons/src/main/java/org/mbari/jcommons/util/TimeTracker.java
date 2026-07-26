package org.mbari.jcommons.util;

import java.util.function.Supplier;

class TimeTracker implements AutoCloseable {

    private final String name;
    private final long startTime;
    private static final System.Logger log = System.getLogger(TimeTracker.class.getName());

    private TimeTracker(String name) {
        this.name = name;
        this.startTime = System.nanoTime();
    }

    public static TimeTracker of(String name) {
        return new TimeTracker(name);
    }

    public static <T> T safeTrack(String name, Supplier<T> supplier) {
        try (TimeTracker tracker = TimeTracker.of(name)) {
            return supplier.get();
        }
        catch (Exception e) {
            log.log(System.Logger.Level.ERROR, "Error in " + name, e);
            return null;
        }
    }

    public static void safeTrack(String name, Runnable runnable) {
        try (TimeTracker tracker = TimeTracker.of(name)) {
            runnable.run();
        }
        catch (Exception e) {
            log.log(System.Logger.Level.ERROR, "Error in " + name, e);
        }
    }

    @Override
    public void close() {
        long endTime = System.nanoTime();
        long duration = endTime - startTime / 1_000_000;
        log.log(System.Logger.Level.INFO, name + " took " + duration + " ms");
    }






    
}

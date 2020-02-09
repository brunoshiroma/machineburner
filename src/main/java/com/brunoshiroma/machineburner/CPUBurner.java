package com.brunoshiroma.machineburner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CPUBurner implements Burner {

    @Override
    public void burn() {

        final List<Thread> threads = new ArrayList<>();

        Thread runner = new Thread(() -> {
            final int maxThreads = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1);

            for (int i = 0; i < maxThreads; i++) {
                final Thread thread = new Thread(() -> {
                    System.out.println(String.format("STARTING THREAD %s", Thread.currentThread().getName()));
                    while (true) {
                        Math.pow(Math.random(), Math.random());
                    }
                });
                thread.setDaemon(false);
                thread.setUncaughtExceptionHandler((t, e) -> {
                    System.out.println(String.format("CPU BURNER error %s", e.getMessage()));
                });

                thread.start();
                threads.add(thread);
            }

        });

        runner.setDaemon(false);
        runner.start();
    }
}
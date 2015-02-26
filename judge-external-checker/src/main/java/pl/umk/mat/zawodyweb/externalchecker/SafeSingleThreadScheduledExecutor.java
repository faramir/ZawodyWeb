/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;

/**
 * @author Marek Nowicki /faramir/
 */
public class SafeSingleThreadScheduledExecutor implements ScheduledExecutorService {
    
    private static final Logger logger = Logger.getLogger(SafeSingleThreadScheduledExecutor.class);
    final ScheduledExecutorService executor;
    
    public SafeSingleThreadScheduledExecutor() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }
    
    private Runnable wrap(Runnable command) {
        return () -> {
            try {
                command.run();
            } catch (Throwable t) {
                logger.fatal("Exception occurs", t);
            }
        };
    }
    
    private <V> Callable<V> wrap(Callable<V> command) {
        return () -> {
            try {
                return command.call();
            } catch (Throwable t) {
                logger.fatal("Exception occurs", t);
            }
            return null;
        };
    }
    
    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return executor.schedule(wrap(command), delay, unit);
    }
    
    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return executor.schedule(wrap(callable), delay, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return executor.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
    }
    
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return executor.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
    }
    
    @Override
    public void shutdown() {
        executor.shutdown();
    }
    
    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }
    
    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }
    
    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }
    
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }
    
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }
    
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executor.submit(task, result);
    }
    
    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executor.invokeAll(tasks);
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return executor.invokeAll(tasks, timeout, unit);
    }
    
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executor.invokeAny(tasks);
    }
    
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executor.invokeAny(tasks, timeout, unit);
    }
    
    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
    
}

package pl.umk.mat.zawodyweb.judge;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author lukash2k
 */
public class InterruptTimer extends Timer {

    private class InterruptThread extends TimerTask {

        private Thread thread;

        public InterruptThread(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            thread.interrupt();
        }
    }

    /**
     * Kills thread after specified amount of time
     *
     * @param thread thread to be killed
     * @param timelimit delay in milliseconds before task is to be killed.
     */
    public void schedule(Thread thread, long timelimit) {
        this.schedule(new InterruptThread(thread), timelimit);
    }
}

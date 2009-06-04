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

    public void schedule(Thread thread, int timelimit) {
        this.schedule(new InterruptThread(Thread.currentThread()), timelimit);
    }
}

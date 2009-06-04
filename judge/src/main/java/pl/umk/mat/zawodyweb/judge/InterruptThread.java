package pl.umk.mat.zawodyweb.judge;

import java.util.TimerTask;

/**
 *
 * @author lukash2k
 */
public class InterruptThread extends TimerTask {

    private Thread thread;

    public InterruptThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void run() {
        thread.interrupt();
    }
}

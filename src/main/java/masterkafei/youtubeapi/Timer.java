package masterkafei.youtubeapi;

import java.util.TimerTask;

public class Timer {

    private int milliseconds;
    private java.util.Timer timer;
    private TimerTask task;
    public Timer(int milliseconds) {

        this.milliseconds = milliseconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public boolean start(TimerTask task) {
        if(this.timer != null) {
            return false;
        }
        this.task = task;
        this.timer = new java.util.Timer();
        this.timer.schedule(task, 0, this.milliseconds);
        return true;
    }

    public boolean stop() {
        if(this.timer == null) {
            return false;
        }
        this.task.cancel();
        this.timer.cancel();
        this.timer.purge();
        this.timer = null;
        return true;
    }
}

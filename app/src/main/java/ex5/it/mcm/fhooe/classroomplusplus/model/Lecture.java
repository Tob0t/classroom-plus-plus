package ex5.it.mcm.fhooe.classroomplusplus.model;

/**
 * Created by Tob0t on 10.05.2016.
 */
public class Lecture {
    private long startTime;
    private String name;
    private long endTime;

    public Lecture() {
    }

    public Lecture(long startTime, String name, long endTime) {
        this.startTime = startTime;
        this.name = name;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public long getEndTime() {
        return endTime;
    }
}

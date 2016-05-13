package ex5.it.mcm.fhooe.classroomplusplus.model;

/**
 * Created by Tob0t on 10.05.2016.
 */
public class Lecture {
    private long startTime;
    private String lectureName;
    private long endTime;
    private String personName;

    public Lecture() {
    }

    public Lecture(long startTime, String lectureName, long endTime, String personName) {
        this.startTime = startTime;
        this.lectureName = lectureName;
        this.endTime = endTime;
        this.personName = personName;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getLectureName() {
        return lectureName;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getPersonName() {
        return personName;
    }
}

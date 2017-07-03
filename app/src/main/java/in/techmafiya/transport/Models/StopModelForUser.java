package in.techmafiya.transport.Models;

/**
 * Created by saiso on 10-12-2016.
 */

public class StopModelForUser {
    public String stopName;
    public String time;

    public StopModelForUser(String stopName, String time) {
        this.stopName = stopName;
        this.time = time;
    }
    public StopModelForUser(){

    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

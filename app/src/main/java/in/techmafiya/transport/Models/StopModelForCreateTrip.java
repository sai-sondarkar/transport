package in.techmafiya.transport.Models;

/**
 * Created by saiso on 10-12-2016.
 */

public class StopModelForCreateTrip {
    public String startStopName;
    public String endStopName;

    public StopModelForCreateTrip(String startStopName, String endStopName) {
        this.startStopName = startStopName;
        this.endStopName = endStopName;
    }
    public StopModelForCreateTrip(){

    }

    public String getStartStopName() {
        return startStopName;
    }

    public void setStartStopName(String startStopName) {
        this.startStopName = startStopName;
    }

    public String getEndStopName() {
        return endStopName;
    }

    public void setEndStopName(String endStopName) {
        this.endStopName = endStopName;
    }
}

package in.techmafiya.transport.Models;

import java.util.List;

/**
 * Created by ABCD on 21-Oct-16.
 */
public class UserModel {

    List<String> createdTrips;
    List<StopModelForUser> sheduledTrips;
    List<String> collabrationTrips;
    List<String> pastScheduledTrips;

    public UserModel(){
    }

    public UserModel(List<String> createdTrips, List<StopModelForUser> sheduledTrips, List<String> collabrationTrips, List<String> pastScheduledTrips) {
        this.createdTrips = createdTrips;
        this.sheduledTrips = sheduledTrips;
        this.collabrationTrips = collabrationTrips;
        this.pastScheduledTrips = pastScheduledTrips;
    }

    public List<String> getCreatedTrips() {
        return createdTrips;
    }

    public void setCreatedTrips(List<String> createdTrips) {
        this.createdTrips = createdTrips;
    }

    public List<StopModelForUser> getSheduledTrips() {
        return sheduledTrips;
    }

    public void setSheduledTrips(List<StopModelForUser> sheduledTrips) {
        this.sheduledTrips = sheduledTrips;
    }

    public List<String> getCollabrationTrips() {
        return collabrationTrips;
    }

    public void setCollabrationTrips(List<String> collabrationTrips) {
        this.collabrationTrips = collabrationTrips;
    }

    public List<String> getPastScheduledTrips() {
        return pastScheduledTrips;
    }

    public void setPastScheduledTrips(List<String> pastScheduledTrips) {
        this.pastScheduledTrips = pastScheduledTrips;
    }


}

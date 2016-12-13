package in.techmafiya.transport.Models;

import java.util.List;

/**
 * Created by ABCD on 19-Oct-16.
 */
public class SingleLocalityDetails {

    public boolean active;
    public boolean passed;
    public String stopPoint;
    public String eta;
    public int color;
    public List<String> uidNotify;

    public SingleLocalityDetails(boolean active, boolean passed, String stopPoint, String eta, List<String> uidNotify) {
        this.active = active;
        this.passed = passed;
        this.stopPoint = stopPoint;
        this.eta = eta;
        this.uidNotify = uidNotify;
    }

    public SingleLocalityDetails(){

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<String> getUidNotify() {
        return uidNotify;
    }

    public void setUidNotify(List<String> uidNotify) {
        this.uidNotify = uidNotify;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getStopPoint() {
        return stopPoint;
    }

    public void setStopPoint(String stopPoint) {
        this.stopPoint = stopPoint;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleLocalityDetails)) return false;

        SingleLocalityDetails singleLocalityDetails = (SingleLocalityDetails) o;

        return getStopPoint().equals(singleLocalityDetails.getStopPoint());

    }

    @Override
    public int hashCode() {
        return getStopPoint().hashCode();
    }
}

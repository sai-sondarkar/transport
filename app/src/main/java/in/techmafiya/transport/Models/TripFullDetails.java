package in.techmafiya.transport.Models;

import java.util.List;

/**
 * Created by ABCD on 19-Nov-16.
 */
public class TripFullDetails {

    public String uid;
    public boolean check;
    public List<SingleLocalityDetails> stoplist;

    public TripFullDetails(){ //default const.
    }

    public TripFullDetails(boolean check, List<SingleLocalityDetails> stoplist) {
        this.check = check;
        this.stoplist = stoplist;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public List<SingleLocalityDetails> getStoplist() {
        return stoplist;
    }

    public void setStoplist(List<SingleLocalityDetails> stoplist) {
        this.stoplist = stoplist;
    }
}

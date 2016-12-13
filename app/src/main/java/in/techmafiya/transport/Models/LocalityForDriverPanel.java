package in.techmafiya.transport.Models;

/**
 * Created by ABCD on 22-Oct-16.
 */
public class LocalityForDriverPanel {
    private int localityId;
    private String localityName;

    public LocalityForDriverPanel() {
    }

    public LocalityForDriverPanel(int localityId, String localityName) {
        this.localityId = localityId;
        this.localityName = localityName;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    @Override
    public String toString() {
        return "LocalityForDriverPanel{" +
                "localityId=" + localityId +
                ", localityName='" + localityName + '\'' +
                '}';
    }
}
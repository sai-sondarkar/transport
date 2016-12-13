package in.techmafiya.transport.Models;

/**
 * Created by saiso on 11-12-2016.
 */

public class AwareSpot {
    public String id;
    public String title;
    public String description;
    public String city;
    public double lat;
    public double lng;

    public AwareSpot(String id, String title, String description, String city, double lat, double lng) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }
    public AwareSpot(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AwareSpot)) return false;

        AwareSpot awareSpot = (AwareSpot) o;

        return id.equals(awareSpot.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

package ma.geomatic.GeocodageJava.helper;

public class PointCsv {
	private String dh;
	private String lat;
	private String lon;
	
	@Override
	public String toString() {
		return "PointCsv [dh=" + dh + ", lat=" + lat + ", lng=" + lon + "]";
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
	
	
}

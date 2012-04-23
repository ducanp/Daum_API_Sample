package daum.kml.Example;

public class CoordinatesData {
	/*
	  	longitude ≥ −180 and <= 180
		latitude ≥ −90 and ≤ 90
		altitude values (optional) are in meters above sea level
	 * */
	
	private double lng;
	private double lat;
	private double alt;
	
	public CoordinatesData(double lng, double lat, double alt){		
		this.lng = lng;
		this.lat = lat;
		this.alt = alt;
	}
	
	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
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
}
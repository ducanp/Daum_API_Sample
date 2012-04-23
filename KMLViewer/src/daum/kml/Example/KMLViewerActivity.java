package daum.kml.Example;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class KMLViewerActivity extends Activity {
	
	private MapView mapView = null;

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //MapAPI �ʱ�ȭ
        initMap();
        
        try {
        	/*
        	 * KML ���� ���
        	 * web�� �����ϴ� ���� ��θ� ����ϰų�
        	 * local ���￡ �����ϴ� ������ ����ص� �ȴ�.
        	 * ��, �Ʒ��� ���� ���������� file://�� ������ �־�� �Ѵ�.
        	 * */
        	
			//URL kmlPath = new URL("file:///sdcard/kml/c10c87be89cf3a79.kml");
        	URL kmlPath = new URL("http://www.likejazz.com/kmz/z/2c286317b839c6d7.kml");
        	//URL kmlPath = new URL("http://www.likejazz.com/kmz/z/c10c87be89cf3a79.kml");

			InputStream kmlData = kmlPath.openStream();
			
			//KML �����͸� �Ľ�, ���׸���
			XMLParserWithPull parser = new XMLParserWithPull();
			drawPolyLine( parser.xmlParserWithPull(kmlData) );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * KML Data �޾ƿͼ� �� �׸���
	 * */
	public void drawPolyLine(ArrayList<CoordinatesData> coordinatesData){
		try {
			if(coordinatesData == null) return;
						
			MapPolyline polyline = new MapPolyline();
	        polyline.setLineColor(Color.argb(128, 0, 0, 255));
	        
	        /*
	         * KML�� ��ü ��ǥ ������ ���� ������, ����Ͽ����� ǥ���ϱ� ��ƴ�.(�ִ� 256�� ǥ��)
	         * ����, ��ü ������ 256���� �۰� ����� ���Ͽ� gap��ŭ ��ǥ�� �ǳ� ���� ��´�.
	         * */
				
			int gap = 1;
			int markerSize = coordinatesData.size();
			
			if( markerSize > 256 ){
				gap = (int)(markerSize / 256 ) + 1;
			}
			
			Log.e("MarkerSize", "��ü ���� : " + markerSize +
										" / gap : " + gap + 
										" / ���� ���� : " + markerSize / gap );
			
			for(int i=0 ; i < markerSize ; i+=gap){
				CoordinatesData lnglat = coordinatesData.get(i);
				
				//����Ʈ �߰�
				polyline.addPoint(MapPoint.mapPointWithGeoCoord(lnglat.getLat(), lnglat.getLng()));
			}
				
			//������ ��� �߰�
			mapView.addPolyline(polyline);						
			mapView.fitMapViewAreaToShowAllPolylines();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * ���ʱ�ȭ
	 * */
	public void initMap(){
		mapView = new MapView(this);

		// �߱޹��� APIKEY�� �ٲ�� �Ѵ�.
        mapView.setDaumMapApiKey("982fc2298db83bc1f2345bbbc79e65b37384fe9e");

		// MapType�� Hybrid�� �ܿ��� Standard(�Ϲ� ����), Satellite(��ī�̺�) �� ������ �� �ִ�.
        mapView.setMapType(MapView.MapType.Hybrid);

		// �� Activity�� ������ mapView ����
        setContentView(mapView);
	}
	
	public MapView getMapView() {
		return mapView;
	}
}
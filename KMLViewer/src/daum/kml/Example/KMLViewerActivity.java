package daum.kml.Example;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class KMLViewerActivity extends Activity implements MapView.OpenAPIKeyAuthenticationResultListener,
																MapView.MapViewEventListener,
																MapView.CurrentLocationEventListener,
																MapView.POIItemEventListener {
	
	private MapView mapView = null;
	
	private static KMLViewerActivity instance = null;

	public static KMLViewerActivity getInstance() {
		return instance;
	}

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        instance = this;
        
        //MapAPI �ʱ�ȭ
        mapInit();
        
        try {
        	/*
        	 * KML ���� ���
        	 * web�� �����ϴ� ���� ��θ� ����ϰų�
        	 * local ���￡ �����ϴ� ������ ����ص� �ȴ�.
        	 * ��, �Ʒ��� ���� ���������� file://�� ������ �־�� �Ѵ�.
        	 * */
        	
			//URL kmlPath = new URL("file:///sdcard/kml/c10c87be89cf3a79.kml");
        	//URL kmlPath = new URL("http://www.likejazz.com/kmz/z/2c286317b839c6d7.kml");
        	URL kmlPath = new URL("http://www.likejazz.com/kmz/z/c10c87be89cf3a79.kml");

			InputStream kmlData = kmlPath.openStream();
			
			//KML �����͸� �Ľ�, ���׸���
			drawPolyLine( new XMLParserWithPull().xmlParserWithPull(kmlData) );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * KML Data �޾ƿͼ� �� �׸���
	 * */
	public void drawPolyLine(ArrayList<CoordinatesData> coordinatesData){
		
		MapView mapView = KMLViewerActivity.getInstance().getMapView();
		
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
	public void mapInit(){
		mapView = new MapView(this);

		// �߱޹��� APIKEY�� �ٲ�� �Ѵ�.
        mapView.setDaumMapApiKey("982fc2298db83bc1f2345bbbc79e65b37384fe9e");
        mapView.setOpenAPIKeyAuthenticationResultListener(this);

		// �� �������� �ʿ��� �̺�Ʈ �����ʵ� ����
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setPOIItemEventListener(this);
        

		// MapType�� Hybrid�� �ܿ��� Standard(�Ϲ� ����), Satellite(��ī�̺�) �� ������ �� �ִ�.
        mapView.setMapType(MapView.MapType.Hybrid);

		// �� Activity�� ������ mapView ����
        setContentView(mapView);
	}
	
	public MapView getMapView() {
		return mapView;
	}

	@Override
	public void onCurrentLocationDeviceHeadingUpdate(MapView arg0, float arg1) {
	}

	@Override
	public void onCurrentLocationUpdate(MapView arg0, MapPoint arg1, float arg2) {	
	}

	@Override
	public void onCurrentLocationUpdateCancelled(MapView arg0) {
	}

	@Override
	public void onCurrentLocationUpdateFailed(MapView arg0) {
	}

	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewLongPressed(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
		//zoom level Ȯ�ο�
		Log.d("daum.kml.Example", "zoom Level : " + mapView.getZoomLevel());
	}

	@Override
	public void onDaumMapOpenAPIKeyAuthenticationResult(MapView arg0, int arg1,
			String arg2) {
	}

	@Override
	public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1) {
	}

	@Override
	public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1,
			MapPoint arg2) {
	}

	@Override
	public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
	}
}
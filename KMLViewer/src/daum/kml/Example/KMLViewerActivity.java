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
        
        //MapAPI 초기화
        mapInit();
        
        try {
        	/*
        	 * KML 파일 경로
        	 * web에 존재하는 파일 경로를 사용하거나
        	 * local 영억에 존재하는 파일을 상용해도 된다.
        	 * 단, 아래와 같이 프로토콜을 file://로 지정해 주어야 한다.
        	 * */
        	
			//URL kmlPath = new URL("file:///sdcard/kml/c10c87be89cf3a79.kml");
        	//URL kmlPath = new URL("http://www.likejazz.com/kmz/z/2c286317b839c6d7.kml");
        	URL kmlPath = new URL("http://www.likejazz.com/kmz/z/c10c87be89cf3a79.kml");

			InputStream kmlData = kmlPath.openStream();
			
			//KML 데이터를 파싱, 선그리기
			drawPolyLine( new XMLParserWithPull().xmlParserWithPull(kmlData) );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * KML Data 받아와서 선 그리기
	 * */
	public void drawPolyLine(ArrayList<CoordinatesData> coordinatesData){
		
		MapView mapView = KMLViewerActivity.getInstance().getMapView();
		
		try {
			if(coordinatesData == null) return;
						
			MapPolyline polyline = new MapPolyline();
	        polyline.setLineColor(Color.argb(128, 0, 0, 255));
	        
	        /*
	         * KML의 전체 좌표 개수가 많기 때문에, 모바일에서는 표현하기 어렵다.(최대 256개 표현)
	         * 따라서, 전체 개수를 256보다 작게 만들기 위하여 gap만큼 좌표를 건너 띄어가며 찍는다.
	         * */
				
			int gap = 1;
			int markerSize = coordinatesData.size();
			
			if( markerSize > 256 ){
				gap = (int)(markerSize / 256 ) + 1;
			}
			
			Log.e("MarkerSize", "전체 개수 : " + markerSize +
										" / gap : " + gap + 
										" / 찍힐 개수 : " + markerSize / gap );
			
			for(int i=0 ; i < markerSize ; i+=gap){
				CoordinatesData lnglat = coordinatesData.get(i);
				
				//포인트 추가
				polyline.addPoint(MapPoint.mapPointWithGeoCoord(lnglat.getLat(), lnglat.getLng()));
			}
				
			//지도에 경로 추가
			mapView.addPolyline(polyline);						
			mapView.fitMapViewAreaToShowAllPolylines();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * 맵초기화
	 * */
	public void mapInit(){
		mapView = new MapView(this);

		// 발급받은 APIKEY로 바꿔야 한다.
        mapView.setDaumMapApiKey("982fc2298db83bc1f2345bbbc79e65b37384fe9e");
        mapView.setOpenAPIKeyAuthenticationResultListener(this);

		// 이 예제에서 필요한 이벤트 리스너들 설정
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setPOIItemEventListener(this);
        

		// MapType은 Hybrid형 외에도 Standard(일반 지도), Satellite(스카이뷰) 를 설정할 수 있다.
        mapView.setMapType(MapView.MapType.Hybrid);

		// 이 Activity와 연동될 mapView 지정
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
		//zoom level 확인용
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
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
        
        //MapAPI 초기화
        initMap();
        
        try {
        	/*
        	 * KML 파일 경로
        	 * web에 존재하는 파일 경로를 사용하거나
        	 * local 영억에 존재하는 파일을 상용해도 된다.
        	 * 단, 아래와 같이 프로토콜을 file://로 지정해 주어야 한다.
        	 * */
        	
			//URL kmlPath = new URL("file:///sdcard/kml/c10c87be89cf3a79.kml");
        	URL kmlPath = new URL("http://www.likejazz.com/kmz/z/2c286317b839c6d7.kml");
        	//URL kmlPath = new URL("http://www.likejazz.com/kmz/z/c10c87be89cf3a79.kml");

			InputStream kmlData = kmlPath.openStream();
			
			//KML 데이터를 파싱, 선그리기
			XMLParserWithPull parser = new XMLParserWithPull();
			drawPolyLine( parser.xmlParserWithPull(kmlData) );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/*
	 * KML Data 받아와서 선 그리기
	 * */
	public void drawPolyLine(ArrayList<CoordinatesData> coordinatesData){
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
	public void initMap(){
		mapView = new MapView(this);

		// 발급받은 APIKEY로 바꿔야 한다.
        mapView.setDaumMapApiKey("982fc2298db83bc1f2345bbbc79e65b37384fe9e");

		// MapType은 Hybrid형 외에도 Standard(일반 지도), Satellite(스카이뷰) 를 설정할 수 있다.
        mapView.setMapType(MapView.MapType.Hybrid);

		// 이 Activity와 연동될 mapView 지정
        setContentView(mapView);
	}
	
	public MapView getMapView() {
		return mapView;
	}
}
package daum.kml.Example;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class KMLViewerActivity extends Activity {
	
	private MapView mapView = null;
	private EditText txtURL;

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //MapAPI 초기화
        initMap();
        
    	/*
    	 * KML 파일 경로
    	 * web에 존재하는 파일 경로를 사용하거나
    	 * local 영억에 존재하는 파일을 상용해도 된다.
    	 * 단, 아래와 같이 프로토콜을 file://로 지정해 주어야 한다.
    	 * */
    	
    	try {
    		//URL kmlPath = new URL("file:///sdcard/kml/c10c87be89cf3a79.kml");
			URL kmlPath = new URL("http://www.likejazz.com/kmz/z/c10c87be89cf3a79.kml");
			
			parseKML(kmlPath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	

		Toast.makeText(this, "KML 파일을 바꾸려면 메뉴를 누르세요.", Toast.LENGTH_SHORT).show();
    }
	
	/*
	 * KML 데이터를 파싱, 선그리기
	 * */
	public void parseKML(URL kmlPath){
		try{
			InputStream kmlData = kmlPath.openStream();
			
			XMLParserWithPull parser = new XMLParserWithPull();
			drawPolyLine( parser.xmlParserWithPull(kmlData) );
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * KML Data 받아와서 선 그리기
	 * */
	public void drawPolyLine(ArrayList<CoordinatesData> coordinatesData){
		try {
			if(coordinatesData == null) return;
			
			//맵에 있는 PolyLine 제거
			mapView.removeAllPolylines();
			
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
			
			//경로에 맞춰서 지도 줌, 센터 자동으로 맞추기
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
	
	/* 
	 * 옵션메뉴
	 * return 	true - 화면에 옵션 메뉴를 보여줌
	 * 			false - 화면에 옵션 메뉴를 보여주지 않음
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.main_option_menu, menu);

		return true;
	}
	
	/*
	 * 옵션 메뉴 선택시에 발생하는 이벤트 처리 메소드
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.input_url){
			//URL 입력할 다이얼로그 띄우기
			txtURL = new EditText(this);
			txtURL.setText("http://www.likejazz.com/kmz/z/9e3c51ceb56e5c9c.kml");
			
			new AlertDialog.Builder(this)
					.setTitle("URL을 입력하세요.")
					.setView(txtURL)
					.setPositiveButton("확인", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							try {
								//입력받은 URL로 새로운 경로 표시
								parseKML(new URL( txtURL.getText().toString() ));
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}
						}
					})
					.setNegativeButton("취소", null)
					.show();
		}
		
		return true;
	}
}
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
        
        //MapAPI �ʱ�ȭ
        initMap();
        
    	/*
    	 * KML ���� ���
    	 * web�� �����ϴ� ���� ��θ� ����ϰų�
    	 * local ���￡ �����ϴ� ������ ����ص� �ȴ�.
    	 * ��, �Ʒ��� ���� ���������� file://�� ������ �־�� �Ѵ�.
    	 * */
    	
    	try {
    		//URL kmlPath = new URL("file:///sdcard/kml/c10c87be89cf3a79.kml");
			URL kmlPath = new URL("http://www.likejazz.com/kmz/z/c10c87be89cf3a79.kml");
			
			parseKML(kmlPath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	

		Toast.makeText(this, "KML ������ �ٲٷ��� �޴��� ��������.", Toast.LENGTH_SHORT).show();
    }
	
	/*
	 * KML �����͸� �Ľ�, ���׸���
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
	 * KML Data �޾ƿͼ� �� �׸���
	 * */
	public void drawPolyLine(ArrayList<CoordinatesData> coordinatesData){
		try {
			if(coordinatesData == null) return;
			
			//�ʿ� �ִ� PolyLine ����
			mapView.removeAllPolylines();
			
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
			
			//��ο� ���缭 ���� ��, ���� �ڵ����� ���߱�
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
	
	/* 
	 * �ɼǸ޴�
	 * return 	true - ȭ�鿡 �ɼ� �޴��� ������
	 * 			false - ȭ�鿡 �ɼ� �޴��� �������� ����
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.main_option_menu, menu);

		return true;
	}
	
	/*
	 * �ɼ� �޴� ���ýÿ� �߻��ϴ� �̺�Ʈ ó�� �޼ҵ�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.input_url){
			//URL �Է��� ���̾�α� ����
			txtURL = new EditText(this);
			txtURL.setText("http://www.likejazz.com/kmz/z/9e3c51ceb56e5c9c.kml");
			
			new AlertDialog.Builder(this)
					.setTitle("URL�� �Է��ϼ���.")
					.setView(txtURL)
					.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							try {
								//�Է¹��� URL�� ���ο� ��� ǥ��
								parseKML(new URL( txtURL.getText().toString() ));
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}
						}
					})
					.setNegativeButton("���", null)
					.show();
		}
		
		return true;
	}
}
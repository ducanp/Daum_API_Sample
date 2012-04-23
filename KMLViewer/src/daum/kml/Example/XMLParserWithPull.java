package daum.kml.Example;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLParserWithPull {
	public ArrayList<CoordinatesData> xmlParserWithPull(InputStream kmlData) {
		
		if(kmlData == null) return null;
		
		XmlPullParserFactory parserCreator;
		try {
			parserCreator = XmlPullParserFactory.newInstance();
		
	    	XmlPullParser parser = parserCreator.newPullParser();
	    	
	    	parser.setInput( kmlData, null );
	    	
	    	ArrayList<CoordinatesData> coordinatesData = new ArrayList<CoordinatesData>();
	    	int parserEvent = parser.getEventType();
	        String tag = "";
	        boolean inTitle = false;
			
			while (parserEvent != XmlPullParser.END_DOCUMENT ){
				switch (parserEvent) {
	
				case XmlPullParser.TEXT:
					if (inTitle) {
						if (tag.equals("coordinates")) {
							//��ǥ���� �޸�(",")�� ���� �Ǿ� �־ ������ �����Ѵ�
							String lnglat[] =  parser.getText().replaceAll("\n", ",").replaceAll("\\p{Space}", ",").split(",");
							
							double lng, lat, alt;
							
							for(int l=0 ; l<lnglat.length ; l++) {
								try {
									lng = Double.parseDouble( lnglat[l].trim() );
									lat = Double.parseDouble( lnglat[++l].trim() );
									alt = Double.parseDouble( lnglat[++l].trim() );
								}catch(NumberFormatException e){
									continue;
								}
								
								coordinatesData.add( new CoordinatesData(lng , lat, alt) );
							}
						}
					}
					break;
					
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.equals("coordinates")) {
						inTitle = true;
					}
					break;
					
				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals("coordinates")) {
						inTitle = false;
					}
					break;
					
				}
				parserEvent = parser.next();
			}
			
			//�Ľ��� ��� ����
			return coordinatesData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

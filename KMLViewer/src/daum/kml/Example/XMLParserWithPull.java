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
	        boolean isTitle = false;
			
			while (parserEvent != XmlPullParser.END_DOCUMENT ){
				switch (parserEvent) {
	
				case XmlPullParser.TEXT:
					if (isTitle) {
						if (tag.equals("coordinates")) {
							//좌표셋은 콤마(",")로 구분 되어 있어서 나누어 저장한다
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
						isTitle = true;
					}
					break;
					
				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals("coordinates")) {
						isTitle = false;
					}
					break;
					
				}
				parserEvent = parser.next();
			}
			
			//파싱한 결과 리턴
			return coordinatesData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

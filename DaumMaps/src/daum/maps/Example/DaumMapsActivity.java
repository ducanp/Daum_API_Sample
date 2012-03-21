package daum.maps.Example;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DaumMapsActivity extends Activity {
	private WebView webView;
	  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String MAP_URL;
        /* �⺻���� �ø��� */
        MAP_URL = "http://dna.daum.net/examples/maps/maps3/mobile_simple.html";
        //MAP_URL = "file:///android_asset/www/map.html";
        
        /* �ε�� �⺻ �����ϱ� */
        //MAP_URL = "file:///android_asset/www/loadview.html";
        
	    webView = (WebView) findViewById(R.id.webview);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.setVerticalScrollBarEnabled(false);
	    webView.setHorizontalScrollBarEnabled(false);
	    webView.setWebViewClient(new WebViewClient());
	    webView.loadUrl(MAP_URL);
    }
}
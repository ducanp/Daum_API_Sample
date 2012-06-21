package net.daum.test;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class OauthTestWebViewActivity extends Activity {
	private WebView webView;
	private String URL;
	  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        URL = "file:///android_asset/www/TESTOAUTH/cafe.html";
        //URL = "file:///android_asset/www/TESTOAUTH/yozm.html";
                
	    webView = (WebView) findViewById(R.id.webview);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.setVerticalScrollBarEnabled(false);
	    webView.setHorizontalScrollBarEnabled(false);
	    webView.setWebChromeClient(new WebChromeClient());
	    
	    webView.loadUrl(URL);
	    
    }
}
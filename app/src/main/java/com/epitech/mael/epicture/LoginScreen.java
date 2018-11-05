package com.epitech.mael.epicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        FrameLayout layout = new FrameLayout(this);
        final WebView webView = new WebView(this);
        layout.addView(webView);
        setContentView(layout);
        webView.loadUrl("@string/IMGUR_login_url");
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            private String _accessToken = "";
            private String _refreshToken = "";
            private String _username = "";

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                Log.i("INFO/", "Login Page:");
                if (url.contains("callback") && getUserInfos(url)) {
                    Intent newIntent;
                    newIntent = new Intent(LoginScreen.this, MainActivity.class);
                    newIntent.putExtra("accessToken", _accessToken);
                    newIntent.putExtra("refreshToken", _refreshToken);
                    newIntent.putExtra("username", _username);
                    startActivity(newIntent);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            private boolean getUserInfos(String url) {
                int index = 0;
                String[] outerSplit = url.split("#")[1].split("&");

                for (String s : outerSplit) {
                    String[] innerSplit = s.split("\\=");

                    switch (index) {
                        // Access Token
                        case 0:
                            _accessToken = innerSplit[1];
                            break;

                        // Refresh Token
                        case 3:
                            _refreshToken = innerSplit[1];
                            break;

                        // Username
                        case 4:
                            _username = innerSplit[1];
                            break;
                        default:

                    }
                    index++;
                }
                return !_accessToken.equals("") && !_refreshToken.equals("") && !_username.equals("");
            }
        });
    }
}

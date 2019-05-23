package com.yiwangrencai.ywkj.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AboutUsACtivity extends BaseActiviyt implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initData() {

    }

    private FrameLayout fram_job_back;
    private WebView webview;
    private ProgressBar progress;

    @Override
    public void initView() {

        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        webview = (WebView) findViewById(R.id.webview);
        progress = (ProgressBar) findViewById(R.id.progress);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new MywebViewClient());
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebChromeClient(new WebChromeClient());
        fram_job_back.setOnClickListener(this);
        webview.loadUrl(ContentUrl.BASE_URL_SHORT+"webcontent_aboutus.html");

    }

    class MywebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webview.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:
                finish();
                break;
        }
    }

}

package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;

/**
 * Created by Administrator on 2017/5/20.
 */

public class WebViewActivity2 extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initData() {

    }

    private WebView webView;
    private ProgressBar progress;

    @Override
    public void initView() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        webView = (WebView) findViewById(R.id.webview);
        progress= (ProgressBar) findViewById(R.id.progress);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setVisibility(View.INVISIBLE);
        webView.loadUrl(id);
        fram_job_back.setOnClickListener(this);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
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

package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.InitDatas;

/**
 * Created by Administrator on 2017/5/20.
 */

public class WebViewActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initData() {

    }

    private WebView webView;
    private ProgressBar progress;
    String id;
    private TextView text_view_title;
    @Override
    public void initView() {
        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        webView = (WebView) findViewById(R.id.webview);
        text_view_title= (TextView) findViewById(R.id.text_view_title);
        text_view_title.setHorizontallyScrolling(true);
        text_view_title.setFocusable(true);
        text_view_title.setFocusableInTouchMode(true);
        progress = (ProgressBar) findViewById(R.id.progress);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MywebViewClient());
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new MyWebChromeClient());

        webView.loadUrl(ContentUrl.BASE_URL_SHORT+"mobile/newsinfo_"+id+".html");
        fram_job_back.setOnClickListener(this);
    }

    class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            text_view_title.setText(title);
        }
    }

    class MywebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:function myFunction(){var a=document.getElementsByTagName('body')[0].childNodes;for(var i=a.length-1;i>=0;i--){if(a[i].className!='news_info'){a[i].remove();}}}");
            view.loadUrl("javascript:myFunction()");
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

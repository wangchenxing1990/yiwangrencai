package com.yiwangrencai.ywkj.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;

/**
 * Created by Administrator on 2017/7/22.
 */

public class FindSceretActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.seceret_activity;
    }

    @Override
    public void initData() {
//启用支持javascript

    }

    @Override
    public void initView() {
        RelativeLayout image_view= (RelativeLayout) findViewById(R.id.image_view);
        WebView web_view = (WebView) findViewById(R.id.web_view);
        image_view.setOnClickListener(this);
        WebSettings settings = web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        web_view.loadUrl(ContentUrl.BASE_URL_SHORT+"mobile/personal/find_password");
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        //web_view.loadUrl("http://baidu.com");
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });
       // web_view.goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_view:
                finish();
                break;
        }
    }
}

package com.yiwangrencai.ywkj.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;

import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.yiwangrencai.R;

/**
 * Created by Administrator on 2017/6/9.
 */
public class AddressActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {

        return R.layout.activity_address;
    }

    @Override
    public void initData() {

    }

    private MapView bmapView = null;
    private BaiduMap mBaiduMap;
    private TextView title_tilte;
    private FrameLayout fram_job_back;

    @Override
    public void initView() {

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        String latitudes = intent.getStringExtra("latitude");
        String longitudes = intent.getStringExtra("longitude");
        String coordinate_address = intent.getStringExtra("coordinate_address");

        SharedPreferences share=getSharedPreferences("data",MODE_PRIVATE);
        String latitude=share.getString("latitude","");
        String longitude=share.getString("lontitude","");

        title_tilte = (TextView) findViewById(R.id.title_tilte);
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        fram_job_back.setOnClickListener(this);

        if (address==null||"".equals(address)){
            title_tilte.setText(coordinate_address);
        }else{
            title_tilte.setText(address);
        }

      //  showContacts();
        bmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        // mBaiduMap.setMyLocationEnabled(true);
        // 定义Maker坐标点
        Log.i("latitudelatitudelatitude",latitude);
        Log.i("latitudelatitudelatitudess",longitude);
        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(20)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);

        OverlayOptions ooCircle = new CircleOptions().fillColor(0xAA3fbcf4)
                .center(point)
                .stroke(new Stroke(5, 0xAA00FF00))
                .radius(50);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        mBaiduMap.addOverlay(ooCircle);

        LatLng points = new LatLng(Double.parseDouble(latitudes), Double.parseDouble(longitudes));
        MapStatus mMapStatuss = new MapStatus.Builder()
                .target(points)
                .zoom(20)
                .build();
        MapStatusUpdate mMapStatusUpdates = MapStatusUpdateFactory.newMapStatus(mMapStatuss);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdates);
        //构建Marker图标
        BitmapDescriptor bitmaps = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions()
                .position(points)
                .icon(bitmaps);
        //创建InfoWindow展示的view
        View view=View.inflate(getApplicationContext(),R.layout.address_text_view,null);
        TextView text_view= (TextView) view.findViewById(R.id.text_view);
        text_view.setText(coordinate_address);

        InfoWindow mInfoWindow = new InfoWindow(view, points, -47);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);

        mBaiduMap.addOverlay(options);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bmapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        bmapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回上一个界面
                finish();
                break;
        }
    }
}

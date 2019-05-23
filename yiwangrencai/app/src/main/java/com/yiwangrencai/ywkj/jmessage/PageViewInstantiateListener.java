package com.yiwangrencai.ywkj.jmessage;

import android.view.View;
import android.view.ViewGroup;

public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}

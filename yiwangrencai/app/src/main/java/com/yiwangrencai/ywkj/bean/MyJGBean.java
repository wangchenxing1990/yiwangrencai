package com.yiwangrencai.ywkj.bean;

import java.io.File;

import cn.jpush.im.android.api.callback.DownloadAvatarCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/9/9.
 */

public class MyJGBean extends UserInfo {
    @Override
    public String getNotename() {
        return null;
    }

    @Override
    public String getNoteText() {
        return null;
    }

    @Override
    public long getBirthday() {
        return 0;
    }

    @Override
    public File getAvatarFile() {
        return null;
    }

    @Override
    public void getAvatarFileAsync(DownloadAvatarCallback downloadAvatarCallback) {

    }

    @Override
    public void getAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

    }

    @Override
    public void getBigAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

    }

    @Override
    public int getBlacklist() {
        return 0;
    }

    @Override
    public int getNoDisturb() {
        return 0;
    }

    @Override
    public boolean isFriend() {
        return false;
    }

    @Override
    public String getAppKey() {
        return null;
    }

    @Override
    public void setBirthday(long l) {

    }

    @Override
    public void setNoDisturb(int i, BasicCallback basicCallback) {

    }

    @Override
    public void removeFromFriendList(BasicCallback basicCallback) {

    }

    @Override
    public void updateNoteName(String s, BasicCallback basicCallback) {

    }

    @Override
    public void updateNoteText(String s, BasicCallback basicCallback) {

    }

    @Override
    public String getDisplayName() {
        return null;
    }
}

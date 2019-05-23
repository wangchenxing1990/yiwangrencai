package com.yiwangrencai.ywkj.jmessage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.fragment.ConversationListFragment;


/**
 * Created by ${chenyn} on 2017/3/13.
 */

public class ConversationListView {
    private View mConvListFragment;
    private ListView mConvListView = null;
    private TextView mTitle;
    private ImageButton mCreateGroup;
    private LinearLayout mSearchHead;
    private RelativeLayout mHeader;
    private RelativeLayout mLoadingHeader;
    private ImageView mLoadingIv;
    private LinearLayout mLoadingTv;
    private Context mContext;
    private LinearLayout mNull_conversation;
    private LinearLayout mSearch;
    private TextView mAllUnReadMsg;
    private ConversationListFragment mFragment;
    private RelativeLayout relative_message;
    private TextView text_bags;

    public ConversationListView(View view, Context context, ConversationListFragment fragment) {
        this.mConvListFragment = view;
        this.mContext = context;
        this.mFragment = fragment;
    }

    public void initModule() {
        mConvListView = (ListView) mConvListFragment.findViewById(R.id.conv_list_view);
        mCreateGroup = (ImageButton) mConvListFragment.findViewById(R.id.create_group_btn);
        relative_message = (RelativeLayout) mConvListFragment.findViewById(R.id.relative_message);
        text_bags = (TextView) mConvListFragment.findViewById(R.id.text_bags);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //    mHeader = (RelativeLayout) inflater.inflate(R.layout.conv_list_head_view, mConvListView, false);
        //  mSearchHead = (LinearLayout) inflater.inflate(R.layout.conversation_head_view, mConvListView, false);
        //  mLoadingHeader = (RelativeLayout) inflater.inflate(R.layout.jmui_drop_down_list_header, mConvListView, false);
        //  mLoadingIv = (ImageView) mLoadingHeader.findViewById(R.id.jmui_loading_img);
        //  mLoadingTv = (LinearLayout) mLoadingHeader.findViewById(R.id.loading_view);
        //  mSearch = (LinearLayout) mSearchHead.findViewById(R.id.search_title);
        mNull_conversation = (LinearLayout) mConvListFragment.findViewById(R.id.null_conversation);
        // mAllUnReadMsg = (TextView) mFragment.getActivity().findViewById(R.id.all_unread_number);
//        mConvListView.addHeaderView(mLoadingHeader);
        //   mConvListView.addHeaderView(mHeader);
//        mConvListView.addHeaderView(mSearchHead);
        SharedPreferences shared = mFragment.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int badges = shared.getInt("badges", 0);
        if (badges == 0) {
            text_bags.setText("没有面试通知");
        } else {
            text_bags.setText(badges + "条面试通知");
        }

    }

    public void setConvListAdapter(ListAdapter adapter) {
        mConvListView.setAdapter(adapter);
    }


    public void setListener(View.OnClickListener onClickListener) {
        relative_message.setOnClickListener(onClickListener);
        mCreateGroup.setOnClickListener(onClickListener);
    }

    public void setItemListeners(AdapterView.OnItemClickListener onClickListener) {
        mConvListView.setOnItemClickListener(onClickListener);
    }

    public void setLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mConvListView.setOnItemLongClickListener(listener);
    }


    public void showHeaderView() {
//        mHeader.findViewById(R.id.network_disconnected_iv).setVisibility(View.VISIBLE);
//        mHeader.findViewById(R.id.check_network_hit).setVisibility(View.VISIBLE);
    }

    public void dismissHeaderView() {
//        mHeader.findViewById(R.id.network_disconnected_iv).setVisibility(View.GONE);
//        mHeader.findViewById(R.id.check_network_hit).setVisibility(View.GONE);
    }


    public void showLoadingHeader() {
        mLoadingIv.setVisibility(View.VISIBLE);
        mLoadingTv.setVisibility(View.VISIBLE);
        AnimationDrawable drawable = (AnimationDrawable) mLoadingIv.getDrawable();
        drawable.start();
    }

    public void dismissLoadingHeader() {
        mLoadingIv.setVisibility(View.GONE);
        mLoadingTv.setVisibility(View.GONE);
    }

    public void setNullConversation(boolean isHaveConv) {
        if (isHaveConv) {
            mNull_conversation.setVisibility(View.GONE);
        } else {
            mNull_conversation.setVisibility(View.VISIBLE);
        }
    }


    public void setUnReadMsg(final int count) {
        ThreadUtil.runInUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAllUnReadMsg != null) {
                    if (count > 0) {
                        mAllUnReadMsg.setVisibility(View.VISIBLE);
                        if (count < 100) {
                            mAllUnReadMsg.setText(count + "");
                        } else {
                            mAllUnReadMsg.setText("99+");
                        }
                    } else {
                        mAllUnReadMsg.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


}

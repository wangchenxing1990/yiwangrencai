package com.yiwangrencai.ywkj.jmessage;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.InterviewActivity;
import com.yiwangrencai.ywkj.activity.MainActivity;
import com.yiwangrencai.ywkj.fragment.ConversationListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * Created by ${chenyn} on 2017/2/20.
 */

public class ConversationListController implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ConversationListView mConvListView;
    private ConversationListFragment mContext;
    private int mWidth;
    private ConversationListAdapter mListAdapter;
    private List<Conversation> mDatas = new ArrayList<Conversation>();
    private List<Conversation> mConv = new ArrayList<Conversation>();
    private Dialog mDialog;

    public ConversationListController(ConversationListView listView, ConversationListFragment context,
                                      int width) {
        this.mConvListView = listView;
        this.mContext = context;
        this.mWidth = width;
        initConvListAdapter();
    }

    //得到会话列表
    private void initConvListAdapter() {
        mDatas = JMessageClient.getConversationList();
        Log.i("mDatasmDatasmDatas",mDatas+"   111111111111");
        if (mDatas != null && mDatas.size() > 0) {
            mConvListView.setNullConversation(true);
            SortConvList sortConvList = new SortConvList();
            Collections.sort(mDatas, sortConvList);
         /*   for (int i = 0; i < SharePreferenceManager.getTopSize(); i++) {
                ConversationEntry topConversation = ConversationEntry.getTopConversation(i);
                for (int x = 0; x < mDatas.size(); x++) {
                    if (topConversation.targetname.equals(mDatas.get(x).getTargetId())) {
                        mConv.add(mDatas.get(x));
                        mDatas.remove(mDatas.get(x));

                        mDatas.add(i, mConv.get(0));
                        mConv.clear();
                        break;

                    }
                }
            }*/
        } else {
            mConvListView.setNullConversation(false);
        }
        mListAdapter = new ConversationListAdapter(mContext.getActivity(), mDatas, mConvListView);
        mConvListView.setConvListAdapter(mListAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.create_group_btn:
//             //   mContext.showPopWindow();
//                break;
            case R.id.relative_message:
//                MainActivity mainActivity= (MainActivity) mContext.getActivity();
//                mainActivity.showContacts();
                Intent intent = new Intent();
                intent.setClass(mContext.getActivity(), InterviewActivity.class);
                intent.putExtra("register","message");
                mContext.startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击会话条目
        Intent intent = new Intent();
        if (position >=0) {
            //这里-3是减掉添加的三个headView

            Conversation conv = mDatas.get(position);
            intent.putExtra(JGApplication.CONV_TITLE, conv.getTitle());
            UserInfo userInfos= (UserInfo) mDatas.get(position).getTargetInfo();
            Log.i("convconv",mDatas.get(position).getTargetInfo()+"::convconvconv");
          //  Toast.makeText(mContext.getActivity(),"aaaaaa   "+position,Toast.LENGTH_SHORT).show();
            //群聊
            if (conv.getType() == ConversationType.group) {
                if (mListAdapter.includeAtMsg(conv)) {
                    intent.putExtra("atMsgId", mListAdapter.getAtMsgId(conv));
                }

                if (mListAdapter.includeAtAllMsg(conv)) {
                    intent.putExtra("atAllMsgId", mListAdapter.getatAllMsgId(conv));
                }
                long groupId = ((GroupInfo) conv.getTargetInfo()).getGroupID();
                intent.putExtra(JGApplication.GROUP_ID, groupId);
                intent.putExtra(JGApplication.DRAFT, getAdapter().getDraft(conv.getId()));

                intent.setClass(mContext.getActivity(), ChatActivity.class);
                mContext.getActivity().startActivity(intent);
                mListAdapter.notifyDataSetChanged();
                return;
                //单聊
            } else {
                String targetId = ((UserInfo) conv.getTargetInfo()).getUserName();
                String nickname = ((UserInfo) conv.getTargetInfo()).getNickname();
              //  Log.i("nicknamenickname","nickname::"+nickname);
                intent.putExtra(JGApplication.TARGET_ID, targetId);
                intent.putExtra(JGApplication.CONV_TITLE, nickname);
                intent.putExtra(JGApplication.TARGET_APP_KEY, conv.getTargetAppKey());
                intent.putExtra("com_id", userInfos.getRegion());
                intent.putExtra(JGApplication.DRAFT, getAdapter().getDraft(conv.getId()));
            }
            intent.setClass(mContext.getActivity(), ChatActivity.class);
            mContext.getContext().startActivity(intent);

        }
    }

    public ConversationListAdapter getAdapter() {
        return mListAdapter;
    }
}

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//        final Conversation conv = mDatas.get(position - 1);
//        if (conv != null) {
//            View.OnClickListener listener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    switch (v.getId()) {
                        //会话置顶
                        /*case R.id.jmui_top_conv_ll:
                            mListAdapter.setConvTop(conv);
                            int topSize = SharePreferenceManager.getTopSize();
                            ConversationEntry entry = new ConversationEntry(conv.getTargetId(), topSize);
                            entry.save();
                            ++topSize;
                            SharePreferenceManager.setTopSize(topSize);
                            mDialog.dismiss();
                            break;*/
                        //删除会话
//                        case R.id.jmui_delete_conv_ll:
//                            if (conv.getType() == ConversationType.group) {
//                                JMessageClient.deleteGroupConversation(((GroupInfo) conv.getTargetInfo()).getGroupID());
//                            } else {
//                                JMessageClient.deleteSingleConversation(((UserInfo) conv.getTargetInfo()).getUserName());
//                            }
//                            mDatas.remove(position - 3);
//                            if (mDatas.size() > 0) {
//                                mConvListView.setNullConversation(true);
//                            } else {
//                                mConvListView.setNullConversation(false);
//                            }
//                            mListAdapter.notifyDataSetChanged();
//                            mDialog.dismiss();
//                            break;
//                        default:
//                            break;
//                    }
//
//                }
//            };
//            mDialog = DialogCreator.createDelConversationDialog(mContext.getActivity(), listener);
//            mDialog.show();
//            mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
//        }
//        return true;
//    }


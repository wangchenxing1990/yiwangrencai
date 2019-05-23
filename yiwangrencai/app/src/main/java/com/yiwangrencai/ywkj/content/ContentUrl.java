package com.yiwangrencai.ywkj.content;



/**
 * Created by Administrator on 2017/4/5.
 */

public class ContentUrl {
    public final static String ACCEPT = "Accept";
    public final static String APPJSON = "application/json";
    public final static String AUTHORIZATION = "Authorization";
    public final static String BEAR = "Bearer ";
    public final static String PHONE_PARTTEN = "^1\\d{10}$";
    //所有的网络连接中的基本的那个URL
//      public final static String BASE_URL = "http://192.168.1.123:1024/api/v1/";
//      public final static String BASE_ICON_URL = "http://192.168.1.123:1024/upload/";
//      public final static String BASE_URL_SHORT = "http://192.168.1.123:1024/";

    //  public final static String BASE_URL = "http://192.168.1.110:1025/api/v1/";
    //  public final static String BASE_ICON_URL = "http://192.168.1.110:1025/upload/";
    //  public final static String BASE_URL_SHORT = "http://192.168.1.110:1025/";

       //所有的网络连接中的图片的基本路
     public final static String BASE_URL = "https://www.15hr.com/api/v1/";
     public final static String BASE_URL_SHORT = "https://www.15hr.com/";
     public final static String BASE_ICON_URL = "https://www.15hr.com/upload/";

     public final static String BASE_URL_SHORT_CODE = "15hr.com";
    //首页的URL
    public final static String HOME_URL = "user/index";
    //发送验证码
    public final static String SEND_CODE = "user/smscode";
    //*编辑工作经验：resume_workexp/edit
    public final static String RESUME_WORKEXP_EDIT="resume_workexp/edit";
    //注册
    public final static String REGISTER_USER = "user/register";
    //*编辑简历基本信息：resume_data/edit
    public final static String RESUME_DATA_EDIT = "resume_data/edit";
    //*更新简历基本信息：resume_data/update
    public final static String RESUME_DATA_UPDATE = "resume_data/update";
    //职场资讯的基础接口
    public final static String CAREER_INFORMATION = "news/category";
    //用户登录接口
    public final static String USER_LOGIN = "user/login";
    //创建简历的接口
    public final static String ADD_RESUME = "resume_info/create";
    //地域的接口
    public final static String AREA_CODE = "webareacode";
    //简历基本信息
    public final static String RESUME_INFO = "resume_info/create";
    //更新简历意向
    public final static String RESUME_INTENT = "resume_intent/update";
    //添加教育经验接口
    public final static String RESUME_EDUEXP = "resume_eduexp/create";
    //添加工作经验接口
    public final static String RESUME_WORKEXP = "resume_workexp/create";
    //*添加工作经验：resume_workexp/create
    public final static String RESUME_WORKEXP_CREATE = "resume_workexp/create";
    //*更新工作经验：resume_workexp/update
    public final static String RESUME_WORKEXP_UPDATE = "resume_workexp/update";
    //*编辑简历意向：resume_intent/edit
    public final static String RESUME_INTENT_EDIT = "resume_intent/edit";
    //简历详情
    public final static String RESUME_SHOW = "resume/show";
    //更新联系方式
    public final static String RESUME_CONN = "resume_conn/update";
    //更新自我介绍
    public final static String RESUME_INTRO = "resume_intro/update";
    //添加项目经验
    public final static String RESUME_PROEXP = "resume_proexp/create";
    //添加语言能力
    public final static String RESUME_LANG = "resume_lang/create";
    //编辑技能 resume_skill/edit
    public final static String RESUME_SKILL = "resume_skill/edit";
    //语言等级：option/second
    public final static String OPTION_SECOND = "option/second";
    //添加语言能力：resume_lang/create
    public final static String RESUME_LANG_CREATE = "resume_lang/create";
    //*添加技能：resume_skill/create
    public final static String RESUME_SKILL_CREATE = "resume_skill/create";
    //*添加证书：resume_cer/create
    public final static String RESUME_CER_CREATE = "resume_cer/create";
    //*添加其他信息：resume_other/create
    public final static String RESUME_OTHER_CREATE = "resume_other/create";
    //*删除工作经验：resume_workexp/delete
    public final static String RESUME_WORKEXP_DELETE = "resume_workexp/delete";
    //*编辑教育经验：resume_eduexp/edit
    public final static String RESUME_EDUEXP_EDIT = "resume_eduexp/edit";
    // *删除教育经验：resume_eduexp/delete
    public final static String RESUME_EDUEXP_DELETE = "resume_eduexp/delete";
    //*更新教育经验：resume_eduexp/update
    public final static String RESUME_EDUEXP_UPDATE = "resume_eduexp/update";
    //*编辑项目经验：resume_proexp/edit
    public final static String RESUME_PROEXP_EDIT = "resume_proexp/edit";
    //*更新项目经验：resume_proexp/update
    public final static String RESUME_PROEXP_UPDATE = "resume_proexp/update";
    //*删除项目经验：resume_proexp/delete
    public final static String RESUME_PROEXP_DELETE = "resume_proexp/delete";
    //*编辑语言能力：resume_lang/edit
    public final static String RESUME_LANG_EDIT = "resume_lang/edit";
    //*更新语言能力：resume_lang/update
    public final static String RESUME_LANG_UPDATE = "resume_lang/update";
    //*删除语言能力信息：resume_lang/delete
    public final static String RESUME_LANG_DELETE = "resume_lang/delete";
    //*编辑技能：resume_skill/edit
    public final static String RESUME_SKILL_EDIT = "resume_skill/edit";
    //qq登录：user/qqlogin
    public final static String USER_QQLOGIN = "user/qqlogin";
    //微信登录：user/wxlogin
    public final static String USER_WXLOGIN = "user/wxlogin";
    //*更新技能：resume_skill/update
    public final static String RESUME_SKILL_UPDATE = "resume_skill/update";
    //*删除技能信息：resume_skill/delete
    public final static String RESUME_SKILL_DELETE = "resume_skill/delete";
    //*编辑证书信息：resume_ cer/edit
    public final static String RESUME_CER_EDIT = "resume_cer/edit";
    //*更新证书：resume_ cer/update
    public final static String RESUME_CER_UPDATE = "resume_cer/update";
    //*删除证书信息：resume_ cer/delete
    public final static String RESUME_CER_DELETE = "resume_cer/delete";
    //*编辑其他信息：resume_ other/edit
    public final static String RESUME_OTHER_EDIT = "resume_other/edit";
    //*更新其他信息：resume_ other/update
    public final static String RESUME_OTHER_UPDATE = "resume_other/update";
    //*删除其他信息：resume_ other/delete
    public final static String RESUME_OTHER_DELETE = "resume_other/delete";
    //*更新简历状态：resume/status
    public final static String RESUME_STATUS = "resume/status";
    //*更新简历时间：resume/refresh
    public final static String RESUME_REFRESH = "resume/refresh";
    //*搜索公司名：shield/search(分页)
    public final static String SHIELD_SEARCH = "shield/search";
    //*公司黑名单列表：shield
    public final static String SHIELD = "shield";
    //职位详情：company_job/show
    public final static String COMPANY_JOB_SHOW = "company_job/show";
    //公司详情：company_basic/show
    public final static String COMPANY_BASIC_SHOW = "company_basic/show";
    //*收藏职位：favorites/create
    public final static String FAVORITES_CREATE = "favorites/create";
    //*投递简历：resume_send/create
    public final static String RESUME_SEND_CREATE = "resume_send/create";
    //公司所有职位：company_job(分页)
    public final static String COMPANY_JOB = "company_job";
    public final static String COMPANY_JOB_LIST = "company_job/per_list";
    //*修改密码：user/password_reset
    public final static String USER_PASSWORD_RESET = "user/password_reset";
    //*投递简历列表：resume_send
    public final static String RESUME_SEND = "resume_send";
    //*面试通知列表: interview_receive
    public final static String INTERVIEW_RECEIVE = "interview_receive";
    //*收藏职位列表：favorites(分页)
    public final static String FAVORITES = "favorites";
    //*公司浏览记录：user/browse/com(分页)
    public final static String USER_BROWSE_COM = "user/browse/com";
    //*职位浏览记录：user/browse/job(分页)
    public final static String USER_BROWSE_JOB = "user/browse/job";
    //*搜索职位：company_job/search(分页)
    public final static String COMPANY_JOB_SEARCH = "company_job/search";
    //*附近职位：user/near_job(分页)
    public final static String USER_NEAR_JOB = "user/near_job";
    //*推荐职位：user/commend(分页)
    public final static String USER_COMMEND = "user/commend";
    //*删除公司黑名单：shield/delete
    public final static String SHIELD_DELETE = "shield/delete";
    //*添加公司黑名单：shield/create
    public final static String SHIELD_CREATE = "shield/create";
    //资讯列表：news(分页)
    public final static String NEWS = "news";
    //*搜索器列表：search_engine(分页)
    public final static String SEARCH_ENGINE = "search_engine";
    //*添加搜索器：search_engine/create
    public final static String SEARCH_ENGINE_CREATE = "search_engine/create";
    //*编辑搜索器：search_engine/edit
    public final static String SEARCH_ENGINE_EDIT = "search_engine/edit";
    //*删除搜索器：search_engine/delete
    public final static String SEARCH_ENGINE_DELETE = "search_engine/delete";
    //*更新搜索器：search_engine/update
    public final static String SEARCH_ENGINE_UPDATE = "search_engine/update";
    //*撤销简历投递：resume_send/update
    public final static String RESUME_SEND_UPDATE = "resume_send/update";
    //*隐藏头像：resume/chkphoto_open
    public final static String RESUME_CHKPHOTO_OPEN = "resume/chkphoto_open";
    //*更新简历头像：resume/avatar
    public final static String RESUME_AVATAR = "resume/avatar";
    //*更新简历时间及手机和状态：resume/refresh_info
    public final static String RESUME_REFRESH_INFO = "resume/refresh_info";
    //*是否需要刷新简历时间:resume/need_refresh
    public final static String RESUME_NEED_REFRESH = "resume/need_refresh";
    //版本更新接口 user/check_version
    public final static String USER_CHECK_VERSION = "user/check_version";
    //*查看，接受，拒绝面试：interview/update
    public final static String INTERVIEW_UPDATE="interview/update";
    //*取消收藏职位：favorites/delete
    public final static String FAVORITES_DELETE="favorites/delete";
    //*创建兼职简历：part_resume/create
    public final static String PART_RESUM_CREATE="part_resume/create";
    //*编辑简历:part_resume/edit
    public final static String PART_RESUME_EDIT="part_resume/edit";
    //*更新简历:part_resume/update
    public final static String PART_RESUME_UPDATE="part_resume/update";
    //搜索兼职职位:part_job/search
    public final static String PART_JOB_SEARCH="part_job/search";
   // *报名列表:part_sign_up/(分页)
   public final static String PART_SIGN_UP="part_sign_up";
    //撤销报名记录：part_sign_up/update
    public final static String PART_SIGN_UPUPDATE="part_sign_up/update";
    //*收到兼职邀请列表：part_sign_process/receive(分页)
    public final static String PART_SIGN_PROCESS_RECEIVE="part_sign_process/receive";
    //*操作邀请记录状态：part_sign_process/update
    public final static String PART_SIGN_PROCESS_UPDATE="part_sign_process/update";
    //显示职位详情：part_job/show
    public final static String PART_JOB_SHOW="part_job/show";
    //*兼职报名: part_sign_up/create
    public final static String PART_SIGN_UP_CREATE="part_sign_up/create";

}

package com.yiwangrencai.ywkj.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class InitDatas {


    public static List<String>  edcationData(){
        List<String> educationData=new ArrayList<>();

        educationData.add("初中");
        educationData.add("高中");
        educationData.add("中专");
        educationData.add("大专");
        educationData.add("本科");
        educationData.add("硕士");
        educationData.add("博士");

        return educationData;
    }

    public static List<String>  edcationDataId(){
        List<String> educationID=new ArrayList<>();

        educationID.add("10");
        educationID.add("20");
        educationID.add("30");
        educationID.add("40");
        educationID.add("50");
        educationID.add("60");
        educationID.add("70");

        return educationID;
    }

    public static List<String > expersionData(){
        List<String>  expersionDatas=new ArrayList<String>();
        expersionDatas.add("在校学生");
        expersionDatas.add("应届毕业生");
        expersionDatas.add("1年以上");
        expersionDatas.add("2年以上");
        expersionDatas.add("3年以上");
        expersionDatas.add("4年以上");
        expersionDatas.add("5年以上");
        expersionDatas.add("6年以上");
        expersionDatas.add("7年以上");
        expersionDatas.add("8年以上");
        expersionDatas.add("9年以上");
        expersionDatas.add("10年以上");
        return expersionDatas;
    }

    public static List<String > expersionDataID(){
        List<String>  expersionDataID=new ArrayList<String>();
        expersionDataID.add("9");
        expersionDataID.add("10");
        expersionDataID.add("11");
        expersionDataID.add("12");
        expersionDataID.add("13");
        expersionDataID.add("14");
        expersionDataID.add("15");
        expersionDataID.add("16");
        expersionDataID.add("17");
        expersionDataID.add("18");
        expersionDataID.add("19");
        expersionDataID.add("20");
        return expersionDataID;
    }

    public static List<String> saralyDatas(){
       List<String> salaryDatas=new ArrayList<>();
        salaryDatas.add("面议");
        salaryDatas.add("1000");
        salaryDatas.add("1500");
        salaryDatas.add("2000");
        salaryDatas.add("2500");
        salaryDatas.add("3000");
        salaryDatas.add("3500");
        salaryDatas.add("4000");
        salaryDatas.add("5000");
        salaryDatas.add("6000");
        salaryDatas.add("7000");
        salaryDatas.add("8000");
        salaryDatas.add("9000");
        salaryDatas.add("1万");
        salaryDatas.add("1.5万");
        salaryDatas.add("2万");
        salaryDatas.add("3万");
        salaryDatas.add("4万");
        salaryDatas.add("5万");
        salaryDatas.add("10万");
        salaryDatas.add("50万");
        salaryDatas.add("100万");
        return salaryDatas;
    }

    public static List<String> saralyDataID(){
        List<String> salaryDataID=new ArrayList<>();
        salaryDataID.add("0");
        salaryDataID.add("1000");
        salaryDataID.add("1500");
        salaryDataID.add("2000");
        salaryDataID.add("2500");
        salaryDataID.add("3000");
        salaryDataID.add("3500");
        salaryDataID.add("4000");
        salaryDataID.add("5000");
        salaryDataID.add("6000");
        salaryDataID.add("7000");
        salaryDataID.add("8000");
        salaryDataID.add("9000");
        salaryDataID.add("10000");
        salaryDataID.add("15000");
        salaryDataID.add("20000");
        salaryDataID.add("30000");
        salaryDataID.add("40000");
        salaryDataID.add("50000");
        salaryDataID.add("100000");
        salaryDataID.add("500000");
        salaryDataID.add("1000000");
        return salaryDataID;
    }

    public static List<String> natrueBusiness(){
        List<String> comKindList=new ArrayList<>();
        comKindList.add("国有企业");
        comKindList.add("外资企业");
        comKindList.add("合资企业");
        comKindList.add("私营企业");
        comKindList.add("民营企业");
        comKindList.add("股份制企业");
        comKindList.add("集体企业");
        comKindList.add("集体事业");
        comKindList.add("乡镇企业");
        comKindList.add("行政机关");
        comKindList.add("社会团体");
        comKindList.add("事业单位");
        comKindList.add("跨国公司(集团)");
        comKindList.add("上市公司");
        comKindList.add("其他");
        return comKindList;
    }

    public static List<String> natrueBusinessId(){
        List<String> comKindListID=new ArrayList<>();
        comKindListID.add("1");
        comKindListID.add("2");
        comKindListID.add("3");
        comKindListID.add("4");
        comKindListID.add("5");
        comKindListID.add("6");
        comKindListID.add("7");
        comKindListID.add("8");
        comKindListID.add("9");
        comKindListID.add("10");
        comKindListID.add("11");
        comKindListID.add("12");
        comKindListID.add("13");
        comKindListID.add("14");
        comKindListID.add("20");
        return comKindListID;
    }

    public static  List<String> companyScale(){
        List<String> salaryDatas=new ArrayList<>();
        salaryDatas.add("少于50人");
        salaryDatas.add("50-200人");
        salaryDatas.add("200-500人");
        salaryDatas.add("500-1000人");
        salaryDatas.add("1000人以上");
        return salaryDatas;
    }
    public static  List<String> companyScaleID(){
        List<String> salaryDatasID=new ArrayList<>();
        salaryDatasID.add("50");
        salaryDatasID.add("200");
        salaryDatasID.add("500");
        salaryDatasID.add("1000");
        salaryDatasID.add("2000");
        return salaryDatasID;
    }

    public static  List<String> langData(){
        List<String> langDatas=new ArrayList<>();
        langDatas.add("普通话");
        langDatas.add("粤语");
        langDatas.add("英语");
        langDatas.add("日语");
        langDatas.add("法语");
        langDatas.add("德语");
        langDatas.add("阿拉伯语");
        langDatas.add("俄语");
        langDatas.add("西班牙语");
        langDatas.add("朝鲜语");
        langDatas.add("意大利语");
        langDatas.add("韩语");
        langDatas.add("葡萄牙语");
        langDatas.add("其他语种");

        return langDatas;
    }

    public static  List<String> langDataID(){
        List<String> langDatasID=new ArrayList<>();
        langDatasID.add("1");
        langDatasID.add("2");
        langDatasID.add("3");
        langDatasID.add("4");
        langDatasID.add("5");
        langDatasID.add("6");
        langDatasID.add("7");
        langDatasID.add("8");
        langDatasID.add("9");
        langDatasID.add("10");
        langDatasID.add("11");
        langDatasID.add("12");
        langDatasID.add("13");
        langDatasID.add("30");

        return langDatasID;
    }

    public static  List<String> otherData(){
        List<String> otherDatas=new ArrayList<>();

        otherDatas.add("兴趣爱好");
        otherDatas.add("宗教信仰");
        otherDatas.add("职业目标");
        otherDatas.add("获奖荣誉");
        otherDatas.add("个性特长");
        otherDatas.add("自定义");


        return otherDatas;
    }

    public static List<String> industryData(){
        List<String> industryDatas=new ArrayList<>();
        industryDatas.add("互联网、电子商务");
        industryDatas.add("计算机业（软件、数据库、系统集成）");
        industryDatas.add("计算机业（硬件、网络设备）");
        industryDatas.add("电子、微电子技术");
        industryDatas.add("通讯、电信网络设备业");
        industryDatas.add("家具、家电、工艺品、玩具");
        industryDatas.add("批发零售(百货、超市、购物中心、专卖店…");
        industryDatas.add("贸易、商务、进出口");
        industryDatas.add("电气");
        industryDatas.add("电力、能源、矿产业");
        industryDatas.add("石油、化工业");
        industryDatas.add("生物工程、制药、环保");
        industryDatas.add("机械制造、机电设备、重工业");
        industryDatas.add("汽车、摩托车及配件业");
        industryDatas.add("仪器仪表、电工设备");
        industryDatas.add("广告、公关、设计");
        industryDatas.add("艺术、文化传播");
        industryDatas.add("快速消费品（食品、饮料、粮油、化妆品、烟）");
        industryDatas.add("纺织品业（服饰、鞋类、家纺用品、皮具…）");
        industryDatas.add("咨询业（顾问、会计师、审计师、法律）");
        industryDatas.add("金融业（投资、保险、证券、银行、基金）");
        industryDatas.add("建筑、装潢");
        industryDatas.add("运输、物流、快递");
        industryDatas.add("旅游业、酒店");
        industryDatas.add("办公设备、文化体育休闲用品");
        industryDatas.add("印刷、包装、造纸");
        industryDatas.add("生产、制造、修饰加工");
        industryDatas.add("教育、培训、科研院所");
        industryDatas.add("医疗、美容保健、卫生服务");
        industryDatas.add("人才交流、中介");
        industryDatas.add("协会、社团");
        industryDatas.add("农、林、牧、副、渔业");
        industryDatas.add("法律");
        industryDatas.add("通信、电信运营、增值服务业");
        industryDatas.add("媒体、影视制作、新闻出版");
        industryDatas.add("房地产、物业管理");
        industryDatas.add("餐饮、娱乐");
        industryDatas.add("政府公用事业、社区服务");
        industryDatas.add("其他");
        return industryDatas;
    }

    public static List<String> industryDataId(){
        List<String> industryDataID=new ArrayList<>();
        industryDataID.add("1");
        industryDataID.add("2");
        industryDataID.add("3");
        industryDataID.add("4");
        industryDataID.add("5");
        industryDataID.add("6");
        industryDataID.add("7");
        industryDataID.add("8");
        industryDataID.add("9");
        industryDataID.add("10");
        industryDataID.add("11");
        industryDataID.add("12");
        industryDataID.add("13");
        industryDataID.add("14");
        industryDataID.add("15");
        industryDataID.add("16");
        industryDataID.add("17");
        industryDataID.add("18");
        industryDataID.add("19");
        industryDataID.add("20");
        industryDataID.add("21");
        industryDataID.add("22");
        industryDataID.add("23");
        industryDataID.add("24");
        industryDataID.add("25");
        industryDataID.add("26");
        industryDataID.add("27");
        industryDataID.add("28");
        industryDataID.add("29");
        industryDataID.add("30");
        industryDataID.add("31");
        industryDataID.add("32");
        industryDataID.add("33");
        industryDataID.add("34");
        industryDataID.add("35");
        industryDataID.add("36");
        industryDataID.add("37");
        industryDataID.add("38");
        industryDataID.add("39");
        return industryDataID;
    }

    public static List<String> issueTimeData(){
        List<String> issueTimeDatas=new ArrayList<>();
        issueTimeDatas.add("1天内");
        issueTimeDatas.add("3天内");
        issueTimeDatas.add("一周内");
        issueTimeDatas.add("半月内");
        issueTimeDatas.add("一个月内");
        issueTimeDatas.add("三个月内");
        issueTimeDatas.add("半年内");
        issueTimeDatas.add("一年内");
        return issueTimeDatas;
    }

    public static List<String> issueTimeDataId(){
        List<String> issueTimeDataId=new ArrayList<>();
        issueTimeDataId.add("1");
        issueTimeDataId.add("3");
        issueTimeDataId.add("7");
        issueTimeDataId.add("15");
        issueTimeDataId.add("30");
        issueTimeDataId.add("90");
        issueTimeDataId.add("183");
        issueTimeDataId.add("365");
        return issueTimeDataId;
    }

    public static List<String> saralyDatass(){
        List<String> saralyDatass=new ArrayList<>();
        saralyDatass.add("不限");
        saralyDatass.add("2000以下");
        saralyDatass.add("2000-4000");
        saralyDatass.add("4000-6000");
        saralyDatass.add("6000-8000");
        saralyDatass.add("8000-1万");
        saralyDatass.add("1万-1.5万");
        saralyDatass.add("1.5万-2万");
        saralyDatass.add("2万-3万");
        saralyDatass.add("3万-4万");
        saralyDatass.add("4万-5万");
        return saralyDatass;
    }

    public static List<String> saralyDatassId(){
        List<String> saralyDatassId=new ArrayList<>();
        saralyDatassId.add("1");
        saralyDatassId.add("2");
        saralyDatassId.add("3");
        saralyDatassId.add("4");
        saralyDatassId.add("5");
        saralyDatassId.add("6");
        saralyDatassId.add("7");
        saralyDatassId.add("8");
        saralyDatassId.add("9");
        saralyDatassId.add("10");
        saralyDatassId.add("11");
        return saralyDatassId;
    }

    public static List<String> ageDatas(){
        List<String> ageDatas=new ArrayList<>();

        for (int i=0;i<44;i++){
            if (i==0){
                ageDatas.add("年龄不限");
            }else{
                ageDatas.add(17+i+"");
            }

        }
        return ageDatas;
    }

    public static List<String> ageDatasId(){
        List<String> ageDatasId=new ArrayList<>();
        ageDatasId.add("0");
        for (int i=0;i<44;i++){
            if (i==0){
                ageDatasId.add("0");
            }else{
                ageDatasId.add(17+i+"");
            }

        }
        return ageDatasId;
    }

    public static List<String> initdataSearch(){
        List<String> initdataSearch=new ArrayList<>();
        initdataSearch.add("销售");
        initdataSearch.add("客户服务");
        initdataSearch.add("会计");
        initdataSearch.add("出纳");
        initdataSearch.add("行政");
        initdataSearch.add("平面设计");
        initdataSearch.add("置业顾问");
        initdataSearch.add("市场");
        initdataSearch.add("前台");
        initdataSearch.add("文员");
        initdataSearch.add("人力资源");
        initdataSearch.add("软件开发");
        initdataSearch.add("策划");
        initdataSearch.add("服务员");
        initdataSearch.add("技术支持");
        initdataSearch.add("编辑");
        initdataSearch.add("驾驶员");
        initdataSearch.add("项目管理");
        initdataSearch.add("施工");
        initdataSearch.add("普工");
        initdataSearch.add("电话营销");
        initdataSearch.add("网络推广");
        initdataSearch.add("教师");
        initdataSearch.add("美工");
        return initdataSearch;
    }

    public static List<String> initdataSearchCompany(){
        List<String> initdataSearch=new ArrayList<>();
        initdataSearch.add("圣华家具");
        initdataSearch.add("乐购汽车");
        initdataSearch.add("广洋泵业");
        initdataSearch.add("人民泵业");
        initdataSearch.add("普轩特泵业");
        initdataSearch.add("颐顿机电");
        initdataSearch.add("开元酒业");
        initdataSearch.add("普济生物");
        initdataSearch.add("易车保");
        initdataSearch.add("勃森灯饰");
        initdataSearch.add("大元泵业");
        initdataSearch.add("讴歌泳乐汇");
        initdataSearch.add("申华日化");
        initdataSearch.add("安诚财产");
        initdataSearch.add("脐带血造血干细胞");
        initdataSearch.add("中国平安人寿");
        initdataSearch.add("顾家家居");
        initdataSearch.add("雅格品牌");
        initdataSearch.add("乐蛙泵业");
        initdataSearch.add("大溪小天使幼儿园");
        initdataSearch.add("路桥区公路管理局");
        initdataSearch.add("九创精密机械");
        initdataSearch.add("福正百货");
        initdataSearch.add("乐沃家具");
        initdataSearch.add("步步乐箱包");
        initdataSearch.add("永美科技");
        initdataSearch.add("东方州强塑模");
        initdataSearch.add("佰仕德厨柜");
        return initdataSearch;
    }

    public static List<String> partJobDatas(){
        List<String> partJob=new ArrayList<>();
        partJob.add("不限");
        partJob.add("传单/举牌");
        partJob.add("促销/导购");
        partJob.add("销售/业务");
        partJob.add("服务员/店员");
        partJob.add("礼仪/模特");
        partJob.add("文员/客服");
        partJob.add("快递/配送");
        partJob.add("老师/家教");
        partJob.add("美工/设计");
        partJob.add("技工/普工");
        partJob.add("会计/出纳");
        partJob.add("活动执行/安保");
        partJob.add( "其它");
        return partJob;
    }

    public static List<String> partJobDatasId(){
        List<String> partJobId=new ArrayList<>();
        partJobId.add("0");
        partJobId.add("11");
        partJobId.add("12");
        partJobId.add("13");
        partJobId.add("14");
        partJobId.add("15");
        partJobId.add("16");
        partJobId.add("17");
        partJobId.add("18");
        partJobId.add("19");
        partJobId.add("20");
        partJobId.add("21");
        partJobId.add("22");
        partJobId.add("23");
        return partJobId;
    }
}

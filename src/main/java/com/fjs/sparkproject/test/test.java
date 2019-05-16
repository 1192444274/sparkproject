package com.fjs.sparkproject.test;

import com.fjs.sparkproject.dao.*;
import com.fjs.sparkproject.dao.factory.DAOFactory;
import com.fjs.sparkproject.domain.*;
import com.fjs.sparkproject.util.DateUtils;
import com.fjs.sparkproject.util.StringUtils;
import org.apache.spark.sql.catalyst.expressions.Rand;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class test {

    @Test
    public void sqlOperator() throws ParseException {

        DateUtils.getNDate(DateUtils.getTodayDate(),-7);
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        Random random = new Random();
        int u = 1;
        UserRetention userRetention;
        UserRetentionDAO userRetentionDAO = DAOFactory.getUserRetentionDAO();
        for (int i=0;i<=50;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(),-10+i);
            int originalNum = random.nextInt(500)+100;
            int retentionNum = originalNum;
            for (int k=0;k<3;k++){
                int websiteId = 1;
                int keepTypeId = k+1;
                List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
                dimensionIds.remove(0);
                for (Integer dimensionId1:dimensionIds){
                    List<DimensionInformation> dimensionInformationIds1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId1);
                    for (DimensionInformation dimensionInformation1:dimensionInformationIds1){
                        int dimensionInformationId1 = dimensionInformation1.getDimensionInformationId();
                        for (Integer dimensionId2:dimensionIds){
                            if (dimensionId1<dimensionId2){
                                List<DimensionInformation> dimensionInformationIds2 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId2);
                                for (DimensionInformation dimensionInformation2:dimensionInformationIds2){
                                    int dimensionInformationId2 = dimensionInformation2.getDimensionInformationId();
                                    int originalNum1 = random.nextInt(2*originalNum/dimensionInformationIds1.size()/dimensionInformationIds2.size());
                                    if (originalNum1<13){
                                        originalNum1 = originalNum1 + 7;
                                    }
                                    retentionNum = originalNum1;
                                    for (int j=0;j<7;j++){
                                        String endDate = DateUtils.getNDate(startDate,j+1);
                                        retentionNum = random.nextInt(retentionNum);
                                        if (retentionNum<3&&j<6){
                                            retentionNum = retentionNum + 2;
                                        }
                                        System.out.println(u++ + " " + startDate + " " + endDate + " " + dimensionId1 + " " + dimensionInformationId1 + " " + dimensionId2 + " " + dimensionInformationId2 + " " + originalNum1 + " " +  retentionNum);
                                        userRetention = new UserRetention(DateUtils.parseTime(startDate + " 00:00:00"),DateUtils.parseTime(endDate + " 00:00:00"),websiteId,keepTypeId,dimensionId1
                                                ,dimensionInformationId1,dimensionId2,dimensionInformationId2,originalNum1,retentionNum);
                                        userRetentionDAO.insert(userRetention);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void abc() throws ParseException {
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        UserRetentionDAO userRetentionDAO = DAOFactory.getUserRetentionDAO();
        List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
        dimensionIds.remove(0);
        UserRetention userRetention;
        int u = 0;
        Random random = new Random();
        for (int dimensionId:dimensionIds){
            List<DimensionInformation> dimensionInformationIds = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId);
            for (DimensionInformation dimensionInformation:dimensionInformationIds){
                int dimensionInformationId = dimensionInformation.getDimensionInformationId();
                for (int i=0;i<=50;i++){
                    String startDate = DateUtils.getNDate(DateUtils.getTodayDate(),-10+i);
                    for (int j=0;j<7;j++){
                        String endDate = DateUtils.getNDate(startDate,j+1);
                        for (int k=0;k<3;k++){
                            int websiteId = 1;
                            int keepTypeId = k+1;
                            int originalNum = userRetentionDAO.getOrginalSumByCondition(dimensionId,dimensionInformationId,DateUtils.parseTime(startDate + " 00:00:00")
                                    ,DateUtils.parseTime(endDate + " 00:00:00"),keepTypeId);
                            originalNum = originalNum/5;
                            int sum = userRetentionDAO.getRerentionSumByCondition(dimensionId,dimensionInformationId,DateUtils.parseTime(startDate + " 00:00:00")
                            ,DateUtils.parseTime(endDate + " 00:00:00"),keepTypeId);
                            sum = sum/5;
                            if (sum > originalNum){
                                sum = originalNum - random.nextInt(100);
                            }
                            System.out.println(++u + " " + startDate + " " + endDate + " " + dimensionId + " " + dimensionInformationId + " " + originalNum + " " +  sum);
                            userRetention = new UserRetention(DateUtils.parseTime(startDate + " 00:00:00"),DateUtils.parseTime(endDate + " 00:00:00"),websiteId,keepTypeId,dimensionId
                                    ,dimensionInformationId,null,null,originalNum,sum);
                            userRetentionDAO.insert(userRetention);
                        }
                    }

                }
            }
        }
    }

    @Test
    public void abc1() throws ParseException {
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        UserRetentionDAO userRetentionDAO = DAOFactory.getUserRetentionDAO();
        List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
        dimensionIds.remove(0);
        UserRetention userRetention;
        int u = 1;
        Random random = new Random();
        for (int i=0;i<=50;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(),-10+i);
            for (int j=0;j<7;j++){
                String endDate = DateUtils.getNDate(startDate,j+1);
                for (int k=0;k<3;k++){
                    int websiteId = 1;
                    int keepTypeId = k+1;
                    int originalNum = userRetentionDAO.getOrginalSumByCondition1(DateUtils.parseTime(startDate + " 00:00:00")
                            ,DateUtils.parseTime(endDate + " 00:00:00"),keepTypeId);
                    originalNum = originalNum/25;
                    int sum = userRetentionDAO.getRerentionSumByCondition1(DateUtils.parseTime(startDate + " 00:00:00")
                            ,DateUtils.parseTime(endDate + " 00:00:00"),keepTypeId);
                    sum = sum/25;
                    if (sum > originalNum){
                        sum = originalNum - random.nextInt(100);
                    }
                    System.out.println(u++ + " " + startDate + " " + endDate + " " + keepTypeId + " " + originalNum + " " +  sum);
                    userRetention = new UserRetention(DateUtils.parseTime(startDate + " 00:00:00"),DateUtils.parseTime(endDate + " 00:00:00"),websiteId,keepTypeId,null
                            ,null,null,null,originalNum,sum);
                    userRetentionDAO.insert(userRetention);
                }
            }

        }
    }


    @Test
    public void sqlOperator1() throws ParseException {

        DateUtils.getNDate(DateUtils.getTodayDate(),-7);
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        Random random = new Random();
        int u = 1;
        UserActive userActive;
        UserActiveDAO userActiveDAO = DAOFactory.getUserActiveDAO();
        for (int i=0;i<=50;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(),-10+i);
            for (int k=0;k<3;k++){
                int websiteId = 1;
                int active_type_id = k+1;
                int activeNum = random.nextInt(400)+100;
                System.out.println(u++ + " " + startDate + " "  + websiteId + " " + active_type_id +  " " + "空" + " " + "空" + " " + "空" + " " + "空" + " " +  activeNum);
                userActive = new UserActive(null,DateUtils.parseTime(startDate + " 00:00:00"),websiteId,active_type_id,null,null,null,null,
                        activeNum);
                userActiveDAO.insert(userActive);
                List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
                dimensionIds.remove(0);
                for (Integer dimensionId1:dimensionIds){
                    List<DimensionInformation> dimensionInformationIds1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId1);
                    for (DimensionInformation dimensionInformation1:dimensionInformationIds1){
                        int dimensionInformationId1 = dimensionInformation1.getDimensionInformationId();
                        int activeNum1 = random.nextInt(activeNum*2/5)+5;
                        System.out.println(u++ + " " + startDate  + " " + websiteId + " " + active_type_id +  " " + dimensionId1 + " " + dimensionInformationId1 + " " + "空" + " " + "空" + " " +  activeNum1);
                        userActive = new UserActive(null,DateUtils.parseTime(startDate + " 00:00:00"),websiteId,active_type_id,dimensionId1,dimensionInformationId1,null,null,
                                activeNum1);
                        userActiveDAO.insert(userActive);
                        for (Integer dimensionId2:dimensionIds){
                            if (dimensionId1<dimensionId2){
                                List<DimensionInformation> dimensionInformationIds2 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId2);
                                for (DimensionInformation dimensionInformation2:dimensionInformationIds2){
                                    int dimensionInformationId2 = dimensionInformation2.getDimensionInformationId();
                                    int activeNum2 = random.nextInt(activeNum1*2/5);
                                    System.out.println(u++ + " " + startDate  + " " + websiteId + " " + active_type_id +  " " + dimensionId1 + " " + dimensionInformationId1 + " " + dimensionId2 + " " + dimensionInformationId2 + " " +  activeNum2);
                                    userActive = new UserActive(null,DateUtils.parseTime(startDate + " 00:00:00"),websiteId,active_type_id,dimensionId1,dimensionInformationId1,dimensionId2,dimensionInformationId2,
                                            activeNum2);
                                    userActiveDAO.insert(userActive);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test
    public void abc2() throws ParseException {
        EventSplitAnalysisDAO eventSplitAnalysisDAO = DAOFactory.getEventSplitAnalysisDAO();
        EventSplitAnalysis eventSplitAnalysis;
        Random random = new Random();
        for (int i=1;i<=36;i++){
            String split = "" + i;
            int happentime = random.nextInt(1200)+200;
            for (int j=1;j<=36;j++){
                String split2 = split + "_" + j;
                double conversionRate = (double)random.nextInt(100)/9;
                if (j==i+1)
                    conversionRate = (double)random.nextInt(540)/18+30;
                int happentime1 = happentime*(new Double(conversionRate).intValue())/100;

                eventSplitAnalysis = new EventSplitAnalysis(null,1,split2,happentime1,conversionRate,new Date());

                System.out.println((i-1)*36+j + " " + eventSplitAnalysis.toString());

                eventSplitAnalysisDAO.insert(eventSplitAnalysis);
            }
        }
    }

    @Test
    public void abc3() throws ParseException {
        EventSimiliarityDAO eventSimiliarityDAO = DAOFactory.getEventSimiliarityDAO();
        EventSimiliarity eventSimiliarity;
        Random random = new Random();
        for (int i=1;i<=36;i++){
            int event1 = i;
            for (int j=1;j<=36;j++){
                int event2 = j;
                int similiarity = random.nextInt(100);

                eventSimiliarity = new EventSimiliarity(null,1,event1,event2,similiarity);

                System.out.println((i-1)*36+j + " " +eventSimiliarity);

                eventSimiliarityDAO.insert(eventSimiliarity);
            }
        }
    }
    @Test
    public void test1() throws ParseException {
        Random random = new Random();
        String[] s = new String[]{"登录","提交投资项目","提现","挂单","撤单","投资到期","开户成功","点击注册","注册成功","验证手机","选择账户类型","提交回访问卷","风险测评","安装数字证书","完成电话回访","设置密码","点击投资","投资成功",
                "上传身份证","实名认证","完成复审","提交其他项目","收藏股票","入门选择"};
        String[] s2 = new String[]{ "login","submit_investment_projects","withdrawl","list","canel_list","expire","open_account","click_registration","success_registration","verify_phone","choose_account_type","submit_volume","risk_assessment",
                "install_certificate","complete_telephone_callback","set_password","click_investment","investment_success"
                ,"upload_id_card","real_name_authentication","complete_review","submit_other_projects","stock_collection","entry_selection"};
        EventMessage eventMessage;
        EventMessageDAO eventMessageDAO = DAOFactory.getEventMessageDAO();
        for (int i=0;i<s.length;i++){

            String start = DateUtils.getNDate(DateUtils.getTodayDate(),-random.nextInt(5));
            String end = DateUtils.getNDate(start,random.nextInt(10)+1);
            eventMessage = new EventMessage(i+1,1,s[i],s2[i],"click",null,"这是为了记录"+s[i]+"的事件",DateUtils.parseTime(start + " " + random.nextInt(24) +
            ":" + random.nextInt(60) + ":" + random.nextInt(60)),DateUtils.parseTime(end + " " + random.nextInt(24) +
                    ":" + random.nextInt(60) + ":" + random.nextInt(60)));

            System.out.println(eventMessage);
            eventMessageDAO.insert(eventMessage);
        }

    }
    @Test
    public void ac() throws ParseException {
        String[] s = new String[]{"新访客","老访客"};
        String[] s1 = new String[]{"直接访问","外部链接","搜索引擎","其他"};
        Random random = new Random();
        int u = 1;
        PageAnalysis pageAnalysis;
        PageAnalysisDAO pageAnalysisDAO = DAOFactory.getPageAnalysisDAO();
        PageMessageDAO pageMessageDAO = DAOFactory.getPageMessageDAO();
        List<PageMessage> pageMessages = pageMessageDAO.findByWebsiteId(1);
        for (int i=0;i<=20;i++){
            Date startDate = DateUtils.parseTime(DateUtils.getNDate(DateUtils.getTodayDate(),-16+i) + " 00:00:00");
            for (PageMessage pageMessage:pageMessages){
                String url = pageMessage.getUrl();
                int browseNum = random.nextInt(5000)+1000;
                int user = browseNum - random.nextInt(500);
                int ip = browseNum - random.nextInt(500);
                int conversation = (browseNum + user)/2;
                double exit = (double) random.nextInt(20)/100;
                double conversion = 1 - exit;
                int access = random.nextInt(200);
                int entry = random.nextInt(browseNum);
                pageAnalysis = new PageAnalysis(null,url,1,null,null,browseNum,conversation,
                        startDate,user,ip,exit,conversion, access+0.0,entry);
                System.out.println(pageAnalysis);
                pageAnalysisDAO.insert(pageAnalysis);
                for (String q:s){
                    browseNum = random.nextInt(2500)+500;
                    user = browseNum - random.nextInt(250);
                    ip = browseNum - random.nextInt(250);
                    conversation = (browseNum + user)/2;
                    exit = (double) random.nextInt(20)/100;
                    conversion = 1 - exit;
                    access = random.nextInt(200);
                    entry = random.nextInt(browseNum);
                    pageAnalysis = new PageAnalysis(null,url,1,q,null,browseNum,conversation,
                            startDate,user,ip,exit,conversion, access+0.0,entry);
                    System.out.println(pageAnalysis);
                    pageAnalysisDAO.insert(pageAnalysis);
                }
                for (String q:s1){
                    browseNum = random.nextInt(1250)+250;
                    user = browseNum - random.nextInt(125);
                    ip = browseNum - random.nextInt(125);
                    conversation = (browseNum + user)/2;
                    exit = (double) random.nextInt(20)/100;
                    conversion = 1 - exit;
                    access = random.nextInt(200);
                    entry = random.nextInt(browseNum);
                    pageAnalysis = new PageAnalysis(null,url,1,null,q,browseNum,conversation,
                            startDate,user,ip,exit,conversion, access+0.0,entry);
                    System.out.println(pageAnalysis);
                    pageAnalysisDAO.insert(pageAnalysis);
                }

                for (String q:s){
                    for (String q1:s1){
                        browseNum = random.nextInt(1800)+400;
                        user = browseNum - random.nextInt(400);
                        ip = browseNum - random.nextInt(400);
                        conversation = (browseNum + user)/2;
                        exit = (double) random.nextInt(20)/100;
                        conversion = 1 - exit;
                        access = random.nextInt(200);
                        entry = random.nextInt(browseNum);
                        pageAnalysis = new PageAnalysis(null,url,1,q,q1,browseNum,conversation,
                                startDate,user,ip,exit,conversion, access+0.0,entry);
                        System.out.println(pageAnalysis);
                        pageAnalysisDAO.insert(pageAnalysis);
                    }
                }



            }
        }


    }
    @Test
    public void sqlOperator2() throws ParseException {
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        Random random = new Random();
        int u = 1;
        EventAnalysis eventAnalysis;
        EventAnalysisDAO eventAnalysisDAO = DAOFactory.getEventAnalysisDAO();
        for (int i=0;i<=20;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(),-16+i);
            for (int k=0;k<29;k++){
                int websiteId = 1;
                int eventId = k+1;
                int click = random.nextInt(2000)+1000;
                int user = click - random.nextInt(500);
                int ip = click - random.nextInt(500);
                int conversation = (click + user)/2;
                Integer money = null;
                Integer goods = null;
                if (eventId == 3||eventId == 4||eventId == 18){
                    money = random.nextInt(3000000)+100000;
                    goods = random.nextInt(10000)+1000;
                }
                eventAnalysis = new EventAnalysis(null,eventId,null,null,null,null,click,user
                ,ip,conversation,money,goods,DateUtils.parseTime(startDate + " 00:00:00"));

                System.out.println(u++ + eventAnalysis.toString());
                eventAnalysisDAO.insert(eventAnalysis);
                List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
                dimensionIds.remove(0);
                for (Integer dimensionId1:dimensionIds){
                    List<DimensionInformation> dimensionInformationIds1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId1);
                    for (DimensionInformation dimensionInformation1:dimensionInformationIds1){
                        int dimensionInformationId1 = dimensionInformation1.getDimensionInformationId();
                        int click1 = random.nextInt(click*2/5)+200;
                        int user1 = click1 - random.nextInt(100);
                        int ip1 = click1 - random.nextInt(100);
                        int conversation1 = (click1 + user1)/2;
                        if (eventId == 3||eventId == 4||eventId == 18){
                            money = random.nextInt(1000000)+40000;
                            goods = random.nextInt(4000)+400;
                        }
                        eventAnalysis = new EventAnalysis(null,eventId,dimensionId1,dimensionInformationId1,null,null,click1,user1
                                ,ip1,conversation1,money,goods,DateUtils.parseTime(startDate + " 00:00:00"));

                        System.out.println(u++ + eventAnalysis.toString());
                eventAnalysisDAO.insert(eventAnalysis);
                        for (Integer dimensionId2:dimensionIds){
                            if (dimensionId1<dimensionId2){
                                List<DimensionInformation> dimensionInformationIds2 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId2);
                                for (DimensionInformation dimensionInformation2:dimensionInformationIds2){
                                    int dimensionInformationId2 = dimensionInformation2.getDimensionInformationId();
                                    int click2 = random.nextInt(click1*2/5)+80;
                                    int user2 = click2 - random.nextInt(40);
                                    int ip2 = click2 - random.nextInt(40);
                                    int conversation2 = (click2 + user2)/2;
                                    if (eventId == 3||eventId == 4||eventId == 18){
                                        money = random.nextInt(400000)+15000;
                                        goods = random.nextInt(1500)+150;
                                    }
                                    eventAnalysis = new EventAnalysis(null,eventId,dimensionId1,dimensionInformationId1,dimensionId2,dimensionInformationId2,click2,user2
                                            ,ip2,conversation2,money,goods,DateUtils.parseTime(startDate + " 00:00:00"));

                                    System.out.println(u++ + eventAnalysis.toString());
                                    eventAnalysisDAO.insert(eventAnalysis);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test
    public void tes(){
        for (int i = 0;i<13;i++){
            System.out.println("INSERT INTO `event_message` VALUES ( " + (i+30) + ", '1', '浏览page" + (i+5) + "', 'browse_browse_120.78.89.40:8080/buypage4"  + "', 'browse', null, '这是为了记录浏览page" + (i+5) + "的事件', '2019-04-18 12:31:13', '2019-04-21 23:30:02');");
        }
    }

    @Test
    public void tes2(){
        Random random = new Random();
        UserPortrait userPortrait;
        int userId;
        double active_depth;
        double contribution;
        double profitability;
        int j = 1;
        UserPortraitDAO userPortraitDAO = DAOFactory.getUserPortraitDAO();
        for (int i = 0;i<10000;i++){
//            userId=random.nextInt(90000000)+10000000;
//            active_depth = (double) (random.nextInt(2000)-1000)/1000;
//            contribution = (double) (random.nextInt(2000)-1000)/1000;
//            profitability = (double) (random.nextInt(20000)-7000)/100;
//            userPortrait = new UserPortrait(null,userId,active_depth,contribution,profitability);
//            System.out.println(j++ + " " + userPortrait.toString());
//            userPortraitDAO.insert(userPortrait);
            System.out.println(userPortraitDAO.query(i+1).toString());
        }
    }

    @Test
    public void sqlOperator5() throws ParseException {

        DateUtils.getNDate(DateUtils.getTodayDate(),-9);
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        Random random = new Random();
        int u = 1;
        IntervalAnalysis intervalAnalysis;
        IntervalAnalysisDAO intervalAnalysisDAO = DAOFactory.getIntervalTimeAnalysisDAO();
        List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
        dimensionIds.remove(0);
        for (int i=0;i<=5;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(),-10+i);
            for (int k=0;k<12;k++){
                int eventid1 = k+1;
                for (int j=0;j<12;j++){
                    int eventid2 = j+1;
                    double median = (((double)random.nextInt(10)+1)/10)*(random.nextInt(1000)+100)+(double)random.nextInt(10)/10;
                    double av = median + random.nextInt(10)-10;
                    if (av<1){
                        av = av + 5;
                    }
                    double big = median + av + random.nextInt(100);
                    double small = av - random.nextInt(new Double(av).intValue());
                    int num = random.nextInt(10000)+1000;
                    intervalAnalysis = new IntervalAnalysis(null,eventid1,eventid2,null,null,
                            null,null,median,av,big,small,num,DateUtils.parseTime(startDate + " 00:00:00"));
                    System.out.println(intervalAnalysis);
                    intervalAnalysisDAO.insert(intervalAnalysis);
                    for (Integer dimensionId1:dimensionIds){
                        List<DimensionInformation> dimensionInformationIds1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId1);
                        for (DimensionInformation dimensionInformation1:dimensionInformationIds1){
                            int dimensionInformationId1 = dimensionInformation1.getDimensionInformationId();
                             median = ((double)(random.nextInt(10)+1)/10)*(random.nextInt(1000)+100)+(double)random.nextInt(10)/10;
                             av = median + random.nextInt(10)-10;
                            if (av<1){
                                av = av + 5;
                            }
                             big = median + av + random.nextInt(100);
                             small = av - random.nextInt(new Double(av).intValue());
                             num = random.nextInt(2000)+200;
                            intervalAnalysis = new IntervalAnalysis(null,eventid1,eventid2,dimensionId1,dimensionInformationId1,
                                    null,null,median,av,big,small,num,DateUtils.parseTime(startDate + " 00:00:00"));
                            System.out.println(intervalAnalysis);
                            intervalAnalysisDAO.insert(intervalAnalysis);
                            for (Integer dimensionId2:dimensionIds){
                                if (dimensionId1<dimensionId2){
                                    List<DimensionInformation> dimensionInformationIds2 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId2);
                                    for (DimensionInformation dimensionInformation2:dimensionInformationIds2){
                                        int dimensionInformationId2 = dimensionInformation2.getDimensionInformationId();
                                        median = (((double)random.nextInt(10)+1)/10)*(random.nextInt(1000)+100)+(double)random.nextInt(10)/10;
                                        av = median + random.nextInt(10)-10;
                                        if (av<1){
                                            av = av + 5;
                                        }
                                        big = median + av + random.nextInt(100);
                                        small = av - random.nextInt(new Double(av).intValue());
                                        num = random.nextInt(500)+50;
                                        intervalAnalysis = new IntervalAnalysis(null,eventid1,eventid2,dimensionId1,dimensionInformationId1,
                                                dimensionId2,dimensionInformationId2,median,av,big,small,num,DateUtils.parseTime(startDate + " 00:00:00"));
                                        System.out.println(u++ + " " + intervalAnalysis);
                                        intervalAnalysisDAO.insert(intervalAnalysis);
                                    }
                                }
                              }
                         }
                    }
                }
            }
        }
    }

    @Test
    public void sqlOperator6() throws ParseException {

        DateUtils.getNDate(DateUtils.getTodayDate(),-9);
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        Random random = new Random();
        int u = 1;
        EventParamAnalysis eventParamAnalysis;
        EventParamAnalysisDAO eventParamAnalysisDAO = DAOFactory.getEventParamAnalysisDAO();
        List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
        dimensionIds.remove(0);
        String[] types = new String[]{"账户类型"};
//        String[] contents = new String[]{"金融类","零售","贸易","计算机设备","半导体及元件","保险","传媒",
//                "电力","房地产","物流","新材料","生物制品","通信设备","养殖业","证券","中药"};
        String[] contents = new String[50];
        for (int i=0;i<50;i++){
            contents[i]="项目" + StringUtils.fulfuill(i+"");
        }
        for (int i=0;i<=5;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(),-10+i);
            int eventid = 2;
            Integer[] clicknum = new Integer[50];
            for (int k=0;k<50;k++){
                clicknum[k] = random.nextInt(400)+40;
            }
            int sum = 0;
            for (int k=0;k<clicknum.length;k++){
                sum += clicknum[k];
            }
            for (int j=0;j<50;j++){
                String type = "投资项目";
                String content = contents[j];
                double rate = (double)clicknum[j]/sum;
                eventParamAnalysis = new EventParamAnalysis(null,eventid,type,content,null,
                        null,null,null,clicknum[j],rate,DateUtils.parseTime(startDate + " 00:00:00"));
                System.out.println(eventParamAnalysis);
                eventParamAnalysisDAO.insert(eventParamAnalysis);
            }

            for (Integer dimensionId1:dimensionIds){
                List<DimensionInformation> dimensionInformationIds1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId1);
                for (DimensionInformation dimensionInformation1:dimensionInformationIds1) {
                    int dimensionInformationId1 = dimensionInformation1.getDimensionInformationId();
                    clicknum = new Integer[50];
                    for (int k=0;k<50;k++){
                        clicknum[k] = random.nextInt(100)+12;
                    }
                    sum = 0;
                    for (int k=0;k<clicknum.length;k++){
                        sum += clicknum[k];
                    }
                    for (int j=0;j<50;j++){
                        String type = "投资项目";
                        String content = contents[j];
                        double rate = (double)clicknum[j]/sum;
                        eventParamAnalysis = new EventParamAnalysis(null,eventid,type,content,dimensionId1,
                                dimensionInformationId1,null,null,clicknum[j],rate,DateUtils.parseTime(startDate + " 00:00:00"));
                        System.out.println(eventParamAnalysis);
                        eventParamAnalysisDAO.insert(eventParamAnalysis);
                    }

                    for (Integer dimensionId2:dimensionIds){
                        if (dimensionId1<dimensionId2){
                            List<DimensionInformation> dimensionInformationIds2 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId2);
                            for (DimensionInformation dimensionInformation2:dimensionInformationIds2) {
                                int dimensionInformationId2 = dimensionInformation2.getDimensionInformationId();
                                clicknum = new Integer[50];
                                for (int k=0;k<50;k++){
                                    clicknum[k] = random.nextInt(25)+2;
                                }
                                sum = 0;
                                for (int k=0;k<clicknum.length;k++){
                                    sum += clicknum[k];
                                }
                                for (int j=0;j<50;j++){
                                    String type = "投资项目";
                                    String content = contents[j];
                                    double rate = (double)clicknum[j]/sum;
                                    eventParamAnalysis = new EventParamAnalysis(null,eventid,type,content,dimensionId1,
                                            dimensionInformationId1,dimensionId2,dimensionInformationId2,clicknum[j],rate,DateUtils.parseTime(startDate + " 00:00:00"));
                                    System.out.println(u++ + " " + eventParamAnalysis);
                                    eventParamAnalysisDAO.insert(eventParamAnalysis);
                                }
                            }}
                }}
        }
    }}



    @Test
    public void tis1() throws ParseException {
        Random random = new Random();
        FunnelSplitAnalysisDAO funnelSplitAnalysisDAO = DAOFactory.getFunnelSplitAnalysisDAO();
        FunnelMessageDAO funnelMessageDAO = DAOFactory.getFunnelMessageDAO();
        List<FunnelMessage> funnelMessages = funnelMessageDAO.findAllFunnel();
        FunnelSplitAnalysis funnelSplitAnalysis;
        int u=1;
        for (int q=0;q<=10;q++) {
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(), -10 + q);
            for (int i=0;i<3;i++){
                int funnelid = i+1;
                FunnelMessage funnelMessage = funnelMessages.get(i);
                String events = funnelMessage.getEventIds();

                String[] eventids = events.split("_");
                int happentime = random.nextInt(3000)+100;
                int lasthappentime = happentime;
                for (int j=0;j<eventids.length;j++){
                    happentime = random.nextInt(lasthappentime) + 10;
                    while (happentime > lasthappentime){
                        happentime = random.nextInt(lasthappentime) + 10;
                    }
                    Double converrate = (double) happentime/lasthappentime;
                    if (j == 0){
                        converrate = null;
                    }
                    String eventSplit = "";
                    for (int k=0;k<j+1;k++){
                        eventSplit = eventSplit + eventids[k] + "_";
                    }
                    eventSplit = eventSplit.substring(0,eventSplit.length()-1);

                    funnelSplitAnalysis = new FunnelSplitAnalysis(null,funnelid,eventSplit,happentime,converrate,DateUtils.parseTime(startDate + " 00:00:00"));
                    System.out.println(u++ + " " + funnelSplitAnalysis);
                    funnelSplitAnalysisDAO.insert(funnelSplitAnalysis);
                    lasthappentime = happentime;
                }


            }
        }
    }


    @Test
    public void ti2() throws ParseException {

        Random random = new Random();
        WebsiteAnalysisDAO websiteAnalysisDAO = DAOFactory.getWebsiteAnalysisDAO();
        WebsiteAnalysis websiteAnalysis;
        DimensionDAO dimensionDAO = DAOFactory.getDimensionDAO();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        int u= 1;
        for (int i=0;i<20;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(), -29 + i);
            int websiteid=1;
            long usernum = random.nextInt(2000)+200;
            long ipnum = usernum - random.nextInt(200);
            double bounce = (double)random.nextInt(50)/100;
            double second = (double)random.nextInt(80)/100;
            double ave = random.nextInt(400) + (double)random.nextInt(100)/100;
            double dept = random.nextInt(30) + (double)random.nextInt(100)/100;
            websiteAnalysis = new WebsiteAnalysis(null,websiteid,null,null,null,null,DateUtils.parseTime(startDate + " 00:00:00"),usernum,ipnum,bounce,second,ave,dept);
            System.out.println(websiteAnalysis);
            websiteAnalysisDAO.insert(websiteAnalysis);
            List<Integer> dimensionIds = dimensionDAO.findAllDimensionIds();
            for (Integer dimensionId1:dimensionIds){
                List<DimensionInformation> dimensionInformationIds1 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId1);
                for (DimensionInformation dimensionInformation1:dimensionInformationIds1){
                    int dimensionInformationId1 = dimensionInformation1.getDimensionInformationId();
                    usernum = random.nextInt(400)+50;
                    ipnum = usernum - random.nextInt(50);
                    bounce = (double)random.nextInt(50)/100;
                    second = (double)random.nextInt(80)/100;
                    ave = random.nextInt(400) + (double)random.nextInt(100)/100;
                    dept = random.nextInt(30) + (double)random.nextInt(100)/100;
                    websiteAnalysis = new WebsiteAnalysis(null,websiteid,dimensionId1,dimensionInformationId1,null,null,DateUtils.parseTime(startDate + " 00:00:00"),usernum,ipnum,bounce,second,ave,dept);
                    System.out.println(websiteAnalysis);
                    websiteAnalysisDAO.insert(websiteAnalysis);
                    for (Integer dimensionId2:dimensionIds){
                        if (dimensionId1<dimensionId2){
                            List<DimensionInformation> dimensionInformationIds2 = dimensionInformationDAO.findDimensionInformationByDimensionId(dimensionId2);
                            for (DimensionInformation dimensionInformation2:dimensionInformationIds2){
                                int dimensionInformationId2 = dimensionInformation2.getDimensionInformationId();
                                usernum = random.nextInt(80)+10;
                                ipnum = usernum - random.nextInt(10);
                                bounce = (double)random.nextInt(50)/100;
                                second = (double)random.nextInt(80)/100;
                                ave = random.nextInt(400) + (double)random.nextInt(100)/100;
                                dept = random.nextInt(30) + (double)random.nextInt(100)/100;
                                websiteAnalysis = new WebsiteAnalysis(null,websiteid,dimensionId1,dimensionInformationId1,dimensionId2,dimensionInformationId2,DateUtils.parseTime(startDate + " 00:00:00"),usernum,ipnum,bounce,second,ave,dept);
                                System.out.println(u++ + " " + websiteAnalysis);
                                websiteAnalysisDAO.insert(websiteAnalysis);
                            }
                        }
                    }
                }
            }
        }
    }


    @Test
    public void sdja() throws ParseException {
        Random random = new Random();
        DimensionInformationDAO dimensionInformationDAO = DAOFactory.getDimensionInformationDAO();
        List<DimensionInformation> areas = dimensionInformationDAO.findDimensionInformationByDimensionId(1);
        WebsiteSourceDAO websiteSourceDAO = DAOFactory.getWebsiteSourceDAO();
        List<Integer> source = new ArrayList<Integer>();
        source.add(1);source.add(2);source.add(3);source.add(4);
        List<String> usertype = new ArrayList<String>();
        usertype.add("新访客");usertype.add("老访客");usertype.add("活跃用户");usertype.add("非活跃用户");
        WebsiteSource websiteSource;
        int u = 1;
        for (int i=0;i<20;i++){
            String startDate = DateUtils.getNDate(DateUtils.getTodayDate(), -29 + i);

            int usernum = random.nextInt(2000)+200;
            int visitnum = 2*usernum - random.nextInt(usernum);
            int ipnum = usernum - random.nextInt(200);
            int newuser = random.nextInt(usernum/2);
            double bounce_rate = (double)random.nextInt(50)/100;
            double avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
            double avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
            int transfernum = random.nextInt(usernum);
            double transferrate = (double)transfernum/usernum;

            websiteSource = new WebsiteSource(null,1,null,null,null,null,
                    usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
            System.out.println(u++ + " " + websiteSource);
            websiteSourceDAO.insert(websiteSource);
            for (DimensionInformation area:areas){
                String city = area.getDimensionInformation();
                usernum = random.nextInt(100)+10;
                visitnum = 2*usernum - random.nextInt(usernum);
                ipnum = usernum - random.nextInt(10);
                newuser = random.nextInt(usernum/2);
                bounce_rate = (double)random.nextInt(50)/100;
                avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                transfernum = random.nextInt(usernum);
                transferrate = (double)transfernum/usernum;
                websiteSource = new WebsiteSource(null,1,null,null,city,null,
                        usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                System.out.println(u++ + " " + websiteSource);
                websiteSourceDAO.insert(websiteSource);
                for (Integer s:source){
                    if (s != 4){
                        usernum = random.nextInt(40)+4;
                        visitnum = 2*usernum - random.nextInt(usernum);
                        ipnum = usernum - random.nextInt(4);
                        newuser = random.nextInt(usernum/2);
                        bounce_rate = (double)random.nextInt(50)/100;
                        avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                        avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                        transfernum = random.nextInt(usernum);
                        transferrate = (double)transfernum/usernum;
                        websiteSource = new WebsiteSource(null,1,null,s,city,null,
                                usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                        System.out.println(u++ + " " + websiteSource);
                        websiteSourceDAO.insert(websiteSource);
                    }else {
                        usernum = random.nextInt(10)+3;
                        visitnum = 2*usernum - random.nextInt(usernum);
                        ipnum = usernum - random.nextInt(2);
                        newuser = random.nextInt(usernum/2);
                        bounce_rate = (double)random.nextInt(50)/100;
                        avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                        avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                        transfernum = random.nextInt(usernum);
                        transferrate = (double)transfernum/usernum;
                        websiteSource = new WebsiteSource(null,1,null,s,city,null,
                                usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                        System.out.println(u++ + " " + websiteSource);
                        websiteSourceDAO.insert(websiteSource);
                    }
                }
                for (String type:usertype){
                    usernum = random.nextInt(50)+5;
                    visitnum = 2*usernum - random.nextInt(usernum);
                    ipnum = usernum - random.nextInt(5);
                    newuser = random.nextInt(usernum/2);
                    bounce_rate = (double)random.nextInt(50)/100;
                    avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                    avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                    transfernum = random.nextInt(usernum);
                    transferrate = (double)transfernum/usernum;
                    websiteSource = new WebsiteSource(null,1,null,null,city,type,
                            usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                    System.out.println(u++ + " " + websiteSource);
                    websiteSourceDAO.insert(websiteSource);
                    for (Integer s:source){
                        if (s != 4){
                            usernum = random.nextInt(10)+2;
                            visitnum = 2*usernum - random.nextInt(usernum);
                            ipnum = usernum - random.nextInt(1);
                            newuser = random.nextInt(usernum/2);
                            bounce_rate = (double)random.nextInt(50)/100;
                            avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                            avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                            transfernum = random.nextInt(usernum);
                            transferrate = (double)transfernum/usernum;
                            websiteSource = new WebsiteSource(null,1,null,s,city,type,
                                    usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                            System.out.println(u++ + " " + websiteSource);
                            websiteSourceDAO.insert(websiteSource);
                        }else {
                            usernum = random.nextInt(5)+2;
                            visitnum = 2*usernum - random.nextInt(usernum);
                            ipnum = usernum - random.nextInt(1);
                            newuser = random.nextInt(usernum/2);
                            bounce_rate = (double)random.nextInt(50)/100;
                            avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                            avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                            transfernum = random.nextInt(usernum);
                            transferrate = (double)transfernum/usernum;
                            websiteSource = new WebsiteSource(null,1,null,s,city,type,
                                    usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                            System.out.println(u++ + " " + websiteSource);
                            websiteSourceDAO.insert(websiteSource);
                        }
                    }

                }


            }

            for (Integer s:source){
                if (s != 4){
                    usernum = random.nextInt(800)+80;
                    visitnum = 2*usernum - random.nextInt(usernum);
                    ipnum = usernum - random.nextInt(80);
                    newuser = random.nextInt(usernum/2);
                    bounce_rate = (double)random.nextInt(50)/100;
                    avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                    avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                    transfernum = random.nextInt(usernum);
                    transferrate = (double)transfernum/usernum;
                    websiteSource = new WebsiteSource(null,1,null,s,null,null,
                            usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                    System.out.println(u++ + " " + websiteSource);
                    websiteSourceDAO.insert(websiteSource);
                }else {
                    usernum = random.nextInt(200)+20;
                    visitnum = 2*usernum - random.nextInt(usernum);
                    ipnum = usernum - random.nextInt(20);
                    newuser = random.nextInt(usernum/2);
                    bounce_rate = (double)random.nextInt(50)/100;
                    avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                    avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                    transfernum = random.nextInt(usernum);
                    transferrate = (double)transfernum/usernum;
                    websiteSource = new WebsiteSource(null,1,null,s,null,null,
                            usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                    System.out.println(u++ + " " + websiteSource);
                    websiteSourceDAO.insert(websiteSource);
                }

            }

            for (String type:usertype){
                usernum = random.nextInt(1000)+100;
                visitnum = 2*usernum - random.nextInt(usernum);
                ipnum = usernum - random.nextInt(100);
                newuser = random.nextInt(usernum/2);
                bounce_rate = (double)random.nextInt(50)/100;
                avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                transfernum = random.nextInt(usernum);
                transferrate = (double)transfernum/usernum;
                websiteSource = new WebsiteSource(null,1,null,null,null,type,
                        usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                System.out.println(u++ + " " + websiteSource);
                websiteSourceDAO.insert(websiteSource);
                for (Integer s:source){
                    if (s != 4){
                        usernum = random.nextInt(400)+40;
                        visitnum = 2*usernum - random.nextInt(usernum);
                        ipnum = usernum - random.nextInt(40);
                        newuser = random.nextInt(usernum/2);
                        bounce_rate = (double)random.nextInt(50)/100;
                        avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                        avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                        transfernum = random.nextInt(usernum);
                        transferrate = (double)transfernum/usernum;
                        websiteSource = new WebsiteSource(null,1,null,s,null,type,
                                usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                        System.out.println(u++ + " " + websiteSource);
                        websiteSourceDAO.insert(websiteSource);
                    }else {
                        usernum = random.nextInt(100)+10;
                        visitnum = 2*usernum - random.nextInt(usernum);
                        ipnum = usernum - random.nextInt(10);
                        newuser = random.nextInt(usernum/2);
                        bounce_rate = (double)random.nextInt(50)/100;
                        avertime = random.nextInt(200)+10 + (double)random.nextInt(100)/100;
                        avedepth = random.nextInt(10) + (double)random.nextInt(100)/100;
                        transfernum = random.nextInt(usernum);
                        transferrate = (double)transfernum/usernum;
                        websiteSource = new WebsiteSource(null,1,null,s,null,type,
                                usernum,visitnum,visitnum,ipnum,newuser,bounce_rate,avertime,avedepth,transfernum,transferrate,DateUtils.parseTime(startDate + " 00:00:00"));
                        System.out.println(u++ + " " + websiteSource);
                        websiteSourceDAO.insert(websiteSource);
                    }
                }
            }
        }
    }

    @Test
    public void skdja(){
        UserPortraitDAO userPortraitDAO = DAOFactory.getUserPortraitDAO();
        System.out.println(userPortraitDAO.query(1));
    }


}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.*;
import net.bytebuddy.utility.RandomString;

import java.util.Random;

/**
 * Utils that create fake entities instances for tests
 */
public class TestDataService {

    public BidList makeTestBidList() {
        Random random = new Random();
        BidList bidList = new BidList();
        bidList.setBidListId(random.nextInt());
        bidList.setAccount(RandomString.make(64));
        bidList.setBidQuantity(random.nextDouble());
        bidList.setType(RandomString.make(64));
        return bidList;
    }

    public CurvePoint makeTestCurvePoint() {
        Random random = new Random();
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(random.nextInt());
        curvePoint.setCurveId(random.nextInt());
        curvePoint.setTerm(random.nextDouble());
        curvePoint.setValue(random.nextDouble());
        return curvePoint;
    }

    public Rating makeTestRating() {
        Random random = new Random();
        Rating rating = new Rating();
        rating.setId(random.nextInt());
        rating.setFitchRating(RandomString.make(64));
        rating.setSandPRating(RandomString.make(64));
        rating.setMoodysRating(RandomString.make(64));
        rating.setOrderNumber(random.nextInt());
        return rating;
    }

    public RuleName makeTestRuleName() {
        Random random = new Random();
        RuleName ruleName = new RuleName();
        ruleName.setId(random.nextInt());
        ruleName.setDescription(RandomString.make(64));
        ruleName.setJson(RandomString.make(64));
        ruleName.setTemplate(RandomString.make(64));
        ruleName.setSqlStr(RandomString.make(64));
        ruleName.setSqlPart(RandomString.make(64));
        ruleName.setName(RandomString.make(64));
        return ruleName;
    }

    public Trade makeTestTrade() {
        Random random = new Random();
        Trade trade = new Trade();
        trade.setTradeId(random.nextInt());
        trade.setType(RandomString.make(64));
        trade.setAccount(RandomString.make(64));
        trade.setBuyQuantity(random.nextDouble());
        return trade;
    }

    public User makeTestUser() {
        User user = new User();
        user.setId(new Random().nextInt());
        user.setUsername(RandomString.make(64));
        user.setFullname(RandomString.make(64));
        user.setRole(RandomString.make(64));
        user.setPassword(RandomString.make(64));
        return user;
    }

}

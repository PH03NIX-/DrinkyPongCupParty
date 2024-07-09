package com.ph_3nix.drinkypongcupparty;

import java.util.List;

public interface AdsController {
    public void showRewardedVideo();
    public void loadRewardedVideoAd();
    public int getReward();
    public void setReward(int newReward);
    public void getMoreBalls( final Long requiredBottleCaps, final Long currentBottleCaps);
    public int checkMoreBalls();
    public void setMoreBalls(int newMoreBalls);
    public void instructions();
    public void getAvailableProducts();
    public void setBuyingCapsStage(int i);
    public void setBuyingCaps(int i);
    public int getBuyingCapsStage();
    public int getBuyingCaps();
}

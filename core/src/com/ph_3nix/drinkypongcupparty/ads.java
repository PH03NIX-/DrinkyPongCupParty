/*
package com.ph_3nix.drinkypongcupparty;
import com.google.android.gms.ads.rewarded.RewardedAd;
*/

/*package com.ph_3nix.drinkypongcupparty;
import com.google.android.gms.ads.rewarded.RewardedAd;

import sun.rmi.runtime.Log;

public class ads extends DrinkyPongCupParty {
    private RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }
}




MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});


		AdRequest adRequest = new AdRequest.Builder().build();

		RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", // testing unit id - replace when publishing
				adRequest, new RewardedAdLoadCallback(){
					@Override
					public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
						// Handle the error.
						Log.d(TAG, loadAdError.getMessage());
						mRewardedAd = null;
					}

					@Override
					public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
						mRewardedAd = rewardedAd;
						Log.d(TAG, "Ad was loaded.");
					}
				});

		mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
			@Override
			public void onAdShowedFullScreenContent() {
				// Called when ad is shown.
				Log.d(TAG, "Ad was shown.");
				mRewardedAd = null;
			}

			@Override
			public void onAdFailedToShowFullScreenContent(AdError adError) {
				// Called when ad fails to show.
				Log.d(TAG, "Ad failed to show.");
			}

			@Override
			public void onAdDismissedFullScreenContent() {
				// Called when ad is dismissed.
				// Don't forget to set the ad reference to null so you
				// don't show the ad a second time.
				Log.d(TAG, "Ad was dismissed.");
			}
		});










	MobileAds.initialize(this, new OnInitializationCompleteListener() {
@Override
public void onInitializationComplete(InitializationStatus initializationStatus) {
        }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest mRewardedAd = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", // testing unit id - replace when publishing
        adRequest, new RewardedAdLoadCallback(){
@Override
public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        // Handle the error.
        Log.d(TAG, loadAdError.getMessage());
        mRewardedAd = null;
        }

@Override
public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
        mRewardedAd = rewardedAd;
        Log.d(TAG, "Ad was loaded.");
        }
        //RewardedAd mRewardedAd = adRequest;
        }
        );

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
@Override
public void onAdShowedFullScreenContent() {
        // Called when ad is shown.
        Log.d(TAG, "Ad was shown.");
        mRewardedAd = null;
        }

@Override
public void onAdFailedToShowFullScreenContent(AdError adError) {
        // Called when ad fails to show.
        Log.d(TAG, "Ad failed to show.");
        }

@Override
public void onAdDismissedFullScreenContent() {
        // Called when ad is dismissed.
        // Don't forget to set the ad reference to null so you
        // don't show the ad a second time.
        Log.d(TAG, "Ad was dismissed.");
        }
        });



*/
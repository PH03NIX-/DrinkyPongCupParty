package com.ph_3nix.drinkypongcupparty;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ph_3nix.drinkypongcupparty.DrinkyPongCupParty;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import com.ph_3nix.drinkypongcupparty.AdsController;

import java.util.ArrayList;
import java.util.List;

import static com.android.billingclient.api.BillingClient.SkuType.INAPP;

public class AndroidLauncher extends AndroidApplication implements AdsController, RewardedVideoAdListener {
	//private FullScreenContentCallback fullScreenContentCallback;
	private RewardedVideoAd rewardedVideoAd;
	private int rewardAmount = 0;
	private int additionalBallAmount = 0;
	private int buyingCaps = 0;
	private int buyingStage = 0;
	private Button button;
	private BillingClient billingClient;
	//private final String TAG = "MainActivity";

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new DrinkyPongCupParty(this), config);

		rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		rewardedVideoAd.setRewardedVideoAdListener(this);

		loadRewardedVideoAd();
		startConnectionWithGooglePlay();

		// In KITKAT (4.4) and next releases, hide the virtual buttons
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			hideVirtualButtons();
		}*/

	}


	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					//Yes button clicked
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(rewardedVideoAd.isLoaded()) {
								rewardedVideoAd.show();
							}
							else loadRewardedVideoAd();
						}
					});
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					//No button clicked
					break;
			}
		}
	};


	@Override
	public void showRewardedVideo() {

		final Context thisContext = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				/*if(rewardedVideoAd.isLoaded()) {
					rewardedVideoAd.show();
				}
				else loadRewardedVideoAd();*/
				AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
				builder.setMessage("Watch an ad for additional 10 bottlecaps?");

				builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(rewardedVideoAd.isLoaded()) {
							rewardedVideoAd.show();
						}
						else loadRewardedVideoAd();

						dialog.dismiss();
					}
				});

				builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog

						dialog.dismiss();
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	@Override
	public void loadRewardedVideoAd() {
		rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", // testing - UPDATE WHEN PUBLISHING
				new AdRequest.Builder().build());
	}


	@Override
	public void onRewardedVideoAdLoaded() {
		//Toast.makeText(this, "ad has loaded", Toast.LENGTH_SHORT).show();
		//alert = new AlertDialog(this);
		//alert.setMessage("Watch and add for +10 bottlecaps?")
	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {
		loadRewardedVideoAd();
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {
		Toast.makeText(this, "+"+rewardItem.getAmount()+" bottlecaps rewarded", Toast.LENGTH_SHORT).show();
		rewardAmount = rewardItem.getAmount();
	}

	@Override
	public int getReward() {
		return rewardAmount;
	}

	@Override
	public void setReward(int newReward) {
		rewardAmount = newReward;
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {

	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
		Toast.makeText(this, "ad failed to load", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoCompleted() {

	}

	public void getMoreBalls( final Long requiredBottleCaps, final Long currentBottleCaps) {
		final Context thisContext = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
				builder.setMessage("Get additional 5 balls for "+requiredBottleCaps+" bottle caps?");

				builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if( currentBottleCaps < requiredBottleCaps ) {
							Toast.makeText(thisContext, "Not enough bottle caps to upgrade, need "+(requiredBottleCaps-currentBottleCaps)+" more.", Toast.LENGTH_SHORT).show();
						}
						else {
							Toast.makeText(thisContext, "Recieved +5 balls.", Toast.LENGTH_SHORT).show();
							setMoreBalls(5);
						}

						dialog.dismiss();
					}
				});

				builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog

						dialog.dismiss();
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	@Override
	public int checkMoreBalls() {
		return additionalBallAmount;
	}

	@Override
	public void setMoreBalls(int newMoreBalls) {
		additionalBallAmount = newMoreBalls;
	}

	@Override
	public void instructions() {
		final Context thisContext = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
				builder.setTitle("HOW TO PLAY");
				builder.setMessage("Swipe from left side of screen accross edge of table at various speeds and angles to get ping pong balls into cups. Enjoy :D");

				builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	private void handlePurchase(Purchase purchase) {
		Log.d("#response", "IT WORKED");

		// Verify the purchase.
		// Ensure entitlement was not already granted for this purchaseToken.
		// Grant entitlement to the user.

		ConsumeParams consumeParams =
			ConsumeParams.newBuilder()
				.setPurchaseToken(purchase.getPurchaseToken())
				.build();

		ConsumeResponseListener listener = new ConsumeResponseListener() {
			@Override
			public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
				if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
					setBuyingCapsStage(2);
				}
				else {
					setBuyingCapsStage(0);
					//setBuyingCaps(0);
				}
			}
		};

		billingClient.consumeAsync(consumeParams, listener);
	}

	private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
		@Override
		public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
			if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
					&& purchases != null) {
				for (Purchase purchase : purchases) {
					handlePurchase(purchase);
				}
			} else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
				setBuyingCapsStage(0);
				//setBuyingCaps(0);
			} else {

				setBuyingCapsStage(0);
				//setBuyingCaps(0);
			}
		}
	};



	public void startConnectionWithGooglePlay() {
		billingClient = BillingClient.newBuilder(/*activity*/this)
				.setListener(purchasesUpdatedListener)
				.enablePendingPurchases()
				.build();

		final Context thisContext = this;
		billingClient.startConnection(new BillingClientStateListener() {
			@Override
			public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
				if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
					Log.d("#billing", "onBillingSetupFinished: Success");
					//queryToFindProducts();
				}
			}

			@Override
			public void onBillingServiceDisconnected() {
				startConnectionWithGooglePlay();
				Log.d("#billing", "onBillingServiceDisconnected: Not Connected");
				Toast.makeText(thisContext, "Failure", Toast.LENGTH_SHORT).show();
			}

		});


	}


	public /*List<SkuDetails>*/void getPurchaseConfirmation(final String requestedPurchase) {

		final Activity thisContext = this;
		final List<SkuDetails> resultsDetailsList = new ArrayList<>();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				List<String> skuList = new ArrayList<>();
				skuList.add(requestedPurchase);
				SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
				params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
				Log.d("#response2", "starting");
				billingClient.querySkuDetailsAsync(params.build(),
						new SkuDetailsResponseListener() {
							@Override
							public void onSkuDetailsResponse(BillingResult billingResult,
															 List<SkuDetails> skuDetailsList) {
								Log.d("#response2", "trying");
								for (SkuDetails skuDetails : skuDetailsList) {
									Log.d("#response2", "trying true");
									//resultsDetailsList.add(skuDetails);
									BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
											.setSkuDetails(skuDetails)
											.build();
									int responseCode = billingClient.launchBillingFlow(thisContext, billingFlowParams).getResponseCode();
									if( responseCode == BillingClient.BillingResponseCode.OK ) {
										Log.d("#response", "" + responseCode);
									}
									else if (responseCode == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE) {
										Toast.makeText(thisContext, "Service Unavailable", Toast.LENGTH_SHORT).show();
									}

									Log.d("#response2", ""+responseCode);
								}
								// Process the result.
							}
						});
				//return resultsDetailsList;
			}
		});
		//return resultsDetailsList;
	}

	public void getAvailableProducts() {

		final Context thisContext = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final String[] ids = {"111234561"
									, "111234562"
									, "111234563"
									, "111234564"
									, "111234565" };
				final String[] displayedText = {"200 Caps - USD 0.99"
												, "500 Caps - USD 1.99"
												, "1200 Caps - USD 3.99"
												, "3000 Caps - USD 9.99"
												, "10000 Caps - USD 19.99" };
				final int[] values = {200
						, 500
						, 1200
						, 3000
						, 10000 };
				AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
				builder.setTitle("GET SOME CAPPAGE!");
				builder.setItems(displayedText, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setBuyingCapsStage(1);
						setBuyingCaps(values[which]);
						getPurchaseConfirmation(ids[which]);
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	public int getBuyingCaps() {
		return buyingCaps;
	}
	public int getBuyingCapsStage() {  // 0=n/a, 1=pend, 2=success
		return buyingStage;
	}
	public void setBuyingCaps(int i) {
		buyingCaps = i;
	}
	public void setBuyingCapsStage(int i) {
		buyingStage = i;
	}

}

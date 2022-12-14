package com.kidoz.sdk.sample.app;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.kidoz.mediation.admob.KidozSDKInfo;
//import com.kidoz.mediation.admob.KidozSDKInfo;

/**
 * Created by KIDOZ.
 */
public class MainActivity extends Activity {

    //Ad Units
    private static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-5967470543517808/1318954375";
    private static final String ADMOB_REWARDED_VIDEO_ID = "ca-app-pub-5967470543517808/9701675577";
    private static final String ADMOB_BANNER_ID =  "ca-app-pub-5967470543517808/9432160371";

    // Radio button indicating if the integration type to test is the adapter.
    private RadioButton adapterRadioButton;

    // The banner ad view.
    private AdView adView;
    // A loaded interstitial ad.
    private InterstitialAd interstitial;
    // A loaded rewarded ad.
    private RewardedAd rewardedAd;
    // The load interstitial button.
    private Button loadInterstitialButton;
    // The show interstitial button.
    private Button showInterstitialButton;
    // The load rewarded ad button.
    private Button loadRewardedButton;
    // The load rewarded ad button.
    private Button showRewardedButton;

    ScrollView feedbackContainer;
    private TextView feedBackTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle();
        setContentView(R.layout.activity_main);

        feedbackContainer = findViewById(R.id.feedbackContainer);
        feedBackTV = findViewById(R.id.feedback_tv);

        // Banner ads.
        Button loadBannerButton = findViewById(R.id.banner_load);
        Button closeBannerButton = findViewById(R.id.banner_close);
        ViewGroup bannerContainer = findViewById(R.id.bannerContainer);

        loadBannerButton.setOnClickListener(view -> {
            adView = new AdView(MainActivity.this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(ADMOB_BANNER_ID);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    String message = "Failed to load banner: " + loadAdError;
                    log(message);
                    Toast.makeText(MainActivity.this,
                            message,
                            Toast.LENGTH_SHORT).show();
                }
            });
            bannerContainer.removeAllViews();
            bannerContainer.addView(adView);
            adView.loadAd(new AdRequest.Builder().build());

            log("Load Banner");
            loadBannerButton.setEnabled(false);
            closeBannerButton.setEnabled(true);
//                // Add banner to view hierarchy.

        });

        closeBannerButton.setOnClickListener(v -> {
            loadBannerButton.setEnabled(true);
            closeBannerButton.setEnabled(false);
            adView.destroy();
            bannerContainer.removeAllViews();
            log("Close Banner");
        });

        // Interstitial ads.
        loadInterstitialButton = (Button) findViewById(R.id.interstitial_load);
        loadInterstitialButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        log("Load Interstitial");
                        loadInterstitialButton.setEnabled(false);
                        InterstitialAd.load(MainActivity.this,
                                ADMOB_INTERSTITIAL_ID,
                                new AdRequest.Builder().build(),
                                new InterstitialAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                        interstitial = interstitialAd;
                                        interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                                            @Override
                                            public void onAdFailedToShowFullScreenContent(@NonNull AdError error) {
                                                String message =  "Failed to show interstitial: " + error;
                                                log(message);
                                                Toast.makeText(MainActivity.this,
                                                        message,
                                                        Toast.LENGTH_SHORT).show();
                                                loadInterstitialButton.setEnabled(true);
                                            }

                                            @Override
                                            public void onAdDismissedFullScreenContent() {
                                                loadInterstitialButton.setEnabled(true);
                                                log("Interstitial closed");
                                            }
                                        });
                                        showInterstitialButton.setEnabled(true);
                                    }

                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                        String message = "Failed to load interstitial: " + loadAdError;
                                        log(message);
                                        Toast.makeText(MainActivity.this,
                                                message,
                                                Toast.LENGTH_SHORT).show();
                                        interstitial = null;
                                        loadInterstitialButton.setEnabled(true);
                                    }
                                });
                    }
                });

        showInterstitialButton = (Button) findViewById(R.id.interstitial_show);
        showInterstitialButton.setOnClickListener(view -> {
            showInterstitialButton.setEnabled(false);
            if (interstitial != null) {
                interstitial.show(MainActivity.this);
            }
            log("Show Interstitial");
        });

        //Sample Adapter Rewarded Ad Button.
        loadRewardedButton = (Button) findViewById(R.id.rewarded_load);
        loadRewardedButton.setOnClickListener(view -> {
            log("Load Rewarded");
            loadRewardedButton.setEnabled(false);
            RewardedAd.load(MainActivity.this,
                    ADMOB_REWARDED_VIDEO_ID,
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            rewardedAd = ad;
                            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError error) {
                                    String message =  "Failed to show Rewarded Video: " + error;
                                    log(message);
                                    Toast.makeText(MainActivity.this,
                                            message,
                                            Toast.LENGTH_SHORT).show();
                                    loadRewardedButton.setEnabled(true);
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    loadRewardedButton.setEnabled(true);
                                    log("Rewarded Video closed");
                                }
                            });
                            showRewardedButton.setEnabled(true);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            String message = "Failed to load rewarded ad: " + loadAdError;
                            log(message);
                            Toast.makeText(MainActivity.this,
                                    message,
                                    Toast.LENGTH_SHORT).show();
                            rewardedAd = null;
                            loadRewardedButton.setEnabled(true);
                        }
                    });
        });

        showRewardedButton = (Button) findViewById(R.id.rewarded_show);
        showRewardedButton.setOnClickListener(view -> {
            showRewardedButton.setEnabled(false);
            if (rewardedAd != null) {
                rewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        String message = String.format("User earned reward. Type: %s, amount: %d",
                                rewardItem.getType(), rewardItem.getAmount());
                        log(message);
                        Toast.makeText(MainActivity.this,
                                message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void log(String message){
        Log.d(MainActivity.class.getSimpleName(),message);
        String text = feedBackTV.getText().toString() + message + "\n";
        feedBackTV.setText(text);
        feedbackContainer.post(() -> {
            feedbackContainer.fullScroll(View.FOCUS_DOWN);
        });
    }

    private void setActivityTitle() {
        String title = "AdMob Adapter v" + KidozSDKInfo.getKidozSDKVersion() + "::" + KidozSDKInfo.getMediationAdapterVersion();
        setTitle(title);
    }
}

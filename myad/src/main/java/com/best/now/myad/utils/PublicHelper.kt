package com.best.now.myad.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.best.now.myad.ext.getSpValue
import com.best.now.myad.ext.putSpValue
import com.best.now.myad.view.AdsRewardsPop
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.best.now.myad.R
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.DBCookieStore
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import okhttp3.OkHttpClient

class PublicHelper {

}


val adParentList = arrayListOf<LinearLayout>()
fun isRewarded(context: Activity):Boolean{
    val rewarded = context.getSpValue("isRewarded",0)
    if (rewarded<3){
        //弹框
        showRewardsPop(context,rewarded)
    }
    return rewarded>=3
}
fun enable(context: Activity):Boolean{
    val rewarded = context.getSpValue("isRewarded",0)
    return rewarded>=3
}
private var pop: AdsRewardsPop? = null
fun showRewardsPop(context: Activity,count:Int) {
    pop = AdsRewardsPop(context,count){
        showRewardedAds(context)
        pop?.dismiss()
        pop = null
    }
    pop?.showPopupWindow()
}

/*** 为控件加载广告 */
fun loadAd(linearLayout: LinearLayout) {

    if (linearLayout.childCount == 0) {
        var adView = AdView(linearLayout.context.applicationContext)
        adView.adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            linearLayout.context,
            SizeUtils.px2dp(ScreenUtils.getScreenWidth().toFloat())
        )
//        adView.adSize = AdSize.BANNER
        adView.adUnitId = Constant.AD_BANNER_ID
        adView.adListener = object :AdListener(){
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                LogUtils.e("adView:"+p0.message)
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                LogUtils.e("adView:onAdLoaded")
            }
        }
        linearLayout.addView(adView)
    }

    var adView = linearLayout.getChildAt(0) as AdView

    linearLayout.visibility = View.VISIBLE
    adView.loadAd(AdRequest.Builder().build())

    if (!adParentList.contains(linearLayout)) {
        adParentList.add(linearLayout)
    }
}

/*** 更新广告控件 */
fun updateAdView() {
    for (adParentView in adParentList) {
        if (adParentView != null && adParentView.isAttachedToWindow) {
            // TODO: 广告一直显示
//            if (isPurchased()) {
            if (false) {
                var adView = adParentView.getChildAt(0) as AdView
                adView?.pause()
                adView?.destroy()

                adParentView.removeAllViews()
                adParentView.visibility = View.GONE
            } else {
                adParentView.visibility = View.VISIBLE
            }
        }
    }
}

var mInterstitialAd: InterstitialAd? = null
private var mRewardedAd: RewardedAd? = null
fun loadRewardedAds(context: Context){
    var adRequest = AdRequest.Builder().build()
    RewardedAd.load(context,Constant.AD_APP_REWARDED_ID, adRequest, object : RewardedAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            mRewardedAd = null
        }
        override fun onAdLoaded(rewardedAd: RewardedAd) {
            mRewardedAd = rewardedAd
        }
    })
}
/*** 显示激励广告 */
fun showRewardedAds(activity: Activity, callback: (() -> Unit)? = null) {
    if (mRewardedAd != null) {
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                mRewardedAd = null
                loadRewardedAds(activity)
                callback?.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                mRewardedAd = null
                callback?.invoke()
            }

            override fun onAdShowedFullScreenContent() {
            }
        }

        mRewardedAd!!.show(activity, OnUserEarnedRewardListener {
            val next = activity.getSpValue("isRewarded",0)+1
            activity.putSpValue("isRewarded",next)
            BusUtils.post("check")
        })
    } else {
        loadRewardedAds(activity)
        callback?.invoke()
    }

}

/*** 加载插页广告 */
fun loadInterstitialAd(context: Context) {
    InterstitialAd.load(
        context,
        Constant.AD_INTERSTITIAL_ID,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd

            }
        })
}

/*** 显示插屏广告 */
fun showInterstitialAd(activity: Activity, callback: (() -> Unit)? = null) {
    if (mInterstitialAd != null) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null
                loadInterstitialAd(activity)
                callback?.invoke()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                mInterstitialAd = null
                callback?.invoke()
            }

            override fun onAdShowedFullScreenContent() {
            }
        }

        mInterstitialAd!!.show(activity)
    } else {
        loadInterstitialAd(activity)
        callback?.invoke()
    }

}

fun getLoadingDialog(
    context: Context,
    toastStr: String?,
    canceledOnTouchOutside: Boolean,
    cancelable: Boolean,
    backgroundDim: Float
): Dialog {
    val dialog = Dialog(context!!, R.style.CustomDialog)
    if (backgroundDim >= 0) {
        dialog.window!!.setDimAmount(backgroundDim)
    }
    val view: View = LayoutInflater.from(context).inflate(
        R.layout.dialog_loading, null
    )
    val toastTv = view.findViewById<TextView>(R.id.tv_toast)
    if (!TextUtils.isEmpty(toastStr)) {
        toastTv.text = toastStr
    } else {
        toastTv.text = "loading…"
    }

    setCustomerDialogAttributes(
        dialog,
        view,
        Gravity.CENTER,
        canceledOnTouchOutside,
        cancelable,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    return dialog
}

fun getLoadingDialog(context: Context, toastStr: String?): Dialog {
    return getLoadingDialog(context, toastStr, false, true, -1f)
}

fun setCustomerDialogAttributes(
    dlg: Dialog,
    contentView: View?, gravity: Int, canceledOnTouchOutside: Boolean,
    cancelable: Boolean, width: Int, height: Int
): TextView? {
    dlg.setCanceledOnTouchOutside(canceledOnTouchOutside)
    dlg.setCancelable(cancelable)
    dlg.setContentView(contentView!!)
    val window = dlg.window
    val params = window!!.attributes
    params.gravity = gravity
    params.width = width
    params.height = height
    //		window.setAttributes(params);
    //
    //		dlg.onWindowAttributesChanged(params);
    return null
}

/*** 指定位置到顶部 */
fun RecyclerView.positionToTop(position: Int, offset: Int? = 0) {
    //滑动滚动-会触发滚动事件
//    var mScroller = RecycleScrollTopScroller(this.context);
//    mScroller.targetPosition = position;
//    this.layoutManager?.startSmoothScroll(mScroller)

    //不会触发滚动事件，还能设置偏移量
    var layoutManager = this.layoutManager as LinearLayoutManager
    layoutManager.scrollToPositionWithOffset(position, offset!!)
//当有足够的项目填充屏幕时，它可以正常工作，但如果RecyclerView中只有少数项目，它会将它们向下推，并在屏幕顶​​部留下空白区域
//                    layoutManager.stackFromEnd = true//当item不满一屏时，从底部填充视图，顶部会空白

}

fun isServiceON(context: Context,className:String):Boolean{
    val manager:ActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
    val list = manager.getRunningServices(100)
    if (list.isNullOrEmpty())
        return false
    else{
        list.forEach {
            if (it.service.className==className)
                return true
        }
        return false
    }
}
/*** 初始化OkGo */
fun initOkGo(application: Application) {

    //okGo网络框架初始化和全局配置

    //okGo网络框架初始化和全局配置
    val builder = OkHttpClient.Builder()

    //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));      //使用sp保持cookie，如果cookie不过期，则一直有效
    //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));      //使用sp保持cookie，如果cookie不过期，则一直有效
    builder.cookieJar(CookieJarImpl(DBCookieStore(application))) //使用数据库保持cookie，如果cookie不过期，则一直有效

    //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));      //使用内存保持cookie，app退出后，cookie消失
    //设置请求头
    //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));      //使用内存保持cookie，app退出后，cookie消失
    //设置请求头
    val headers = HttpHeaders()
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
    //设置请求参数
    //        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
    //设置请求参数
    val params = HttpParams()
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
    //        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
    OkGo.getInstance().init(application) //必须调用初始化
//            .setOkHttpClient(builder.build()) //建议设置OkHttpClient，不设置会使用默认的
        .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST) //全局统一缓存模式，默认不使用缓存，可以不传
        .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //全局统一缓存时间，默认永不过期，可以不传
        .setRetryCount(0) //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        .addCommonHeaders(headers) //全局公共头
        .addCommonParams(params)
}


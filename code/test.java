package com.sogou.novel.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.util.i;
import com.baidu.mobads.interfaces.IXAdRequestInfo;
import com.google.gson.Gson;
import com.huawei.hms.framework.common.ContainerUtils;
import com.huawei.hms.support.api.entity.common.CommonConstant;
import com.idlefish.flutterboost.FlutterBoost;
import com.lechuan.midunovel.base.okgo.cache.CacheEntity;
import com.meizu.cloud.pushsdk.constants.PushConstants;
import com.sogou.booklib.db.dao.WebBook;
import com.sogou.booklib.db.model.PirateData;
import com.sogou.bqdatacollect.BQLogAgent;
import com.sogou.commonlib.base.BaseApplication;
import com.sogou.commonlib.config.BQConsts;
import com.sogou.commonlib.kits.Empty;
import com.sogou.commonlib.kits.FileDownloadManager;
import com.sogou.commonlib.kits.TimeUtil;
import com.sogou.commonlib.kits.ToastUtils;
import com.sogou.novel.Application;
import com.sogou.novel.R;
import com.sogou.novel.adsdk.SNAdManager;
import com.sogou.novel.adsdk.model.SNAdItem;
import com.sogou.novel.adsdk.view.SNAdView;
import com.sogou.novel.app.config.BQConsts;
import com.sogou.novel.app.config.BQUtil;
import com.sogou.novel.app.config.Constants;
import com.sogou.novel.app.config.map.ActivityNameMap;
import com.sogou.novel.app.config.sharedpreferences.SpConfig;
import com.sogou.novel.app.log.Logger;
import com.sogou.novel.app.stat.DataSendUtil;
import com.sogou.novel.base.BaseActivity;
import com.sogou.novel.base.db.gen.Book;
import com.sogou.novel.base.db.gen.Chapter;
import com.sogou.novel.base.manager.DBManager;
import com.sogou.novel.base.manager.TaskManager;
import com.sogou.novel.flutter.FlutterRoutePath;
import com.sogou.novel.home.MainActivity;
import com.sogou.novel.home.bookshelf.AddStoreBookManager;
import com.sogou.novel.home.bookshelf.clientshelf.ShelfBookCallBack;
import com.sogou.novel.home.newshelf.BookManager;
import com.sogou.novel.home.user.UserManager;
import com.sogou.novel.home.user.login.UserLoginController;
import com.sogou.novel.loginsdk.PlatformConfig;
import com.sogou.novel.loginsdk.PlatformType;
import com.sogou.novel.loginsdk.SocialApi;
import com.sogou.novel.loginsdk.listener.AuthListener;
import com.sogou.novel.loginsdk.weixin.WXMiniProgramHelper;
import com.sogou.novel.network.http.LinkStatus;
import com.sogou.novel.network.http.Request;
import com.sogou.novel.network.http.Response;
import com.sogou.novel.network.http.api.SogouNovel;
import com.sogou.novel.network.http.api.model.FreeBookSearchData;
import com.sogou.novel.network.http.api.model.SearchData;
import com.sogou.novel.network.http.api.model.StoreAdInfoBean;
import com.sogou.novel.network.http.api.model.VersionBean;
import com.sogou.novel.network.http.api.model.VersionData;
import com.sogou.novel.network.http.api.model.event.AddBookEvent;
import com.sogou.novel.network.http.api.model.event.BuySuccessEvent;
import com.sogou.novel.network.http.api.model.event.DownloadBookEvent;
import com.sogou.novel.network.http.api.model.event.ReadingEvent;
import com.sogou.novel.network.http.api.model.event.SearchTabChangeEvent;
import com.sogou.novel.network.http.api.model.event.SignedInEvent;
import com.sogou.novel.network.http.api.model.event.VipOpenSuccEvent;
import com.sogou.novel.player.activity.AlbumDetailActivity;
import com.sogou.novel.player.activity.ListenWebActivity;
import com.sogou.novel.reader.ad.DefaultVideoAdListener;
import com.sogou.novel.reader.bookdetail.BookInfoActivity;
import com.sogou.novel.reader.bookdetail.ChapterStoreListActivity;
import com.sogou.novel.reader.bookdetail.StoreBookDetailActivity;
import com.sogou.novel.reader.buy.BuyActivity;
import com.sogou.novel.reader.buy.LimitPreferentialActivity;
import com.sogou.novel.reader.buy.RechargeActivity;
import com.sogou.novel.reader.buy.SendMessageBroadcastReceiver;
import com.sogou.novel.reader.buy.VipActivity;
import com.sogou.novel.reader.download.DownloadListenerImpl;
import com.sogou.novel.reader.download.Downloader;
import com.sogou.novel.reader.download.bookdownload.BookDownloadManager;
import com.sogou.novel.reader.promotion.CategoryActivity;
import com.sogou.novel.reader.reading.OpenBookActivity;
import com.sogou.novel.reader.reading.ReadProgress;
import com.sogou.novel.reader.reading.page.ChapterManager;
import com.sogou.novel.reader.reading.payment.ChapterPayDetail;
import com.sogou.novel.reader.reading.payment.PaymentRuleDialog;
import com.sogou.novel.reader.search.SearchWebActivity;
import com.sogou.novel.reader.settings.UserFeedbackTask;
import com.sogou.novel.scorewall.core.ScoreWallManager;
import com.sogou.novel.upgrade.UpdateService;
import com.sogou.novel.utils.CollectionUtil;
import com.sogou.novel.utils.FileUtil;
import com.sogou.novel.utils.PackageUtil;
import com.sogou.novel.utils.SDKWrapUtil;
import com.sogou.novel.utils.StringUtil;
import com.sogou.novel.utils.ToastUtil;
import com.sogou.novel.utils.Utils;
import com.sogou.passportsdk.PassportConstant;
import com.sogou.reader.doggy.ad.QQBrowserUtil;
import com.sogou.reader.doggy.ad.SNAdManagerPlugin;
import com.sogou.reader.doggy.ad.ad.SNAdLocation;
import com.sogou.reader.doggy.ad.manager.VideoAdManager;
import com.sogou.reader.doggy.ad.net.AdInnerConfig;
import com.sogou.udp.push.common.Constants4Inner;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.TencentOpenHost;
import com.umeng.message.proguard.l;
import com.wlx.common.util.NetworkUtil;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebInfoInterface implements AuthListener {
    private static final int ADD_FAIL = 33;
    private static final int ADD_SUCC = 32;
    static String activity_pre = "com.sogou.novel.";
    static HashMap<String, Class<?>> sAppHostmap = new HashMap<>();
    String bookey;
    private String chapterId;
    private int downloadBookAdTime = 0;
    private String downloadBookIds = "";
    String failCallString;
    private String from;
    String getOpenIdCallBack;
    private boolean isStartReading = false;
    private FrameLayout loadingLayout;
    private TextView loadingTextView;
    private StoreBookDownloadListenerImpl mBookDownloadListenerImpl;
    protected Activity mContent;
    Handler mHandler = new Handler() { // from class: com.sogou.novel.app.WebInfoInterface.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message != null) {
                try {
                    switch (message.what) {
                        case 32:
                            FrameLayout frameLayout = WebInfoInterface.this.loadingLayout;
                            if (frameLayout != null) {
                                WebInfoInterface.this.loadingLayout.setVisibility(8);
                            }
                            WebView webView = WebInfoInterface.this.mWeb;
                            if (webView != null) {
                                WebView webView2 = WebInfoInterface.this.mWeb;
                                webView2.loadUrl("javascript:Acb." + WebInfoInterface.this.succCallString + "([\"" + WebInfoInterface.this.bookey + "\"])");
                                return;
                            }
                            return;
                        case 33:
                            FrameLayout frameLayout2 = WebInfoInterface.this.loadingLayout;
                            if (frameLayout2 != null) {
                                WebInfoInterface.this.loadingLayout.setVisibility(8);
                            }
                            WebView webView3 = WebInfoInterface.this.mWeb;
                            if (webView3 != null) {
                                WebView webView4 = WebInfoInterface.this.mWeb;
                                webView4.loadUrl("javascript:Acb." + WebInfoInterface.this.failCallString + "([\"" + WebInfoInterface.this.bookey + "\"])");
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private UpdatePopupHeightListener mPopupHeightListener;
    private UpdateBannerHeightListener mUpdateBannerHeightListener;
    WebView mWeb;
    String succCallString;
    private String tipName;

    public interface GetBkeyListener {
        String getBkey();
    }

    public interface GoldListener {
        void getChapterFreeAdStatus(String str);

        void showVideoAd(String str, String str2, String str3);
    }

    public interface PayListener {
        void doPay(String str, String str2, String str3, String str4, String str5, String str6);

        void getMobile();
    }

    public interface ShowChapterListener {
        void showChapterList();
    }

    public interface UpdateBannerHeightListener {
        void updateBannerHeight(int i);
    }

    public interface UpdateCategoryShareListen {
        void evokeShareInfo(String str, String str2, String str3, String str4, String str5, String str6);
    }

    public interface UpdateIconListener {
        void updateSearchIcon();
    }

    public interface UpdatePopupHeightListener {
        void updatePopupHeight(int i);
    }

    public interface UpdateTitleListener {
        void setRechargeType(int i);

        void updateTitle(String str);
    }

    public interface VipPayListener {
        void doVipPay(String str, String str2, String str3, String str4);
    }

    private void force_update() {
    }

    @Override // com.sogou.novel.loginsdk.listener.AuthListener
    public void onCancel(PlatformType platformType) {
    }

    static /* synthetic */ int access$308(WebInfoInterface webInfoInterface) {
        int i = webInfoInterface.downloadBookAdTime;
        webInfoInterface.downloadBookAdTime = i + 1;
        return i;
    }

    static {
        sAppHostmap.put("accoutLeft", MainActivity.class);
        sAppHostmap.put("search", SearchWebActivity.class);
        sAppHostmap.put(FlutterBoost.ConfigBuilder.DEFAULT_DART_ENTRYPOINT, MainActivity.class);
    }

    public void setPopupHeightListener(UpdatePopupHeightListener updatePopupHeightListener) {
        this.mPopupHeightListener = updatePopupHeightListener;
    }

    public UpdatePopupHeightListener getPopupHeightListener() {
        return this.mPopupHeightListener;
    }

    public void setUpdateBannerHeightListener(UpdateBannerHeightListener updateBannerHeightListener) {
        this.mUpdateBannerHeightListener = updateBannerHeightListener;
    }

    public void setChapterId(String str) {
        this.chapterId = str;
    }

    public WebInfoInterface(Activity activity, WebView webView) {
        this.mContent = activity;
        this.mWeb = webView;
    }

    @JavascriptInterface
    public void todetail(int i, final String str) {
        if (str != null) {
            this.mContent.runOnUiThread(new Runnable() { // from class: com.sogou.novel.app.WebInfoInterface.2
                @Override // java.lang.Runnable
                public void run() {
                    WebInfoInterface.this.onJS(str);
                }
            });
        }
    }

    @JavascriptInterface
    public void tobrowser(String str) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(str));
        intent.setAction(CommonConstant.ACTION.HWID_SCHEME_URL);
        this.mContent.startActivity(intent);
    }

    @JavascriptInterface
    public void xmlyNoResult() {
        this.mContent.runOnUiThread(new Runnable() { // from class: com.sogou.novel.app.WebInfoInterface.3
            @Override // java.lang.Runnable
            public void run() {
                boolean z = WebInfoInterface.this.mContent instanceof SearchWebActivity;
            }
        });
    }

    @JavascriptInterface
    public void notifyQuery(final String str) {
        this.mContent.runOnUiThread(new Runnable() { // from class: com.sogou.novel.app.WebInfoInterface.4
            @Override // java.lang.Runnable
            public void run() {
                boolean z = WebInfoInterface.this.mContent instanceof SearchWebActivity;
                if (z) {
                    ((SearchWebActivity) WebInfoInterface.this.mContent).updateQuery(str);
                }
            }
        });
    }

    @JavascriptInterface
    public void copyToClipboard(String str) {
        ((ClipboardManager) this.mContent.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("sogou_novel", str));
    }

    @JavascriptInterface
    public void getHeight(int i) {
        Activity activity = this.mContent;
        boolean z = activity instanceof UpdateBannerHeightListener;
        if (z) {
            ((UpdateBannerHeightListener) activity).updateBannerHeight(i);
            return;
        }
        UpdatePopupHeightListener updatePopupHeightListener = this.mPopupHeightListener;
        if (updatePopupHeightListener != null) {
            updatePopupHeightListener.updatePopupHeight(i);
            return;
        }
        UpdateBannerHeightListener updateBannerHeightListener = this.mUpdateBannerHeightListener;
        if (updateBannerHeightListener != null) {
            updateBannerHeightListener.updateBannerHeight(i);
        }
    }

    @JavascriptInterface
    public void onBQEvent(String str) {
        BQLogAgent.onEvent(str);
    }

    @JavascriptInterface
    public void onBQEventEnum(String str, String str2) {
        BQLogAgent.onEvent(str, str2);
    }

    @JavascriptInterface
    public void onBQEventOnline(String str) {
        BQLogAgent.onEventOnline(str);
    }

    @JavascriptInterface
    public void onBQEventEnumOnline(String str, String str2) {
        BQLogAgent.onEventOnline(str, str2);
    }

    @JavascriptInterface
    public void onBQCustomEvent(String str, String str2) {
        BQLogAgent.onEventCustom(str, str2);
    }

    @JavascriptInterface
    public void openWeixinMiniPro(String str, String str2, String str3) {
        StringBuffer stringBuffer = new StringBuffer(str2);
        boolean contains = str2.contains("?");
        if (!contains) {
            stringBuffer.append("?");
        } else {
            stringBuffer.append("&");
        }
        stringBuffer.append("callback=");
        stringBuffer.append(str3);
        WXMiniProgramHelper.getInstance(this.mContent, PlatformConfig.weixinAppId).openMiniProgram(str, stringBuffer.toString());
    }

    @JavascriptInterface
    public void openDefaultWeixinMiniPro(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.miniProgramPath);
        int gender = SpConfig.getGender();
        sb.append(gender == 0 ? "0" : "1");
        openWeixinMiniPro(Constants.miniProgramId, sb.toString(), str);
    }

    @JavascriptInterface
    public void openDefaultWeixinMiniPro() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.miniProgramPath);
        int gender = SpConfig.getGender();
        sb.append(gender == 0 ? "0" : "1");
        openWeixinMiniPro(Constants.miniProgramId, sb.toString(), null);
    }

    public boolean notifyStoreGuide(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("notifyStoreGuide");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof MainActivity;
        if (!z) {
            return true;
        }
        ((MainActivity) activity).showStoreTopGuide();
        return true;
    }

    private boolean showError(String str) {
        boolean equalsIgnoreCase = str.equalsIgnoreCase("error");
        if (!equalsIgnoreCase) {
            return false;
        }
        ToastUtil.getInstance().setText(Application.getInstance().getString(R.string.string_http_download_data_fail_server_err));
        return true;
    }

    private boolean gotoSearch(String str) {
        boolean equalsIgnoreCase = str.equalsIgnoreCase("search:");
        if (!equalsIgnoreCase) {
            return false;
        }
        Activity activity = this.mContent;
        activity.startActivity(new Intent(activity, SearchWebActivity.class));
        this.mContent.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_scale);
        return true;
    }

    private boolean startAApp(String str) {
        boolean startsWith = str.startsWith("app://");
        if (!startsWith) {
            return false;
        }
        startAppActivity(Uri.parse(str));
        return true;
    }

    private boolean updateApp(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("update");
        return optJSONObject != null;
    }

    /* JADX WARNING: Incorrect condition in loop: B:15:0x004d */
    private boolean startActivity(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("activity");
        String str = "";
        String str2 = "";
        if (optJSONObject == null) {
            return false;
        }
        Intent intent = new Intent();
        int optInt = optJSONObject.optInt(CacheEntity.KEY);
        JSONObject optJSONObject2 = optJSONObject.optJSONObject("info");
        if (optJSONObject2 != null) {
            boolean equals = "".equals(optJSONObject2);
            if (!equals) {
                str = optJSONObject2.optString(Constants.SP_LOGIN_KEY);
                str2 = optJSONObject2.optString(Constants.SP_LOGIN_URL);
            }
        }
        if (optJSONObject2 != null) {
            boolean equals2 = "".equals(optJSONObject2);
            if (!equals2) {
                boolean isEmpty = StringUtil.isEmpty(str);
                if (isEmpty) {
                    Iterator<String> keys = optJSONObject2.keys();
                    while (r7) {
                        String next = keys.next();
                        String str3 = null;
                        try {
                            str3 = optJSONObject2.getString(next);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        boolean equals3 = str3.equals("true");
                        if (equals3) {
                            intent.putExtra(next, true);
                        } else {
                            boolean equals4 = str3.equals("false");
                            if (equals4) {
                                intent.putExtra(next, false);
                            } else {
                                boolean matches = str3.matches("[0-9]+");
                                if (matches) {
                                    intent.putExtra(next, Integer.parseInt(str3));
                                } else {
                                    intent.putExtra(next, str3);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (optInt <= 0) {
            return true;
        }
        if (optInt == 16 || optInt == 19 || optInt == 22) {
            UserLoginController goToLoginV2Activity = Utils.goToLoginV2Activity(this.mContent, 0);
            goToLoginV2Activity.setLoginKey(str);
            goToLoginV2Activity.setLoginUrl(str2);
            goToLoginV2Activity.setLoginResult(42);
        } else if (optInt == 54) {
            boolean z = this.mContent instanceof MainActivity;
            if (z) {
                BaseApplication.getInstance().getGloableHandler().postDelayed(new Runnable() { // from class: com.sogou.novel.app.WebInfoInterface.5
                    @Override // java.lang.Runnable
                    public void run() {
                        ((MainActivity) WebInfoInterface.this.mContent).autoSign();
                    }
                }, 300);
            }
        } else {
            intent.setClassName(this.mContent, ActivityNameMap.getActivityName(optInt));
            Bundle bundle = new Bundle();
            bundle.putString(Constants.SP_LOGIN_KEY, str);
            bundle.putString(Constants.SP_LOGIN_URL, str2);
            boolean z2 = this.mContent instanceof VipActivity;
            if (z2) {
                bundle.putInt(Constants.LOGIN_REASON, 39);
            }
            intent.putExtras(bundle);
            this.mContent.startActivity(intent);
            this.mContent.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_scale);
        }
        Activity activity = this.mContent;
        boolean z3 = activity instanceof RechargeActivity;
        if (!z3) {
            return true;
        }
        activity.finish();
        return true;
    }

    private void getBuyChaperList(String str) {
        TaskManager.startHttpDataRequset(SogouNovel.getInstance().getChapterBuyRecordList(str), null);
    }

    private boolean buySuccess(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("buySucc");
        if (optJSONObject == null) {
            return false;
        }
        EventBus.getDefault().post(new BuySuccessEvent());
        int optInt = optJSONObject.optInt("costGl");
        int optInt2 = optJSONObject.optInt("costVoucher");
        int optInt3 = optJSONObject.optInt("yueGl");
        int optInt4 = optJSONObject.optInt("yueVoucher");
        optJSONObject.optInt("yue");
        int optInt5 = optJSONObject.optInt("amount");
        int buyFrom = SpConfig.getBuyFrom();
        int optInt6 = optJSONObject.optInt("lastBuyIndex");
        String optString = optJSONObject.optString("lastBuyCkey");
        String optString2 = optJSONObject.optString("decryptKey");
        String optString3 = optJSONObject.optString(Constants.MN_MAIN_TOAST);
        Intent intent = new Intent();
        intent.putExtra("costGl", optInt);
        intent.putExtra("costVoucher", optInt2);
        intent.putExtra("yueGl", optInt3);
        intent.putExtra("yueVoucher", optInt4);
        intent.putExtra("amount", optInt5);
        intent.putExtra("fromBuySucc", true);
        intent.putExtra("lastBuyIndex", optInt6);
        intent.putExtra("decryptKey", optString2);
        SpConfig.setBuySuccess(true);
        if (optString != null) {
            intent.putExtra("lastBuyCkey", optString);
        }
        int i = Constants.BOOKINFO_BUY_STATUS_SUCCS;
        if (buyFrom == i) {
            this.mContent.finish();
            this.mContent.startActivity(intent);
            this.mContent.finish();
            Activity activity = this.mContent;
            boolean z = activity instanceof BuyActivity;
            if (!z) {
                return true;
            }
            activity.overridePendingTransition(0, 0);
            return true;
        }
        Activity activity2 = this.mContent;
        boolean z2 = activity2 instanceof GetBkeyListener;
        if (z2) {
            getBuyChaperList(((GetBkeyListener) activity2).getBkey());
        }
        String str = "花费" + optInt + "搜豆和" + optInt2 + "搜豆";
        int i2 = Constants.BUY_NOTDOWNLOAD_STATUS_SUCCS;
        if (buyFrom == i2) {
            Book bookDB = ChapterManager.getInstance().getBookDB();
            DBManager.insertBook(bookDB);
            Chapter chapter = ChapterManager.getInstance().getCurrentChapter().chapterDB;
            int parseInt = Integer.parseInt(bookDB.getChargeType());
            if (chapter != null) {
                Chapter chapter2 = DBManager.getChapter(ChapterManager.getInstance().getBookDB(), chapter.getChapterIndex().intValue() + 1);
                if (parseInt == 0) {
                    boolean isEmpty = TextUtils.isEmpty(optString3);
                    if (!isEmpty) {
                        ToastUtil.getInstance().setText(optString3);
                    } else {
                        ToastUtil.getInstance().setText("您已成功购买了全本," + str);
                    }
                    toDownload(bookDB.getBookId(), bookDB.get_id(), String.valueOf(bookDB.getBookBuildFrom()), chapter2.getChapterId(), (((int) DBManager.getChapterCountByBookTableId(bookDB.get_id())) - chapter2.getChapterIndex().intValue()) + 1, chapter2.getChapterIndex());
                } else {
                    int intValue = (optInt6 - chapter.getChapterIndex().intValue()) + 1;
                    if (optString3 != null) {
                        boolean equals = optString3.equals("");
                        if (!equals) {
                            ToastUtil.getInstance().setText(optString3);
                            toDownload(bookDB.getBookId(), bookDB.get_id(), String.valueOf(bookDB.getBookBuildFrom()), chapter2.getChapterId(), intValue, chapter2.getChapterIndex());
                        }
                    }
                    ToastUtil.getInstance().setText("您已成功购买了" + optInt5 + "章," + str);
                    toDownload(bookDB.getBookId(), bookDB.get_id(), String.valueOf(bookDB.getBookBuildFrom()), chapter2.getChapterId(), intValue, chapter2.getChapterIndex());
                }
            }
        } else {
            int i3 = Constants.BUY_STATUS_SUCCS;
            if (buyFrom == i3) {
                Book bookDB2 = ChapterManager.getInstance().getBookDB();
                DBManager.insertBook(bookDB2);
                Chapter chapterByChapterId = DBManager.getChapterByChapterId(bookDB2.get_id().longValue(), this.chapterId);
                int parseInt2 = Integer.parseInt(bookDB2.getChargeType());
                if (chapterByChapterId != null) {
                    if (parseInt2 == 0) {
                        if (optString3 != null) {
                            boolean equals2 = optString3.equals("");
                            if (!equals2) {
                                ToastUtil.getInstance().setText(optString3);
                                toDownload(bookDB2.getBookId(), bookDB2.get_id(), String.valueOf(bookDB2.getBookBuildFrom()), chapterByChapterId.getChapterId(), (((int) DBManager.getChapterCountByBookTableId(bookDB2.get_id())) - chapterByChapterId.getChapterIndex().intValue()) + 1, chapterByChapterId.getChapterIndex());
                            }
                        }
                        ToastUtil.getInstance().setText("您已成功购买了全本," + str);
                        toDownload(bookDB2.getBookId(), bookDB2.get_id(), String.valueOf(bookDB2.getBookBuildFrom()), chapterByChapterId.getChapterId(), (((int) DBManager.getChapterCountByBookTableId(bookDB2.get_id())) - chapterByChapterId.getChapterIndex().intValue()) + 1, chapterByChapterId.getChapterIndex());
                    } else if (optInt5 == 1) {
                        if (optString3 != null) {
                            boolean equals3 = optString3.equals("");
                            if (!equals3) {
                                ToastUtil.getInstance().setText(optString3);
                                toDownload(bookDB2.getBookId(), bookDB2.get_id(), String.valueOf(bookDB2.getBookBuildFrom()), chapterByChapterId.getChapterId(), optInt5, chapterByChapterId.getChapterIndex());
                            }
                        }
                        ToastUtil.getInstance().setText("您已成功购买章节：" + chapterByChapterId.getName() + "," + str);
                        toDownload(bookDB2.getBookId(), bookDB2.get_id(), String.valueOf(bookDB2.getBookBuildFrom()), chapterByChapterId.getChapterId(), optInt5, chapterByChapterId.getChapterIndex());
                    } else {
                        if (optString3 != null) {
                            boolean equals4 = optString3.equals("");
                            if (!equals4) {
                                ToastUtil.getInstance().setText(optString3);
                                toDownload(bookDB2.getBookId(), bookDB2.get_id(), String.valueOf(bookDB2.getBookBuildFrom()), chapterByChapterId.getChapterId(), (optInt6 - chapterByChapterId.getChapterIndex().intValue()) + 2, chapterByChapterId.getChapterIndex());
                            }
                        }
                        ToastUtil.getInstance().setText("您已成功购买了" + optInt5 + "章," + str);
                        toDownload(bookDB2.getBookId(), bookDB2.get_id(), String.valueOf(bookDB2.getBookBuildFrom()), chapterByChapterId.getChapterId(), (optInt6 - chapterByChapterId.getChapterIndex().intValue()) + 2, chapterByChapterId.getChapterIndex());
                    }
                }
            } else {
                int i4 = Constants.PRE_BUY_STATUS_SUCCS;
                if (buyFrom == i4) {
                    Activity activity3 = this.mContent;
                    boolean z3 = activity3 instanceof BuyActivity;
                    if (z3) {
                        Intent intent2 = activity3.getIntent();
                        Intent intent3 = new Intent(Constants.PREDOWNLOAD_BUY_SUCCESS);
                        intent3.putExtra(BQConsts.bkey, intent2.getStringExtra(BQConsts.bkey));
                        intent3.putExtra("ckey", intent2.getStringExtra("ckey"));
                        intent3.putExtra("amount", intent2.getStringExtra("amount"));
                        EventBus.getDefault().post(new ReadingEvent(intent3));
                        SDKWrapUtil.sendBroadcast(this.mContent, intent3);
                    }
                } else {
                    int i5 = Constants.BUY_STATUS_SUCCS_FROM_CHAPTERLIST;
                    if (buyFrom == i5) {
                        Book bookIgnoreDelete = DBManager.getBookIgnoreDelete(this.mContent.getIntent().getStringExtra(BQConsts.bkey));
                        DBManager.insertBook(bookIgnoreDelete);
                        Chapter chapterByChapterId2 = DBManager.getChapterByChapterId(bookIgnoreDelete.get_id().longValue(), this.chapterId);
                        int parseInt3 = Integer.parseInt(bookIgnoreDelete.getChargeType());
                        if (chapterByChapterId2 != null) {
                            if (parseInt3 == 0) {
                                if (optString3 != null) {
                                    boolean equals5 = optString3.equals("");
                                    if (!equals5) {
                                        ToastUtil.getInstance().setText(optString3);
                                        toDownload(bookIgnoreDelete.getBookId(), bookIgnoreDelete.get_id(), String.valueOf(bookIgnoreDelete.getBookBuildFrom()), chapterByChapterId2.getChapterId(), (((int) DBManager.getChapterCountByBookTableId(bookIgnoreDelete.get_id())) - chapterByChapterId2.getChapterIndex().intValue()) + 1, chapterByChapterId2.getChapterIndex());
                                    }
                                }
                                ToastUtil.getInstance().setText("您已成功购买了全本," + str);
                                toDownload(bookIgnoreDelete.getBookId(), bookIgnoreDelete.get_id(), String.valueOf(bookIgnoreDelete.getBookBuildFrom()), chapterByChapterId2.getChapterId(), (((int) DBManager.getChapterCountByBookTableId(bookIgnoreDelete.get_id())) - chapterByChapterId2.getChapterIndex().intValue()) + 1, chapterByChapterId2.getChapterIndex());
                            } else if (optInt5 == 1) {
                                if (optString3 != null) {
                                    boolean equals6 = optString3.equals("");
                                    if (!equals6) {
                                        ToastUtil.getInstance().setText(optString3);
                                        toDownload(bookIgnoreDelete.getBookId(), bookIgnoreDelete.get_id(), String.valueOf(bookIgnoreDelete.getBookBuildFrom()), chapterByChapterId2.getChapterId(), optInt5, chapterByChapterId2.getChapterIndex());
                                    }
                                }
                                ToastUtil.getInstance().setText("您已成功购买章节：" + chapterByChapterId2.getName() + "," + str);
                                toDownload(bookIgnoreDelete.getBookId(), bookIgnoreDelete.get_id(), String.valueOf(bookIgnoreDelete.getBookBuildFrom()), chapterByChapterId2.getChapterId(), optInt5, chapterByChapterId2.getChapterIndex());
                            } else {
                                if (optString3 != null) {
                                    boolean equals7 = optString3.equals("");
                                    if (!equals7) {
                                        ToastUtil.getInstance().setText(optString3);
                                        toDownload(bookIgnoreDelete.getBookId(), bookIgnoreDelete.get_id(), String.valueOf(bookIgnoreDelete.getBookBuildFrom()), chapterByChapterId2.getChapterId(), (optInt6 - chapterByChapterId2.getChapterIndex().intValue()) + 2, chapterByChapterId2.getChapterIndex());
                                    }
                                }
                                ToastUtil.getInstance().setText("您已成功购买了" + optInt5 + "章," + str);
                                toDownload(bookIgnoreDelete.getBookId(), bookIgnoreDelete.get_id(), String.valueOf(bookIgnoreDelete.getBookBuildFrom()), chapterByChapterId2.getChapterId(), (optInt6 - chapterByChapterId2.getChapterIndex().intValue()) + 2, chapterByChapterId2.getChapterIndex());
                            }
                        }
                    } else {
                        int i6 = Constants.BUY_FULL_SUCCS;
                        if (buyFrom == i6) {
                            EventBus.getDefault().post(new ReadingEvent(new Intent(Constants.REFRESH_BOOK)));
                        }
                    }
                }
            }
        }
        Activity activity4 = this.mContent;
        boolean z4 = activity4 instanceof BuyActivity;
        if (!z4) {
            return true;
        }
        activity4.finish();
        this.mContent.overridePendingTransition(0, 0);
        return true;
    }

    private boolean closeActivity(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("closeActivity");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof BaseActivity;
        if (z) {
            ((BaseActivity) activity).quitActivity();
        } else {
            activity.finish();
        }
        Activity activity2 = this.mContent;
        boolean z2 = activity2 instanceof BuyActivity;
        if (!z2) {
            return true;
        }
        activity2.overridePendingTransition(0, 0);
        return true;
    }

    private boolean showAlipay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("alipay");
        if (optJSONObject == null) {
            return false;
        }
        optJSONObject.optString("url");
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString(Constants.HTTP_STATUS_OK);
        String optString3 = optJSONObject.optString("fail");
        String optString4 = optJSONObject.optString("createOrder");
        Activity activity = this.mContent;
        boolean z = activity instanceof PayListener;
        if (!z) {
            return true;
        }
        ((PayListener) activity).doPay(Constants.RECHARGE_FROM_APIPAY, optString, optString2, optString3, optString4, null);
        return true;
    }

    private boolean vipAlipay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("vip_alipay");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString("createOrder");
        String optString3 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        Activity activity = this.mContent;
        boolean z = activity instanceof VipPayListener;
        if (!z) {
            return true;
        }
        ((VipPayListener) activity).doVipPay(Constants.RECHARGE_FROM_APIPAY_NATIVE, optString, optString2, optString3);
        return true;
    }

    private boolean showWeixinpay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("weixin");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString("finish");
        String optString3 = optJSONObject.optString("fail");
        String optString4 = optJSONObject.optString("createOrder");
        String optString5 = optJSONObject.optString("from");
        Activity activity = this.mContent;
        boolean z = activity instanceof PayListener;
        if (!z) {
            return true;
        }
        ((PayListener) activity).doPay(Constants.RECHARGE_FROM_WEIXINPAY, optString, optString2, optString3, optString4, optString5);
        return true;
    }

    private boolean vipWeiXinMonthlyPay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("vip_weixin.monthly");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString("createOrder");
        String optString3 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        Activity activity = this.mContent;
        boolean z = activity instanceof VipPayListener;
        if (!z) {
            return true;
        }
        ((VipPayListener) activity).doVipPay(Constants.RECHARGE_FROM_WEIXINPAY_NATIVE, optString, optString2, optString3);
        return true;
    }

    private boolean vipWeiXinPay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("vip_weixin");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString("createOrder");
        String optString3 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        Activity activity = this.mContent;
        boolean z = activity instanceof VipPayListener;
        if (!z) {
            return true;
        }
        ((VipPayListener) activity).doVipPay(Constants.RECHARGE_FROM_WEIXINPAY_NATIVE, optString, optString2, optString3);
        return true;
    }

    private boolean vipHuaWeiPay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("vip_huawei_pay");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString("createOrder");
        String optString3 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        Activity activity = this.mContent;
        boolean z = activity instanceof VipPayListener;
        if (!z) {
            return true;
        }
        ((VipPayListener) activity).doVipPay(Constants.RECHARGE_FROM_HUAWEIPAY_NATIVE, optString, optString2, optString3);
        return true;
    }

    private boolean showHWpay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("huawei_pay");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString("finish");
        String optString3 = optJSONObject.optString("fail");
        String optString4 = optJSONObject.optString("createOrder");
        Activity activity = this.mContent;
        boolean z = activity instanceof PayListener;
        if (!z) {
            return true;
        }
        ((PayListener) activity).doPay(Constants.RECHARGE_FROM_HUAWEIPAY, optString, optString2, optString3, optString4, null);
        return true;
    }

    private boolean openVipSucc(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("openVipSucc");
        if (optJSONObject == null) {
            return false;
        }
        EventBus.getDefault().post(new VipOpenSuccEvent());
        UserManager.getInstance().getVipStatus();
        return true;
    }

    private boolean updateGlobalAutoBuyFlag(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("autoBuy");
        if (optJSONObject == null) {
            return false;
        }
        SpConfig.setAutoBuyByGlobal(true);
        return true;
    }

    private boolean autoBuy(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("autoBuyStatus");
        boolean z = false;
        if (optJSONObject == null) {
            return false;
        }
        int optInt = optJSONObject.optInt("checked");
        String optString = optJSONObject.optString(BQConsts.bkey);
        BookManager instance = BookManager.getInstance();
        if (optInt == 1) {
            z = true;
        }
        instance.setBookAutoBuy(optString, z);
        return true;
    }

    private boolean needLogin(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("needLogin");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof BuyActivity;
        if (z) {
            ((BuyActivity) activity).forceLoginDialog();
            return true;
        }
        int buyFrom = SpConfig.getBuyFrom();
        Activity activity2 = this.mContent;
        int i = Constants.BOOKINFO_BUY_STATUS_SUCCS;
        Utils.goToLoginV2Activity(activity2, buyFrom == i ? 20 : 1);
        this.mContent.finish();
        return true;
    }

    private boolean updateTitle(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("title");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("title");
        Activity activity = this.mContent;
        boolean z = activity instanceof UpdateTitleListener;
        if (!z) {
            return true;
        }
        ((UpdateTitleListener) activity).updateTitle(optString);
        return true;
    }

    private boolean evokeShareInfo(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("shareInfo");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("img_url");
        String optString2 = optJSONObject.optString("web_url");
        String optString3 = optJSONObject.optString(Constants.SHARE_TYPE);
        String optString4 = optJSONObject.optString("title");
        String optString5 = optJSONObject.optString("descr");
        String optString6 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        boolean isEmpty = StringUtil.isEmpty(optString);
        if (isEmpty) {
            return false;
        }
        boolean isEmpty2 = StringUtil.isEmpty(optString2);
        if (isEmpty2) {
            return false;
        }
        Class<?> cls = this.mContent.getClass();
        if (CategoryActivity.class != cls) {
            return true;
        }
        ((CategoryActivity) this.mContent).evokeShareInfo(optString3, optString, optString2, optString4, optString5, optString6);
        return true;
    }

    private boolean sendSms(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("sms");
        if (optJSONObject == null) {
            return false;
        }
        SmsManager.getDefault().sendTextMessage(optJSONObject.optString("number"), null, optJSONObject.optString("sms"), PendingIntent.getBroadcast(this.mContent, 0, new Intent(SendMessageBroadcastReceiver.SENT_SMS_ACTION), 0), null);
        return true;
    }

    private boolean getMobile(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("getMobile");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof PayListener;
        if (!z) {
            return true;
        }
        ((PayListener) activity).getMobile();
        return true;
    }

    private boolean cmccMobile(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("cmccMobile");
        if (optJSONObject == null) {
            return false;
        }
        SpConfig.setRechargeSuccNumber(optJSONObject.optString(NetworkUtil.MOBILE));
        return true;
    }

    private boolean showToast(JSONObject jSONObject) {
        String str = null;
        if (jSONObject != null) {
            try {
                boolean has = jSONObject.has(Constants.MN_MAIN_TOAST);
                if (has) {
                    str = jSONObject.getString(Constants.MN_MAIN_TOAST);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        boolean isEmpty = TextUtils.isEmpty(str);
        if (isEmpty) {
            return false;
        }
        ToastUtil.getInstance().setText(str);
        return true;
    }

    private boolean feedback(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("feedback");
        if (optJSONObject == null) {
            return false;
        }
        boolean checkWifiAndGPRS = com.sogou.novel.utils.NetworkUtil.checkWifiAndGPRS();
        if (checkWifiAndGPRS) {
            try {
                String optString = optJSONObject.optString("text");
                String optString2 = optJSONObject.optString(NetworkUtil.MOBILE);
                String optString3 = optJSONObject.optString("backType");
                String optString4 = optJSONObject.optString("typeContent");
                int i = optJSONObject.getInt("type");
                boolean isEmpty = TextUtils.isEmpty(optString);
                boolean isEmpty2 = TextUtils.isEmpty(optString4);
                if (!isEmpty || !isEmpty2) {
                    boolean isEmpty3 = TextUtils.isEmpty(optString2);
                    if (isEmpty3) {
                        ToastUtil.getInstance().setText("亲，联系方式不能为空");
                        return true;
                    }
                    boolean isUserInfoNotMatch = isUserInfoNotMatch(optString2);
                    if (isUserInfoNotMatch) {
                        ToastUtil.getInstance().setText("亲，电话号码或邮箱有错!");
                        return true;
                    } else if (i == 4) {
                        Book bookDB = ChapterManager.getInstance().getBookDB();
                        Chapter chapter = ChapterManager.getInstance().getCurrentChapter().chapterDB;
                        if (isEmpty || isEmpty2) {
                            if (!isEmpty) {
                                optString4 = "";
                            }
                            if (!isEmpty2) {
                                optString = optString4;
                            }
                        } else {
                            optString = optString4 + "/" + optString;
                        }
                        new UserFeedbackTask(this.mContent, i) { // from class: com.sogou.novel.app.WebInfoInterface.6
                            /* access modifiers changed from: protected */
                            public void onPostExecute(Boolean bool) {
                                super.onPostExecute((AnonymousClass6) bool);
                                boolean booleanValue = bool.booleanValue();
                                if (booleanValue) {
                                    ToastUtil.getInstance().setText("感谢您的提交");
                                    WebInfoInterface.this.mContent.finish();
                                    return;
                                }
                                ToastUtil.getInstance().setText("抱歉提交失败");
                            }
                        }.execute(new String[]{optString, optString2, bookDB.getBookName(), bookDB.getAuthor(), bookDB.getLoc(), "" + chapter.getChapterIndex(), optString3});
                        return true;
                    } else {
                        new UserFeedbackTask(this.mContent, i) { // from class: com.sogou.novel.app.WebInfoInterface.7
                            /* access modifiers changed from: protected */
                            public void onPostExecute(Boolean bool) {
                                super.onPostExecute((AnonymousClass7) bool);
                                boolean booleanValue = bool.booleanValue();
                                if (booleanValue) {
                                    ToastUtil.getInstance().setText("感谢您的提交");
                                    WebInfoInterface.this.mContent.finish();
                                    return;
                                }
                                ToastUtil.getInstance().setText("抱歉提交失败");
                            }
                        }.execute(new String[]{optString, optString2});
                        return true;
                    }
                } else {
                    ToastUtil.getInstance().setText("亲，反馈信息不能为空");
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return true;
            }
        } else {
            ToastUtil.getInstance().setText(Application.getInstance().getString(R.string.string_http_no_net));
            return true;
        }
    }

    private boolean isUserInfoNotMatch(String str) {
        boolean matches = Pattern.compile("[0-9]*").matcher(str).matches();
        if (matches) {
            return false;
        }
        boolean isEmail = FileUtil.isEmail(str);
        return !isEmail;
    }

    private boolean pageType(JSONObject jSONObject) {
        String optString = jSONObject.optString("pageType");
        if (optString == null) {
            return false;
        }
        boolean equals = optString.equals("");
        if (equals) {
            return false;
        }
        boolean startsWith = optString.startsWith("recharge");
        if (startsWith) {
            Activity activity = this.mContent;
            boolean z = activity instanceof UpdateTitleListener;
            if (z) {
                ((UpdateTitleListener) activity).setRechargeType(0);
            }
        }
        boolean startsWith2 = optString.startsWith("buy");
        if (!startsWith2) {
            return true;
        }
        Activity activity2 = this.mContent;
        boolean z2 = activity2 instanceof UpdateTitleListener;
        if (!z2) {
            return true;
        }
        ((UpdateTitleListener) activity2).setRechargeType(1);
        return true;
    }

    private boolean startReading(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("startReading");
        if (optJSONObject == null) {
            return false;
        }
        boolean z = this.isStartReading;
        if (z) {
            return false;
        }
        String optString = optJSONObject.optString(BQConsts.bkey);
        String optString2 = optJSONObject.optString("from");
        BQUtil.setReadingFrom(optString, optString2);
        Activity activity = this.mContent;
        boolean z2 = activity instanceof StoreBookDetailActivity;
        if (z2) {
            ((StoreBookDetailActivity) activity).read();
            return true;
        }
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            ToastUtil.getInstance().setText("亲，获取书籍内容失败");
            return true;
        }
        Book bookIgnoreDelete = DBManager.getBookIgnoreDelete(optString);
        if (bookIgnoreDelete != null) {
            boolean isEmpty2 = TextUtils.isEmpty(optString2);
            if (!isEmpty2) {
                boolean equals = "search_result".equals(optString2);
                if (!equals) {
                    BQUtil.sendReadFrom(optString, optString2);
                    startReadAct(bookIgnoreDelete);
                    return true;
                }
            }
            BQUtil.sendReadFrom(optString, SpConfig.getSearchFrom());
            startReadAct(bookIgnoreDelete);
            return true;
        }
        this.isStartReading = true;
        TaskManager.startHttpDataRequset(SogouNovel.getInstance().getStoreBookInfo(optString), new Response() { // from class: com.sogou.novel.app.WebInfoInterface.8
            @Override // com.sogou.novel.network.http.Response
            public void onHttpReceiving(Request request, int i, int i2, String str) {
            }

            @Override // com.sogou.novel.network.http.Response
            public void onHttpOK(Request request, Object obj) {
                HashMap hashMap = (HashMap) obj;
                SearchData searchData = (SearchData) hashMap.get("returndata");
                if (searchData == null) {
                    ToastUtil.getInstance().setText(Application.getInstance().getString(R.string.toast_search_failed));
                    return;
                }
                WebInfoInterface.this.isStartReading = false;
                Book book = new Book(searchData);
                book.setViewAdFree(((Boolean) hashMap.get("isViewAdFree")).booleanValue());
                book.canViewAdFree = ((Boolean) hashMap.get("canViewAdFree")).booleanValue();
                book.canDisplayAd = ((Boolean) hashMap.get("canDisplayAd")).booleanValue();
                WebInfoInterface.this.startReadAct(book);
            }

            @Override // com.sogou.novel.network.http.Response
            public void onHttpError(Request request, LinkStatus linkStatus, String str) {
                WebInfoInterface.this.isStartReading = false;
            }

            @Override // com.sogou.novel.network.http.Response
            public void onHttpCancelled(Request request) {
                ToastUtil.getInstance().setText("亲，获取书籍内容失败");
                WebInfoInterface.this.isStartReading = false;
            }
        });
        return true;
    }

    private boolean openFreeBook(JSONObject jSONObject) {
        String optString = jSONObject.optString("pirated");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return false;
        }
        SearchData searchData = (SearchData) new Gson().fromJson(optString, (Class<Object>) SearchData.class);
        if (searchData == null) {
            ToastUtil.getInstance().setText("亲，打开书籍失败");
            return true;
        }
        Intent intent = new Intent(this.mContent, BookInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("keyword", searchData.getbookname());
        bundle.putParcelable("SearchData", searchData);
        intent.putExtras(bundle);
        this.mContent.startActivity(intent);
        return true;
    }

    private boolean freeBookStartReading(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("piratedStartReading");
        if (optJSONObject == null) {
            return false;
        }
        Class<?> cls = this.mContent.getClass();
        if (BookInfoActivity.class != cls) {
            return true;
        }
        SearchData searchData = ((BookInfoActivity) this.mContent).getmData();
        if (searchData == null) {
            ToastUtil.getInstance().setText("亲，获取书籍内容失败");
            return true;
        }
        Book bookIgnoreDelete = DBManager.getBookIgnoreDelete(searchData.getBook_id(), searchData.getBook_md());
        if (bookIgnoreDelete == null) {
            bookIgnoreDelete = new Book(searchData);
        }
        startReadAct(bookIgnoreDelete);
        return true;
    }

    private boolean showStoreCategory(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("cate");
        if (optJSONObject == null) {
            return false;
        }
        try {
            String string = optJSONObject.getString("url");
            try {
                new URI(string);
                DataSendUtil.sendData(this.mContent, "14106", string, optJSONObject.optString("title"));
                Intent intent = new Intent(this.mContent, CategoryActivity.class);
                intent.putExtra(Constants.PARM_STORE_URL, string);
                intent.putExtra(Constants.PARM_CATEGORY_TITLE, optJSONObject.optString("title"));
                intent.putExtra(Constants4Inner.PARAM_IS_DIRECT, optJSONObject.optBoolean(Constants4Inner.PARAM_IS_DIRECT, false));
                this.mContent.startActivity(intent);
                this.mContent.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_scale);
                boolean z = this.mContent instanceof CategoryActivity;
                if (!z) {
                    return true;
                }
                this.mContent.finish();
                return true;
            } catch (Exception unused) {
                ToastUtil.getInstance().setText("啊哦，出错了，请稍后再试");
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean showChapterStoreList(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("dir");
        if (optJSONObject == null) {
            return false;
        }
        boolean checkWifiAndGPRS = com.sogou.novel.utils.NetworkUtil.checkWifiAndGPRS();
        if (checkWifiAndGPRS) {
            Activity activity = this.mContent;
            boolean z = activity instanceof ShowChapterListener;
            if (z) {
                ((ShowChapterListener) activity).showChapterList();
                return true;
            }
        }
        ToastUtil.getInstance().setText(Application.getInstance().getString(R.string.string_http_no_net));
        return true;
    }

    private boolean showFreeBookChapterList(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("piratedDir");
        if (optJSONObject == null) {
            return false;
        }
        boolean checkWifiAndGPRS = com.sogou.novel.utils.NetworkUtil.checkWifiAndGPRS();
        if (checkWifiAndGPRS) {
            Class<?> cls = this.mContent.getClass();
            if (BookInfoActivity.class != cls) {
                return true;
            }
            SearchData searchData = ((BookInfoActivity) this.mContent).getmData();
            if (searchData == null) {
                ToastUtil.getInstance().setText("打开目录页失败，请您重试");
                return true;
            }
            DataSendUtil.sendData(this.mContent, "4000", "4", "1");
            Intent intent = new Intent(this.mContent, ChapterStoreListActivity.class);
            Bundle bundle = new Bundle();
            Book book = new Book(((BookInfoActivity) this.mContent).getmData());
            Book bookIgnoreDelete = DBManager.getBookIgnoreDelete(book.getBookId(), book.getMd());
            if (bookIgnoreDelete == null) {
                book.setIsDeleted(true);
                book.setUpdateTime(PackageUtil.getCurrentFormatDay());
                DBManager.insertBook(book);
            }
            bundle.putString(Constants.CHAPTERLISTBOOKID, book.getBookId());
            bundle.putString("from", Constants.CHAPTERLISTFROMWEB);
            boolean isEmpty = TextUtils.isEmpty(book.getMd());
            if (!isEmpty) {
                bundle.putString(Constants.CHAPTERLISTMD, book.getMd());
            }
            intent.putExtras(bundle);
            this.mContent.startActivity(intent);
            this.mContent.overridePendingTransition(R.anim.slide_in_from_left, 0);
            return true;
        }
        ToastUtil.getInstance().setText(Application.getInstance().getString(R.string.string_http_no_net));
        return true;
    }

    public void onJS(String str) {
        Logger.e("yuanye:" + str);
        try {
            boolean showError = showError(str);
            if (!showError) {
                boolean gotoSearch = gotoSearch(str);
                if (!gotoSearch) {
                    boolean startAApp = startAApp(str);
                    if (!startAApp) {
                        JSONObject jSONObject = new JSONObject(str);
                        interuptMainViewPager(jSONObject.opt("stopSwipe"));
                        this.from = jSONObject.optString("s");
                        JSONObject optJSONObject = jSONObject.optJSONObject("book");
                        hideBottomBar(jSONObject);
                        if (optJSONObject != null) {
                            showBookInfo(optJSONObject);
                            insertHistory(optJSONObject);
                            return;
                        }
                        boolean uploadSearchHistoryItem = uploadSearchHistoryItem(jSONObject);
                        if (!uploadSearchHistoryItem) {
                            boolean deleteSearchHistoryItems = deleteSearchHistoryItems(jSONObject);
                            if (!deleteSearchHistoryItems) {
                                boolean shelfLastReadedBooks = getShelfLastReadedBooks(jSONObject);
                                if (!shelfLastReadedBooks) {
                                    boolean showBuyGift = showBuyGift(jSONObject);
                                    if (!showBuyGift) {
                                        boolean z = touchConflict(jSONObject);
                                        if (!z) {
                                            boolean shelfAdsInfo = getShelfAdsInfo(jSONObject);
                                            if (shelfAdsInfo) {
                                                Log.e(com.sogou.udp.push.common.Constants.TAG, "return");
                                                return;
                                            }
                                            boolean aduInfo = toAduInfo(jSONObject);
                                            if (aduInfo) {
                                                Log.e(com.sogou.udp.push.common.Constants.TAG, "return");
                                                return;
                                            }
                                            boolean addBooksToShelf = addBooksToShelf(jSONObject);
                                            if (!addBooksToShelf) {
                                                boolean addYDBooks2Shelf = addYDBooks2Shelf(jSONObject);
                                                if (!addYDBooks2Shelf) {
                                                    boolean YDreadChapter = YDreadChapter(jSONObject);
                                                    if (!YDreadChapter) {
                                                        boolean booksOnShelf = getBooksOnShelf(jSONObject);
                                                        if (!booksOnShelf) {
                                                            boolean showChapterStoreList = showChapterStoreList(jSONObject);
                                                            if (!showChapterStoreList) {
                                                                boolean showStoreCategory = showStoreCategory(jSONObject);
                                                                if (!showStoreCategory) {
                                                                    boolean startActivity = startActivity(jSONObject);
                                                                    if (!startActivity) {
                                                                        boolean buySuccess = buySuccess(jSONObject);
                                                                        if (!buySuccess) {
                                                                            boolean closeActivity = closeActivity(jSONObject);
                                                                            if (!closeActivity) {
                                                                                boolean showAlipay = showAlipay(jSONObject);
                                                                                if (!showAlipay) {
                                                                                    boolean vipAlipay = vipAlipay(jSONObject);
                                                                                    if (!vipAlipay) {
                                                                                        boolean vipWeiXinPay = vipWeiXinPay(jSONObject);
                                                                                        if (!vipWeiXinPay) {
                                                                                            boolean vipWeiXinMonthlyPay = vipWeiXinMonthlyPay(jSONObject);
                                                                                            if (!vipWeiXinMonthlyPay) {
                                                                                                boolean vipHuaWeiPay = vipHuaWeiPay(jSONObject);
                                                                                                if (!vipHuaWeiPay) {
                                                                                                    boolean openVipSucc = openVipSucc(jSONObject);
                                                                                                    if (!openVipSucc) {
                                                                                                        boolean autoBuy = autoBuy(jSONObject);
                                                                                                        if (!autoBuy) {
                                                                                                            boolean needLogin = needLogin(jSONObject);
                                                                                                            if (!needLogin) {
                                                                                                                boolean updateTitle = updateTitle(jSONObject);
                                                                                                                if (!updateTitle) {
                                                                                                                    boolean evokeShareInfo = evokeShareInfo(jSONObject);
                                                                                                                    if (!evokeShareInfo) {
                                                                                                                        boolean sendSms = sendSms(jSONObject);
                                                                                                                        if (!sendSms) {
                                                                                                                            boolean mobile = getMobile(jSONObject);
                                                                                                                            if (!mobile) {
                                                                                                                                boolean cmccMobile = cmccMobile(jSONObject);
                                                                                                                                if (!cmccMobile) {
                                                                                                                                    boolean showToast = showToast(jSONObject);
                                                                                                                                    if (!showToast) {
                                                                                                                                        boolean feedback = feedback(jSONObject);
                                                                                                                                        if (!feedback) {
                                                                                                                                            boolean startReading = startReading(jSONObject);
                                                                                                                                            if (!startReading) {
                                                                                                                                                boolean pageType = pageType(jSONObject);
                                                                                                                                                if (!pageType) {
                                                                                                                                                    boolean rechargeSucc = rechargeSucc(jSONObject);
                                                                                                                                                    if (!rechargeSucc) {
                                                                                                                                                        boolean rechargePop = rechargePop(jSONObject);
                                                                                                                                                        if (!rechargePop) {
                                                                                                                                                            boolean rechargePopWeiXin = rechargePopWeiXin(jSONObject);
                                                                                                                                                            if (!rechargePopWeiXin) {
                                                                                                                                                                boolean rechargePopAlipay = rechargePopAlipay(jSONObject);
                                                                                                                                                                if (!rechargePopAlipay) {
                                                                                                                                                                    boolean reharge2BuyCheckSucc = reharge2BuyCheckSucc(jSONObject);
                                                                                                                                                                    if (!reharge2BuyCheckSucc) {
                                                                                                                                                                        boolean reharge2BuyCloseActivity = reharge2BuyCloseActivity(jSONObject);
                                                                                                                                                                        if (!reharge2BuyCloseActivity) {
                                                                                                                                                                            boolean showWeixinpay = showWeixinpay(jSONObject);
                                                                                                                                                                            if (!showWeixinpay) {
                                                                                                                                                                                boolean showHWpay = showHWpay(jSONObject);
                                                                                                                                                                                if (!showHWpay) {
                                                                                                                                                                                    boolean addFreeBook2Shelf = addFreeBook2Shelf(jSONObject);
                                                                                                                                                                                    if (!addFreeBook2Shelf) {
                                                                                                                                                                                        boolean showFreeBookChapterList = showFreeBookChapterList(jSONObject);
                                                                                                                                                                                        if (!showFreeBookChapterList) {
                                                                                                                                                                                            boolean freeBookStartReading = freeBookStartReading(jSONObject);
                                                                                                                                                                                            if (!freeBookStartReading) {
                                                                                                                                                                                                boolean checkIn = checkIn(jSONObject);
                                                                                                                                                                                                if (!checkIn) {
                                                                                                                                                                                                    boolean openFreeBook = openFreeBook(jSONObject);
                                                                                                                                                                                                    if (!openFreeBook) {
                                                                                                                                                                                                        boolean goYYB = goYYB(jSONObject);
                                                                                                                                                                                                        if (!goYYB) {
                                                                                                                                                                                                            boolean gotoXmlyDetail = gotoXmlyDetail(jSONObject);
                                                                                                                                                                                                            if (!gotoXmlyDetail) {
                                                                                                                                                                                                                boolean go2VRActivity = go2VRActivity(jSONObject);
                                                                                                                                                                                                                if (!go2VRActivity) {
                                                                                                                                                                                                                    boolean go2VRActivityNewestChapter = go2VRActivityNewestChapter(jSONObject);
                                                                                                                                                                                                                    if (!go2VRActivityNewestChapter) {
                                                                                                                                                                                                                        boolean gotoMainActivity = gotoMainActivity(jSONObject);
                                                                                                                                                                                                                        if (!gotoMainActivity) {
                                                                                                                                                                                                                            boolean gotoBannerActivity = gotoBannerActivity(jSONObject);
                                                                                                                                                                                                                            if (!gotoBannerActivity) {
                                                                                                                                                                                                                                boolean updateTitleIcon = updateTitleIcon(jSONObject);
                                                                                                                                                                                                                                if (!updateTitleIcon) {
                                                                                                                                                                                                                                    boolean updateGlobalAutoBuyFlag = updateGlobalAutoBuyFlag(jSONObject);
                                                                                                                                                                                                                                    if (!updateGlobalAutoBuyFlag) {
                                                                                                                                                                                                                                        boolean sendData = sendData(jSONObject);
                                                                                                                                                                                                                                        if (!sendData) {
                                                                                                                                                                                                                                            boolean isPreload = isPreload(jSONObject);
                                                                                                                                                                                                                                            if (!isPreload) {
                                                                                                                                                                                                                                                boolean freeAndBatchLoad = freeAndBatchLoad(jSONObject);
                                                                                                                                                                                                                                                if (!freeAndBatchLoad) {
                                                                                                                                                                                                                                                    boolean videoFreeBookDownload = videoFreeBookDownload(jSONObject);
                                                                                                                                                                                                                                                    if (!videoFreeBookDownload) {
                                                                                                                                                                                                                                                        boolean isBuyFromPreload = isBuyFromPreload(jSONObject);
                                                                                                                                                                                                                                                        if (!isBuyFromPreload) {
                                                                                                                                                                                                                                                            boolean sendLogAction = sendLogAction(jSONObject);
                                                                                                                                                                                                                                                            if (!sendLogAction) {
                                                                                                                                                                                                                                                                boolean showDialog = showDialog(jSONObject);
                                                                                                                                                                                                                                                                if (!showDialog) {
                                                                                                                                                                                                                                                                    boolean openWeixinMiniProg = openWeixinMiniProg(jSONObject);
                                                                                                                                                                                                                                                                    if (!openWeixinMiniProg) {
                                                                                                                                                                                                                                                                        boolean notifyStoreGuide = notifyStoreGuide(jSONObject);
                                                                                                                                                                                                                                                                        if (!notifyStoreGuide) {
                                                                                                                                                                                                                                                                            boolean clickAd = clickAd(jSONObject);
                                                                                                                                                                                                                                                                            if (!clickAd) {
                                                                                                                                                                                                                                                                                boolean showRewardVideoAd = showRewardVideoAd(jSONObject);
                                                                                                                                                                                                                                                                                if (!showRewardVideoAd) {
                                                                                                                                                                                                                                                                                    boolean regPageAdStatus = getRegPageAdStatus(jSONObject);
                                                                                                                                                                                                                                                                                    if (!regPageAdStatus) {
                                                                                                                                                                                                                                                                                        boolean openWeixinOauth = openWeixinOauth(jSONObject);
                                                                                                                                                                                                                                                                                        if (!openWeixinOauth) {
                                                                                                                                                                                                                                                                                            boolean hasSignedIn = hasSignedIn(jSONObject);
                                                                                                                                                                                                                                                                                            if (!hasSignedIn) {
                                                                                                                                                                                                                                                                                                boolean searchTabchange = searchTabchange(jSONObject);
                                                                                                                                                                                                                                                                                                if (!searchTabchange) {
                                                                                                                                                                                                                                                                                                    boolean showVideoAd = showVideoAd(jSONObject);
                                                                                                                                                                                                                                                                                                    if (!showVideoAd) {
                                                                                                                                                                                                                                                                                                        boolean chapterFreeAdStatus = getChapterFreeAdStatus(jSONObject);
                                                                                                                                                                                                                                                                                                        if (!chapterFreeAdStatus) {
                                                                                                                                                                                                                                                                                                            boolean chapterPayDetail = getChapterPayDetail(jSONObject, str);
                                                                                                                                                                                                                                                                                                            if (!chapterPayDetail) {
                                                                                                                                                                                                                                                                                                                boolean openQQBrowser = openQQBrowser(jSONObject);
                                                                                                                                                                                                                                                                                                                if (!openQQBrowser) {
                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private boolean hideBottomBar(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("hideBottomBar");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof StoreBookDetailActivity;
        if (!z) {
            return true;
        }
        ((StoreBookDetailActivity) activity).hideBootomBar();
        return true;
    }

    private boolean freeAndBatchLoad(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("preloadFreeAndPurchased");
        if (optJSONObject == null) {
            return false;
        }
        Intent intent = this.mContent.getIntent();
        String stringExtra = intent.getStringExtra(BQConsts.bkey);
        boolean booleanExtra = intent.getBooleanExtra("fromBookInfo", false);
        boolean isEmpty = TextUtils.isEmpty(stringExtra);
        if (isEmpty) {
            return true;
        }
        if (booleanExtra) {
            ToastUtil.getInstance().setText("下载成功");
        }
        Book book = (Book) this.mContent.getIntent().getParcelableExtra("bookInfo");
        if (book == null) {
            Book bookDB = ChapterManager.getInstance().getBookDB();
            if (bookDB != null) {
                book = ChapterManager.getInstance().getBookDB();
            } else {
                book = new Book();
                book.setBookId(stringExtra);
            }
        }
        BookDownloadManager.getInstance().postBatchDownloadTask(book);
        EventBus.getDefault().post(new AddBookEvent(stringExtra));
        this.mContent.finish();
        return true;
    }

    private boolean videoFreeBookDownload(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("preloadAllChapter");
        if (optJSONObject == null) {
            return false;
        }
        Intent intent = this.mContent.getIntent();
        final String stringExtra = intent.getStringExtra(BQConsts.bkey);
        boolean isEmpty = TextUtils.isEmpty(stringExtra);
        if (isEmpty) {
            stringExtra = intent.getStringExtra("bookKey");
        }
        AdInnerConfig adInnerConfig = SNAdManagerPlugin.getInstance().getAdInnerConfig(this.mContent, SNAdLocation.DOWNLOAD_BOOK_FREE_VIDEO.getName());
        String[] split = SpConfig.getVideoDownloadAdTime().split("_");
        boolean check = Empty.check((Object[]) split);
        if (!check) {
            int length = split.length;
            if (length == 2) {
                int parseInt = Integer.parseInt(split[0]);
                int currentFormatDay = TimeUtil.getCurrentFormatDay();
                if (parseInt != currentFormatDay) {
                    this.downloadBookAdTime = 0;
                    this.downloadBookIds = "";
                } else {
                    this.downloadBookAdTime = Integer.parseInt(split[1]);
                    this.downloadBookIds = SpConfig.getVideoDownloadFreeBookIds();
                }
            }
        }
        int i = this.downloadBookAdTime;
        int video_limit = adInnerConfig.getVideo_limit();
        if (i >= video_limit) {
            boolean contains = this.downloadBookIds.contains(stringExtra);
            if (!contains) {
                ToastUtil.getInstance().setText("今日已到达上限");
                return true;
            }
        }
        VideoAdManager videoAdManager = new VideoAdManager();
        String name = SNAdLocation.DOWNLOAD_BOOK_FREE_VIDEO.getName();
        Activity activity = this.mContent;
        videoAdManager.showRewardVideoAd(name, activity, new DefaultVideoAdListener(activity) { // from class: com.sogou.novel.app.WebInfoInterface.9
            @Override // com.sogou.novel.reader.ad.DefaultVideoAdListener, com.sogou.reader.doggy.ad.manager.VideoAdManager.DefaultVideoAdListener, com.sogou.reader.doggy.ad.listener.SNRewardVideoListener
            public void onReward(String str) {
                BQLogAgent.onEvent(BQConsts.video_ad_book_download.video_complete);
                boolean contains2 = WebInfoInterface.this.downloadBookIds.contains(stringExtra);
                if (!contains2) {
                    WebInfoInterface webInfoInterface = WebInfoInterface.this;
                    webInfoInterface.downloadBookIds = WebInfoInterface.this.downloadBookIds + "_" + stringExtra;
                    WebInfoInterface.access$308(WebInfoInterface.this);
                    SpConfig.setVideoDownloadAdTime(TimeUtil.getCurrentFormatDay() + "_" + WebInfoInterface.this.downloadBookAdTime);
                    SpConfig.setVideoDownloadFreeBookIds(WebInfoInterface.this.downloadBookIds);
                }
                boolean isEmpty2 = TextUtils.isEmpty(stringExtra);
                if (!isEmpty2) {
                    Book book = (Book) WebInfoInterface.this.mContent.getIntent().getParcelableExtra("bookInfo");
                    if (book == null) {
                        Book bookDB = ChapterManager.getInstance().getBookDB();
                        if (bookDB != null) {
                            book = ChapterManager.getInstance().getBookDB();
                        } else {
                            book = new Book();
                            book.setBookId(stringExtra);
                        }
                    }
                    BookDownloadManager.getInstance().postBatchDownloadTask(book, true);
                    EventBus.getDefault().post(new AddBookEvent(stringExtra));
                    WebInfoInterface.this.mContent.finish();
                }
            }
        });
        return true;
    }

    private boolean isPreload(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("preload");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("ckey", null);
        int optInt = optJSONObject.optInt("amount", 0);
        Activity activity = this.mContent;
        boolean z = activity instanceof BuyActivity;
        if (!z) {
            return true;
        }
        Intent intent = activity.getIntent();
        boolean booleanExtra = intent.getBooleanExtra("fromBookInfo", false);
        String stringExtra = intent.getStringExtra(BQConsts.bkey);
        boolean isEmpty = TextUtils.isEmpty(stringExtra);
        if (isEmpty) {
            return true;
        }
        BQUtil.sendReadFromEvent(stringExtra, BQConsts.reading.read_buy_count);
        if (booleanExtra) {
            this.mContent.finish();
            EventBus.getDefault().post(new DownloadBookEvent(stringExtra, optString, optInt));
            return true;
        }
        EventBus.getDefault().post(new AddBookEvent(stringExtra));
        Intent intent2 = new Intent(Constants.PREDOWNLOAD_BUY_SUCCESS);
        intent2.putExtra(BQConsts.bkey, stringExtra);
        intent2.putExtra("ckey", optString);
        intent2.putExtra("amount", Integer.toString(optInt));
        EventBus.getDefault().post(new ReadingEvent(intent2));
        EventBus.getDefault().post(new BuySuccessEvent());
        this.mContent.finish();
        return true;
    }

    private boolean isBuyFromPreload(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("buyFromPreload");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("url");
        Activity activity = this.mContent;
        boolean z = activity instanceof BuyActivity;
        if (!z) {
            return true;
        }
        ((BuyActivity) activity).loadUrl(optString);
        return true;
    }

    private boolean sendData(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("sendData");
        if (optJSONObject == null) {
            return false;
        }
        DataSendUtil.sendData(Application.getInstance(), optJSONObject.optString("parentId"), optJSONObject.optString("content"), optJSONObject.optString("action"));
        return true;
    }

    private boolean updateTitleIcon(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("searchIcon");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof UpdateIconListener;
        if (!z) {
            return true;
        }
        ((UpdateIconListener) activity).updateSearchIcon();
        return true;
    }

    private boolean uploadSearchHistoryItem(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("searchHistory");
        if (optJSONObject == null) {
            return false;
        }
        WebView webView = this.mWeb;
        webView.loadUrl("javascript:Acb." + optJSONObject.optString(TencentOpenHost.CALLBACK) + l.s + StringUtil.ArraytoString(null) + l.t);
        return true;
    }

    private boolean go2VRActivityNewestChapter(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("ToNewestPiratedChapter");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("json");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (!isEmpty) {
            try {
                JSONObject jSONObject2 = new JSONObject(optString);
                SearchData searchData = new SearchData();
                searchData.setbookname(jSONObject2.optString("book_name"));
                searchData.setauthor_name(jSONObject2.optString("author"));
                searchData.setBook_id(jSONObject2.optString("id"));
                searchData.setBook_md(jSONObject2.optString(IXAdRequestInfo.TEST_MODE));
                searchData.setCategoryName(jSONObject2.optString("type"));
                searchData.setloc(jSONObject2.optInt("loc"));
                searchData.isVR = jSONObject2.optInt("isVR");
                searchData.vrUrl = jSONObject2.optString("vrurl");
                searchData.setsite(jSONObject2.optString("sourceUrl"));
                searchData.setpicurl(jSONObject2.optString(SocialConstants.PARAM_APP_ICON));
                searchData.setAuthorMd5(jSONObject2.optString("authorMD5"));
                searchData.setstatus(Integer.valueOf(jSONObject2.optString("status")).intValue());
                searchData.setdesc(jSONObject2.optString("info"));
                jSONObject2.optString("newestChapterUrl");
                jSONObject2.optString("newestChapterMd");
                WebBook webBook = new WebBook();
                webBook.setBookId(jSONObject2.optString("id"));
                webBook.setName(jSONObject2.optString("book_name"));
                webBook.setAuthor(jSONObject2.optString("author"));
                webBook.setCover(jSONObject2.optString(SocialConstants.PARAM_APP_ICON));
                webBook.setLoc("6");
                webBook.setLastReadTime(System.currentTimeMillis());
                webBook.setIntro(jSONObject2.optString("info"));
                PirateData pirateData = new PirateData();
                pirateData.setAuthorMd5(jSONObject2.optString("authorMD5"));
                pirateData.setCurrentChapterUrl(jSONObject2.optString("vrurl"));
                pirateData.setSite(jSONObject2.optString("sourceUrl"));
                pirateData.setVrUrl(jSONObject2.optString("vrurl"));
                webBook.setPirate(pirateData.toString());
                ARouter.getInstance().build("/reader/open").withString("book", new Gson().toJson(webBook)).withTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left).navigation(this.mContent);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return true;
            }
        } else {
            ToastUtil.getInstance().setText(R.string.toast_search_failed);
            return true;
        }
    }

    private boolean go2VRActivity(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("SearchPiratedInfo");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("json");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (!isEmpty) {
            Object obj = null;
            boolean z = obj instanceof SearchData;
            if (z) {
                Logger.e("");
            }
            try {
                JSONObject jSONObject2 = new JSONObject(optString);
                SearchData searchData = new SearchData();
                searchData.setbookname(jSONObject2.optString("book_name"));
                searchData.setauthor_name(jSONObject2.optString("author"));
                searchData.setBook_id(jSONObject2.optString("id"));
                searchData.setBook_md(jSONObject2.optString(IXAdRequestInfo.TEST_MODE));
                searchData.setCategoryName(jSONObject2.optString("type"));
                searchData.setloc(jSONObject2.optInt("loc"));
                searchData.isVR = jSONObject2.optInt("isVR");
                searchData.vrUrl = jSONObject2.optString("vrurl");
                searchData.setsite(jSONObject2.optString("sourceUrl"));
                searchData.setpicurl(jSONObject2.optString(SocialConstants.PARAM_APP_ICON));
                searchData.setAuthorMd5(jSONObject2.optString("authorMD5"));
                searchData.setstatus(Integer.valueOf(jSONObject2.optString("status")).intValue());
                searchData.setdesc(jSONObject2.optString("info"));
                WebBook webBook = new WebBook();
                webBook.setBookId(jSONObject2.optString("id"));
                webBook.setName(jSONObject2.optString("book_name"));
                webBook.setAuthor(jSONObject2.optString("author"));
                webBook.setCover(jSONObject2.optString(SocialConstants.PARAM_APP_ICON));
                webBook.setLoc("6");
                webBook.setLastReadTime(System.currentTimeMillis());
                webBook.setIntro(jSONObject2.optString("info"));
                PirateData pirateData = new PirateData();
                pirateData.setAuthorMd5(jSONObject2.optString("authorMD5"));
                pirateData.setCurrentChapterUrl(jSONObject2.optString("vrurl"));
                pirateData.setSite(jSONObject2.optString("sourceUrl"));
                pirateData.setVrUrl(jSONObject2.optString("vrurl"));
                webBook.setPirate(pirateData.toString());
                ARouter.getInstance().build("/reader/open").withString("book", new Gson().toJson(webBook)).withTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left).navigation(this.mContent);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return true;
            }
        } else {
            ToastUtil.getInstance().setText(R.string.toast_search_failed);
            return true;
        }
    }

    private boolean deleteSearchHistoryItems(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("clearSearchHistory");
        return optJSONObject != null;
    }

    private boolean touchConflict(JSONObject jSONObject) {
        String optString = jSONObject.optString("touchConflict");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return false;
        }
        boolean equals = "true".equals(optString);
        if (equals) {
            this.mWeb.requestDisallowInterceptTouchEvent(true);
            return true;
        }
        this.mWeb.requestDisallowInterceptTouchEvent(false);
        return true;
    }

    private boolean showBuyGift(JSONObject jSONObject) {
        Log.v("buy", "showBuyGift root:" + jSONObject.toString());
        String optString = jSONObject.optString("preferential");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return false;
        }
        BuyActivity.setReturnUrl(optString);
        Log.v("buy", "returnUrl:" + optString);
        return true;
    }

    private void interuptMainViewPager(Object obj) {
        if (obj != null) {
            Intent intent = new Intent();
            intent.setAction("viewpager.scroll");
            SDKWrapUtil.sendBroadcast(this.mContent, intent);
        }
    }

    private boolean rechargeSucc(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("rechargeSucc");
        if (optJSONObject == null) {
            return false;
        }
        SpConfig.setBuySuccess(true);
        SpConfig.setIsUserRecharged(true);
        Activity activity = this.mContent;
        boolean z = activity instanceof RechargeActivity;
        if (!z) {
            return true;
        }
        ((RechargeActivity) activity).rechargeSucc();
        return true;
    }

    private boolean rechargePop(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("recharge2buyPop");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("url");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return true;
        }
        LimitPreferentialActivity.goToLimitPreferentialActivity(this.mContent, optString, SpConfig.getPaoPaoDownloadUrl1(), null, true);
        return true;
    }

    private boolean rechargePopWeiXin(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("recharge2buyWeixin");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString(BQConsts.bkey);
        String optString3 = optJSONObject.optString("ckey");
        String optString4 = optJSONObject.optString("amount");
        optJSONObject.optString(Constants.HTTP_STATUS_OK);
        optJSONObject.optString("fail");
        String optString5 = optJSONObject.optString("createOrder");
        String optString6 = optJSONObject.optString("check");
        boolean isEmpty = TextUtils.isEmpty(optString5);
        if (isEmpty) {
            return true;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof LimitPreferentialActivity;
        if (!z) {
            return true;
        }
        ((LimitPreferentialActivity) activity).setBuyInfo(optString2, optString3, optString4, optString6);
        ((LimitPreferentialActivity) this.mContent).pay(Constants.RECHARGE_FROM_WEIXINPAY_NATIVE, optString, optString5);
        return true;
    }

    private boolean rechargePopAlipay(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("recharge2buyAlipay");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rmb");
        String optString2 = optJSONObject.optString(BQConsts.bkey);
        String optString3 = optJSONObject.optString("ckey");
        String optString4 = optJSONObject.optString("amount");
        optJSONObject.optString(Constants.HTTP_STATUS_OK);
        optJSONObject.optString("fail");
        String optString5 = optJSONObject.optString("createOrder");
        String optString6 = optJSONObject.optString("check");
        boolean isEmpty = TextUtils.isEmpty(optString5);
        if (isEmpty) {
            return true;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof LimitPreferentialActivity;
        if (!z) {
            return true;
        }
        ((LimitPreferentialActivity) activity).setBuyInfo(optString2, optString3, optString4, optString6);
        ((LimitPreferentialActivity) this.mContent).pay(Constants.RECHARGE_FROM_APIPAY_NATIVE, optString, optString5);
        return true;
    }

    private boolean reharge2BuyCheckSucc(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("recharge2buyCheckSucc");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof LimitPreferentialActivity;
        if (!z) {
            return false;
        }
        ((LimitPreferentialActivity) activity).notifyPaySuccess();
        return true;
    }

    private boolean reharge2BuyCloseActivity(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("recharge2buyCloseActivity");
        if (optJSONObject == null) {
            return false;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof LimitPreferentialActivity;
        if (!z) {
            return false;
        }
        ((LimitPreferentialActivity) activity).notifyPaySuccess();
        return true;
    }

    /* JADX WARNING: Incorrect condition in loop: B:6:0x002c */
    private boolean addBooksToShelf(JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("add2Shelf");
        if (optJSONObject == null) {
            return false;
        }
        JSONArray optJSONArray = optJSONObject.optJSONArray(com.taobao.aranger.constant.Constants.PARAM_KEYS);
        String optString = optJSONObject.optString("succCallback");
        String optString2 = optJSONObject.optString("failCallback");
        String optString3 = optJSONObject.optString("from");
        if (optJSONArray == null) {
            return true;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < r5; i++) {
            arrayList.add(optJSONArray.getString(i));
        }
        addBooksToShelf(arrayList, optString, optString2, optString3);
        return true;
    }

    /* JADX WARNING: Incorrect condition in loop: B:6:0x001b */
    private boolean addYDBooks2Shelf(JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("YDadd2Shelf");
        if (optJSONObject == null) {
            return false;
        }
        JSONArray optJSONArray = optJSONObject.optJSONArray(com.taobao.aranger.constant.Constants.PARAM_KEYS);
        if (optJSONArray == null) {
            return true;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < r3; i++) {
            arrayList.add(optJSONArray.getString(i));
        }
        BookManager.getInstance().addYDBooks(arrayList);
        boolean isCollectBook = SpConfig.isCollectBook();
        if (isCollectBook) {
            return true;
        }
        SpConfig.setCollectBook(true);
        new CollectBookDialog(this.mContent).show();
        return true;
    }

    private boolean YDreadChapter(JSONObject jSONObject) throws JSONException {
        JSONObject optJSONObject = jSONObject.optJSONObject("YDreadChapter");
        if (optJSONObject == null) {
            return false;
        }
        int optInt = optJSONObject.optInt("chapterIndex");
        int optInt2 = optJSONObject.optInt("chapterCount");
        String optString = optJSONObject.optString("name");
        String optString2 = optJSONObject.optString("md5");
        String optString3 = optJSONObject.optString(BQConsts.bkey);
        BookManager.getInstance().updateYDBookReadHistory(optString3, optInt + "_" + optInt2 + "_" + optString + "_" + optString2 + "_" + optString3);
        return true;
    }

    private boolean toAduInfo(JSONObject jSONObject) {
        String optString = jSONObject.optString("adu");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return false;
        }
        StoreAdInfoBean storeAdInfoBean = (StoreAdInfoBean) new Gson().fromJson(optString, (Class<Object>) StoreAdInfoBean.class);
        if (storeAdInfoBean == null) {
            return true;
        }
        String appname = storeAdInfoBean.getAppname();
        int clicktype = (int) storeAdInfoBean.getClicktype();
        String link = storeAdInfoBean.getLink();
        switch (clicktype) {
            case 0:
                Intent intent = new Intent(CommonConstant.ACTION.HWID_SCHEME_URL);
                intent.setData(Uri.parse(link));
                this.mContent.startActivity(Intent.createChooser(intent, null));
                return true;
            case 1:
                boolean isPackageExist = PackageUtil.isPackageExist(appname, this.mContent);
                if (isPackageExist) {
                    PackageUtil.invokeThirdApp(this.mContent, appname);
                    return true;
                }
                Intent intent2 = new Intent(CommonConstant.ACTION.HWID_SCHEME_URL);
                intent2.setData(Uri.parse(link));
                this.mContent.startActivity(Intent.createChooser(intent2, null));
                return true;
            default:
                return true;
        }
    }

    private boolean getShelfAdsInfo(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("getAduInfo");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString(TencentOpenHost.CALLBACK);
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return true;
        }
        WebView webView = this.mWeb;
        webView.loadUrl("javascript:Acb." + optString + l.s + SNAdManager.getInstance().getBaseParams(this.mContent) + l.t);
        return true;
    }

    /* JADX WARNING: Incorrect condition in loop: B:16:0x005f */
    /* JADX WARNING: Incorrect condition in loop: B:23:0x0094 */
    /* JADX WARNING: Incorrect condition in loop: B:9:0x0034 */
    private boolean getBooksOnShelf(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("getShelf");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString(TencentOpenHost.CALLBACK);
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return true;
        }
        try {
            StringBuilder sb = new StringBuilder("{\"copyright\":[");
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            List<Book> loadAllBook = DBManager.loadAllBook();
            if (loadAllBook == null) {
                return true;
            }
            Iterator<Book> it = loadAllBook.iterator();
            while (r5) {
                Book next = it.next();
                int parseInt = Integer.parseInt(next.getLoc());
                if (parseInt == 4) {
                    arrayList.add(next.getBookId());
                } else {
                    arrayList2.add(next.getMd());
                }
            }
            Iterator it2 = arrayList.iterator();
            while (r5) {
                sb.append("\"");
                sb.append((String) it2.next());
                sb.append("\",");
            }
            int size = arrayList.size();
            if (size > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
            sb.append("],\"pirated\":[");
            Iterator it3 = arrayList2.iterator();
            while (r4) {
                sb.append("\"");
                sb.append((String) it3.next());
                sb.append("\",");
            }
            int size2 = arrayList2.size();
            if (size2 > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
            sb.append("]}");
            WebView webView = this.mWeb;
            webView.loadUrl("javascript:Acb." + optString + l.s + sb.toString() + l.t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /* JADX WARNING: Incorrect condition in loop: B:12:0x003a */
    private boolean getShelfLastReadedBooks(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("getShelfWithDetail");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString(TencentOpenHost.CALLBACK);
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return true;
        }
        try {
            List<Book> loadReadedStoreBook = DBManager.loadReadedStoreBook();
            boolean isEmpty2 = CollectionUtil.isEmpty(loadReadedStoreBook);
            if (isEmpty2) {
                return true;
            }
            int size = loadReadedStoreBook.size();
            if (size > 4) {
                loadReadedStoreBook = loadReadedStoreBook.subList(0, 4);
            }
            StringBuilder sb = new StringBuilder("{\"books\":[");
            Iterator<Book> it = loadReadedStoreBook.iterator();
            while (r3) {
                Book next = it.next();
                sb.append("{");
                sb.append("\"bkey\":");
                sb.append("\"");
                sb.append(next.getBookId());
                sb.append("\"");
                sb.append(",");
                sb.append("\"image\":");
                sb.append("\"");
                sb.append(next.getCover());
                sb.append("\"");
                sb.append(",");
                sb.append("\"name\":");
                sb.append("\"");
                sb.append(next.getBookName());
                sb.append("\"");
                sb.append(",");
                sb.append("\"haveupdate\":");
                sb.append(next.getIsUpdate());
                sb.append(i.d);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]}");
            WebView webView = this.mWeb;
            webView.loadUrl("javascript:Acb." + optString + l.s + sb.toString() + l.t);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private synchronized void addABookToShelf(String str, String str2, String str3, String str4) {
        boolean isBookOnShelf = DBManager.isBookOnShelf(str, null);
        if (isBookOnShelf) {
            WebView webView = this.mWeb;
            webView.loadUrl("javascript:Acb." + str2 + "([\"" + str + "\"])");
            return;
        }
        Class<?> cls = this.mContent.getClass();
        if (cls == CategoryActivity.class) {
            this.loadingLayout = (FrameLayout) this.mContent.findViewById(R.id.loadingView);
            this.loadingTextView = (TextView) this.mContent.findViewById(R.id.waiting_dialog_message);
            this.loadingLayout.setVisibility(0);
            this.loadingTextView.setText(R.string.adding_book);
        }
        GiftAddBookCallbackListener giftAddBookCallbackListener = new GiftAddBookCallbackListener(str, str2, str3);
        giftAddBookCallbackListener.from = str4;
        AddStoreBookManager.getInstance().setmAddBookCallbackLisener(giftAddBookCallbackListener);
        AddStoreBookManager.getInstance().addBook(str);
    }

    public class GiftAddBookCallbackListener implements AddStoreBookManager.AddBookCallbackLisener {
        public String from;

        public GiftAddBookCallbackListener(String str, String str2, String str3) {
            WebInfoInterface.this.bookey = str;
            WebInfoInterface.this.succCallString = str2;
            WebInfoInterface.this.failCallString = str3;
        }

        @Override // com.sogou.novel.home.bookshelf.AddStoreBookManager.AddBookCallbackLisener
        public void addSucc() {
            WebInfoInterface.this.mHandler.sendEmptyMessage(32);
            BQUtil.setReadingFrom(WebInfoInterface.this.bookey, this.from);
            Book book = DBManager.getBook(WebInfoInterface.this.bookey);
            if (book != null) {
                book.bookFrom = this.from;
                DBManager.updataOneBook(book);
            }
        }

        @Override // com.sogou.novel.home.bookshelf.AddStoreBookManager.AddBookCallbackLisener
        public void addFail(String str) {
            WebInfoInterface.this.mHandler.sendEmptyMessage(33);
        }
    }

    /* JADX WARNING: Incorrect condition in loop: B:5:0x000d */
    private void addBooksToShelf(List<String> list, String str, String str2, String str3) {
        if (list != null) {
            int size = list.size();
            if (size > 0) {
                for (int i = 0; i < r1; i++) {
                    addABookToShelf(list.get(i), str, str2, str3);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void startReadAct(Book book) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.INTENT_BOOK_INFO, book);
        intent.putExtras(bundle);
        intent.setClass(this.mContent, OpenBookActivity.class);
        this.mContent.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void insertHistory(JSONObject jSONObject) {
        boolean optBoolean = jSONObject.optBoolean("addTip", false);
        if (optBoolean) {
            String optString = jSONObject.optString("title");
            if (optString != null) {
                int length = optString.trim().length();
                if (length > 0) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.SEARCHFRAMERECEIVER);
                    intent.putExtra(Constants.SEARCHFRAMERECEIVERBOOKNAME, optString);
                    SDKWrapUtil.sendBroadcast(this.mContent, intent);
                }
            }
        }
    }

    private void startAppActivity(Uri uri) {
        boolean equals = uri.getScheme().equals(PushConstants.EXTRA_APPLICATION_PENDING_INTENT);
        if (equals) {
            boolean containsKey = sAppHostmap.containsKey(uri.getHost());
            if (containsKey) {
                Intent putExtra = new Intent(this.mContent, sAppHostmap.get(uri.getHost())).putExtra("app_path", uri.getPath());
                buildBundle(putExtra.getExtras(), uri);
                this.mContent.startActivity(putExtra);
                this.mContent.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_scale);
                this.mContent.finish();
            }
        }
    }

    private void buildBundle(Bundle bundle, Uri uri) {
        int length;
        String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery != null) {
            int i = 0;
            do {
                int indexOf = encodedQuery.indexOf(38, i);
                if (indexOf == -1) {
                    indexOf = encodedQuery.length();
                }
                int indexOf2 = encodedQuery.indexOf(61, i);
                if (indexOf2 > indexOf || indexOf2 == -1) {
                    indexOf2 = indexOf;
                }
                String substring = encodedQuery.substring(i, indexOf2);
                boolean containsKey = bundle.containsKey(substring);
                if (!containsKey && indexOf2 < indexOf) {
                    bundle.putString(substring, encodedQuery.substring(indexOf2, indexOf));
                }
                i = indexOf + 1;
                length = encodedQuery.length();
            } while (i < length);
        }
    }

    /* access modifiers changed from: protected */
    public void showBookInfo(JSONObject jSONObject) {
        DataSendUtil.sendData(this.mContent, "14105", "1", jSONObject.optString("book"));
        String optString = jSONObject.optString("url");
        String optString2 = jSONObject.optString("title");
        String str = StringUtil.getURLRequestParams(optString).get(BQConsts.bkey);
        String optString3 = jSONObject.optString("from");
        String str2 = str + "&" + BQConsts.read_position + ContainerUtils.KEY_VALUE_DELIMITER + optString3;
        boolean isEmpty = TextUtils.isEmpty(str);
        if (!isEmpty) {
            BQUtil.setReadingFrom(str, optString3);
            DataSendUtil.sendData(this.mContent, "14105", "2", optString2);
            Intent intent = new Intent(this.mContent, StoreBookDetailActivity.class);
            intent.putExtra("bookKey", str);
            intent.putExtra("bookKeyAndPosition", str2);
            intent.putExtra("bookUrl", optString + Application.getUserInfo(optString));
            intent.putExtra("fromWeb", true);
            intent.putExtra("title", optString2);
            String str3 = this.from;
            if (str3 != null) {
                boolean equals = str3.equals("");
                if (!equals) {
                    boolean isNumeric = FileUtil.isNumeric(this.from);
                    if (isNumeric) {
                        intent.putExtra("from", Integer.parseInt(this.from));
                    }
                }
            }
            this.mContent.startActivity(intent);
            this.mContent.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_scale);
            return;
        }
        boolean checkWifiAndGPRS = com.sogou.novel.utils.NetworkUtil.checkWifiAndGPRS();
        if (!checkWifiAndGPRS) {
            ToastUtil.getInstance().setText(Application.getInstance().getString(R.string.string_http_no_net));
            return;
        }
        DataSendUtil.sendData(this.mContent, "14105", "3", optString2);
        Intent intent2 = new Intent(this.mContent, SearchWebActivity.class);
        intent2.putExtra("querystring", optString2);
        String str4 = this.from;
        if (str4 != null) {
            boolean equals2 = str4.equals("");
            if (!equals2) {
                boolean isNumeric2 = FileUtil.isNumeric(this.from);
                if (isNumeric2) {
                    intent2.putExtra("from", Integer.parseInt(this.from));
                }
            }
        }
        this.mContent.startActivity(intent2);
        this.mContent.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_scale);
    }

    /* access modifiers changed from: package-private */
    public void startUpdateSvr(VersionBean versionBean, int i, boolean z) {
        Intent intent = new Intent(this.mContent, UpdateService.class);
        VersionData data = versionBean.getData();
        intent.putExtra("fileName", "SogouNovel_" + i + ".apk");
        intent.putExtra(UpdateService.URL_KEY1, data.getDownloadUrl1());
        intent.putExtra(UpdateService.URL_KEY2, data.getDownloadUrl2());
        intent.putExtra(UpdateService.ACN_KEY, this.mContent.getLocalClassName());
        intent.putExtra(UpdateService.MD5_KEY, data.getContentMd5());
        intent.putExtra("show_nofitication", z);
        this.mContent.startService(intent);
        DataSendUtil.sendData(this.mContent, "9", "3", "1");
    }

    /* access modifiers changed from: protected */
    public void gotoCategory(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString("url");
            DataSendUtil.sendData(this.mContent, "14106", string, jSONObject.getString("title"));
            Intent intent = new Intent(this.mContent, CategoryActivity.class);
            intent.putExtra(Constants.PARM_STORE_URL, string);
            intent.putExtra(Constants.PARM_CATEGORY_TITLE, jSONObject.getString("title"));
            this.mContent.startActivity(intent);
            this.mContent.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_scale);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean addFreeBook2Shelf(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("addPirated2Shelf");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("json");
        String optString2 = optJSONObject.optString("succCallback");
        String optString3 = optJSONObject.optString("failCallback");
        FreeBookSearchData freeBookSearchData = null;
        try {
            boolean isEmpty = TextUtils.isEmpty(optString);
            if (!isEmpty) {
                freeBookSearchData = (FreeBookSearchData) new Gson().fromJson(optString, (Class<Object>) FreeBookSearchData.class);
            }
            if (freeBookSearchData == null) {
                return true;
            }
            Book bookIgnoreDelete = DBManager.getBookIgnoreDelete(freeBookSearchData.getBook_id(), freeBookSearchData.getBook_md());
            if (bookIgnoreDelete != null) {
                boolean booleanValue = bookIgnoreDelete.getIsDeleted().booleanValue();
                if (booleanValue) {
                    bookIgnoreDelete.setIsDeleted(false);
                    bookIgnoreDelete.setNativeUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    bookIgnoreDelete.setLastReadTime(Long.valueOf(System.currentTimeMillis()));
                    bookIgnoreDelete.setUserTableId(Long.valueOf(UserManager.getInstance().getUserTableId()));
                    DBManager.insertBook(bookIgnoreDelete);
                    WebView webView = this.mWeb;
                    webView.loadUrl("javascript:Acb." + optString2 + "()");
                    return true;
                }
            }
            if (bookIgnoreDelete == null) {
                bookIgnoreDelete = new Book(freeBookSearchData);
            }
            bookIgnoreDelete.setNativeUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
            bookIgnoreDelete.setLastReadTime(Long.valueOf(System.currentTimeMillis()));
            bookIgnoreDelete.setUserTableId(Long.valueOf(UserManager.getInstance().getUserTableId()));
            DBManager.insertBook(bookIgnoreDelete);
            WebView webView = this.mWeb;
            webView.loadUrl("javascript:Acb." + optString2 + "()");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            WebView webView2 = this.mWeb;
            webView2.loadUrl("javascript:Acb." + optString3 + "()");
            return true;
        }
    }

    private boolean checkIn(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("reged");
        if (optJSONObject == null) {
            return false;
        }
        boolean isVisitor = UserManager.getInstance().isVisitor();
        if (isVisitor) {
            return true;
        }
        UserManager.getInstance().setUserCheckIn();
        ShelfBookCallBack.notifyRefreshAd();
        ((CategoryActivity) this.mContent).popCheckInView(optJSONObject.optInt("sd"));
        return true;
    }

    private boolean goYYB(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("go_yyb");
        if (optJSONObject == null) {
            return false;
        }
        ScoreWallManager.startScoreWall(this.mContent, UserManager.getInstance().isVisitor(), new ScoreWallManager.VisitorAction() { // from class: com.sogou.novel.app.-$$Lambda$WebInfoInterface$M--arixMDmswDgNJ5HCRDANgQ_I
            @Override // com.sogou.novel.scorewall.core.ScoreWallManager.VisitorAction
            public final void onVisitorClick() {
                WebInfoInterface.lambda$goYYB$0(WebInfoInterface.this);
            }
        });
        return true;
    }

    public static /* synthetic */ void lambda$goYYB$0(WebInfoInterface webInfoInterface) {
        ToastUtils.show(webInfoInterface.mContent, (int) R.string.user_login_please);
        Utils.goToLoginV2Activity(webInfoInterface.mContent, 0).setLoginKey(String.valueOf(36));
    }

    private boolean gotoXmlyDetail(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("xmly");
        if (optJSONObject == null) {
            return false;
        }
        JSONObject optJSONObject2 = optJSONObject.optJSONObject("info");
        if (optJSONObject2 == null) {
            return true;
        }
        Class<?> cls = this.mContent.getClass();
        if (cls == ListenWebActivity.class) {
            ((ListenWebActivity) this.mContent).toDetail(Long.valueOf(optJSONObject2.optString("id")).longValue());
            return true;
        }
        Class<?> cls2 = this.mContent.getClass();
        if (cls2 == SearchWebActivity.class) {
            AlbumDetailActivity.show(this.mContent, Long.valueOf(optJSONObject2.optString("id")).longValue());
            return true;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("albumID", Long.valueOf(optJSONObject2.optString("id")));
        FlutterRoutePath.openPageByUrl(this.mContent, Constants.FLU_AUDIO_ALBUM_DETAIL, hashMap);
        return true;
    }

    private boolean gotoMainActivity(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject(FlutterBoost.ConfigBuilder.DEFAULT_DART_ENTRYPOINT);
        if (optJSONObject == null) {
            return false;
        }
        switch (optJSONObject.optInt("idx")) {
            case 0:
                Activity activity = this.mContent;
                boolean z = activity instanceof MainActivity;
                if (z) {
                    ((MainActivity) activity).switchTab(Constants.TAB_BOOKSHELF);
                    return true;
                }
                ((BaseActivity) activity).backToShelf();
                return true;
            case 1:
                Activity activity2 = this.mContent;
                boolean z2 = activity2 instanceof MainActivity;
                if (z2) {
                    ((MainActivity) activity2).switchTab(Constants.TAB_STORE);
                    return true;
                }
                ((BaseActivity) activity2).backToStore();
                return true;
            case 2:
                Activity activity3 = this.mContent;
                boolean z3 = activity3 instanceof MainActivity;
                if (z3) {
                    ((MainActivity) activity3).switchTab(Constants.TAB_DISCOVERY);
                    return true;
                }
                ((BaseActivity) activity3).backToDiscovery();
                return true;
            case 3:
                Activity activity4 = this.mContent;
                boolean z4 = activity4 instanceof MainActivity;
                if (z4) {
                    ((MainActivity) activity4).switchTab(Constants.TAB_USER);
                    return true;
                }
                ((BaseActivity) activity4).backToUser();
                return true;
            default:
                Activity activity5 = this.mContent;
                boolean z5 = activity5 instanceof MainActivity;
                if (z5) {
                    ((MainActivity) activity5).switchTab(Constants.TAB_BOOKSHELF);
                    return true;
                }
                ((BaseActivity) activity5).backToShelf();
                return true;
        }
    }

    private boolean gotoBannerActivity(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("subPage");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("url");
        String optString2 = optJSONObject.optString("title");
        boolean optBoolean = optJSONObject.optBoolean(PassportConstant.INTENT_EXTRA_WEB_SHOW_TITLE);
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (!isEmpty) {
            Intent intent = new Intent(this.mContent, CategoryActivity.class);
            intent.putExtra(Constants.PARM_STORE_URL, optString + "&sgnavtrans=1&sgstatusbar=light");
            intent.putExtra(Constants.PARM_CATEGORY_TITLE, optString2);
            intent.putExtra(Constants.SHOW_TITLE, optBoolean);
            this.mContent.startActivity(intent);
            return true;
        }
        ToastUtil.getInstance().setText(R.string.refresh_chapters_no_data_error);
        return true;
    }

    private boolean sendLogAction(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("logAction");
        if (optJSONObject == null) {
            return false;
        }
        BQUtil.sendWebActionLog(optJSONObject.optString("type"), optJSONObject.optString("content"));
        return true;
    }

    private boolean showDialog(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("dialog");
        if (optJSONObject == null) {
            return false;
        }
        boolean equals = "paymentLimit".equals(optJSONObject.optString("type"));
        if (!equals) {
            return true;
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof BuyActivity;
        if (z) {
            ((BuyActivity) activity).showPaymentDialog();
            return true;
        }
        new PaymentRuleDialog(activity).show();
        return true;
    }

    private boolean openWeixinMiniProg(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("openXCX");
        if (optJSONObject == null) {
            return false;
        }
        openWeixinMiniPro(optJSONObject.optString("miniId"), optJSONObject.optString("path"), optJSONObject.optString(TencentOpenHost.CALLBACK));
        return true;
    }

    private boolean clickAd(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("clickAd");
        if (optJSONObject == null) {
            return false;
        }
        SNAdView sNAdView = new SNAdView((SNAdItem) new Gson().fromJson(optJSONObject.toString(), (Class<Object>) SNAdItem.class));
        sNAdView.setRootLayout(new View(this.mContent));
        sNAdView.performClick();
        return true;
    }

    private boolean showVideoAd(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("showVideoAd");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("url");
        String optString2 = optJSONObject.optString("location");
        String optString3 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        Activity activity = this.mContent;
        boolean z = activity instanceof GoldListener;
        if (z) {
            ((GoldListener) activity).showVideoAd(optString, optString2, optString3);
            return true;
        }
        boolean z2 = activity instanceof MainActivity;
        if (!z2) {
            return false;
        }
        ((MainActivity) activity).showVideoAd(optString, optString2, optString3);
        return true;
    }

    private boolean showRewardVideoAd(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("showRewardVideoAd");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("rewardUrl");
        String optString2 = optJSONObject.optString("location");
        String optString3 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        Activity activity = this.mContent;
        boolean z = activity instanceof GoldListener;
        if (z) {
            ((GoldListener) activity).showVideoAd(optString, optString2, optString3);
            return true;
        }
        boolean z2 = activity instanceof MainActivity;
        if (!z2) {
            return false;
        }
        ((MainActivity) activity).showVideoAd(optString, optString2, optString3);
        return true;
    }

    private boolean getChapterFreeAdStatus(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("getChapterFreeAdStatus");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString(TencentOpenHost.CALLBACK);
        Activity activity = this.mContent;
        boolean z = activity instanceof GoldListener;
        if (z) {
            ((GoldListener) activity).getChapterFreeAdStatus(optString);
            return true;
        }
        boolean z2 = activity instanceof MainActivity;
        if (!z2) {
            return false;
        }
        ((MainActivity) activity).getChapterFreeAdStatus(optString);
        return true;
    }

    private boolean getChapterPayDetail(JSONObject jSONObject, String str) {
        JSONObject optJSONObject = jSONObject.optJSONObject("chapterPayDetail");
        int i = 0;
        if (optJSONObject == null) {
            return false;
        }
        ChapterPayDetail chapterPayDetail = new ChapterPayDetail();
        chapterPayDetail.status = optJSONObject.optInt("status");
        chapterPayDetail.msg = optJSONObject.optString("msg");
        JSONArray optJSONArray = optJSONObject.optJSONArray("payRules");
        if (optJSONArray != null) {
            int length = optJSONArray.length();
            if (length > 0) {
                chapterPayDetail.payRules = new String[optJSONArray.length()];
                while (true) {
                    try {
                        int length2 = optJSONArray.length();
                        if (i >= length2) {
                            break;
                        }
                        chapterPayDetail.payRules[i] = optJSONArray.getString(i);
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        JSONObject optJSONObject2 = optJSONObject.optJSONObject("appPayDetailInfo");
        if (optJSONObject2 != null) {
            chapterPayDetail.appPayDetailInfo = new ChapterPayDetail.AppPayDetailInfo();
            chapterPayDetail.appPayDetailInfo.payCz = optJSONObject2.optInt("payCz");
            chapterPayDetail.appPayDetailInfo.payTotal = optJSONObject2.optInt("payTotal");
            chapterPayDetail.appPayDetailInfo.voucher = optJSONObject2.optInt("voucher");
            chapterPayDetail.appPayDetailInfo.payZs = optJSONObject2.optInt("payZs");
        }
        Activity activity = this.mContent;
        boolean z = activity instanceof BuyActivity;
        if (!z) {
            return true;
        }
        ((BuyActivity) activity).showChapterPayDialog(chapterPayDetail);
        return true;
    }

    private boolean openQQBrowser(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("openQQBrowser");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString("url");
        String optString2 = optJSONObject.optString("scheme");
        String optString3 = optJSONObject.optString("cut_scheme");
        final String optString4 = optJSONObject.optString(TencentOpenHost.CALLBACK);
        int openQQBrowser = QQBrowserUtil.openQQBrowser(this.mContent, optString, optString2, optString3, new FileDownloadManager.FileDownloadListener() { // from class: com.sogou.novel.app.WebInfoInterface.10
            @Override // com.sogou.commonlib.kits.FileDownloadManager.FileDownloadListener
            public void onProgress(int i) {
            }

            @Override // com.sogou.commonlib.kits.FileDownloadManager.FileDownloadListener
            public void onStart() {
                BQLogAgent.onEvent("js_40_2_1");
            }

            @Override // com.sogou.commonlib.kits.FileDownloadManager.FileDownloadListener
            public void onFail() {
                BQLogAgent.onEvent(BQConsts.QQBrowser.h5_qb_download_fail);
                Activity activity = WebInfoInterface.this.mContent;
                if (activity != null) {
                    boolean isDestroyed = WebInfoInterface.this.mContent.isDestroyed();
                    if (!isDestroyed) {
                        WebView webView = WebInfoInterface.this.mWeb;
                        if (webView != null) {
                            boolean isEmpty = TextUtils.isEmpty(optString4);
                            if (!isEmpty) {
                                WebView webView2 = WebInfoInterface.this.mWeb;
                                webView2.loadUrl("javascript:Acb." + optString4 + "(2)");
                            }
                        }
                    }
                }
            }

            @Override // com.sogou.commonlib.kits.FileDownloadManager.FileDownloadListener
            public void onFinish(File file) {
                BQLogAgent.onEvent("js_40_2_2");
                Activity activity = WebInfoInterface.this.mContent;
                if (activity != null) {
                    boolean isDestroyed = WebInfoInterface.this.mContent.isDestroyed();
                    if (!isDestroyed) {
                        WebView webView = WebInfoInterface.this.mWeb;
                        if (webView != null) {
                            boolean isEmpty = TextUtils.isEmpty(optString4);
                            if (!isEmpty) {
                                WebView webView2 = WebInfoInterface.this.mWeb;
                                webView2.loadUrl("javascript:Acb." + optString4 + "(1)");
                            }
                        }
                    }
                }
            }
        });
        if (openQQBrowser == 3) {
            BQLogAgent.onEvent(BQConsts.QQBrowser.h5_qb_scheme);
            this.mWeb.loadUrl("javascript:Acb." + optString4 + "(3)");
            return true;
        } else if (openQQBrowser != 4) {
            return true;
        } else {
            this.mWeb.loadUrl("javascript:Acb." + optString4 + "(4)");
            return true;
        }
    }

    private boolean getRegPageAdStatus(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("getRegPageStatus");
        if (optJSONObject == null) {
            return false;
        }
        String optString = optJSONObject.optString(TencentOpenHost.CALLBACK);
        String optString2 = optJSONObject.optString("location");
        boolean isEmpty = TextUtils.isEmpty(optString);
        if (isEmpty) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:Acb.");
        sb.append(optString);
        sb.append("(\"");
        boolean hasCheckInRewardVideo = SNAdManagerPlugin.getInstance().hasCheckInRewardVideo(this.mContent, optString2);
        sb.append(hasCheckInRewardVideo ? Constants.HTTP_STATUS_OK : "fail");
        sb.append("\")");
        this.mWeb.loadUrl(sb.toString());
        return true;
    }

    private boolean searchTabchange(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("searchTab");
        if (optJSONObject == null) {
            return false;
        }
        boolean check = Empty.check(optJSONObject.optString("position"));
        if (check) {
            return false;
        }
        EventBus.getDefault().post(new SearchTabChangeEvent(optJSONObject.optString("position")));
        return true;
    }

    private void toDownload(String str, Long l, String str2, String str3, int i, Integer num) {
        int buyFrom = SpConfig.getBuyFrom();
        int i2 = Constants.PRE_BUY_STATUS_SUCCS;
        if (buyFrom == i2) {
            Intent intent = this.mContent.getIntent();
            intent.setAction(Constants.PREDOWNLOAD_BUY_SUCCESS);
            EventBus.getDefault().post(new ReadingEvent(intent));
            return;
        }
        this.mBookDownloadListenerImpl = new StoreBookDownloadListenerImpl(str);
        this.mBookDownloadListenerImpl.setChapterIndex(num.intValue());
        this.mBookDownloadListenerImpl.setBookTableId(l.longValue());
        this.mBookDownloadListenerImpl.setCount(i);
        Downloader.getInstance().registDownloadListener(this.mBookDownloadListenerImpl);
        int buyFrom2 = SpConfig.getBuyFrom();
        int i3 = Constants.BUY_STATUS_SUCCS_FROM_CHAPTERLIST;
        if (buyFrom2 == i3) {
            Book bookIgnoreDelete = DBManager.getBookIgnoreDelete(l);
            String bookId = bookIgnoreDelete.getBookId();
            str2 = String.valueOf(bookIgnoreDelete.getBookBuildFrom());
            str = bookId;
        }
        Downloader.getInstance().downloadChapterContentOfStoreBook(str, str3, i, str2);
    }

    public class StoreBookDownloadListenerImpl extends DownloadListenerImpl {
        private long bookTableId = 0;
        private int chapterIndex = 1;
        private int count;

        public StoreBookDownloadListenerImpl(String str) {
            super(str);
        }

        public void setCount(int i) {
            this.count = i;
        }

        public void setBookTableId(long j) {
            this.bookTableId = j;
        }

        public void setChapterIndex(int i) {
            this.chapterIndex = i;
        }

        @Override // com.sogou.novel.reader.download.DownloadListener
        public void onOneChapterFinishDownload(int i, String str, String str2) {
            Logger.i("下载一章内容完成====" + str2);
            if (i == 1) {
                int buyFrom = SpConfig.getBuyFrom();
                int i2 = Constants.BUY_STATUS_SUCCS;
                if (buyFrom == i2) {
                    ReadProgress readProgress = new ReadProgress();
                    readProgress.setBookDBId(this.bookTableId);
                    readProgress.setChapterIndex(this.chapterIndex);
                    readProgress.setCurrentPosition(-100);
                    Intent intent = new Intent();
                    intent.setAction(Constants.REFRESH_BOOK);
                    intent.putExtra(Constants.REFRESH_BOOK_READ_PROGRESS, (Parcelable) readProgress);
                    SDKWrapUtil.sendBroadcast(WebInfoInterface.this.mContent, intent);
                    return;
                }
                int buyFrom2 = SpConfig.getBuyFrom();
                int i3 = Constants.BUY_STATUS_SUCCS_FROM_CHAPTERLIST;
                if (buyFrom2 == i3) {
                    Intent intent2 = new Intent();
                    Book bookIgnoreDelete = DBManager.getBookIgnoreDelete(Long.valueOf(this.bookTableId));
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.INTENT_BOOK_INFO, bookIgnoreDelete);
                    bundle.putString(Constants.START_CHAPTER_MD5, str2);
                    bundle.putBoolean(Constants.GET_AUTO_BOOKMARK_IGNORE_BOOK_IS_DELETE, true);
                    intent2.putExtras(bundle);
                    intent2.setClass(WebInfoInterface.this.mContent, OpenBookActivity.class);
                    WebInfoInterface.this.mContent.startActivity(intent2);
                    WebInfoInterface.this.mContent.finish();
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:7:0x0022, code lost:
            if (r1 == r2) goto L_0x0024;
         */
        @Override // com.sogou.novel.reader.download.DownloadListener
        public void onDownloadError(String str, String str2, LinkStatus linkStatus, String str3) {
            StoreBookDownloadListenerImpl storeBookDownloadListenerImpl = WebInfoInterface.this.mBookDownloadListenerImpl;
            if (storeBookDownloadListenerImpl != null) {
                SpConfig.setBuyFrom(0);
                int buyFrom = SpConfig.getBuyFrom();
                int i = Constants.BUY_NOTDOWNLOAD_STATUS_SUCCS;
                if (buyFrom != i) {
                    int buyFrom2 = SpConfig.getBuyFrom();
                    int i2 = Constants.BUY_STATUS_SUCCS;
                    if (buyFrom2 != i2) {
                        int buyFrom3 = SpConfig.getBuyFrom();
                        int i3 = Constants.BUY_STATUS_SUCCS_FROM_CHAPTERLIST;
                    }
                }
                int i4 = this.count;
                if (i4 > 1) {
                    ToastUtil.getInstance().setText("批量下载章节失败");
                } else {
                    ToastUtil.getInstance().setText("下载章节失败");
                }
                Downloader.getInstance().unRegistDownloadListener(WebInfoInterface.this.mBookDownloadListenerImpl);
                WebInfoInterface.this.mBookDownloadListenerImpl = null;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:7:0x001b, code lost:
            if (r1 == r2) goto L_0x001d;
         */
        @Override // com.sogou.novel.reader.download.DownloadListener
        public void onLastChapterFinishDownload(String str, String str2) {
            int i = this.count;
            if (i > 1) {
                int buyFrom = SpConfig.getBuyFrom();
                int i2 = Constants.BUY_NOTDOWNLOAD_STATUS_SUCCS;
                if (buyFrom != i2) {
                    int buyFrom2 = SpConfig.getBuyFrom();
                    int i3 = Constants.BUY_STATUS_SUCCS;
                    if (buyFrom2 != i3) {
                        int buyFrom3 = SpConfig.getBuyFrom();
                        int i4 = Constants.BUY_STATUS_SUCCS_FROM_CHAPTERLIST;
                    }
                }
                ToastUtil.getInstance().setText("批量下载章节完成");
            }
            SpConfig.setBuyFrom(0);
            StoreBookDownloadListenerImpl storeBookDownloadListenerImpl = WebInfoInterface.this.mBookDownloadListenerImpl;
            if (storeBookDownloadListenerImpl != null) {
                Downloader.getInstance().unRegistDownloadListener(WebInfoInterface.this.mBookDownloadListenerImpl);
                WebInfoInterface.this.mBookDownloadListenerImpl = null;
            }
        }
    }

    private boolean openWeixinOauth(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("getWeiXinOpenId");
        if (optJSONObject == null) {
            return false;
        }
        this.getOpenIdCallBack = optJSONObject.optString(TencentOpenHost.CALLBACK);
        boolean isEmpty = TextUtils.isEmpty(this.getOpenIdCallBack);
        if (isEmpty) {
            return true;
        }
        try {
            SocialApi.get(Application.getInstance()).doOauthVerify(this.mContent, PlatformType.WEIXIN, this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean hasSignedIn(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("signin");
        if (optJSONObject == null) {
            return false;
        }
        EventBus.getDefault().post(new SignedInEvent(true));
        return true;
    }

    @Override // com.sogou.novel.loginsdk.listener.AuthListener
    public void onComplete(PlatformType platformType, String str, String str2, String str3, String str4) {
        boolean isEmpty = TextUtils.isEmpty(str4);
        if (!isEmpty) {
            boolean isEmpty2 = TextUtils.isEmpty(this.getOpenIdCallBack);
            if (!isEmpty2) {
                WebView webView = this.mWeb;
                webView.loadUrl("javascript:Acb." + this.getOpenIdCallBack + "({\"status\":\"succ\", \"openid\":\"" + str4 + "\"})");
            }
        }
    }

    @Override // com.sogou.novel.loginsdk.listener.AuthListener
    public void onError(PlatformType platformType, int i, String str) {
        boolean isEmpty = TextUtils.isEmpty(this.getOpenIdCallBack);
        if (!isEmpty) {
            WebView webView = this.mWeb;
            webView.loadUrl("javascript:Acb." + this.getOpenIdCallBack + "({\"status\":\"fail\",\"openid\":\"\"})");
        }
    }
}
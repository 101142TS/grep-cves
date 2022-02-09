package com.sina.weibo.sdk.share;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.b.a;
import com.sina.weibo.sdk.b.c;
import com.sina.weibo.sdk.b.d;
import com.sina.weibo.sdk.b.e;
import com.tencent.qqlive.module.videoreport.collect.EventCollector;

public class ShareTransActivity extends BaseActivity {
    private Intent t;
    private FrameLayout u;
    private d v;
    private String w;
    private Handler x = new Handler(Looper.getMainLooper()) { // from class: com.sina.weibo.sdk.share.ShareTransActivity.1
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 1) {
                boolean z = message.obj instanceof Intent;
                if (z) {
                    ShareTransActivity.this.b((Intent) message.obj);
                    return;
                }
            }
            ShareTransActivity.this.m();
        }
    };

    @Override // com.sina.weibo.sdk.share.BaseActivity, android.app.Activity, android.view.Window.Callback
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        EventCollector.getInstance().onActivityDispatchTouchEvent(this, motionEvent, false, true);
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        EventCollector.getInstance().onActivityDispatchTouchEvent(this, motionEvent, dispatchTouchEvent, false);
        return dispatchTouchEvent;
    }

    @Override // com.sina.weibo.sdk.share.BaseActivity, android.app.Activity, android.content.ComponentCallbacks
    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        EventCollector.getInstance().onActivityConfigurationChanged(this, configuration);
    }

    /* access modifiers changed from: protected */
    @Override // com.sina.weibo.sdk.share.BaseActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        View view;
        super.onCreate(bundle);
        c.a("WBShareTag", "start share activity.");
        Intent intent = getIntent();
        this.t = intent;
        if (intent == null) {
            finish();
            return;
        }
        int intExtra = intent.getIntExtra("start_flag", -1);
        if (intExtra != 1001) {
            finish();
            return;
        }
        this.u = new FrameLayout(this);
        int intExtra2 = getIntent().getIntExtra("progress_id", -1);
        if (intExtra2 != -1) {
            view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(intExtra2, (ViewGroup) null);
        } else {
            view = new ProgressBar(this);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        this.u.addView(view, layoutParams);
        this.u.setBackgroundColor(855638016);
        setContentView(this.u);
        c.a("WBShareTag", "prepare wb resource.");
        Bundle extras = this.t.getExtras();
        if (extras == null) {
            finish();
            return;
        }
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.readFromBundle(extras);
        MultiImageObject multiImageObject = weiboMultiMessage.multiImageObject;
        if (multiImageObject == null) {
            VideoSourceObject videoSourceObject = weiboMultiMessage.videoSourceObject;
            if (videoSourceObject == null) {
                a(weiboMultiMessage);
                return;
            }
        }
        d dVar = this.v;
        if (dVar != null) {
            dVar.cancel(true);
        }
        d dVar2 = new d(this, new b() { // from class: com.sina.weibo.sdk.share.ShareTransActivity.2
            @Override // com.sina.weibo.sdk.share.b
            public final void a(c cVar) {
                ShareTransActivity.this.u.setVisibility(4);
                if (cVar == null) {
                    ShareTransActivity.this.d("Trans result is null.");
                    return;
                }
                boolean z = cVar.z;
                if (z) {
                    ShareTransActivity.this.a(cVar.A);
                    return;
                }
                boolean isEmpty = TextUtils.isEmpty(cVar.errorMessage);
                if (isEmpty) {
                    ShareTransActivity.this.d("Trans resource fail.");
                } else {
                    ShareTransActivity.this.d(cVar.errorMessage);
                }
            }
        });
        this.v = dVar2;
        dVar2.execute(weiboMultiMessage);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void a(WeiboMultiMessage weiboMultiMessage) {
        c.a("WBShareTag", "start wb composer");
        try {
            this.t.putExtra("start_flag", 1002);
            String g = d.g(String.valueOf((Math.random() * 10000.0d) + ((double) System.currentTimeMillis())));
            this.w = g;
            this.t.putExtra("share_back_flag", g);
            this.t.putExtra("share_flag_for_new_version", 1);
            Bundle extras = this.t.getExtras();
            Intent intent = new Intent("com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY");
            a.C0022a c = a.c(this);
            if (c != null) {
                intent.setPackage(c.packageName);
            }
            intent.putExtras(weiboMultiMessage.writeToBundle(extras));
            intent.putExtra("_weibo_sdkVersion", "0041005000");
            intent.putExtra("_weibo_appPackage", getPackageName());
            intent.putExtra("_weibo_appKey", com.sina.weibo.sdk.a.b().getAppKey());
            intent.putExtra("_weibo_flag", 538116905);
            intent.putExtra("_weibo_sign", d.g(e.b(this, getPackageName())));
            String stringExtra = this.t.getStringExtra("start_web_activity");
            boolean isEmpty = TextUtils.isEmpty(stringExtra);
            if (!isEmpty) {
                boolean equals = "com.sina.weibo.sdk.web.WebActivity".equals(stringExtra);
                if (equals) {
                    intent.setClassName(this, stringExtra);
                    startActivityForResult(intent, 10001);
                    return;
                }
            }
            boolean a2 = com.sina.weibo.sdk.a.a(this);
            if (a2) {
                if (c != null) {
                    intent.setPackage(c.packageName);
                }
                startActivityForResult(intent, 10001);
                return;
            }
            d("Start weibo client's composer fail. And Weibo client is not installed.");
        } catch (Throwable th) {
            c.b("WBShareTag", "start wb composer fail," + th.getMessage());
            d("Start weibo client's composer fail. " + th.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        c.a("WBShareTag", "onActivityResult. Means share result coming!");
        Handler handler = this.x;
        if (handler == null) {
            return;
        }
        if (i2 == -1) {
            Message obtain = Message.obtain(handler, 1);
            obtain.obj = intent;
            this.x.sendMessageDelayed(obtain, 100);
            return;
        }
        handler.sendEmptyMessageDelayed(0, 100);
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        c.a("WBShareTag", "start share activity again. Means share result coming!");
        int intExtra = intent.getIntExtra("start_flag", -1);
        if (intExtra != 1001) {
            if (intExtra == 1002) {
                b(intent);
            } else {
                m();
            }
        }
    }

    private boolean a(Intent intent) {
        boolean isEmpty = TextUtils.isEmpty(this.w);
        if (isEmpty || intent == null) {
            return false;
        }
        boolean containsKey = intent.getExtras().containsKey("share_back_flag");
        if (!containsKey) {
            return false;
        }
        boolean equals = TextUtils.equals(this.w, intent.getStringExtra("share_back_flag"));
        if (!equals) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void b(Intent intent) {
        FrameLayout frameLayout = this.u;
        if (frameLayout != null) {
            frameLayout.setVisibility(4);
        }
        Handler handler = this.x;
        if (handler != null) {
            handler.removeMessages(0);
            this.x = null;
        }
        boolean a2 = a(intent);
        if (!a2) {
            m();
            return;
        }
        setResult(-1, intent);
        finish();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void d(String str) {
        FrameLayout frameLayout = this.u;
        if (frameLayout != null) {
            frameLayout.setVisibility(4);
        }
        Handler handler = this.x;
        if (handler != null) {
            handler.removeMessages(0);
            this.x = null;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("_weibo_resp_errcode", 2);
        bundle.putString("_weibo_resp_errstr", str);
        intent.putExtras(bundle);
        setResult(-1, intent);
        finish();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void m() {
        FrameLayout frameLayout = this.u;
        if (frameLayout != null) {
            frameLayout.setVisibility(8);
        }
        Handler handler = this.x;
        if (handler != null) {
            handler.removeMessages(0);
            this.x = null;
        }
        try {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("_weibo_resp_errcode", 1);
            intent.putExtras(bundle);
            setResult(-1, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}
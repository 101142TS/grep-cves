package com.tencent.connect.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.umeng.socialize.utils.SLog;
import org.json.JSONObject;
public class AssistActivity extends Activity {
    public static final String EXTRA_INTENT = "openSDK_LOG.AssistActivity.ExtraIntent";
    protected static final int FINISH_BY_TIMEOUT = 0;
    private static final String RESTART_FLAG = "RESTART_FLAG";
    private static final String RESUME_FLAG = "RESUME_FLAG";
    private static final String TAG = "openSDK_LOG.AssistActivity";
    protected Handler handler = new Handler() { // from class: com.tencent.connect.common.AssistActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                boolean isFinishing = AssistActivity.this.isFinishing();
                if (!isFinishing) {
                    AssistActivity.this.finish();
                }
            }
        }
    };
    private boolean isRestart = false;
    private String mAppId;
    protected boolean mOnResumeIsInited = false;

    public static Intent getAssistActivityIntent(Context context) {
        return new Intent(context, AssistActivity.class);
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        int i;
        String str;
        requestWindowFeature(1);
        super.onCreate(bundle);
        setRequestedOrientation(3);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        Intent intent2 = (Intent) getIntent().getParcelableExtra(EXTRA_INTENT);
        if (intent2 == null) {
            i = 0;
        } else {
            i = intent2.getIntExtra("key_request_code", 0);
        }
        if (intent2 == null) {
            str = "";
        } else {
            str = intent2.getStringExtra("appid");
        }
        this.mAppId = str;
        Bundle bundleExtra = getIntent().getBundleExtra("h5_share_data");
        if (bundle != null) {
            this.isRestart = bundle.getBoolean(RESTART_FLAG);
            this.mOnResumeIsInited = bundle.getBoolean(RESUME_FLAG, false);
        }
        boolean z = this.isRestart;
        if (!z && bundleExtra == null) {
            if (intent2 != null) {
                startActivityForResult(intent2, i);
            } else {
                finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        boolean booleanExtra = intent.getBooleanExtra("is_login", false);
        if (!booleanExtra) {
            boolean booleanExtra2 = intent.getBooleanExtra("is_qq_mobile_share", false);
            if (!booleanExtra2) {
                boolean z = this.isRestart;
                if (z) {
                    boolean isFinishing = isFinishing();
                    if (!isFinishing) {
                        finish();
                    }
                }
            }
            boolean z2 = this.mOnResumeIsInited;
            if (z2) {
                this.handler.sendMessage(this.handler.obtainMessage(0));
                return;
            }
            this.mOnResumeIsInited = true;
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        this.handler.removeMessages(0);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intent.putExtra("key_action", "action_share");
        setResult(-1, intent);
        boolean isFinishing = isFinishing();
        if (!isFinishing) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean(RESTART_FLAG, true);
        bundle.putBoolean(RESUME_FLAG, this.mOnResumeIsInited);
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 0) {
            if (intent != null) {
                intent.putExtra("key_action", "action_login");
            }
            setResultData(i, intent);
            finish();
        }
    }

    public void setResultData(int i, Intent intent) {
        if (intent == null) {
            setResult(0);
            return;
        }
        try {
            String stringExtra = intent.getStringExtra("key_response");
            boolean isEmpty = TextUtils.isEmpty(stringExtra);
            if (!isEmpty) {
                JSONObject jSONObject = new JSONObject(stringExtra);
                String optString = jSONObject.optString("openid");
                String optString2 = jSONObject.optString("access_token");
                boolean isEmpty2 = TextUtils.isEmpty(optString);
                if (!isEmpty2) {
                    boolean isEmpty3 = TextUtils.isEmpty(optString2);
                    if (!isEmpty3) {
                        setResult(-1, intent);
                        return;
                    }
                }
                setResult(0, intent);
                return;
            }
            setResult(-1, intent);
        } catch (Exception e) {
            SLog.error(e);
        }
    }
}

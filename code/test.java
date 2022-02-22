package com.tmall.wireless.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import com.alibaba.ariver.kernel.api.monitor.PerfId;
import com.android.alibaba.ip.runtime.InstantReloadException;
import com.android.alibaba.ip.runtime.IpChange;
import com.handmark.pulltorefresh.library.internal.b;
import com.handmark.pulltorefresh.library.internal.d;
import com.tmall.wireless.R;
import com.tmall.wireless.common.navigator.TMNav;
import com.tmall.wireless.common.util.g;
import com.tmall.wireless.module.TMActivity;
import com.tmall.wireless.ui.widget.TMImageView;
import com.ut.mini.UTAnalytics;
import tm.jit;
import tm.jiu;

public class TMPullMoreCacheActivity extends TMActivity {
    public static volatile transient /* synthetic */ IpChange $ipChange = null;
    private static final int KEY_REQUEST_CODE = 56782;
    private d loadingControl = null;

    public static /* synthetic */ Object ipc$super(TMPullMoreCacheActivity tMPullMoreCacheActivity, String str, Object... objArr) {
        int hashCode = str.hashCode();
        if (hashCode == -1504501726) {
            super.onDestroy();
            return null;
        } else if (hashCode == -641568046) {
            super.onCreate((Bundle) objArr[0]);
            return null;
        } else if (hashCode == 1257714799) {
            super.onActivityResult(((Number) objArr[0]).intValue(), ((Number) objArr[1]).intValue(), (Intent) objArr[2]);
            return null;
        } else {
            throw new InstantReloadException(String.format("String switch could not find '%s' with hashcode %s in %s", str, Integer.valueOf(str.hashCode()), "com/tmall/wireless/homepage/activity/TMPullMoreCacheActivity"));
        }
    }

    public static /* synthetic */ d access$000(TMPullMoreCacheActivity tMPullMoreCacheActivity) {
        IpChange ipChange = $ipChange;
        if (ipChange != null) {
            boolean z = ipChange instanceof IpChange;
            if (z) {
                return (d) ipChange.ipc$dispatch("access$000.(Lcom/tmall/wireless/homepage/activity/TMPullMoreCacheActivity;)Lcom/handmark/pulltorefresh/library/internal/d;", new Object[]{tMPullMoreCacheActivity});
            }
        }
        return tMPullMoreCacheActivity.loadingControl;
    }

    @Override // com.tmall.wireless.module.TMActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        IpChange ipChange = $ipChange;
        if (ipChange != null) {
            boolean z = ipChange instanceof IpChange;
            if (z) {
                ipChange.ipc$dispatch("onCreate.(Landroid/os/Bundle;)V", new Object[]{this, bundle});
                return;
            }
        }
        super.onCreate(bundle);
        UTAnalytics.getInstance().getDefaultTracker().skipPage(this);
        FrameLayout frameLayout = (FrameLayout) FrameLayout.inflate(this, R.layout.tm_homepage_pullmore_cache, null);
        setContentView(frameLayout);
        this.loadingControl = new b((TMImageView) frameLayout.findViewById(R.id.tm_pullmore_cache_loading));
        jit.b(new jiu("ani") { // from class: com.tmall.wireless.homepage.activity.TMPullMoreCacheActivity.1
            public static volatile transient /* synthetic */ IpChange $ipChange;

            public static /* synthetic */ Object ipc$super(AnonymousClass1 r1, String str, Object... objArr) {
                str.hashCode();
                throw new InstantReloadException(String.format("String switch could not find '%s' with hashcode %s in %s", str, Integer.valueOf(str.hashCode()), "com/tmall/wireless/homepage/activity/TMPullMoreCacheActivity$1"));
            }

            @Override // java.lang.Runnable
            public void run() {
                IpChange ipChange2 = $ipChange;
                if (ipChange2 != null) {
                    boolean z2 = ipChange2 instanceof IpChange;
                    if (z2) {
                        ipChange2.ipc$dispatch("run.()V", new Object[]{this});
                        return;
                    }
                }
                TMPullMoreCacheActivity.access$000(TMPullMoreCacheActivity.this).a();
            }
        }, 210);
        startPrompt(getIntent());
    }

    private void startPrompt(Intent intent) {
        IpChange ipChange = $ipChange;
        if (ipChange != null) {
            boolean z = ipChange instanceof IpChange;
            if (z) {
                ipChange.ipc$dispatch("startPrompt.(Landroid/content/Intent;)V", new Object[]{this, intent});
                return;
            }
        }
        if (intent != null) {
            Bundle extras = getIntent().getExtras();
            String s = intent.getStringArrayExtra("213");
            if (extras != null) {
                final String string = extras.getString("pullMoreAction");
                boolean isEmpty = TextUtils.isEmpty(string);
                if (!isEmpty) {
                    jit.b(new jiu(PerfId.startActivity) { // from class: com.tmall.wireless.homepage.activity.TMPullMoreCacheActivity.2
                        public static volatile transient /* synthetic */ IpChange $ipChange;

                        public static /* synthetic */ Object ipc$super(AnonymousClass2 r1, String str, Object... objArr) {
                            str.hashCode();
                            throw new InstantReloadException(String.format("String switch could not find '%s' with hashcode %s in %s", str, Integer.valueOf(str.hashCode()), "com/tmall/wireless/homepage/activity/TMPullMoreCacheActivity$2"));
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            IpChange ipChange2 = $ipChange;
                            if (ipChange2 != null) {
                                boolean z2 = ipChange2 instanceof IpChange;
                                if (z2) {
                                    ipChange2.ipc$dispatch("run.()V", new Object[]{this});
                                    return;
                                }
                            }
                            TMNav.from(TMPullMoreCacheActivity.this).withFlags(65536).forResult(TMPullMoreCacheActivity.KEY_REQUEST_CODE).toUri(string);
                            boolean j = g.j(TMPullMoreCacheActivity.this);
                            if (j) {
                                TMPullMoreCacheActivity.this.finish();
                                TMPullMoreCacheActivity.this.overridePendingTransition(R.anim.tm_pull_more_prompt_finish_start, R.anim.tm_pull_more_prompt_finish_end);
                            }
                        }
                    }, 50);
                }
            }
        }
    }

    @Override // com.tmall.wireless.module.TMActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        IpChange ipChange = $ipChange;
        if (ipChange != null) {
            boolean z = ipChange instanceof IpChange;
            if (z) {
                ipChange.ipc$dispatch("onDestroy.()V", new Object[]{this});
                return;
            }
        }
        super.onDestroy();
        this.loadingControl.d();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        IpChange ipChange = $ipChange;
        if (ipChange != null) {
            boolean z = ipChange instanceof IpChange;
            if (z) {
                ipChange.ipc$dispatch("onActivityResult.(IILandroid/content/Intent;)V", new Object[]{this, new Integer(i), new Integer(i2), intent});
                return;
            }
        }
        super.onActivityResult(i, i2, intent);
        finish();
        overridePendingTransition(R.anim.tm_pull_more_prompt_finish_start, R.anim.tm_pull_more_prompt_finish_end);
    }
}
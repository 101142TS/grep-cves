package com.pinterest.activity.webhook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.gson.m;
import com.pinterest.activity.create.PinItActivity;
import com.pinterest.activity.create.RepinActivity;
import com.pinterest.activity.task.model.Location;
import com.pinterest.activity.task.model.Navigation;
import com.pinterest.activity.unauth.UnauthActivity;
import com.pinterest.analytics.a.c;
import com.pinterest.analytics.n;
import com.pinterest.api.UnauthAccountApi;
import com.pinterest.api.b.b;
import com.pinterest.api.g;
import com.pinterest.api.i;
import com.pinterest.api.model.Board;
import com.pinterest.api.model.Interest;
import com.pinterest.api.model.df;
import com.pinterest.api.model.dp;
import com.pinterest.api.model.fj;
import com.pinterest.api.remote.AccountApi;
import com.pinterest.api.remote.ae;
import com.pinterest.api.remote.ah;
import com.pinterest.api.remote.h;
import com.pinterest.base.Application;
import com.pinterest.common.c.d;
import com.pinterest.common.d.f.i;
import com.pinterest.common.d.f.k;
import com.pinterest.common.f.a;
import com.pinterest.common.reporting.CrashReporting;
import com.pinterest.experience.h;
import com.pinterest.experiment.c;
import com.pinterest.feature.home.b.k;
import com.pinterest.kit.h.aa;
import com.pinterest.kit.h.f;
import com.pinterest.navigation.view.g;
import com.pinterest.q.p;
import com.pinterest.s.e.ce;
import com.pinterest.s.e.cf;
import com.pinterest.s.e.n;
import com.pinterest.s.e.y;
import io.reactivex.b.b;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.FutureTask;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebhookActivity extends com.pinterest.kit.activity.a {
    private static Set<String> h = new HashSet<String>() { // from class: com.pinterest.activity.webhook.WebhookActivity.4
        {
            add("user");
            add("board");
            add("pin");
            add("conversation");
        }
    };
    private static final Callback i = new Callback() { // from class: com.pinterest.activity.webhook.WebhookActivity.8
        @Override // okhttp3.Callback
        public final void onFailure(Call call, IOException iOException) {
        }

        @Override // okhttp3.Callback
        public final void onResponse(Call call, Response response) {
        }
    };

    /* renamed from: a  reason: collision with root package name */
    public OkHttpClient f14572a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f14573b = false;

    /* renamed from: c  reason: collision with root package name */
    private a f14574c;

    /* renamed from: d  reason: collision with root package name */
    private c f14575d;
    private Uri e;
    private final p f = Application.c().o.l();
    private b g;

    @Override // com.pinterest.framework.a.a
    public cf getViewType() {
        return cf.DEEP_LINKING;
    }

    @Override // com.pinterest.kit.activity.j, com.pinterest.framework.a.a
    public ce getViewParameterType() {
        return ce.DEEP_LINKING_APP;
    }

    /* access modifiers changed from: protected */
    @Override // com.pinterest.kit.activity.a, com.pinterest.kit.activity.j, com.pinterest.kit.activity.i, android.support.v7.app.c, android.support.v4.app.FragmentActivity, android.support.v4.app.ac, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivityComponent().a(this);
        Intent intent = getIntent();
        if (intent != null) {
            this.f14574c = new a(this, (byte) 0);
            this.f14575d = new c();
            a(getIntent().getData());
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.pinterest.kit.activity.a, com.pinterest.kit.activity.j, com.pinterest.kit.activity.i, android.support.v7.app.c, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        b bVar = this.g;
        if (bVar != null) {
            boolean bC_ = this.g.bC_();
            if (!bC_) {
                this.g.dW_();
            }
        }
        a aVar = this.f14574c;
        if (aVar != null) {
            a aVar2 = this.f14574c;
            b bVar2 = aVar2.f25636c;
            if (bVar2 != null) {
                aVar2.f25636c.dW_();
                aVar2.f25636c = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0041, code lost:
        if (r0 != false) goto L_0x0043;
     */
    public final void a(Uri uri) {
        y yVar;
        new Object[1][0] = uri;
        if (uri == null) {
            b();
            return;
        }
        new Thread(new FutureTask(
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x002c: INVOKE  
              (wrap: java.lang.Thread : 0x0029: CONSTRUCTOR  (r0v3 java.lang.Thread) = 
              (wrap: java.util.concurrent.FutureTask : 0x0024: CONSTRUCTOR  (r3v0 java.util.concurrent.FutureTask) = 
              (wrap: com.pinterest.analytics.a.c$1 : 0x0021: CONSTRUCTOR  (r4v0 com.pinterest.analytics.a.c$1) = 
              (wrap: com.pinterest.analytics.a.c : 0x000e: CONSTRUCTOR  (r0v2 com.pinterest.analytics.a.c) =  call: com.pinterest.analytics.a.c.<init>():void type: CONSTRUCTOR)
              (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS])
              (wrap: java.lang.String : 0x0011: INVOKE  (r1v1 java.lang.String) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: DIRECT call: com.pinterest.activity.webhook.WebhookActivity.c():java.lang.String)
              (wrap: android.net.Uri : 0x0019: INVOKE  (r2v1 android.net.Uri) = 
              (wrap: android.content.Intent : 0x0015: INVOKE  (r2v0 android.content.Intent) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: VIRTUAL call: android.app.Activity.getIntent():android.content.Intent)
             type: VIRTUAL call: android.content.Intent.getData():android.net.Uri)
             call: com.pinterest.analytics.a.c.1.<init>(com.pinterest.analytics.a.c, android.app.Activity, java.lang.String, android.net.Uri):void type: CONSTRUCTOR)
             call: java.util.concurrent.FutureTask.<init>(java.util.concurrent.Callable):void type: CONSTRUCTOR)
             call: java.lang.Thread.<init>(java.lang.Runnable):void type: CONSTRUCTOR)
             type: VIRTUAL call: java.lang.Thread.start():void in method: com.pinterest.activity.webhook.WebhookActivity.a(android.net.Uri):void, file: classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:104)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:64)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:64)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:64)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:64)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:345)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:298)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:267)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0029: CONSTRUCTOR  (r0v3 java.lang.Thread) = 
              (wrap: java.util.concurrent.FutureTask : 0x0024: CONSTRUCTOR  (r3v0 java.util.concurrent.FutureTask) = 
              (wrap: com.pinterest.analytics.a.c$1 : 0x0021: CONSTRUCTOR  (r4v0 com.pinterest.analytics.a.c$1) = 
              (wrap: com.pinterest.analytics.a.c : 0x000e: CONSTRUCTOR  (r0v2 com.pinterest.analytics.a.c) =  call: com.pinterest.analytics.a.c.<init>():void type: CONSTRUCTOR)
              (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS])
              (wrap: java.lang.String : 0x0011: INVOKE  (r1v1 java.lang.String) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: DIRECT call: com.pinterest.activity.webhook.WebhookActivity.c():java.lang.String)
              (wrap: android.net.Uri : 0x0019: INVOKE  (r2v1 android.net.Uri) = 
              (wrap: android.content.Intent : 0x0015: INVOKE  (r2v0 android.content.Intent) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: VIRTUAL call: android.app.Activity.getIntent():android.content.Intent)
             type: VIRTUAL call: android.content.Intent.getData():android.net.Uri)
             call: com.pinterest.analytics.a.c.1.<init>(com.pinterest.analytics.a.c, android.app.Activity, java.lang.String, android.net.Uri):void type: CONSTRUCTOR)
             call: java.util.concurrent.FutureTask.<init>(java.util.concurrent.Callable):void type: CONSTRUCTOR)
             call: java.lang.Thread.<init>(java.lang.Runnable):void type: CONSTRUCTOR in method: com.pinterest.activity.webhook.WebhookActivity.a(android.net.Uri):void, file: classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:138)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:116)
            	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:749)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:394)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:275)
            	... 19 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0024: CONSTRUCTOR  (r3v0 java.util.concurrent.FutureTask) = 
              (wrap: com.pinterest.analytics.a.c$1 : 0x0021: CONSTRUCTOR  (r4v0 com.pinterest.analytics.a.c$1) = 
              (wrap: com.pinterest.analytics.a.c : 0x000e: CONSTRUCTOR  (r0v2 com.pinterest.analytics.a.c) =  call: com.pinterest.analytics.a.c.<init>():void type: CONSTRUCTOR)
              (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS])
              (wrap: java.lang.String : 0x0011: INVOKE  (r1v1 java.lang.String) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: DIRECT call: com.pinterest.activity.webhook.WebhookActivity.c():java.lang.String)
              (wrap: android.net.Uri : 0x0019: INVOKE  (r2v1 android.net.Uri) = 
              (wrap: android.content.Intent : 0x0015: INVOKE  (r2v0 android.content.Intent) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: VIRTUAL call: android.app.Activity.getIntent():android.content.Intent)
             type: VIRTUAL call: android.content.Intent.getData():android.net.Uri)
             call: com.pinterest.analytics.a.c.1.<init>(com.pinterest.analytics.a.c, android.app.Activity, java.lang.String, android.net.Uri):void type: CONSTRUCTOR)
             call: java.util.concurrent.FutureTask.<init>(java.util.concurrent.Callable):void type: CONSTRUCTOR in method: com.pinterest.activity.webhook.WebhookActivity.a(android.net.Uri):void, file: classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:138)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:116)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:955)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:690)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:390)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:258)
            	... 25 more
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0021: CONSTRUCTOR  (r4v0 com.pinterest.analytics.a.c$1) = 
              (wrap: com.pinterest.analytics.a.c : 0x000e: CONSTRUCTOR  (r0v2 com.pinterest.analytics.a.c) =  call: com.pinterest.analytics.a.c.<init>():void type: CONSTRUCTOR)
              (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS])
              (wrap: java.lang.String : 0x0011: INVOKE  (r1v1 java.lang.String) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: DIRECT call: com.pinterest.activity.webhook.WebhookActivity.c():java.lang.String)
              (wrap: android.net.Uri : 0x0019: INVOKE  (r2v1 android.net.Uri) = 
              (wrap: android.content.Intent : 0x0015: INVOKE  (r2v0 android.content.Intent) = (r5v0 'this' com.pinterest.activity.webhook.WebhookActivity A[IMMUTABLE_TYPE, THIS]) type: VIRTUAL call: android.app.Activity.getIntent():android.content.Intent)
             type: VIRTUAL call: android.content.Intent.getData():android.net.Uri)
             call: com.pinterest.analytics.a.c.1.<init>(com.pinterest.analytics.a.c, android.app.Activity, java.lang.String, android.net.Uri):void type: CONSTRUCTOR in method: com.pinterest.activity.webhook.WebhookActivity.a(android.net.Uri):void, file: classes2.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:138)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:116)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:955)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:690)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:390)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:258)
            	... 31 more
            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.pinterest.analytics.a.c, state: GENERATED_AND_UNLOADED
            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:212)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:657)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:390)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:258)
            	... 37 more
            */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0041, code lost:
            if (r0 != false) goto L_0x0043;
         */
        /*
            this = this;
            r0 = 1
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r1 = 0
            r0[r1] = r6
            if (r6 != 0) goto L_0x000c
            r5.b()
        L_0x000b:
            return
        L_0x000c:
            com.pinterest.analytics.a.c r0 = new com.pinterest.analytics.a.c
            r0.<init>()
            java.lang.String r1 = r5.c()
            android.content.Intent r2 = r5.getIntent()
            android.net.Uri r2 = r2.getData()
            java.util.concurrent.FutureTask r3 = new java.util.concurrent.FutureTask
            com.pinterest.analytics.a.c$1 r4 = new com.pinterest.analytics.a.c$1
            r4.<init>(r5, r1, r2)
            r3.<init>(r4)
            java.lang.Thread r0 = new java.lang.Thread
            r0.<init>(r3)
            r0.start()
            java.lang.String r1 = r5.c()
            java.lang.String r0 = "PULL_NOTIF"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x0043
            java.lang.String r0 = "PUSH_NOTIF"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x006d
        L_0x0043:
            com.pinterest.analytics.h r2 = r5._pinalytics
            java.lang.String r0 = "PULL_NOTIF"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x007f
            com.pinterest.s.e.y r0 = com.pinterest.s.e.y.PULL_NOTIFICATION_OPENED_BY
        L_0x004f:
            com.pinterest.common.f.a r3 = com.pinterest.common.f.a.C0251a.f16404a
            java.lang.String r3 = r3.b()
            r2.a(r0, r3)
            java.lang.String r0 = "PUSH_NOTIF"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x006d
            java.lang.String r0 = "opened"
            android.content.Intent r1 = r5.getIntent()
            android.os.Bundle r1 = r1.getExtras()
            com.pinterest.api.remote.b.a(r0, r1)
        L_0x006d:
            r5.showWaitDialog()
            java.lang.String r0 = "start"
            com.pinterest.analytics.a.c.a(r0)
            boolean r0 = com.pinterest.kit.h.f.b(r6)
            if (r0 != 0) goto L_0x0082
            r5.finish()
            goto L_0x000b
        L_0x007f:
            com.pinterest.s.e.y r0 = com.pinterest.s.e.y.PUSH_NOTIFICATION_OPENED_BY
            goto L_0x004f
        L_0x0082:
            r5.b(r6)
            r5.handleInstagramAuthIfNecessary(r6)
            r5.handleEtsyAuthIfNecessary(r6)
            r5.e = r6
            r0 = 8
            r5.ensureResources(r0)
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pinterest.activity.webhook.WebhookActivity.a(android.net.Uri):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:145:0x035d, code lost:
        if (r1 != false) goto L_0x035f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:277:0x064a, code lost:
        if (r0 == false) goto L_0x064c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0031, code lost:
        if (r0 != false) goto L_0x0033;
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x0274  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0300  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x032f  */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x0428  */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x0432  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0450  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x046e  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0052 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x04bf  */
    /* JADX WARNING: Removed duplicated region for block: B:209:0x04ea  */
    /* JADX WARNING: Removed duplicated region for block: B:219:0x050b  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:230:0x0544  */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x0548  */
    /* JADX WARNING: Removed duplicated region for block: B:242:0x0579  */
    /* JADX WARNING: Removed duplicated region for block: B:244:0x0599  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x05d1  */
    /* JADX WARNING: Removed duplicated region for block: B:259:0x05d9  */
    /* JADX WARNING: Removed duplicated region for block: B:283:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:284:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:295:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:296:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:299:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0131  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0192  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01ca  */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x01e1  */
    @Override // com.pinterest.kit.activity.a, com.pinterest.l.b.c
    public void onResourcesReady(int i2) {
        boolean z;
        boolean equals;
        boolean z2;
        boolean contains;
        boolean contains2;
        UnauthAccountApi.UnauthLoginParams unauthLoginParams;
        String queryParameter;
        String str;
        String str2;
        String str3;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        Uri uri;
        String str4;
        boolean contains3;
        boolean z7;
        int size;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14;
        Uri uri2;
        int size2;
        boolean z15 = true;
        boolean z16 = false;
        final Uri uri3 = this.e;
        fj b2 = df.b();
        if (b2 != null) {
            boolean d2 = com.pinterest.api.c.d();
            if (d2) {
                z = true;
                ArrayList arrayList = new ArrayList(uri3.getPathSegments());
                equals = "www.pinterest.com".equals(uri3.getHost());
                if (!equals) {
                    boolean equals2 = "pinterest.com".equals(uri3.getHost());
                }
                size2 = arrayList.size();
                if (size2 == 1) {
                    boolean contains4 = arrayList.contains("login");
                    if (contains4) {
                        String queryParameter2 = uri3.getQueryParameter("next");
                        if (queryParameter2 != null) {
                            z2 = true;
                            contains = arrayList.contains("secure");
                            if (!contains || z2) {
                                contains2 = arrayList.contains("login");
                                if (!contains2) {
                                    boolean contains5 = arrayList.contains("autologin");
                                    if (!contains5) {
                                        com.pinterest.activity.a.a((Context) this, uri3);
                                        finish();
                                        c.a("secure");
                                    }
                                }
                                if (!z) {
                                    boolean contains6 = arrayList.contains("autologin");
                                    if (contains6) {
                                        b();
                                    } else {
                                        try {
                                            AnonymousClass9 r6 = new i() { // from class: com.pinterest.activity.webhook.WebhookActivity.9
                                                @Override // com.pinterest.api.i
                                                public final void a(g gVar) {
                                                    super.a(gVar);
                                                    try {
                                                        d dVar = (d) gVar.e();
                                                        if (dVar == null) {
                                                            a(new Throwable(""), new d());
                                                            return;
                                                        }
                                                        com.pinterest.api.c.a(dVar.a("access_token", ""));
                                                        ah.a((ah.b) new ah.b() { // from class: com.pinterest.activity.webhook.WebhookActivity.9.1
                                                            @Override // com.pinterest.api.remote.ah.b
                                                            public final void a(fj fjVar) {
                                                                super.a(fjVar);
                                                                String queryParameter3 = uri3.getQueryParameter("next");
                                                                boolean a2 = org.apache.commons.b.b.a((CharSequence) queryParameter3);
                                                                if (!a2) {
                                                                    boolean equals3 = uri3.getPath().equals("/secure/login/");
                                                                    if (equals3) {
                                                                        boolean startsWith = queryParameter3.startsWith("/?");
                                                                        if (startsWith) {
                                                                            WebhookActivity.this.b();
                                                                            return;
                                                                        }
                                                                    }
                                                                    Uri parse = Uri.parse(queryParameter3);
                                                                    if (parse != null) {
                                                                        WebhookActivity.this.a(parse);
                                                                        return;
                                                                    }
                                                                }
                                                                WebhookActivity.this.b();
                                                            }

                                                            @Override // com.pinterest.api.h, com.pinterest.api.i
                                                            public final void a(Throwable th, g gVar2) {
                                                                super.a(th, gVar2);
                                                                WebhookActivity.this.b();
                                                            }
                                                        }, WebhookActivity.this._apiTag);
                                                    } catch (Exception e) {
                                                        a(new Throwable(""), new d());
                                                    }
                                                }

                                                @Override // com.pinterest.api.i
                                                public final void a(Throwable th, g gVar) {
                                                    super.a(th, gVar);
                                                    CrashReporting.a().b(th);
                                                    p unused = WebhookActivity.this.f;
                                                    boolean b3 = p.b();
                                                    if (b3) {
                                                        WebhookActivity.this.b();
                                                    } else {
                                                        WebhookActivity.this.d();
                                                    }
                                                }
                                            };
                                            synchronized ("ApiTagPersist") {
                                                if (uri3 == null) {
                                                    r6.a(new InvalidParameterException("URI is empty"), "");
                                                } else {
                                                    String queryParameter3 = uri3.getQueryParameter("next");
                                                    if (queryParameter3 != null) {
                                                        Uri parse = Uri.parse(queryParameter3);
                                                        boolean equals3 = uri3.getPath().equals("/secure/login/");
                                                        if (equals3) {
                                                            String queryParameter4 = parse.getQueryParameter("token");
                                                            String queryParameter5 = parse.getQueryParameter("expiration");
                                                            String queryParameter6 = parse.getQueryParameter("user_id");
                                                            String queryParameter7 = parse.getQueryParameter("stored");
                                                            queryParameter = queryParameter6;
                                                            str = queryParameter4;
                                                            str2 = queryParameter5;
                                                            str3 = queryParameter7;
                                                        } else {
                                                            String queryParameter8 = uri3.getQueryParameter("token");
                                                            String queryParameter9 = uri3.getQueryParameter("expiration");
                                                            queryParameter = uri3.getQueryParameter("user_id");
                                                            str = queryParameter8;
                                                            str2 = queryParameter9;
                                                            str3 = "";
                                                        }
                                                        boolean a2 = org.apache.commons.b.b.a((CharSequence) str);
                                                        if (!a2) {
                                                            boolean a3 = org.apache.commons.b.b.a((CharSequence) str2);
                                                            if (!a3) {
                                                                boolean a4 = org.apache.commons.b.b.a((CharSequence) queryParameter);
                                                                if (!a4) {
                                                                    unauthLoginParams = new UnauthAccountApi.UnauthLoginParams();
                                                                    unauthLoginParams.f = str;
                                                                    unauthLoginParams.f15051d = str2;
                                                                    unauthLoginParams.e = queryParameter;
                                                                    unauthLoginParams.y = str3;
                                                                    if (unauthLoginParams != null) {
                                                                        r6.a(new InvalidParameterException(uri3.toString()), "");
                                                                    } else {
                                                                        UnauthAccountApi.a(unauthLoginParams, new UnauthAccountApi.c(r6, (byte) 0));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    unauthLoginParams = null;
                                                    if (unauthLoginParams != null) {
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            CrashReporting.a().a(e, "DeeplinkLogin");
                                            b();
                                        }
                                    }
                                } else {
                                    String queryParameter10 = uri3.getQueryParameter("next");
                                    boolean a5 = org.apache.commons.b.b.a((CharSequence) queryParameter10);
                                    if (!a5) {
                                        boolean contains7 = queryParameter10.contains("//pinterest.com");
                                        if (!contains7) {
                                            boolean contains8 = queryParameter10.contains("//www.pinterest.com");
                                            if (!contains8) {
                                                queryParameter10 = "https://pinterest.com" + queryParameter10;
                                            }
                                        }
                                        Uri parse2 = Uri.parse(queryParameter10);
                                        if (parse2 != null) {
                                            a(parse2);
                                            return;
                                        }
                                    }
                                    b();
                                }
                                c.a("secure");
                            }
                            boolean equals4 = TextUtils.equals(uri3.getHost(), "post.pinterest.com");
                            if (equals4) {
                                final String queryParameter11 = uri3.getQueryParameter("target");
                                boolean a6 = org.apache.commons.b.b.a((CharSequence) queryParameter11);
                                if (a6) {
                                    b();
                                } else {
                                    runOnUiThread(new Runnable() { // from class: com.pinterest.activity.webhook.WebhookActivity.7
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            WebhookActivity.this.a(Uri.parse(queryParameter11));
                                        }
                                    });
                                    this.f14572a.newCall(new Request.Builder().url(uri3.toString()).build()).enqueue(i);
                                    z3 = true;
                                    if (z3) {
                                        boolean equals5 = TextUtils.equals(uri3.getHost(), "www.pinterest.com");
                                        if (equals5) {
                                            String encodedPath = uri3.getEncodedPath();
                                            boolean f = f.f(encodedPath);
                                            if (f) {
                                                boolean equals6 = "/parental-consent/confirmation/".equals(encodedPath);
                                                if (equals6) {
                                                    uri2 = uri3.buildUpon().appendQueryParameter("device_id", a.C0251a.f16404a.b()).build();
                                                } else {
                                                    uri2 = uri3;
                                                }
                                                com.pinterest.activity.a.a((Activity) this, uri2);
                                                z4 = true;
                                                if (z4) {
                                                    int size3 = arrayList.size();
                                                    if (size3 >= 2) {
                                                        boolean a7 = org.apache.commons.b.b.a((CharSequence) arrayList.get(0), "pw");
                                                        if (a7) {
                                                            TreeMap treeMap = new TreeMap();
                                                            treeMap.put("username", arrayList.get(1));
                                                            treeMap.put("expiration", uri3.getQueryParameter("e"));
                                                            treeMap.put("token", uri3.getQueryParameter("t"));
                                                            Intent intent = new Intent(Application.g(), UnauthActivity.class);
                                                            intent.putExtra("com.pinterest.EXTRA_CREATE_PASSWORD", true);
                                                            intent.putExtra("com.pinterest.EXTRA_USERNAME", (String) treeMap.get("username"));
                                                            intent.putExtra("com.pinterest.EXTRA_PASSWORD_EXPIRATION", (String) treeMap.get("expiration"));
                                                            intent.putExtra("com.pinterest.EXTRA_PASSWORD_TOKEN", (String) treeMap.get("token"));
                                                            startActivity(intent);
                                                            finish();
                                                            z5 = true;
                                                            if (!z5) {
                                                                c.a("pw");
                                                                return;
                                                            }
                                                            boolean a8 = a(arrayList, uri3, z);
                                                            if (!a8) {
                                                                int size4 = arrayList.size();
                                                                if (size4 != 1) {
                                                                    z6 = false;
                                                                } else {
                                                                    Location a9 = com.pinterest.activity.task.model.b.a((String) arrayList.get(0));
                                                                    if (a9 == null) {
                                                                        z6 = false;
                                                                    } else {
                                                                        Navigation navigation = new Navigation(a9);
                                                                        String queryParameter12 = uri3.getQueryParameter("tab");
                                                                        boolean c2 = org.apache.commons.b.b.c((CharSequence) queryParameter12);
                                                                        if (!c2) {
                                                                            navigation.a("TAB", queryParameter12.trim());
                                                                        }
                                                                        a(navigation);
                                                                        z6 = true;
                                                                    }
                                                                }
                                                                if (z6) {
                                                                    c.a("sitemap");
                                                                    return;
                                                                }
                                                                String host = uri3.getHost();
                                                                aa.c();
                                                                boolean e2 = aa.e(host);
                                                                if (e2) {
                                                                    com.pinterest.activity.a.a((Context) this, uri3);
                                                                    finish();
                                                                    return;
                                                                }
                                                                boolean equals7 = "pinterest".equals(uri3.getScheme());
                                                                if (!equals7) {
                                                                    if (host != null) {
                                                                        boolean a10 = org.apache.commons.b.b.a((CharSequence) host, (CharSequence) "http");
                                                                        if (a10) {
                                                                            Uri parse3 = Uri.parse(uri3.toString().replace("http://", ""));
                                                                            uri = parse3;
                                                                            str4 = parse3.getHost();
                                                                            contains3 = str4.contains("facebook");
                                                                            if (!contains3) {
                                                                                c.a("facebook");
                                                                                if (!z) {
                                                                                    d();
                                                                                    finish();
                                                                                    return;
                                                                                }
                                                                                b();
                                                                                return;
                                                                            }
                                                                            String queryParameter13 = uri.getQueryParameter("url");
                                                                            boolean a11 = k.a((CharSequence) queryParameter13);
                                                                            if (a11) {
                                                                                c.a("redirect");
                                                                                if (!z) {
                                                                                    d();
                                                                                    return;
                                                                                }
                                                                                boolean contains9 = str4.contains("pinit");
                                                                                if (!contains9) {
                                                                                    boolean contains10 = arrayList.contains("pinit");
                                                                                    if (!contains10) {
                                                                                        boolean contains11 = arrayList.contains("pin");
                                                                                    }
                                                                                }
                                                                                Intent intent2 = new Intent(this, PinItActivity.class);
                                                                                intent2.putExtra("android.intent.extra.TEXT", queryParameter13);
                                                                                String queryParameter14 = uri.getQueryParameter("description");
                                                                                boolean a12 = k.a((CharSequence) queryParameter14);
                                                                                if (a12) {
                                                                                    intent2.putExtra("com.pinterest.EXTRA_DESCRIPTION", queryParameter14);
                                                                                }
                                                                                String queryParameter15 = uri.getQueryParameter("media");
                                                                                boolean a13 = k.a((CharSequence) queryParameter15);
                                                                                if (a13) {
                                                                                    intent2.putExtra("com.pinterest.EXTRA_IMAGE", queryParameter15);
                                                                                }
                                                                                String queryParameter16 = uri.getQueryParameter("method");
                                                                                boolean a14 = k.a((CharSequence) queryParameter16);
                                                                                if (a14) {
                                                                                    intent2.putExtra("com.pinterest.EXTRA_PIN_CREATE_TYPE", queryParameter16);
                                                                                }
                                                                                intent2.putExtra("com.pinterest.EXTRA_META", c(uri));
                                                                                startActivity(intent2);
                                                                                finish();
                                                                                return;
                                                                            }
                                                                            h.d.f17136a.b();
                                                                            boolean isEmpty = arrayList.isEmpty();
                                                                            if (isEmpty) {
                                                                                String queryParameter17 = uri.getQueryParameter("utm_campaign");
                                                                                String queryParameter18 = uri.getQueryParameter("e_t_s");
                                                                                boolean a15 = org.apache.commons.b.b.a((CharSequence) queryParameter17, (CharSequence) "breakup");
                                                                                if (a15 && queryParameter18 != null) {
                                                                                    HashMap hashMap = new HashMap();
                                                                                    hashMap.put(h.a.CONTEXT_EMAIL_UTM_CAMPAIGN.m, queryParameter17);
                                                                                    hashMap.put(h.a.CONTEXT_EMAIL_E_T_S.m, queryParameter18);
                                                                                    h.d.f17136a.a(com.pinterest.s.f.h.ANDROID_GLOBAL_NAG, hashMap);
                                                                                }
                                                                            }
                                                                            boolean contains12 = uri.getQueryParameterNames().contains("invite_code");
                                                                            if (!contains12) {
                                                                                boolean contains13 = uri.getQueryParameterNames().contains("utm_source");
                                                                                if (!contains13) {
                                                                                    this._pinalytics.a(y.LANDING_PAGE_ENTRY, uri.toString());
                                                                                }
                                                                            }
                                                                            com.pinterest.experiment.c cVar = c.a.f17157a;
                                                                            boolean b3 = cVar.f17156a.b("android_prefetch_img_following_deeplink", "enabled", 1);
                                                                            if (!b3) {
                                                                                boolean b4 = cVar.f17156a.b("android_prefetch_img_following_deeplink");
                                                                                if (!b4) {
                                                                                    z7 = false;
                                                                                    if (z7) {
                                                                                        k.a.f21988a.f21987b = true;
                                                                                    }
                                                                                    size = arrayList.size();
                                                                                    if (size == 1) {
                                                                                        boolean equals8 = "notifications".equals(arrayList.get(0));
                                                                                        if (equals8) {
                                                                                            Navigation navigation2 = new Navigation(Location.NOTIFICATIONS);
                                                                                            com.pinterest.analytics.a.c.a("notifications");
                                                                                            a(navigation2);
                                                                                            z8 = true;
                                                                                            if (!z8) {
                                                                                                int size5 = arrayList.size();
                                                                                                if (size5 >= 2) {
                                                                                                    boolean equals9 = "news_hub".equals(arrayList.get(0));
                                                                                                    if (equals9) {
                                                                                                        boolean d3 = com.pinterest.api.c.d();
                                                                                                        if (!d3) {
                                                                                                            d();
                                                                                                            z9 = true;
                                                                                                        } else {
                                                                                                            String str5 = (String) arrayList.get(1);
                                                                                                            if (str5 != null) {
                                                                                                                a(new Navigation(Location.NEWS_HUB, str5));
                                                                                                                z9 = true;
                                                                                                            }
                                                                                                        }
                                                                                                        if (z9) {
                                                                                                            int size6 = arrayList.size();
                                                                                                            if (size6 >= 2) {
                                                                                                                boolean equals10 = "conversation".equals(arrayList.get(0));
                                                                                                                if (equals10) {
                                                                                                                    String str6 = (String) arrayList.get(1);
                                                                                                                    boolean a16 = com.pinterest.common.d.f.k.a((CharSequence) str6);
                                                                                                                    if (a16) {
                                                                                                                        String queryParameter19 = uri.getQueryParameter("fsf");
                                                                                                                        String queryParameter20 = uri.getQueryParameter("pin_id");
                                                                                                                        Navigation navigation3 = new Navigation(Location.CONVERSATION, str6);
                                                                                                                        if (!(queryParameter19 == null || queryParameter20 == null)) {
                                                                                                                            navigation3.a("com.pinterest.EXTRA_FEEDBACK_PIN_ID", queryParameter20);
                                                                                                                            navigation3.a("com.pinterest.EXTRA_FEEDBACK_TYPE", Integer.parseInt(queryParameter19));
                                                                                                                        }
                                                                                                                        com.pinterest.analytics.a.c.a("conversation");
                                                                                                                        a(navigation3);
                                                                                                                    }
                                                                                                                    finish();
                                                                                                                    z10 = true;
                                                                                                                    if (!z10) {
                                                                                                                        this._pinalytics.a(y.CONVERSATION_APP_LAND, uri.toString());
                                                                                                                        return;
                                                                                                                    }
                                                                                                                    int size7 = arrayList.size();
                                                                                                                    if (size7 < 2) {
                                                                                                                        z12 = false;
                                                                                                                    } else {
                                                                                                                        boolean equals11 = "explore".equals(arrayList.get(0));
                                                                                                                        if (!equals11) {
                                                                                                                            boolean equals12 = "categories".equals(arrayList.get(0));
                                                                                                                            if (!equals12) {
                                                                                                                                boolean equals13 = "topics".equals(arrayList.get(0));
                                                                                                                                if (!equals13) {
                                                                                                                                    z11 = false;
                                                                                                                                    if (z11) {
                                                                                                                                        z12 = false;
                                                                                                                                    } else {
                                                                                                                                        String str7 = (String) arrayList.get(1);
                                                                                                                                        boolean equals14 = "explore".equals(arrayList.get(0));
                                                                                                                                        if (!equals14) {
                                                                                                                                            boolean equals15 = "topics".equals(arrayList.get(0));
                                                                                                                                            if (!equals15) {
                                                                                                                                                boolean equals16 = "categories".equals(arrayList.get(0));
                                                                                                                                                if (equals16) {
                                                                                                                                                    fj b5 = df.b();
                                                                                                                                                    if (b5 != null) {
                                                                                                                                                        boolean d4 = com.pinterest.api.c.d();
                                                                                                                                                        if (d4) {
                                                                                                                                                            z14 = true;
                                                                                                                                                            if (z14) {
                                                                                                                                                                d();
                                                                                                                                                                z12 = true;
                                                                                                                                                            } else {
                                                                                                                                                                boolean equals17 = "home".equals(str7);
                                                                                                                                                                if (equals17) {
                                                                                                                                                                    b();
                                                                                                                                                                } else {
                                                                                                                                                                    a(new Navigation(Location.CATEGORY, str7));
                                                                                                                                                                }
                                                                                                                                                                z12 = true;
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    z14 = false;
                                                                                                                                                    if (z14) {
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                                com.pinterest.analytics.a.c.a("categories");
                                                                                                                                                z12 = true;
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        String a17 = a(str7);
                                                                                                                                        fj b6 = df.b();
                                                                                                                                        if (b6 != null) {
                                                                                                                                            boolean d5 = com.pinterest.api.c.d();
                                                                                                                                            if (d5) {
                                                                                                                                                z13 = true;
                                                                                                                                                if (z13) {
                                                                                                                                                    com.pinterest.api.remote.b.a("unauth_klp_deeplink");
                                                                                                                                                    Intent intent3 = new Intent(Application.g(), UnauthActivity.class);
                                                                                                                                                    intent3.putExtra("com.pinterest.EXTRA_KLP_ID", a17);
                                                                                                                                                    startActivity(intent3);
                                                                                                                                                    finish();
                                                                                                                                                    z12 = true;
                                                                                                                                                } else {
                                                                                                                                                    boolean equals18 = "explore".equals(arrayList.get(0));
                                                                                                                                                    if (equals18) {
                                                                                                                                                        a(a17, true);
                                                                                                                                                        z12 = true;
                                                                                                                                                    } else {
                                                                                                                                                        a(a17, false);
                                                                                                                                                        com.pinterest.analytics.a.c.a("categories");
                                                                                                                                                        z12 = true;
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        z13 = false;
                                                                                                                                        if (z13) {
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                        z11 = true;
                                                                                                                        if (z11) {
                                                                                                                        }
                                                                                                                    }
                                                                                                                    if (!z12) {
                                                                                                                        int size8 = arrayList.size();
                                                                                                                        if (size8 >= 3) {
                                                                                                                            boolean equals19 = ((String) arrayList.get(2)).equals("invite");
                                                                                                                            if (equals19) {
                                                                                                                                boolean d6 = com.pinterest.api.c.d();
                                                                                                                                if (!d6) {
                                                                                                                                    d();
                                                                                                                                } else {
                                                                                                                                    String format = String.format("%s/%s", arrayList.get(0), arrayList.get(1));
                                                                                                                                    fj b7 = df.b();
                                                                                                                                    if (b7 != null) {
                                                                                                                                        boolean d7 = com.pinterest.api.c.d();
                                                                                                                                        if (d7) {
                                                                                                                                            z16 = true;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    if (!z16) {
                                                                                                                                        this.f14574c.b(format);
                                                                                                                                        WebhookActivity.this.finish();
                                                                                                                                    } else {
                                                                                                                                        com.pinterest.api.remote.h.a(format, com.pinterest.api.c.d(), (h.a) new h.a() { // from class: com.pinterest.activity.webhook.WebhookActivity.5
                                                                                                                                            @Override // com.pinterest.api.remote.h.a
                                                                                                                                            public final void a(Board board) {
                                                                                                                                                super.a(board);
                                                                                                                                                if (board != null) {
                                                                                                                                                    WebhookActivity.this.f14574c.a(board, false, true);
                                                                                                                                                }
                                                                                                                                                WebhookActivity.this.finish();
                                                                                                                                            }

                                                                                                                                            @Override // com.pinterest.api.h, com.pinterest.api.i
                                                                                                                                            public final void a(Throwable th, g gVar) {
                                                                                                                                                boolean c3 = i.a.f16390a.c();
                                                                                                                                                if (!c3) {
                                                                                                                                                    super.a(th, gVar);
                                                                                                                                                    WebhookActivity.this.finish();
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }, WebhookActivity.this._apiTag);
                                                                                                                                        fj b8 = df.b();
                                                                                                                                        if (b8 != null) {
                                                                                                                                            boolean d8 = com.pinterest.api.c.d();
                                                                                                                                        }
                                                                                                                                        ah.a(new ah.a(), WebhookActivity.this._apiTag);
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                if (z15) {
                                                                                                                                    boolean a18 = this.f14574c.a(uri, str4);
                                                                                                                                    if (!a18) {
                                                                                                                                        b();
                                                                                                                                        com.pinterest.analytics.a.c.a("others");
                                                                                                                                        return;
                                                                                                                                    }
                                                                                                                                    return;
                                                                                                                                }
                                                                                                                                return;
                                                                                                                            }
                                                                                                                        }
                                                                                                                        z15 = false;
                                                                                                                        if (z15) {
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        return;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            z10 = false;
                                                                                                            if (!z10) {
                                                                                                            }
                                                                                                        } else {
                                                                                                            return;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                z9 = false;
                                                                                                if (z9) {
                                                                                                }
                                                                                            } else {
                                                                                                return;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    z8 = false;
                                                                                    if (!z8) {
                                                                                    }
                                                                                }
                                                                            }
                                                                            z7 = true;
                                                                            if (z7) {
                                                                            }
                                                                            size = arrayList.size();
                                                                            if (size == 1) {
                                                                            }
                                                                            z8 = false;
                                                                            if (!z8) {
                                                                            }
                                                                        } else {
                                                                            int length = host.length();
                                                                            if (length > 0) {
                                                                                boolean e3 = org.apache.commons.b.b.e(host, "pinterest");
                                                                                if (!e3) {
                                                                                    b();
                                                                                    com.pinterest.activity.a.b(this, uri3);
                                                                                    return;
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        b();
                                                                        return;
                                                                    }
                                                                }
                                                                uri = uri3;
                                                                str4 = host;
                                                                contains3 = str4.contains("facebook");
                                                                if (!contains3) {
                                                                }
                                                            } else {
                                                                return;
                                                            }
                                                        }
                                                    }
                                                    z5 = false;
                                                    if (!z5) {
                                                    }
                                                } else {
                                                    return;
                                                }
                                            }
                                        }
                                        z4 = false;
                                        if (z4) {
                                        }
                                    } else {
                                        return;
                                    }
                                }
                            }
                            z3 = false;
                            if (z3) {
                            }
                        }
                    }
                }
                z2 = false;
                contains = arrayList.contains("secure");
                if (!contains) {
                }
                contains2 = arrayList.contains("login");
                if (!contains2) {
                }
                if (!z) {
                }
                com.pinterest.analytics.a.c.a("secure");
            }
        }
        z = false;
        ArrayList arrayList = new ArrayList(uri3.getPathSegments());
        equals = "www.pinterest.com".equals(uri3.getHost());
        if (!equals) {
        }
        size2 = arrayList.size();
        if (size2 == 1) {
        }
        z2 = false;
        contains = arrayList.contains("secure");
        if (!contains) {
        }
        contains2 = arrayList.contains("login");
        if (!contains2) {
        }
        if (!z) {
        }
        com.pinterest.analytics.a.c.a("secure");
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    /* JADX WARNING: Incorrect condition in loop: B:2:0x0011 */
    private static String c(Uri uri) {
        m mVar = new m();
        Iterator<String> it = uri.getQueryParameterNames().iterator();
        while (r0) {
            String next = it.next();
            mVar.a(next, uri.getQueryParameter(next));
        }
        return mVar.toString();
    }

    private void a(String str, final boolean z) {
        AnonymousClass6 r0 = new ae.a() { // from class: com.pinterest.activity.webhook.WebhookActivity.6
            @Override // com.pinterest.api.remote.ae.a
            public final void a(Interest interest) {
                super.a(interest);
                if (interest != null) {
                    Navigation navigation = new Navigation(Location.INTEREST, interest);
                    boolean z2 = z;
                    if (z2) {
                        navigation.a("com.pinterest.EXTRA_INTEREST_TYPE", com.pinterest.activity.interest.b.a.a(com.pinterest.activity.interest.b.a.KLP));
                    }
                    WebhookActivity.this.a(navigation);
                    return;
                }
                WebhookActivity.this.b();
            }

            @Override // com.pinterest.api.h, com.pinterest.api.i
            public final void a(Throwable th, g gVar) {
                WebhookActivity.this.b();
            }
        };
        com.pinterest.api.b.b bVar = b.a.f15112a;
        ae.a(str, (ae.a) r0, com.pinterest.api.b.b.a(14), this._apiTag, true);
    }

    private static String a(String str) {
        String[] split = str.split("-");
        int length = split.length;
        if (length < 2) {
            return str;
        }
        String str2 = split[split.length - 1];
        int length2 = str2.length();
        if (length2 < 12) {
            return str;
        }
        for (int i2 = 0; i2 < length2; i2++) {
            boolean isDigit = Character.isDigit(str2.charAt(i2));
            if (!isDigit) {
                return str;
            }
        }
        return str2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void b() {
        com.pinterest.analytics.a.c.a("home");
        com.pinterest.activity.a.b((Activity) this);
        finish();
    }

    /* access modifiers changed from: package-private */
    public final void a(Navigation navigation) {
        b(navigation);
        finish();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void b(Navigation navigation) {
        startActivity(com.pinterest.activity.a.a(this, navigation));
    }

    private String c() {
        try {
            return getIntent().getStringExtra("com.pinterest.EXTRA_SOURCE");
        } catch (Exception e) {
            CrashReporting.a().b(e);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:37:? A[SYNTHETIC] */
    private boolean a(List<String> list, Uri uri, boolean z) {
        Throwable th;
        boolean z2;
        boolean z3 = false;
        boolean isEmpty = list.isEmpty();
        if (!isEmpty) {
            boolean a2 = org.apache.commons.b.b.a((CharSequence) list.get(0), (CharSequence) "community");
            if (a2) {
                return false;
            }
        }
        String queryParameter = uri.getQueryParameter("invite_code");
        try {
            boolean c2 = org.apache.commons.b.b.c((CharSequence) queryParameter);
            if (!c2) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("invite_code", queryParameter);
                hashMap.put("full_url", uri.toString());
                this._pinalytics.a(y.INVITE_APP_LAND, (String) null, hashMap);
                if (z) {
                    com.pinterest.api.remote.b.b("handleinvite_code_deeplink_auth");
                } else {
                    com.pinterest.api.remote.b.b("handleinvite_code_deeplink_unauth");
                }
                AccountApi.b(queryParameter);
                z2 = true;
            } else {
                z2 = false;
            }
            try {
                boolean isEmpty2 = list.isEmpty();
                if (!isEmpty2) {
                    boolean a3 = org.apache.commons.b.b.a((CharSequence) list.get(0), (CharSequence) "invited");
                    if (a3) {
                        String queryParameter2 = uri.getQueryParameter("inviter_user_id");
                        boolean a4 = org.apache.commons.b.b.a((CharSequence) queryParameter2);
                        if (!a4) {
                            this.g = this.f.c(queryParameter2).b(io.reactivex.j.a.b()).a(io.reactivex.a.b.a.a()).a(new io.reactivex.d.f(this) { // from class: com.pinterest.activity.webhook.a

                                /* renamed from: a  reason: collision with root package name */
                                private final WebhookActivity f14590a;

                                {
                                    this.f14590a = r1;
                                }

                                @Override // io.reactivex.d.f
                                public final void a(Object obj) {
                                    this.f14590a.a(new Navigation(Location.CONVERSATION_INBOX));
                                }
                            }, io.reactivex.e.b.a.b(), b.f14591a);
                            com.pinterest.analytics.a.c.a("invited");
                            return true;
                        }
                    }
                }
                if (z2) {
                    com.pinterest.analytics.a.c.a("invited");
                }
                return false;
            } catch (Throwable th2) {
                th = th2;
                z3 = z2;
                if (!z3) {
                }
            }
        } catch (Throwable th3) {
            th = th3;
            if (!z3) {
                com.pinterest.analytics.a.c.a("invited");
                throw th;
            }
            throw th;
        }
    }

    static final /* synthetic */ void a() {
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void d() {
        com.pinterest.activity.a.a((Activity) this);
        finish();
    }

    /* access modifiers changed from: private */
    public class a extends f {
        private a() {
        }

        /* synthetic */ a(WebhookActivity webhookActivity, byte b2) {
            this();
        }

        @Override // com.pinterest.kit.h.f
        public final void a(Navigation navigation) {
            WebhookActivity.this.b(navigation);
        }

        @Override // com.pinterest.kit.h.f
        public final void a(g.a aVar) {
            WebhookActivity.a(WebhookActivity.this, aVar);
        }

        @Override // com.pinterest.kit.h.f
        public final void a() {
            WebhookActivity.this.finish();
        }

        @Override // com.pinterest.kit.h.f
        public final void a(String str, String str2) {
            com.pinterest.analytics.a.c unused = WebhookActivity.this.f14575d;
            com.pinterest.analytics.a.c.a(str);
        }

        @Override // com.pinterest.kit.h.f
        public final void a(y yVar, HashMap<String, String> hashMap) {
            WebhookActivity.this._pinalytics.a(yVar, (String) null, hashMap);
        }

        @Override // com.pinterest.kit.h.f
        public final void a(Uri uri) {
            com.pinterest.activity.a.a((Activity) WebhookActivity.this, uri);
        }

        @Override // com.pinterest.kit.h.f
        public final boolean b() {
            WebhookActivity.this.b();
            return true;
        }

        @Override // com.pinterest.kit.h.f
        public final void a(fj fjVar, boolean z) {
            com.pinterest.activity.library.a aVar = com.pinterest.activity.library.a.f12729a;
            Intent a2 = com.pinterest.activity.a.a(WebhookActivity.this, com.pinterest.activity.library.a.b(fjVar.a()));
            com.pinterest.kit.h.p.a(a2, z);
            WebhookActivity.this.startActivity(a2);
        }

        @Override // com.pinterest.kit.h.f
        public final void a(Board board, boolean z) {
            a(board, z, false);
        }

        public final void a(Board board, boolean z, boolean z2) {
            Navigation navigation = new Navigation(Location.BOARD, board);
            if (z) {
                navigation.b("com.pinterest.EXTRA_IS_DEEPLINK", true);
            }
            if (z2) {
                navigation.b("com.pinterest.EXTRA_SHOULD_AUTO_SHOW_BOARD_COLLABORATOR_MODAL", true);
            }
            Intent a2 = com.pinterest.activity.a.a(WebhookActivity.this, navigation);
            com.pinterest.kit.h.p.a(a2, false);
            WebhookActivity.this.startActivity(a2);
        }

        @Override // com.pinterest.kit.h.f
        public final void a(dp dpVar) {
            Intent intent = new Intent(WebhookActivity.this, RepinActivity.class);
            com.pinterest.activity.a.a(intent);
            intent.putExtra("com.pinterest.EXTRA_PIN_ID", dpVar.a());
            WebhookActivity.this.startActivity(intent);
        }

        @Override // com.pinterest.kit.h.f
        public final void a(String str) {
            com.pinterest.api.remote.b.a("unauth_pin_deeplink");
            Intent intent = new Intent(Application.g(), UnauthActivity.class);
            intent.putExtra("com.pinterest.EXTRA_PIN_ID", str);
            WebhookActivity.this.startActivity(intent);
        }

        @Override // com.pinterest.kit.h.f
        public final void b(String str) {
            com.pinterest.api.remote.b.a("unauth_board_deeplink");
            Intent intent = new Intent(Application.g(), UnauthActivity.class);
            intent.putExtra("com.pinterest.EXTRA_BOARD_ID", str);
            WebhookActivity.this.startActivity(intent);
        }

        @Override // com.pinterest.kit.h.f
        public final String c() {
            return WebhookActivity.this._apiTag;
        }
    }

    /* JADX WARNING: Incorrect condition in loop: B:26:0x00ce */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00bf A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    private void b(Uri uri) {
        cf cfVar;
        final cf cfVar2;
        final ce ceVar = null;
        com.pinterest.common.d.f.i iVar = i.a.f16390a;
        Uri a2 = com.pinterest.common.d.f.i.a(uri.toString());
        if (a2 != null) {
            boolean equals = TextUtils.equals(a2.getHost(), "post.pinterest.com");
            if (!equals) {
                String queryParameter = a2.getQueryParameter("invite_code");
                boolean c2 = org.apache.commons.b.b.c((CharSequence) queryParameter);
                if (!c2) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("app_start_source", com.pinterest.s.e.c.WEB_URL.toString());
                    hashMap.put("full_url", a2.toString());
                    hashMap.put("invite_code", queryParameter);
                    this._pinalytics.a(new n() { // from class: com.pinterest.activity.webhook.WebhookActivity.2
                        @Override // com.pinterest.analytics.a
                        public final com.pinterest.s.e.n generateLoggingContext() {
                            n.a aVar = new n.a();
                            aVar.f26756a = cf.SERVICE_ENTRY_INVITE_CODE;
                            aVar.f26757b = ce.SERVICE_ENTRY_EXTERNAL_SHARE;
                            return aVar.a();
                        }
                    }.generateLoggingContext(), y.SERVICE_ENTRY, null, null, hashMap);
                    return;
                }
                Set<String> queryParameterNames = a2.getQueryParameterNames();
                if (queryParameterNames != null) {
                    boolean contains = queryParameterNames.contains("utm_source");
                    if (contains) {
                        boolean contains2 = queryParameterNames.contains("utm_medium");
                        if (contains2) {
                            String queryParameter2 = a2.getQueryParameter("utm_content");
                            String queryParameter3 = a2.getQueryParameter("utm_source");
                            String queryParameter4 = a2.getQueryParameter("utm_medium");
                            String queryParameter5 = a2.getQueryParameter("utm_pai");
                            if (queryParameter5 != null) {
                                try {
                                    cfVar = cf.SERVICE_ENTRY_PAID;
                                } catch (NumberFormatException e) {
                                    cfVar = null;
                                    cfVar2 = cfVar;
                                    if (cfVar2 == null) {
                                        return;
                                    }
                                    return;
                                }
                            } else {
                                cfVar = cf.a(Integer.parseInt(queryParameter3));
                            }
                            try {
                                ceVar = ce.a(Integer.parseInt(queryParameter4));
                                cfVar2 = cfVar;
                            } catch (NumberFormatException e2) {
                                cfVar2 = cfVar;
                                if (cfVar2 == null) {
                                }
                            }
                            if (cfVar2 == null && ceVar != null) {
                                try {
                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                    Iterator<String> it = queryParameterNames.iterator();
                                    while (r0) {
                                        String next = it.next();
                                        boolean g = org.apache.commons.b.b.g(next, "cl_");
                                        if (g) {
                                            hashMap2.put(next.replaceFirst("cl_", ""), a2.getQueryParameter(next));
                                        } else {
                                            boolean g2 = org.apache.commons.b.b.g(next, "utm_");
                                            if (g2) {
                                                hashMap2.put(next, a2.getQueryParameter(next));
                                            } else {
                                                boolean a3 = org.apache.commons.b.b.a((CharSequence) next, (CharSequence) "e_t");
                                                if (a3) {
                                                    hashMap2.put("tracking_id", a2.getQueryParameter(next));
                                                }
                                            }
                                        }
                                    }
                                    this._pinalytics.a(new com.pinterest.analytics.n() { // from class: com.pinterest.activity.webhook.WebhookActivity.1
                                        @Override // com.pinterest.analytics.a
                                        public final com.pinterest.s.e.n generateLoggingContext() {
                                            n.a aVar = new n.a();
                                            aVar.f26756a = cfVar2;
                                            aVar.f26757b = ceVar;
                                            return aVar.a();
                                        }
                                    }.generateLoggingContext(), y.SERVICE_ENTRY, queryParameter2, null, hashMap2);
                                    return;
                                } catch (NumberFormatException e3) {
                                    CrashReporting.a().b(e3);
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                    }
                }
                HashMap<String, String> hashMap3 = new HashMap<>();
                hashMap3.put("app_start_source", com.pinterest.s.e.c.WEB_URL.toString());
                hashMap3.put("full_url", a2.toString());
                this._pinalytics.a(new com.pinterest.analytics.n() { // from class: com.pinterest.activity.webhook.WebhookActivity.3
                    @Override // com.pinterest.analytics.a
                    public final com.pinterest.s.e.n generateLoggingContext() {
                        n.a aVar = new n.a();
                        aVar.f26756a = cf.SERVICE_ENTRY_WEB;
                        aVar.f26757b = ce.SERVICE_ENTRY_WEB_UNKNOWN;
                        return aVar.a();
                    }
                }.generateLoggingContext(), y.SERVICE_ENTRY, null, null, hashMap3);
            }
        }
    }

    static /* synthetic */ void a(WebhookActivity webhookActivity, g.a aVar) {
        webhookActivity.startActivity(com.pinterest.activity.a.a(webhookActivity, aVar));
    }
}
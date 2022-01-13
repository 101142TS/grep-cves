package oicq.wlogin_sdk.request;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tencent.smtt.sdk.TbsListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import oicq.wlogin_sdk.sharemem.WloginLoginInfo;
import oicq.wlogin_sdk.sharemem.WloginSigInfo;
import oicq.wlogin_sdk.sharemem.WloginSimpleInfo;
import oicq.wlogin_sdk.tools.cryptor;
import oicq.wlogin_sdk.tools.util;

public class d {

    /* renamed from: f  reason: collision with root package name */
    private static final Object f12556f = new Object();

    /* renamed from: g  reason: collision with root package name */
    private static final Object f12557g = new Object();

    /* renamed from: h  reason: collision with root package name */
    private static b f12558h;

    /* renamed from: i  reason: collision with root package name */
    private static b f12559i;
    Context a;
    WloginLastLoginInfo b = new WloginLastLoginInfo();

    /* renamed from: c  reason: collision with root package name */
    TreeMap<Long, WloginAllSigInfo> f12560c = new TreeMap<>();

    /* renamed from: d  reason: collision with root package name */
    TreeMap<String, UinInfo> f12561d = new TreeMap<>();

    /* renamed from: e  reason: collision with root package name */
    public int f12562e;

    public d(Context context) {
        this.a = context;
        TreeMap<String, UinInfo> a = a(context, "name_file", 0);
        this.f12561d = a;
        if (a == null) {
            this.f12561d = new TreeMap<>();
        }
    }

    /* JADX WARNING: Incorrect condition in loop: B:41:0x0151 */
    /* JADX WARNING: Incorrect condition in loop: B:47:0x01be */
    public synchronized int a(long j2, long j3, byte[][] bArr, long j4, long j5, long j6, long j7, long j8, WloginSimpleInfo wloginSimpleInfo, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6, byte[] bArr7, byte[] bArr8, byte[] bArr9, byte[] bArr10, byte[] bArr11, byte[] bArr12, byte[] bArr13, byte[][] bArr14, long[] jArr, int i2) {
        int i3;
        WloginAllSigInfo wloginAllSigInfo;
        util.LOGI("start put_siginfo", "" + j2);
        Context context = this.a;
        i3 = 0;
        if (context != null) {
            synchronized (f12556f) {
                util.LOGI("put_siginfo load file", "" + j2);
                TreeMap a = a(this.a, "tk_file", j2);
                if (a == null) {
                    a = new TreeMap();
                }
                WloginAllSigInfo wloginAllSigInfo2 = this.f12560c.get(Long.valueOf(j2));
                if (wloginAllSigInfo2 == null) {
                    wloginAllSigInfo2 = (WloginAllSigInfo) a.get(Long.valueOf(j2));
                    if (wloginAllSigInfo2 == null) {
                        wloginAllSigInfo2 = new WloginAllSigInfo();
                    }
                }
                util.LOGI("mainSigMap 0x" + Integer.toHexString(this.f12562e), "" + j2);
                wloginAllSigInfo2.mainSigMap = this.f12562e;
                byte[] bArr15 = new byte[0];
                byte[] bArr16 = new byte[0];
                byte[] bArr17 = new byte[0];
                byte[] bArr18 = new byte[0];
                byte[] bArr19 = new byte[0];
                byte[] bArr20 = new byte[0];
                byte[] bArr21 = new byte[0];
                WloginSigInfo wloginSigInfo = wloginAllSigInfo2._tk_map.get(Long.valueOf(j3));
                if (wloginSigInfo != null) {
                    byte[] bArr22 = wloginSigInfo._en_A1;
                    if (bArr22 != null) {
                        bArr15 = (byte[]) bArr22.clone();
                        byte[] bArr23 = wloginSigInfo._noPicSig;
                        if (bArr23 != null) {
                            bArr16 = (byte[]) bArr23.clone();
                        }
                    }
                    byte[] bArr24 = wloginSigInfo._G;
                    if (bArr24 != null) {
                        bArr17 = (byte[]) bArr24.clone();
                    }
                    byte[] bArr25 = wloginSigInfo._dpwd;
                    if (bArr25 != null) {
                        bArr18 = (byte[]) bArr25.clone();
                    }
                    byte[] bArr26 = wloginSigInfo._randseed;
                    if (bArr26 != null) {
                        bArr19 = (byte[]) bArr26.clone();
                    }
                    bArr20 = wloginSigInfo._psKey;
                    bArr21 = wloginSigInfo._pt4Token;
                }
                byte[] bArr27 = bArr14[6];
                if (bArr27 != null) {
                    int length = bArr14[6].length;
                    if (length > 2) {
                        HashMap hashMap = new HashMap();
                        HashMap hashMap2 = new HashMap();
                        HashMap hashMap3 = new HashMap();
                        HashMap hashMap4 = new HashMap();
                        Ticket.parsePsBuf(bArr20, j6, hashMap, hashMap2);
                        Ticket.parsePsBuf(bArr21, j6, hashMap3, hashMap4);
                        util.LOGI("current pskey size:" + hashMap.size() + ":" + hashMap2.size(), "");
                        Iterator it2 = hashMap2.entrySet().iterator();
                        while (r5) {
                            Map.Entry entry = (Map.Entry) it2.next();
                            String str = (String) entry.getKey();
                            boolean isPskeyStorageExpired = Ticket.isPskeyStorageExpired(((Long) entry.getValue()).longValue());
                            if (isPskeyStorageExpired) {
                                it2.remove();
                                hashMap.remove(str);
                                util.LOGI("delete expired pskey from file,key:" + str, "");
                            }
                        }
                        Iterator it3 = hashMap4.entrySet().iterator();
                        util.LOGI("current pt4token size:" + hashMap3.size() + ":" + hashMap4.size(), "");
                        while (r5) {
                            Map.Entry entry2 = (Map.Entry) it3.next();
                            String str2 = (String) entry2.getKey();
                            boolean isPskeyStorageExpired2 = Ticket.isPskeyStorageExpired(((Long) entry2.getValue()).longValue());
                            if (isPskeyStorageExpired2) {
                                it3.remove();
                                hashMap3.remove(str2);
                                util.LOGI("delete expired pt4token from file,key:" + str2, "");
                            }
                        }
                        Ticket.parseSvrPs(bArr14[6], j6, hashMap, hashMap2, hashMap3, hashMap4);
                        try {
                            bArr14[6] = Ticket.packPsBuf(hashMap, j6, hashMap2);
                            bArr14[12] = Ticket.packPsBuf(hashMap3, j6, hashMap4);
                        } catch (BufferOverflowException unused) {
                            util.LOGI("map size " + hashMap.size() + "is too large", "" + j2);
                            a(Long.valueOf(j2));
                            return util.E_BUFFER_OVERFLOW;
                        }
                    }
                }
                wloginAllSigInfo2.put_simpleinfo(wloginSimpleInfo);
                wloginAllSigInfo2.put_siginfo(j4, j5, j6, j7, j8, bArr2, bArr3, bArr4, bArr5, bArr6, bArr7, bArr8, bArr9, bArr10, bArr11, bArr12, bArr13, bArr14, jArr, i2);
                wloginAllSigInfo2.put_siginfo(j3, bArr, j6);
                a.put(Long.valueOf(j2), wloginAllSigInfo2.get_clone());
                i3 = a(a, "tk_file");
                util.LOGI("save key result:tk_file:" + i3, "");
                if (i3 != 0) {
                    wloginAllSigInfo = wloginAllSigInfo2;
                    WloginSigInfo wloginSigInfo2 = wloginAllSigInfo._tk_map.get(Long.valueOf(j3));
                    if (wloginSigInfo2 != null) {
                        wloginSigInfo2._en_A1 = (byte[]) bArr15.clone();
                        wloginSigInfo2._noPicSig = (byte[]) bArr16.clone();
                        wloginSigInfo2._G = (byte[]) bArr17.clone();
                        wloginSigInfo2._dpwd = (byte[]) bArr18.clone();
                        wloginSigInfo2._randseed = (byte[]) bArr19.clone();
                    }
                } else {
                    wloginAllSigInfo = wloginAllSigInfo2;
                }
                this.f12560c.put(Long.valueOf(j2), wloginAllSigInfo.get_clone());
            }
        }
        return i3;
    }

    public synchronized void b(long j2, long j3) {
        util.LOGI("start clearNewST " + j3, "" + j2);
        Context context = this.a;
        int i2 = 0;
        if (context != null) {
            synchronized (f12556f) {
                TreeMap<Long, WloginAllSigInfo> a = a(this.a, "tk_file", j2);
                if (a != null) {
                    WloginAllSigInfo wloginAllSigInfo = a.get(Long.valueOf(j2));
                    if (wloginAllSigInfo != null) {
                        util.LOGI("clearNewST clear newST in file", "" + j2);
                        wloginAllSigInfo.putNewST(j3, new byte[0], new byte[0]);
                        int a2 = a(a, "tk_file");
                        this.f12560c = a;
                        i2 = a2;
                    } else {
                        return;
                    }
                }
            }
        }
        util.LOGI("end clearNewST ret " + i2, "" + j2);
    }

    public synchronized void c(long j2, long j3) {
        util.LOGI("clear_pskey " + j3, "" + j2);
        WloginAllSigInfo wloginAllSigInfo = this.f12560c.get(Long.valueOf(j2));
        if (wloginAllSigInfo != null) {
            WloginSigInfo wloginSigInfo = wloginAllSigInfo._tk_map.get(Long.valueOf(j3));
            if (wloginSigInfo != null) {
                wloginSigInfo._pt4Token = new byte[0];
                wloginSigInfo._psKey = new byte[0];
                wloginSigInfo.cacheTickets = null;
                wloginSigInfo.cacheUpdateStamp = 0;
                wloginAllSigInfo._tk_map.put(Long.valueOf(j2), wloginSigInfo);
                Context context = this.a;
                if (context != null) {
                    synchronized (f12556f) {
                        TreeMap<Long, WloginAllSigInfo> a = a(this.a, "tk_file", j2);
                        if (a != null) {
                            a.put(Long.valueOf(j2), wloginAllSigInfo);
                            a(a, "tk_file");
                            this.f12560c = a;
                        }
                    }
                }
            }
        }
    }

    public synchronized WloginSigInfo d(long j2, long j3) {
        WloginAllSigInfo a = a(j2);
        if (a == null) {
            return null;
        }
        util.LOGI("get_siginfo get WloginAllSigInfo " + a._tk_map.size() + " " + a._tk_map, j2 + "");
        WloginSigInfo wloginSigInfo = a._tk_map.get(Long.valueOf(j3));
        if (wloginSigInfo == null) {
            return null;
        }
        util.LOGI("get_siginfo get WloginSigInfo sdkappid " + j3 + " " + wloginSigInfo, j2 + "");
        return wloginSigInfo;
    }

    public synchronized WloginSimpleInfo b(long j2) {
        util.LOGI("start get_simpleinfo", "uin=" + j2);
        WloginAllSigInfo a = a(j2);
        if (a == null) {
            return null;
        }
        return a._useInfo.get_clone();
    }

    private static void c(String str) {
        long lastModified = new File(str).lastModified();
        util.LOGI("file " + str + " last update stample " + lastModified, "");
    }

    /* JADX WARNING: Incorrect condition in loop: B:4:0x000f */
    public synchronized String b(Long l) {
        Iterator<String> it2 = this.f12561d.keySet().iterator();
        while (r1) {
            String next = it2.next();
            UinInfo uinInfo = this.f12561d.get(next);
            if (uinInfo != null) {
                boolean equals = uinInfo._uin.equals(l);
                if (equals) {
                    return next;
                }
            }
        }
        return null;
    }

    public synchronized void b(String str) {
        this.f12561d.remove(str);
        util.LOGI("clear_account " + str, "");
        Context context = this.a;
        if (context != null) {
            synchronized (f12557g) {
                TreeMap a = a(this.a, "name_file", 0);
                if (a != null) {
                    a.remove(str);
                    a(a, "name_file");
                }
            }
        }
    }

    public synchronized int b(TreeMap treeMap, String str) {
        int i2;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(treeMap);
            objectOutputStream.flush();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            i2 = a(this.a, str, cryptor.encrypt(byteArray, 0, byteArray.length, t.B));
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (Throwable th) {
            util.LOGI("saveTKTreeMap failed " + th.getStackTrace().toString(), "");
            util.printThrowable(th, "");
            i2 = util.E_SAVE_TICKET_ERROR;
        }
        return i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:158:0x02a9 A[SYNTHETIC, Splitter:B:158:0x02a9] */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x0282 A[EDGE_INSN: B:164:0x0282->B:144:0x0282 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0091  */
    private static TreeMap b(Context context, String str) {
        ObjectInputStream objectInputStream;
        TreeMap treeMap;
        Object th;
        byte[] bArr;
        int i2;
        int i3;
        FileInputStream openFileInput;
        int read;
        ObjectInputStream objectInputStream2;
        int i4;
        int i5;
        int i6;
        ObjectInputStream objectInputStream3;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(t.B, "DESede");
            Cipher instance = Cipher.getInstance("DESede");
            instance.init(2, secretKeySpec);
            objectInputStream = new ObjectInputStream(new CipherInputStream(context.openFileInput(str), instance));
            try {
                treeMap = (TreeMap) objectInputStream.readObject();
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            objectInputStream = null;
            boolean z = th instanceof FileNotFoundException;
            int i7 = 256;
            byte b = 0;
            if (!z) {
                try {
                    byte[] bArr2 = new byte[256];
                    FileInputStream openFileInput2 = context.openFileInput(str);
                    while (true) {
                        int read2 = openFileInput2.read(bArr2, 0, 256);
                        if (read2 <= 0) {
                            break;
                        }
                        util.LOGI(util.buf_to_string(bArr2, read2));
                    }
                    openFileInput2.close();
                } catch (Exception unused) {
                }
            }
            try {
                SecretKeySpec secretKeySpec2 = new SecretKeySpec("%4;7t>;28<fc.5*6".getBytes(), "DESede");
                Cipher instance2 = Cipher.getInstance("DESede");
                instance2.init(2, secretKeySpec2);
                ObjectInputStream objectInputStream4 = new ObjectInputStream(new CipherInputStream(context.openFileInput(str), instance2));
                try {
                    objectInputStream = objectInputStream4;
                    treeMap = (TreeMap) objectInputStream4.readObject();
                } catch (Throwable unused2) {
                    objectInputStream = objectInputStream4;
                    try {
                        bArr = new byte[256];
                        i2 = 40;
                        i3 = TbsListener.ErrorCode.INCR_UPDATE_ERROR;
                        openFileInput = context.openFileInput(str);
                        FileOutputStream openFileOutput = context.openFileOutput("tmp_tk_file", 0);
                        while (true) {
                            read = openFileInput.read(bArr, i2, i3);
                            if (read > 0) {
                                break;
                            }
                            for (int i8 = i2 + read; i8 < i7; i8++) {
                                try {
                                    bArr[i8] = b;
                                } catch (Exception unused3) {
                                    treeMap = null;
                                    context.deleteFile("tmp_tk_file");
                                    if (treeMap != null) {
                                    }
                                    return treeMap;
                                }
                            }
                            int i9 = 0;
                            while (i9 >= 0) {
                                byte[] bytes = new String("WloginAllSigInfo").getBytes();
                                int a = a(bArr, i9, bytes);
                                if (a < 0) {
                                    break;
                                }
                                int length = a + bytes.length;
                                int i10 = length + 8;
                                if (i10 <= i7) {
                                    int i11 = length + 0;
                                    byte b2 = bArr[i11];
                                    if (b2 == -127) {
                                        int i12 = length + 1;
                                        byte b3 = bArr[i12];
                                        if (b3 == 64) {
                                            int i13 = length + 2;
                                            byte b4 = bArr[i13];
                                            if (b4 == 1) {
                                                int i14 = length + 3;
                                                byte b5 = bArr[i14];
                                                if (b5 == 111) {
                                                    int i15 = length + 4;
                                                    byte b6 = bArr[i15];
                                                    if (b6 == -111) {
                                                        int i16 = length + 5;
                                                        byte b7 = bArr[i16];
                                                        if (b7 == -44) {
                                                            int i17 = length + 6;
                                                            byte b8 = bArr[i17];
                                                            objectInputStream3 = objectInputStream;
                                                            if (b8 == 26) {
                                                                int i18 = length + 7;
                                                                try {
                                                                    byte b9 = bArr[i18];
                                                                    i6 = length;
                                                                    if (b9 == -101) {
                                                                        bArr[i11] = 0;
                                                                        bArr[i12] = 0;
                                                                        bArr[i13] = 0;
                                                                        bArr[i14] = 0;
                                                                        bArr[i15] = 0;
                                                                        bArr[i16] = 0;
                                                                        bArr[i17] = 0;
                                                                        bArr[i18] = 1;
                                                                    }
                                                                    objectInputStream = objectInputStream3;
                                                                    i9 = i6;
                                                                    i7 = 256;
                                                                } catch (Exception unused4) {
                                                                    objectInputStream = objectInputStream3;
                                                                    treeMap = null;
                                                                    context.deleteFile("tmp_tk_file");
                                                                    if (treeMap != null) {
                                                                    }
                                                                    return treeMap;
                                                                }
                                                            }
                                                            i6 = length;
                                                            objectInputStream = objectInputStream3;
                                                            i9 = i6;
                                                            i7 = 256;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                objectInputStream3 = objectInputStream;
                                i6 = length;
                                objectInputStream = objectInputStream3;
                                i9 = i6;
                                i7 = 256;
                            }
                            int i19 = 0;
                            while (true) {
                                if (i19 < 0) {
                                    break;
                                }
                                byte[] bytes2 = new String("WloginSigInfo").getBytes();
                                int a2 = a(bArr, i19, bytes2);
                                if (a2 < 0) {
                                    break;
                                }
                                i19 = a2 + bytes2.length;
                                int i20 = i19 + 8;
                                if (i20 <= 256) {
                                    int i21 = i19 + 0;
                                    byte b10 = bArr[i21];
                                    if (b10 == 0) {
                                        int i22 = i19 + 1;
                                        byte b11 = bArr[i22];
                                        if (b11 == 0) {
                                            int i23 = i19 + 2;
                                            byte b12 = bArr[i23];
                                            if (b12 == 0) {
                                                int i24 = i19 + 3;
                                                byte b13 = bArr[i24];
                                                if (b13 == 0) {
                                                    int i25 = i19 + 4;
                                                    byte b14 = bArr[i25];
                                                    if (b14 == 0) {
                                                        int i26 = i19 + 5;
                                                        byte b15 = bArr[i26];
                                                        if (b15 == 0) {
                                                            int i27 = i19 + 6;
                                                            byte b16 = bArr[i27];
                                                            if (b16 == 0) {
                                                                int i28 = i19 + 7;
                                                                byte b17 = bArr[i28];
                                                                if (b17 == 0) {
                                                                    bArr[i21] = 0;
                                                                    bArr[i22] = 0;
                                                                    bArr[i23] = 0;
                                                                    bArr[i24] = 0;
                                                                    bArr[i25] = 0;
                                                                    bArr[i26] = 0;
                                                                    bArr[i27] = 0;
                                                                    bArr[i28] = 1;
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
                            int i29 = 0;
                            while (true) {
                                if (i29 < 0) {
                                    break;
                                }
                                byte[] bytes3 = new String("WloginSimpleInfo").getBytes();
                                int a3 = a(bArr, i29, bytes3);
                                if (a3 < 0) {
                                    break;
                                }
                                int length2 = a3 + bytes3.length;
                                int i30 = length2 + 8;
                                if (i30 <= 256) {
                                    int i31 = length2 + 0;
                                    byte b18 = bArr[i31];
                                    if (b18 == 57) {
                                        int i32 = length2 + 1;
                                        byte b19 = bArr[i32];
                                        if (b19 == -69) {
                                            int i33 = length2 + 2;
                                            byte b20 = bArr[i33];
                                            if (b20 == -84) {
                                                int i34 = length2 + 3;
                                                byte b21 = bArr[i34];
                                                if (b21 == 110) {
                                                    int i35 = length2 + 4;
                                                    byte b22 = bArr[i35];
                                                    if (b22 == -46) {
                                                        int i36 = length2 + 5;
                                                        byte b23 = bArr[i36];
                                                        if (b23 == 98) {
                                                            int i37 = length2 + 6;
                                                            byte b24 = bArr[i37];
                                                            if (b24 == -31) {
                                                                int i38 = length2 + 7;
                                                                byte b25 = bArr[i38];
                                                                i5 = length2;
                                                                if (b25 == -113) {
                                                                    bArr[i31] = 0;
                                                                    bArr[i32] = 0;
                                                                    bArr[i33] = 0;
                                                                    bArr[i34] = 0;
                                                                    bArr[i35] = 0;
                                                                    bArr[i36] = 0;
                                                                    bArr[i37] = 0;
                                                                    bArr[i38] = 1;
                                                                }
                                                                i29 = i5;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                i5 = length2;
                                i29 = i5;
                            }
                            int i39 = 0;
                            while (true) {
                                if (i39 < 0) {
                                    break;
                                }
                                byte[] bytes4 = new String("UinInfo").getBytes();
                                int a4 = a(bArr, i39, bytes4);
                                if (a4 < 0) {
                                    break;
                                }
                                int length3 = a4 + bytes4.length;
                                int i40 = length3 + 8;
                                if (i40 <= 256) {
                                    int i41 = length3 + 0;
                                    byte b26 = bArr[i41];
                                    if (b26 == -118) {
                                        int i42 = length3 + 1;
                                        byte b27 = bArr[i42];
                                        if (b27 == -23) {
                                            int i43 = length3 + 2;
                                            byte b28 = bArr[i43];
                                            if (b28 == Byte.MIN_VALUE) {
                                                int i44 = length3 + 3;
                                                byte b29 = bArr[i44];
                                                if (b29 == -19) {
                                                    int i45 = length3 + 4;
                                                    byte b30 = bArr[i45];
                                                    if (b30 == -26) {
                                                        int i46 = length3 + 5;
                                                        byte b31 = bArr[i46];
                                                        if (b31 == 99) {
                                                            int i47 = length3 + 6;
                                                            byte b32 = bArr[i47];
                                                            if (b32 == 41) {
                                                                int i48 = length3 + 7;
                                                                byte b33 = bArr[i48];
                                                                i4 = length3;
                                                                if (b33 == 14) {
                                                                    bArr[i41] = 0;
                                                                    bArr[i42] = 0;
                                                                    bArr[i43] = 0;
                                                                    bArr[i44] = 0;
                                                                    bArr[i45] = 0;
                                                                    bArr[i46] = 0;
                                                                    bArr[i47] = 0;
                                                                    bArr[i48] = 1;
                                                                    i39 = i4;
                                                                }
                                                                i39 = i4;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                i4 = length3;
                                i39 = i4;
                            }
                            i2 = 40;
                            openFileOutput.write(bArr, 40, read);
                            if (read > 40) {
                                System.arraycopy(bArr, read, bArr, 0, 40);
                            }
                            objectInputStream = objectInputStream;
                            i3 = TbsListener.ErrorCode.INCR_UPDATE_ERROR;
                            i7 = 256;
                            b = 0;
                        }
                        openFileInput.close();
                        openFileOutput.close();
                        objectInputStream2 = new ObjectInputStream(context.openFileInput("tmp_tk_file"));
                        try {
                            treeMap = (TreeMap) objectInputStream2.readObject();
                            objectInputStream = objectInputStream2;
                        } catch (Exception unused5) {
                            objectInputStream = objectInputStream2;
                            treeMap = null;
                            context.deleteFile("tmp_tk_file");
                            if (treeMap != null) {
                            }
                            return treeMap;
                        }
                    } catch (Exception unused6) {
                        treeMap = null;
                        context.deleteFile("tmp_tk_file");
                        if (treeMap != null) {
                        }
                        return treeMap;
                    }
                    context.deleteFile("tmp_tk_file");
                    if (treeMap != null) {
                    }
                    return treeMap;
                }
            } catch (Throwable unused7) {
                bArr = new byte[256];
                i2 = 40;
                i3 = TbsListener.ErrorCode.INCR_UPDATE_ERROR;
                openFileInput = context.openFileInput(str);
                FileOutputStream openFileOutput = context.openFileOutput("tmp_tk_file", 0);
                while (true) {
                    read = openFileInput.read(bArr, i2, i3);
                    if (read > 0) {
                    }
                    objectInputStream = objectInputStream;
                    i3 = TbsListener.ErrorCode.INCR_UPDATE_ERROR;
                    i7 = 256;
                    b = 0;
                }
                openFileInput.close();
                openFileOutput.close();
                objectInputStream2 = new ObjectInputStream(context.openFileInput("tmp_tk_file"));
                treeMap = (TreeMap) objectInputStream2.readObject();
                objectInputStream = objectInputStream2;
                context.deleteFile("tmp_tk_file");
                if (treeMap != null) {
                }
                return treeMap;
            }
            if (treeMap != null) {
            }
            return treeMap;
        }
        if (treeMap != null) {
            try {
                objectInputStream.close();
            } catch (Exception unused8) {
                return null;
            }
        }
        return treeMap;
    }

    public synchronized int a(long j2, long j3) {
        util.LOGI("start clear_da2 " + j3, "" + j2);
        Context context = this.a;
        int i2 = 0;
        if (context != null) {
            synchronized (f12556f) {
                TreeMap<Long, WloginAllSigInfo> a = a(this.a, "tk_file", j2);
                if (a != null) {
                    WloginAllSigInfo wloginAllSigInfo = a.get(Long.valueOf(j2));
                    if (wloginAllSigInfo == null) {
                        return -1;
                    }
                    util.LOGI("clear_da2 clear DA2 in file", "" + j2);
                    wloginAllSigInfo.put_da2(j3, new byte[0]);
                    int a2 = a(a, "tk_file");
                    this.f12560c = a;
                    i2 = a2;
                }
            }
        }
        util.LOGI("end clear_da2 ret " + i2, "" + j2);
        return i2;
    }

    public synchronized void a(long j2, long j3, byte[] bArr) {
        util.LOGI("start put_randseed " + j3, "" + j2);
        Context context = this.a;
        if (context != null) {
            synchronized (f12556f) {
                TreeMap<Long, WloginAllSigInfo> a = a(this.a, "tk_file", j2);
                if (a != null) {
                    WloginAllSigInfo wloginAllSigInfo = a.get(Long.valueOf(j2));
                    if (wloginAllSigInfo == null) {
                        util.LOGI("fileInfo is null " + j3, "" + j2);
                        return;
                    }
                    wloginAllSigInfo.put_randseed(j3, bArr);
                    int a2 = a(a, "tk_file");
                    if (a2 != 0) {
                        util.LOGI("put_randseed refreshTKTreeMap failed ret " + a2 + " appid " + j3, "" + j2);
                        return;
                    }
                    this.f12560c = a;
                    util.LOGI("end put_randseed", "" + j2);
                }
            }
        }
    }

    public synchronized int a(long j2, long j3, long j4, long j5, byte[] bArr, byte[] bArr2) {
        util.LOGI("start put_siginfo " + j3, "" + j2);
        WloginAllSigInfo wloginAllSigInfo = this.f12560c.get(Long.valueOf(j2));
        if (wloginAllSigInfo == null) {
            return -1;
        }
        int put_siginfo = wloginAllSigInfo.put_siginfo(j3, j4, j5, bArr, bArr2);
        Context context = this.a;
        if (context != null) {
            synchronized (f12556f) {
                TreeMap a = a(this.a, "tk_file", j2);
                if (a == null) {
                    a = new TreeMap();
                }
                a.put(Long.valueOf(j2), wloginAllSigInfo.get_clone());
                a(a, "tk_file");
            }
        }
        return put_siginfo;
    }

    public synchronized WloginAllSigInfo a(long j2) {
        WloginAllSigInfo wloginAllSigInfo = this.f12560c.get(Long.valueOf(j2));
        if (wloginAllSigInfo != null) {
            util.LOGI("get_all_siginfo got in mem", "");
            return wloginAllSigInfo;
        }
        Context context = this.a;
        if (context == null) {
            return null;
        }
        TreeMap a = a(context, "tk_file", j2);
        if (a == null) {
            return null;
        }
        WloginAllSigInfo wloginAllSigInfo2 = (WloginAllSigInfo) a.get(Long.valueOf(j2));
        if (wloginAllSigInfo2 == null) {
            return null;
        }
        util.LOGI("got in file", "");
        this.f12560c.put(Long.valueOf(j2), wloginAllSigInfo2);
        return wloginAllSigInfo2;
    }

    public synchronized void a() {
        util.LOGI("refresh_all_siginfo ...", "");
        TreeMap<Long, WloginAllSigInfo> a = a(this.a, "tk_file", 0);
        this.f12560c = a;
        if (a == null) {
            this.f12560c = new TreeMap<>();
        }
    }

    public synchronized void a(Long l) {
        util.LOGI("start clear_sig", "" + l);
        this.f12560c.remove(l);
        Context context = this.a;
        if (context != null) {
            synchronized (f12556f) {
                TreeMap a = a(this.a, "tk_file", l.longValue());
                if (a != null) {
                    a.remove(l);
                    a(a, "tk_file");
                }
            }
        }
    }

    public synchronized void a(Long l, Long l2) {
        util.LOGI("start clear_sig", "" + l);
        WloginAllSigInfo wloginAllSigInfo = this.f12560c.get(l);
        if (wloginAllSigInfo != null) {
            wloginAllSigInfo._tk_map.remove(l2);
            util.LOGI("uin " + l + " appid " + l2 + " sig has been cleared");
        }
        Context context = this.a;
        if (context != null) {
            synchronized (f12556f) {
                TreeMap a = a(this.a, "tk_file", l.longValue());
                if (a != null) {
                    WloginAllSigInfo wloginAllSigInfo2 = (WloginAllSigInfo) a.get(l);
                    if (wloginAllSigInfo2 != null) {
                        wloginAllSigInfo2._tk_map.remove(l2);
                        a(a, "tk_file");
                    }
                }
            }
        }
    }

    public synchronized void a(String str, Long l, boolean z) {
        util.LOGI("put account " + str, "" + l);
        String b = b(l);
        if (b != null) {
            this.f12561d.remove(b);
        }
        UinInfo uinInfo = new UinInfo(l, z);
        this.f12561d.put(str, uinInfo);
        Context context = this.a;
        if (context != null) {
            synchronized (f12557g) {
                TreeMap a = a(this.a, "name_file", 0);
                if (a == null) {
                    a = new TreeMap();
                }
                if (b != null) {
                    a.remove(b);
                }
                a.put(str, uinInfo);
                a(a, "name_file");
            }
        }
    }

    public synchronized void a(String str) {
        this.f12561d.remove(str);
        Context context = this.a;
        if (context != null) {
            synchronized (f12557g) {
                TreeMap a = a(this.a, "name_file", 0);
                if (a == null) {
                    a = new TreeMap();
                }
                a.remove(str);
                a(a, "name_file");
            }
        }
    }

    public synchronized UinInfo a(String str, boolean z) {
        if (z) {
            UinInfo uinInfo = this.f12561d.get(str);
            if (uinInfo != null) {
                util.LOGI("mem got_account name: " + str + " uin: " + uinInfo._uin + ", " + uinInfo.getHasPassword(), "");
                return uinInfo;
            }
        }
        Context context = this.a;
        if (context == null) {
            return null;
        }
        TreeMap a = a(context, "name_file", 0);
        if (a == null) {
            return null;
        }
        UinInfo uinInfo2 = (UinInfo) a.get(str);
        if (uinInfo2 == null) {
            return null;
        }
        this.f12561d.put(str, uinInfo2);
        util.LOGI("file got_account name: " + str + " uin: " + uinInfo2._uin + ", " + uinInfo2.getHasPassword(), "");
        return uinInfo2;
    }

    /* JADX WARNING: Incorrect condition in loop: B:11:0x0024 */
    /* JADX WARNING: Incorrect condition in loop: B:19:0x0052 */
    public synchronized List<WloginLoginInfo> a(boolean z) {
        ArrayList arrayList = new ArrayList();
        Context context = this.a;
        if (context != null) {
            TreeMap a = a(context, "tk_file", 0);
            if (a == null) {
                return arrayList;
            }
            Iterator it2 = a.keySet().iterator();
            while (r4) {
                Long l = (Long) it2.next();
                WloginAllSigInfo wloginAllSigInfo = this.f12560c.get(l);
                if (wloginAllSigInfo == null) {
                    wloginAllSigInfo = (WloginAllSigInfo) a.get(l);
                    if (wloginAllSigInfo != null) {
                        this.f12560c.put(l, wloginAllSigInfo);
                    }
                }
                Iterator<Long> it3 = wloginAllSigInfo._tk_map.keySet().iterator();
                while (r7) {
                    Long next = it3.next();
                    WloginSigInfo wloginSigInfo = wloginAllSigInfo._tk_map.get(next);
                    if (wloginSigInfo != null) {
                        String b = b(l);
                        if (b == null) {
                            b = String.valueOf(l);
                        }
                        WloginSimpleInfo wloginSimpleInfo = wloginAllSigInfo._useInfo;
                        byte[] bArr = wloginSimpleInfo._img_url;
                        if (bArr == null) {
                            wloginSimpleInfo._img_url = new byte[0];
                        }
                        arrayList.add(new WloginLoginInfo(b, l.longValue(), next.longValue(), new String(wloginAllSigInfo._useInfo._img_url), wloginSigInfo._create_time, z ? WloginLoginInfo.TYPE_LOACL : WloginLoginInfo.TYPE_REMOTE, wloginSigInfo._login_bitmap));
                        a = a;
                        it2 = it2;
                    }
                }
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0010, code lost:
        if (r1 != false) goto L_0x0012;
     */
    public synchronized int a(TreeMap treeMap, String str) {
        int i2;
        i2 = 0;
        boolean equals = "tk_file".equals(str);
        if (!equals) {
            boolean equals2 = "name_file".equals(str);
        }
        i2 = b(treeMap, str);
        return i2;
    }

    /* JADX WARNING: Incorrect condition in loop: B:2:0x0005 */
    public static int a(byte[] bArr, int i2, byte[] bArr2) {
        for (int i3 = i2; i3 < r1; i3++) {
            boolean z = false;
            int i4 = 0;
            while (true) {
                int length = bArr2.length;
                if (i4 >= length) {
                    z = true;
                    break;
                }
                byte b = bArr[i3 + i4];
                byte b2 = bArr2[i4];
                if (b != b2) {
                    break;
                }
                i4++;
            }
            if (z) {
                return i3;
            }
        }
        return -1;
    }

    /* JADX WARNING: Incorrect condition in loop: B:15:0x0095 */
    /* JADX WARNING: Incorrect condition in loop: B:20:0x0105 */
    public static TreeMap a(Context context, String str, long j2) {
        util.LOGI("loadTKTreeMap sigfile " + str, "");
        byte[] a = a(context, str);
        if (a != null) {
            util.LOGI("loadTKTreeMap len:" + a.length + " at " + t.l(), "");
            try {
                byte[] decrypt = cryptor.decrypt(a, 0, a.length, t.B);
                if (decrypt != null) {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decrypt);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    TreeMap treeMap = (TreeMap) objectInputStream.readObject();
                    objectInputStream.close();
                    byteArrayInputStream.close();
                    if (treeMap != null) {
                        util.LOGI("loadTKTreeMap tree size: " + treeMap.size(), "");
                        boolean equals = "tk_file".equals(str);
                        if (equals) {
                            int i2 = (0 > j2 ? 1 : (0 == j2 ? 0 : -1));
                            if (i2 == 0) {
                                try {
                                    Iterator it2 = treeMap.keySet().iterator();
                                    while (r9) {
                                        Object next = it2.next();
                                        util.LOGI(next + " allsig: " + ((WloginAllSigInfo) treeMap.get(next))._tk_map, "" + next);
                                    }
                                    return treeMap;
                                } catch (Exception unused) {
                                    return treeMap;
                                }
                            } else {
                                util.LOGI(j2 + " allsig: " + ((WloginAllSigInfo) treeMap.get(Long.valueOf(j2)))._tk_map, "" + j2);
                                return treeMap;
                            }
                        } else {
                            Iterator it3 = treeMap.keySet().iterator();
                            while (r9) {
                                Object next2 = it3.next();
                                UinInfo uinInfo = (UinInfo) treeMap.get(next2);
                                if (uinInfo != null) {
                                    util.LOGI(next2 + " is uin: " + uinInfo._uin, "");
                                }
                            }
                            return treeMap;
                        }
                    } else {
                        util.LOGI("tree is null", "");
                        return null;
                    }
                } else {
                    SecretKeySpec secretKeySpec = new SecretKeySpec(t.B, "DESede");
                    Cipher instance = Cipher.getInstance("DESede");
                    instance.init(2, secretKeySpec);
                    ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(a);
                    ObjectInputStream objectInputStream2 = new ObjectInputStream(new CipherInputStream(byteArrayInputStream2, instance));
                    TreeMap treeMap2 = (TreeMap) objectInputStream2.readObject();
                    if (treeMap2 != null) {
                        objectInputStream2.close();
                        byteArrayInputStream2.close();
                        return treeMap2;
                    }
                }
            } catch (Throwable th) {
                util.printThrowable(th, "");
            }
        } else {
            util.LOGI("dbdata is null", "");
        }
        return b(context, str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00f8 A[Catch:{ all -> 0x0134 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00fb A[Catch:{ all -> 0x0134 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0143  */
    /* JADX WARNING: Removed duplicated region for block: B:65:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:68:? A[SYNTHETIC] */
    public static int a(Context context, String str, byte[] bArr) {
        SQLiteDatabase sQLiteDatabase;
        Throwable th;
        Cursor cursor;
        Exception e2;
        boolean equals;
        SQLiteDatabase sQLiteDatabase2;
        Cursor cursor2 = null;
        try {
            boolean equals2 = str.equals("tk_file");
            if (equals2) {
                b bVar = f12558h;
                if (bVar == null) {
                    f12558h = new b(context, str, null, 1);
                }
                sQLiteDatabase2 = f12558h.getWritableDatabase();
            } else {
                b bVar2 = f12559i;
                if (bVar2 == null) {
                    f12559i = new b(context, str, null, 1);
                }
                sQLiteDatabase2 = f12559i.getWritableDatabase();
            }
            sQLiteDatabase = sQLiteDatabase2;
            try {
                c(sQLiteDatabase.getPath());
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + str + " (ID INTEGER PRIMARY KEY, " + str + " BLOB);");
                cursor = sQLiteDatabase.query(str, new String[]{"ID"}, "ID=0", null, null, null, null);
            } catch (Exception e3) {
                e2 = e3;
                cursor = null;
                try {
                    equals = str.equals("tk_file");
                    if (equals) {
                    }
                    util.printException(e2, "");
                    util.LOGI("save ticket to file failed " + e2.getMessage(), "");
                    if (cursor != null) {
                    }
                    if (sQLiteDatabase == null) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor2 = cursor;
                    if (cursor2 != null) {
                        boolean isClosed = cursor2.isClosed();
                        if (!isClosed) {
                            cursor2.close();
                        }
                    }
                    if (sQLiteDatabase != null) {
                        boolean isOpen = sQLiteDatabase.isOpen();
                        if (true == isOpen) {
                            util.LOGI("write_to_db db closed", "");
                            sQLiteDatabase.close();
                            throw th;
                        }
                        throw th;
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (cursor2 != null) {
                }
                if (sQLiteDatabase != null) {
                }
            }
            try {
                int count = cursor.getCount();
                if (count == 0) {
                    sQLiteDatabase.execSQL("insert into " + str + " (ID, " + str + ") values (?,?);", new Object[]{0, new byte[1]});
                }
                sQLiteDatabase.execSQL("update " + str + " set " + str + " =? where ID=0", new Object[]{bArr});
                if (cursor != null) {
                    boolean isClosed2 = cursor.isClosed();
                    if (!isClosed2) {
                        cursor.close();
                    }
                }
                if (sQLiteDatabase == null) {
                    return 0;
                }
                boolean isOpen2 = sQLiteDatabase.isOpen();
                if (true != isOpen2) {
                    return 0;
                }
                util.LOGI("write_to_db db closed", "");
                sQLiteDatabase.close();
                return 0;
            } catch (Exception e4) {
                e2 = e4;
                equals = str.equals("tk_file");
                if (equals) {
                }
                util.printException(e2, "");
                util.LOGI("save ticket to file failed " + e2.getMessage(), "");
                if (cursor != null) {
                }
                if (sQLiteDatabase == null) {
                }
            }
        } catch (Exception e5) {
            e2 = e5;
            cursor = null;
            sQLiteDatabase = null;
            equals = str.equals("tk_file");
            if (equals) {
                f12558h = null;
            } else {
                f12559i = null;
            }
            util.printException(e2, "");
            util.LOGI("save ticket to file failed " + e2.getMessage(), "");
            if (cursor != null) {
                boolean isClosed3 = cursor.isClosed();
                if (!isClosed3) {
                    cursor.close();
                }
            }
            if (sQLiteDatabase == null) {
                return util.E_SAVE_TICKET_ERROR;
            }
            boolean isOpen3 = sQLiteDatabase.isOpen();
            if (true != isOpen3) {
                return util.E_SAVE_TICKET_ERROR;
            }
            util.LOGI("write_to_db db closed", "");
            sQLiteDatabase.close();
            return util.E_SAVE_TICKET_ERROR;
        } catch (Throwable th4) {
            th = th4;
            sQLiteDatabase = null;
            if (cursor2 != null) {
            }
            if (sQLiteDatabase != null) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:110:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:113:? A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x011b A[Catch:{ all -> 0x013d }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x011e A[Catch:{ all -> 0x013d }] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x014c  */
    public static byte[] a(Context context, String str) {
        SQLiteDatabase sQLiteDatabase;
        Throwable th;
        Cursor cursor;
        Exception e2;
        boolean equals;
        SQLiteDatabase sQLiteDatabase2;
        Boolean bool = Boolean.FALSE;
        Cursor cursor2 = null;
        try {
            boolean equals2 = str.equals("tk_file");
            if (equals2) {
                b bVar = f12558h;
                if (bVar == null) {
                    f12558h = new b(context, str, null, 1);
                }
                sQLiteDatabase2 = f12558h.getReadableDatabase();
            } else {
                b bVar2 = f12559i;
                if (bVar2 == null) {
                    f12559i = new b(context, str, null, 1);
                }
                sQLiteDatabase2 = f12559i.getReadableDatabase();
            }
            sQLiteDatabase = sQLiteDatabase2;
            try {
                c(sQLiteDatabase.getPath());
                cursor = sQLiteDatabase.rawQuery("select count(*) from sqlite_master where type ='table' and name ='" + str + "' ", null);
                try {
                    boolean moveToNext = cursor.moveToNext();
                    if (moveToNext) {
                        int i2 = cursor.getInt(0);
                        if (i2 > 0) {
                            bool = Boolean.TRUE;
                        }
                    }
                    if (cursor != null) {
                        boolean isClosed = cursor.isClosed();
                        if (!isClosed) {
                            cursor.close();
                        }
                    }
                    boolean booleanValue = bool.booleanValue();
                    if (!booleanValue) {
                        if (cursor != null) {
                            boolean isClosed2 = cursor.isClosed();
                            if (!isClosed2) {
                                cursor.close();
                            }
                        }
                        if (sQLiteDatabase == null) {
                            return null;
                        }
                        boolean isOpen = sQLiteDatabase.isOpen();
                        if (true != isOpen) {
                            return null;
                        }
                        util.LOGI("get_from_db db closed", "");
                        sQLiteDatabase.close();
                        return null;
                    }
                    Cursor query = sQLiteDatabase.query(str, new String[]{str}, "ID=0", null, null, null, null);
                    if (query == null) {
                        if (query != null) {
                            boolean isClosed3 = query.isClosed();
                            if (!isClosed3) {
                                query.close();
                            }
                        }
                        if (sQLiteDatabase == null) {
                            return null;
                        }
                        boolean isOpen2 = sQLiteDatabase.isOpen();
                        if (true != isOpen2) {
                            return null;
                        }
                        util.LOGI("get_from_db db closed", "");
                        sQLiteDatabase.close();
                        return null;
                    }
                    boolean moveToFirst = query.moveToFirst();
                    if (moveToFirst) {
                        byte[] blob = query.getBlob(0);
                        query.close();
                        if (query != null) {
                            boolean isClosed4 = query.isClosed();
                            if (!isClosed4) {
                                query.close();
                            }
                        }
                        if (sQLiteDatabase == null) {
                            return blob;
                        }
                        boolean isOpen3 = sQLiteDatabase.isOpen();
                        if (true != isOpen3) {
                            return blob;
                        }
                        util.LOGI("get_from_db db closed", "");
                        sQLiteDatabase.close();
                        return blob;
                    }
                    query.close();
                    if (query != null) {
                        boolean isClosed5 = query.isClosed();
                        if (!isClosed5) {
                            query.close();
                        }
                    }
                    if (sQLiteDatabase == null) {
                        return null;
                    }
                    boolean isOpen4 = sQLiteDatabase.isOpen();
                    if (true != isOpen4) {
                        return null;
                    }
                    util.LOGI("get_from_db db closed", "");
                    sQLiteDatabase.close();
                    return null;
                } catch (Exception e3) {
                    e2 = e3;
                    try {
                        equals = str.equals("tk_file");
                        if (equals) {
                        }
                        util.printException(e2, "");
                        if (cursor != null) {
                        }
                        if (sQLiteDatabase == null) {
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        cursor2 = cursor;
                        if (cursor2 != null) {
                        }
                        if (sQLiteDatabase != null) {
                        }
                    }
                }
            } catch (Exception e4) {
                e2 = e4;
                cursor = null;
                equals = str.equals("tk_file");
                if (equals) {
                }
                util.printException(e2, "");
                if (cursor != null) {
                }
                if (sQLiteDatabase == null) {
                }
            } catch (Throwable th3) {
                th = th3;
                if (cursor2 != null) {
                }
                if (sQLiteDatabase != null) {
                }
            }
        } catch (Exception e5) {
            e2 = e5;
            sQLiteDatabase = null;
            cursor = null;
            equals = str.equals("tk_file");
            if (equals) {
                f12558h = null;
            } else {
                f12559i = null;
            }
            util.printException(e2, "");
            if (cursor != null) {
                boolean isClosed6 = cursor.isClosed();
                if (!isClosed6) {
                    cursor.close();
                }
            }
            if (sQLiteDatabase == null) {
                return null;
            }
            boolean isOpen5 = sQLiteDatabase.isOpen();
            if (true != isOpen5) {
                return null;
            }
            util.LOGI("get_from_db db closed", "");
            sQLiteDatabase.close();
            return null;
        } catch (Throwable th4) {
            th = th4;
            sQLiteDatabase = null;
            if (cursor2 != null) {
                boolean isClosed7 = cursor2.isClosed();
                if (!isClosed7) {
                    cursor2.close();
                }
            }
            if (sQLiteDatabase != null) {
                boolean isOpen6 = sQLiteDatabase.isOpen();
                if (true == isOpen6) {
                    util.LOGI("get_from_db db closed", "");
                    sQLiteDatabase.close();
                    throw th;
                }
                throw th;
            }
            throw th;
        }
    }
}
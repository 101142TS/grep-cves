package com.microsoft.office.auth.live;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.widget.Toast;
import com.microsoft.office.onenotelib.R;
public class OAuthDeprecatedAuthenticatorActivity extends AccountAuthenticatorActivity {
    /* access modifiers changed from: protected */
    @Override // android.accounts.AccountAuthenticatorActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int length = AccountManager.get(this).getAccountsByType(DeprecatedAuthConst.ACCOUNT_TYPE_LIVE_ID).length;
        if (length > 0) {
            Toast.makeText(this, R.string.liveAuthAct_more_than_one, 1).show();
        } else {
            Toast.makeText(this, R.string.liveaccount_deprecated, 1).show();
        }
        setResult(0, getIntent());
        finish();
    }
}

package com.android.uccapp;

import android.support.v4.app.Fragment;

public class LoginActivity extends SuperFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}

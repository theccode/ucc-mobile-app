package com.android.uccapp;

import android.content.Context;

import java.io.File;

public class PathGeneratorObject {
    private static Context mContext;

    public static String getPathName(Context context){
        mContext = context;
        File path = new File(mContext.getExternalFilesDir(null) +
                File.separator +
                context.getResources().getString(R.string.app_name)
                + File.separator
                );
        if (!path.exists()) path.mkdirs();
        return path.getPath() + File.separator;
    }
}

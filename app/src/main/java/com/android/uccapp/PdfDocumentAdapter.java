package com.android.uccapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class PdfDocumentAdapter extends PrintDocumentAdapter {
    private String mPath;
    private Context mContext;
    private String mRegistrationNumber;
    public PdfDocumentAdapter(Context context, String path, String registrationNumber) {
        mPath = path;
        mContext = context;
        mRegistrationNumber = registrationNumber;
    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        if (cancellationSignal.isCanceled()){
            layoutResultCallback.onLayoutCancelled();
        } else {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder(mRegistrationNumber+"_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN).build();
            layoutResultCallback.onLayoutFinished(builder.build(), printAttributes1.equals(printAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            File file = new File(mPath);
            in  = new FileInputStream(file);
            out = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            int size;
            byte[] buff = new byte[1638];
            while((size = in.read(buff)) >= 0 && !cancellationSignal.isCanceled()){
                out.write(buff, 0, size);
            }
            if (cancellationSignal.isCanceled()){
                writeResultCallback.onWriteCancelled();
            } else {
                writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        } catch (IOException e) {
            Log.e("UCC 2021", ""+e.getMessage());
            writeResultCallback.onWriteFailed(""+e.getMessage());
            e.printStackTrace();
        }
    }
}

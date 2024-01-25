package com.glitss.teabreak_app.Ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.glitss.teabreak_app.ModelClass.orderDetails;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.Constant;

public class CCAvenueActivity extends AppCompatActivity {

    Context mContext;
    WebView webview;
    ProgressDialog progress;
    orderDetails order;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ccavenue);
//        ButterKnife.bind(this);
        mContext = CCAvenueActivity.this;
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(true);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new MyWebViewClient());

        order = getIntent().getParcelableExtra("order");
        show();
        String data="encRequest=" + order.getEnc_val() + "&access_code=" + order.getAccess_code();
        webview.postUrl(Constant.CCAVENUE_URL ,data.getBytes());

    }




    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(webview, url);
            if (progress.isShowing())
                hide();

            if (url.equalsIgnoreCase(order.getRedirect_url()) || url.equalsIgnoreCase(order.getCancel_url())) {
                webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        }
    }

    @SuppressWarnings("unused")
    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            // process the html source code to get final status of transaction
            String status = null;
            if (html.indexOf("Failure") != -1) {
                status = "Transaction Declined!";
            } else if (html.indexOf("Success") != -1) {
                status = "Transaction Successful!";
            } else if (html.indexOf("Aborted") != -1) {
                status = "Transaction Cancelled!";
            } else {
                status = "Status Not Known!";
            }
            Intent intent = new Intent(getApplicationContext(), PaymentStatusActivity.class);
            intent.putExtra("transStatus", status);
            startActivity(intent);
            finish();
        }
    }



    public void show() {
        if (!((Activity) mContext).isFinishing())
            if (progress != null && !progress.isShowing())
                progress.show();

    }

    public void hide() {
        if (!((Activity) mContext).isFinishing())
            if (progress != null && progress.isShowing())
                progress.dismiss();

    }
}
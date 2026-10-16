package com.masiddiqui.feescalculator;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.print.PrintDocumentAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView w;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        w = new WebView(this);
        w.setWebViewClient(new WebViewClient());
        w.setWebChromeClient(new WebChromeClient());

        WebSettings s = w.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);

        w.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void savePDF() {
                runOnUiThread(() -> createPDF(w));
            }
        }, "AndroidPrint");

        setContentView(w);

        w.loadUrl("file:///android_asset/index.html");
    }

    private void createPDF(WebView webView) {

        PrintManager printManager =
            (PrintManager) getSystemService(Context.PRINT_SERVICE);

        if (printManager != null) {

            String jobName = "Fees Receipt";

            PrintDocumentAdapter adapter =
                webView.createPrintDocumentAdapter(jobName);

            PrintAttributes attributes =
                new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(
                        new PrintAttributes.Resolution(
                            "pdf", "pdf", 300, 300
                        )
                    )
                    .setMinMargins(
                        PrintAttributes.Margins.NO_MARGINS
                    )
                    .build();

            printManager.print(jobName, adapter, attributes);

        } else {
            Toast.makeText(
                this,
                "PDF save facility is not available",
                Toast.LENGTH_LONG
            ).show();
        }
    }
}

package com.freshlancers.barcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.editViewName)
    EditText editViewName;
    @BindView(R.id.editViewAddress)
    EditText editViewAddress;
    @BindView(R.id.qrCode)
    ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.generateQRCode, R.id.scanQRCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.generateQRCode:
                String json = "{'name':'" +
                        editViewName.getText().toString() +
                        "','address':'" +
                        editViewAddress.getText().toString() +
                        "'}";
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(json, BarcodeFormat.QR_CODE, 400, 400);
                    qrCodeImageView.setImageBitmap(bitmap);
                    Log.e(TAG, "onViewClicked: " + json);
                    Toast.makeText(MainActivity.this, json, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "onViewClicked: " + e.getMessage());
                }
                break;
            case R.id.scanQRCode:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator
                        .setOrientationLocked(true)
                        .setCameraId(0)
                        .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                        .setPrompt("Scan a QR Code")
                        .initiateScan();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "onActivityResult: " + result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

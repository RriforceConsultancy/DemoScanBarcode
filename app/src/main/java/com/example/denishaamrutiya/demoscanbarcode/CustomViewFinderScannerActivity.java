package com.example.denishaamrutiya.demoscanbarcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.denishaamrutiya.demoscanbarcode.compatibility.CodeReuse;
import com.example.denishaamrutiya.demoscanbarcode.helper.DbHelper;
import com.example.denishaamrutiya.demoscanbarcode.model.ScanningItemPojo;
import com.google.zxing.Result;
import com.zookey.universalpreferences.UniversalPreferences;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CustomViewFinderScannerActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private List<ScanningItemPojo> scanList = new ArrayList<>();
    private CodeReuse codeReuse = new CodeReuse();
    private Realm realm;
    private DbHelper dbHelper;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_custom_view_finder_scanner);
        getSupportActionBar().hide();

        realm = Realm.getDefaultInstance();
        dbHelper = new DbHelper(this, realm);
        /* setupToolbar();
         */
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        RealmResults<ScanningItemPojo> realmResults = realm.where(ScanningItemPojo.class).equalTo("title",rawResult.getText().toString()).findAll();
        Log.e("@@@data",rawResult.getText());
        if(realmResults.isEmpty()){
            if(dbHelper.addScanningItemPojo(rawResult.getText().toString(),"1")){
                Intent intent = new Intent(CustomViewFinderScannerActivity.this, MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
            }
        }else {
            ScanningItemPojo scanningItemPojo=realmResults.first();
            int quantity= Integer.parseInt(scanningItemPojo.getQuntity())+1;

            if(dbHelper.updateScanningItemPojo(scanningItemPojo.getId(),rawResult.getText(), String.valueOf(quantity))){
                Intent intent = new Intent(CustomViewFinderScannerActivity.this, MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
            }

        }


        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(CustomViewFinderScannerActivity.this);
            }
        }, 2000);



    }

    private static class CustomViewFinderView extends ViewFinderView {
        //public static final String TRADE_MARK_TEXT = "ZXing";
        //public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public static final int TRADE_MARK_TEXT_SIZE_SP = 80;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            /* canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);*/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

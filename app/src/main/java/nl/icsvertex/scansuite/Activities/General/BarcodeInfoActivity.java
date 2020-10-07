package nl.icsvertex.scansuite.Activities.General;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.R;

public class BarcodeInfoActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;

    private TextView textViewBarcodeData;
    private TextView textViewLenghtData;
    private TextView textViewTypeData;
    private TextView textViewPrefixData;
    private TextView textViewCrlfData;
    private TextView textViewCheckDigitData;
    private TextView textViewBarcodeLayoutData;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_barcodeinfo);
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
}

    @Override
    protected void onStop() {
        super.onStop();
        cBarcodeScan.pUnregisterBarcodeReceiver();
        finish();
    }

    @Override
    public void onBackPressed() {
        this.mLeaveActivity();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();
        this.mFindViews();
        this.mSetToolbar(getResources().getString(R.string.screentitle_barcodeinfo));
        this.mSetListeners();
        this.mFieldsInitialize();
        this.mInitScreen();
    }

    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity  = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager  = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {
            this.toolbarImage = findViewById(R.id.toolbarImage);
            this.toolbarTitle = findViewById(R.id.toolbarTitle);

            this.textViewBarcodeData = findViewById(R.id.textViewBarcodeData);
            this.textViewLenghtData = findViewById(R.id.textViewLenghtData);
            this.textViewTypeData = findViewById(R.id.textViewTypeData);
            this.textViewPrefixData = findViewById(R.id.textViewPrefixData);
            this.textViewCrlfData = findViewById(R.id.textViewCrlfData);
            this.textViewCheckDigitData = findViewById(R.id.textViewCheckDigitData);
            this.textViewBarcodeLayoutData = findViewById(R.id.textViewBarcodeLayoutData);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_barcode_black_24dp);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {

    }

    @Override
    public void mInitScreen() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() ==  android.R.id.home) {
            this.mLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan){

        this.textViewBarcodeData.setText(pvBarcodeScan.getBarcodeOriginalStr());
        this.textViewLenghtData.setText(cText.pIntToStringStr(pvBarcodeScan.getBarcodeOriginalStr().length()));
        this.textViewTypeData.setText(pvBarcodeScan.getBarcodeTypeStr());

        String prefixStr = cRegex.pGetPrefix(pvBarcodeScan.getBarcodeOriginalStr());

        if (prefixStr.isEmpty()) {
            prefixStr = cAppExtension.activity.getString(R.string.none);
        }

        this.textViewPrefixData.setText(prefixStr);

        if (pvBarcodeScan.barcodeTypeStr.toUpperCase().contains("EAN")) {
            this.textViewCheckDigitData.setText(pvBarcodeScan.getBarcodeOriginalStr().replace(pvBarcodeScan.getBarcodeFormattedStr(),""));
        }
        else {
            this.textViewCheckDigitData.setText(cAppExtension.activity.getString(R.string.none));
        }

        String crlfStr;

        if (pvBarcodeScan.getContainsCrlfBln()) {
            crlfStr = "true";
        }
        else{
            crlfStr = "false";
        }

        this.textViewCrlfData.setText(crlfStr);


        StringBuilder barcodeLayoutsStr = new StringBuilder(cAppExtension.activity.getString(R.string.none));

        ArrayList<cBarcodeLayout> barcodeLayoutsObl = cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr());

        assert barcodeLayoutsObl != null;
        if (barcodeLayoutsObl.size() > 0) {
            barcodeLayoutsStr = new StringBuilder();

            for (cBarcodeLayout barcodeLayout : barcodeLayoutsObl) {
                barcodeLayoutsStr.append(barcodeLayout.getBarcodeLayoutStr()).append(",");
            }
        }

        if (barcodeLayoutsStr.toString().endsWith(",")) {
            barcodeLayoutsStr = new StringBuilder(barcodeLayoutsStr.substring(0, barcodeLayoutsStr.length() - 1));
        }

        this.textViewBarcodeLayoutData.setText(barcodeLayoutsStr.toString());

    }



    //End Region Public Methods

    //Region Private Methods

    private void mLeaveActivity(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, MainDefaultActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }



    //End Region Private Methods

}

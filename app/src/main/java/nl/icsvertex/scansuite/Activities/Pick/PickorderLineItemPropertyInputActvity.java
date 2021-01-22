package nl.icsvertex.scansuite.Activities.Pick;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLineProperty;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValueInputAdapter;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class PickorderLineItemPropertyInputActvity extends AppCompatActivity implements iICSDefaultActivity {

   //Region Private Properties
   private  ImageView toolbarImage;
   private  TextView toolbarTitle;

   private TextView articleDescriptionCompactText;
   private TextView articleDescription2CompactText;
   private TextView articleItemCompactText;
   private TextView articleBarcodeCompactText;
   private TextView quantityCompactText;

    private  RecyclerView itemPropertyRecyclerview;
    private  Button imageButtonDone;
    private  List<cPickorderLinePropertyValue> localItemPropertyValueObl;

    public double getQuantityHandledDbl(){

        double quantityDbl = 0;

        if (this.localItemPropertyValueObl == null || this.localItemPropertyValueObl.size() == 0) {
            return quantityDbl;
        }

        for (cPickorderLinePropertyValue pickorderLinePropertyValue : this.localItemPropertyValueObl ) {
            quantityDbl += pickorderLinePropertyValue.getQuanitityDbl();
        }

        return  quantityDbl;

    }

    public  double getQuantityAvailable() {
     return    cPickorderLine.currentPickOrderLine.getQuantityDbl() - this.getQuantityHandledDbl();
    }

    private cPickorderLinePropertyValueInputAdapter pickorderLinePropertyValueInputAdapter;
    private cPickorderLinePropertyValueInputAdapter getPickorderLinePropertyValueInputAdapter(){
        if (this.pickorderLinePropertyValueInputAdapter == null) {
            this.pickorderLinePropertyValueInputAdapter = new cPickorderLinePropertyValueInputAdapter();
        }

        return  pickorderLinePropertyValueInputAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public  PickorderLineItemPropertyInputActvity() {

    }


    //End Region Constructor

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pickorderlineitemproperty_input);

        Bundle extras = getIntent().getExtras();
        this.localItemPropertyValueObl  = extras.getParcelableArrayList("pickorderLinePropertyValuesObl");

        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cConnection.pRegisterWifiChangedReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
            cConnection.pUnregisterWifiChangedReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.mGoBackToPickActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        mGoBackToPickActivity();
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_itemproperty_input));

        this.mFieldsInitialize();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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

            this.articleDescriptionCompactText = findViewById(R.id.articleDescriptionCompactText);
            this.articleDescription2CompactText = findViewById(R.id.articleDescription2CompactText);
            this.articleItemCompactText = findViewById(R.id.articleItemCompactText);
            this.articleBarcodeCompactText = findViewById(R.id.articleBarcodeCompactText);
            this.quantityCompactText = findViewById(R.id.quantityCompactText);
            this.itemPropertyRecyclerview = findViewById(R.id.itemPropertyRecyclerview);
            this.imageButtonDone = findViewById(R.id.imageButtonDone);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        this.toolbarImage.setImageResource(R.drawable.ic_info);
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
        this.mSetArticleInfo();
        this.mSetQuantityText();
        this.mShowHideOKButton();
    }

    @Override
    public void mSetListeners() {
        this.mSetHeaderListener();
        this.mSetCloseListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Regioni ICSDefaultFragment methods

    //Region Public Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        if (cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl().size() == 1) {
            cPickorderLineProperty.currentPickorderLineProperty = cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl().get(0);

            if (!cRegex.pCheckRegexBln( cPickorderLineProperty.currentPickorderLineProperty.getItemProperty().getLayoutStr(),pvBarcodeScan.getBarcodeOriginalStr())) {
                cUserInterface.pShowSnackbarMessage(this.itemPropertyRecyclerview,cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),R.raw.badsound, true);
                return;
            }

            if (this.getQuantityHandledDbl() == cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
                cUserInterface.pShowSnackbarMessage(this.itemPropertyRecyclerview,cAppExtension.activity.getString(R.string.message_extra_pieces_not_allowed),R.raw.badsound, true);
                return;
            }

            cPickorderLineProperty.currentPickorderLineProperty.pValueAdded(pvBarcodeScan.getBarcodeOriginalStr());
            this.localItemPropertyValueObl = cPickorderLineProperty.currentPickorderLineProperty.propertyValueObl();
            this.pRefreshActivity();
        }
    }

    public  void pHandled() {

    }

    public  void pRefreshActivity(){
        this.mSetItemPropertyValueRecycler();
        this.mSetQuantityText();
        this.mShowHideOKButton();
    }

    public void pDeleteValueFromRecyler() {
        this.localItemPropertyValueObl.remove(cPickorderLinePropertyValue.currentPickorderLinePropertyValue);

        if (this.localItemPropertyValueObl.size() == 0 ) {
            cPickorderLinePropertyValue pickorderLinePropertyValue  =  new cPickorderLinePropertyValue(cPickorderLineProperty.currentPickorderLineProperty);
            this.localItemPropertyValueObl.add(pickorderLinePropertyValue);
        }

        cPickorderLinePropertyValue.currentPickorderLinePropertyValue = null;
    }

    public void pShowNumericInputFragment() {
        mShowNumberFragment();
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetCloseListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getQuantityAvailable() > 0 ) {
                    mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
                    return;
                }

               pHandled();
            }
        });
    }

    private void mSetHeaderListener() {

        this.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToBottom();
            }
        });

        this.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToTop();
                return true;
            }
        });
    }

    private void mScrollToTop() {
        this.itemPropertyRecyclerview.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (this.getPickorderLinePropertyValueInputAdapter()!= null) {
            if (this.getPickorderLinePropertyValueInputAdapter().getItemCount() > 0) {
                this.itemPropertyRecyclerview.smoothScrollToPosition(this.getPickorderLinePropertyValueInputAdapter().getItemCount() -1 );
            }
        }
    }

    private void mSetItemPropertyValueRecycler() {
        this.itemPropertyRecyclerview.setHasFixedSize(false);
        this.itemPropertyRecyclerview.setAdapter(this.getPickorderLinePropertyValueInputAdapter());
        this.itemPropertyRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.getPickorderLinePropertyValueInputAdapter().pFillData(this.localItemPropertyValueObl);
    }

    private void mSetArticleInfo(){
        this.articleDescriptionCompactText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.articleDescription2CompactText.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        this.articleItemCompactText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
        this.articleBarcodeCompactText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
    }

    private  void mSetQuantityText() {
        String quantityStr = cText.pDoubleToStringStr(this.getQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl());
        this.quantityCompactText.setText(quantityStr);
    }

    private  void mShowHideOKButton() {

        if (this.getQuantityHandledDbl() == 0) {
            this.imageButtonDone.setVisibility(View.GONE);
            return;
        }

        this.imageButtonDone.setVisibility(View.VISIBLE);

    }

    private  void mShowUnderPickDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_underpick_header),
                cAppExtension.activity.getString(R.string.message_underpick_text,
                        cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl()),
                        cText.pDoubleToStringStr(this.getQuantityHandledDbl())),
                pvRejectStr,
                pvAcceptStr ,
                false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    public   void pTryToChangePickedQuantity(Boolean pvIsPositiveBln,  Boolean pvAmountFixedBln, double pvAmountDbl) {

        double newQuantityDbl;
        List<cPickorderLineBarcode>  hulpObl;
        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {

                //Check if we already have barcodes and clear them
                if (cPickorderLine.currentPickOrderLine.handledBarcodesObl().size() > 0 ) {

                    hulpObl = new ArrayList<>(cPickorderLine.currentPickOrderLine.handledBarcodesObl());

                    for (cPickorderLineBarcode pickorderLineBarcode : hulpObl) {
                        pickorderLineBarcode.pDeleteFromDatabaseBln();
                    }
                }
                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() + pvAmountDbl;
            }

            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;

            //Add or update line barcodeStr
            cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcode(pvAmountDbl);

            //Update orderline info (quantityDbl, timestamp, localStatusInt)
            cPickorderLine.currentPickOrderLine.pHandledIndatabase();
            return;
        }

        //negative
        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() == 0 ) {
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl= cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = 0.0;
        }else {
            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
        }

        //Remove or update line barcodeStr
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcode();

    }

    private void mShowNumberFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cPickorderLinePropertyValue.currentPickorderLinePropertyValue.getQuanitityDbl());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, this.getQuantityAvailable());
        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);
        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private  void mGoBackToPickActivity() {

        Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    //End Region Private Methods
}

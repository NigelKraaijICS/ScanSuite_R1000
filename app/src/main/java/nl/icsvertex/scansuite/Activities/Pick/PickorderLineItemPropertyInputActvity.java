package nl.icsvertex.scansuite.Activities.Pick;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderCompositeBarcode.cPickorderCompositeBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLineProperty;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DatePickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DynamicItemPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyNoInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyTextInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.PagerAdapters.ItemPropertyPagerAdapter;
import nl.icsvertex.scansuite.R;

public class PickorderLineItemPropertyInputActvity extends AppCompatActivity implements iICSDefaultActivity {

   //Region Private Properties
   private  ImageView toolbarImage;
   private  TextView toolbarTitle;

   private AppCompatImageButton imageButtonNoInputPropertys;
   private TextView articleDescriptionCompactText;
   private TextView articleDescription2CompactText;
   private TextView articleItemCompactText;
   private TextView articleBarcodeCompactText;
   private TextView quantityCompactText;

    private List<String> titleObl;
    private AppCompatImageButton imageButtonDone;

    private TabLayout itemPropertyTabLayout;
    private ViewPager itemPropertyViewpager;
    public  int numberOfTabsInt;
    public boolean amountHandledBln;

    private  List<cPickorderLinePropertyValue> localItemPropertyValueObl (){

        List<cPickorderLinePropertyValue> resultObl = new ArrayList<>();
        List<cPickorderLineProperty> hulpObl = new ArrayList<>();

        if (cPickorderLine.currentPickOrderLine.pickorderLinePropertyValuesObl() != null && cPickorderLine.currentPickOrderLine.pickorderLinePropertyValuesObl() .size() > 0 ) {
            resultObl.addAll(cPickorderLine.currentPickOrderLine.pickorderLinePropertyValuesObl());
            for(cPickorderLinePropertyValue pickorderLinePropertyValue : resultObl){
                if (!hulpObl.contains(pickorderLinePropertyValue.getPickorderLineProperty())){
                    hulpObl.add(pickorderLinePropertyValue.getPickorderLineProperty());
                }
            }
        }

        for (cPickorderLineProperty inputPickorderLineProperty : cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl()) {
            if (hulpObl.contains(inputPickorderLineProperty)){
                continue;
            }
            resultObl.add(new cPickorderLinePropertyValue(inputPickorderLineProperty));
        }
        return resultObl;
    }

    private LinkedHashMap<String, ArrayList<cPickorderLinePropertyValue>> localItemPropertySortObl(){
        LinkedHashMap<String, ArrayList<cPickorderLinePropertyValue>> linkedHashMap = new LinkedHashMap<>();
       // ArrayList<cPickorderLinePropertyValue> pickorderLinePropertyValues = new ArrayList<>();

        for (cPickorderLinePropertyValue pickorderLinePropertyValue : localItemPropertyValueObl()) {
                //Create the hasmap dynammically and fill it
            ArrayList<cPickorderLinePropertyValue> loopList = linkedHashMap.get(pickorderLinePropertyValue.getPropertyCodeStr());
            if (loopList == null) {
                ArrayList<cPickorderLinePropertyValue> propertyValues = new ArrayList<>();
                propertyValues.add(pickorderLinePropertyValue);
                linkedHashMap.put(pickorderLinePropertyValue.getPropertyCodeStr(), propertyValues);
            }
            else{
                loopList.add(pickorderLinePropertyValue);
            }

        }
        return linkedHashMap;
    }

    public double getQuantityHandledDbl(){

        double quantityDbl = 0;

        if (this.titleObl == null || this.localItemPropertySortObl().size() == 0) {
            return quantityDbl;
        }

        ArrayList<cPickorderLinePropertyValue> loopList = localItemPropertySortObl().get(this.titleObl.get(0));

        for (cPickorderLinePropertyValue pickorderLinePropertyValue : loopList ) {
            quantityDbl += pickorderLinePropertyValue.getQuantityDbl();
        }

        return  quantityDbl;

    }

    public  double getQuantityAvailable() {
     return    cPickorderLine.currentPickOrderLine.getQuantityDbl() - this.getQuantityHandledDbl();
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
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cConnection.pRegisterWifiChangedReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
            cConnection.pUnregisterWifiChangedReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (getQuantityAvailable() > 0 ) {
                mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
                return true;
            }
            if (!mCheckAllPropertysHandledBln()){
                return true;
            }
            this.mGoBackToLinesActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (getQuantityAvailable() > 0 ) {
            mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }
        if (!mCheckAllPropertysHandledBln()){
            return;
        }
        mGoBackToLinesActivity();
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

            this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);
            this.articleDescriptionCompactText = findViewById(R.id.articleDescriptionCompactText);
            this.articleDescription2CompactText = findViewById(R.id.articleDescription2CompactText);
            this.articleItemCompactText = findViewById(R.id.articleItemCompactText);
            this.articleBarcodeCompactText = findViewById(R.id.articleBarcodeCompactText);
            this.quantityCompactText = findViewById(R.id.quantityCompactText);
            this.itemPropertyTabLayout = findViewById(R.id.itemPropertyTabLayout);
            this.itemPropertyViewpager = findViewById(R.id.itemPropertyViewpager);
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
       // this.mSetItemPropertyValueRecycler();
        this.mSetArticleInfo();
        this.mSetQuantityText();
        this.mShowHideOKButton();
        this.mBuildAndFillTabs();
    }

    @Override
    public void mSetListeners() {
        this.mSetHeaderListener();
        this.mSetNoInputPropertyListener();
        this.mSetCloseListener();
        this.mSetTabLayoutListener();
    }

    @Override
    public void mInitScreen() {

        if ( cPickorderCompositeBarcode.currentCompositePickorderBarcode != null) {
            this.mHandleCompositeData();
        }
    }

    //End Regioni ICSDefaultFragment methods

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        if (cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl().size() == 1) {
            cPickorderLineProperty.currentPickorderLineProperty = cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl().get(0);
            this.pHandeManualAction(pvBarcodeScan);
        }
    }

    public  void pCancelPick() {
        //Check if we need to remove the SalesorderPackingTableLines
        if (cPickorderLine.currentPickOrderLine.pGetLinesForProcessingSequenceObl().size() <= 1)  {
            cSalesOrderPackingTable.pDeleteFromDatabaseBln(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
        }
        cPickorderLine.currentPickOrderLine.pUpdateProcessingSequenceBln("");
        cPickorderLine.currentPickOrderLine.pCancelIndatabase();
        this.mGoBackToLinesActivity();
    }

    public  void pHandeManualAction(cBarcodeScan pvBarcodeScan){
        if (cPickorderLineProperty.currentPickorderLineProperty.getItemProperty()== null ) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_property_unknown),"",true, true);
            return;
        }


        //Check if we scanned (another) composite barcode and handle it
        List<cPickorderCompositeBarcode> compositeBarcodesMatchedObl =   cPickorderLine.currentPickOrderLine.pFindCompositeBarcodeForLine(pvBarcodeScan);
        if  (compositeBarcodesMatchedObl.size() > 0) {
            if (!cPickorderLine.currentPickOrderLine.pFindBarcodeViaCompositeBarcodeInLineBarcodes(compositeBarcodesMatchedObl, pvBarcodeScan.getBarcodeOriginalStr())) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),"",true, true);
            }
            else{
                this.mHandleCompositeData();
            }
            return;
        }

        if (!cRegex.pCheckRegexBln( cPickorderLineProperty.currentPickorderLineProperty.getItemProperty().getLayoutStr(),pvBarcodeScan.getBarcodeOriginalStr())) {
            cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout,cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),R.raw.badsound, true);
            return;
        }

        if (!this.amountHandledBln){
            if (this.getQuantityHandledDbl() == cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
                cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout,cAppExtension.activity.getString(R.string.message_extra_pieces_not_allowed),R.raw.badsound, true);
                return;
            }
        }
        //Check unique values if needed
        cResult hulpRst = cPickorderLineProperty.currentPickorderLineProperty.pCheckScanForUniquePropertyRst(pvBarcodeScan.getBarcodeOriginalStr());
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true, true);
            return;
        }

        cPickorderLineProperty.currentPickorderLineProperty.pValueAdded(pvBarcodeScan.getBarcodeOriginalStr());

        if (this.amountHandledBln){
            cPickorderLinePropertyValue.currentPickorderLinePropertyValue.quantityDbl = cPickorderLine.currentPickOrderLine.getQuantityDbl();
        }

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, pvBarcodeScan.getBarcodeOriginalStr() + " "  + cAppExtension.activity.getString(R.string.addedorhandled),R.raw.headsupsound,false);
        this.pTryToChangePickedQuantity(true,false,1);
        this.pRefreshActivity();

    }

    public void pHandled() {
        PickorderPickActivity.handledViaPropertysBln = true;
        if (getQuantityAvailable() > 0 && !this.amountHandledBln) {
            mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }
        if (!mCheckAllPropertysHandledBln()){
            return;
        }
        this.mGoBackToLinesActivity();
    }

    public void pRefreshActivity(){
       // this.mSetItemPropertyValueRecycler();
        this.mSetQuantityText();
        this.mShowHideOKButton();
        this.mBuildAndFillTabs();
        this.mSelectTabAndItem();
    }

    private void mSelectTabAndItem(){
        if(cPickorderLinePropertyValue.currentPickorderLinePropertyValue ==null){
            return;
        }
        this.itemPropertyTabLayout.selectTab(this.itemPropertyTabLayout.getTabAt(this.titleObl.indexOf(cPickorderLinePropertyValue.currentPickorderLinePropertyValue.getPropertyCodeStr())));
    }

    public void pDeleteValueFromRecyler() {
        cPickorderLinePropertyValue.allLinePropertysValuesObl.remove(cPickorderLinePropertyValue.currentPickorderLinePropertyValue);
        cPickorderLinePropertyValue.currentPickorderLinePropertyValue = null;
    }

    public void pShowNumericInputFragment() {
        mShowNumberPickerFragment();
    }

    public void pShowTextInputFragment() {
        mShowTextInputFragment();
    }

    public void pResetTab(boolean pvResetBln){
        if (pvResetBln) {
            this.itemPropertyTabLayout.selectTab(this.itemPropertyTabLayout.getTabAt(0));
       //     this.itemPropertyViewpager.setCurrentItem(0);
        }
    }
    public void pShowDatePickerDialog() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTDATEFRAGMENT_TAG);
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
                amountHandledBln = true;
                if (!mCheckAllPropertysHandledBln()){
                   return;
                }
                pHandled();
            }
        });
    }

    private boolean mCheckAllPropertysHandledBln(){

        if (!this.amountHandledBln){
            return false;
        }

        for (cPickorderLineProperty inputPickorderLineProperty : cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl()){
            double quantityDbl = 0.0;
            ArrayList<cPickorderLinePropertyValue> loopList = localItemPropertySortObl().get(inputPickorderLineProperty.getPropertyCodeStr());
            for (cPickorderLinePropertyValue pickorderLinePropertyValue : loopList ) {
                quantityDbl += pickorderLinePropertyValue.getQuantityDbl();
            }
            if (quantityDbl != this.getQuantityHandledDbl()){
                this.itemPropertyTabLayout.selectTab(this.itemPropertyTabLayout.getTabAt(this.titleObl.indexOf(inputPickorderLineProperty.getPropertyCodeStr())));
                cUserInterface.pDoNope(itemPropertyTabLayout, true, true);
                cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, cAppExtension.activity.getString(R.string.message_fill_all_properties),R.raw.headsupsound,false);
                return false;
            }
        }
        return true;
    }

    private void mSetTabLayoutListener(){
        this.itemPropertyTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() > 0){
                    if (getQuantityAvailable() > 0) {
                        mShowUnderPickPropertyDialog(cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_accept));
                    } else { amountHandledBln = true; }
                }
                itemPropertyViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
    }

    private void mScrollToBottom() {
    }

    private void mSetArticleInfo(){
        this.articleDescriptionCompactText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.articleDescription2CompactText.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        this.articleItemCompactText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
        this.articleBarcodeCompactText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());

        if (!cPickorderLine.currentPickOrderLine.hasPropertysBln() || cPickorderLine.currentPickOrderLine.pickorderLinePropertyNoInputObl() == null || cPickorderLine.currentPickOrderLine.pickorderLinePropertyNoInputObl().size() == 0) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
        }
        else {
            this.imageButtonNoInputPropertys.setVisibility(View.VISIBLE);
        }
    }

    private  void mSetQuantityText() {
        String quantityStr =   (int)this.getQuantityHandledDbl() + "/" +  cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl());
        this.quantityCompactText.setText(quantityStr);
    }

    private  void mShowHideOKButton() {

        if (this.getQuantityHandledDbl() == 0) {
            this.imageButtonDone.setVisibility(View.INVISIBLE);
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

    private  void mShowUnderPickPropertyDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectPropertyFragment acceptRejectPropertyFragment = new AcceptRejectPropertyFragment(cAppExtension.activity.getString(R.string.message_underpick_header),
                cAppExtension.activity.getString(R.string.message_underpick_property_text,
                        cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl()),
                        cText.pDoubleToStringStr(this.getQuantityHandledDbl())),
                pvRejectStr,
                pvAcceptStr );
        acceptRejectPropertyFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectPropertyFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    public   void pTryToChangePickedQuantity(Boolean pvIsPositiveBln,  Boolean pvAmountFixedBln, double pvAmountDbl) {

        if (this.amountHandledBln){
            return;
        }
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
                cPickorderLinePropertyValue.currentPickorderLinePropertyValue.quantityDbl = newQuantityDbl;
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

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cPickorderLinePropertyValue.currentPickorderLinePropertyValue.getQuantityDbl());

        double availableDbl  = cPickorderLine.currentPickOrderLine.getQuantityDbl();
        ArrayList<cPickorderLinePropertyValue> loopList = localItemPropertySortObl().get(cPickorderLinePropertyValue.currentPickorderLinePropertyValue.getPropertyCodeStr());

        for (cPickorderLinePropertyValue pickorderLinePropertyValue : loopList ) {
            availableDbl -= pickorderLinePropertyValue.getQuantityDbl();
        }

        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, availableDbl);
        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);
        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private void mShowTextInputFragment() {
        if (cPickorderLinePropertyValue.currentPickorderLinePropertyValue == null){
            return;
        }
        cUserInterface.pCheckAndCloseOpenDialogs();
        ItemPropertyTextInputFragment itemPropertyTextInputFragment = new ItemPropertyTextInputFragment(cPickorderLinePropertyValue.currentPickorderLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase());
        itemPropertyTextInputFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTTEXTFRAGMENT_TAG);
    }


    private  void mGoBackToLinesActivity() {

        //Reset current branch
        if (cPickorder.currentPickOrder.destionationBranch() == null) {
            cPickorder.currentPickOrder.scannedBranch =  null;
        }


        Intent intent = new Intent(cAppExtension.context, PickorderLinesActivity.class);
        PickorderLinesActivity.startedViaOrderSelectBln = false;

        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private final BroadcastReceiver mNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int numberChosenInt = 0;
            Bundle extras = intent.getExtras();

            if (extras != null) {
                numberChosenInt = extras.getInt(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER);
            }
            mHandleQuantityChosen(numberChosenInt);
        }
    };

    private void mHandleQuantityChosen(double pvQuantityDbl) {
        this.pTryToChangePickedQuantity(pvQuantityDbl != 0, true,pvQuantityDbl);
        this.pRefreshActivity();
    }


    private void mSetNoInputPropertyListener() {
        this.imageButtonNoInputPropertys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowItemPropertyNoInputFragment();
            }
        });
    }

    private  void mShowItemPropertyNoInputFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ItemPropertyNoInputFragment itemPropertyNoInputFragment = new ItemPropertyNoInputFragment();
        itemPropertyNoInputFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.ITEMPROPERTYVALUENOINPUTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private void mHandleCompositeData(){

        if (cPickorderCompositeBarcode.currentCompositePickorderBarcode == null || cPickorderCompositeBarcode.currentCompositePickorderBarcode.KeysAndValuesObl == null || cPickorderCompositeBarcode.currentCompositePickorderBarcode.KeysAndValuesObl.size() == 0 ) {
            return;
        }

        for (cPickorderLineProperty pickorderLineProperty : cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl() ) {
            String barcodeStr = cPickorderCompositeBarcode.currentCompositePickorderBarcode.KeysAndValuesObl.get(pickorderLineProperty.getPropertyCodeStr());

            if (barcodeStr != null && !barcodeStr.isEmpty()) {
                this.pHandleScan(cBarcodeScan.pFakeScan(barcodeStr));
            }
        }

        cPickorderCompositeBarcode.currentCompositePickorderBarcode = null;


    }

    private void mBuildAndFillTabs() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        this.titleObl = new ArrayList<>();
        for (Map.Entry<String, ArrayList<cPickorderLinePropertyValue>> itemPropertyEntry :  this.localItemPropertySortObl().entrySet()) {


            this.itemPropertyTabLayout.addTab(this.itemPropertyTabLayout.newTab());

            DynamicItemPropertyFragment dynamicItemPropertyFragment = new DynamicItemPropertyFragment(itemPropertyEntry.getValue());
            fragments.add(dynamicItemPropertyFragment);

            this.titleObl.add(itemPropertyEntry.getKey());
        }

        ItemPropertyPagerAdapter itemPropertyPagerAdapter = new ItemPropertyPagerAdapter(fragments, this.titleObl);
        this.itemPropertyViewpager.setAdapter(itemPropertyPagerAdapter);
        this.itemPropertyTabLayout.setupWithViewPager(itemPropertyViewpager);
    }


    //End Region Private Methods
}

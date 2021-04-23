package nl.icsvertex.scansuite.Activities.Pick;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.Objects;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderCompositeBarcode.cPickorderCompositeBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
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
    private boolean deletedFromRecyclerBln;
    private int tabIndexInt;

    private  List<cLinePropertyValue> localItemPropertyValueObl (){

        List<cLinePropertyValue> resultObl = new ArrayList<>();
        List<cLineProperty> hulpObl = new ArrayList<>();

        if (cPickorderLine.currentPickOrderLine.linePropertyValuesObl() != null && cPickorderLine.currentPickOrderLine.linePropertyValuesObl() .size() > 0 ) {
            resultObl.addAll(cPickorderLine.currentPickOrderLine.linePropertyValuesObl());
            for(cLinePropertyValue linePropertyValue : resultObl){
                if (!hulpObl.contains(linePropertyValue.getLineProperty())){
                    hulpObl.add(linePropertyValue.getLineProperty());
                }
            }
        }

        for (cLineProperty inputLineProperty : cPickorderLine.currentPickOrderLine.linePropertyInputObl()) {
            if (hulpObl.contains(inputLineProperty)){
                continue;
            }
            resultObl.add(new cLinePropertyValue(inputLineProperty));
        }
        return resultObl;
    }

    private LinkedHashMap<String, ArrayList<cLinePropertyValue>> localItemPropertySortObl(){
        LinkedHashMap<String, ArrayList<cLinePropertyValue>> linkedHashMap = new LinkedHashMap<>();
       // ArrayList<cPickorderLinePropertyValue> pickorderLinePropertyValues = new ArrayList<>();

        for (cLinePropertyValue linePropertyValue : localItemPropertyValueObl()) {
                //Create the hasmap dynammically and fill it
            ArrayList<cLinePropertyValue> loopList = linkedHashMap.get(linePropertyValue.getPropertyCodeStr());
            if (loopList == null) {
                ArrayList<cLinePropertyValue> propertyValues = new ArrayList<>();
                propertyValues.add(linePropertyValue);
                linkedHashMap.put(linePropertyValue.getPropertyCodeStr(), propertyValues);
            }
            else{
                loopList.add(linePropertyValue);
            }

        }
        return linkedHashMap;
    }

    public double getQuantityHandledDbl(){

        double quantityDbl = 0;

        if (this.titleObl == null || this.localItemPropertySortObl().size() == 0) {
            return quantityDbl;
        }

        ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(this.titleObl.get(0));

        for (cLinePropertyValue pickorderLinePropertyValue : loopList ) {
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
    protected void onDestroy() {
        super.onDestroy();
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

        if (cPickorderLine.currentPickOrderLine.linePropertyInputObl().size() == 1) {
            cLineProperty.currentLineProperty = cPickorderLine.currentPickOrderLine.linePropertyInputObl().get(0);

        } else{
            cLineProperty.currentLineProperty = cPickorderLine.currentPickOrderLine.getLineProperty(Objects.requireNonNull(Objects.requireNonNull(this.itemPropertyTabLayout.getTabAt(this.itemPropertyTabLayout.getSelectedTabPosition())).getText()).toString());
        }

        this.pHandeManualAction(pvBarcodeScan);
    }

    public  void pCancelPick() {
        //Check if we need to remove the SalesorderPackingTableLines
        if (cPickorderLine.currentPickOrderLine.pGetLinesForProcessingSequenceObl().size() <= 1)  {
            cSalesOrderPackingTable.pDeleteFromDatabaseBln(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
        }
        cPickorderLine.currentPickOrderLine.pUpdateProcessingSequenceBln("");
        cPickorderLine.currentPickOrderLine.pCancelIndatabase();
        cPickorderLine.currentPickOrderLine = null;
        this.mGoBackToLinesActivity();
    }

    public  void pHandeManualAction(cBarcodeScan pvBarcodeScan){
        if (cLineProperty.currentLineProperty.getItemProperty()== null ) {
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

        if (!cRegex.pCheckRegexBln( cLineProperty.currentLineProperty.getItemProperty().getLayoutStr(),pvBarcodeScan.getBarcodeOriginalStr())) {
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
        cResult hulpRst = cLineProperty.currentLineProperty.pCheckScanForUniquePropertyRst(pvBarcodeScan.getBarcodeOriginalStr());
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true, true);
            return;
        }

        cLineProperty.currentLineProperty.pValueAdded(pvBarcodeScan.getBarcodeOriginalStr());

        if (this.amountHandledBln && !cLinePropertyValue.currentLinePropertyValue.getItemProperty().getUniqueBln()){
            cLinePropertyValue.currentLinePropertyValue.quantityDbl = cPickorderLine.currentPickOrderLine.getQuantityDbl();
        }

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, pvBarcodeScan.getBarcodeOriginalStr() + " "  + cAppExtension.activity.getString(R.string.addedorhandled),R.raw.headsupsound,false);
        this.pTryToChangePickedQuantity();
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
        this.mGetNextPickLineForCurrentBin();
    }

    public void pRefreshActivity(){
       // this.mSetItemPropertyValueRecycler();
        this.mSetQuantityText();
        this.mShowHideOKButton();
        this.mBuildAndFillTabs();
        this.mSelectTabAndItem();
    }

    private void mSelectTabAndItem(){
        if(cLinePropertyValue.currentLinePropertyValue ==null){
            if(this.deletedFromRecyclerBln){
                this.itemPropertyTabLayout.selectTab(this.itemPropertyTabLayout.getTabAt(this.tabIndexInt));
            }
            this.deletedFromRecyclerBln = false;
            return;
        }
        this.itemPropertyTabLayout.selectTab(this.itemPropertyTabLayout.getTabAt(this.titleObl.indexOf(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr())));
    }

    public void pDeleteValueFromRecyler() {
        this.deletedFromRecyclerBln = true;
        this.tabIndexInt = this.itemPropertyTabLayout.getSelectedTabPosition();
        cLinePropertyValue.allLinePropertysValuesObl.remove(cLinePropertyValue.currentLinePropertyValue);
        cLinePropertyValue.currentLinePropertyValue = null;
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
        DatePickerFragment datePickerFragment = new DatePickerFragment(cPickorderLine.currentPickOrderLine.presetValueObl);
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

        for (cLineProperty inputPickorderLineProperty : cPickorderLine.currentPickOrderLine.linePropertyInputObl()){
            if (!inputPickorderLineProperty.getIsRequiredBln()){
                continue;
            }
            double quantityDbl = 0.0;
            ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(inputPickorderLineProperty.getPropertyCodeStr());
            for (cLinePropertyValue linePropertyValue : loopList ) {
                quantityDbl += linePropertyValue.getQuantityDbl();
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

        if (!cPickorderLine.currentPickOrderLine.hasPropertysBln() || cPickorderLine.currentPickOrderLine.linePropertyNoInputObl() == null || cPickorderLine.currentPickOrderLine.linePropertyNoInputObl().size() == 0) {
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

    public   void pTryToChangePickedQuantity() {

        if (this.amountHandledBln){
            return;
        }
        double newQuantityDbl;

        newQuantityDbl = cLinePropertyValue.currentLinePropertyValue.getQuantityDbl();

        if (newQuantityDbl <= 0) {
            newQuantityDbl = 0;
        }

        //Set the new quantityDbl and show in Activity
        cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;

        //Add or update line barcodeStr
        cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcode(newQuantityDbl);

        //Update orderline info (quantityDbl, timestamp, localStatusInt)
        cPickorderLine.currentPickOrderLine.pHandledIndatabase();
        return;

    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cLinePropertyValue.currentLinePropertyValue.getQuantityDbl());

        double availableDbl  = cPickorderLine.currentPickOrderLine.getQuantityDbl();
        ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr());

        for (cLinePropertyValue linePropertyValue : loopList ) {
            availableDbl -= linePropertyValue.getQuantityDbl();
        }

        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, availableDbl);
        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);
        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private void mShowTextInputFragment() {
        if (cLinePropertyValue.currentLinePropertyValue == null){
            return;
        }
        cUserInterface.pCheckAndCloseOpenDialogs();
        ItemPropertyTextInputFragment itemPropertyTextInputFragment = new ItemPropertyTextInputFragment(cLinePropertyValue.currentLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase());
        itemPropertyTextInputFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTTEXTFRAGMENT_TAG);
    }


    private  void mGoBackToLinesActivity() {

        //Reset current branch
        if (cPickorder.currentPickOrder.destionationBranch() == null) {
            cPickorder.currentPickOrder.scannedBranch =  null;
        }
        cPickorderLine.currentPickOrderLine = null;

        Intent intent = new Intent(cAppExtension.context, PickorderLinesActivity.class);
        PickorderLinesActivity.startedViaOrderSelectBln = false;

        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
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

        for (cLineProperty lineProperty : cPickorderLine.currentPickOrderLine.linePropertyInputObl() ) {
            String barcodeStr = cPickorderCompositeBarcode.currentCompositePickorderBarcode.KeysAndValuesObl.get(lineProperty.getPropertyCodeStr());

            if (barcodeStr != null && !barcodeStr.isEmpty()) {
                this.pHandleScan(cBarcodeScan.pFakeScan(barcodeStr));
            }
        }

        cPickorderCompositeBarcode.currentCompositePickorderBarcode = null;


    }

    private  void mGetNextPickLineForCurrentBin() {

        cResult hulpResult;

        if (!cPickorder.currentPickOrder.isPickAutoNextBln()) {
            cPickorderLine.currentPickOrderLine = null;
            this.mGoBackToLinesActivity();
            return;
        }

        //check if there is a next line for this BIN
        cPickorderLine nextLine = cPickorder.currentPickOrder.pGetNextLineToHandleForBin(cPickorderLine.currentPickOrderLine.getBinCodeStr());

        //There is no next line, so close this activity
        if (nextLine == null) {
            //Clear current barcodeStr and reset defaults
            cPickorderLine.currentPickOrderLine = null;
            this.mGoBackToLinesActivity();
            return;
        }

        //Set the current line, and update it to busy

        cPickorderLine.currentPickOrderLine = nextLine;

        hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
        if (!hulpResult.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            cPickorderLine.currentPickOrderLine = null;
            this.mGoBackToLinesActivity();
            return;
        }

        this.mInitnewLineForBin();
    }

    private void mInitnewLineForBin() {

        //Play a sound
        cUserInterface.pPlaySound(R.raw.message, null);
        cPickorderBarcode.currentPickorderBarcode = null;

        Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    private void mBuildAndFillTabs() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        this.titleObl = new ArrayList<>();
        for (Map.Entry<String, ArrayList<cLinePropertyValue>> itemPropertyEntry :  this.localItemPropertySortObl().entrySet()) {


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

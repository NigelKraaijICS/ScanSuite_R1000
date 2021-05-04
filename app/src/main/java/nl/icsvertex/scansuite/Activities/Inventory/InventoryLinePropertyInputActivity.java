package nl.icsvertex.scansuite.Activities.Inventory;

import android.content.Intent;
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
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DatePickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DynamicItemPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyNoInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyTextInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.PagerAdapters.ItemPropertyPagerAdapter;
import nl.icsvertex.scansuite.R;

public class InventoryLinePropertyInputActivity extends AppCompatActivity implements iICSDefaultActivity {

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

        if (cInventoryorderLine.currentInventoryOrderLine.linePropertyValuesObl() != null && cInventoryorderLine.currentInventoryOrderLine.linePropertyValuesObl() .size() > 0 ) {
            resultObl.addAll(cInventoryorderLine.currentInventoryOrderLine.linePropertyValuesObl());
            for(cLinePropertyValue linePropertyValue : resultObl){
                if (!hulpObl.contains(linePropertyValue.getLineProperty())){
                    hulpObl.add(linePropertyValue.getLineProperty());
                }
            }
        }

        for (cLineProperty inputLineProperty : cInventoryorderLine.currentInventoryOrderLine.linePropertyInputObl()) {
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
        return    cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl() - this.getQuantityHandledDbl();
    }


    //End Region Private Properties


    //Region Constructor
    public  InventoryLinePropertyInputActivity() {

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
            if (cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl() == 0 ) {
               this.mStartInventoryBINActivity();
                return true;
            }
            amountHandledBln = true;
            if (mCheckAllPropertysHandledBln()){

                this.mShowAcceptFragment();
                return true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl() == 0){
            this.mStartInventoryBINActivity();
            return;
        }
        amountHandledBln = true;
        if (mCheckAllPropertysHandledBln()){

            this.mShowAcceptFragment();
        }
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

    }

    //End Regioni ICSDefaultFragment methods

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        if (cInventoryorderLine.currentInventoryOrderLine.linePropertyInputObl().size() == 1) {
            cLineProperty.currentLineProperty = cInventoryorderLine.currentInventoryOrderLine.linePropertyInputObl().get(0);

        } else{
            cLineProperty.currentLineProperty = cInventoryorderLine.currentInventoryOrderLine.getLineProperty(Objects.requireNonNull(Objects.requireNonNull(this.itemPropertyTabLayout.getTabAt(this.itemPropertyTabLayout.getSelectedTabPosition())).getText()).toString());
        }

        this.pHandeManualAction(pvBarcodeScan);
    }

    public  void pCancel() {
        //Check if we need to remove the SalesorderPackingTableLines

        cInventoryorderLine.currentInventoryOrderLine.pResetRst();
        cInventoryorderLine.currentInventoryOrderLine = null;
        this.mStartInventoryBINActivity();
    }

    public  void pHandeManualAction(cBarcodeScan pvBarcodeScan){
        if (cLineProperty.currentLineProperty.getItemProperty()== null ) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_property_unknown),"",true, true);
            return;
        }

        if (!cRegex.pCheckRegexBln( cLineProperty.currentLineProperty.getItemProperty().getLayoutStr(),pvBarcodeScan.getBarcodeOriginalStr())) {

            //Check for article scan if so handle article
            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
                mHandleArticleScan(pvBarcodeScan);
                return;
            }

            cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout,cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),R.raw.badsound, true);
            return;
        }

//        if (!this.amountHandledBln){
//            if (this.getQuantityHandledDbl() == cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl()) {
//                cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout,cAppExtension.activity.getString(R.string.message_extra_pieces_not_allowed),R.raw.badsound, true);
//                return;
//            }
//        }
        //Check unique values if needed
        cResult hulpRst = cLineProperty.currentLineProperty.pCheckScanForUniquePropertyRst(pvBarcodeScan.getBarcodeOriginalStr());
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true, true);
            return;
        }

        cLineProperty.currentLineProperty.pValueAdded(pvBarcodeScan.getBarcodeOriginalStr());

        if (this.amountHandledBln && !cLinePropertyValue.currentLinePropertyValue.getItemProperty().getUniqueBln()){
            cLinePropertyValue.currentLinePropertyValue.quantityDbl = cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl();
        }

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, pvBarcodeScan.getBarcodeOriginalStr() + " "  + cAppExtension.activity.getString(R.string.addedorhandled),R.raw.headsupsound,false);
        this.pTryToChangeQuantity();
        this.pRefreshActivity();

    }

    public void pHandled() {

        if (!mCheckAllPropertysHandledBln()){
            return;
        }
        this.mHandleDone();
    }

    public void pRefreshActivity(){
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
        DatePickerFragment datePickerFragment = new DatePickerFragment(cInventoryorderLine.currentInventoryOrderLine.presetValueObl);
        datePickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTDATEFRAGMENT_TAG);
    }


    //End Region Public Methods

    //Region Private Methods

    private void mHandleArticleScan(cBarcodeScan pvBarcodeScan){
        amountHandledBln = true;
        if (!mCheckAllPropertysHandledBln()){
            return;
        }

        //Keep the scan, so BIN activity can handle it
        InventoryorderBinActivity.barcodeScanToHandle = pvBarcodeScan;

        this.mHandleDone();
    }

    private void mSetCloseListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        for (cLineProperty inputPickorderLineProperty : cInventoryorderLine.currentInventoryOrderLine.linePropertyInputObl()){
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
                amountHandledBln = tab.getPosition() > 0;
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
        this.articleDescriptionCompactText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
        this.articleDescription2CompactText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());
        this.articleItemCompactText.setText(cInventoryorderLine.currentInventoryOrderLine.getItemNoAndVariantCodeStr());
        this.articleBarcodeCompactText.setText(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeAndQuantityStr());

        if (!cInventoryorderLine.currentInventoryOrderLine.hasPropertysBln() || cInventoryorderLine.currentInventoryOrderLine.linePropertyNoInputObl() == null || cInventoryorderLine.currentInventoryOrderLine.linePropertyNoInputObl().size() == 0) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
        }
        else {
            this.imageButtonNoInputPropertys.setVisibility(View.VISIBLE);
        }
    }

    private  void mSetQuantityText() {
        String quantityStr ;
        if (cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl() > 0) {
            quantityStr =   (int)this.getQuantityHandledDbl() + "/" +  cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl());
        }else {
            quantityStr =   cText.pIntToStringStr( (int)this.getQuantityHandledDbl());
        }

        this.quantityCompactText.setText(quantityStr);
    }

    private  void mShowHideOKButton() {

        if (this.getQuantityHandledDbl() == 0) {
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonDone.setVisibility(View.VISIBLE);

    }

    public   void pTryToChangeQuantity() {

        if (this.amountHandledBln){
            return;
        }
        double newQuantityDbl;

        newQuantityDbl = this.getQuantityHandledDbl();

        if (newQuantityDbl <= 0) {
            newQuantityDbl = 0;
        }

        //Set the new quantityDbl and show in Activity

        cInventoryorderLineBarcode.pDeleteAllOtherLinesForBarcode(cInventoryorderLine.currentInventoryOrderLine.getLineNoInt(),
                                                                  cInventoryorderLineBarcode.currentInventoryorderLineBarcode.getBarcodeStr() );

        cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl = newQuantityDbl;
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl =  newQuantityDbl;

    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cLinePropertyValue.currentLinePropertyValue.getQuantityDbl());
        double availableDbl = 0.0;
        if (cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl() != 0){
            availableDbl  = cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl();
            ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr());

            for (cLinePropertyValue linePropertyValue : loopList ) {
                availableDbl -= linePropertyValue.getQuantityDbl();
            }
        }   else {
            availableDbl = 999;
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


    private  void mStartInventoryBINActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        this.mResetCurrents();
        InventoryorderBinActivity.busyBln = false;

        final Intent intent = new Intent(cAppExtension.context, InventoryorderBinActivity.class);
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

    private  void mHandleDone() {

        //Try to save the line to the database
        if (!cInventoryorderLine.currentInventoryOrderLine.pSaveLineViaWebserviceBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);
            return;
        }

        //Change quantityDbl handled in database
        cInventoryorderLine.currentInventoryOrderLine.pUpdateQuantityInDatabase();

        cUserInterface.pHideGettingData();

        this.mStartInventoryBINActivity();
    }

    private void mResetCurrents(){
        cInventoryorderLine.currentInventoryOrderLine = null;
        cInventoryorderBarcode.currentInventoryOrderBarcode = null;
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode = null;
    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        String acceptStr = cAppExtension.activity.getString(R.string.message_accept_line);
        String rejectStr = cAppExtension.activity.getString(R.string.message_cancel_line);

        if (BuildConfig.FLAVOR.toUpperCase().equalsIgnoreCase("BMN")) {
            acceptStr =  cAppExtension.activity.getString(R.string.message_yes);
            rejectStr = cAppExtension.activity.getString(R.string.message_no);
        }

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderlinebusy_header),
                cAppExtension.activity.getString(R.string.message_orderlinebusy_text),
                rejectStr, acceptStr, false);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    //End Region Private Methods
}

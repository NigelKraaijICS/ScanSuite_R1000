package nl.icsvertex.scansuite.Activities.Move;

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
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.Moveorders.cMoveorder;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DatePickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DynamicItemPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyNoInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyStockFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyTextInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SetBinFragment;
import nl.icsvertex.scansuite.PagerAdapters.ItemPropertyPagerAdapter;
import nl.icsvertex.scansuite.R;

public class MoveLineItemPropertyActivity  extends AppCompatActivity implements iICSDefaultActivity {

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
    public boolean takeConfirmedBln;
    private boolean deletedFromRecyclerBln;
    private int tabIndexInt;

    private ArrayList<cMoveorderBarcode> barcodeObl;
    public static modusEnu currentModus;

    public enum modusEnu{
        GENERATEDTAKE,
        GENERATEDPLACE,
        MTTAKE,
        MTPLACE,
        NULL
    }

    private  List<cLinePropertyValue> localItemPropertyValueObl (){

        List<cLinePropertyValue> resultObl = new ArrayList<>();
        List<cLineProperty> hulpObl = new ArrayList<>();

        if (currentModus == modusEnu.GENERATEDPLACE){
            resultObl.addAll(cMoveorder.currentMoveOrder.currentMoveItemVariant.linePropertyValueObl);
            return resultObl;
        }

        if (currentModus == modusEnu.GENERATEDTAKE){
            resultObl.addAll(cMoveorderLine.currentMoveOrderLine.generatedValueObl);
            return resultObl;
        }

        if (cMoveorderLine.currentMoveOrderLine.linePropertyValuesObl() != null && cMoveorderLine.currentMoveOrderLine.linePropertyValuesObl() .size() > 0 ) {
            resultObl.addAll(cMoveorderLine.currentMoveOrderLine.linePropertyValuesObl());
            for(cLinePropertyValue linePropertyValue : resultObl){
                if (!hulpObl.contains(linePropertyValue.getLineProperty())){
                    hulpObl.add(linePropertyValue.getLineProperty());
                }

            }
        }

        for (cLineProperty inputLineProperty : cMoveorderLine.currentMoveOrderLine.linePropertyInputObl()) {
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

        for (cLinePropertyValue linePropertyValue : loopList ) {
            quantityDbl += linePropertyValue.getQuantityDbl();
        }

        return  quantityDbl;

    }

    public  double getQuantityAvailable() {
        return    cMoveorderLine.currentMoveOrderLine.getQuantityDbl() - this.getQuantityHandledDbl();
    }


    //End Region Private Properties


    //Region Constructor
    public  MoveLineItemPropertyActivity() {

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
            if (cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl() == 0 ) {
                this.mGoBackToLinesActivity();
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
        if (cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl() == 0){
            this.mGoBackToLinesActivity();
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
        this.mSetArticleInfo();
        this.mSetQuantityText();
        this.mBuildAndFillTabs();
        this.mShowHideOKButton();
        this.pRefreshActivity();
        this.mShowSetBinFragment();
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

        this.pHandeManualAction(pvBarcodeScan);
    }

    public  void pCancel() {
        //Check if we need to remove the SalesorderPackingTableLines

        cMoveorderLine.currentMoveOrderLine.pResetRst();
        cMoveorderLine.currentMoveOrderLine = null;
        this.mGoBackToLinesActivity();
    }

    public  void pHandeManualAction(cBarcodeScan pvBarcodeScan){

        cLineProperty.currentLineProperty = cLinePropertyValue.currentLinePropertyValue.getLineProperty();

        boolean foundBln = false;
        if(  cMoveorderLine.currentMoveOrderLine.presetValueObl!= null){

            ArrayList<String> propertyObl = new ArrayList<>();
            for (cLinePropertyValue propertyValue : cMoveorderLine.currentMoveOrderLine.presetValueObl){
                if(propertyValue.getPropertyCodeStr().equalsIgnoreCase(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr())){
                    propertyObl.add(propertyValue.getValueStr());}
            }
            if (propertyObl.size() > 0){
                for (String string: propertyObl){
                    if (string.equalsIgnoreCase(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()))){
                        foundBln = true;
                        break;
                    }
                }
                if(!foundBln){
                    cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_property_not_allowed),"",true, true);
                    return;
                }
            }
        }

        if (currentModus == modusEnu.GENERATEDTAKE){
            //Check for new property and get Stock
            foundBln = false;
            for (cLinePropertyValue linePropertyValue: cMoveorderLine.currentMoveOrderLine.generatedValueObl){
                if (linePropertyValue.getPropertyCodeStr().equalsIgnoreCase(cLineProperty.currentLineProperty.getPropertyCodeStr()) && linePropertyValue.getValueStr().equalsIgnoreCase(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()))) {
                    foundBln = true;
                    break;
                }
            }
            if (!foundBln){
                //New value get stock
                this.mGetStockWithProperty(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
                return;
            }
        }

        if (cLineProperty.currentLineProperty.getItemProperty()== null ) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_property_unknown),"",true, true);
            return;
        }

        if (!this.amountHandledBln){
            if (this.getQuantityHandledDbl() == cMoveorderLine.currentMoveOrderLine.getQuantityDbl()) {
                cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout,cAppExtension.activity.getString(R.string.message_extra_pieces_not_allowed),R.raw.badsound, true);
                return;
            }
        }
        //Check unique values if needed
        cResult hulpRst = cLineProperty.currentLineProperty.pCheckScanForUniquePropertyRst(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true, true);
            return;
        }

        cLineProperty.currentLineProperty.pValueAdded(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

        if (currentModus == modusEnu.MTPLACE || currentModus == modusEnu.MTTAKE){
            if (this.amountHandledBln && !cLinePropertyValue.currentLinePropertyValue.getItemProperty().getUniqueBln()){
                cLinePropertyValue.currentLinePropertyValue.quantityDbl = getQuantityHandledDbl();
            }
        }
        if (currentModus == modusEnu.GENERATEDPLACE){

            for (cLinePropertyValue linePropertyValue : cMoveorder.currentMoveOrder.currentMoveItemVariant.linePropertyValueObl){
                if (linePropertyValue.getPropertyCodeStr().equalsIgnoreCase(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr()) && linePropertyValue.getValueStr().equalsIgnoreCase(cLinePropertyValue.currentLinePropertyValue.getValueStr())){
                    continue;
                }
                cMoveorder.currentMoveOrder.currentMoveItemVariant.linePropertyValueObl.add(cLinePropertyValue.currentLinePropertyValue);
                break;
            }

        }

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()) + " "  + cAppExtension.activity.getString(R.string.addedorhandled),R.raw.headsupsound,false);
        this.pTryToChangeQuantity(true, false, cLinePropertyValue.quantityPerUnitOfMeasureDbl);
        this.pRefreshActivity();

    }

    public void pHandled() {

        if (!mCheckAllPropertysHandledBln()){
            return;
        }

        if (currentModus ==  modusEnu.MTTAKE && !takeConfirmedBln){
            //Check if we picked less then asked, if so then show dialog
            if (this.getQuantityHandledDbl() != (cMoveorderLine.currentMoveOrderLine.getQuantityDbl()) ) {
                this.mShowUnderMoveDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
                return;
            }
        }

        if (currentModus == modusEnu.MTPLACE){
            if (this.getQuantityHandledDbl() != (cMoveorderLine.currentMoveOrderLine.getQuantityDbl()) ) {
                String messageStr =   cAppExtension.activity.getString(R.string.message_underplace_text,
                        cText.pDoubleToStringStr(this.getQuantityHandledDbl()),
                        cText.pDoubleToStringStr(cMoveorderLine.currentMoveOrderLine.getQuantityDbl()),
                        cText.pDoubleToStringStr(0.0));

                cUserInterface.pDoExplodingScreen(messageStr,"",true,true);
                return;
            }
        }

        this.mHandleDone();
    }

    public void pRefreshActivity(){
        cAppExtension.activity.runOnUiThread(() ->{
            this.mSetQuantityText();
            this.mShowHideOKButton();
            this.mBuildAndFillTabs();
            this.mSelectTabAndItem();
        });
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

        if (currentModus == modusEnu.GENERATEDTAKE){
            cMoveorderLine.currentMoveOrderLine.generatedValueObl.remove(cLinePropertyValue.currentLinePropertyValue);
        }
        if (currentModus == modusEnu.GENERATEDPLACE){
            cMoveorder.currentMoveOrder.currentMoveItemVariant.linePropertyValueObl.remove(cLinePropertyValue.currentLinePropertyValue);
        }
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
        DatePickerFragment datePickerFragment = new DatePickerFragment(cMoveorderLine.currentMoveOrderLine.presetValueObl);
        datePickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTDATEFRAGMENT_TAG);
    }


    //End Region Public Methods

    //Region Private Methods

    private void mGetStockWithProperty(String pvValueStr){
        ItemPropertyStockFragment itemPropertyStockFragment = new ItemPropertyStockFragment(cMoveorder.currentMoveOrder.currentArticle, cMoveorder.currentMoveOrder.getOrderTypeStr(), pvValueStr);
        itemPropertyStockFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYSTOCKFRAGMENT_TAG);
    }

    private void mShowSetBinFragment(){
        if (cMoveorder.currentMoveOrder.currentBranchBin !=null){
            return;
        }
        SetBinFragment setBinFragment = new SetBinFragment();
        setBinFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SETBIN_TAG);
    }


    private void mSetCloseListener() {
        this.imageButtonDone.setOnClickListener(view -> {

            amountHandledBln = true;
            if (!mCheckAllPropertysHandledBln()){
                return;
            }
            pHandled();
        });
    }

    private boolean mCheckAllPropertysHandledBln(){

        if (!this.amountHandledBln){
            return false;
        }

        for (cLineProperty lineProperty : cMoveorderLine.currentMoveOrderLine.linePropertyInputObl()){
            if (!lineProperty.getIsRequiredBln()){
                continue;
            }
            double quantityDbl = 0.0;
            ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(lineProperty.getPropertyCodeStr());
            for (cLinePropertyValue linePropertyValue : loopList ) {
                quantityDbl += linePropertyValue.getQuantityDbl();
            }
            if (quantityDbl != this.getQuantityHandledDbl()){
                this.itemPropertyTabLayout.selectTab(this.itemPropertyTabLayout.getTabAt(this.titleObl.indexOf(lineProperty.getPropertyCodeStr())));
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

    private void mShowExtraPiecesNotAllowed(){

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, cAppExtension.context.getString(R.string.number_cannot_be_higher_than_stock), null, false);
        cUserInterface.pDoNope(this.articleItemCompactText, true, true);
        cUserInterface.pDoNope(this.quantityCompactText, false, false);
    }

    private void mSetArticleInfo(){

        cLinePropertyValue.quantityPerUnitOfMeasureDbl = cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityPerUnitOfMeasureDbl();

        this.barcodeObl = new ArrayList<>();
        if (currentModus == modusEnu.GENERATEDTAKE){
            this.barcodeObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);
        }
        this.articleDescriptionCompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
        this.articleDescription2CompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        this.articleItemCompactText.setText(cMoveorderLine.currentMoveOrderLine.getItemNoAndVariantCodeStr());
        this.articleBarcodeCompactText.setText(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeAndQuantityStr());

        if (!cMoveorderLine.currentMoveOrderLine.hasPropertysBln() || cMoveorderLine.currentMoveOrderLine.linePropertyNoInputObl() == null || cMoveorderLine.currentMoveOrderLine.linePropertyNoInputObl().size() == 0) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
        }
        else {
            this.imageButtonNoInputPropertys.setVisibility(View.VISIBLE);
        }
        if (currentModus == modusEnu.GENERATEDPLACE){
            if (cMoveorderLine.currentMoveOrderLine.generatedPlaceLineValueObl == null){
                cMoveorderLine.currentMoveOrderLine.generatedPlaceLineValueObl = new ArrayList<>();
            }
        }
    }

    private  void mSetQuantityText() {
        String quantityStr ;
        if (cMoveorderLine.currentMoveOrderLine.getQuantityDbl() > 0) {
            quantityStr =   (int)this.getQuantityHandledDbl() + "/" +  cText.pDoubleToStringStr(cMoveorderLine.currentMoveOrderLine.getQuantityDbl());
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

    public   void pTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        if (this.amountHandledBln){
            return;
        }
        if (this.barcodeObl == null){
            this.barcodeObl = new ArrayList<>();
        }
        double newQuantityDbl;

        this.mUpdateOtherProperties();
        if (pvIsPositiveBln) {
            double countDbl = 0;
            //Determine the new amount
            if (pvAmountFixedBln) {
                newQuantityDbl = pvAmountDbl;

                //Check if we would exceed amount stock available, then show message
                if (newQuantityDbl > cMoveorderLine.currentMoveOrderLine.getQuantityDbl()) {
                    this.mShowExtraPiecesNotAllowed();
                    return;
                }

                //Clear the barcodeStr list and refill it
                this.barcodeObl.clear();

                do{
                    countDbl += cLinePropertyValue.quantityPerUnitOfMeasureDbl;
                    //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                    this.barcodeObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);
                }
                while(countDbl < newQuantityDbl);
                return;
            } else {
                newQuantityDbl = cMoveorderLine.currentMoveOrderLine.quantityHandledDbl + pvAmountDbl;
            }

            //Check if we would exceed amount stock available, then show message
            if ( newQuantityDbl > cMoveorderLine.currentMoveOrderLine.getQuantityDbl()) {
                this.mShowExtraPiecesNotAllowed();
                return;
            }

            //Set the new quantityDbl and show in Activity
            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = newQuantityDbl;
            //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
            do{
                countDbl += cLinePropertyValue.quantityPerUnitOfMeasureDbl;
                //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                this.barcodeObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);
            }
            while(countDbl < pvAmountDbl);
            return;
        }

        //negative
        //Remove the last barcodeStr in the list
        this.barcodeObl.remove( this.barcodeObl.size()-1);
        newQuantityDbl= cMoveorderLine.currentMoveOrderLine.quantityHandledDbl - pvAmountDbl;

        if (newQuantityDbl <= 0) {
            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = 0;
        }else {
            //Set the new quantityDbl and show in Activity
            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = newQuantityDbl;
        }
    }

    private void mUpdateOtherProperties(){
        if (this.localItemPropertySortObl().size() == 1) {
            return;
        }

        ArrayList<Double> amountObl;
        int loopcounterInt = 0;
        int hulpLoopInt;
        if (currentModus == modusEnu.GENERATEDTAKE){
                amountObl = new ArrayList<>();
            for (Map.Entry<String, ArrayList<cLinePropertyValue>> itemPropertyEntry :  this.localItemPropertySortObl().entrySet()) {

                loopcounterInt += 1;
                hulpLoopInt = 0;
                for(cLinePropertyValue linePropertyValue : itemPropertyEntry.getValue()){
                    if (loopcounterInt == 1){
                        amountObl.add(linePropertyValue.getQuantityDbl());
                    } else{
                        linePropertyValue.quantityDbl = amountObl.get(hulpLoopInt);
                        hulpLoopInt += 1;
                    }
                }
            }
        }

        if (currentModus == modusEnu.GENERATEDPLACE){
            amountObl = new ArrayList<>();
            for (Map.Entry<String, ArrayList<cLinePropertyValue>> itemPropertyEntry :  this.localItemPropertySortObl().entrySet()) {

                loopcounterInt += 1;
                hulpLoopInt = 0;
                for(cLinePropertyValue linePropertyValue : itemPropertyEntry.getValue()){
                    if (loopcounterInt == 1){
                        amountObl.add(linePropertyValue.getQuantityDbl());
                    } else{
                        if (linePropertyValue.getLineProperty().getIsRequiredBln()){
                            linePropertyValue.quantityDbl = amountObl.get(hulpLoopInt);
                            hulpLoopInt += 1;
                        }
                    }
                }
            }
        }
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cLinePropertyValue.currentLinePropertyValue.getQuantityDbl());
        double availableDbl = 0.0;

        if (currentModus == modusEnu.GENERATEDPLACE || currentModus == modusEnu.GENERATEDTAKE){
            availableDbl = cLinePropertyValue.currentLinePropertyValue.getQuantityAvailableDbl() - cLinePropertyValue.currentLinePropertyValue.getQuantityDbl();

        } else{
            if (cMoveorderLine.currentMoveOrderLine.getQuantityDbl() != 0){
                availableDbl  = cMoveorderLine.currentMoveOrderLine.getQuantityDbl();
                ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr());

                for (cLinePropertyValue linePropertyValue : loopList ) {
                    availableDbl -= linePropertyValue.getQuantityDbl();
                }
            }   else {
                availableDbl = 999;
            }
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
        ItemPropertyTextInputFragment itemPropertyTextInputFragment = new ItemPropertyTextInputFragment(cLinePropertyValue.currentLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase(), cMoveorderLine.currentMoveOrderLine.presetValueObl);
        itemPropertyTextInputFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTTEXTFRAGMENT_TAG);
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

        cUserInterface.pShowGettingData();

        new Thread(this::mSendScans).start();

    }

    private void mGoBackToLinesActivity() {


        this.mResetCurrents();
        if (!cMoveorder.currentMoveOrder.pGetLinesViaWebserviceBln(true)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.error_get_movelines_failed),"",null,true);
            return ;
        }

        if(currentModus == modusEnu.GENERATEDPLACE || currentModus == modusEnu.GENERATEDTAKE){
            Intent intent = new Intent(cAppExtension.context, MoveLinesActivity.class);
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
            currentModus = modusEnu.NULL;
        }

        if(currentModus == modusEnu.MTTAKE){
            Intent intent = new Intent(cAppExtension.context, MoveLinesTakeMTActivity.class);
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
            currentModus = modusEnu.NULL;
        }
        if(currentModus == modusEnu.MTPLACE){
            Intent intent = new Intent(cAppExtension.context, MoveLinesPlaceMTActivity.class);
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
            currentModus = modusEnu.NULL;
        }

    }

    private void mSendScans() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return;
        }

       if (currentModus == modusEnu.GENERATEDTAKE) {

           cMoveorderLine.currentMoveOrderLine.pResetRst();
           if(!cMoveorder.currentMoveOrder.pMoveTakeItemHandledBln(this.barcodeObl)) {
               cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "", null, true);
               return ;
           }
           for (cLineProperty lineProperty : cLineProperty.allLinePropertysObl){
               if (lineProperty.getLineNoInt().equals(0)){
                   cLineProperty.allLinePropertysObl.remove(lineProperty);
               }
           }

           for (cLinePropertyValue linePropertyValue : cLinePropertyValue.allLinePropertysValuesObl){
               if(linePropertyValue.getLineNoInt() == 0) {
                   cLinePropertyValue.allLinePropertysValuesObl.remove(linePropertyValue);
               }
           }
       }
        if (currentModus == modusEnu.GENERATEDPLACE) {
            this.mUpdatePropertyLists();

            if(!cMoveorder.currentMoveOrder.pMovePlaceItemHandledBln(this.barcodeObl)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "", null, true);
                return ;
            }

        }
        if (currentModus == modusEnu.MTTAKE) {
            if(!cMoveorder.currentMoveOrder.pMoveTakeMTItemHandledBln(this.barcodeObl)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "", null, true);
                return ;
            }

            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = this.getQuantityHandledDbl();
            cMoveorderLine.currentMoveOrderLine.handledBln = true;
        }
        if (currentModus == modusEnu.MTPLACE) {
            if(!cMoveorder.currentMoveOrder.pMovePlaceMTItemHandledBln(this.barcodeObl)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "", null, true);
                return ;
            }
            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = this.getQuantityHandledDbl();
            cMoveorderLine.currentMoveOrderLine.handledBln = true;
        }

        this.mGoBackToLinesActivity();

    }

    private void mResetCurrents(){

        cMoveorderLine.currentMoveOrderLine.generatedPlaceLineValueObl = null;
        cMoveorderLine.currentMoveOrderLine = null;
        cMoveorder.currentMoveOrder.currentArticle = null;
        cMoveorder.currentMoveOrder.currentBranchBin = null;
        cMoveorder.currentMoveOrder.currentMoveorderBarcode = null;
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

    private  void mShowUnderMoveDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_underpick_header),
                cAppExtension.activity.getString(R.string.message_undermove_text,
                        cText.pDoubleToStringStr(cMoveorderLine.currentMoveOrderLine.getQuantityDbl()),
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

    private void mUpdatePropertyLists(){
        for (Map.Entry<String, ArrayList<cLinePropertyValue>> itemPropertyEntry :  this.localItemPropertySortObl().entrySet()) {

            for (cLinePropertyValue linePropertyValue : itemPropertyEntry.getValue()){
                if (linePropertyValue.getQuantityDbl() > 0){
                    cMoveorderLine.currentMoveOrderLine.generatedPlaceLineValueObl.add(linePropertyValue);
                }
            }
        }

    }

    //End Region Private Methods
}

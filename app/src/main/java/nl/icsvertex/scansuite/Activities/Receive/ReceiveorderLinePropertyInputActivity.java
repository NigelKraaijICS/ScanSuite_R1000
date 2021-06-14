package nl.icsvertex.scansuite.Activities.Receive;

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
import java.util.Collections;
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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DatePickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DynamicItemPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyNoInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyTextInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.PagerAdapters.ItemPropertyPagerAdapter;
import nl.icsvertex.scansuite.R;

public class ReceiveorderLinePropertyInputActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    private ImageView toolbarImage;
    private TextView toolbarTitle;

    private AppCompatImageButton imageButtonNoInputPropertys;
    private TextView articleDescriptionCompactText;
    private TextView articleDescription2CompactText;
    private TextView articleItemCompactText;
    private TextView articleBarcodeCompactText;
    private TextView quantityCompactText;
    private cIntakeorderBarcode intakeorderBarcode;

    private List<String> titleObl;
    private AppCompatImageButton imageButtonDone;

    private  List<cIntakeorderBarcode> scannedBarcodesObl;

    private TabLayout itemPropertyTabLayout;
    private ViewPager itemPropertyViewpager;
    public  int numberOfTabsInt;
    public boolean amountHandledBln;
    private boolean isGeneratedBln;
    private boolean deletedFromRecyclerBln;
    private int tabIndexInt;


    private LinkedHashMap<String, ArrayList<cLinePropertyValue>> localItemPropertySortObl(){
        LinkedHashMap<String, ArrayList<cLinePropertyValue>> linkedHashMap = new LinkedHashMap<>();

        Collections.sort(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl, (o1, o2) -> Integer.compare(o1.getLineProperty().getSortingSequenceNoInt(), o2.getLineProperty().getSortingSequenceNoInt()));

        for (cLinePropertyValue linePropertyValue : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl) {
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

        if (loopList != null) {
            for (cLinePropertyValue linePropertyValue : loopList ) {
                quantityDbl += linePropertyValue.getQuantityDbl();
            }
        }

        return  quantityDbl;

    }

    public  double getQuantityAvailable() {
        return    cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() - mQuantityHandledDbl();
    }


    //End Region Private Properties


    //Region Constructor
    public  ReceiveorderLinePropertyInputActivity() {

    }


    //End Region Constructor

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pickorderlineitemproperty_input);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mActivityInitialize();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (this.intakeorderBarcode.getQuantityHandledDbl() == 0){
                this.mGoBackToLinesActivity(true);

                return true;
            }
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

        if (this.intakeorderBarcode.getQuantityHandledDbl() == 0){
            this.mGoBackToLinesActivity(true);

            return;
        }
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

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();

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
        this.mFillSummaryPropertyList();
        this.mBuildAndFillTabs();
        this.mSetQuantityText();
        this.mShowHideOKButton();
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

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyInputObl().size() == 1) {
            cLineProperty.currentLineProperty = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyInputObl().get(0);
        } else {
            cLineProperty.currentLineProperty = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getLineProperty(Objects.requireNonNull(Objects.requireNonNull(this.itemPropertyTabLayout.getTabAt(this.itemPropertyTabLayout.getSelectedTabPosition())).getText()).toString());
        }
        this.pHandeManualAction(pvBarcodeScan);
    }

    public  void pCancelReceive() {

        this.mGoBackToLinesActivity(true);
    }

    public  void pHandeManualAction(cBarcodeScan pvBarcodeScan){

        if(  cReceiveorderSummaryLine.currentReceiveorderSummaryLine.presetValueObl() != null){
            boolean foundBln = false;
            ArrayList<String> propertyObl = new ArrayList<>();
            for (cLinePropertyValue propertyValue : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.presetValueObl()){
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
        //Check if kwnown value is selected
        if(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyValue(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())) != null){
            cLinePropertyValue.currentLinePropertyValue = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyValue(pvBarcodeScan.getBarcodeOriginalStr());
            cLineProperty.currentLineProperty = cLinePropertyValue.currentLinePropertyValue.getLineProperty();
        }

        if (cLineProperty.currentLineProperty.getItemProperty()== null ) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_property_unknown),"",true, true);
            return;
        }


        if (!cRegex.pCheckRegexBln( cLineProperty.currentLineProperty.getItemProperty().getLayoutStr(),cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()))) {
            cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout,cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),R.raw.badsound, true);
            return;
        }

        if (!this.amountHandledBln){
            if (this.getQuantityHandledDbl() == cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl() && !this.isGeneratedBln) {
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

        cLineProperty.currentLineProperty.pReceiveLineValueAdded(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

        if (this.amountHandledBln && !cLinePropertyValue.currentLinePropertyValue.getItemProperty().getUniqueBln()){

            if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyValueObl(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr()).size() > 1 ){

                int totalInt = (int) Math.round(this.intakeorderBarcode.getQuantityHandledDbl());
                int availableInt = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.specificLinePropertyObl(cLineProperty.currentLineProperty.getPropertyCodeStr()).size();
                int commitInt;
                double amountDbl ;
                for(cLinePropertyValue linePropertyValue : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyValueObl(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr())){

                    if (linePropertyValue.getQuantityDbl() > 0 && !linePropertyValue.getValueStr().equalsIgnoreCase(cLinePropertyValue.currentLinePropertyValue.getValueStr())){
                        totalInt -= (int) Math.round(linePropertyValue.getQuantityDbl());
                        availableInt -= 1;
                        continue;
                    }
                    amountDbl = totalInt/ availableInt;
                    commitInt = (int) Math.round(amountDbl);
                    totalInt -= commitInt;

                    if (availableInt == 1) {
                        commitInt += totalInt;
                    }
                    linePropertyValue.quantityDbl = commitInt;

                    availableInt -= 1;
                }
            }
            else{
                cLinePropertyValue.currentLinePropertyValue.quantityDbl = this.intakeorderBarcode.getQuantityHandledDbl();
            }
        }

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()) + " "  + cAppExtension.activity.getString(R.string.addedorhandled),R.raw.headsupsound,false);
        this.pTryToChangeQuantity();
        this.pRefreshActivity();

    }

    public void pHandled() {
        if (!mCheckAllPropertysHandledBln()){
            return;
        }
        this.mGoBackToLinesActivity(false);
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
       cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl.remove(cLinePropertyValue.currentLinePropertyValue);
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
        }
    }
    public void pShowDatePickerDialog() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        DatePickerFragment datePickerFragment = new DatePickerFragment(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.presetValueObl());
        datePickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTDATEFRAGMENT_TAG);
    }

    public   void pTryToChangeQuantity() {

        if (this.amountHandledBln){return;}

        double newQuantityDbl = 0.0;

        for(cLinePropertyValue linePropertyValue: cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyValueObl(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr())){
            newQuantityDbl += linePropertyValue.getQuantityDbl();
        }


        //Check if we would exceed amount, then show message if needed
        if (newQuantityDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl()) {

            if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln() && !cIntakeorder.currentIntakeOrder.isGenerated() && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0 ) {
                this.mShowExtraPiecesNotAllowed();
                return ;
            }

            if (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() > 0 && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0  && (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY())) {

                //Check if the new quantity would exceed the allowed quantity
                if (newQuantityDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl()) {

                    //We would exceed the allowed quantity so show that this is not allowed
                    this.mShowExtraPiecesNotAllowedByPercentage(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl());
                    return ;
                }
            }
            intakeorderBarcode.quantityHandledDbl = newQuantityDbl;
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine.quantityHandledDbl = mQuantityHandledDbl();
            return;
        }

        //Check if value already is zero
        if ( newQuantityDbl <= 0 ) {
            intakeorderBarcode.quantityHandledDbl = 0.0;
            return;
        }
        intakeorderBarcode.quantityHandledDbl = newQuantityDbl;
        cReceiveorderSummaryLine.currentReceiveorderSummaryLine.quantityHandledDbl = mQuantityHandledDbl();
    }

    //End Region Public Methods

    //Region Private Methods

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

        for (cLineProperty lineProperty : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyInputObl()){
            if (!lineProperty.getIsRequiredBln()){
                continue;
            }
            double quantityDbl = 0.0;
            ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(lineProperty.getPropertyCodeStr());
            if (loopList != null) {
                for (cLinePropertyValue linePropertyValue : loopList ) {
                    quantityDbl += linePropertyValue.getQuantityDbl();
                }
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

    private double mQuantityHandledDbl(){
        Double newQuantityDbl= 0.0;

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl()!= null){
            for (cIntakeorderBarcode intakeorderBarcode: cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl()){
                newQuantityDbl += intakeorderBarcode.getQuantityHandledDbl();
            }
        }
        newQuantityDbl += intakeorderBarcode.quantityHandledDbl;

        return newQuantityDbl;
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

        this.toolbarTitle.setOnClickListener(view -> mScrollToBottom());

        this.toolbarTitle.setOnLongClickListener(view -> {
            mScrollToTop();
            return true;
        });
    }

    private void mScrollToTop() {
    }

    private void mScrollToBottom() {
    }

    private void mSetArticleInfo(){

        this.mSetBarcode();
        this.isGeneratedBln = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() == 0.0;

        this.articleDescriptionCompactText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescriptionStr());
        this.articleDescription2CompactText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescription2Str());
        this.articleItemCompactText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getItemNoAndVariantCodeStr());
        this.articleBarcodeCompactText.setText(intakeorderBarcode.getBarcodeAndQuantityStr());

        if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.hasPropertysBln() || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyNoInputObl() == null || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyNoInputObl().size() == 0) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
        }
        else {
            this.imageButtonNoInputPropertys.setVisibility(View.VISIBLE);
        }
    }

    private  void mSetBarcode(){
        this.scannedBarcodesObl = new ArrayList<>();
        intakeorderBarcode = new cIntakeorderBarcode(cIntakeorderBarcode.currentIntakeOrderBarcode);
        this.scannedBarcodesObl.add(intakeorderBarcode);

        cLinePropertyValue.quantityPerUnitOfMeasureDbl = cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityHandledDbl() > 0) {
            Double handledDbl = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityHandledDbl();
            for (cIntakeorderBarcode intakeorderBarcode : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl()){
                handledDbl -= intakeorderBarcode.getQuantityHandledDbl();
            }
            if (handledDbl > 0){
                cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().get(0).quantityHandledDbl = handledDbl;
            }
        }
    }

    private  void mSetQuantityText() {
        String quantityStr;

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0){
            quantityStr =   cText.pDoubleToStringStr(mQuantityHandledDbl()) + "/" +  cText.pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl());
        } else {
            quantityStr =    cText.pIntToStringStr((int)this.getQuantityHandledDbl());
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

    private  void mShowExtraPiecesNotAllowed(){
        cUserInterface.pShowSnackbarMessage(toolbarImage , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
    }

    private  void mShowExtraPiecesNotAllowedByPercentage(Double pvValueDbl){

        cUserInterface.pShowSnackbarMessage(toolbarImage , cAppExtension.context.getString(R.string.number_received_total_cant_be_higher_then, cText.pDoubleToStringStr(pvValueDbl)) , null, false);

    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cLinePropertyValue.currentLinePropertyValue.getQuantityDbl());

        double availableDbl  = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() - cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityHandledDbl();
        ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr());

        if (loopList != null) {
            for (cLinePropertyValue linePropertyValue : loopList ) {
                availableDbl -= linePropertyValue.getQuantityDbl();
            }
        }

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() == 0){
            availableDbl = 999;
        }

        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, availableDbl);
        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);
        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private void mShowTextInputFragment() {
        if ( cLinePropertyValue.currentLinePropertyValue == null){
            return;
        }
        cUserInterface.pCheckAndCloseOpenDialogs();
        ItemPropertyTextInputFragment itemPropertyTextInputFragment = new ItemPropertyTextInputFragment( cLinePropertyValue.currentLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase(), cReceiveorderSummaryLine.currentReceiveorderSummaryLine.presetValueObl());
        itemPropertyTextInputFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTTEXTFRAGMENT_TAG);
    }


    private  void mGoBackToLinesActivity(Boolean pvCancelBln) {

        cUserInterface.pShowGettingData();

        if (!pvCancelBln){
            new Thread(this::mSendScans).start();}
        else{
            mShowLinesActivity();
        }
    }

    private void mShowLinesActivity(){
        this.mResetCurrents();

        Intent intent = new Intent(cAppExtension.context, ReceiveLinesActivity.class);
        startActivity(intent);
        finish();
    }


    private void mSetNoInputPropertyListener() {
        this.imageButtonNoInputPropertys.setOnClickListener(view -> mShowItemPropertyNoInputFragment());
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

    private void mSendScans() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return;
        }

        if(!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pItemVariantHandledBln(this.scannedBarcodesObl)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "",true,true);
            return;
        }
        cIntakeorderBarcode.currentIntakeOrderBarcode.quantityHandledDbl += this.intakeorderBarcode.getQuantityHandledDbl();

        this.mShowLinesActivity();

    }

    private void mResetCurrents(){
        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
        cReceiveorderLine.currentReceiveorderLine = null;
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = null;
        cIntakeorderBarcode.currentIntakeOrderBarcode = null;
    }


    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        String acceptStr = cAppExtension.activity.getString(R.string.message_accept_line);
        String rejectStr = cAppExtension.activity.getString(R.string.message_cancel_line);
        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderlinebusy_header),
                cAppExtension.activity.getString(R.string.message_orderlinebusy_text),
                rejectStr, acceptStr, false);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });
    }

    private  void mFillSummaryPropertyList (){

        List<cLinePropertyValue> resultObl = new ArrayList<>();
        List<cLineProperty> hulpObl = new ArrayList<>();

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl == null){
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl = new ArrayList<>();
        }else {
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl.clear();
        }
        if ( cReceiveorderSummaryLine.currentReceiveorderSummaryLine.presetValueObl() != null &&  cReceiveorderSummaryLine.currentReceiveorderSummaryLine.presetValueObl().size() > 0){
            resultObl.addAll(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.presetValueObl());
            for(cLinePropertyValue linePropertyValue : resultObl){
                if (!hulpObl.contains(linePropertyValue.getLineProperty())){
                    hulpObl.add(linePropertyValue.getLineProperty());
                }
            }
        }

        for (cLineProperty lineProperty : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.linePropertyInputObl()) {
            if (hulpObl.contains(lineProperty)){
                continue;
            }
            resultObl.add(new cLinePropertyValue(lineProperty));
        }

        cReceiveorderSummaryLine.currentReceiveorderSummaryLine.handledPropertyValueObl.addAll(resultObl);
    }



    //End Region Private Methods
}

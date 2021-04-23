package nl.icsvertex.scansuite.Activities.Intake;

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
import java.util.Comparator;
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
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DatePickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.DynamicItemPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyNoInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemPropertyTextInputFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.PagerAdapters.ItemPropertyPagerAdapter;
import nl.icsvertex.scansuite.R;

public class IntakeOrderLinePropertyInputActivity extends AppCompatActivity implements iICSDefaultActivity {

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

    private  void mFillSummaryPropertyList (){

        List<cLinePropertyValue> resultObl = new ArrayList<>();
        List<cLineProperty> hulpObl = new ArrayList<>();

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl == null){
            cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl = new ArrayList<>();
        }else {
            cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl.clear();
        }
        if ( cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.presetValueObl() != null &&  cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.presetValueObl().size() > 0){
            resultObl.addAll(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.presetValueObl());
            for(cLinePropertyValue linePropertyValue : resultObl){
                if (!hulpObl.contains(linePropertyValue.getLineProperty())){
                    hulpObl.add(linePropertyValue.getLineProperty());
                }
            }
        }

        for (cLineProperty lineProperty : cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyInputObl()) {
            if (hulpObl.contains(lineProperty)){
                continue;
            }
            resultObl.add(new cLinePropertyValue(lineProperty));
        }

        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl.addAll(resultObl);
    }

    private LinkedHashMap<String, ArrayList<cLinePropertyValue>> localItemPropertySortObl(){
        LinkedHashMap<String, ArrayList<cLinePropertyValue>> linkedHashMap = new LinkedHashMap<>();
        // ArrayList<cPickorderLinePropertyValue> pickorderLinePropertyValues = new ArrayList<>();

        Collections.sort(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl, new Comparator<cLinePropertyValue>() {

            @Override
            public int compare(cLinePropertyValue o1, cLinePropertyValue o2) {
                return Integer.compare(o1.getLineProperty().getSortingSequenceNoInt(), o2.getLineProperty().getSortingSequenceNoInt());
            }
        });

        for (cLinePropertyValue linePropertyValue : cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl) {
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
        return    cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl() - mQuantityHandledDbl();
    }


    //End Region Private Properties


    //Region Constructor
    public  IntakeOrderLinePropertyInputActivity() {

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
            if (this.intakeorderBarcode.getQuantityHandledDbl() == 0){
                this.mResetCurrents();
                this.mGoBackToLinesActivity();

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
            this.mResetCurrents();
            this.mGoBackToLinesActivity();

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

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyInputObl().size() == 1) {
            cLineProperty.currentLineProperty = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyInputObl().get(0);
        } else {
            cLineProperty.currentLineProperty = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getLineProperty(Objects.requireNonNull(Objects.requireNonNull(this.itemPropertyTabLayout.getTabAt(this.itemPropertyTabLayout.getSelectedTabPosition())).getText()).toString());
        }
        this.pHandeManualAction(pvBarcodeScan);
    }

    public  void pCancelReceive() {

        this.mResetCurrents();
        this.mGoBackToLinesActivity();
    }

    public  void pHandeManualAction(cBarcodeScan pvBarcodeScan){
        //Check if kwnown value is selected
        if(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyValue(pvBarcodeScan.getBarcodeOriginalStr()) != null){
            cLinePropertyValue.currentLinePropertyValue = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyValue(pvBarcodeScan.getBarcodeOriginalStr());
            cLineProperty.currentLineProperty = cLinePropertyValue.currentLinePropertyValue.getLineProperty();
        }

        if (cLineProperty.currentLineProperty.getItemProperty()== null ) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_property_unknown),"",true, true);
            return;
        }


        if (!cRegex.pCheckRegexBln( cLineProperty.currentLineProperty.getItemProperty().getLayoutStr(),pvBarcodeScan.getBarcodeOriginalStr())) {
            cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout,cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),R.raw.badsound, true);
            return;
        }

        if (!this.amountHandledBln){
            if (this.getQuantityHandledDbl() == cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl() && cSetting.RECEIVE_NO_EXTRA_ITEMS() && cSetting.RECEIVE_NO_EXTRA_PIECES() && !this.isGeneratedBln) {
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

        cLineProperty.currentLineProperty.pIntakeLineValueAdded(pvBarcodeScan.getBarcodeOriginalStr());

        if (this.amountHandledBln && !cLinePropertyValue.currentLinePropertyValue.getItemProperty().getUniqueBln()){
            //Check if summary line has more than 1 property if so devide equally

            if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyValueObl(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr()).size() > 1 ){

                int totalInt = (int) Math.round(this.intakeorderBarcode.getQuantityHandledDbl());
                int availableInt = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyValueObl(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr()).size();
                int commitInt;
                double amountDbl ;
                for(cLinePropertyValue linePropertyValue : cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyValueObl(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr())){

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

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, pvBarcodeScan.getBarcodeOriginalStr() + " "  + cAppExtension.activity.getString(R.string.addedorhandled),R.raw.headsupsound,false);
        this.pTryToChangeQuantity();
        this.pRefreshActivity();

    }

    public void pHandled() {

        if (!mCheckAllPropertysHandledBln()){
            return;
        }
        this.mGoBackToIntakeActivity(false);
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
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl.remove(cLinePropertyValue.currentLinePropertyValue);
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
        DatePickerFragment datePickerFragment = new DatePickerFragment(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.presetValueObl());
        datePickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTDATEFRAGMENT_TAG);
    }

    public   void pTryToChangeQuantity() {

        if (this.amountHandledBln){return;}

        double newQuantityDbl = 0.0;

        for(cLinePropertyValue linePropertyValue: cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyValueObl(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr())){
            newQuantityDbl += linePropertyValue.getQuantityDbl();
        }


        //Check if we would exceed amount, then show message if needed
        if (newQuantityDbl > cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl()) {

            if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln() && !cIntakeorder.currentIntakeOrder.isGenerated() && cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl() > 0 ) {
                this.mShowExtraPiecesNotAllowed();
                return ;
            }

            if (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() > 0 && cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl() > 0  && (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY())) {

                //Check if the new quantity would exceed the allowed quantity
                if (newQuantityDbl > cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl() && cSetting.RECEIVE_NO_EXTRA_PIECES() && cSetting.RECEIVE_NO_EXTRA_ITEMS()) {

                    //We would exceed the allowed quantity so show that this is not allowed
                    this.mShowExtraPiecesNotAllowed();
                    return ;
                }
            }
            intakeorderBarcode.quantityHandledDbl = newQuantityDbl;
            return;
        }

        //Check if value already is zero
        if ( newQuantityDbl <= 0 ) {
            intakeorderBarcode.quantityHandledDbl = 0.0;
            return;
        }
        intakeorderBarcode.quantityHandledDbl = newQuantityDbl;
    }

    public void pSendScansBln() {

        IntakeOrderIntakeActivity.handledViaPropertysBln = true;
        cIntakeorderBarcode.currentIntakeOrderBarcode.quantityHandledDbl = this.intakeorderBarcode.getQuantityHandledDbl();
        mShowIntakeActivity();

    }

    //End Region Public Methods

    //Region Private Methods

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

        for (cLineProperty lineProperty : cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyInputObl()){
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

    private double mQuantityHandledDbl(){
        Double newQuantityDbl= 0.0;

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl()!= null){
            for (cIntakeorderBarcode intakeorderBarcode: cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl()){
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

        this.mSetBarcode();
        this.isGeneratedBln = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl() == 0.0;

        this.articleDescriptionCompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
        this.articleDescription2CompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
        this.articleItemCompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoAndVariantCodeStr());
        this.articleBarcodeCompactText.setText(intakeorderBarcode.getBarcodeAndQuantityStr());

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.hasPropertysBln() || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyNoInputObl() == null || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.linePropertyNoInputObl().size() == 0) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
        }
        else {
            this.imageButtonNoInputPropertys.setVisibility(View.VISIBLE);
        }
    }

    private  void mSetBarcode(){

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0);
        }

        intakeorderBarcode = new cIntakeorderBarcode(cIntakeorderBarcode.currentIntakeOrderBarcode);

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl() > 0) {
            Double handledDbl = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl();
            for (cIntakeorderBarcode intakeorderBarcode : cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl()){
                handledDbl -= intakeorderBarcode.getQuantityHandledDbl();
            }
            if (handledDbl > 0){
                cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0).quantityHandledDbl = handledDbl;
            }
        }
    }

    private  void mSetQuantityText() {
        String quantityStr = "";

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl() > 0){
            quantityStr =   cText.pDoubleToStringStr(mQuantityHandledDbl()) + "/" +  cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl());
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


    private void mGoBackToLinesActivity() {

        Intent intent = null;

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS")) {
            intent = new Intent(cAppExtension.context, IntakeorderMASLinesActivity.class);
        }

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAT")) {
            intent = new Intent(cAppExtension.context, IntakeorderMATLinesActivity.class);
        }

        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mResetCurrents() {

        IntakeOrderIntakeActivity.handledViaPropertysBln = false;
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
        cIntakeorderMATLine.currentIntakeorderMATLine = null;
        cIntakeorder.currentIntakeOrder.currentBin = null;
        this.scannedBarcodesObl = null;
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cLinePropertyValue.currentLinePropertyValue.getQuantityDbl());

        double availableDbl  = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl() - cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl();
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
        if ( cLinePropertyValue.currentLinePropertyValue == null){
            return;
        }
        cUserInterface.pCheckAndCloseOpenDialogs();
        ItemPropertyTextInputFragment itemPropertyTextInputFragment = new ItemPropertyTextInputFragment( cLinePropertyValue.currentLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase());
        itemPropertyTextInputFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTTEXTFRAGMENT_TAG);
    }


    private  void mGoBackToIntakeActivity(Boolean pvCancelBln) {

        cUserInterface.pShowGettingData();

        if (!pvCancelBln){ new Thread(new Runnable() {
            public void run() {
                pSendScansBln();
            }
        }).start();}
        else{
            mShowIntakeActivity();
        }
    }

    private void mShowIntakeActivity(){
        Intent intent = new Intent(cAppExtension.context, IntakeOrderIntakeActivity.class);
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

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                cAppExtension.activity.getString(R.string.message_orderbusy_text),
                cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line), false);
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

package nl.icsvertex.scansuite.Activities.Pick;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.Scanning.cProGlove;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainer;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderCompositeBarcode.cPickorderCompositeBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectPropertyFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ContentlabelContainerFragment;
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

    private AppCompatImageView imageContentContainer;
    private  TextView contentLabelContainerText;

    private List<String> titleObl;
    private AppCompatImageButton imageButtonDone;

    private TabLayout itemPropertyTabLayout;
    private ViewPager itemPropertyViewpager;
    public  int numberOfTabsInt;
    public boolean amountHandledBln;
    private boolean deletedFromRecyclerBln;
    private int tabIndexInt;

    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

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

        if (loopList != null) {
            for (cLinePropertyValue pickorderLinePropertyValue : loopList ) {
                quantityDbl += pickorderLinePropertyValue.getQuantityDbl();
            }
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
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_pick,pvMenu);
        return true;
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

        if (item.getItemId() == R.id.item_new_container){
            this.mAddContentContainer();
        }

        if (item.getItemId() == R.id.item_switch_container){
            DialogFragment selectedFragment = new ContentlabelContainerFragment();
            selectedFragment.setCancelable(true);
            selectedFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BINITEMSFRAGMENT_TAG);
        }

        // deselect everything
        int size = actionMenuNavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            actionMenuNavigation.getMenu().getItem(i).setChecked(false);
        }

        // close drawer when item is tapped
        this.menuActionsDrawer.closeDrawers();

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {

        if (cPickorder.currentPickOrder.isPickPickToContainerBln() || cSetting.PICK_PICK_TO_CONTAINER()){
            MenuItem item_new_container = pvMenu.findItem(R.id.item_new_container);
            item_new_container.setVisible(true);

            if (cPickorder.currentPickOrder.contentlabelContainerObl != null){
                if (cPickorder.currentPickOrder.contentlabelContainerObl.size() > 1){
                    MenuItem item_switch_container = pvMenu.findItem(R.id.item_switch_container);
                    item_switch_container.setVisible(true);
                }
            }
        }

        return super.onPrepareOptionsMenu(pvMenu);
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

        this.mSetProGloveScreen();
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

        this.imageContentContainer = findViewById(R.id.imageContentContainer);
        this.contentLabelContainerText = findViewById(R.id.contentLabelContainerText);

        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);
        this.articleDescriptionCompactText = findViewById(R.id.articleDescriptionCompactText);
        this.articleDescription2CompactText = findViewById(R.id.articleDescription2CompactText);
        this.articleItemCompactText = findViewById(R.id.articleItemCompactText);
        this.articleBarcodeCompactText = findViewById(R.id.articleBarcodeCompactText);
        this.quantityCompactText = findViewById(R.id.quantityCompactText);
        this.itemPropertyTabLayout = findViewById(R.id.itemPropertyTabLayout);
        this.itemPropertyViewpager = findViewById(R.id.itemPropertyViewpager);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawerProperty);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);
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
        this.mCheckContainerObl();
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

    private void mSetProGloveScreen() {
        String proglovedata = "1||" + getString(R.string.proglove_check_terminal_screen);
        cProGlove myproglove= new cProGlove();
        myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, true, 0, 0);
    }
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

        if (cPickorderLine.currentPickOrderLine.presetValueObl() != null){
            boolean foundBln = false;
            ArrayList<String> propertyObl = new ArrayList<>();
            for (cLinePropertyValue propertyValue : cPickorderLine.currentPickOrderLine.presetValueObl()){
                if(propertyValue.getPropertyCodeStr().equalsIgnoreCase( cLineProperty.currentLineProperty.getPropertyCodeStr())){
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

        if (!cRegex.pCheckRegexBln( cLineProperty.currentLineProperty.getItemProperty().getLayoutStr(),cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()))) {
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
        cResult hulpRst = cLineProperty.currentLineProperty.pCheckScanForUniquePropertyRst(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true, true);
            return;
        }

        cLineProperty.currentLineProperty.pValueAdded(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

        if (this.amountHandledBln && !cLinePropertyValue.currentLinePropertyValue.getItemProperty().getUniqueBln()){
            cLinePropertyValue.currentLinePropertyValue.quantityDbl = cPickorderLine.currentPickOrderLine.getQuantityDbl();
        }

        cUserInterface.pShowSnackbarMessage(this.itemPropertyTabLayout, cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()) + " "  + cAppExtension.activity.getString(R.string.addedorhandled),R.raw.headsupsound,false);
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

        //Try to send the line
        cPickorderLine.currentPickOrderLine.pHandledBln();

        this.mGetNextPickLineForCurrentBin();
    }

    public void pRefreshActivity(){
        this.mSetQuantityText();
        this.mShowHideOKButton();
        this.mBuildAndFillTabs();
        this.mSelectTabAndItem();
    }

    public void pRefreshArticleInfo(){
        this.mSetArticleInfo();
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
        }
    }

    public void pShowDatePickerDialog() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        DatePickerFragment datePickerFragment = new DatePickerFragment(cPickorderLine.currentPickOrderLine.presetValueObl());
        datePickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ITEMPROPERTYINPUTDATEFRAGMENT_TAG);
    }

    public void pTryToChangePickedQuantity() {

        if (this.amountHandledBln){
            return;
        }
        double newQuantityDbl;

        newQuantityDbl = this.getQuantityHandledDbl();

        if (newQuantityDbl <= 0) {
            newQuantityDbl = 0;
        }

        //Set the new quantityDbl and show in Activity
        cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;

        //Add or set line barcodeStr
        cPickorderLine.currentPickOrderLine.pAddOrSetLineBarcode(newQuantityDbl);

        //Update orderline info (quantityDbl, timestamp, localStatusInt)
        cPickorderLine.currentPickOrderLine.pHandledIndatabase();

    }

    //End Region Public Methods

    //Region Private Methods

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

    private void mSetCloseListener() {
        this.imageButtonDone.setOnClickListener(view -> {

            if (getQuantityAvailable() > 0 ) {
                mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
                return;
            }
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

        for (cLineProperty inputPickorderLineProperty : cPickorderLine.currentPickOrderLine.linePropertyInputObl()){
            if (!inputPickorderLineProperty.getIsRequiredBln()){
                continue;
            }
            double quantityDbl = 0.0;
            ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(inputPickorderLineProperty.getPropertyCodeStr());
            if (loopList != null) {
                for (cLinePropertyValue linePropertyValue : loopList ) {
                    quantityDbl += linePropertyValue.getQuantityDbl();
                }
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

        cLinePropertyValue.quantityPerUnitOfMeasureDbl = cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl();

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
        if (cPickorder.currentPickOrder.contentlabelContainerObl != null){
            this.imageContentContainer.setVisibility(View.VISIBLE);
            this.contentLabelContainerText.setText(cAppExtension.context.getString(R.string.container_sequence_no) + " " + cText.pLongToStringStr(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng()));
        } else {
            this.imageContentContainer.setVisibility(View.GONE);
            this.contentLabelContainerText.setVisibility(View.GONE);
        }
    }

    private void mCheckContainerObl(){
        if (cPickorder.currentPickOrder.isPickPickToContainerBln() || cSetting.PICK_PICK_TO_CONTAINER()) {
            if (cPickorderLine.currentPickOrderLine.containerObl == null){
                cPickorderLine.currentPickOrderLine.containerObl = new ArrayList<>();
            }
        }

        if(cPickorderLine.currentPickOrderLine.containerObl != null){
            if (cPickorderLine.currentPickOrderLine.containerObl.size() == 0){
                //No containers added to the line so make one
                cContentlabelContainer contentlabelContainer = new cContentlabelContainer(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng(), 0.0);
                cPickorderLine.currentPickOrderLine.containerObl.add(contentlabelContainer);
                cPickorderLine.currentPickOrderLine.currentContainer = contentlabelContainer;
            } else {
                boolean foundBln = false;
                if(!cPickorderLine.currentPickOrderLine.currentContainer.getContainerSequencoNoLng().equals(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng())){
                    //check if container is in the list
                    for (cContentlabelContainer contentlabelContainer : cPickorderLine.currentPickOrderLine.containerObl){
                        if (contentlabelContainer.getContainerSequencoNoLng().equals(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng())){
                            cPickorderLine.currentPickOrderLine.currentContainer = contentlabelContainer;
                            foundBln = true;
                        }
                    }
                    if (!foundBln){
                        //Not found add new container to list
                        cContentlabelContainer contentlabelContainer = new cContentlabelContainer(cContentlabelContainer.currentContentlabelContainer.getContainerSequencoNoLng(), 0.0);
                        cPickorderLine.currentPickOrderLine.containerObl.add(contentlabelContainer);
                        cPickorderLine.currentPickOrderLine.currentContainer = contentlabelContainer;
                    }
                }
            }
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
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
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
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            acceptRejectPropertyFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cLinePropertyValue.currentLinePropertyValue.getQuantityDbl());

        double availableDbl  = cPickorderLine.currentPickOrderLine.getQuantityDbl();
        ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr());

        if (loopList != null) {
            for (cLinePropertyValue linePropertyValue : loopList ) {
                availableDbl -= linePropertyValue.getQuantityDbl();
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
        ItemPropertyTextInputFragment itemPropertyTextInputFragment = new ItemPropertyTextInputFragment(cLinePropertyValue.currentLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase(), cPickorderLine.currentPickOrderLine.presetValueObl());
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
        this.imageButtonNoInputPropertys.setOnClickListener(view -> mShowItemPropertyNoInputFragment());
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

    private void mAddContentContainer(){

        Long sequenceNoLng = 0L;

        sequenceNoLng = cText.pIntegerToLongLng(cPickorder.currentPickOrder.contentlabelContainerObl.size() + 1);

        cContentlabelContainer contentlabelContainer = new cContentlabelContainer(sequenceNoLng,0.0);
        cPickorder.currentPickOrder.contentlabelContainerObl.add(contentlabelContainer);
        cPickorderLine.currentPickOrderLine.containerObl.add(contentlabelContainer);
        cPickorderLine.currentPickOrderLine.currentContainer = contentlabelContainer;

        this.mSetArticleInfo();
    }

    //End Region Private Methods
}

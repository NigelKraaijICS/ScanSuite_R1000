package nl.icsvertex.scansuite.Activities.Pick;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.ArticleStock.cArticleStock;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Move.Moveorders.cMoveorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BinItemsFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemStockFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintBinLabelFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintItemLabelFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class PickorderPickGeneratedActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties

    private int pickCounterMinusHelperInt;
    private int pickCounterPlusHelperInt;

    private Handler minusHandler;
    private Handler plusHandler;

    private  ConstraintLayout pickorderPickGeneratedContainer;

    private  CardView articleContainer;
    private ConstraintLayout articleInfoContainer;

    private Spinner destinationSpinner;

    private  Toolbar toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;

    private  ImageView articleThumbImageView;
    private  ImageView imageButtonBarcode;
    private ImageView imageButtonNoInputPropertys;

    private  TextView quantityText;
    private  TextView quantityRequiredText;

    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;

    private  TextView textViewAction;

    private cArticleStock articleStock = null;
    private Double quantityScannedDbl = 0.0;
    private List<cPickorderBarcode> scannedBarcodesObl;

    private cBranch  scannedBranch;
    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickorder_pick_generated);
        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_pick,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {
        //  invalidateOptionsMenu();


        MenuItem item_bin_stock = pvMenu.findItem(R.id.item_bin_stock);
        item_bin_stock.setVisible(true);
        if (cSetting.GENERIC_PRINT_BINLABEL()){
            MenuItem item_print_bin = pvMenu.findItem(R.id.item_print_bin);
            item_print_bin.setVisible(cPickorder.currentPickOrder.currentBranchBin != null);
        }
        if (cSetting.GENERIC_PRINT_ITEMLABEL()){
            MenuItem item_print_item = pvMenu.findItem(R.id.item_print_item);
            item_print_item.setVisible(cPickorderBarcode.currentPickorderBarcode != null);
        }
        return super.onPrepareOptionsMenu(pvMenu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        DialogFragment selectedFragment = null;

        switch (item.getItemId()) {

            case android.R.id.home:
                if (this.quantityScannedDbl > 0) {
                    mShowAcceptFragment();
                    return true;
                }

                pCancelPick();

                return true;

            case R.id.item_bin_stock:
                selectedFragment = new BinItemsFragment(cPickorderLine.currentPickOrderLine.getBinCodeStr());
                break;

            case R.id.item_print_bin:
                selectedFragment = new PrintBinLabelFragment();
                break;

            case R.id.item_print_item:
                cArticle.currentArticle= cPickorder.currentPickOrder.articleObl.get(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
                selectedFragment = new PrintItemLabelFragment();
                break;


            default:
                break;
        }


        // deselect everything
        int size = actionMenuNavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            actionMenuNavigation.getMenu().getItem(i).setChecked(false);
        }

        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        this.menuActionsDrawer.closeDrawers();

        if (selectedFragment != null) {
            selectedFragment.setCancelable(true);
            selectedFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BINITEMSFRAGMENT_TAG);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if (this.quantityScannedDbl > 0) {
            mShowAcceptFragment();
            return;
        }

        pCancelPick();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_pickorderpick));

        this.mInitScreen();

        this.mFieldsInitialize();

    }

    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {

        this.pickorderPickGeneratedContainer = findViewById(R.id.pickorderPickGeneratedContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);
        this.articleContainer = findViewById(R.id.articleContainer);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);

        this.destinationSpinner = findViewById(R.id.destinationSpinner);

        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.textViewAction = findViewById(R.id.textViewAction);
        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick_pf);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubtext.setSelected(true);
        this.toolbarSubtext.setText(cPickorder.currentPickOrder.currentBranchBin.getBinCodeStr());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.pickCounterPlusHelperInt = 0;
        this.pickCounterMinusHelperInt = 0;


        this.mShowArticleImage();
        this.mShowOrHideArticleInfoContainer();

        this.mShowArticleInfo();
        this.mShowBarcodeInfo();
        this.mShowNoInputPropertyInfo();
        this.mFillDestinationSpinner();
        this.mShowQuantityInfo();
        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mSetInstructions();
    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.PICK_SELECTEREN_BARCODE()) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDestinationSpinnerListener();
        this.mSetDoneListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        //If we scan a branch reset current branch
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            cResult hulpRst = this.mHandleDestinationScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pShowSnackbarMessage(this.pickorderPickGeneratedContainer,hulpRst.messagesStr(),null,true);
                this.mSetInstructions();
                this.mFieldsInitialize();
            }
            return;
        }

        //Scan must be an article, so handle the scan
        cResult hulpRst = this.mHandleArticleScanRst(pvBarcodeScan);
        if (! hulpRst.resultBln) {
            cUserInterface.pShowSnackbarMessage(this.pickorderPickGeneratedContainer,hulpRst.messagesStr(),null,true);
            this.mSetInstructions();
            this.mFieldsInitialize();
        }

    }

    public  void pAcceptPick(boolean ignoreAccept) {
        if (ignoreAccept) {
            cUserInterface.pPlaySound(R.raw.headsupsound,null);
            cUserInterface.pDoBoing(this.textViewAction);
            return;
        }

        cPickorderLine.currentPickOrderLine.pHandledIndatabase();
        this.mPickDone();
    }

    public  void pCancelPick() {
        this.mGoBackToLinesActivity();
    }

    public  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    //End Region Public Methods

    //Region Private Methods

    //Views


    private void mSetArticleImageListener() {
        this.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();

            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        this.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cPickorder.currentPickOrder.currentArticle.barcodesObl == null || cPickorder.currentPickOrder.currentArticle.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cPickorder.currentPickOrder.currentArticle.barcodesObl.size() == 1) {
                    pHandleScan(cBarcodeScan.pFakeScan(cPickorder.currentPickOrder.currentArticle.barcodesObl.get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();
            }
        });
    }

    private void mSetDestinationSpinnerListener() {

        this.destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (destinationSpinner.getSelectedItem().toString().equalsIgnoreCase(cAppExtension.activity.getString(R.string.message_select_a_destination))) {
                    scannedBranch = null;
                    return;
                }

                scannedBranch = cBranch.pGetBranchByDescriptionStr(destinationSpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                scannedBranch = null;
            }

        });
    }

    private  void mShowOrHideArticleInfoContainer() {

        boolean hideArticleInfoContainerBln = false;

        if (cPickorder.currentPickOrder.currentArticle == null) {
            hideArticleInfoContainerBln =true;
        }
        else
        {
            //Get article info via the web service
            cArticle.currentArticle  = new cArticle(cPickorder.currentPickOrder.currentArticle.getItemNoStr(), cPickorder.currentPickOrder.currentArticle.getVariantCodeStr());
                hideArticleInfoContainerBln =true;

                //todo: something with itempropertys
//            if ( cPickorderLine.currentPickOrderLine.itemProperyDataObl() == null) {
//                hideArticleInfoContainerBln =true;
//            }
        }

        if (hideArticleInfoContainerBln) {
            this.articleInfoContainer.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            newCardViewLayoutParams.setMargins(15,15,15,15);
            this.articleContainer.setLayoutParams(newCardViewLayoutParams);

            ConstraintSet constraintSetSpace = new ConstraintSet();
            constraintSetSpace.clone(pickorderPickGeneratedContainer);
            constraintSetSpace.connect(articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
            constraintSetSpace.applyTo(pickorderPickGeneratedContainer);
            return;
        }

        this.articleInfoContainer.setVisibility(View.VISIBLE);

        //todo: something with itempropertys
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.articleInfoContainer, new ArticleInfoFragment(cPickorderLine.currentPickOrderLine.itemProperyDataObl()));
//        transaction.commit();
    }

    private  void mShowArticleInfo() {

        if (cPickorder.currentPickOrder.currentArticle == null) {
            this.articleDescriptionText.setText(cAppExtension.activity.getString(R.string.novalueyet));
            this.articleDescription2Text.setText(cAppExtension.activity.getString(R.string.novalueyet));
            this.articleItemText.setText(cAppExtension.activity.getString(R.string.novalueyet));
            return;
        }

        this.articleDescriptionText.setText(cPickorder.currentPickOrder.currentArticle.getDescriptionStr());
        this.articleDescription2Text.setText(cPickorder.currentPickOrder.currentArticle.getDescription2Str());
        this.articleItemText.setText(cPickorder.currentPickOrder.currentArticle.getItemNoAndVariantCodeStr());

        if (cPickorder.currentPickOrder.currentArticle.getDescription2Str().isEmpty()) {
            this.articleDescription2Text.setVisibility(View.GONE);
        }
    }

    private  void mShowBarcodeInfo() {

        if (cPickorder.currentPickOrder.currentArticle == null) {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.novalueyet));
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            if (cPickorder.currentPickOrder.currentArticle.barcodesObl.size() == 1) {
                cPickorderBarcode.currentPickorderBarcode =  new cPickorderBarcode(cPickorder.currentPickOrder.currentArticle.barcodesObl.get(0)) ;
            }
        }

        if (cPickorderBarcode.currentPickorderBarcode != null) {
            this.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private  void mShowNoInputPropertyInfo() {

        if (cPickorder.currentPickOrder.currentArticle == null) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
            return;
        }

        this.imageButtonNoInputPropertys.setVisibility(View.GONE);

        //todo: something with propertys
//        if (!cPickorderLine.currentPickOrderLine.hasPropertysBln() || cPickorderLine.currentPickOrderLine.pickorderLinePropertyNoInputObl() == null || cPickorderLine.currentPickOrderLine.pickorderLinePropertyNoInputObl().size() == 0) {
//            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
//        }
//        else {
//            this.imageButtonNoInputPropertys.setVisibility(View.VISIBLE);
//        }
    }

    private void mShowQuantityInfo(){

        if (cPickorder.currentPickOrder.currentArticle == null) {
            this.quantityText.setVisibility(View.INVISIBLE);
            this.quantityRequiredText.setVisibility(View.INVISIBLE);
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        this.quantityText.setVisibility(View.VISIBLE);
        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));


    }

    private  void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (cPickorder.currentPickOrder.currentArticle == null || !cPickorder.currentPickOrder.isPickWithPictureBln()) {
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            this.articleThumbImageView.setVisibility(View.GONE);
            return;
        }

        this.articleThumbImageView.setVisibility(View.VISIBLE);

        //If picture is not in cache (via webservice) then show no image
        if (!cPickorder.currentPickOrder.currentArticle.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cPickorder.currentPickOrder.currentArticle.articleImage == null || cPickorder.currentPickOrder.currentArticle.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cPickorder.currentPickOrder.currentArticle.articleImage.imageBitmap());
    }

    private  void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cPickorder.currentPickOrder.currentArticle== null) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
            return;
        }

        if (cSetting.PICK_PER_SCAN()) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
        }

        if (!cSetting.PICK_SELECTEREN_BARCODE()) {
            this.imageButtonBarcode.setVisibility(View.GONE);
        } else {
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

    }

    //Scans and manual input

    private void mNumberClicked() {

        if (cSetting.PICK_PER_SCAN()) {
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(pickorderPickGeneratedContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(pickorderPickGeneratedContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberPickerFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {
        this.mTryToChangeQuantity(pvQuantityDbl != 0, true,pvQuantityDbl);
    }

    // Lines, Barcodes, Packing Tables and destionation

    private  void mSendPickorderLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            return;
        }


        cResult hulpRst;
        List<cPickorderBarcode> sortedBarcodeList = this.mSortBarcodeList(this.scannedBarcodesObl);

        //Create new line to send
        cPickorderLine.currentPickOrderLine = new cPickorderLine(this.scannedBranch.getBranchStr(), this.quantityScannedDbl);

        hulpRst = cPickorderLine.currentPickOrderLine.pGeneratedLineHandledRst(sortedBarcodeList);
        if (!hulpRst.resultBln) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line) + " " + hulpRst.messagesStr(), null);
            cPickorderLine.currentPickOrderLine.pErrorSending();
            return;
        }

        cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_line_send), R.raw.headsupsound);
        this.mResetCurrents();
        this.mFieldsInitialize();
    }

    private void mHandlePickDoneClick() {

        if (this.scannedBranch == null) {
            cUserInterface.pShowSnackbarMessage(pickorderPickGeneratedContainer, cAppExtension.activity.getString(R.string.message_select_a_destination),null,true );
            return;
        }

        if (this.quantityScannedDbl == 0) {
            cUserInterface.pShowSnackbarMessage(pickorderPickGeneratedContainer, cAppExtension.activity.getString(R.string.message_scan_article_first),null,true );
            return;
        }

        //All is done
        this.mPickDone();
    }

    private  void mPickDone() {
        this.mSendPickorderLine();
    }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandlePickDoneClick();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        this.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (plusHandler != null) return true;
                    plusHandler = new Handler();
                    plusHandler.postDelayed(mPlusAction, 750);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (plusHandler == null) return true;
                    plusHandler.removeCallbacks(mPlusAction);
                    plusHandler = null;
                    pickCounterPlusHelperInt = 0;
                }

                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcodeStr, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }

                mTryToChangeQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        this.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (minusHandler != null) return true;
                    minusHandler = new Handler();
                    minusHandler.postDelayed(mMinusAction, 750);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (minusHandler == null) return true;
                    minusHandler.removeCallbacks(mMinusAction);
                    minusHandler = null;
                    pickCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcodeStr, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeQuantity(false, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    private void mSetNumberListener() {
        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
        quantityRequiredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
    }

    //Dialogs and Activitys

    private  void mShowFullArticleFragment() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);
    }

    private void mShowAcceptFragment(){
        boolean ignoreAccept = false;
        if (cPickorder.currentPickOrder.isPVBln()) {
            ignoreAccept = true;
        }

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                cAppExtension.activity.getString(R.string.message_orderbusy_text),
                cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line),ignoreAccept);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private  void mGoBackToLinesActivity() {
        this.mResetCurrents();
        Intent intent = new Intent(cAppExtension.context, PickorderLinesGeneratedActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEPICKERFRAGMENT_TAG);
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY,this.quantityScannedDbl.intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,this.articleStock.getQuantityDbl().intValue());

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
    }

    private void mFillDestinationSpinner() {

        List<String> destinationObl = new ArrayList<>();

        destinationObl.add(cAppExtension.activity.getString(R.string.message_select_a_destination));

        for (cBranch branch :cBranch.allBranchesObl ) {

            if (branch.getBranchStr().equalsIgnoreCase(cUser.currentUser.currentBranch.getBranchStr())) {
                continue;
            }

            destinationObl.add(branch.getBranchNameStr());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                                                          android.R.layout.simple_spinner_dropdown_item,
                                                          destinationObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.destinationSpinner.setAdapter(adapter);

        if (this.scannedBranch == null) {
            return;
        }

        this.destinationSpinner.setSelection(adapter.getPosition(this.scannedBranch.getBranchNameStr()));
    }

    private cResult mHandleArticleScanRst(cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        boolean scanHandledBln = false;

        //We scanned an article, so first check if we already have a current baroce

        //We have a current barcode
        if (cPickorderBarcode.currentPickorderBarcode != null) {
            //Handle the scan for the current barcode
            result = this.mHandleBarcodeScanForCurrentItemRst(pvBarcodeScan);
            if (!result.resultBln) {
                scanHandledBln = true;
            }
            else
            {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.activity.getString(R.string.error_barcode_doesnt_belong_to_line));
                return result;
            }

        }

        //If we still need to handle the scan, do this
        if (!scanHandledBln){

            //Check if we already know the barcode
            cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvBarcodeScan);

            //This is a new barcode
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                //if something went wrong we are done, so return result
                result = this.mHandleUnknownBarcodeScanRst(pvBarcodeScan);
                if (!result.resultBln) {
                    return  result;
                }
            }
            else {
                //Get article from cache
                cPickorder.currentPickOrder.currentArticle = cPickorder.currentPickOrder.articleObl.get( cPickorderBarcode.currentPickorderBarcode.getItemNoAndVariantCodeStr());
                if ( cPickorder.currentPickOrder.currentArticle == null) {
                    result.resultBln = false;
                    result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_article));
                    return  result;
                }
            }
        }

        //Scanned barcode matches current barcode so raise barcode scanned
        this.mArticleBarcodeScanned();
        this.mFieldsInitialize();

        //if we receive false, we are done and scan is handled
        result.resultBln = true;
        return  result;

    }

    private cResult mHandleDestinationScanRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        this.scannedBranch = null;

        cResult hulpRst = this.mCheckDestionationRst(pvBarcodeScan);
        if (! hulpRst.resultBln) {
            result.resultBln = false;
            result.pAddErrorMessage(hulpRst.messagesStr());
            return result;
        }

        this.mFillDestinationSpinner();
        return result;
    }

    private void mArticleBarcodeScanned() {

        cResult result;

        cUserInterface.pCheckAndCloseOpenDialogs();

        result = this.mGetArticleStockForBINRst();
        if (!result.resultBln) {
            this.mStepFailed(result.messagesStr());
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            result.resultBln = false;
            return;
        }

        this.mShowBarcodeInfo();
        this.mTryToChangeQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    private cResult mGetArticleStockForBINRst(){

        cResult result = new cResult();
        result.resultBln = true;

        Double quantityAvailable;

        if ( cPickorder.currentPickOrder.currentBranchBin == null || cPickorder.currentPickOrder.currentArticle == null) {
            this.quantityRequiredText.setVisibility(View.GONE);
            return result;
        }

        //Show required quantity
        this.quantityRequiredText.setVisibility(View.VISIBLE);

        //We alrady have this articlestock, so don't need to get it another time
        if ( this.articleStock != null) {
            quantityAvailable = cPickorder.currentPickOrder.pGetQuantityToPick(this.articleStock);

            if (quantityAvailable == 0) {
                this.quantityRequiredText.setVisibility(View.GONE);
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_stock_available));
                return result;
            }

            this.quantityRequiredText.setText( cText.pDoubleToStringStr(quantityAvailable));
            return  result;
        }

        cUserInterface.pShowGettingData();
        this.articleStock = cPickorder.currentPickOrder.currentArticle.pGetStockForBINViaWebservice(cPickorder.currentPickOrder.currentBranchBin.getBinCodeStr());
        if (articleStock == null) {
            this.quantityRequiredText.setVisibility(View.GONE);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_stock_available));
            return result;
        }

        quantityAvailable = cPickorder.currentPickOrder.pGetQuantityToPick(this.articleStock);
        if (quantityAvailable == 0) {
            this.quantityRequiredText.setVisibility(View.GONE);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_stock_available));
            return result;
        }

        this.quantityRequiredText.setText( cText.pDoubleToStringStr(quantityAvailable));

        cUserInterface.pHideGettingData();
        return  result;
    }

    private cResult mHandleUnknownBarcodeScanRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;
        result = this.mAddUnknownScanRst(pvBarcodeScan);
        return  result;
    }

    private cResult mAddUnknownScanRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //We can add a line, and we need to check with the ERP, so check, add and open it
        result = this.mAddERPArticleRst(pvBarcodeScan);
        return  result;
    }

    private cResult mHandleBarcodeScanForCurrentItemRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //Check if scanned barcode matched the current barcode
        if (cPickorderBarcode.currentPickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {
            result.resultBln = false;
            return  result;
        }

        if (cPickorder.currentPickOrder.currentArticle == null || cPickorder.currentPickOrder.currentArticle.barcodesObl == null) {
            return  result;
        }

        //We have a different barcode, so check if this barcode belong to the current article
        for (cArticleBarcode articleBarcode : cPickorder.currentPickOrder.currentArticle.barcodesObl) {
            if (articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                articleBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {

                //We have a match, try to get this barcode from the order
                cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvBarcodeScan);

                // We are done
                if (cPickorderBarcode.currentPickorderBarcode != null) {
                    result.resultBln = false;
                    return result;
                }

                //We don't have a match, so add new barcode to the order originated from article barcode
                cPickorderBarcode pickorderBarcode = new cPickorderBarcode(articleBarcode);
                cPickorderBarcode.allBarcodesObl.add(pickorderBarcode);
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;

                //Set result to false, so we know that we just can raise the quantity
                result.resultBln = false;
                return result;
            }
        }

        result.resultBln = true;
        return result;

    }

    private  cResult mCheckDestionationRst(cBarcodeScan pvBarcodeScan) {

        cResult resultRst = new cResult();
        resultRst.resultBln = true;

        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        for (cBranch branch : cBranch.allBranchesObl) {
            if (branch.getBranchStr().equalsIgnoreCase(barcodewithoutPrefix)) {
                this.scannedBranch = branch;
                return resultRst;
            }
        }

        resultRst.resultBln = false;
        resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.destination_invalid));
        return resultRst;

    }

    private void mResetCurrents(){
        cPickorderLine.currentPickOrderLine = null;
        cPickorder.currentPickOrder.currentArticle = null;
        cArticle.currentArticle = null;
        cPickorderBarcode.currentPickorderBarcode = null;
        this.scannedBarcodesObl = null;
        this.quantityScannedDbl = (double) 0;
        this.articleStock = null;
    }

    private  List<cPickorderBarcode> mSortBarcodeList(List<cPickorderBarcode> pvUnsortedBarcodeObl) {

        List<cPickorderBarcode> resultList = new ArrayList<>();

        boolean barcodeFoundBln = false;

        for (cPickorderBarcode pickorderBarcode : pvUnsortedBarcodeObl) {
            for (cPickorderBarcode resultBarcode : resultList) {
                if (resultBarcode.getBarcodeStr().equalsIgnoreCase(pickorderBarcode.getBarcodeStr())) {
                    resultBarcode.quantityHandledDbl +=  pickorderBarcode.getQuantityPerUnitOfMeasureDbl();
                    barcodeFoundBln = true;
                }
            }
            if (barcodeFoundBln) {
                barcodeFoundBln = false;
            }
            else {
                //new barcode, so add
                pickorderBarcode.quantityHandledDbl = pickorderBarcode.getQuantityPerUnitOfMeasureDbl();
                resultList.add(pickorderBarcode);
            }
        }
        return resultList;
    }

    //Region Number Broadcaster

    private final Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (pickCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (pickCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (pickCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (pickCounterMinusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedMinus(this, milliSecsLng);
        }
    };

    private final Runnable mPlusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonPlus.performClick();
            long milliSecsLng;
            if (pickCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (pickCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (pickCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (pickCounterPlusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedPlus(this, milliSecsLng);
        }
    };

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

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.pickCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.pickCounterPlusHelperInt += 1;
    }

    private void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, "", true, true);
    }

    private void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        double newQuantityDbl;


        if ( this.scannedBarcodesObl == null) {
            this.scannedBarcodesObl = new ArrayList<>();
        }

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                newQuantityDbl = pvAmountDbl;

                //Check if we would exceed amount stock available, then show message
                if (this.articleStock != null &&  newQuantityDbl > cText.pStringToDoubleDbl(this.quantityRequiredText.getText().toString())) {
                    this.mShowExtraPiecesNotAllowed();
                    return;
                }

                //Clear the barcodeStr list and refill it
                this.scannedBarcodesObl.clear();
                int countInt = 0;
                do{
                    countInt += 1;
                    //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                    this.scannedBarcodesObl.add(cPickorderBarcode.currentPickorderBarcode);
                }
                while(countInt < newQuantityDbl);

                //Update activity and Check if this line is done
                this.quantityScannedDbl = newQuantityDbl;
                this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));
                this.mFieldsInitialize();
                return;


            } else {
                newQuantityDbl = this.quantityScannedDbl + pvAmountDbl;
            }

            //Check if we would exceed amount stock available, then show message
            if (this.articleStock != null &&  newQuantityDbl > this.articleStock.getQuantityDbl()) {
                this.mShowExtraPiecesNotAllowed();
                return;
            }

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(pDoubleToStringStr(this.quantityScannedDbl));

            //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
            this.scannedBarcodesObl.add(cPickorderBarcode.currentPickorderBarcode);

            //Check if this line is done
            this.mFieldsInitialize();
            return;
        }

        //negative

        //Check if value already is zero
        if ( this.quantityScannedDbl < 1 ) {

            //If we have a decimal, correct it to zero
            if (this.quantityScannedDbl > 0 ) {
                pvAmountDbl = 0;
                pvAmountFixedBln = true;

            } else {
                cUserInterface.pDoNope(this.quantityText, true, true);
                return;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(this.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;

            //Clear the barcodeStr list and refill it
            this.scannedBarcodesObl.clear();
            int countInt = 0;
            do{
                countInt += 1;
                //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                this.scannedBarcodesObl.add(cPickorderBarcode.currentPickorderBarcode);
            }while(countInt < newQuantityDbl);

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(pDoubleToStringStr(this.quantityScannedDbl));
            return;


        }else {
            //Remove the last barcodeStr in the list
            this.scannedBarcodesObl.remove( this.scannedBarcodesObl.size()-1);
            newQuantityDbl= this.quantityScannedDbl - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            this.quantityScannedDbl = (double) 0;
        }else {
            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
        }

        this.quantityText.setText(pDoubleToStringStr(this.quantityScannedDbl));
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
    }

    private cResult mAddERPArticleRst (cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        cUserInterface.pShowGettingData();

        //Add the barcodeStr via the webservice
        if (!cPickorder.currentPickOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_barcode_unknown_ERP,pvBarcodeScan.barcodeOriginalStr));
            cUserInterface.pHideGettingData();
            return result;
        }
        //Refresh the activity
        this.mFieldsInitialize();
        cUserInterface.pHideGettingData();
        return  result;
    }

    private void mShowExtraPiecesNotAllowed(){
        this.quantityText.setText(this.quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(this.pickorderPickGeneratedContainer, cAppExtension.context.getString(R.string.number_cannot_be_higher_than_stock), null, false);
        cUserInterface.pDoNope(this.quantityText, true, true);
        cUserInterface.pDoNope(this.quantityRequiredText, false, false);
    }

    private void mSetInstructions() {

        cResult result =  new cResult();
        result.resultBln = true;

        //Start with complete
        this.imageButtonDone.setVisibility(View.VISIBLE);

        //If we don't have a current barcode, you can't close the line
        if (cPickorder.currentPickOrder.currentArticle == null) {
            this.textViewAction.setText(cAppExtension.activity.getString(R.string.message_scan_article));
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        this.textViewAction.setText(cAppExtension.activity.getString(R.string.message_scan_article_or_close_line));
        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}


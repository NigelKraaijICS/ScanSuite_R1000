package nl.icsvertex.scansuite.Activities.Move;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.IdentifierWithDestination.cIdentifierWithDestination;
import SSU_WHS.Basics.IdentifierWithDestination.cIndentifierInfoAdapter;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.R;

public class MoveMISinglepieceActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    // Region Views

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private TextView textViewIdentifierData;
    private TextView textViewInfoData;
    private RecyclerView recyclerIdentifierInfo;
    private TextView textViewDestinationData;
    private TextView textViewInstructionData;

    private cIndentifierInfoAdapter indentifierInfoAdapter;
    private cIndentifierInfoAdapter getIndentifierInfoAdapter(){
        if (this.indentifierInfoAdapter == null) {
            this.indentifierInfoAdapter = new cIndentifierInfoAdapter();
        }
        return  this.indentifierInfoAdapter;
    }

    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_move_mi_singlepiece);
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof MoveMISinglepieceActivity) {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        this.mActivityInitialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.mLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.mLeaveActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_move_mi_singlepiece));

        this.mFieldsInitialize();

        this.mSetListeners();

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

        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.textViewIdentifierData = findViewById(R.id.textViewIdentifierData);
        this.recyclerIdentifierInfo = findViewById(R.id.recyclerIdentifierInfo);
        this.textViewInfoData = findViewById(R.id.textViewInfoData);
        this.textViewDestinationData = findViewById(R.id.textViewDestinationData);
        this.textViewInstructionData = findViewById(R.id.textViewInstructionData);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {


        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_move);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setText(cUser.currentUser.currentBranch.getBranchNameStr());
        ViewCompat.setTransitionName(this.toolbarImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, cPublicDefinitions.VIEW_NAME_HEADER_TEXT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.textViewIdentifierData.setText(cAppExtension.activity.getString(R.string.questionmarks));
        this.textViewDestinationData.setText(cAppExtension.activity.getString(R.string.questionmarks));
        this.textViewInstructionData.setText(cAppExtension.activity.getString(R.string.message_scan_identifier));

        this.textViewInfoData.setVisibility(View.VISIBLE);
        this.textViewInfoData.setText(cAppExtension.activity.getString(R.string.questionmarks));
        this.recyclerIdentifierInfo.setVisibility(View.INVISIBLE);

    }

    @Override
    public void mSetListeners() {

    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pHandleScan(final cBarcodeScan pvBarcodeScan) {


        if (cIdentifierWithDestination.currentIdentifier == null) {

            // Show that we are getting data
            cUserInterface.pShowGettingData();

            new Thread(new Runnable() {
                public void run() {
                    mGetIdentifier(pvBarcodeScan);
                }
            }).start();

            return;

        }



        this.mHandleDestinationScan(pvBarcodeScan);

    }

    //End Region Public Methods

    // Region Private Methods

    private  void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIdentifierWithDestination.currentIdentifier.getDestinationStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    // End Region Private Methods

    // Region View Methods

    private void mLeaveActivity(){

        this.mResetCurrents();
        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    //Listeners

    private void mGetIdentifier(cBarcodeScan pvBarcodeScan){

        if (cIdentifierWithDestination.pGetIdentifierViaWebserviceBln(pvBarcodeScan.getBarcodeOriginalStr())) {

            this.textViewIdentifierData.setText(cIdentifierWithDestination.currentIdentifier.getIndentifierStr());
            this.mFillRecycler();

            if (!cIdentifierWithDestination.currentIdentifier.getDestinationStr().isEmpty()) {
                this.textViewDestinationData.setText(cIdentifierWithDestination.currentIdentifier.getDestinationStr());
            }
            else {
                this.textViewDestinationData.setText(cAppExtension.activity.getString(R.string.questionmarks));
            }

            this.textViewInstructionData.setText(cAppExtension.activity.getString(R.string.message_scan_destination_mi_single_piece));

        }
        else{

            this.textViewIdentifierData.setText(pvBarcodeScan.getBarcodeOriginalStr());
            this.recyclerIdentifierInfo.setVisibility(View.INVISIBLE);
            this.textViewInfoData.setVisibility(View.VISIBLE);
            this.textViewInfoData.setText(R.string.message_identifier_unknown);
            this.textViewDestinationData.setText(cAppExtension.activity.getString(R.string.questionmarks));

            this.textViewInstructionData.setText(cAppExtension.activity.getString(R.string.message_scan_identifier));
        }

        cUserInterface.pHideGettingData();

    }

    private void mFillRecycler() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewInfoData.setVisibility(View.INVISIBLE);
                recyclerIdentifierInfo.setVisibility(View.VISIBLE);
                getIndentifierInfoAdapter().pFillData();
                recyclerIdentifierInfo.setHasFixedSize(false);
                recyclerIdentifierInfo.setAdapter(getIndentifierInfoAdapter());
                recyclerIdentifierInfo.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
            }
        });

    }

    private void mHandleDestinationScan(cBarcodeScan pvBarcodeScan) {

        if (!pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cIdentifierWithDestination.currentIdentifier.getDestinationStr())) {

            if (!cIdentifierWithDestination.currentIdentifier.getDestinationStr().isEmpty()) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.destination_invalid));
                this.textViewInstructionData.setText(cAppExtension.activity.getString(R.string.message_scan_destination_mi_single_piece));
                return;
            }
            return;
        }

        cIdentifierWithDestination.currentIdentifier.destinationStr = pvBarcodeScan.getBarcodeOriginalStr();
        this.mShowSending();
        new Thread(new Runnable() {
            public void run() {
                mMoveMISinglePieceDone();
            }
        }).start();

    }

    private void mResetCurrents(){
        cIdentifierWithDestination.currentIdentifier = null;
        cMoveorder.currentMoveOrder = null;

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFieldsInitialize();
            }
        });
    }

    private  void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SENDING_TAG);
            }
        });
    }

    private  void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mShowNotSent(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    private void mMoveMISinglePieceDone(){

        cResult resultRst = new cResult();
        resultRst.resultBln = true;

        resultRst = cMoveorder.pCreateMoveOrderMIViaWebserviceRst();
        if (!resultRst.resultBln) {
            this.mShowNotSent(cAppExtension.activity.getString(R.string.error_get_moveorders_failed));
            return;
        }

        if (!cMoveorder.currentMoveOrder.pAddUnkownBarcodeAndItemVariantBln(cBarcodeScan.pFakeScan(cIdentifierWithDestination.currentIdentifier.getIndentifierStr()))) {
            this.mShowNotSent(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed));
            return;
        }

        List <cMoveorderBarcode> barcodeObl = new ArrayList<>();
        barcodeObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);

        if (!cMoveorder.currentMoveOrder.pMovePlaceMIItemHandledBln(barcodeObl)) {
            this.mShowNotSent(cAppExtension.activity.getString(R.string.couldnt_send_line));
            return;
        }

        resultRst = cMoveorder.currentMoveOrder.pHandledViaWebserviceRst();
        if (!resultRst.resultBln) {
            this.mShowNotSent(cAppExtension.activity.getString(R.string.message_action_failed));
            return;
        }

        this.mShowSent();
        cUserInterface.pShowToastMessage(cAppExtension.activity.getString(R.string.message_action_handled),R.raw.headsupsound);
        this.mResetCurrents();
    }

    // End No orders icon

    // End Region View Method

}


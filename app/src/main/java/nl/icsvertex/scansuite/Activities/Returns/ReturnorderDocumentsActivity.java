package nl.icsvertex.scansuite.Activities.Returns;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocumentAdapter;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.PagerAdapters.ReturnorderDocumentsPagerAdapter;
import nl.icsvertex.scansuite.R;

public class ReturnorderDocumentsActivity extends AppCompatActivity implements iICSDefaultActivity {


    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties
    private TextView textViewChosenOrder;
    private TextView quantityText;
    private TabLayout returnorderDocumentsTabLayout;
    private cNoSwipeViewPager returnorderDocumentsViewpager;

    private  ImageView imageButtonComments;

    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubTitle;

    public  static Fragment currentDocumentFragment ;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnorder_documents);
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            mTryToLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        mTryToLeaveActivity();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_returnorderdocuments));

        this.mFieldsInitialize();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver();

        this.pCheckForSingleDocument();
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
        this.returnorderDocumentsTabLayout = findViewById(R.id.returnorderDocumentTabLayout);
        this.returnorderDocumentsViewpager = findViewById(R.id.returnorderDocumentsViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
        this.quantityText = findViewById(R.id.quantityText);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_return);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarSubTitle.setText(cReturnorder.currentReturnOrder.getNumberOfItemsStr());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        ViewCompat.setTransitionName(this.textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);
        this.textViewChosenOrder.setText(cReturnorder.currentReturnOrder.getOrderNumberStr());
        this.returnorderDocumentsTabLayout.addTab(this.returnorderDocumentsTabLayout.newTab().setText(R.string.tab_inventorybin_todo));
        this.returnorderDocumentsTabLayout.addTab(this.returnorderDocumentsTabLayout.newTab().setText(R.string.tab_inventorybin_done));
        this.returnorderDocumentsTabLayout.addTab(this.returnorderDocumentsTabLayout.newTab().setText(R.string.tab_inventorybin_total));

        if (cBranchReason.currentBranchReason == null){
            cBranchReason.currentBranchReason = cUser.currentUser.currentBranch.pGetReasonByName(cReturnorder.currentReturnOrder.getReasonStr());
        }

        ReturnorderDocumentsPagerAdapter returnorderDocumentsPagerAdapter = new ReturnorderDocumentsPagerAdapter(this.returnorderDocumentsTabLayout.getTabCount());
        this.returnorderDocumentsViewpager.setAdapter(returnorderDocumentsPagerAdapter);

        this.returnorderDocumentsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.returnorderDocumentsTabLayout));
        this.returnorderDocumentsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                returnorderDocumentsViewpager.setCurrentItem(pvTab.getPosition());
                mChangeSelectedTab(pvTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab pvTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab pvTab) {

            }
        });
    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
    }

    @Override
    public void mInitScreen() {

    }

    public void pCheckForSingleDocument(){
        if (!cReturnorder.currentReturnOrder.getRetourMultiDocumentBln()){
            if (cReturnorderDocument.allReturnorderDocumentObl == null || cReturnorderDocument.allReturnorderDocumentObl.size() == 0) {
                return;
            }

            for (cReturnorderDocument returnorderDocument : cReturnorderDocument.allReturnorderDocumentObl) {
                cReturnorderDocument.currentReturnOrderDocument = returnorderDocument;
                this.pReturnorderDocumentSelected();
            }
        }
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvBarcodeScan.getBarcodeOriginalStr());
            }
        }).start();
    }

    public void pHandleAddDocument(final String pvDocumentStr) {
        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleAddDocument(pvDocumentStr);
            }
        }).start();
    }

    public void pHandleOrderCloseClick() {
        this.mShowOrderCloseDialog();
    }

    public void pCloseOrder(){
        mShowSending();
        new Thread(new Runnable() {
            public void run() {
                mHandleCloseOrder();
            }
        }).start();
    }

    public void pReturnorderDocumentSelected() {
        this.mOpenReturnActivity();
    }

    public void pChangeTabCounterText(String pvTextStr){
        this.quantityText.setText(pvTextStr);
    }

    public void pHandleFragmentDismissed() {
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cReturnorder.currentReturnOrder.pFeedbackCommentObl(),"");
            }
        });
    }

    private void mHandleCloseOrder(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        for (cReturnorderDocument returnorderDocument : cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl()) {
            cReturnorderDocument.currentReturnOrderDocument = returnorderDocument;
            if (!returnorderDocument.pCloseBln()) {
                this.mShowNotSend(returnorderDocument.getSourceDocumentStr() + ' ' +  cAppExtension.activity.getString(R.string.message_document_close_fail));
                return;
            }
        }

        if (cReturnorder.currentReturnOrder.linesObl().size() == 0){
            if(! cReturnorder.currentReturnOrder.pReturnorderDisposedBln()){
                this.mShowNotSend(cReturnorder.currentReturnOrder.getOrderNumberStr() + ' ' +  cAppExtension.activity.getString(R.string.message_remove_return_order));
                return;
            }
        }

        hulpResult = cReturnorder.currentReturnOrder.pOrderHandledViaWebserviceRst();

        cBranchReason.currentBranchReason = null;
        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            this.mShowSent();
            this.mStartOrderSelectActivity();
            return;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            this.mShowNotSend(hulpResult.messagesStr());
            return;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {
            mShowNotSend("");
            //If we got any comments, show them
            if (cReturnorder.currentReturnOrder.pFeedbackCommentObl() != null && cReturnorder.currentReturnOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                this.mShowCommentsFragment(cReturnorder.currentReturnOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

        }

    }

    private void mHandleScan(String pvScannedBarcodeStr){

        //Only Document scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            this.mDoUnknownScan(cAppExtension.context.getString(R.string.message_scan_return_document), pvScannedBarcodeStr);
            return;
        }

        //Strip the prefix if thats neccesary
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

        cResult hulpRst;
        hulpRst = this.mCheckAndGetSourceDocumentRst(barcodewithoutPrefix);
        //Something went wrong, so show message and return
        if (! hulpRst.resultBln) {
            this.mStepFailed(hulpRst.messagesStr());
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                pReturnorderDocumentSelected();
                // Actions to do after 0.3 seconds
            }
        }, 300);
    }

    private void mHandleAddDocument(String pvDocumentStr){
        cResult hulpRst;
        hulpRst = this.mCheckAndGetSourceDocumentRst(pvDocumentStr);
        //Something went wrong, so show message and return
        if (! hulpRst.resultBln) {
            this.mStepFailed(hulpRst.messagesStr());
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                pReturnorderDocumentSelected();
                // Actions to do after 0.3 seconds
            }
        }, 300);
    }

    private cResult mCheckAndGetSourceDocumentRst(String pvSourceDocumentStr){

        cResult result = new cResult();
        result.resultBln = true;

        //Search for the Document in current Documents
        cReturnorderDocument.currentReturnOrderDocument = cReturnorder.currentReturnOrder.pGetDocument(pvSourceDocumentStr);

        //We found a Document so we are done
        if (cReturnorderDocument.currentReturnOrderDocument  != null) {

            return result;
        }

        //We scanned a NEW Document so check of we are allowed to add a Document
        if (!cReturnorder.currentReturnOrder.isGeneratedBln() ) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_document_add_not_allowed));
            return result;
        }
        else {
            cReturnorderDocument returnorderDocument = new cReturnorderDocument(pvSourceDocumentStr);
            returnorderDocument.pInsertInDatabaseBln();
            cReturnorderDocument.currentReturnOrderDocument = returnorderDocument;
        }
        return  result;
    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                this.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));
                break;
            case 1:
                this.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));
                break;
            case 2:
                this.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));
                break;
            default:

        }
    }

    private void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mOpenReturnActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container =  cAppExtension.activity.findViewById(R.id.returnorderDocumentscontainer);

        final Intent intent = new Intent(cAppExtension.context, ReturnorderDocumentActivity.class);
        final View clickedDocument = container.findViewWithTag(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());
        final View clickedDocumentImage = container.findViewWithTag(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr() + "_IMG");
        if (clickedDocument != null &&clickedDocumentImage != null) {

            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedDocument, cPublicDefinitions.VIEW_CHOSEN_DOCUMENT),new Pair<>(clickedDocumentImage, cPublicDefinitions.VIEW_CHOSEN_DOCUMENT_IMAGE) );
                    ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
                }
            });
        }
        else {
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
        }
    }

    private void mTryToLeaveActivity() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cAppExtension.context);
        alertDialog.setTitle(R.string.message_sure_leave_screen_title);
        alertDialog.setMessage(getString(R.string.message_sure_leave_screen_text));
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pvDialogInterface, int i) {

                if (!cReturnorder.currentReturnOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return)) {
                    cUserInterface.pDoExplodingScreen(getString(R.string.error_couldnt_release_lock_order), "", true, true);
                    return;
                }
                mStartOrderSelectActivity();
            }
        });
        alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing (close the dialog)
            }
        });

        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void mStartOrderSelectActivity() {
        cBranchReason.currentBranchReason = null;
        Intent intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cReturnorder.currentReturnOrder.getOrderNumberStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mShowOrderCloseDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                cAppExtension.activity.getString(R.string.message_close_order_text), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_close),false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private void mShowSending() {
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

    private void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private void mShowNotSend(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvPositionInt) {

        if (!(pvViewHolder instanceof cReturnorderDocumentAdapter.ReturnorderDocumentViewHolder)) {
            return;
        }

        cReturnorderDocument.currentReturnOrderDocument = cReturnorderDocument.allReturnorderDocumentObl.get(pvPositionInt);

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        if (!cReturnorder.currentReturnOrder.isGeneratedBln()){
            cResult hulpRst = cReturnorder.currentReturnOrder.pResetLinesToZeroFromDocument();
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
                return;
            }
        }

        cResult hulpRst = cReturnorder.currentReturnOrder.pDeleteDetailsFromDocument();
        if (! hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        if (cAppExtension.activity instanceof  ReturnorderDocumentActivity) {
            ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
            returnorderDocumentActivity.pFillLines();
        }
    }

    //End Region Private Methods

}

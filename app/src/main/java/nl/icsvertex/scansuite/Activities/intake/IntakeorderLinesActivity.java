package nl.icsvertex.scansuite.Activities.intake;

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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Fragments.dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.intake.IntakeorderLinesToDoFragment;
import nl.icsvertex.scansuite.PagerAdapters.IntakeorderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

public class IntakeorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    private static String SENDING_TAG = "SENDING_TAG";
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views
    private TextView textViewChosenOrder;
    static private TextView quantityIntakeordersText;
    static private TabLayout intakeorderLinesTabLayout;
    static private ViewPager intakeorderLinesViewPager;

    private IntakeorderLinesPagerAdapter intakeorderLinesPagerAdapter;
    static private ImageView imageButtonComments;


    private ImageView toolbarImage;
    private TextView toolbarTitle;


    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_intakeorderlines);
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
    }

    @Override
    protected void onResume() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_intakeorderlines));

        this.mFieldsInitialize();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver();
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

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.quantityIntakeordersText = findViewById(R.id.quantityIntakeordersText);
        this.intakeorderLinesTabLayout = findViewById(R.id.intakeorderLinesTabLayout);

        this.intakeorderLinesViewPager = findViewById(R.id.intakeorderLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        this.toolbarTitle.setText(pvScreenTitleStr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        ViewCompat.setTransitionName(this.textViewChosenOrder, this.VIEW_CHOSEN_ORDER);

        this.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        this.intakeorderLinesTabLayout.addTab(this.intakeorderLinesTabLayout.newTab().setText(R.string.tab_todo));
        this.intakeorderLinesTabLayout.addTab(this.intakeorderLinesTabLayout.newTab().setText(R.string.tab_done));
        this.intakeorderLinesTabLayout.addTab(this.intakeorderLinesTabLayout.newTab().setText(R.string.tab_total));

        this.intakeorderLinesPagerAdapter = new IntakeorderLinesPagerAdapter(this.intakeorderLinesTabLayout.getTabCount());
        this.intakeorderLinesViewPager.setAdapter(this.intakeorderLinesPagerAdapter);
        this.intakeorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.intakeorderLinesTabLayout));
        this.intakeorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                intakeorderLinesViewPager.setCurrentItem(pvTab.getPosition());
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

        this.mShowComments();

        //Call this here, because this is called everytime the activiy gets shown
        this.mCheckAllDone();

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

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pChangeTabCounterText(String pvTextStr) {
        IntakeorderLinesActivity.quantityIntakeordersText.setText(pvTextStr);
    }

    public static void pIntakelineSelected(cIntakeorderMATLine pvIntakeorderMATLine) {
        cIntakeorderMATLine.currentIntakeorderMATLine = pvIntakeorderMATLine;
    }

    public static void pIntakelineToResetSelected(cIntakeorderMATLine pvIntakeorderMATLine) {
        cIntakeorderMATLine.currentIntakeorderMATLine = pvIntakeorderMATLine;
    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvLineSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            //Clear current barcode
            cIntakeorderBarcode.currentIntakeOrderBarcode = null;

            hulpResult = cIntakeorderMATLine.currentIntakeorderMATLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr());
                cIntakeorderMATLine.currentIntakeorderMATLine = null;
                return;
            }


            //we have a line to handle, so start Store activity
            if (!IntakeorderLinesActivity.mHandleStartBln()) {
                return;
            }

            IntakeorderLinesActivity.mStartStoreActivity();

        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            //Handle the BIN scan
            hulpResult = IntakeorderLinesActivity.mHandleBINScan(pvBarcodeScan.getBarcodeOriginalStr());

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            //Show correct instruction
            IntakeorderLinesToDoFragment.pSetQuickHelpText(cAppExtension.activity.getString(R.string.scan_article));

            if (!IntakeorderLinesActivity.mHandleStartBln()) {
                return;
            }

            IntakeorderLinesActivity.mStartStoreActivity();

        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            //Handle the ARTICLE scan
            hulpResult = IntakeorderLinesActivity.mHandleArticleScan(pvBarcodeScan);

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            //Build instruction string
            String instructionStr = cAppExtension.activity.getString(R.string.scan_bincode);
            instructionStr += "(" + cIntakeorder.currentIntakeOrder.currentIntakeOrderbarcode.getItemNoStr();

            if (!cIntakeorder.currentIntakeOrder.currentIntakeOrderbarcode.getVariantCodeStr().isEmpty()) {
                instructionStr += ' ' + cIntakeorder.currentIntakeOrder.currentIntakeOrderbarcode.getVariantCodeStr();
            }

            instructionStr += ")";

            //Show correct instruction
            IntakeorderLinesToDoFragment.pSetQuickHelpText(instructionStr);

            if (!IntakeorderLinesActivity.mHandleStartBln()) {
                return;
            }

            IntakeorderLinesActivity.mStartStoreActivity();


        }

        //unknown scan
        mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

    public static void pHandleLineReset() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.pResetBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_reset_line), cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt().toString(), true, true);
            return;
        }

        //Set the selected tab to 0, and after that change it back to this tab
        IntakeorderLinesActivity.intakeorderLinesViewPager.setCurrentItem(0);

    }

    public static void pDone() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClose();
            }
        }).start();

    }

    public static void pLeaveActivity() {

        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        mStartOrderSelectActivity();
        return;

    }

    //End Region Public Methods

    //Region Private Methods

    private static cResult mHandleBINScan(String pvScannedBarcodeStr) {

        cResult result = new cResult();
        result.resultBln = true;

        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

        //Search for BIN in Branch cache/via webservice
        cIntakeorder.currentIntakeOrder.currentBranchBin = cUser.currentUser.currentBranch.pGetBinByCode(pvScannedBarcodeStr);
        if (cIntakeorder.currentIntakeOrder.currentBranchBin == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_not_valid));
            return result;
        }

        //We can't add BINS and there are no lines with empty bins, so the scanned bin should be available in the lines
        if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraBinsBln() && !cIntakeorder.currentIntakeOrder.getReceiveMATEmptyBinsBln()) {
            //Search for all LINES with this BIN
            cIntakeorder.currentIntakeOrder.linesForBinObl = cIntakeorder.currentIntakeOrder.pGetLinesForBinObl(barcodewithoutPrefix);
            if (cIntakeorder.currentIntakeOrder.linesForBinObl.size() == 0) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString(R.string.message_no_lines_for_this_bin));
                return result;
            }
        }

        //BIN is known, so everything is fine
        return result;
    }

    private static cResult mHandleArticleScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //Check if this is a barcode we already know
        cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorder.currentIntakeOrder.pGetOrderBarcode(pvBarcodeScan);
        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode));
            return result;
        }

        //Get all lines to handle from database
        List<cIntakeorderMATLine> hulpObl = cIntakeorder.currentIntakeOrder.pGetLinesNotHandledFromDatabasObl();

        //Init article list
        cIntakeorder.currentIntakeOrder.linesForArticleObl = new ArrayList<>();

        //Check if we have any matching lines for this article
        for (cIntakeorderMATLine intakeorderMATLine : hulpObl) {

            if (intakeorderMATLine.getItemNoStr().equalsIgnoreCase(cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr()) &&
                    intakeorderMATLine.getVariantCodeStr().equalsIgnoreCase(cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr())) {
                cIntakeorder.currentIntakeOrder.linesForArticleObl.add((intakeorderMATLine));
            }
        }

        if (cIntakeorder.currentIntakeOrder.linesForArticleObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode));
            return result;
        }

        //Article is known and also not handled, so everything is fine
        return result;
    }

    private static boolean mTryToCloseOrderBln() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cIntakeorder.currentIntakeOrder.pHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(), "", true, true);
            return false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {

            //If we got any comments, show them
            if (cIntakeorder.currentIntakeOrder.pFeedbackCommentObl() != null && cIntakeorder.currentIntakeOrder.pFeedbackCommentObl().size() > 0) {
                //Process comments from webresult
                IntakeorderLinesActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return false;
        }

        return true;

    }

    private static void mHandleClose() {

        if (!IntakeorderLinesActivity.mTryToCloseOrderBln()) {
            return;
        }

        mStartOrderSelectActivity();

    }

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (!this.mAllLinesDoneBln()) {
            return;
        }

        // If not everything is sent, then leave
        if (!this.mCheckAndSentLinesBln(true)) {
            return;
        }

        IntakeorderLinesActivity.mShowOrderDoneFragment();
    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));
                break;
            case 1:
                IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));
                break;
            case 2:
                IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));
                break;
            default:

        }
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
            }
        });
    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private static void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private static void mShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(false);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    private Boolean mCheckAndSentLinesBln(Boolean pvShowAnimationBln) {

        final List<cIntakeorderMATLine> linesToSendObl = cIntakeorder.currentIntakeOrder.pGetLinesToSendFromDatabasObl();

        // If there is nothing to send, then we are done
        if (linesToSendObl.size() == 0) {
            return true;
        }


        if (pvShowAnimationBln) {
            mShowSending();
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> callableBln = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                // Try to send each line, if one failes then stop
                for (cIntakeorderMATLine intakeorderMATLine : linesToSendObl) {

                    //Set the current line
                    cIntakeorderMATLine.currentIntakeorderMATLine = intakeorderMATLine;

                    //Try to send the line
                    return cIntakeorderMATLine.currentIntakeorderMATLine.pHandledBln();

                }
                return false;
            }
        };


        try {
            Future<Boolean> callableResultBln = executorService.submit(callableBln);
            Boolean hulpBln = callableResultBln.get();

            if (!hulpBln) {

                if (pvShowAnimationBln) {
                    mShowNotSent();
                }

                return false;
            }

            if (pvShowAnimationBln) {
                this.mShowSent();
            }

            return true;

        } catch (InterruptedException | ExecutionException e) {

        }

        return false;


    }

    private Boolean mAllLinesDoneBln() {

        if (cIntakeorder.currentIntakeOrder.pGetLinesNotHandledFromDatabasObl().size() <= 0) {
            return true;
        }
        return false;
    }

    private static void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(cAppExtension.fragmentManager, SENDING_TAG);
            }
        });
    }

    private static void mShowNotSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation("");
            }
        }
    }

    private static void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private static void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true);
        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mTryToLeaveActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_sure_leave_screen_title),
                cAppExtension.activity.getString(R.string.message_sure_leave_screen_text), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_leave));
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });

    }

    private static void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, IntakeorderSelectActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private static Boolean mHandleStartBln() {

        //Line selected in grid, so start the activity because we already have a line
        if (cIntakeorderMATLine.currentIntakeorderMATLine != null) {
          return true;
        }

        //We only scanned a BIN
        if (cIntakeorder.currentIntakeOrder.linesForBinObl.size() > 0) {
            cIntakeorder.currentIntakeOrder.linesForBinObl = null;
            cIntakeorder.currentIntakeOrder.linesForArticleObl = null;
            return true;
        }

        //We scanned a BIN, but also an article
        if (cIntakeorder.currentIntakeOrder.linesForArticleObl.size() > 0) {


            List<cIntakeorderMATLine> hulpObl  = new ArrayList<>();

            for (cIntakeorderMATLine intakeorderMATLine : cIntakeorder.currentIntakeOrder.linesForArticleObl) {

                if (intakeorderMATLine.getBinCodeStr().equalsIgnoreCase(cIntakeorder.currentIntakeOrder.currentBranchBin.getBinCodeStr()) ||
                  intakeorderMATLine.getBinCodeHandledStr().equalsIgnoreCase(cIntakeorder.currentIntakeOrder.currentBranchBin.getBinCodeStr())) {
                    hulpObl.add(intakeorderMATLine);
                }
            }

            //Unset last selected index
            cIntakeorder.currentIntakeOrder.lastSelectedIndexInt = -1;

            if (hulpObl.size() == 1) {

                //Set last selected index
                cIntakeorderMATLine.currentIntakeorderMATLine = hulpObl.get(0);
                cIntakeorder.currentIntakeOrder.linesForBinObl = null;
                cIntakeorder.currentIntakeOrder.linesForArticleObl = null;
                return true;
            }
        }

        return false;
    }

    private static void mShowComments() {

        if (cIntakeorder.currentIntakeOrder.pCommentObl() == null || cIntakeorder.currentIntakeOrder.pCommentObl().size() == 0) {
            IntakeorderLinesActivity.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        IntakeorderLinesActivity.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        IntakeorderLinesActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
        cComment.commentsShownBln = true;
    }

    private static void mStartStoreActivity(){


        //we have a LINE to handle, so start Store activity
        Intent intent = new Intent(cAppExtension.context, IntakeOrderIntakeActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    //End Region Private Methods

}

package nl.icsvertex.scansuite.Activities.Packaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.PagerAdapters.PackagingPagerAdapter;
import nl.icsvertex.scansuite.R;

public class PackagingActivity extends AppCompatActivity implements iICSDefaultActivity {

    public static Fragment currentPackagingFragment;
    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;

    private TextView textViewChosenOrder;
    private ImageView imageButtonCloseOrder;

    private TabLayout packagingTabLayout;
    private cNoSwipeViewPager packagingViewPager;

    //End Region Private Properties


    //Region Default Methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packaging);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mActivityInitialize();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        if (pvMenuItem.getItemId() == android.R.id.home) {
            ReceiveLinesActivity.packagingHandledBln = false;
            this.pDone();
            return true;
        }
        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        ReceiveLinesActivity.packagingHandledBln = false;
        finish();
    }

    //End Region Default Methods


    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_packaging));

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

        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
        this.packagingTabLayout = findViewById(R.id.packagingTabLayout);
        this.packagingViewPager = findViewById(R.id.packagingViewpager);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
        this.toolbarTitle.setText(pvScreenTitle);
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
        this.mSetSourceNo();

        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);

        if (!cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingIntakeBln() && cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingShippedBln()) {
            this.packagingTabLayout.addTab(this.packagingTabLayout.newTab().setText(R.string.tab_out));
        }
        else

            {
            if (cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingIntakeBln()) {
                this.packagingTabLayout.addTab(this.packagingTabLayout.newTab().setText(R.string.tab_in));
            }

            if (cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingShippedBln()) {
                this.packagingTabLayout.addTab(this.packagingTabLayout.newTab().setText(R.string.tab_out));
            }
        }

        this.packagingTabLayout.addTab(this.packagingTabLayout.newTab().setText(R.string.tab_selected));

        PackagingPagerAdapter packagingPagerAdapter = new PackagingPagerAdapter(this.packagingTabLayout.getTabCount());
        this.packagingViewPager.setAdapter(packagingPagerAdapter);
        this.packagingViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.packagingTabLayout));
        this.packagingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                packagingViewPager.setCurrentItem(pvTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab pvTab) {
                cUserInterface.pKillAllSounds();
            }

            @Override
            public void onTabReselected(TabLayout.Tab pvTab) {

            }
        });
    }

    @Override
    public void mSetListeners() {
        this.mSetOrderDoneListener();

    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public  void pHandlePackagingDone(){

            mShowSending();
            new Thread(this::mPackagingDone).start();

    }

    public  void pDone(){
        this.mGoBackToIntakeAndReceiveSelectActivity();
    }

    //End Region Public Methods

    private  void mGoBackToIntakeAndReceiveSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
        startActivity(intent);
        finish();
    }

    private void mShowClosePackagingDialog(String pvRejectStr, String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_packaging_question),
                                                                                      "",
                                                                                       pvRejectStr,
                                                                                       pvAcceptStr,
                                                                                        false);

        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });

    }


    //region default private voids

    //region private voids

    private void mSetSourceNo() {
            this.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
    }

    private void mSetOrderDoneListener() {

        this.imageButtonCloseOrder.setOnClickListener(view -> {

            String acceptStr =cAppExtension.activity.getString(R.string.message_close);
            String rejectStr = cAppExtension.activity.getString(R.string.message_cancel);
            mShowClosePackagingDialog(rejectStr,acceptStr);
        });
    }

    private void mPackagingDone() {

        cResult result = cIntakeorder.currentIntakeOrder.pPackagingHandledViaWebserviceRst();

        //Something went wrong
        if (! result.resultBln) {
            this.mShowpackagingNotSent(result.messagesStr());
            return;
        }

        result = cIntakeorder.currentIntakeOrder.pReceiveHandledViaWebserviceRst();
        //Everything was fine, so we are done
        if (! result.resultBln) {
            this.mShowpackagingNotSent(result.messagesStr());
            return;
        }

        //We are done, so show we are done
        this.mShowSent();

    }

    private  void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            sendingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SENDING_TAG);
        });
    }

    private  void mShowSent() {
        ReceiveLinesActivity.packagingHandledBln = true;
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mShowpackagingNotSent(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    //endregion private voids

}

package nl.icsvertex.scansuite.Activities.General;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.cShippingAgentShipMethod;
import SSU_WHS.Basics.Users.cUser;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cUserInterface;
import SSU_WHS.ScannerLogon.cScannerLogon;
import nl.icsvertex.scansuite.Fragments.Dialogs.BranchFragment;
import nl.icsvertex.scansuite.R;

public class LoginActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    static final String BRANCHPICKERFRAGMENT_TAG = "BRANCHPICKERFRAGMENT_TAG";
    //End Region Public Properties

    //Region Private Properties
    private RecyclerView recyclerViewUsers;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private ImageView toolbarImageHelp;
    //End Region Private Properties

    //Region Default Methods
    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_login);
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {

        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public void onBackPressed() {
        this.mLeaveActivity();
    }


    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();
        this.mFindViews();
        this.mSetToolbar(getResources().getString(R.string.screentitle_login));
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver();
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
        this.toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        this.recyclerViewUsers = findViewById(R.id.recyclerViewLines);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_login);
        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarImageHelp.setVisibility(View.INVISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.mSetUserRecycler();
    }

    @Override
    public void mSetListeners() {

    }

    @Override
    public void mInitScreen() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.mLeaveActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){
        LoginActivity.pUserSelected(pvBarcodeScan, true);
        return;
    }

    public static void pUserSelected(cBarcodeScan pvBarcodeScan, Boolean pvScannedBln) {

        cUser User = cUser.pGetUserByName(pvBarcodeScan.getBarcodeOriginalStr());
        // Scanned/selected user doesn't exist, so show message and end void
        if (User == null){
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_user), pvBarcodeScan.getBarcodeOriginalStr(), true, true );
            return;
        }

        // User is known, so set current user
        cUser.currentUser = User;

        // If user is scanned, then we don't need a password, show branches fragment and return
        if (pvScannedBln == true) {
            LoginActivity.mGetAndShowBranchesForLoggedInUser();
            return;
        }

        // If user is selected, show password dialog and return
        cUserInterface.pShowpasswordDialog(cAppExtension.context.getString(R.string.password_header_default) ,cUser.currentUser.getNameStr(),true );
        return;
    }

    public static void pBranchSelected(cBranch pvBranch) {

        // Branch is known, so set current branch of current user
        cUser.currentUser.currentBranch = pvBranch;
        LoginActivity.mStartMenuActivity();
        return;
    }

    public static void pLoginSuccess(){
        cUser.currentUser.loggedInBln = true;
        LoginActivity.mGetAndShowBranchesForLoggedInUser();
        return;
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetUserRecycler() {
        this.recyclerViewUsers.setHasFixedSize(false);
        this.recyclerViewUsers.setAdapter(cUser.getUserAdapter());
        this.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private static void mGetAndShowBranchesForLoggedInUser() {

        boolean result = cUser.currentUser.pGetBranchesBln();
        if (result == true) {
            if (cUser.currentUser.branchesObl.size() == 1) {
                cUser.currentUser.currentBranch = cUser.currentUser.branchesObl.get(0);
                mStartMenuActivity();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(cPublicDefinitions.BRANCHFRAGMENT_LIST_TAG, cUser.currentUser.branchesObl);

            final BranchFragment branchFragment = new BranchFragment();
            branchFragment.setArguments(bundle);
            branchFragment.show(cAppExtension.fragmentManager, BRANCHPICKERFRAGMENT_TAG); }
    }

    private static void mStartMenuActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        cAppExtension.activity.startActivity(intent);
        return;
    }

    private void mLeaveActivity(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        this.mResetBasics();

        Intent intent = new Intent(cAppExtension.context, MainDefaultActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    private void mResetBasics(){
        cScannerLogon.scannerLoggedOnBln = false;
        cUser.usersAvailableBln = false;
        cBarcodeLayout.barcodeLayoutsAvailableBln  =false;
        cSetting.settingsAvailableBln = false;
        cShippingAgent.shippingAgentsAvailableBln = false;
        cShippingAgentService.shippingAgentServicesAvailableBln = false;
        cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln = false;
        cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln = false;
        cItemProperty.itemPropertiesAvaliableBln = false;

    }

    //End Region Private Methods

}

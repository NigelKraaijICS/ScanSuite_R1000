package nl.icsvertex.scansuite.Activities.General;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cProductFlavor;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.cShippingAgentShipMethod;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.ScannerLogon.cScannerLogon;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.BranchFragment;
import nl.icsvertex.scansuite.R;

public class LoginActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    static final String BRANCHPICKERFRAGMENT_TAG = "BRANCHPICKERFRAGMENT_TAG";
    //End Region Public Properties

    //Region Private Properties
    private static RecyclerView recyclerViewUsers;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
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

        if (cAppExtension.activity != null) {
            LoginActivity.toolbarImage = cAppExtension.activity.findViewById(R.id.toolbarImage);
            LoginActivity.toolbarTitle = cAppExtension.activity.findViewById(R.id.toolbarTitle);
            LoginActivity.recyclerViewUsers = cAppExtension.activity.findViewById(R.id.recyclerViewLines);
        }

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        LoginActivity.toolbarImage.setImageResource(R.drawable.ic_login);
        LoginActivity.toolbarTitle.setText(pvScreenTitle);
        LoginActivity.toolbarTitle.setSelected(true);

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

        if (item.getItemId() ==  android.R.id.home) {
            this.mLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){

        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            LoginActivity.pUserSelected(pvBarcodeScan, false);
            return;
        }

        LoginActivity.pUserSelected(pvBarcodeScan, true);
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
        if (pvScannedBln) {
            LoginActivity.mGetAndShowBranchesForLoggedInUser();
            return;
        }

        // If user is selected, show password dialog and return
        cUserInterface.pShowpasswordDialog(cAppExtension.context.getString(R.string.password_header_default) ,cUser.currentUser.getNameStr(),true );

    }

    public static void pBranchSelected(cBranch pvBranch) {

        // Branch is known, so set current branch of current user
        cUser.currentUser.currentBranch = pvBranch;
        LoginActivity.mStartMenuActivity();
    }

    public static void pLoginSuccess(){
        cUser.currentUser.loggedInBln = true;
        LoginActivity.mGetAndShowBranchesForLoggedInUser();

    }

    public static void pHandlePasswordFragmentDismissed(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetUserRecycler() {
        LoginActivity.recyclerViewUsers.setHasFixedSize(false);
        LoginActivity.recyclerViewUsers.setAdapter(cUser.getUserAdapter());
        LoginActivity.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private static void mGetAndShowBranchesForLoggedInUser() {

        boolean result = cUser.currentUser.pGetBranchesBln();
        if (result) {
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
        MenuActivity.refreshBln = true;
        cAppExtension.activity.startActivity(intent);
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
        cBranch.BranchesAvailableBln = false;
        cShippingAgent.shippingAgentsAvailableBln = false;
        cShippingAgentService.shippingAgentServicesAvailableBln = false;
        cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln = false;
        cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln = false;
        cItemProperty.itemPropertiesAvaliableBln = false;


    }

    //End Region Private Methods

}

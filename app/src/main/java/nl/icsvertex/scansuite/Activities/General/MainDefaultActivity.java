package nl.icsvertex.scansuite.Activities.General;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.concurrent.ExecutionException;

import ICS.Environments.cEnvironment;
import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cProGlove;
import ICS.Utils.cConnection;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cPermissions;
import ICS.Utils.cPower;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.CompositeBarcode.cCompositeBarcode;
import SSU_WHS.Basics.CustomAuthorisations.cCustomAuthorisation;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.Basics.LabelTemplate.cLabelTemplate;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroup;
import SSU_WHS.Basics.Scanners.cScanner;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.cShippingAgentShipMethod;
import SSU_WHS.Basics.StockOwner.cStockOwner;
import SSU_WHS.Basics.Translations.cTranslation;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.ScannerLogon.cScannerLogon;
import SSU_WHS.Webservice.cWebservice;
import nl.icsvertex.scansuite.Fragments.Dialogs.EnvironmentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoConnectionFragment;
import nl.icsvertex.scansuite.Fragments.Main.DateTimeFragment;
import nl.icsvertex.scansuite.Fragments.Main.HomeFragment;
import nl.icsvertex.scansuite.Fragments.Main.LanguageFragment;
import nl.icsvertex.scansuite.Fragments.Support.SupportFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.Scanning.cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_CHECKBOX;
import static ICS.Utils.Scanning.cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1TITLE_1FIELD_CHECKBOX;
import static SSU_WHS.General.cPublicDefinitions.PROGLOVE_CONNECT_ACTION;
import static SSU_WHS.General.cPublicDefinitions.SHAREDPREFERENCE_USEPROGLOVE;


public class MainDefaultActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties

    //region views
    private  ImageView imageHome;
    private  Toolbar Toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private  FrameLayout mainFramelayout;
    private  DrawerLayout menuMainDrawer;
    private  NavigationView mainmenuNavigation;


    //End region views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        //check permissions first
        cPermissions.checkPermissions(this, this);

         //set Crashlytics, otherwise Firebase wont work
        FirebaseAnalytics.getInstance(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Standard methods to initialize the Activity
        this.mActivityInitialize();

        cPower.pRegisterPowerConnectReceiver();
        cPower.pRegisterPowerLevelChangedReceiver();
        cUserInterface.pEnableScanner();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cPower.pUnregisterConnectPowerReceiver();
        cPower.pUnregisterPowerLevelChangedReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int pvRequestCodeInt, int pvResultCodeInt, Intent data) {

        super.onActivityResult(pvRequestCodeInt, pvResultCodeInt, data);
        if (pvRequestCodeInt == cPublicDefinitions.CHANGELANGUAGE_REQUESTCODE) {
            //we've changed the language, or not, who cares, but go back to language
            this.setTitle(R.string.screentitle_language);
            this.toolbarTitle.setText(R.string.screentitle_language);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFramelayout, new LanguageFragment());
            transaction.commit();
        }

        //internet set?
        if (pvRequestCodeInt == cPublicDefinitions.ACTIVITY_WIFI_SETTINGS) {

            if (!cConnection.isInternetConnectedBln()) {
                cUserInterface.pCheckAndCloseOpenDialogs();
                final NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
                noConnectionFragment.setCancelable(true);
                noConnectionFragment.show(((MainDefaultActivity) cAppExtension.context).getSupportFragmentManager(), "NOCONNECTION");
                return;
            }
            try {
                this.pLetsGetThisPartyStartedOrNot();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults
    @Override
    public void mActivityInitialize() {

        //Set App Extensions
        this.mSetAppExtensions();

        //Find all views in Activity
        this.mFindViews();

        //set environment from preferences/by QR code
        this.mSetEnviroment();

        //Try to set the serialnumber
        this.mSetSerialNumberIfPossible();

        //Set darmode
        this.pChangeDarkModus();

        // Show and set toolbar
        this.mSetToolbar(getResources().getString(R.string.screentitle_main));

        // Don't do shit
        this.mFieldsInitialize();

        // Set event listeners
        this.mSetListeners();

        // Init screen
        this.mInitScreen();

        this.mCheckProGlove();

        //Set Proglove Screen
        this.mSetProGloveScreen();
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

        this.Toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.imageHome = findViewById(R.id.imageHome);

        this.mainFramelayout = findViewById(R.id.mainFramelayout);
        this.menuMainDrawer = findViewById(R.id.menuMainDrawer);
        this.mainmenuNavigation = findViewById(R.id.mainMenuNavigation);
    }


    @Override
    public void mSetToolbar(String pvScreenTitle) {

        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarImage.setImageResource(R.drawable.ic_welcome);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubtext.setSelected(true);

        this.Toolbar.showOverflowMenu();

        setSupportActionBar(this.Toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
    }

    @Override
    public void mFieldsInitialize() {
        Boolean useProGlove = cSharedPreferences.pGetSharedPreferenceBoolean(SHAREDPREFERENCE_USEPROGLOVE, false);
        if (!useProGlove) {
            View proGloveView =  findViewById(R.id.action_proglove);
            if (proGloveView != null) {
                proGloveView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetHomeListener();
        this.mSetMenuListener();
    }

    @Override
    public void mInitScreen() {
        this.mShowHomeFragment();
    }

    //End Region iICSDefaultActivity defaults
    private void mCheckProGlove() {
        cProGlove myproglove= new cProGlove();
        if ( cSharedPreferences.pGetSharedPreferenceBoolean(SHAREDPREFERENCE_USEPROGLOVE, false)){
            if (!myproglove.pIsProgloveInstalled()) {
                cUserInterface.pShowToastMessage(getString(R.string.proglove_not_installed), R.raw.headsupsound);
            }
        }
    }
    private void mSetProGloveScreen() {
        if (cEnvironment.currentEnvironment != null){
            String proglovedata = "1|" + getResources().getString(R.string.proglove_header_scansuite) + "|" + cEnvironment.currentEnvironment.getDescriptionStr() + "|2|" + getResources().getString(R.string.proglove_on_terminal) + "|" + getResources().getString(R.string.proglove_press_begin_scanner);
            cProGlove myproglove= new cProGlove();
            myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_2FIELD_2HEADER, proglovedata, true, 0, 0);
        }
    }

    //Region Public Methods
    public void pLetsGetThisPartyStartedOrNot() throws ExecutionException {

        // If scanner had different interface version then web service, then stop
        if (!cWebservice.pWebserviceIsAvailableAndRightVersionBln()) {

            cAppExtension.activity.runOnUiThread(this::mShowHomeFragment);
            return;
        }

        // If we already have everything we need, then next fragment
        if (this.mAllBasicsAvailableBln()) {
            this.mStartLoginActivity();
        }

        // Get all basic data via webservice, then next fragment
        if (this.mGetBasicDataBln()) {
            this.mStartLoginActivity();
        }
    }

    public void pChangeDarkModus() {
                if (cSharedPreferences.getDarkModusBln()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    return;
                }

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public  void pSetChosenEnvironment() {

        Fragment FragmentFrg = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.ENVIRONMENTFRAGMENT_TAG);
        if (FragmentFrg != null) {
            DialogFragment DialogFragmentDfr = (DialogFragment) FragmentFrg;
            DialogFragmentDfr.dismiss();
        }

        toolbarSubtext.setText(cEnvironment.currentEnvironment.getDescriptionStr());
        cUserInterface.pShowSnackbarMessage(mainFramelayout, cAppExtension.context.getString(R.string.environment_set_to_parameter1, cEnvironment.currentEnvironment.getDescriptionStr()), R.raw.goodsound, false );

    }

    public void pPasswordCancelled() {
        this.mShowHomeFragment();
    }

    public  void pPasswordSuccess(){
        this.mShowEnvironmentFragment();
    }

    //End Region Public Methods

    //Region Private Methods
    private boolean mAllBasicsAvailableBln() {
        return cScannerLogon.scannerLoggedOnBln &&
                cUser.usersAvailableBln &&
                cBarcodeLayout.barcodeLayoutsAvailableBln &&
                cSetting.settingsAvailableBln &&
                cBranch.BranchesAvailableBln &&
                cShippingAgent.shippingAgentsAvailableBln &&
                cShippingAgentService.shippingAgentServicesAvailableBln &&
                cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln &&
                cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln &&
                cItemProperty.itemPropertiesAvaliableBln &&
                cPropertyGroup.propertyGroupsAvailableBln &&
                cCompositeBarcode.compositeBarcodesAvailableBln &&
                cScanner.scannersAvailableBln &&
                cCustomAuthorisation.customAutorisationsAvailableBln &&
                cTranslation.translationsAvailableBln &&
                cStockOwner.stockOwnersAvailableBln;
    }

    private boolean mGetBasicDataBln() throws ExecutionException {

        if (!cScannerLogon.pScannerLogonViaWebserviceBln()) {
            return  false;
        }

        if (!cScannerLogon.currentScannerLogon.pScannerVersionCheckBln(mainFramelayout)){
            return  false;
        }

        if (!cSetting.pGetSettingsViaWebserviceBln(true)) {
            return false;
        }

        if (!cBranch.pGetBranchesViaWebserviceBln(true)) {
            return  false;
        }

        if (!cUser.pGetUsersViaWebserviceBln(true)) {
            return false;
        }

        if (!cBarcodeLayout.pGetBarcodeLayoutsViaWebserviceBln(true)) {
            return false;
        }

        if (!cItemProperty.pGetItemPropertiesViaWebserviceBln(true)) {
            return false;
        }

        if (!cLabelTemplate.pGetLabelTemplatesViaWebserviceBln()){
            return false;
        }

        if (!cPropertyGroup.pGetPropertyGroupsViaWebserviceBln(true)) {
            return  false;
        }

        if (!cCompositeBarcode.pGetCompositeBarcodesViaWebserviceBln(true)) {
            return  false;
        }

        if (!mGetshippingInfoViawebserviceBln()) {
            return false;
        }

        if (!cScanner.pGetScannersViaWebserviceBln()) {
            return false;
        }

        if (!cCustomAuthorisation.pGetCustomAutorisationsViaWebserviceBln(true)) {
            return false;
        }

        if (!cStockOwner.pStockOwnerViaWebserviceBln(true)){
            return false;
        }

        if (!cTranslation.pGetTranslationsViaWebserviceBln()) {
            return false;
        }

        if (!cStockOwner.pStockOwnerViaWebserviceBln(true)) {
            return false;
        }

        return  mAllBasicsAvailableBln();

    }

    private boolean mGetshippingInfoViawebserviceBln() throws ExecutionException {

        cShippingAgent.pGetShippingAgentsViaWebservice(true);
        if (!cShippingAgent.shippingAgentsAvailableBln) {
            return false;
        }

        cShippingAgentService.pGetShippingAgentServicesViaWebservice(true);
        if (!cShippingAgentService.shippingAgentServicesAvailableBln) {
            return false;
        }

        cShippingAgentServiceShippingUnit.pGetShippingAgentServicesShippingUnitsViaWebservice(true);
        if (!cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln) {
            return false;
        }

        cShippingAgentShipMethod.pGetShippingAgentServicesShippingUnitsViaWebservice(true);
        return cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln;
    }

    public void pSetAddedEnvironment() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        this.mShowEnvironmentFragment();
    }

    private void mSetEnviroment(){
        cEnvironment.pSetEnvironment();
        if (cEnvironment.currentEnvironment != null) {
            toolbarSubtext.setText(cEnvironment.currentEnvironment.getDescriptionStr());
        }
    }

    private void mShowHomeFragment() {
        this.imageHome.setVisibility(View.GONE);
        this.toolbarTitle.setText(R.string.screentitle_main);
        this.toolbarImage.setImageResource(R.drawable.ic_welcome);
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFramelayout, new HomeFragment());
        transaction.commit();
    }

    private  void mShowEnvironmentFragment() {
        final EnvironmentFragment environmentFragment = new EnvironmentFragment();
        environmentFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ENVIRONMENTFRAGMENT_TAG);
    }

    //End Region Private Methods


    //Region Listeners
    private void mSetHomeListener() {
        this.imageHome.setOnClickListener(view -> mShowHomeFragment());
    }


    private void mSetMenuListener() {
        this.mainmenuNavigation.setNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {

                case R.id.action_home:
                    imageHome.setVisibility(View.GONE);
                    selectedFragment = new HomeFragment();
                    toolbarTitle.setText(R.string.screentitle_main);
                    toolbarImage.setImageResource(R.drawable.ic_welcome);
                    break;

                case R.id.action_settings:
                    imageHome.setVisibility(View.VISIBLE);
                    toolbarTitle.setText(R.string.screentitle_settings);
                    toolbarImage.setImageResource(R.drawable.ic_settings);
                    break;

                case R.id.action_support:
                    imageHome.setVisibility(View.VISIBLE);
                    selectedFragment = new SupportFragment();
                    toolbarTitle.setText(R.string.screentitle_support);
                    toolbarImage.setImageResource(R.drawable.ic_support);
                    break;

                case R.id.action_language:
                    imageHome.setVisibility(View.VISIBLE);
                    selectedFragment = new LanguageFragment();
                    toolbarTitle.setText(R.string.screentitle_language);
                    toolbarImage.setImageResource(R.drawable.ic_language);
                    break;

                case R.id.action_datetime:
                    imageHome.setVisibility(View.VISIBLE);
                    selectedFragment = new DateTimeFragment();
                    toolbarTitle.setText(R.string.screentitle_datetime);
                    toolbarImage.setImageResource(R.drawable.ic_calendar);
                    break;
                case R.id.action_environments:
                    cUserInterface.pShowpasswordDialog(cAppExtension.context.getString(R.string.password_header_default) ,cAppExtension.context.getString(R.string.dialog_password_enviroment_text),false);
                    break;

                case R.id.action_proglove:
                    imageHome.setVisibility(View.GONE);
                    cProGlove myProGlove = new cProGlove();
                    myProGlove.pShowPairGlove();
                    return true;

                case R.id.action_close_application:

                   finishAffinity();


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    }

                    System.exit(0);
                    return true;

                default:
                    imageHome.setVisibility(View.GONE);
                    selectedFragment = new HomeFragment();
                    toolbarTitle.setText(R.string.screentitle_main);
                    toolbarImage.setImageResource(R.drawable.ic_welcome);
                    break;
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFramelayout, selectedFragment);
                transaction.commit();
            }

            // deselect everything
            int size = mainmenuNavigation.getMenu().size();
            for (int i = 0; i < size; i++) {
                mainmenuNavigation.getMenu().getItem(i).setChecked(false);
            }

            // set item as selected to persist highlight
            menuItem.setChecked(true);
            // close drawer when item is tapped
            menuMainDrawer.closeDrawers();
            return true;
        });
    }

    //endregion Listeners

    //region Event handlers
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.menuMainDrawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mStartLoginActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, LoginActivity.class);
        cAppExtension.context.startActivity(intent);
    }

    private void mSetSerialNumberIfPossible(){

        if (!cSharedPreferences.getSerialNumerStr().isEmpty()) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !cDeviceInfo.getSerialnumberStr().isEmpty()) {
            cSharedPreferences.setSerialNumerStr(cDeviceInfo.getSerialnumberStr());
        }

    }

    private void mShowPairGlove() {
        Intent i = new Intent();
        i.setAction(cPublicDefinitions.PROGLOVE_CONNECT_ACTION);
        cAppExtension.context.sendBroadcast(i);
    }

}

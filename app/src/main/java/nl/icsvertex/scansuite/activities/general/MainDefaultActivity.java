package nl.icsvertex.scansuite.activities.general;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Environments.cEnvironment;
import ICS.Interfaces.iICSDefaultActivity;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.ShippingAgentsServiceShipMethods.cShippingAgentShipMethod;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.ScannerLogon.cScannerLogon;
import SSU_WHS.Webservice.cWebservice;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cConnection;
import ICS.Utils.cUserInterface;
import ICS.Utils.cPermissions;

import io.fabric.sdk.android.Fabric;
import nl.icsvertex.scansuite.fragments.dialogs.AddEnvironmentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.EnvironmentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.NoConnectionFragment;
import nl.icsvertex.scansuite.fragments.main.DateTimeFragment;
import nl.icsvertex.scansuite.fragments.main.HomeFragment;
import nl.icsvertex.scansuite.fragments.main.LanguageFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.fragments.support.SupportFragment;

public class MainDefaultActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    static final int ACTIVITY_WIFI_SETTINGS = 1;
    static final String ENVIRONMENTFRAGMENT_TAG = "ENVIRONMENTFRAGMENT_TAG";
    static final String ADDENVIRONMENTMANUALLYFRAGMENT_TAG = "ADDENVIRONMENTMANUALLYFRAGMENT_TAG";
    //End Region Public Properties

    //Region Private Properties

    //region views
    private ImageView imageHome;
    private ImageView imageEnvironment;

    private Toolbar Toolbar;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private ImageView toolbarImageHelp;
    private TextView toolbarSubtext;

    private FrameLayout mainFramelayout;
    private DrawerLayout menuMainDrawer;
    private NavigationView mainmenuNavigation;
    //End region views

    //End Region Private Properties

    //Region Default Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        // Standard methods to initialize the Activity
        this.mActivityInitialize();

        //check permissions first
        cPermissions.checkPermissions();

        //set environment from preferences/by QR code
        this.mSetEnviroment();

        //set Crashlytics, otherwise Firebase wont work
        Fabric.with(this, new Crashlytics());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int pvRequestCodeInt, int pvResultCodeInt, Intent data) {

        super.onActivityResult(pvRequestCodeInt, pvResultCodeInt, data);
        if (pvRequestCodeInt == cPublicDefinitions.CHANGELANGUAGE_REQUESTCODE) {
            //we've changed the language, or not, who cares, but go back to language
            this.setTitle(R.string.screentitle_language);
            this.toolbarTitle.setText(R.string.screentitle_language);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFramelayout, LanguageFragment.newInstance());
            transaction.commit();
        }

        //internet set?
        if (pvRequestCodeInt == ACTIVITY_WIFI_SETTINGS) {

            if (cConnection.isInternetConnectedBln() == false) {
                cUserInterface.pCheckAndCloseOpenDialogs();
                final NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
                noConnectionFragment.setCancelable(true);
                noConnectionFragment.show(((MainDefaultActivity) cAppExtension.context).getSupportFragmentManager(), "NOCONNECTION");
                return;
            }
            this.pLetsGetThisPartyStartedOrNot();
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

        // Set all view models
        this.mSetViewModels();

        this.mSetSettings();

        // Show and set toolbar
        this.mSetToolbar(getResources().getString(R.string.screentitle_main));

        // Don't do shit
        this.mFieldsInitialize();

        // Set event listeners
        this.mSetListeners();

        // Init screen
        this.mInitScreen();
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
        this.toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.imageHome = findViewById(R.id.imageHome);
        this.imageEnvironment = findViewById(R.id.imageEnvironment);

        this.mainFramelayout = findViewById(R.id.mainFramelayout);
        this.menuMainDrawer = findViewById(R.id.menuMainDrawer);
        this.mainmenuNavigation = findViewById(R.id.mainmenuNavigation);
    }

    @Override
    public void mSetViewModels() {
    }

    @Override
    public void mSetSettings() {

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {

        this.toolbarImageHelp.setVisibility(View.INVISIBLE);
        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarImage.setImageResource(R.drawable.ic_welcome);

        setSupportActionBar(this.Toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_more_vert_black_24dp);
        }
    }

    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        this.mSetHomeListener();
        this.mSetEnvironmentListener();
        this.mSetMenuListener();
    }

    @Override
    public void mInitScreen() {
        this.mSetDefaultScreen();
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods
    public void pLetsGetThisPartyStartedOrNot() {

        // If scanner had different interface version then web service, then stop
        if (cWebservice.pWebserviceIsAvailableAndRightVersionBln() == false) {
            return;
        }

        // If we already have everything we need, then next fragment
        if (this.mAllBasicsAvailableBln() == true) {
            HomeFragment.pFragmentDone();
            return;
        }

        // Get all basic data via webservice, then next fragment
        if (this.mGetBasicDataBln() == true) {
            HomeFragment.pFragmentDone();
            return;
        }
    }

    public void pSetChosenEnvironment() {
        Fragment l_FragmentFrg = getSupportFragmentManager().findFragmentByTag(ENVIRONMENTFRAGMENT_TAG);
        if (l_FragmentFrg != null) {
            DialogFragment l_DialogFragmentDfr = (DialogFragment) l_FragmentFrg;
            l_DialogFragmentDfr.dismiss();
        }

        toolbarSubtext.setText(cEnvironment.currentEnvironment.getDescriptionStr());
        cUserInterface.pShowSnackbarMessage(imageEnvironment, getString(R.string.environment_set_to_parameter1, cEnvironment.currentEnvironment.getDescriptionStr()), R.raw.goodsound, false );
        return;
    }


    public static void pPasswordSuccess(){
        MainDefaultActivity.mShowEnvironmentPicker();
        return;
    }

    //End Region Public Methods

    //Region Private Methods
    private boolean mAllBasicsAvailableBln() {
        if (cScannerLogon.scannerLoggedOnBln &&
                cUser.usersAvailableBln &&
                cBarcodeLayout.barcodeLayoutsAvailableBln &&
                cSetting.settingsAvailableBln &&
                cShippingAgent.shippingAgentsAvailableBln &&
                cShippingAgentService.shippingAgentServicesAvailableBln &&
                cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln &&
                cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln &&
                cItemProperty.itemPropertiesAvaliableBln )
        {
            return true;
        }
        else {
            return  false;
        }
    }

    private boolean mGetBasicDataBln() {

        if (cScannerLogon.pScannerLogonViaWebserviceBln() == false) {
            return  false;
        }

        if (cScannerLogon.currentScannerLogon.pScannerVersionCheckBln(mainFramelayout) == false ){
            return  false;
        }


        if (cSetting.pGetSettingsViaWebserviceBln(false) == false) {
            return false;
        }

        if ( cUser.pGetUsersViaWebserviceBln(false) == false) {
            return false;
        }

        if ( cBarcodeLayout.pGetBarcodeLayoutsViaWebserviceBln(false) == false) {
            return false;
        }

        if ( cItemProperty.pGetItemPropertiesViaWebserviceBln(  false) == false) {
            return false;
        }

        if ( mGetshippingInfoViawebserviceBln(false) == false) {
            return false;
        }

        if (mAllBasicsAvailableBln()  == true) {
            return  true;
        } else {
            return  false;
        }
    }

    private boolean mGetshippingInfoViawebserviceBln(Boolean pvRefreshBln) {

        cShippingAgent.pGetShippingAgentsViaWebserviceBln(pvRefreshBln);
        if (cShippingAgent.shippingAgentsAvailableBln == false) {
            return false;
        }

        cShippingAgentService.pGetShippingAgentServicesViaWebserviceBln(pvRefreshBln);
        if (cShippingAgentService.shippingAgentServicesAvailableBln == false) {
            return false;
        }

        cShippingAgentServiceShippingUnit.pGetShippingAgentServicesShippingUnitsViaWebserviceBln(pvRefreshBln);
        if (cShippingAgentServiceShippingUnit.shippingAgentServiceShippingUnitsAvailableBln == false) {
            return false;
        }

        cShippingAgentShipMethod.pGetShippingAgentServicesShippingUnitsViaWebserviceBln(pvRefreshBln);
        if (cShippingAgentShipMethod.ShippingAgentServiceShippingMethodsAvailableBln == false) {
            return false;
        }

        return  true;
    }

    public void setAddedEnvironment() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        this.mShowEnvironmentPicker();
    }

    public void setAddEnvironmentManually() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        this.mShowEnvironmentManually();
    }

    private void mSetEnviroment(){
        cEnvironment.pSetEnvironment();
        if (cEnvironment.currentEnvironment != null) {
            this.toolbarSubtext.setText(cEnvironment.currentEnvironment.getDescriptionStr());
        }
    }

    private void mSetDefaultScreen() {
        this.imageHome.setVisibility(View.INVISIBLE);
        this.imageEnvironment.setVisibility(View.VISIBLE);
        this.toolbarTitle.setText(R.string.screentitle_main);
        this.toolbarImage.setImageResource(R.drawable.ic_welcome);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFramelayout, HomeFragment.newInstance());
        transaction.commit();
    }

    private static void mShowEnvironmentPicker() {
        final EnvironmentFragment environmentFragment = new EnvironmentFragment();
        environmentFragment.show(cAppExtension.fragmentManager, ENVIRONMENTFRAGMENT_TAG);
    }

    private void mShowEnvironmentManually() {
        final AddEnvironmentFragment addEnvironmentFragment = new AddEnvironmentFragment();
        addEnvironmentFragment.show(cAppExtension.fragmentManager,ADDENVIRONMENTMANUALLYFRAGMENT_TAG);
    }

    //End Region Private Methods


    //Region Listeners
    private void mSetHomeListener() {
        this.imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSetDefaultScreen();
            }
        });
    }

    private void mSetEnvironmentListener() {
        this.imageEnvironment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUserInterface.pShowpasswordDialog(cAppExtension.context.getString(R.string.password_header_default) ,cAppExtension.context.getString(R.string.dialog_password_settings_text),false);
                return;
            }
        });
    }

    private void mSetMenuListener() {
        this.mainmenuNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {

                    case R.id.action_home:
                        imageHome.setVisibility(View.GONE);
                        imageEnvironment.setVisibility(View.VISIBLE);
                        selectedFragment = HomeFragment.newInstance();
                        toolbarTitle.setText(R.string.screentitle_main);
                        toolbarImage.setImageResource(R.drawable.ic_welcome);
                        break;

                    case R.id.action_settings:
                        imageHome.setVisibility(View.VISIBLE);
                        imageEnvironment.setVisibility(View.GONE);
                        toolbarTitle.setText(R.string.screentitle_settings);
                        toolbarImage.setImageResource(R.drawable.ic_settings);
                        break;

                    case R.id.action_support:
                        imageHome.setVisibility(View.VISIBLE);
                        imageEnvironment.setVisibility(View.GONE);
                        selectedFragment = SupportFragment.newInstance();
                        toolbarTitle.setText(R.string.screentitle_support);
                        toolbarImage.setImageResource(R.drawable.ic_support);
                        break;

                    case R.id.action_language:
                        imageHome.setVisibility(View.VISIBLE);
                        imageEnvironment.setVisibility(View.GONE);
                        selectedFragment = LanguageFragment.newInstance();
                        toolbarTitle.setText(R.string.screentitle_language);
                        toolbarImage.setImageResource(R.drawable.ic_language);
                        break;

                    case R.id.action_datetime:
                        imageHome.setVisibility(View.VISIBLE);
                        imageEnvironment.setVisibility(View.GONE);
                        selectedFragment = DateTimeFragment.newInstance();
                        toolbarTitle.setText(R.string.screentitle_datetime);
                        toolbarImage.setImageResource(R.drawable.ic_calendar);
                        break;

                    default:
                        imageHome.setVisibility(View.INVISIBLE);
                        imageEnvironment.setVisibility(View.VISIBLE);
                        selectedFragment = HomeFragment.newInstance();
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
            }
        });
    }


    //endregion Listeners

    //region Event handlers
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.menuMainDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

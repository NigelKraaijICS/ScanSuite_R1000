package nl.icsvertex.scansuite.activities.general;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Environments.cEnvironment;
import ICS.Interfaces.iICSDefaultActivity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutViewModel;
import ICS.Environments.cEnvironmentEntity;
import SSU_WHS.ScannerLogon.cScannerLogonEntity;
import SSU_WHS.ScannerLogon.cScannerLogonViewModel;
import SSU_WHS.Settings.cSettingsEnums;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitEntity;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitViewModel;
import SSU_WHS.ShippingAgentServices.cShippingAgentServiceEntity;
import SSU_WHS.ShippingAgentServices.cShippingAgentServiceViewModel;
import SSU_WHS.ShippingAgents.cShippingAgentEntity;
import SSU_WHS.ShippingAgents.cShippingAgentViewModel;
import SSU_WHS.ShippingAgentsServiceShipMethods.cShippingAgentServiceShipMethodEntity;
import SSU_WHS.ShippingAgentsServiceShipMethods.cShippingAgentServiceShipMethodViewModel;
import SSU_WHS.Users.cUser;
import SSU_WHS.Users.cUserEntity;
import SSU_WHS.Users.cUserViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.cAppExtension;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.cConnection;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUpdate;
import ICS.Utils.cUserInterface;

import java.util.List;

import ICS.Utils.cPermissions;

import SSU_WHS.Settings.cSettingsEntity;
import SSU_WHS.Settings.cSettingsViewModel;
import nl.icsvertex.scansuite.fragments.dialogs.AddEnvironmentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.EnvironmentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.NoConnectionFragment;
import nl.icsvertex.scansuite.fragments.main.DateTimeFragment;
import nl.icsvertex.scansuite.fragments.main.HomeFragment;
import nl.icsvertex.scansuite.fragments.main.LanguageFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.fragments.support.SupportFragment;

import static ICS.Environments.cEnvironment.pSetEnvironment;
import static ICS.Utils.cUserInterface.mShowKeyboard;
import static SSU_WHS.Webservice.cWebservice.mWebserviceIsAvailableAndRightVersion;

public class MainDefaultActivity extends AppCompatActivity implements iICSDefaultActivity {
    static final int ACTIVITY_WIFI_SETTINGS = 1;
    static final String ENVIRONMENTFRAGMENT_TAG = "ENVIRONMENTFRAGMENT_TAG";
    static final String ADDENVIRONMENTMANUALLYFRAGMENT_TAG = "ADDENVIRONMENTMANUALLYFRAGMENT_TAG";
    static final String ACTIVITYNAME = "MainDefaultActivity";


    //region ViewModels
    cScannerLogonViewModel scannerLogonViewModel;
    cSettingsViewModel settingsViewModel;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;

    cShippingAgentViewModel shippingAgentViewModel;
    cShippingAgentServiceViewModel shippingAgentServiceViewModel;
    cShippingAgentServiceShippingUnitViewModel shippingAgentServiceShippingUnitViewModel;
    cShippingAgentServiceShipMethodViewModel shippingAgentServiceShipMethodViewModel;


    cWeberrorViewModel weberrorViewModel;
    //endregion ViewModels

    //region checkBooleans
    Boolean gotScannerLogon = false;
    Boolean gotBarcodeLayout = false;
    Boolean gotSettings = false;
    Boolean gotShippingAgent = false;
    Boolean checkUsers = false;
    Boolean gotShippingAgentService = false;
    Boolean gotShippingAgentServiceShippingUnit = false;
    Boolean gotShippingAgentServiceShipMethod = false;
    //endregion checkBooleans

    //region settings
    Boolean needsShippingInfo;
    //endregion settings

    //region views
    ImageView imageHome;
    ImageView imageEnvironment;

    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;
    TextView toolbarSubtext;

    FrameLayout mainFramelayout;
    DrawerLayout menuMainDrawer;
    NavigationView mainmenuNavigation;
    //endregion views
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // Standard methods to initialize the Activity
        mActivityInitialize();

        //check permissions first
        cPermissions.checkPermissions();

        //set environment from preferences/by QR code
        mSetEnviroment();

    }
    @Override
    public void onResume() {
        super.onResume();
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
    }

    //region iICSDefaultActivity defaults
    @Override
    public void mActivityInitialize() {
        //Set App Extensions
        mSetAppExtensions();

        //Find all views in Activity
        mFindViews();

        // Set all view models
        mSetViewModels();

        mSetSettings();

        // Show and set toolbar
        mSetToolbar(getResources().getString(R.string.screentitle_main));

        // Don't do shit
        mFieldsInitialize();

        // Set event listeners
        mSetListeners();

        // Init screen
        mInitScreen();
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

        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        toolbarSubtext = findViewById(R.id.toolbarSubtext);

        imageHome = findViewById(R.id.imageHome);
        imageEnvironment = findViewById(R.id.imageEnvironment);

        mainFramelayout = findViewById(R.id.mainFramelayout);
        menuMainDrawer = findViewById(R.id.menuMainDrawer);
        mainmenuNavigation = findViewById(R.id.mainmenuNavigation);
    }

    @Override
    public void mSetViewModels() {

        scannerLogonViewModel = ViewModelProviders.of(this).get(cScannerLogonViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        shippingAgentViewModel =ViewModelProviders.of(this).get(cShippingAgentViewModel.class);
        shippingAgentServiceViewModel =ViewModelProviders.of(this).get(cShippingAgentServiceViewModel.class);
        shippingAgentServiceShippingUnitViewModel =ViewModelProviders.of(this).get(cShippingAgentServiceShippingUnitViewModel.class);
        shippingAgentServiceShipMethodViewModel =ViewModelProviders.of(this).get(cShippingAgentServiceShipMethodViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);

    }

    @Override
    public void mSetSettings() {

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        toolbarImageHelp.setVisibility(View.INVISIBLE);
        toolbarTitle.setText(pvScreenTitle);
        toolbarImage.setImageResource(R.drawable.ic_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        mSetHomeListener();
        mSetEnvironmentListener();
        mSetMenuListener();
    }

    @Override
    public void mInitScreen() {

        mSetDefaultScreen();

    }
    //endregion iICSDefaultActivity defaults

    public void mLetsGetThisPartyStartedOrNot() {

        if (!mWebserviceIsAvailableAndRightVersion()) {
            return;
        }
        //if this works, wel'll get settings
        //when we have settings, we'll get the rest of the data
        mGetBasics();
        }


    private void mGetBasics() {
        scannerLogonViewModel.getScannerLogons(true).observe(this, new Observer<List<cScannerLogonEntity>>() {
            @Override
            public void onChanged(@Nullable final List<cScannerLogonEntity> scannerLogonEntities) {

                if (scannerLogonEntities != null && scannerLogonEntities.size() > 0) {
                    gotScannerLogon = true;
                    cScannerLogonEntity scannerLogonEntity = scannerLogonEntities.get(0);
                    String currentAppversion = cDeviceInfo.getAppVersion();
                    String requiredVersion = scannerLogonEntity.getRequiredscannerversion();
                    if (!requiredVersion.equalsIgnoreCase(currentAppversion)) {
                        //update!
                        String subdir = requiredVersion.replaceAll("\\.", "");
                        String URL = cPublicDefinitions.UPDATE_BASE_URL + "/" + subdir + "/" + cPublicDefinitions.UPDATE_PACKAGE_NAME;
                        cUpdate.mUpdateBln(mainFramelayout, URL);
                        return;
                    }
                    mCheckAllDone();
                    mGetSettings();
                }
                else {
                    //todo: needs scannerlogon, doesn't
                }
            }
        });
    }

    private void mGetBarcodeLayouts() {
        //barcodeLayoutViewModel.deleteAll();
        barcodeLayoutViewModel.getBarcodeLayouts().observe(this, new Observer<List<cBarcodeLayoutEntity>>() {
            @Override
            public void onChanged(@Nullable final List<cBarcodeLayoutEntity> barcodeLayoutEntities) {
                if (barcodeLayoutEntities != null && barcodeLayoutEntities.size() > 0) {
                    gotBarcodeLayout = true;
                    mCheckAllDone();
                }
                else {
                    //todo:needs barcodelayouts, doesn't
                }
            }
        });
    }

    private void mGetShippingInfo() {
        shippingAgentViewModel.getShippingAgents(true).observe(this, new Observer<List<cShippingAgentEntity>>() {
            @Override
            public void onChanged(@Nullable List<cShippingAgentEntity> shippingAgentEntities) {
                if (shippingAgentEntities != null) {
                    if (needsShippingInfo) {
                        if (shippingAgentEntities.size() > 0) {
                            gotShippingAgent = true;
                            mCheckAllDone();
                            return;
                        }
                        else {
                            //todo: must have shippingagents, doesn't
                        }
                    }
                    else {
                        gotShippingAgent = true;
                        mCheckAllDone();
                    }
                }
            }
        });

        shippingAgentServiceViewModel.getShippingAgentServicess(true).observe(this, new Observer<List<cShippingAgentServiceEntity>>() {
            @Override
            public void onChanged(@Nullable List<cShippingAgentServiceEntity> shippingAgentServiceEntities) {
                if (shippingAgentServiceEntities != null) {
                    if (needsShippingInfo) {
                        if (shippingAgentServiceEntities.size() > 0) {
                            gotShippingAgentService = true;
                            mCheckAllDone();
                            return;
                        }
                        else {
                            //todo: must have shippingagentservicess, doesn't
                        }
                    }
                    else {
                        gotShippingAgentService = true;
                        mCheckAllDone();
                    }
                }
            }
        });

        shippingAgentServiceShippingUnitViewModel.getShippingAgentServicesShippingUnits(true).observe(this, new Observer<List<cShippingAgentServiceShippingUnitEntity>>() {
            @Override
            public void onChanged(@Nullable List<cShippingAgentServiceShippingUnitEntity> shippingAgentServiceShippingUnitEntities) {
                if (shippingAgentServiceShippingUnitEntities != null) {
                    if (needsShippingInfo) {
                        if (shippingAgentServiceShippingUnitEntities.size() > 0) {
                            gotShippingAgentServiceShippingUnit = true;
                            mCheckAllDone();
                            return;
                        }
                        else {
                            //todo: must have shippingagentservicess, doesn't
                        }
                    }
                    else {
                        gotShippingAgentServiceShippingUnit = true;
                        mCheckAllDone();
                    }
                }
            }
        });

        shippingAgentServiceShipMethodViewModel.getShippingAgentServicesShipMethods(true).observe(this, new Observer<List<cShippingAgentServiceShipMethodEntity>>() {
            @Override
            public void onChanged(@Nullable List<cShippingAgentServiceShipMethodEntity> shippingAgentServiceShipMethodEntities) {
                //no need to check, apparantly
                gotShippingAgentServiceShipMethod = true;
                mCheckAllDone();

//                if (shippingAgentServiceShipMethodEntities != null) {
//                    if (needsShippingInfo) {
//                        if (shippingAgentServiceShipMethodEntities.size() > 0) {
//                            gotShippingAgentServiceShipMethod = true;
//                            mCheckAllDone();
//                            return;
//                        }
//                        else {
//                            //todo: must have shippingagentservicesshipmethod, doesn't
//                        }
//                    }
//                    else {
//                        gotShippingAgentServiceShipMethod = true;
//                        mCheckAllDone();
//                    }
//                }
            }
        });
    }

    private void mGetSettings() {
        cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.SCANNERFIXEDBRANCH.toString()));
        //settingsViewModel.deleteAll();
        settingsViewModel.getSettings().observe(this, new Observer<List<cSettingsEntity>>() {
            @Override
            public void onChanged(@Nullable final List<cSettingsEntity> settingsEntities) {
                if (settingsEntities != null && settingsEntities.size() > 0) {
                    String needShippingInfoSetting = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_SHIPPING_SALES.toString());
                    needsShippingInfo = cText.stringToBoolean(needShippingInfoSetting, true);
                    gotSettings = true;
                    mGetBarcodeLayouts();
                    mGetShippingInfo();
                    mGetUsers();
                    mCheckAllDone();
                }
            }
        });
    }

    private void mGetUsers() {
            String l_branch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");
            cUser.getUserViewModel().gotUsers(true, l_branch).observe(cAppExtension.fragmentActivity, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable final Boolean gotUsers) {
                    checkUsers = gotUsers;
                    mCheckAllDone();
                }
            });

        }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cPublicDefinitions.CHANGELANGUAGE_REQUESTCODE) {
            //we've changed the language, or not, who cares, but go back to language
            setTitle(R.string.screentitle_language);
            toolbarTitle.setText(R.string.screentitle_language);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFramelayout, LanguageFragment.newInstance());
            transaction.commit();
        }
        //internet set?
        if (requestCode == ACTIVITY_WIFI_SETTINGS) {
            if (cConnection.isInternetConnectedBln()) {
                mLetsGetThisPartyStartedOrNot();
            }
            else {
                cUserInterface.checkAndCloseOpenDialogs();
                final NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
                noConnectionFragment.setCancelable(true);
                noConnectionFragment.show(((MainDefaultActivity) cAppExtension.context).getSupportFragmentManager(), "NOCONNECTION");
            }
        }
    }



    private void mCheckAllDone() {

        if (gotScannerLogon &&
                checkUsers &&
                gotBarcodeLayout &&
                gotSettings &&
                gotShippingAgent &&
                gotShippingAgentService &&
                gotShippingAgentServiceShippingUnit &&
                gotShippingAgentServiceShipMethod) {
            mAllDone();
        }
    }

    private void mAllDone() {
        //check for errors
//        if (cPublicDefinitions.weberrors != null) {
//            String l_message = "";
//            for (cWeberror weberror : cPublicDefinitions.weberrors) {
//                l_message += weberror.getError();
//            }
//
//        }

        for (Fragment fragment : cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).doAllDone();
            }
        }
    }

    public void setChosenEnvironment(cEnvironmentEntity environmentEntity) {
        android.support.v4.app.Fragment l_FragmentFrg = getSupportFragmentManager().findFragmentByTag(ENVIRONMENTFRAGMENT_TAG);
        if (l_FragmentFrg != null) {
            android.support.v4.app.DialogFragment l_DialogFragmentDfr = (android.support.v4.app.DialogFragment) l_FragmentFrg;
            l_DialogFragmentDfr.dismiss();
        }
        cWebservice.WEBSERVICE_URL = environmentEntity.getWebserviceurl();
        toolbarSubtext.setText(environmentEntity.getDescription());
        cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ENVIRONMENT, environmentEntity.name);
        cUserInterface.showSnackbarMessage(imageEnvironment, getString(R.string.environment_set_to_parameter1, environmentEntity.getDescription()), R.raw.goodsound, false );
    }

    @SuppressLint("InflateParams")
    private void mShowpasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_password, null);
        builder.setView(dialogView);
        builder.setMessage(getString(R.string.dialog_password_settings_text));
        builder.setTitle(getString(R.string.dialog_password_settings_title));
        final EditText editPassword = dialogView.findViewById(R.id.editPassword);
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        editPassword.setSelectAllOnFocus(true);
        editPassword.requestFocus();
        mShowKeyboard(editPassword);
        final TextView textPasswordIncorrect = dialogView.findViewById(R.id.textPasswordIncorrect);
        builder.setPositiveButton(R.string.button_login, null);
        builder.setNeutralButton(R.string.cancel, null);

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textPasswordIncorrect.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.ic_lock_question_black_24dp);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mShowKeyboard(editPassword);

                final Button buttonPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                            buttonPositive.callOnClick();
                        }
                        return true;
                    }
                });

                buttonPositive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (editPassword.getText().toString().equals(cPublicDefinitions.SETTINGS_PASSWORD)) {
                            dialog.dismiss();
                            mShowEnvironmentPicker();
                        }
                        else {
                            Animation shake = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.shake);
                            editPassword.startAnimation(shake);
                            editPassword.requestFocus();
                            editPassword.setSelection(0,editPassword.getText().toString().length());
                            textPasswordIncorrect.setText(R.string.dialog_password_incorrect);
                            cUserInterface.playSound( R.raw.headsupsound, null);
                            mShowKeyboard(editPassword);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public void setAddedEnvironment() {
        cUserInterface.checkAndCloseOpenDialogs();
        mShowEnvironmentPicker();
    }
    public void setAddEnvironmentManually() {
        cUserInterface.checkAndCloseOpenDialogs();
        mShowEnvironmentManually();
    }

    private void mSetEnviroment(){
        pSetEnvironment();
        if (cEnvironment.currentEnvironmentEntity != null) {
            toolbarSubtext.setText(cEnvironment.currentEnvironmentEntity.getDescription());
        }
    }

    //region Listeners
    private void mSetHomeListener() {
        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSetDefaultScreen();
            }
        });
    }

    private void mSetEnvironmentListener() {
        imageEnvironment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowpasswordDialog();
            }
        });
    }

    private void mSetMenuListener() {
        mainmenuNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
    private void mShowEnvironmentPicker() {
        final EnvironmentFragment environmentFragment = new EnvironmentFragment();
        environmentFragment.show(cAppExtension.fragmentManager, ENVIRONMENTFRAGMENT_TAG);
    }

    private void mShowEnvironmentManually() {
        final AddEnvironmentFragment addEnvironmentFragment = new AddEnvironmentFragment();
        addEnvironmentFragment.show(cAppExtension.fragmentManager,ADDENVIRONMENTMANUALLYFRAGMENT_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuMainDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mSetDefaultScreen() {
        imageHome.setVisibility(View.INVISIBLE);
        imageEnvironment.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.screentitle_main);
        toolbarImage.setImageResource(R.drawable.ic_welcome);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFramelayout, HomeFragment.newInstance());
        transaction.commit();
    }

}

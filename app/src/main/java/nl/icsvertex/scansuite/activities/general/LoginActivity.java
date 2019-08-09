package nl.icsvertex.scansuite.activities.general;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.Basics.Branches.cBranchEntity;
import SSU_WHS.Basics.Branches.cBranchViewModel;
import SSU_WHS.Basics.Users.cUserAdapter;
import SSU_WHS.Basics.Users.cUserEntity;
import SSU_WHS.Basics.Users.cUserViewModel;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Basics.Workplaces.cWorkplaceViewModel;
import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.dialogs.BranchFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.fragments.dialogs.WebserviceErrorFragment;

import static ICS.Utils.cUserInterface.mShowKeyboard;
import static ICS.Weberror.cWeberror.FIREBASE_ACTIVITY;
import static ICS.Weberror.cWeberror.FIREBASE_DEVICE;
import static ICS.Weberror.cWeberror.FIREBASE_ISRESULT;
import static ICS.Weberror.cWeberror.FIREBASE_ISSUCCESS;
import static ICS.Weberror.cWeberror.FIREBASE_ITEMNAME;
import static ICS.Weberror.cWeberror.FIREBASE_METHOD;
import static ICS.Weberror.cWeberror.FIREBASE_PARAMETERS;
import static ICS.Weberror.cWeberror.FIREBASE_TIMESTAMP;
import static ICS.Weberror.cWeberror.FIREBASE_URL;

public class LoginActivity extends AppCompatActivity implements iICSDefaultActivity {
    static final String BRANCHPICKERFRAGMENT_TAG = "BRANCHPICKERFRAGMENT_TAG";
    static final String ACTIVITYNAME = "LoginActivity";

    private FirebaseAnalytics mFirebaseAnalytics;

    ConstraintLayout container;

    private RecyclerView recyclerViewUsers;
    cUserAdapter userAdapter;

    cUserViewModel userViewModel;
    cWorkplaceViewModel workplaceViewModel;
    cBranchViewModel branchViewModel;
    cWeberrorViewModel weberrorViewModel;

    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;

    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivityInitialize();

        mBarcodeReceiver();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(barcodeReceiver, barcodeIntentFilter);
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void mActivityInitialize() {
        mSetAppExtensions();

        mFindViews();

        mSetViewModels();

        mSetSettings();

        mSetToolbar(getResources().getString(R.string.screentitle_login));

        mFieldsInitialize();

        mSetListeners();

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
        container = findViewById(R.id.container);
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
    }

    @Override
    public void mSetViewModels() {
        userViewModel = ViewModelProviders.of(this).get(cUserViewModel.class);
        workplaceViewModel = ViewModelProviders.of(this).get(cWorkplaceViewModel.class);
        branchViewModel = ViewModelProviders.of(this).get(cBranchViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);
    }

    @Override
    public void mSetSettings() {

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        toolbarImage.setImageResource(R.drawable.ic_login);
        toolbarTitle.setText(pvScreenTitle);
        toolbarImageHelp.setVisibility(View.INVISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        List<cUserEntity> userEntities = userViewModel.getLocalUsers();
        mSetUserRecycler(userEntities);
    }

    @Override
    public void mSetListeners() {
        mSetWeberrorOberver();
    }

    @Override
    public void mInitScreen() {

    }

    private void mBarcodeReceiver() {
        barcodeIntentFilter = new IntentFilter();
        for (String str : cBarcodeScanDefinitions.getBarcodeActions()) {
            barcodeIntentFilter.addAction(str);
        }
        for (String str : cBarcodeScanDefinitions.getBarcodeCategories()) {
            barcodeIntentFilter.addCategory(str);
        }

        barcodeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.p_GetBarcode(intent, true);
                if (barcodeStr == null) {
                    barcodeStr = "";
                }
                mHandleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy.
        registerReceiver(barcodeReceiver,barcodeIntentFilter);
    }

    private void mSetUserRecycler(List<cUserEntity> userEntities) {
        userAdapter = new cUserAdapter(cAppExtension.context);
        recyclerViewUsers.setHasFixedSize(false);
        recyclerViewUsers.setAdapter(userAdapter);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter.setUsers(userEntities);
    }

    public void setChosenUser(cUserEntity userEntity) {
        mShowpasswordDialog(userEntity);
    }

    @SuppressLint("InflateParams")
    private void mShowpasswordDialog(final cUserEntity userEntity) {
        String l_usernameStr = userEntity.getNameStr();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_password, null);
        builder.setView(dialogView);
        builder.setMessage(getString(R.string.dialog_password_text, l_usernameStr));
        builder.setTitle(getString(R.string.dialog_password_title));
        final EditText editPassword = dialogView.findViewById(R.id.editPassword);
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
                        cWebresult userLoginWrs = userViewModel.userLogonWrs(userEntity.getUsernameStr(), editPassword.getText().toString(), "");
                        if (!userLoginWrs.getSuccessBln()) {
                            ArrayList messages = new ArrayList();
                            for (String message : userLoginWrs.getResultObl()) {
                                messages.add(message);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList(cPublicDefinitions.WEBSERVICEERROR_LIST_TAG, messages);

                            WebserviceErrorFragment webserviceErrorFragment = new WebserviceErrorFragment();
                            webserviceErrorFragment.setArguments(bundle);
                            webserviceErrorFragment.setCancelable(true);
                            webserviceErrorFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WEBSERVICEERROR_TAG);
                            return;
                        }

                        if (userLoginWrs.getResultBln()) {
                            dialog.dismiss();
                            cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, userEntity.getUsernameStr());
                            mLogin();
                        }
                        else {
                            cUserInterface.doNope(editPassword, false, false);
                            cUserInterface.playSound(R.raw.headsupsound, null);
                            editPassword.requestFocus();
                            editPassword.setSelection(0,editPassword.getText().toString().length());
                            textPasswordIncorrect.setText(R.string.dialog_password_incorrect);

                            mShowKeyboard(editPassword);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void mLogin() {
        mGetBranches();
    }
    private void mSetWeberrorOberver() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        weberrorViewModel.getAllForActivityLive(ACTIVITYNAME).observe(this, new Observer<List<cWeberrorEntity>>() {
            @Override
            public void onChanged(@Nullable List<cWeberrorEntity> cWeberrorEntities) {
                if (cWeberrorEntities != null && cWeberrorEntities.size() > 0) {

                    //only if something is really wrong, else it's just a wrong login, probably
                    boolean isSuccess = true;
                    for (cWeberrorEntity weberrorEntity : cWeberrorEntities) {
                        //send to Firebase
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, FIREBASE_ITEMNAME);
                        bundle.putString(FIREBASE_ISSUCCESS, weberrorEntity.getIssucess().toString());
                        bundle.putString(FIREBASE_ISRESULT, weberrorEntity.getIsresult().toString());
                        bundle.putString(FIREBASE_ACTIVITY, weberrorEntity.getActivity());
                        bundle.putString(FIREBASE_DEVICE, weberrorEntity.getDevice());
                        bundle.putString(FIREBASE_PARAMETERS, weberrorEntity.getParameters());
                        bundle.putString(FIREBASE_METHOD, weberrorEntity.getWebmethod());
                        bundle.putString(FIREBASE_TIMESTAMP, weberrorEntity.getDatetime());
                        bundle.putString(FIREBASE_URL, cWebservice.WEBSERVICE_URL);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                        if (!weberrorEntity.getIssucess()) {
                            isSuccess = false;
                        }
                    }
                    if (!isSuccess) {
                        cUserInterface.doWebserviceError(cWeberrorEntities, false, false );
                    }
                }
                //all right, handled.
                weberrorViewModel.deleteAll();
            }
        });
    }

    private void mHandleScan(String barcodeStr) {
        //if branch is active, let that one handle the scan
        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof BranchFragment) {
                    return;
                }
            }
        }
        cUserInterface.checkAndCloseOpenDialogs();
        cUserEntity userEntity =  userViewModel.getUserByCode(barcodeStr);
        if (userEntity != null) {
            cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, userEntity.getUsernameStr());
            mLogin();
        }
        else {
            //unknown user
            doUnknownScan(getString(R.string.error_unknown_user), barcodeStr);
        }
    }
    private void doUnknownScan(String errormessage, String barcode) {
        cUserInterface.doExplodingScreen( errormessage, barcode, true, true );
    }
    public void mGetBranches() {
        String currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");

        branchViewModel.getBranchesForUser(true, currentUser).observe(this, new Observer<List<cBranchEntity>>() {
            @Override
            public void onChanged(@Nullable final List<cBranchEntity> branchEntities) {
                if (branchEntities != null) {
                    if (branchEntities.size() == 1) {
                        cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, branchEntities.get(0).getBranch());
                        gotoMenu();
                    }
                    else {
                        ArrayList<cBranchEntity> showBranches = new ArrayList<>(branchEntities.size());
                        showBranches.addAll(branchEntities);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(cPublicDefinitions.BRANCHFRAGMENT_LIST_TAG, showBranches);
                        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        final BranchFragment branchFragment = new BranchFragment();
                        branchFragment.setArguments(bundle);
                        branchFragment.show(fragmentManager, BRANCHPICKERFRAGMENT_TAG);
                    }
                }
            }
        });
    }
    public void setChosenBranch(cBranchEntity branchEntity) {
        android.support.v4.app.Fragment l_FragmentFrg = getSupportFragmentManager().findFragmentByTag(BRANCHPICKERFRAGMENT_TAG);
        if (l_FragmentFrg != null) {
            android.support.v4.app.DialogFragment l_DialogFragmentDfr = (android.support.v4.app.DialogFragment) l_FragmentFrg;
            l_DialogFragmentDfr.dismiss();
        }
        cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, branchEntity.getBranch());
        gotoMenu();
    }
    private void gotoMenu() {
        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        startActivity(intent);
    }

}

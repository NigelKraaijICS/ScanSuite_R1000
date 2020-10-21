package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsDoneFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsTotalFragment;
import nl.icsvertex.scansuite.R;


public class PasswordFragment extends DialogFragment implements iICSDefaultFragment {

    private  TextView textPasswordHeader;
    private  TextView textPasswordText;
    private  EditText editPassword;
    private  TextView textPasswordIncorrect;
    private  Button buttonCancel;
    private  Button buttonLogin;

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pShowKeyboard(editPassword);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }


    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.textPasswordHeader = getView().findViewById(R.id.textPasswordHeader);
            this.textPasswordText = getView().findViewById(R.id.textPasswordText);
            this.editPassword = getView().findViewById(R.id.editPassword);
            this.textPasswordIncorrect = getView().findViewById(R.id.textPasswordIncorrect);
            this.buttonCancel = getView().findViewById(R.id.buttonCancel);
            this.buttonLogin = getView().findViewById(R.id.buttonLogin);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.editPassword.setSelectAllOnFocus(true);
        this.editPassword.requestFocus();
        this.textPasswordIncorrect.setVisibility(View.INVISIBLE);
        Bundle args = getArguments();
        if (args != null) {
            String header = args.getString(cPublicDefinitions.PASSWORDFRAGMENT_HEADER, getString(R.string.password_header_default));
            String text = args.getString(cPublicDefinitions.PASSWORDFRAGMENT_TEXT, getString(R.string.password_text_default));
            String hint = args.getString(cPublicDefinitions.PASSWORDFRAGMENT_HINT, getString(R.string.password_hint_default));
            boolean isNumeric = args.getBoolean(cPublicDefinitions.PASSWORDFRAGMENT_ISNUMERIC, false);

            this.textPasswordHeader.setText(header);
            this.textPasswordText.setText(text);
            this.editPassword.setHint(hint);
            if (isNumeric) {
                this.editPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                this.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetCancelListener();
        this.mSetPasswordCheckListener();
        this.mSetTextChangedListener();
        this.mSetEditorActionListener();
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {
        this.editPassword.setText(pvBarcodeScan.barcodeOriginalStr);
        this.buttonLogin.callOnClick();
    }

    private void mSetPasswordCheckListener() {
        this.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.activity instanceof LoginActivity) {
                    if (cUser.currentUser.pLoginBln(editPassword.getText().toString().trim())) {
                        mRightPassword();
                    }
                    else {
                        mWrongPassword();
                    }
                }
                if (cAppExtension.activity instanceof MainDefaultActivity) {
                    if (editPassword.getText().toString().equals(cPublicDefinitions.SETTINGS_PASSWORD)) {
                        mRightPassword();
                    }
                    else {
                        mWrongPassword();
                    }
                }
                if (cAppExtension.activity instanceof InventoryorderBinsActivity) {
                    if (editPassword.getText().toString().equals(cSetting.INV_RESET_PASSWORD())) {
                        mRightPassword();
                    }
                    else {
                        mWrongPassword();
                    }
                }
                if (cAppExtension.activity instanceof InventoryorderBinActivity) {
                    if (editPassword.getText().toString().equals(cSetting.INV_RESET_PASSWORD())) {
                        mRightPassword();
                    }
                    else {
                        mWrongPassword();
                    }
                }
                if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
                    if (editPassword.getText().toString().equals(cSetting.RECEIVE_STORE_DEVIATIONS_PASSWORD())) {
                        mRightPassword();
                    }
                    else {
                        mWrongPassword();
                    }
                }

                if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity || cAppExtension.activity instanceof  ReceiveLinesActivity)  {
                    if (editPassword.getText().toString().equals(cSetting.RECEIVE_RESET_PASSWORD())) {
                        mRightPassword();
                    }
                    else {
                        mWrongPassword();
                    }
                }

            }
        });
    }

    private void mSetCancelListener() {
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.dialogFragment != null) {
                    cAppExtension.dialogFragment.dismiss();

                    if (cAppExtension.activity instanceof InventoryorderBinsActivity) {

                        if (InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsDoneFragment) {
                            InventoryBinsDoneFragment inventoryBinsDoneFragment = (InventoryBinsDoneFragment)InventoryorderBinsActivity.currentBinFragment;
                            inventoryBinsDoneFragment.pPasswordCancelled();
                        }

                        if (InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsTotalFragment) {
                            InventoryBinsTotalFragment inventoryBinsTotalFragment =  (InventoryBinsTotalFragment)InventoryorderBinsActivity.currentBinFragment;
                            inventoryBinsTotalFragment.pPasswordCancelled();
                        }
                    }

                    if (cAppExtension.activity instanceof InventoryorderBinActivity) {
                        InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
                        inventoryorderBinActivity.pPasswordCancelled();
                    }

                    if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
                        IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
                        intakeorderLinesActivity.pPasswordCancelled();
                    }

                    if (cAppExtension.dialogFragment instanceof EnvironmentFragment) {
                        EnvironmentFragment environmentFragment = (EnvironmentFragment)cAppExtension.dialogFragment;
                        environmentFragment.pHandlePasswordFragmentDismissed();
                    }



                    if (cAppExtension.activity instanceof LoginActivity) {
                        LoginActivity loginActivity = (LoginActivity)cAppExtension.activity;
                        loginActivity.pHandlePasswordFragmentDismissed();
                    }

                    if (cAppExtension.activity instanceof  ReceiveLinesActivity) {
                        ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;
                        receiveLinesActivity.pPasswordCancelled();
                    }
                    if (cAppExtension.activity instanceof  ReceiveOrderReceiveActivity) {
                        ReceiveOrderReceiveActivity receiveOrderReceiveActivity = (ReceiveOrderReceiveActivity)cAppExtension.activity;
                        receiveOrderReceiveActivity.pPasswordCancelled();
                    }

                }
            }
        });
    }

    private void mWrongPassword() {
        this.textPasswordIncorrect.setVisibility(View.VISIBLE);
        cUserInterface.pDoNope(editPassword, false, false);
        cUserInterface.pPlaySound(R.raw.headsupsound, null);
        this.editPassword.requestFocus();
        this.editPassword.setSelection(0,editPassword.getText().toString().length());
    }

    private void mRightPassword() {

        if ((getActivity()) == null) {
            return;
        }

        if (cAppExtension.activity instanceof LoginActivity) {
            LoginActivity loginActivity = (LoginActivity)cAppExtension.activity;
            loginActivity.pLoginSuccess();
            dismiss();
            return;
        }

        if (cAppExtension.activity instanceof MainDefaultActivity) {
            dismiss();
            MainDefaultActivity mainDefaultActivity = (MainDefaultActivity)cAppExtension.activity;
            mainDefaultActivity.pPasswordSuccess();
        }

        if (cAppExtension.activity instanceof InventoryorderBinsActivity) {

            if (InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsDoneFragment) {
                InventoryBinsDoneFragment inventoryBinsDoneFragment = (InventoryBinsDoneFragment)InventoryorderBinsActivity.currentBinFragment;
                inventoryBinsDoneFragment.pPasswordSuccess();
                dismiss();
            }

            if (InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsTotalFragment) {
                InventoryBinsTotalFragment inventoryBinsTotalFragment = (InventoryBinsTotalFragment)InventoryorderBinsActivity.currentBinFragment;
                inventoryBinsTotalFragment.pPasswordSuccess();
                dismiss();
            }

        }

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
            inventoryorderBinActivity.pPasswordSuccess();
            dismiss();
        }

        if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
            intakeorderLinesActivity.pPasswordSuccess();
            dismiss();
        }

        if (cAppExtension.activity instanceof ReceiveLinesActivity) {
            ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;

            if (!ReceiveLinesActivity.closeOrderClickedBln) {
                receiveLinesActivity.pPasswordSuccess();
                dismiss();
                return;
            }

            receiveLinesActivity.pDone();
            dismiss();


        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            ReceiveOrderReceiveActivity receiveOrderReceiveActivity = (ReceiveOrderReceiveActivity)cAppExtension.activity;
            receiveOrderReceiveActivity.pPasswordSuccess();
            dismiss();
        }

    }

    private void mSetTextChangedListener() {
       this.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textPasswordIncorrect.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void mSetEditorActionListener() {
        this.editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    buttonLogin.callOnClick();
                }
                return true;
            }
        });
    }
}

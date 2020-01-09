package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsDoneFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;

import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HEADER;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HINT;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_ISNUMERIC;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_TEXT;

public class PasswordFragment extends DialogFragment implements iICSDefaultFragment {

    private static TextView textPasswordHeader;
    private static TextView textPasswordText;
    private static EditText editPassword;
    private static TextView textPasswordIncorrect;
    private static Button buttonCancel;
    private static Button buttonLogin;

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
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();
    }
    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
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

        //cBarcodeScan.pRegisterWifiChangedFragmentReceiver();
    }
    @Override
    public void mFindViews() {
        if (getView() != null) {
            PasswordFragment.textPasswordHeader = getView().findViewById(R.id.textPasswordHeader);
            PasswordFragment.textPasswordText = getView().findViewById(R.id.textPasswordText);
            PasswordFragment.editPassword = getView().findViewById(R.id.editPassword);
            PasswordFragment.textPasswordIncorrect = getView().findViewById(R.id.textPasswordIncorrect);
            PasswordFragment.buttonCancel = getView().findViewById(R.id.buttonCancel);
            PasswordFragment.buttonLogin = getView().findViewById(R.id.buttonLogin);
        }
    }

    @Override
    public void mFieldsInitialize() {
        PasswordFragment.editPassword.setSelectAllOnFocus(true);
        PasswordFragment.editPassword.requestFocus();
        PasswordFragment.textPasswordIncorrect.setVisibility(View.INVISIBLE);
        Bundle args = getArguments();
        if (args != null) {
            String header = args.getString(PASSWORDFRAGMENT_HEADER, getString(R.string.password_header_default));
            String text = args.getString(PASSWORDFRAGMENT_TEXT, getString(R.string.password_text_default));
            String hint = args.getString(PASSWORDFRAGMENT_HINT, getString(R.string.password_hint_default));
            boolean isNumeric = args.getBoolean(PASSWORDFRAGMENT_ISNUMERIC, false);

            PasswordFragment.textPasswordHeader.setText(header);
            PasswordFragment.textPasswordText.setText(text);
            PasswordFragment.editPassword.setHint(hint);
            if (isNumeric) {
                PasswordFragment.editPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                PasswordFragment.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {
        PasswordFragment.editPassword.setText(pvBarcodeScan.barcodeOriginalStr);
        PasswordFragment.buttonLogin.callOnClick();
    }

    private void mSetPasswordCheckListener() {
        PasswordFragment.buttonLogin.setOnClickListener(new View.OnClickListener() {
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
            }
        });
    }

    private void mSetCancelListener() {
        PasswordFragment.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.dialogFragment != null) {
                    cAppExtension.dialogFragment.dismiss();
                    if (cAppExtension.activity instanceof InventoryorderBinsActivity) {
                        InventoryBinsDoneFragment.pPasswordCancelled();
                    }
                    if (cAppExtension.activity instanceof InventoryorderBinActivity) {
                        InventoryorderBinActivity.pPasswordCancelled();
                    }
                    if (cAppExtension.activity instanceof LoginActivity) {
                        LoginActivity.pHandlePasswordFragmentDismissed();
                    }
                }
            }
        });
    }
    private void mWrongPassword() {
        PasswordFragment.textPasswordIncorrect.setVisibility(View.VISIBLE);
        cUserInterface.pDoNope(editPassword, false, false);
        cUserInterface.pPlaySound(R.raw.headsupsound, null);
        PasswordFragment.editPassword.requestFocus();
        PasswordFragment.editPassword.setSelection(0,editPassword.getText().toString().length());
    }
    private void mRightPassword() {
        if ((getActivity()) == null) {
            return;
        }
        if (cAppExtension.activity instanceof LoginActivity) {
            LoginActivity.pLoginSuccess();
            dismiss();
            return;
        }
        if (cAppExtension.activity instanceof MainDefaultActivity) {
            dismiss();
            MainDefaultActivity.pPasswordSuccess();
        }
        if (cAppExtension.activity instanceof InventoryorderBinsActivity) {
            InventoryBinsDoneFragment.pPasswordSuccess();
            dismiss();
        }
        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            InventoryorderBinActivity.pPasswordSuccess();
            dismiss();
        }

    }
    private void mSetTextChangedListener() {
        editPassword.addTextChangedListener(new TextWatcher() {
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
        PasswordFragment.editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

package nl.icsvertex.scansuite.fragments.dialogs;

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
import ICS.Utils.cUserInterface;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.LoginActivity;
import nl.icsvertex.scansuite.activities.general.MainDefaultActivity;

import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HEADER;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HINT;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_ISNUMERIC;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_TEXT;

public class PasswordFragment extends DialogFragment implements iICSDefaultFragment {

    private TextView textPasswordHeader;
    private TextView textPasswordText;
    private EditText editPassword;
    private TextView textPasswordIncorrect;
    private Button buttonCancel;
    private Button buttonLogin;

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
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mSetViewModels();
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
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        this.editPassword.setSelectAllOnFocus(true);
        this.editPassword.requestFocus();
        this.textPasswordIncorrect.setVisibility(View.INVISIBLE);
        Bundle args = getArguments();
        if (args != null) {
            String header = args.getString(PASSWORDFRAGMENT_HEADER, getString(R.string.password_header_default));
            String text = args.getString(PASSWORDFRAGMENT_TEXT, getString(R.string.password_text_default));
            String hint = args.getString(PASSWORDFRAGMENT_HINT, getString(R.string.password_hint_default));
            boolean isNumeric = args.getBoolean(PASSWORDFRAGMENT_ISNUMERIC, false);

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
            }
        });
    }

    private void mSetCancelListener() {
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.dialogFragment != null) {
                    cAppExtension.dialogFragment.dismiss();
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
                LoginActivity.pLoginSuccess();
                dismiss();
                return;    
        }
        if (cAppExtension.activity instanceof MainDefaultActivity) {
            MainDefaultActivity.pPasswordSuccess();
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

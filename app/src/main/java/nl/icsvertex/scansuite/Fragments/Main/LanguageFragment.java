package nl.icsvertex.scansuite.Fragments.Main;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class LanguageFragment extends Fragment implements iICSDefaultFragment {

    //Region Private Properties

    private static  TextView textViewCurrentLanguage;
    private static ImageButton buttonChangeLanguage;

    //End Region Private Properties


     public LanguageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mFragmentInitialize();
    }
    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mFieldsInitialize();
        mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {
         if (getView() != null) {
             LanguageFragment.textViewCurrentLanguage = getView().findViewById(R.id.textViewCurrentLanguage);
             LanguageFragment.buttonChangeLanguage = getView().findViewById(R.id.buttonChangeLanguage);
         }
    }


    @Override
    public void mFieldsInitialize() {
        LanguageFragment.textViewCurrentLanguage.setText(Locale.getDefault().getDisplayLanguage());
    }

    @Override
    public void mSetListeners() {
        mChangeLanguageListener();
    }


    private void mChangeLanguageListener() {
        LanguageFragment.buttonChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().startActivityForResult(new Intent(Settings.ACTION_LOCALE_SETTINGS), cPublicDefinitions.CHANGELANGUAGE_REQUESTCODE);
                }
            }
        });
    }


}

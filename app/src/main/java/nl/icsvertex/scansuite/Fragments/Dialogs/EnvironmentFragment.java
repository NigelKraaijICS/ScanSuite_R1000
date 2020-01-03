package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import ICS.Environments.cEnvironment;
import ICS.Environments.cEnvironmentAdapter;
import ICS.Environments.cEnvironmentRecyclerItemTouchHelper;
import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import ICS.cAppExtension;

public class EnvironmentFragment extends DialogFragment implements cEnvironmentRecyclerItemTouchHelper.RecyclerItemTouchHelperListener, iICSDefaultFragment {


    //Region Public Properties



    //End Region Public Properties

    //Region Private Properties

    private static String ADDENVIRONMENTMANUALLYFRAGMENT_TAG = "ADDENVIRONMENTMANUALLYFRAGMENT_TAG";

    private static RecyclerView environmentRecyclerView;
    private static Button buttonClose;
    private static Button buttonAddManually;
    private static TextView textViewCurrentEnvironment;

    //End Region Private Properties


    //Region Constructor
    public EnvironmentFragment() {
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_environments, container);
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
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
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods

    // Region iICSDefaultFragment defaults

    @Override
    public void mFindViews() {

        EnvironmentFragment.environmentRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.environmentRecyclerview);
        EnvironmentFragment.buttonClose = getView().findViewById(R.id.buttonClose);
        EnvironmentFragment.buttonAddManually = getView().findViewById(R.id.buttonAddManually);
        EnvironmentFragment.textViewCurrentEnvironment = getView().findViewById(R.id.textViewCurrentEnvironment);
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
    }



    @Override
    public void mFieldsInitialize() {

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cEnvironmentRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(EnvironmentFragment.environmentRecyclerView);

        EnvironmentFragment.mGetData();
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
        this.mSetAddManuallyListener();
    }

    //End Region iICSDefaultFragment defaults


    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cEnvironment environment = new cEnvironment(pvBarcodeScan.getBarcodeOriginalStr());
        cResult hulpResult = environment.pValidateRst();

        if (!hulpResult.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return;
        }

        environment.pInsertInDatabaseBln();
        if (cEnvironment.allEnviroments.size() == 1 ) {
            cEnvironment.pSetCurrentEnviroment(environment);
        }

        EnvironmentFragment.mGetData();

    }

    private static void mGetData() {

        cEnvironment.pGetEnviromentsFromDatabase();
        EnvironmentFragment.mFillRecyclerView();

        //get current environment
        cEnvironment.currentEnvironment = cEnvironment.getDefaultEnvironment();

        if (cEnvironment.currentEnvironment != null) {
            EnvironmentFragment.textViewCurrentEnvironment.setText(cEnvironment.currentEnvironment.getDescriptionStr());
        }
        else {
            EnvironmentFragment.textViewCurrentEnvironment.setText(R.string.current_environment_unknown);
        }
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetCloseListener() {
        EnvironmentFragment.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetAddManuallyListener() {
        buttonAddManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowAddEnviromentFragment();
            }
        });
    }

    private static void mFillRecyclerView() {

        EnvironmentFragment.environmentRecyclerView.setHasFixedSize(false);
        EnvironmentFragment.environmentRecyclerView.setAdapter(cEnvironment.getEnviromementAdapter());
        EnvironmentFragment.environmentRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

    }

    //End Region Private Methids

    private static void mShowAddEnviromentFragment() {
        AddEnvironmentFragment addEnvironmentFragment = new AddEnvironmentFragment();
        addEnvironmentFragment.show(cAppExtension.fragmentManager,ADDENVIRONMENTMANUALLYFRAGMENT_TAG);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {


        if (!(pvViewHolder instanceof  cEnvironmentAdapter.EnvironmentViewHolder)) {
            return;
        }

       cEnvironment.restorableEnviroment = cEnvironment.allEnviroments.get(pvViewHolder.getAdapterPosition());

        //if it's the active environment, don't pDelete
        if (  cEnvironment.restorableEnviroment.getNameStr().equalsIgnoreCase(cEnvironment.currentEnvironment.getNameStr())) {
            cUserInterface.pShowSnackbarMessage(EnvironmentFragment.environmentRecyclerView,cAppExtension.context.getString(R.string.message_cant_delete_active_environment),R.raw.headsupsound,true);
            cEnvironment.restorableEnviroment = null;
            return;
        }

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

    }

    private void mRemoveAdapterFromFragment(){

        //Remove the item from the database
        cEnvironment.restorableEnviroment.pDeleteFromDatabaseBln();

        //Renew data, so only current enviroments are shown
        EnvironmentFragment.mGetData();

        //Show clickable snackbar message

        Snackbar snackbar = Snackbar.make(EnvironmentFragment.environmentRecyclerView, cAppExtension.context.getString(R.string.parameter1_is_removed,cEnvironment.restorableEnviroment.getNameStr()), Snackbar.LENGTH_LONG );
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Insert the removed item in the database
                cEnvironment.restorableEnviroment.pInsertInDatabaseBln();

                //Renew data, so all enviroments are shown
                EnvironmentFragment.mGetData();

                cUserInterface.pPlaySound(R.raw.hsart, 1000);
            }
        });

        snackbar.show();
        cUserInterface.pPlaySound(R.raw.trash, 0);
    }


}

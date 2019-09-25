package nl.icsvertex.scansuite.fragments.dialogs;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ICS.Environments.cEnvironment;
import ICS.Environments.cEnvironmentAdapter;
import ICS.Environments.cEnvironmentEntity;
import ICS.Environments.cEnvironmentRecyclerItemTouchHelper;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.MainDefaultActivity;

public class EnvironmentFragment extends DialogFragment implements cEnvironmentRecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    public static String environmentBarcodeNameField = "NAME".toUpperCase();
    public static String environmentBarcodeDescriptionField = "DESCRIPTION".toUpperCase();
    public static String environmentBarcodeURLField = "URL".toUpperCase();
    public static String environmentBarcodeDefaultField = "DEFAULT".toUpperCase();

    RecyclerView environmentRecyclerView;
    Button buttonClose;
    Button buttonAddManually;
    TextView textViewCurrentEnvironment;
    DialogFragment thisFragment;
    cEnvironmentAdapter environmentAdapter;
    FragmentActivity fragmentActivity;
    IntentFilter barcodeFragmentIntentFilter;
    private BroadcastReceiver barcodeFragmentReceiver;

    Context thisContext;
    private Context callerContext;

    public EnvironmentFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_environments, container);
        thisFragment = this;
        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();
        callerContext = this.getContext();
        fragmentActivity = this.getActivity();

        mFindViews();
        mBarcodeReceiver();
        mSetListeners();
        mGetData();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cEnvironmentRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(environmentRecyclerView);
    }
    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }
    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void onResume() {
        getActivity().registerReceiver(barcodeFragmentReceiver, barcodeFragmentIntentFilter);
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
    }
    private void mFindViews() {
        environmentRecyclerView = getView().findViewById(R.id.environmentRecyclerview);
        buttonClose = getView().findViewById(R.id.buttonClose);
        buttonAddManually = getView().findViewById(R.id.buttonAddManually);
        textViewCurrentEnvironment = getView().findViewById(R.id.textViewCurrentEnvironment);
    }
    private void mBarcodeReceiver() {
        barcodeFragmentIntentFilter = new IntentFilter();
        for (String str : cBarcodeScanDefinitions.getBarcodeActions()) {
            barcodeFragmentIntentFilter.addAction(str);
        }
        for (String str : cBarcodeScanDefinitions.getBarcodeCategories()) {
            barcodeFragmentIntentFilter.addCategory(str);
        }

        barcodeFragmentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.pGetBarcode(intent, false);
                if (barcodeStr == null) {
                    barcodeStr = "";
                }
                mHandleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy.
        getActivity().registerReceiver(barcodeFragmentReceiver,barcodeFragmentIntentFilter);
    }

    private void mSetListeners() {
        mSetCloseListener();
        mSetAddManuallyListener();
    }
    private void mSetCloseListener() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisFragment.dismiss();
            }
        });
    }
    private void mSetAddManuallyListener() {
        buttonAddManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callerContext instanceof MainDefaultActivity) {
                    ((MainDefaultActivity)callerContext).setAddEnvironmentManually();
                }
            }
        });
    }
    private void mGetData() {

        cEnvironment.pGetEnviromentsFromDatabase();
        mSetEnvironmentRecycler();

        //get current environment
        cEnvironment.currentEnvironment = cEnvironment.getDefaultEnvironment();

        if (cEnvironment.currentEnvironment != null) {
            textViewCurrentEnvironment.setText(cEnvironment.currentEnvironment.getDescriptionStr());
        }
        else {
            textViewCurrentEnvironment.setText(R.string.current_environment_unknown);
        }
    }

    private void mSetEnvironmentRecycler() {
        this.environmentAdapter = new cEnvironmentAdapter();
        this.environmentRecyclerView.setHasFixedSize(false);
        this.environmentRecyclerView.setAdapter(environmentAdapter);
        this.environmentRecyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

        // todo: check if this is neccesary
//        this.environmentAdapter.setEnvironments(environmentEntities);
    }
    private void mHandleScan(String barcode) {
        if (mCheckEnvironmentBarcode(barcode)) {
            String barcodeFields[] = barcode.split("\\|");

            String nameFields[] = barcodeFields[0].split("=");
            if (nameFields.length != 2) {
                mDoWrongBarcode();
                return;
            }
            String name = nameFields[1];

            String descriptionFields[] = barcodeFields[1].split("=");
            if (descriptionFields.length != 2) {
                mDoWrongBarcode();
                return;
            }
            String description = descriptionFields[1];

            String urlFields[] = barcodeFields[2].split("=");
            if (descriptionFields.length != 2) {
                mDoWrongBarcode();
                return;
            }
            String url = urlFields[1];

            String defaultFields[] = barcodeFields[3].split("=");
            if (defaultFields.length != 2) {
                mDoWrongBarcode();
                return;
            }

            cEnvironmentEntity environmentEntity = new cEnvironmentEntity();
            environmentEntity.name = name;
            environmentEntity.description = description;
            environmentEntity.webserviceurl = url;
            environmentEntity.isdefault  = false;

            cEnvironment environment = new cEnvironment(environmentEntity);
            environment.pInsertInDatabaseBln();

            if (cEnvironment.allEnviroments.size() == 1 ) {
                cEnvironment.pSetCurrentEnviroment(environment);
            }
            mGetData();
        }
        else {
            mDoWrongBarcode();
        }
    }
    private Boolean mCheckEnvironmentBarcode(String barcode) {
        String barcodeFields[] = barcode.split("\\|");
        if (barcodeFields.length != 4) {
            return false;
        }
        if (!barcodeFields[0].isEmpty() && !barcodeFields[0].toUpperCase().startsWith(environmentBarcodeNameField)) {
            return false;
        }
        if (!barcodeFields[1].isEmpty() && !barcodeFields[1].toUpperCase().startsWith(environmentBarcodeDescriptionField)) {
            return false;
        }
        if (!barcodeFields[2].isEmpty() && !barcodeFields[2].toUpperCase().startsWith(environmentBarcodeURLField)) {
            return false;
        }
        if (!barcodeFields[3].isEmpty() && !barcodeFields[3].toUpperCase().startsWith(environmentBarcodeDefaultField)) {
            return false;
        }
        return true;
    }
    private void mDoWrongBarcode() {
        cUserInterface.pDoNope(environmentRecyclerView, true, false);
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof cEnvironmentAdapter.EnvironmentViewHolder) {


            final cEnvironment selectedEnviroment = cEnvironment.allEnviroments.get(viewHolder.getAdapterPosition());
            //if it's the active environment, don't pDelete
            if (selectedEnviroment.getNameStr().equalsIgnoreCase(cEnvironment.currentEnvironment.getNameStr())) {
                Snackbar snackbar = Snackbar
                        .make(environmentRecyclerView, getString(R.string.message_cant_delete_active_environment), Snackbar.LENGTH_LONG );
                snackbar.show();
                cUserInterface.pPlaySound(R.raw.headsupsound, 0);
                return;
            }

            //remove the item from recyclerview
            environmentAdapter.removeItem(selectedEnviroment);

            //show snackbar with undo
            String l_messageStr = getString(R.string.parameter1_is_removed, selectedEnviroment.getNameStr());

            Snackbar snackbar = Snackbar
                    .make(environmentRecyclerView, l_messageStr, Snackbar.LENGTH_LONG );
            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    environmentAdapter.restoreItem(selectedEnviroment);
                    cUserInterface.pPlaySound(R.raw.hsart, 1000);
                }
            });
            snackbar.show();
            cUserInterface.pPlaySound(R.raw.trash, 0);
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        selectedEnviroment.pDeleteFromDatabaseBln();
                    }
                }
            });
        }
    }


}

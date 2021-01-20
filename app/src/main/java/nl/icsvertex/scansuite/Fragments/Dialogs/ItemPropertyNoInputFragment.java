package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValueNoInputAdapter;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import nl.icsvertex.scansuite.R;

public class ItemPropertyNoInputFragment extends DialogFragment implements iICSDefaultFragment {

   //Region Private Properties
   private  ImageView toolbarImage;
   private  TextView toolbarTitle;

    private  RecyclerView itemPropertyRecyclerview;
    private  Button buttonOK;

    private  List<cPickorderLinePropertyValue> localItemPropertyValueObl;


    private cPickorderLinePropertyValueNoInputAdapter pickorderLinePropertyAdapter;
    private cPickorderLinePropertyValueNoInputAdapter getPickorderLinePropertyAdapter(){
        if (this.pickorderLinePropertyAdapter == null) {
            this.pickorderLinePropertyAdapter = new cPickorderLinePropertyValueNoInputAdapter();
        }

        return  pickorderLinePropertyAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public ItemPropertyNoInputFragment() {
        // Required empty public constructor
    }

    public ItemPropertyNoInputFragment(List<cPickorderLinePropertyValue> pvDataObl) {
        this.localItemPropertyValueObl = pvDataObl;
    }
    //End Region Constructor

    //Region Default Methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        return pvInflater.inflate(R.layout.fragment_itemproperty_no_input, pvContainer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cUserInterface.pEnableScanner();
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();
        this.mSetItemPropertyValueRecycler();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.toolbarImage = getView().findViewById(R.id.toolbarImage);
            this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);

            this.itemPropertyRecyclerview = getView().findViewById(R.id.itemPropertyRecyclerview);
            this.buttonOK = getView().findViewById(R.id.buttonOK);
        }
    }


    @Override
    public void mFieldsInitialize() {



    }

    @Override
    public void mSetListeners() {
        this.mSetHeaderListener();
        this.mSetCloseListener();
    }

    //End Regioni ICSDefaultFragment methods

    //Region Public Methods

    //Region Public Methods


    //End Region Public Methods
    //End Region Public Methods

    //Region Private Methods
    private void mSetToolbar() {

        this.toolbarTitle.setText(cAppExtension.activity.getString(R.string.screentitle_itemproperty_no_input));
        this.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.toolbarTitle.setSingleLine(true);
        this.toolbarTitle.setMarqueeRepeatLimit(5);
        this.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        this.toolbarImage.setImageResource(R.drawable.ic_info);

    }
    private void mSetCloseListener() {
        this.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetHeaderListener() {
        this.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToBottom();
            }
        });

        this.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToTop();
                return true;
            }
        });
    }

    private void mScrollToTop() {
        this.itemPropertyRecyclerview.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (this.getPickorderLinePropertyAdapter()!= null) {
            if (this.getPickorderLinePropertyAdapter().getItemCount() > 0) {
                this.itemPropertyRecyclerview.smoothScrollToPosition(this.getPickorderLinePropertyAdapter().getItemCount() -1 );
            }
        }

    }


    private void mSetItemPropertyValueRecycler() {
        this.itemPropertyRecyclerview.setHasFixedSize(false);
        this.itemPropertyRecyclerview.setAdapter(this.getPickorderLinePropertyAdapter());
        this.itemPropertyRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.getPickorderLinePropertyAdapter().pFillData(this.localItemPropertyValueObl);
    }
    //End Region Private Methods





}

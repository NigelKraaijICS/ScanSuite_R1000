package nl.icsvertex.scansuite.fragments.pick;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import SSU_WHS.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Settings.cSettingsEnums;
import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.activities.pick.PickorderPickActivity;
import nl.icsvertex.scansuite.R;


public class PickorderLinesToPickFragment extends Fragment {

    Context thisContext;

    cSettingsViewModel settingsViewModel;

    String currentUser;
    String currentBranch;
    String chosenOrder;

    //region Settings
    Boolean pickBinManualBln;
    Boolean pickBinIsItemBln;
    //endregion Settings

    TextView textViewSelectedBin;

    //region quickhelp
    TextView quickhelpText;
    ImageView quickhelpIcon;
    ConstraintLayout quickhelpContainer;
    //endregion quickhelp


    ConstraintLayout currentLocationView;
    RecyclerView recyclerViewPickorderLinesTopick;
    private cPickorderLineViewModel pickorderLineViewModel;
    private cPickorderLineAdapter pickorderLineAdapter;

    public PickorderLinesToPickFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickorder_lines_to_pick, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        Intent intent = getActivity().getIntent();
        chosenOrder = intent.getStringExtra(cPublicDefinitions.PICKING_CHOSENORDER);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);

        m_findViews();
        m_getSettings();
        m_fieldsInitialize();
        m_setListeners();
        m_getData();
    }

    private void m_findViews() {
        recyclerViewPickorderLinesTopick = getView().findViewById(R.id.recyclerViewPickorderLinesTopick);
        textViewSelectedBin = getView().findViewById(R.id.textViewSelectedBin);
        quickhelpText = getView().findViewById(R.id.quickhelpText);
        quickhelpContainer = getView().findViewById(R.id.quickhelpContainer);
        quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
        currentLocationView = getView().findViewById(R.id.currentLocationView);
    }
    private void m_getSettings() {
        String pickBinManual = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_BIN_MANUAL.toString());
        pickBinManualBln = cText.stringToBoolean(pickBinManual, false);
        String pickBinIsItem = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_BIN_IS_ITEM.toString());
        pickBinIsItemBln = cText.stringToBoolean(pickBinIsItem, false);
    }
    private void m_fieldsInitialize() {
        if (pickBinManualBln) {
            currentLocationView.setVisibility(View.VISIBLE);
        }
        else {
            currentLocationView.setVisibility(View.INVISIBLE);
        }

        if (pickBinIsItemBln) {
            quickhelpText.setText(R.string.scan_article_or_bincode);
        }
        else {
            quickhelpText.setText(R.string.scan_bincode);
        }
    }
    private void m_setListeners() {
        setQuickHelpListener();
        if (pickBinManualBln) {
            m_setCurrentBinListener();
        }
    }
    private void setQuickHelpListener() {
        quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.doRotate(quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void mDoShrinkLeft() {

        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shrink_left);
        quickhelpText.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void mDoGrowLeft() {
        cUserInterface.playSound(R.raw.message, null);
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.grow_left);
        quickhelpText.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                quickhelpText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void setChosenPickorderLine(cPickorderLineEntity pickorderLineEntity) {
        textViewSelectedBin.setText(pickorderLineEntity.getBincode());
    }
    private void m_setCurrentBinListener() {
        currentLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisContext, PickorderPickActivity.class);
                cPickorderLineEntity chosenPickorderLine;
                chosenPickorderLine = ((PickorderLinesActivity)getActivity()).getChosenPickorderLine();
                intent.putExtra(cPublicDefinitions.PICKING_CHOSENITEMNO, chosenPickorderLine.getRecordid());
                startActivity(intent);
            }
        });
    }
    private void m_getData() {
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderLineViewModel.getNotHandledPickorderLineEntities().observe(this, new Observer<List<cPickorderLineEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> cPickorderLineEntities) {
                if (cPickorderLineEntities != null) {
                    if (cPickorderLineEntities.size()>0) {
                        currentLocationView.setVisibility(View.VISIBLE);
                    }
                    else {
                        currentLocationView.setVisibility(View.INVISIBLE);
                    }
                    m_setPickorderLineRecycler(cPickorderLineEntities);
                }

            }
        });
    }
    private void  m_setPickorderLineRecycler(List<cPickorderLineEntity> pickorderLinesNotHandledEntities) {
        pickorderLineAdapter = new cPickorderLineAdapter(thisContext);
        recyclerViewPickorderLinesTopick.setHasFixedSize(false);
        recyclerViewPickorderLinesTopick.setAdapter(pickorderLineAdapter);
        recyclerViewPickorderLinesTopick.setLayoutManager(new LinearLayoutManager(thisContext));
        pickorderLineAdapter.setPickorderLines(pickorderLinesNotHandledEntities);
    }
    private void bounceView(View view) {
        final Animation animation = AnimationUtils.loadAnimation(thisContext, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.2,20);
        animation.setInterpolator(interpolator);
        view.startAnimation(animation);
    }
}

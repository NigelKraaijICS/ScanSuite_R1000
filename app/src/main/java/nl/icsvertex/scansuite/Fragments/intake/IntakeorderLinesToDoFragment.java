package nl.icsvertex.scansuite.Fragments.intake;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.SendOrderFragment;
import nl.icsvertex.scansuite.R;


public class IntakeorderLinesToDoFragment extends  Fragment  implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static  TextView quickhelpText;
    private static ImageView quickhelpIcon;
    private ConstraintLayout quickhelpContainer;
    private ImageView imageViewStart;
    private static RecyclerView recyclerViewIntakeLinesToDo;

    //End Region Private Properties

    //Region Constructor
    public IntakeorderLinesToDoFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_intake_lines_todo, pvContainer, false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {

            IntakeorderLinesActivity.currentLineFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


    //End Region Default Methods

    //Region iICSDefaultFragment defaults
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
    }

    @Override
    public void mFindViews() {
        this.recyclerViewIntakeLinesToDo = getView().findViewById(R.id.recyclerViewIntakeLinesToDo);
        this.imageViewStart = getView().findViewById(R.id.imageViewStart);
        this.quickhelpText = getView().findViewById(R.id.quickhelpText);
        this.quickhelpContainer = getView().findViewById(R.id.quickhelpContainer);
        this.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
    }


    @Override
    public void mFieldsInitialize() {
        this.quickhelpText.setText(R.string.scan_article_or_bincode);
    }

    @Override
    public void mSetListeners() {
        this.mSetQuickHelpListener();
        this.mSetStartListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods


    public static void pSetSelectedIndexInt(final int pvIndexInt) {

        new Handler().postDelayed(new Runnable() {
                @Override
               public void run() {
                    IntakeorderLinesToDoFragment.recyclerViewIntakeLinesToDo.scrollToPosition(pvIndexInt);
                }
           },1);

    }

    public static void pSetQuickHelpText(String pvTextStr) {
        cUserInterface.pDoRotate(IntakeorderLinesToDoFragment.quickhelpIcon, 0);
        IntakeorderLinesToDoFragment.quickhelpText.setVisibility(View.VISIBLE);
        IntakeorderLinesToDoFragment.quickhelpText.setText(pvTextStr);

    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetQuickHelpListener() {
        this.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mSetStartListener() {
        this.imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntakeorderLinesActivity.pHandleScan(null,true);
            }
        });
    }


    private void mGetData() {

        List<cIntakeorderMATLine> notHandledLinesObl = cIntakeorder.currentIntakeOrder.pGetLinesNotHandledFromDatabasObl();

        if (notHandledLinesObl.size()>0) {
            this.imageViewStart.setVisibility(View.VISIBLE);
        }
        else {
            this.imageViewStart.setVisibility(View.INVISIBLE);
        }

        this.mFillRecycler(notHandledLinesObl);
}

    private void mFillRecycler(List<cIntakeorderMATLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);

        //Show the recycler view
        cIntakeorderMATLine.getIntakeorderMATLinesToDoAdapter().pFillData(pvDataObl);
        this.recyclerViewIntakeLinesToDo.setHasFixedSize(false);
        this.recyclerViewIntakeLinesToDo.setAdapter(cIntakeorderMATLine.getIntakeorderMATLinesToDoAdapter());
        this.recyclerViewIntakeLinesToDo.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewIntakeLinesToDo.setVisibility(View.VISIBLE);

        IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));
    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (IntakeorderLinesActivity.currentLineFragment != null && IntakeorderLinesActivity.currentLineFragment == this) {
            //Close no orders fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof SendOrderFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }
                this.quickhelpContainer.setVisibility(View.VISIBLE);
                return;
            }

            //Hide the recycler view
            this.recyclerViewIntakeLinesToDo.setVisibility(View.INVISIBLE);
            this.quickhelpContainer.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            SendOrderFragment fragment = new SendOrderFragment();
            fragmentTransaction.replace(R.id.fragmentIntakeLinesToDo, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));
        }
    }

    //End Region private Methods


}

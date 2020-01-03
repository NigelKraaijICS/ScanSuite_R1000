package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import nl.icsvertex.scansuite.R;


public class GettingDataFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static ImageView imageHourglass;
    private static CardView progressContainer;
    private static TextView textDots;


    //End Region Private Properties

    //Region Constructor Properties

    public GettingDataFragment() {

    }

    //End Region Constructor Properties

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_getting_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
        this.mSetAnimations();
    }


    //End Region Default Methods


    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.setCancelable(false);
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            GettingDataFragment.imageHourglass = getView().findViewById(R.id.imageHourglass);
            GettingDataFragment.progressContainer = getView().findViewById(R.id.progressContainer);
            GettingDataFragment.textDots = getView().findViewById(R.id.textDots);
        }

    }

    @Override
    public void mFieldsInitialize() {
        GettingDataFragment.progressContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void mSetListeners() {
        this.mSetHourglassImageListener();

    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

        //End Region Public Methods

    //Region Private Methods

    private void mSetAnimations() {
        this.mSetDotDotDot();
        this.mAnimateHourglass();
    }

    private void mAnimateHourglass() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                count++;
                if (count == 1)
                {
                    GettingDataFragment.imageHourglass.animate().rotation(180).start();
                }
                else if (count == 2)
                {
                    GettingDataFragment.imageHourglass.animate().rotation(0).start();
                }
                if (count == 2) {
                    count = 0;
                }

                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 1500);
    }

    private void mSetHourglassImageListener() {
        GettingDataFragment.imageHourglass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleDetails();
            }
        });
    }

     private void mToggleDetails() {
        boolean isCurrentlyShown;
         isCurrentlyShown = GettingDataFragment.progressContainer.getVisibility() == View.VISIBLE;
        if (isCurrentlyShown) {
            GettingDataFragment.progressContainer.animate().scaleY(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    GettingDataFragment.progressContainer.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
        else {
            GettingDataFragment.progressContainer.animate().scaleY(1).start();
            GettingDataFragment.progressContainer.setVisibility(View.VISIBLE);
        }
    }

    private void mSetDotDotDot() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            int count = 0;

            @Override
            public void run() {
                count++;
                if (count == 1)
                {
                    GettingDataFragment.textDots.setText(".");
                }
                else if (count == 2)
                {
                    GettingDataFragment.textDots.setText("..");
                }
                else if (count == 3)
                {
                    GettingDataFragment.textDots.setText("...");

                }
                if (count == 3) {
                    count = 0;
                }
                handler.postDelayed(this, 2 * 600);
            }
        };
        handler.postDelayed(runnable, 600);
    }

    //End Region Private Methods

}

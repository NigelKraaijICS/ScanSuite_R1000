package nl.icsvertex.scansuite.Fragments.dialogs;


import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import nl.icsvertex.scansuite.R;


public class GettingDataFragment extends DialogFragment implements iICSDefaultFragment {
    ImageView imageHourglass;
    CardView progressContainer;
    TextView textProgress;
    TextView textDots;
    Button buttonCancel;
    ConstraintLayout gettingDataContainer;

    public GettingDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_getting_data, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFragmentInitialize();
        mSetAnimations();

    }

    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mFieldsInitialize();
        mSetListeners();
    }
    @Override
    public void mFindViews() {
        gettingDataContainer = getView().findViewById(R.id.gettingDataContainer);
        imageHourglass = getView().findViewById(R.id.imageHourglass);
        progressContainer = getView().findViewById(R.id.progressContainer);
        textDots = getView().findViewById(R.id.textDots);
        textProgress = getView().findViewById(R.id.textProgress);
        buttonCancel = getView().findViewById(R.id.buttonCancel);
    }


    @Override
    public void mFieldsInitialize() {
        progressContainer.setVisibility(View.INVISIBLE);
    }
    private void mSetAnimations() {
        mSetDotDotDot();
        mAnimateHourglass();
    }
    @Override
    public void mSetListeners() {
        mSetHourglassImageListener();
        mSetCancelListener();
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
                    imageHourglass.animate().rotation(180).start();
                }
                else if (count == 2)
                {
                    imageHourglass.animate().rotation(0).start();
                }
                if (count == 2) {
                    count = 0;
                }

                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 1500);
    }
    public void mShowMessage(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textProgress.setText(message);
            }
        });
    }
    private void mSetHourglassImageListener() {
        imageHourglass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleDetails();
            }
        });
    }
    private void mSetCancelListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void mToggleDetails() {
        Boolean isCurrentlyShown;
        if (progressContainer.getVisibility() == View.VISIBLE) {
            isCurrentlyShown = true;
        }
        else {
            isCurrentlyShown = false;
        }
        if (isCurrentlyShown) {
            progressContainer.animate().scaleY(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    progressContainer.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
        else {
            progressContainer.animate().scaleY(1).start();
            progressContainer.setVisibility(View.VISIBLE);
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
                    textDots.setText(".");
                }
                else if (count == 2)
                {
                    textDots.setText("..");
                }
                else if (count == 3)
                {
                    textDots.setText("...");

                }
                if (count == 3) {
                    count = 0;
                }
                handler.postDelayed(this, 2 * 600);
            }
        };
        handler.postDelayed(runnable, 1 * 600);
    }
}

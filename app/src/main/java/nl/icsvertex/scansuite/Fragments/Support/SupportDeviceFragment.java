package nl.icsvertex.scansuite.Fragments.Support;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;

public class SupportDeviceFragment extends Fragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static TextView textViewDeviceManufacturer;
    private static TextView textViewDeviceBrand;
    private static TextView textViewDeviceModel;
    private static TextView textViewSerialnumber;
    private static TextView textViewAndroidversion;
    private static TextView textViewBatterypercent;
    private static  TextView textViewBatteryCharging;
    private static TextView textViewBatterypercentOverImage;
    private static ImageView imageViewBattery;
    private static ImageView imageIsCharging;
    private static TextView textChargeState;
    private static ImageView imageViewBatteryRefresh;
    private static AnimationDrawable batteryAnimation;

    //End Region Private Properties


    public SupportDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_device, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }
    @Override
    public void onResume() {
        super.onResume();
        this.mFragmentInitialize();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            SupportDeviceFragment.textViewDeviceManufacturer = getView().findViewById(R.id.textViewDeviceManufacturer);
            SupportDeviceFragment.textViewDeviceBrand = getView().findViewById(R.id.textViewDeviceBrand);
            SupportDeviceFragment.textViewDeviceModel = getView().findViewById(R.id.textViewDeviceModel);
            SupportDeviceFragment.textViewSerialnumber = getView().findViewById(R.id.textViewSerialnumber);
            SupportDeviceFragment.textViewAndroidversion = getView().findViewById(R.id.textViewAndroidversion);
            SupportDeviceFragment.textViewBatterypercent = getView().findViewById(R.id.textViewBatterypercent);
            SupportDeviceFragment.textViewBatteryCharging = getView().findViewById(R.id.textViewBatteryCharging);
            SupportDeviceFragment.textViewBatterypercentOverImage = getView().findViewById(R.id.textViewBatterypercentOverImage);
            SupportDeviceFragment.imageViewBattery = getView().findViewById(R.id.imageViewBattery);
            SupportDeviceFragment.imageIsCharging = getView().findViewById(R.id.imageIsCharging);
            SupportDeviceFragment.textChargeState = getView().findViewById(R.id.textChargeState);
            SupportDeviceFragment.imageViewBatteryRefresh = getView().findViewById(R.id.imageViewBatteryRefresh);
        }
    }

    @Override
    public void mFieldsInitialize() {
        SupportDeviceFragment.textViewDeviceManufacturer.setText(cDeviceInfo.getDeviceManufacturer());
        SupportDeviceFragment.textViewDeviceBrand.setText(cDeviceInfo.getDeviceBrand());
        SupportDeviceFragment.textViewDeviceModel.setText(cDeviceInfo.getDeviceModel());
        SupportDeviceFragment.textViewSerialnumber.setText(cDeviceInfo.getSerialnumberStr());
        SupportDeviceFragment.textViewAndroidversion.setText(cDeviceInfo.getAndroidVersionStr());
        SupportDeviceFragment.mSetBatteryInfo();
    }

    @Override
    public void mSetListeners() {
        this.mSetBatteryRefreshListener();
    }

    private void mSetBatteryRefreshListener() {
        SupportDeviceFragment.imageViewBatteryRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(imageViewBatteryRefresh,0);
                mSetBatteryInfo();
            }
        });
    }

    public static void pPowerChanged() {
        SupportDeviceFragment.mSetBatteryInfo();
    }

    public static void pBatteryLevelChanged() {
        try {
        SupportDeviceFragment.textViewBatterypercentOverImage.setText(cDeviceInfo.getPercentageStr());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private static void mSetBatteryInfo() {
        try {
            SupportDeviceFragment.textChargeState.setText(cDeviceInfo.getChargingStatusString());
            SupportDeviceFragment.textViewBatteryCharging.setText(cDeviceInfo.getChargingStatusString());
            SupportDeviceFragment.textViewBatteryCharging.setVisibility(View.GONE);
            SupportDeviceFragment.textViewBatterypercent.setText(cDeviceInfo.getChargingStr());
            SupportDeviceFragment.textViewBatterypercent.setVisibility(View.GONE);
            SupportDeviceFragment.textViewBatterypercentOverImage.setText(cDeviceInfo.getPercentageStr());
            SupportDeviceFragment.mSetBatteryIcon();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private static void mSetBatteryIcon() {

        if (cDeviceInfo.isCharging()) {
            SupportDeviceFragment.imageIsCharging.setVisibility(View.VISIBLE);
             mAnimateCharging();
            return;
        }

        SupportDeviceFragment.imageIsCharging.setVisibility(View.INVISIBLE);
        if (cDeviceInfo.getBatteryChargePct() < 40) {
            SupportDeviceFragment.imageViewBattery.setImageResource(R.drawable.ic_battery25);
            return;
        }

        if (cDeviceInfo.getBatteryChargePct() < 65) {
            SupportDeviceFragment.imageViewBattery.setImageResource(R.drawable.ic_battery50);
            return;
        }

        if (cDeviceInfo.getBatteryChargePct() < 90) {
            SupportDeviceFragment.imageViewBattery.setImageResource(R.drawable.ic_battery75);
            return;
        }

        SupportDeviceFragment.imageViewBattery.setImageResource(R.drawable.ic_battery100);
    }
    private static void mAnimateCharging() {
        SupportDeviceFragment.imageViewBattery.setImageResource(R.drawable.battery_charging);
        SupportDeviceFragment.batteryAnimation = (AnimationDrawable) imageViewBattery.getDrawable();
        SupportDeviceFragment.batteryAnimation.start();
    }


}

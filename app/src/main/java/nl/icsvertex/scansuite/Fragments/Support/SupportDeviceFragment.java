package nl.icsvertex.scansuite.Fragments.Support;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.R;

public class SupportDeviceFragment extends Fragment implements iICSDefaultFragment {
    TextView textViewDeviceManufacturer;
    TextView textViewDeviceBrand;
    TextView textViewDeviceModel;
    TextView textViewSerialnumber;
    TextView textViewAndroidversion;
    TextView textViewBatterypercent;
    TextView textViewBatteryCharging;
    TextView textViewBatterypercentOverImage;
    ImageView imageViewBattery;
    ImageView imageIsCharging;
    TextView textChargeState;
    ImageView imageViewBatteryRefresh;
    int batteryChargePercent;
    AnimationDrawable batteryAnimation;
    private static SupportDeviceFragment instance;
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
        instance = this;
        mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        mFindViews();

        mFieldsInitialize();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        textViewDeviceManufacturer = getView().findViewById(R.id.textViewDeviceManufacturer);
        textViewDeviceBrand = getView().findViewById(R.id.textViewDeviceBrand);
        textViewDeviceModel = getView().findViewById(R.id.textViewDeviceModel);
        textViewSerialnumber = getView().findViewById(R.id.textViewSerialnumber);
        textViewAndroidversion = getView().findViewById(R.id.textViewAndroidversion);
        textViewBatterypercent = getView().findViewById(R.id.textViewBatterypercent);
        textViewBatteryCharging = getView().findViewById(R.id.textViewBatteryCharging);
        textViewBatterypercentOverImage = getView().findViewById(R.id.textViewBatterypercentOverImage);
        imageViewBattery = getView().findViewById(R.id.imageViewBattery);
        imageIsCharging = getView().findViewById(R.id.imageIsCharging);
        textChargeState = getView().findViewById(R.id.textChargeState);
        imageViewBatteryRefresh = getView().findViewById(R.id.imageViewBatteryRefresh);
    }

    @Override
    public void mFieldsInitialize() {
        textViewDeviceManufacturer.setText(cDeviceInfo.getDeviceManufacturer());
        textViewDeviceBrand.setText(cDeviceInfo.getDeviceBrand());
        textViewDeviceModel.setText(cDeviceInfo.getDeviceModel());
        textViewSerialnumber.setText(cDeviceInfo.getSerialnumberStr());
        String androidVersion = cDeviceInfo.getAndroidBuildVersion() + " - " + cDeviceInfo.getFriendlyVersionName();
        textViewAndroidversion.setText(androidVersion);
        mSetBatteryInfo();
    }

    @Override
    public void mSetListeners() {
        mSetBatteryRefreshListener();
    }
    private void mSetBatteryRefreshListener() {
        imageViewBatteryRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(imageViewBatteryRefresh,0);
                mSetBatteryInfo();
            }
        });
    }
    public void mPowerChanged() {
        mSetBatteryInfo();
    }

    public void mBatteryLevelChanged() {
        mSetBatteryPercent();
    }
    private void mSetBatteryPercent() {
        String percentage = cDeviceInfo.getBatteryChargePct() + "%";
        textViewBatterypercentOverImage.setText(percentage);
    }

    public static SupportDeviceFragment getInstance() {
        return instance;
    }

    private void mSetBatteryInfo() {
        batteryChargePercent = cDeviceInfo.getBatteryChargePct();
        textChargeState.setText(cDeviceInfo.getChargingStatusString());
        textViewBatteryCharging.setText(cDeviceInfo.getChargingStatusString());
        textViewBatteryCharging.setVisibility(View.GONE);
        textViewBatterypercent.setText(getString(R.string.battery_at_parameter1_percent, cText.pIntToStringStr(cDeviceInfo.getBatteryChargePct())));
        textViewBatterypercent.setVisibility(View.GONE);
        String percentage = cDeviceInfo.getBatteryChargePct() + "%";
        textViewBatterypercentOverImage.setText(percentage);
        mSetBatteryIcon();
    }
    private void mSetBatteryIcon() {
        if (cDeviceInfo.isCharging()) {
            imageIsCharging.setVisibility(View.VISIBLE);

            mAnimateCharging();
            return;
        }
        imageIsCharging.setVisibility(View.INVISIBLE);
        if (cDeviceInfo.getBatteryChargePct() < 40) {
            imageViewBattery.setImageResource(R.drawable.ic_battery25);
            return;
        }
        if (cDeviceInfo.getBatteryChargePct() < 65) {
            imageViewBattery.setImageResource(R.drawable.ic_battery50);
            return;
        }
        if (cDeviceInfo.getBatteryChargePct() < 90) {
            imageViewBattery.setImageResource(R.drawable.ic_battery75);
            return;
        }
        imageViewBattery.setImageResource(R.drawable.ic_battery100);
    }
    private void mAnimateCharging() {
        imageViewBattery.setImageResource(R.drawable.battery_charging);
        batteryAnimation = (AnimationDrawable) imageViewBattery.getDrawable();
        batteryAnimation.start();
    }


}

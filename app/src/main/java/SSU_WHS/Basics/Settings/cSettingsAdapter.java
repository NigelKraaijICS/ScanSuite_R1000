package SSU_WHS.Basics.Settings;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cSettingsAdapter extends RecyclerView.Adapter<cSettingsAdapter.SettingsViewHolder>  {


    //Region Public Properties
    public static class SettingsViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewSettingKey;
        private TextView textViewSettingValue;
        public LinearLayout settingItemLinearLayout;

        public SettingsViewHolder(View itemView) {
            super(itemView);
            textViewSettingKey = itemView.findViewById(R.id.textViewSettingKey);
            textViewSettingKey.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewSettingKey.setSingleLine(true);
            textViewSettingKey.setMarqueeRepeatLimit(5);
            textViewSettingKey.setSelected(true);
            textViewSettingValue = itemView.findViewById(R.id.textViewSettingValue);
            settingItemLinearLayout = itemView.findViewById(R.id.settingItemLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cSettingsAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods
    @NonNull
    @Override
    public cSettingsAdapter.SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_user, pvParent, false);
        return new SettingsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cSettingsAdapter.SettingsViewHolder pvHolder, int pvPositionInt) {
        if (cSetting.allSettingsObl != null) {
            final cSetting setting = cSetting.allSettingsObl.get(pvPositionInt);
            pvHolder.textViewSettingKey.setText(setting.getNameStr());
            pvHolder.textViewSettingValue.setText(setting.getValueStr());
                    }
                }

    @Override
    public int getItemCount () {
        if (cSetting.allSettingsObl != null)
            return cSetting.allSettingsObl.size();
        else return 0;
    }
    //End Region Public Methods
}

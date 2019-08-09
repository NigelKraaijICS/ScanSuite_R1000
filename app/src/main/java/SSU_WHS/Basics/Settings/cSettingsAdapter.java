package SSU_WHS.Basics.Settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.icsvertex.scansuite.R;

public class cSettingsAdapter extends RecyclerView.Adapter<cSettingsAdapter.SettingsViewHolder>  {
    private Context callerContext;
    public class SettingsViewHolder extends RecyclerView.ViewHolder{
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

    private final LayoutInflater mInflater;
    List<cSettingsEntity> mSettings; //cached copy of pickorders
    List<cSettingsEntity> mAllSettings;

    public cSettingsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cSettingsAdapter.SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_pickorder, parent, false);
        return new cSettingsAdapter.SettingsViewHolder(itemView);
    }

    public void setSettings(List<cSettingsEntity> settings) {
        mAllSettings = settings;
        mSettings = settings;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cSettingsAdapter.SettingsViewHolder holder, int position) {
        if (mSettings != null) {
            final cSettingsEntity l_SettingsEntity = mSettings.get(position);
            String l_settingKeyStr = l_SettingsEntity.getNameStr();
            String l_settingValueStr = l_SettingsEntity.getValueStr();
            holder.textViewSettingKey.setText(l_settingKeyStr);
            holder.textViewSettingValue.setText(l_settingValueStr);

        }
    }

    @Override
    public int getItemCount () {
        if (mSettings != null)
            return mSettings.size();
        else return 0;
    }
    public void setFilter(String queryText) {
        mSettings = filteredList(queryText);
        notifyDataSetChanged();
    }
    private List<cSettingsEntity> filteredList(String queryText) {
        queryText = queryText.toLowerCase();
        List<cSettingsEntity> filterSettingsList = new ArrayList<>();
        for (cSettingsEntity setting:mAllSettings)
        {
            String settingKey = setting.getNameStr().toLowerCase();
            String settingValue = setting.getValueStr().toLowerCase();

            if (settingKey.contains(queryText) || settingValue.contains(queryText))
            {
                filterSettingsList.add(setting);
            }
        }
        return filterSettingsList;
    }
}

package ICS.Environments;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.MainDefaultActivity;

public class cEnvironmentAdapter extends RecyclerView.Adapter<cEnvironmentAdapter.EnvironmentViewHolder>{
    private Context callerContext;
    public class EnvironmentViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewName;
        private TextView textViewURL;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public EnvironmentViewHolder(View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewDescription.setSingleLine(true);
            textViewDescription.setMarqueeRepeatLimit(5);
            textViewDescription.setSelected(true);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewName.setSingleLine(true);
            textViewName.setMarqueeRepeatLimit(5);
            textViewName.setSelected(true);
            textViewURL = itemView.findViewById(R.id.textViewURL);
            textViewURL.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewURL.setSingleLine(true);
            textViewURL.setMarqueeRepeatLimit(5);
            textViewURL.setSelected(true);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    private final LayoutInflater mInflater;
    public static List<cEnvironmentEntity> mEnvironments; //cached copy of environments

    public cEnvironmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cEnvironmentAdapter.EnvironmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_environment, parent, false);
        return new cEnvironmentAdapter.EnvironmentViewHolder(itemView);
    }

    public void setEnvironments(List<cEnvironmentEntity> environmentEntities) {
        mEnvironments = environmentEntities;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cEnvironmentAdapter.EnvironmentViewHolder holder, int position) {
        if (mEnvironments != null) {
            final cEnvironmentEntity environmentEntity = mEnvironments.get(position);
            String description = environmentEntity.getDescription();
            String name = environmentEntity.getName();
            String url = environmentEntity.getWebserviceurl();

            holder.textViewDescription.setText(description);
            holder.textViewName.setText(name);
            holder.textViewURL.setText(url);

            holder.viewForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cEnvironment.mSaveTheEnvironment(environmentEntity );
                    cEnvironment.mSetWebservice(environmentEntity);
                    if (callerContext instanceof MainDefaultActivity) {
                        ((MainDefaultActivity)callerContext).setChosenEnvironment(environmentEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (mEnvironments != null)
            return mEnvironments.size();
        else return 0;
    }
    public void removeItem(int position) {
        mEnvironments.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
    public void restoreItem(cEnvironmentEntity item, int position) {
        mEnvironments.add(position, item);
        //notify item added by position
        notifyItemInserted(position);
    }
}

package ICS.Environments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.MainDefaultActivity;

public class cEnvironmentAdapter extends RecyclerView.Adapter<cEnvironmentAdapter.EnvironmentViewHolder>{


    //Region Public Properties
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


    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cEnvironmentAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @Override
    public cEnvironmentAdapter.EnvironmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_environment, parent, false);
        return new cEnvironmentAdapter.EnvironmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cEnvironmentAdapter.EnvironmentViewHolder holder, int position) {

        if (cEnvironment.allEnviroments != null) {

            final cEnvironment environment = cEnvironment.allEnviroments.get(position);

            holder.textViewDescription.setText(environment.getDescriptionStr());
            holder.textViewName.setText(environment.getNameStr());
            holder.textViewURL.setText(environment.getWebserviceURLStr());

            holder.viewForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cEnvironment.pSetCurrentEnviroment(environment);
                    if (cAppExtension.context instanceof MainDefaultActivity) {
                        ((MainDefaultActivity)cAppExtension.context).pSetChosenEnvironment();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cEnvironment.allEnviroments != null)
            return cEnvironment.allEnviroments.size();
        else return 0;
    }
    public void removeItem(cEnvironment pvEnviroment) {
        notifyItemRemoved(cEnvironment.allEnviroments.indexOf(pvEnviroment));
        //todo: item is still visible in recyclerview, maybe because object is not yet removed from cEnvironment.allEnviroments
    }
    public void restoreItem(cEnvironment pvEnviroment) {
        int position = cEnvironment.allEnviroments.indexOf(pvEnviroment) + 1;
        notifyItemInserted(position);
    }

    //End Region Public Methods

}

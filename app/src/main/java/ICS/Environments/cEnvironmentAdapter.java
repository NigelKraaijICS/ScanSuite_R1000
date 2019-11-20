package ICS.Environments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;

public class cEnvironmentAdapter extends RecyclerView.Adapter<cEnvironmentAdapter.EnvironmentViewHolder>{


    //Region Public Properties
    public class EnvironmentViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewName;
        private TextView textViewURL;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public EnvironmentViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.textViewName = pvItemView.findViewById(R.id.textViewName);
            this.textViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewName.setSingleLine(true);
            this.textViewName.setMarqueeRepeatLimit(5);
            this.textViewName.setSelected(true);
            this.textViewURL = pvItemView.findViewById(R.id.textViewURL);
            this.textViewURL.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewURL.setSingleLine(true);
            this.textViewURL.setMarqueeRepeatLimit(5);
            this.textViewURL.setSelected(true);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
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

    //Region Default Methods

    @Override
    public cEnvironmentAdapter.EnvironmentViewHolder onCreateViewHolder(ViewGroup pvViewGroup, int pbViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_environment, pvViewGroup, false);
        return new cEnvironmentAdapter.EnvironmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cEnvironmentAdapter.EnvironmentViewHolder pvHolder, int pvPositionInt) {

        if (cEnvironment.allEnviroments != null) {

            final cEnvironment environment = cEnvironment.allEnviroments.get(pvPositionInt);

            pvHolder.textViewDescription.setText(environment.getDescriptionStr());
            pvHolder.textViewName.setText(environment.getNameStr());
            pvHolder.textViewURL.setText(environment.getWebserviceURLStr());

            pvHolder.viewForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cEnvironment.pSetCurrentEnviroment(environment);

                    if (cAppExtension.context instanceof MainDefaultActivity) {
                        MainDefaultActivity.pSetChosenEnvironment();
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

    //End Region Default Methods

    //Region Private Methods


    //End Region Private Methods


}

package SSU_WHS.Basics.Users;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.general.LoginActivity;
import nl.icsvertex.scansuite.R;

public class cUserAdapter extends RecyclerView.Adapter<cUserAdapter.UserViewHolder> {

    //Region Public Properties

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewUserName;
        public LinearLayout userItemLinearLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewName.setSingleLine(true);
            textViewName.setMarqueeRepeatLimit(5);
            textViewName.setSelected(true);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            userItemLinearLayout = itemView.findViewById(R.id.userItemLinearLayout);
        }
    }

    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cUserAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods
    @Override
    public cUserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_user, parent, false);
        return new cUserAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cUserAdapter.UserViewHolder holder, int position) {
        if (cUser.allUsersObl != null) {
            final cUser User = cUser.allUsersObl.get(position);
            holder.textViewName.setText(User.getNameStr());
            holder.textViewUserName.setText(User.getUsernameStr());
            holder.userItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            mUserSelected(User);
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cUser.allUsersObl != null)
            return cUser.allUsersObl.size();
        else return 0;
    }

    private static void mUserSelected(cUser pvUser){
        if (cAppExtension.context instanceof LoginActivity) {
            LoginActivity.pUserSelected(pvUser.getUsernameStr(),false);
        }

    }

    //End Region Public Methods
}

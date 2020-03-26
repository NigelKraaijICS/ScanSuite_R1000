package SSU_WHS.Basics.Users;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.R;

public class cUserAdapter extends RecyclerView.Adapter<cUserAdapter.UserViewHolder> {

    //Region Public Properties

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewUserName;
        private TextView textViewInitials;
        public LinearLayout userItemLinearLayout;

        public UserViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewName = pvItemView.findViewById(R.id.textViewName);
            this.textViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewName.setSingleLine(true);
            this.textViewName.setMarqueeRepeatLimit(5);
            this.textViewName.setSelected(true);
            this.textViewUserName = pvItemView.findViewById(R.id.textViewUserName);
            this.textViewInitials = pvItemView.findViewById(R.id.textViewInitials);
            this.userItemLinearLayout = pvItemView.findViewById(R.id.userItemLinearLayout);
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
    @NonNull
    @Override
    public cUserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_user, parent, false);
        return new cUserAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cUserAdapter.UserViewHolder holder, int position) {
        if (cUser.allUsersObl != null) {
            final cUser User = cUser.allUsersObl.get(position);
            holder.textViewName.setText(User.getNameStr());
            holder.textViewUserName.setText(User.getUsernameStr());
            holder.textViewInitials.setText(User.getInitialsStr());
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
            LoginActivity loginActivity = new LoginActivity();
            loginActivity.pUserSelected(cBarcodeScan.pFakeScan(pvUser.getUsernameStr()),false);
        }

    }

    //End Region Public Methods
}

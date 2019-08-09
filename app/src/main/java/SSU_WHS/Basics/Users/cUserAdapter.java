package SSU_WHS.Basics.Users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nl.icsvertex.scansuite.activities.general.LoginActivity;
import nl.icsvertex.scansuite.R;

public class cUserAdapter extends RecyclerView.Adapter<cUserAdapter.UserViewHolder> {
    private Context callerContext;
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

    private final LayoutInflater mInflater;
    public static List<cUserEntity> mUsers; //cached copy of authorisations

    public cUserAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cUserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_user, parent, false);
        return new cUserAdapter.UserViewHolder(itemView);
    }

    public void setUsers(List<cUserEntity> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cUserAdapter.UserViewHolder holder, int position) {
        if (mUsers != null) {
            final cUserEntity l_UserEntity = mUsers.get(position);
            String l_nameStr = l_UserEntity.getNameStr();
            String l_userNameStr = l_UserEntity.getUsernameStr();

            holder.textViewName.setText(l_nameStr);
            holder.textViewUserName.setText(l_userNameStr);

            holder.userItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callerContext instanceof LoginActivity) {
                        ((LoginActivity)callerContext).setChosenUser(l_UserEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (mUsers != null)
            return mUsers.size();
        else return 0;
    }
}

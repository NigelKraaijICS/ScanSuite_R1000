package SSU_WHS.Comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;

public class cCommentAdapter extends RecyclerView.Adapter<cCommentAdapter.CommentViewHolder>{
    private Context callerContext;
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewComment;
        public LinearLayout commentItemLinearLayout;

        public CommentViewHolder(View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            commentItemLinearLayout = itemView.findViewById(R.id.commentItemLinearLayout);
        }
    }

    private final LayoutInflater mInflater;
    public static List<cCommentEntity> mComments; //cached copy of comments

    public cCommentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cCommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_comment, parent, false);
        return new cCommentAdapter.CommentViewHolder(itemView);
    }

    public void setWorkplaces(List<cCommentEntity> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cCommentAdapter.CommentViewHolder holder, int position) {
        if (mComments != null) {
            final cCommentEntity commentEntity = mComments.get(position);
            String commentText = commentEntity.getCommenttext();
            holder.textViewComment.setText(commentText);

            holder.textViewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Animation animation = AnimationUtils.loadAnimation(callerContext.getApplicationContext(), R.anim.bounce);
                    ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.1,15);
                    animation.setInterpolator(interpolator);
                    v.startAnimation(animation);
                    cUserInterface.playSound(R.raw.message, 0);
                    }
            });
        }
    }


    @Override
    public int getItemCount () {
        if (mComments != null)
            return mComments.size();
        else return 0;
    }
}

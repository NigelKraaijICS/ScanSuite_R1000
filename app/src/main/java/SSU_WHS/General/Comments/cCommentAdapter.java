package SSU_WHS.General.Comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cCommentAdapter extends RecyclerView.Adapter<cCommentAdapter.commentViewHolder>{

    public class commentViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewComment;
        public LinearLayout commentItemLinearLayout;

        public commentViewHolder(View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            commentItemLinearLayout = itemView.findViewById(R.id.commentItemLinearLayout);
        }
    }

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    private List<cComment> localCommentsObl;
    //End Region Private Properties

    //Region Constructor
    public cCommentAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    @Override
    public commentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_comment, parent, false);
        return new commentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(commentViewHolder holder, int pvPosition) {
        if (this.localCommentsObl != null || this.localCommentsObl.size() > 0) {
            final cComment comment = this.localCommentsObl.get(pvPosition);
            String commentText = comment.getCommentTextStr();
            holder.textViewComment.setText(commentText);

            holder.textViewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Animation animation = AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.bounce);
                    ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.1,15);
                    animation.setInterpolator(interpolator);
                    v.startAnimation(animation);
                    cUserInterface.pPlaySound(R.raw.message, 0);
                    }
            });
        }
    }

    public void pFillData(List<cComment> pvDataObl) {
        this.localCommentsObl = pvDataObl;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount () {
        if (this.localCommentsObl != null)
            return this.localCommentsObl.size();
        else return 0;
    }
}

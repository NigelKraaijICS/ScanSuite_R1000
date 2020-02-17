package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class CommentFragment extends DialogFragment implements iICSDefaultFragment {

   //Region Private Properties
   private static ImageView toolbarImage;
   private static TextView toolbarTitle;

    private static RecyclerView commentRecyclerView;
    private static Button buttonRoger;

    private static List<cComment> localCommentObl;

    private static String titleStr;

    //End Region Private Properties


    //Region Constructor
    public CommentFragment() {
        // Required empty public constructor
    }

    public CommentFragment(List<cComment> pvDataObl) {
        CommentFragment.localCommentObl = pvDataObl;
    }
    //End Region Constructor

    //Region Default Methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        return pvInflater.inflate(R.layout.fragment_comments, pvContainer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        CommentFragment.titleStr = cAppExtension.context.getString(R.string.comments);

        if (args != null) {
            CommentFragment.titleStr = args.getString(cPublicDefinitions.KEY_COMMENTHEADER,"");
        }

        if (CommentFragment.titleStr.equalsIgnoreCase("")) {
            CommentFragment.titleStr = getResources().getString(R.string.comments);
        }

        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cUserInterface.pEnableScanner();
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
            this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();
        this.mSetCommentRecycler();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            CommentFragment.toolbarImage = getView().findViewById(R.id.toolbarImage);
            CommentFragment.toolbarTitle = getView().findViewById(R.id.toolbarTitle);

            CommentFragment.commentRecyclerView = getView().findViewById(R.id.commentRecyclerview);
            CommentFragment.buttonRoger = getView().findViewById(R.id.buttonRoger);
        }
    }


    @Override
    public void mFieldsInitialize() {



    }

    @Override
    public void mSetListeners() {
        this.mSetHeaderListener();
        this.mSetCloseListener();
    }

    //End Regioni ICSDefaultFragment methods

    //Region Public Methods

    //Region Public Methods


    //End Region Public Methods
    //End Region Public Methods

    //Region Private Methods
    private void mSetToolbar() {

        CommentFragment.toolbarTitle.setText(CommentFragment.titleStr);
        CommentFragment.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        CommentFragment.toolbarTitle.setSingleLine(true);
        CommentFragment.toolbarTitle.setMarqueeRepeatLimit(5);
        CommentFragment.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommentFragment.toolbarTitle.setSelected(true);
            }
        },1500);

        CommentFragment.toolbarImage.setImageResource(R.drawable.ic_comment);

    }
    private void mSetCloseListener() {
        CommentFragment.buttonRoger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetHeaderListener() {
        CommentFragment.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToBottom();
            }
        });

        CommentFragment.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToTop();
                return true;
            }
        });
    }

    private void mScrollToTop() {
        CommentFragment.commentRecyclerView.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (cComment.getcommentAdapter()!= null) {
            if (cComment.getcommentAdapter().getItemCount() > 0) {
                CommentFragment.commentRecyclerView.smoothScrollToPosition(cComment.getcommentAdapter().getItemCount() -1 );
            }
        }

    }


    private void mSetCommentRecycler() {

        CommentFragment.commentRecyclerView.setHasFixedSize(false);
        CommentFragment.commentRecyclerView.setAdapter(cComment.getcommentAdapter());
        CommentFragment.commentRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        cComment.getcommentAdapter().pFillData(CommentFragment.localCommentObl);
    }
    //End Region Private Methods





}

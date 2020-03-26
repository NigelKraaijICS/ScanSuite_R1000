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
import SSU_WHS.General.Comments.cCommentAdapter;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class CommentFragment extends DialogFragment implements iICSDefaultFragment {

   //Region Private Properties
   private  ImageView toolbarImage;
   private  TextView toolbarTitle;

    private  RecyclerView commentRecyclerView;
    private  Button buttonRoger;

    private  List<cComment> localCommentObl;

    private  String titleStr;

    private cCommentAdapter commentAdapter;
    private cCommentAdapter getcommentAdapter(){
        if (this.commentAdapter == null) {
            this.commentAdapter = new cCommentAdapter();
        }

        return  commentAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public CommentFragment() {
        // Required empty public constructor
    }

    public CommentFragment(List<cComment> pvDataObl) {
        this.localCommentObl = pvDataObl;
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
        this.titleStr = cAppExtension.context.getString(R.string.comments);

        if (args != null) {
            this.titleStr = args.getString(cPublicDefinitions.KEY_COMMENTHEADER,"");
        }

        if (this.titleStr.equalsIgnoreCase("")) {
            this.titleStr = getResources().getString(R.string.comments);
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
            this.toolbarImage = getView().findViewById(R.id.toolbarImage);
            this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);

            this.commentRecyclerView = getView().findViewById(R.id.commentRecyclerview);
            this.buttonRoger = getView().findViewById(R.id.buttonRoger);
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

        this.toolbarTitle.setText(this.titleStr);
        this.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.toolbarTitle.setSingleLine(true);
        this.toolbarTitle.setMarqueeRepeatLimit(5);
        this.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        this.toolbarImage.setImageResource(R.drawable.ic_comment);

    }
    private void mSetCloseListener() {
        this.buttonRoger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetHeaderListener() {
        this.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToBottom();
            }
        });

        this.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToTop();
                return true;
            }
        });
    }

    private void mScrollToTop() {
        this.commentRecyclerView.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (this.getcommentAdapter()!= null) {
            if (this.getcommentAdapter().getItemCount() > 0) {
                this.commentRecyclerView.smoothScrollToPosition(this.getcommentAdapter().getItemCount() -1 );
            }
        }

    }


    private void mSetCommentRecycler() {

        this.commentRecyclerView.setHasFixedSize(false);
        this.commentRecyclerView.setAdapter(this.getcommentAdapter());
        this.commentRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.getcommentAdapter().pFillData(this.localCommentObl);
    }
    //End Region Private Methods





}

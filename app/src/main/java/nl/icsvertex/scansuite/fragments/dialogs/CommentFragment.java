package nl.icsvertex.scansuite.fragments.dialogs;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Comments.cCommentAdapter;
import SSU_WHS.Comments.cCommentEntity;
import SSU_WHS.Comments.cCommentViewModel;
import SSU_WHS.cAppExtension;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import nl.icsvertex.scansuite.R;

public class CommentFragment extends DialogFragment implements iICSDefaultFragment {
    TextView textViewCommentHeader;
    RecyclerView commentRecyclerView;
    Button buttonRoger;
    DialogFragment thisFragment;
    cCommentViewModel commentViewModel;
    cCommentAdapter commentAdapter;

    String currentBranch;
    String currentOrder;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_comments, container);
        thisFragment = this;
        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");
        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");

        mFragmentInitialize();
        mGetData();
    }
    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mSetViewModels();
        mFieldsInitialize();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        textViewCommentHeader = getView().findViewById(R.id.textViewCommentHeader);
        commentRecyclerView = getView().findViewById(R.id.commentRecyclerview);
        buttonRoger = getView().findViewById(R.id.buttonRoger);
    }

    @Override
    public void mSetViewModels() {
        commentViewModel = ViewModelProviders.of(this).get(cCommentViewModel.class);
    }

    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        mSetHeaderListener();
        mSetCloseListener();
    }
    private void mSetCloseListener() {
        buttonRoger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisFragment.dismiss();
            }
        });
    }
    private void mSetHeaderListener() {
        textViewCommentHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToBottom();
            }
        });
        textViewCommentHeader.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToTop();
                return true;
            }
        });
    }
    private void mScrollToTop() {
        commentRecyclerView.smoothScrollToPosition(0);
    }
    private void mScrollToBottom() {
        if (commentAdapter!= null) {
            if (commentAdapter.getItemCount() > 0) {
                commentRecyclerView.smoothScrollToPosition(commentAdapter.getItemCount() -1 );
            }
        }

    }
    private void mGetData() {
        Boolean forceRefresh = false;

        commentViewModel.getComments(forceRefresh, currentBranch, currentOrder).observe(this, new Observer<List<cCommentEntity>>() {
            @Override
            public void onChanged(@Nullable List<cCommentEntity> commentEntities) {
                if (commentEntities != null) {
                    mSetCommentRecycler(commentEntities);
                }
            }
        });
    }
    private void mSetCommentRecycler(List<cCommentEntity> commentEntities) {
        commentAdapter = new cCommentAdapter(cAppExtension.context);
        commentRecyclerView.setHasFixedSize(false);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        commentAdapter.setWorkplaces(commentEntities);
    }
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
    }


}

package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleStock.cArticleStockAdapter;
import nl.icsvertex.scansuite.R;

public class ItemStockFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties



    //End Region Public Properties

    //Region Private Properties
    private RecyclerView stockRecyclerview;

    private cArticleStockAdapter articleStockAdapter;
    private cArticleStockAdapter getArticleStockAdapter(){
        if (this.articleStockAdapter == null) {
            this.articleStockAdapter = new cArticleStockAdapter();
        }

        return  articleStockAdapter;
    }
    TextView textViewItem;

    //End Region Private Properties


    //Region Constructor
    public ItemStockFragment() {

    }
    //End Region Constructor


    //Region Default Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        return pvInflater.inflate(R.layout.fragment_itemstock, pvContainer);
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
    super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        cAppExtension.dialogFragment = this;

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewItem = getView().findViewById(R.id.textViewItem);
            this.stockRecyclerview = getView().findViewById(R.id.stockRecyclerview);

        }

    }


    @Override
    public void mFieldsInitialize() {
        this.textViewItem.setText(cArticle.currentArticle.getItemNoStr() + " " + cArticle.currentArticle.getVariantCodeStr());
        this.mGetData();
    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment methods

    //Region Private Methods

    private void mGetData() {


        cArticle.currentArticle.pGetStockViaWebserviceBln();

        if (cArticle.currentArticle.stockObl != null && cArticle.currentArticle.stockObl.size() > 0) {
            this.mSetStockRecycler();
        }
    }

    private void mSetStockRecycler() {
        this.stockRecyclerview.setHasFixedSize(false);
        this.stockRecyclerview.setAdapter(this.getArticleStockAdapter());
        this.stockRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    //End Region Private Methods





}

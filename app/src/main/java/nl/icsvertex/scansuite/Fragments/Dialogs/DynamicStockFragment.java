package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleStock.cArticleStockAdapter;
import nl.icsvertex.scansuite.R;

public class DynamicStockFragment extends Fragment implements iICSDefaultFragment {

    private TextView textViewItem;
    private RecyclerView stockRecyclerview;
    private ProgressBar progressBar;
    private Button buttonOk;

    private cArticleStockAdapter articleStockAdapter;
    private cArticleStockAdapter getArticleStockAdapter(){
        if (this.articleStockAdapter == null) {
            this.articleStockAdapter = new cArticleStockAdapter(true);
        }

        return  articleStockAdapter;
    }

    public DynamicStockFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itemstock, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        this.mFragmentInitialize();
    }


    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.stockRecyclerview =  getView().findViewById(R.id.stockRecyclerview);
            this.textViewItem = getView().findViewById(R.id.textViewItem);
            this.progressBar = getView().findViewById(R.id.progressBar);
            this.buttonOk = getView().findViewById(R.id.buttonOk);
        }

    }

    @Override
    public void mFieldsInitialize() {
        this.textViewItem.setVisibility(View.GONE);
        this.buttonOk.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.INVISIBLE);

        this.mGetData();
    }

    @Override
    public void mSetListeners() {

    }

    private void mGetData() {

        if (cArticle.currentArticle.stockObl != null && cArticle.currentArticle.stockObl.size() > 0 ) {
            this.mShowNoLinesIcon(false);
            return;
        }

        cArticle.currentArticle.pGetStockViaWebserviceBln();
        this.mShowNoLinesIcon(cArticle.currentArticle.stockObl == null || cArticle.currentArticle.stockObl.size() <= 0);
    }

    private  void mShowNoLinesIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(() -> {

            cUserInterface.pHideGettingData();

            if (pvShowBln) {

                stockRecyclerview.setVisibility(View.INVISIBLE);

                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                NoStockFragment fragment = new NoStockFragment();
                fragmentTransaction.replace(R.id.itemStockContainer, fragment);
                fragmentTransaction.commit();
                return;


            }

            stockRecyclerview.setVisibility(View.VISIBLE);
            stockRecyclerview.setHasFixedSize(false);
            stockRecyclerview.setAdapter(getArticleStockAdapter());
            stockRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

            List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof NoStockFragment) {
                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

}
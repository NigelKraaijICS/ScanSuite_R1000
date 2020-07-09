package SSU_WHS.Basics.ArticleStock;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BinItem.cBinItem;
import nl.icsvertex.scansuite.R;

public class cArticleStockAdapter extends RecyclerView.Adapter<cArticleStockAdapter.ArticleStockViewHolder>{

    //Region Public Properties
    public static class ArticleStockViewHolder extends RecyclerView.ViewHolder{

        private TextView textviewBinCode;
        private TextView textViewQuantity;
        public LinearLayout itemStockItemLinearLayout;

        public ArticleStockViewHolder(View itemView) {
            super(itemView);

            this.itemStockItemLinearLayout = itemView.findViewById(R.id.itemStockItemLinearLayout);


            this.textViewQuantity = itemView.findViewById(R.id.textViewQuanitity);
            this.textviewBinCode = itemView.findViewById(R.id.textviewBinCode);
        }
    }

    //End Region Public Properties

    //Region Constructor

    public cArticleStockAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertoes

    @NonNull
    @Override
    public cArticleStockAdapter.ArticleStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_articlestock, parent, false);
        return new ArticleStockViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull cArticleStockAdapter.ArticleStockViewHolder holder, int position) {
        if (cArticle.currentArticle.stockObl != null) {

            final cArticleStock articleStock = cArticle.currentArticle.stockObl .get(position);

            holder.textviewBinCode.setText(articleStock.getBincodeStr());
            holder.textViewQuantity.setText(cText.pDoubleToStringStr(articleStock.getQuantityDbl()));

            holder.itemStockItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set the current binItem
                    cArticleStock.currentArticleStock = articleStock;

                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cArticle.currentArticle.stockObl != null)
            return cArticle.currentArticle.stockObl.size();
        else return 0;
    }
}

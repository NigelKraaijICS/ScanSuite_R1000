package SSU_WHS.Basics.ArticleStock;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Branches.cBranch;
import nl.icsvertex.scansuite.R;

public class cArticleStockAdapter extends RecyclerView.Adapter<cArticleStockAdapter.ArticleStockViewHolder>{

    //Region Public Properties
    public static class ArticleStockViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout itemStockItemLinearLayout;

        private final TextView textviewBinCode;
        private final TextView textViewQuantity;
        private ImageView imageViewBinItemPositive;
        private ImageView imageViewBinItemNegative;

        public ArticleStockViewHolder(View itemView) {
            super(itemView);

            this.itemStockItemLinearLayout = itemView.findViewById(R.id.itemStockItemLinearLayout);
            this.textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            this.textviewBinCode = itemView.findViewById(R.id.textviewBinCode);
            this.textviewBinCode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textviewBinCode.setSingleLine(true);
            this.textviewBinCode.setMarqueeRepeatLimit(5);
            this.textviewBinCode.setSelected(true);

            try{
                this.imageViewBinItemPositive = itemView.findViewById(R.id.imageViewBinItemPositive);
                this.imageViewBinItemNegative = itemView.findViewById(R.id.imageViewBinItemNegative);
            }
            catch(Exception ignored){
            }
        }
    }

    //End Region Public Properties

    //Region Constructor

    public cArticleStockAdapter(Boolean pvTinyModus) {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
        this.tinyModusBln = pvTinyModus;
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private final boolean tinyModusBln;

    //End Region Private Propertoes

    @NonNull
    @Override
    public cArticleStockAdapter.ArticleStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;

        if (!this.tinyModusBln) {
            itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_articlestock, parent, false);
        }
        else {
            itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_articlestock_tiny, parent, false);
        }

        return new ArticleStockViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ArticleStockViewHolder holder, int position) {
        if (cArticle.currentArticle.stockObl != null) {

            final cArticleStock articleStock = cArticle.currentArticle.stockObl .get(position);

            cBranch branch =  cBranch.pGetBranchByCode(articleStock.getBincodeStr());
            if (branch != null) {
                holder.textviewBinCode.setText(branch.getBranchNameStr());
            }
            else
            {
                holder.textviewBinCode.setText(articleStock.getBincodeStr());
            }
            holder.textViewQuantity.setText(cText.pDoubleToStringStr(articleStock.getQuantityDbl()));

            if (this.tinyModusBln) {
                if (articleStock.getQuantityDbl() > 0) {
                    holder.imageViewBinItemPositive.setVisibility(View.VISIBLE);
                    holder.imageViewBinItemNegative.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.imageViewBinItemPositive.setVisibility(View.INVISIBLE);
                    holder.imageViewBinItemNegative.setVisibility(View.VISIBLE);
                }
            }

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

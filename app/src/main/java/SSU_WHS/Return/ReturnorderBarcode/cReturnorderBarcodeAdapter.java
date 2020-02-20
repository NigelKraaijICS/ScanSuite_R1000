package SSU_WHS.Return.ReturnorderBarcode;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnArticleDetailFragment;
import nl.icsvertex.scansuite.R;

public class cReturnorderBarcodeAdapter extends RecyclerView.Adapter<cReturnorderBarcodeAdapter.returnorderBarcodeViewHolder>  {

    //Region Public Properties
    public class returnorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewBarcode;
        private TextView textViewQuantity;
        public LinearLayout barcodeItemLinearLayout;

        public returnorderBarcodeViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewBarcode = pvItemView.findViewById(R.id.textViewBarcode);

            this.textViewBarcode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBarcode.setSingleLine(true);
            this.textViewBarcode.setMarqueeRepeatLimit(5);
            this.textViewBarcode.setSelected(true);
            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.barcodeItemLinearLayout = pvItemView.findViewById(R.id.barcodeLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cReturnorderBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @Override
    public returnorderBarcodeViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, pvParent, false);
        return new returnorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(returnorderBarcodeViewHolder pvHolder, int pvPositionInt) {

        if (cReturnorderLine.currentReturnOrderLine == null || cReturnorderLine.currentReturnOrderLine.barcodeObl() == null || cReturnorderLine.currentReturnOrderLine.barcodeObl() .size() == 0) {
            return;
        }

        final cReturnorderBarcode returnorderBarcode = cReturnorderLine.currentReturnOrderLine.barcodeObl().get(pvPositionInt);
        pvHolder.textViewBarcode.setText(returnorderBarcode.getBarcodeStr());
        pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(returnorderBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof BarcodeFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                ReturnArticleDetailFragment.pHandleScan(cBarcodeScan.pFakeScan(cReturnorderLine.currentReturnOrderLine.barcodeObl().get(0).getBarcodeStr()));
            }
        });
    }

    @Override
    public int getItemCount () {
        if (cReturnorderLine.currentReturnOrderLine.barcodeObl() != null && cReturnorderLine.currentReturnOrderLine.barcodeObl() != null)
            return cReturnorderLine.currentReturnOrderLine.barcodeObl().size();
        else return 0;
    }
}

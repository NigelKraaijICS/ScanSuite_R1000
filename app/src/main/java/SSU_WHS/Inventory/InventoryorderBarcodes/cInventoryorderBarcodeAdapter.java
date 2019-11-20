package SSU_WHS.Inventory.InventoryorderBarcodes;

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
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryArticleDetailFragment;
import nl.icsvertex.scansuite.R;

public class cInventoryorderBarcodeAdapter extends RecyclerView.Adapter<cInventoryorderBarcodeAdapter.inventoryorderBarcodeViewHolder>  {

    //Region Public Properties
    public class inventoryorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewBarcode;
        private TextView textViewQuantity;
        public LinearLayout barcodeItemLinearLayout;

        public inventoryorderBarcodeViewHolder(View pvItemView) {
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
    public cInventoryorderBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @Override
    public inventoryorderBarcodeViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, pvParent, false);
        return new inventoryorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(inventoryorderBarcodeViewHolder pvHolder, int pvPositionInt) {

        if (cInventoryorderLine.currentInventoryOrderLine == null || cInventoryorderLine.currentInventoryOrderLine.barcodesObl() == null || cInventoryorderLine.currentInventoryOrderLine.barcodesObl() .size() == 0) {
            return;
        }

        final cInventoryorderBarcode inventoryorderBarcode = cInventoryorderLine.currentInventoryOrderLine.barcodesObl().get(pvPositionInt);
        pvHolder.textViewBarcode.setText(inventoryorderBarcode.getBarcodeStr());
        pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(inventoryorderBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cBarcodeScan barcodeScan = new cBarcodeScan();
                barcodeScan.barcodeStr = cInventoryorderLine.currentInventoryOrderLine.barcodesObl().get(0).getBarcodeStr();
                barcodeScan.barcodeOriginalStr = cInventoryorderLine.currentInventoryOrderLine.barcodesObl().get(0).getBarcodeStr();
                barcodeScan.barcodeStr =  cText.pIntToStringStr(cBarcodeScan.BarcodeType.EAN13);

                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof BarcodeFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                InventoryArticleDetailFragment.pHandleScan(barcodeScan);
            }
        });
    }

    @Override
    public int getItemCount () {
        if (cInventoryorderLine.currentInventoryOrderLine.barcodesObl() != null && cInventoryorderLine.currentInventoryOrderLine.barcodesObl() != null)
            return cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size();
        else return 0;
    }
}

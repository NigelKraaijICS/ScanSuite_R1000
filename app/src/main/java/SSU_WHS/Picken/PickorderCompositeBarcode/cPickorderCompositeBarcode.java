package SSU_WHS.Picken.PickorderCompositeBarcode;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.CompositeBarcode.cCompositeBarcode;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeViewModel;

public class cPickorderCompositeBarcode {

    private final String compositeBarcodeStr;
    public String getCompositeBarcodeStr() {return compositeBarcodeStr;}

    private final String itemNoStr;
    public String getItemNoStr() {return itemNoStr;}

    private final String variantcodeStr;
    public String getVariantCodeStr() {return variantcodeStr;}


    private final int sortingSequenceNoInt;
    public int getSortingSequenceNoInt() {return sortingSequenceNoInt;}


    private final cPickorderCompositeBarcodeEntity pickorderCompositeBarcodeEntity;

    public static ArrayList<cPickorderCompositeBarcode> allCompositeBarcodesObl;
    public static cPickorderCompositeBarcode currentCompositePickorderBarcode;

    private cPickorderCompositeBarcodeViewModel getPickorderCompositeBarcodeViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderCompositeBarcodeViewModel.class);
    }

    public cCompositeBarcode compositeBarcode() {
       return cCompositeBarcode.pGetCompositeBarcodeByType(this.getCompositeBarcodeStr());
    }

    public cPickorderCompositeBarcode(JSONObject pvJsonObject) {
        this.pickorderCompositeBarcodeEntity = new cPickorderCompositeBarcodeEntity(pvJsonObject);
        this.compositeBarcodeStr = this.pickorderCompositeBarcodeEntity.getCompositeBarcodeStr();
        this.itemNoStr = this.pickorderCompositeBarcodeEntity.getItemNoStr();
        this.variantcodeStr = this.pickorderCompositeBarcodeEntity.getVariantCodeStr();
        this.sortingSequenceNoInt =  this.pickorderCompositeBarcodeEntity.getSortingSequenceNoInt();

    }

   public LinkedHashMap<String, String> KeysAndValuesObl;


    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getPickorderCompositeBarcodeViewModel().insert(this.pickorderCompositeBarcodeEntity);
        if (cPickorderCompositeBarcode.allCompositeBarcodesObl == null ){
            cPickorderCompositeBarcode.allCompositeBarcodesObl = new ArrayList<>();

        }
        cPickorderCompositeBarcode.allCompositeBarcodesObl.add(this);
        return true;

    }

    public static boolean pTruncateTableBln(){
        cPickorderCompositeBarcodeViewModel pickorderCompositeBarcodeViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderCompositeBarcodeViewModel.class);
        pickorderCompositeBarcodeViewModel.deleteAll();
        return true;
    }


    public static List <cPickorderCompositeBarcode> pGetPickorderCompositeBarcodesViaVariantAndItemNoObl(String pvItemNo, String pvVariantcode) {
        if (cPickorderCompositeBarcode.allCompositeBarcodesObl == null || cPickorderCompositeBarcode.allCompositeBarcodesObl.size() == 0){
            return null;
        }
        List <cPickorderCompositeBarcode> resultObl = null;

        for (cPickorderCompositeBarcode pickorderCompositeBarcode : cPickorderCompositeBarcode.allCompositeBarcodesObl) {
            if (pickorderCompositeBarcode.getVariantCodeStr().equalsIgnoreCase(pvVariantcode) && pickorderCompositeBarcode.getItemNoStr().equalsIgnoreCase(pvItemNo)){
                if (resultObl == null ){
                    resultObl = new ArrayList<>();
                }

                resultObl.add(pickorderCompositeBarcode);
            }
        }return resultObl;
    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

}

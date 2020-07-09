package SSU_WHS.Return.ReturnorderDocument;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import nl.icsvertex.scansuite.R;

public class cReturnorderDocument {

    private cReturnorderDocumentEntity returnorderDocumentEntity;

    public List<cReturnorderLine> returnorderLineObl;
    public cBarcodeScan barcodeScanToHandle;

    public static List<cReturnorderDocument> allReturnorderDocumentObl;
    public static cReturnorderDocument currentReturnOrderDocument;
    public static List<cReturnorderDocument> returnorderDocumentsTodoObl;
    public static List<cReturnorderDocument> returnorderDocumentsDoneObl;
    public static List<cReturnorderDocument> returnorderDocumentsTotalObl;

    private cReturnorderDocumentViewModel getReturnorderDocumentViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderDocumentViewModel.class);
    }



    //Region Public Properties

    private String sourceDocumentStr;
    public String getSourceDocumentStr() {
        return sourceDocumentStr;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    public  String getLineCounterStr(){
       return   cAppExtension.activity.getString(R.string.lines) + ' ' + this.returnorderLineObl.size();
    }

    public  String getItemCounterStr(){

        if (!cReturnorder.currentReturnOrder.isGeneratedBln()) {
            return   cAppExtension.activity.getString(R.string.items) + " "+ cReturnorder.currentReturnOrder.pGetHandledCountForSourceDocumentInt(this)  + '/' +  cReturnorder.currentReturnOrder.pGetCountForSourceDocumentInt(this);
        }

        return cAppExtension.activity.getString(R.string.items) + " "+ cReturnorder.currentReturnOrder.pGetHandledCountForSourceDocumentInt(this);
    }

    //Region Constructor

    public cReturnorderDocument(cReturnorderDocumentEntity pvReturnorderDocumentEntity){
        this.returnorderDocumentEntity = pvReturnorderDocumentEntity;
        this.sourceDocumentStr = this.returnorderDocumentEntity.getSourceDocumentStr();
        this.statusInt = this.returnorderDocumentEntity.getStatusInt();
    }


    //End Region Constructor

    public static boolean pTruncateTableBln(){
        cReturnorderDocumentViewModel returnorderDocumentViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderDocumentViewModel.class);
        returnorderDocumentViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getReturnorderDocumentViewModel().insert(this.returnorderDocumentEntity);

        if (cReturnorderDocument.allReturnorderDocumentObl == null){
            cReturnorderDocument.allReturnorderDocumentObl = new ArrayList<>();
        }
        cReturnorderDocument.allReturnorderDocumentObl.add(this);
        return  true;
    }

    public boolean pCloseBln(){
        return this.getReturnorderDocumentViewModel().pCloseBln();
    }

    public void pAddReturnorderLine(cReturnorderLine returnorderLine){
        this.returnorderLineObl.add(returnorderLine);
    }

    public cReturnorderDocument(String pvSourceDocumentStr) {
        this.returnorderDocumentEntity = new cReturnorderDocumentEntity(pvSourceDocumentStr);
        this.sourceDocumentStr = pvSourceDocumentStr;
        this.statusInt = cWarehouseorder.ReturnDocumentStatusEnu.New;
        this.returnorderLineObl = new ArrayList<>();

    }

    //End Region Constructor

    //Region Public Methods

    public static cReturnorderDocument pGetReturnDocument (String pvReturnDocumentStr){

        if (cReturnorderDocument.allReturnorderDocumentObl == null || cReturnorderDocument.allReturnorderDocumentObl.size() == 0) {
            return  null;
        }

        for (cReturnorderDocument returnorderDocument : cReturnorderDocument.allReturnorderDocumentObl) {
            if (returnorderDocument.getSourceDocumentStr().equalsIgnoreCase(pvReturnDocumentStr)) {
                return  returnorderDocument;
            }
        }

        return  null;

    }

}

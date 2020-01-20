package SSU_WHS.Return.ReturnorderDocument;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import nl.icsvertex.scansuite.R;

public class cReturnorderDocument {

    private cReturnorderDocumentEntity returnorderDocumentEntity;
    public boolean indatabaseBln;

    private static cReturnorderDocumentViewModel gReturnorderDocumentViewModel;
    private static cReturnorderDocumentViewModel getReturnorderDocumentViewModel() {
        if (gReturnorderDocumentViewModel == null) {
            gReturnorderDocumentViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cReturnorderDocumentViewModel.class);
        }
        return gReturnorderDocumentViewModel;
    }

    public List<cReturnorderLine> returnorderLineObl;
    public static List<cReturnorderDocument> allReturnorderDocumentObl;
    public static cReturnorderDocument currentReturnOrderDocument;
    public static List<cReturnorderDocument> returnorderDocumentsTodoObl;
    public static List<cReturnorderDocument> returnorderDocumentsDoneObl;
    public static List<cReturnorderDocument> returnorderDocumentsTotalObl;

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
        cReturnorderDocument.getReturnorderDocumentViewModel().deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        cReturnorderDocument.getReturnorderDocumentViewModel().insert(this.returnorderDocumentEntity);
        this.indatabaseBln = true;

        if (cReturnorderDocument.allReturnorderDocumentObl == null){
            cReturnorderDocument.allReturnorderDocumentObl = new ArrayList<>();
        }
        cReturnorderDocument.allReturnorderDocumentObl.add(this);
        return  true;
    }

    public boolean pCloseBln(){
        return cReturnorderDocument.getReturnorderDocumentViewModel().pCloseBln();
    }

    public void pAddReturnorderLine(cReturnorderLine returnorderLine){
        this.returnorderLineObl.add(returnorderLine);
    }


    private static cReturnorderDocumentAdapter gReturnorderDocumentDoneAdapter;
    public static cReturnorderDocumentAdapter getReturnorderDocumentDoneAdapter() {
        if (gReturnorderDocumentDoneAdapter == null) {
            gReturnorderDocumentDoneAdapter = new cReturnorderDocumentAdapter();
        }
        return gReturnorderDocumentDoneAdapter;
    }
    private static cReturnorderDocumentAdapter gReturnorderDocumentNotDoneAdapter;
    public static cReturnorderDocumentAdapter getReturnorderDocumentNotDoneAdapter() {
        if (gReturnorderDocumentNotDoneAdapter == null) {
            gReturnorderDocumentNotDoneAdapter = new cReturnorderDocumentAdapter();
        }
        return gReturnorderDocumentNotDoneAdapter;
    }
    private static cReturnorderDocumentAdapter gReturnorderDocumentTotalAdapter;
    public static cReturnorderDocumentAdapter getReturnorderDocumentTotalAdapter() {
        if (gReturnorderDocumentTotalAdapter == null) {
            gReturnorderDocumentTotalAdapter = new cReturnorderDocumentAdapter();
        }
        return gReturnorderDocumentTotalAdapter;
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

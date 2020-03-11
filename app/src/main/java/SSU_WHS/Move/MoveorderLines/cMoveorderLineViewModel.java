package SSU_WHS.Move.MoveorderLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Webservice.cWebresult;

public class cMoveorderLineViewModel extends AndroidViewModel {
    //Region Public Properties
    private cMoveorderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cMoveorderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cMoveorderLineRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cMoveorderLineEntity pvMoveorderLineEntity) {this.Repository.insert(pvMoveorderLineEntity);}
    public void delete(cMoveorderLineEntity pvMoveorderLineEntity) {this.Repository.delete(pvMoveorderLineEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public boolean pUpdateQuantityHandledBln(Double pvQuantityHandledDbl) {return this.Repository.pUpdateQuantityHandledBln(pvQuantityHandledDbl);}
    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {return this.Repository.pUpdateLocalStatusBln(pvNewStatusInt);}
    public boolean pUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {return this.Repository.pUpdateLocalHandledTimeStampBln(pvHandledTimeStampStr);}

    public cWebresult pMoveLineHandledTakeMTViaWebserviceWrs() {return this.Repository.pMoveLineHandledTakeMTViaWebserviceWrs();}
    public cWebresult pMoveLineHandledPlaceMTViaWebserviceWrs() {return this.Repository.pMoveLineHandledPlaceMTViaWebserviceWrs();}

    public cWebresult pMoveNewItemHandledViaWebserviceWrs(String pvBinCodeStr, List<cMoveorderBarcode> pvScannedBarcodesObl, String actionType) {return this.Repository.pMoveNewItemHandledViaWebserviceWrs(pvBinCodeStr,pvScannedBarcodesObl, actionType );}
    public cWebresult pMoveItemHandledViaWebserviceWrs() {return this.Repository.pMoveItemHandledViaWebserviceWrs();}

    public cWebresult pMoveItemPlaceHandledViaWebserviceWrs() {return this.Repository.pMoveItemPlaceHandledViaWebserviceWrs();}

    public List<cMoveorderLineEntity> pGetLinesFromDatabaseObl(String pvBincode){return  this.Repository.pGetMoveorderLinesForBincodeFromDatabaseObl(pvBincode);}
    public Double pGetTotalCountDbl(){return  this.Repository.pGetTotalCountDbl();}
    public Double pGetCountForBinCodeDbl(String pvBincode){return  this.Repository.pGetCountForBinCodeDbl(pvBincode);}
    public cWebresult pResetLineViaWebserviceWrs (){return this.Repository.pResetLineViaWebserviceWrs();}
    public Boolean pUpdateQuantityBln(){return  this.Repository.pUpdateQuantityBln();}

    public List<cMoveorderLineEntity> pGetLinesForBinItemNoVariantCodeFromDatabaseObl() {return this.Repository.pGetLinesForBinItemNoVariantCodeFromDatabaseObl();}

}


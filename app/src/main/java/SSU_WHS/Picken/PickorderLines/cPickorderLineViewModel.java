package SSU_WHS.Picken.PickorderLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderLineViewModel extends AndroidViewModel {

    //Region Public Properties
    private final cPickorderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPickorderLineRepository(pvApplication);
    }
    //End Region Constructor

    public void insert(cPickorderLineEntity pvPickorderLineEntity) {this.Repository.pInsert(pvPickorderLineEntity);}
    public void deleteAll() {this.Repository.pTruncate();}
    public void delete(cPickorderLineEntity pvPickorderLineEntity) {this.Repository.pDelete(pvPickorderLineEntity);}

    public boolean pUpdateQuantityHandledBln(Double pvQuantityHandledDbl) {return this.Repository.pUpdateQuantityHandledBln(pvQuantityHandledDbl);}
    public boolean pUpdateProcessingSequenceBln(String pvProcessingSequenceStr) {return this.Repository.pUpdateProcessingSequenceBln(pvProcessingSequenceStr);}
    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {return this.Repository.pUpdateLocalStatusBln(pvNewStatusInt);}
    public boolean pUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {return this.Repository.pUpdateLocalHandledTimeStampBln(pvHandledTimeStampStr);}

    public cWebresult pPickLineHandledViaWebserviceWrs() {return this.Repository.pPickLineHandledViaWebserviceWrs();}
    public cWebresult pPickLineGeneratedHandledViaWebserviceWrs(List<cPickorderBarcode> pvScannedBarcodeObl) {return this.Repository.pPickLineGeneratedHandledViaWebserviceWrs(pvScannedBarcodeObl);}
    public cWebresult pPickLinePropertysHandledViaWebserviceWrs() {return this.Repository.pPickLinePropertysHandledViaWebserviceWrs();}
    public cWebresult pPickLineSortedViaWebserviceWrs() {return this.Repository.pPickLineSortedViaWebserviceWrs();}
    public cWebresult pResetViaWebserviceWrs() {return this.Repository.pResetViaWebserviceWrs();}
    public cWebresult pResetGeneratedViaWebserviceWrs() {return this.Repository.pResetGeneratedViaWebserviceWrs();}
    public cWebresult pGetSortLocationAdviceViaWebserviceWrs(String pvSourceNoStr) {return this.Repository.pGetSortLocationAdviceViaWebserviceWrs(pvSourceNoStr);}

    public cWebresult pAddERPBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan, boolean pvIsUniqueBarcodeBln) {return  this.Repository.pAddERPBarcodeViaWebserviceWrs(pvBarcodeScan, pvIsUniqueBarcodeBln);}

}

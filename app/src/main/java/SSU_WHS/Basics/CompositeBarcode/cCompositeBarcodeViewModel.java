package SSU_WHS.Basics.CompositeBarcode;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroupRepository;
import SSU_WHS.Webservice.cWebresult;

public class cCompositeBarcodeViewModel extends AndroidViewModel {
    //Region Public Properties
    public cCompositeBarcodeRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cCompositeBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cCompositeBarcodeRepository(pvApplication);
    }
    //End Region Constructor


    //Region Public Methods
    public cWebresult pGetCompositeBarcodesFromWebserviceWrs() {return this.Repository.pGetCompositeBarcodesFromWebserviceWrs(); }
    public void insert(cCompositeBarcodeEntity pvCompositeBarcodeEntity) {this.Repository.pInsert(pvCompositeBarcodeEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods
}

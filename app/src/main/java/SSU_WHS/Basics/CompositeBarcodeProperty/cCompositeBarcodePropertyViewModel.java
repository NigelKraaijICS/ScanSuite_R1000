package SSU_WHS.Basics.CompositeBarcodeProperty;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cCompositeBarcodePropertyViewModel extends AndroidViewModel {
    //Region Public Properties
    public cCompositeBarcodePropertyRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cCompositeBarcodePropertyViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cCompositeBarcodePropertyRepository(pvApplication);
    }
    //End Region Constructor


    //Region Public Methods
    public void insert(cCompositeBarcodePropertyEntity pvCompositeBarcodePropertyEntity) {this.Repository.pInsert(pvCompositeBarcodePropertyEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods
}

package SSU_WHS.Move.MoveorderLineBarcode;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeEntity;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeRepository;
import SSU_WHS.Webservice.cWebresult;

public class cMoveorderLineBarcodeViewModel extends AndroidViewModel {
    //Region Public Properties
    private cMoveorderLineBarcodeRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cMoveorderLineBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cMoveorderLineBarcodeRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cMoveorderLineBarcodeEntity pvMoveorderLineBarcodeEntity) {this.Repository.insert(pvMoveorderLineBarcodeEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

}

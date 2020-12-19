package SSU_WHS.PackAndShip.PackAndShipLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPackAndShipOrderLineViewModel extends AndroidViewModel {
    //Region Public Properties
    private cPackAndShipOrderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipOrderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipOrderLineRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipOrderLineEntity pvPackAndShipOrderLineEntity) {this.Repository.insert(pvPackAndShipOrderLineEntity);}
    public void delete(cPackAndShipOrderLineEntity pvPackAndShipOrderLineEntity) {this.Repository.delete(pvPackAndShipOrderLineEntity);}
    public void deleteAll() {this.Repository.deleteAll();}



}


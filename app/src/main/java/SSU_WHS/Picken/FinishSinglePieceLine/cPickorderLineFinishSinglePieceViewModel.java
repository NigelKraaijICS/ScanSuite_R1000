package SSU_WHS.Picken.FinishSinglePieceLine;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cPickorderLineFinishSinglePieceViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderLineFinishSinglePieceRepository pickorderLinePackAndShipRepository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderLineFinishSinglePieceViewModel(Application pvApplication) {
        super(pvApplication);
        this.pickorderLinePackAndShipRepository = new cPickorderLineFinishSinglePieceRepository();
    }
    //End Region Constructor


    public cWebresult pPrintDocumentsViaWebserviceWrs() {return this.pickorderLinePackAndShipRepository.pPrintDocumentViaWebserviceWrs();}



}

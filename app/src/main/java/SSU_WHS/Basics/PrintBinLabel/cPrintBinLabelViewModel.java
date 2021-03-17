package SSU_WHS.Basics.PrintBinLabel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Basics.LabelTemplate.cLabelTemplateEntity;
import SSU_WHS.Basics.LabelTemplate.cLabelTemplateRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPrintBinLabelViewModel extends AndroidViewModel {

    //Region Public Properties
    public cPrintBinLabelRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public  cPrintBinLabelViewModel(Application pvApplication){
        super(pvApplication);
        this.Repository = new cPrintBinLabelRepository();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pPrintBinLabelViaWebserviceWrs() {return this.Repository.pPrintBinLabelViaWebserviceWrs();}

    //END Region Public Methods

}

package SSU_WHS.Basics.LabelTemplate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cLabelTemplateViewModel extends AndroidViewModel {

    //Region Public Properties
    public cLabelTemplateRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public  cLabelTemplateViewModel(Application pvApplication){
        super(pvApplication);
        this.Repository = new cLabelTemplateRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetLabelTemplatesFromWebserviceWrs() {return this.Repository.pGetLabelTemplateFromWebserviceWrs();}
    public void insert(cLabelTemplateEntity labelTemplateEntity) {this.Repository.insert(labelTemplateEntity); }
    public void deleteAll() {Repository.deleteAll();}
    //END Region Public Methods

}

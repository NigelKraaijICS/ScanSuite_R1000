package ICS.Environments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class cEnvironmentViewModel extends AndroidViewModel {

    //Region Public Properties
    public cEnvironmentRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cEnvironmentViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cEnvironmentRepository(pvApplication);
    }
   //End Region Constructor

    //Region Public Methods
    public List<cEnvironmentEntity> getAll() {return Repository.getAllEnviromentsFromDatabase();}
    public void insert(cEnvironmentEntity environmentEntity) {Repository.insert(environmentEntity);}
    public void delete(cEnvironmentEntity environmentEntity) {Repository.delete(environmentEntity);}
    public Integer updateDefaultBln(Boolean pvDefaultBln, String pvNameStr) {return Repository.updateDefault(pvDefaultBln, pvNameStr);}
    public void deleteAll() {Repository.deleteAll();}

    //End Region Public Methods

}

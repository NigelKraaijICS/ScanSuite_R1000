package SSU_WHS.Picken.PickorderShipPackages;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cPickorderShipPackage {

    //Region Public Properties

    public String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    public String shippingAgentCodeStr;
    public String getShippingAgentCodeStrStr() {
        return shippingAgentCodeStr;
    }

    public String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStrStr() {
        return shippingAgentServiceCodeStr;
    }

    public String packageTypeStr;
    public String getPackageTypeStr() {
        return packageTypeStr;
    }

    public int packageSequenceNoInt;
    public int getPackageSequenceNoInt() {
        return packageSequenceNoInt;
    }

    public boolean inDatabaseBln;

    public static cPickorderShipPackageViewModel pickorderShipPackageViewModel;
    public static cPickorderShipPackageViewModel getPickorderShipPackageViewModel() {
        if (pickorderShipPackageViewModel == null){
            pickorderShipPackageViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cPickorderShipPackageViewModel.class);
        }
        return pickorderShipPackageViewModel;
    }

    public cPickorderShipPackageEntity pickorderShipPackageEntity;
    public static List<cPickorderShipPackage> allPackagesObl;

    //End Region Public Properties

    //Region Constructor

    public cPickorderShipPackage(JSONObject pvJsonObject){

        this.pickorderShipPackageEntity = new cPickorderShipPackageEntity(pvJsonObject);

        this.sourceNoStr = this.pickorderShipPackageEntity.getSourcenoStr();
        this.shippingAgentCodeStr = this.pickorderShipPackageEntity.getShippingAgentCodeStr();
        this.shippingAgentServiceCodeStr = this.pickorderShipPackageEntity.getShippingAgentServiceCodeStr();
        this.packageTypeStr = this.pickorderShipPackageEntity.getPackageTypeStr();
        this.packageSequenceNoInt = cText.pStringToIntegerInt(this.pickorderShipPackageEntity.getPackageSequenceNumberStr());

    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cPickorderShipPackage.getPickorderShipPackageViewModel().insert(this.pickorderShipPackageEntity);
        this.inDatabaseBln = true;

        if (cPickorderShipPackage.allPackagesObl == null);
        {cPickorderShipPackage.allPackagesObl  = new ArrayList<>();
        }
        cPickorderShipPackage.allPackagesObl .add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cPickorderShipPackage.getPickorderShipPackageViewModel().deleteAll();
        return true;
    }

    //End Region Public Methods

}

package SSU_WHS.Picken.PickorderShipPackages;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;

public class cPickorderShipPackage {

    //Region Public Properties

    private String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    private String shippingAgentCodeStr;
    public String getShippingAgentCodeStrStr() {
        return shippingAgentCodeStr;
    }

    private String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStr() {
        return shippingAgentServiceCodeStr;
    }

    private String packageTypeStr;
    public String getPackageTypeStr() {
        return packageTypeStr;
    }

    private int packageSequenceNoInt;
    public int getPackageSequenceNoInt() {
        return packageSequenceNoInt;
    }

    private cPickorderShipPackageEntity pickorderShipPackageEntity;
    public static List<cPickorderShipPackage> allPackagesObl;

    private cPickorderShipPackageViewModel getPickorderShipPackageViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderShipPackageViewModel.class);
    }


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
        this.getPickorderShipPackageViewModel().insert(this.pickorderShipPackageEntity);


        if (cPickorderShipPackage.allPackagesObl == null){
            cPickorderShipPackage.allPackagesObl  = new ArrayList<>();
        }

        cPickorderShipPackage.allPackagesObl .add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){

        cPickorderShipPackageViewModel pickorderShipPackageViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderShipPackageViewModel.class);
        pickorderShipPackageViewModel.deleteAll();
        return true;
    }

    //End Region Public Methods

}

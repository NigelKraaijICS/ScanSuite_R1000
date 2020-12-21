package SSU_WHS.PackAndShip.PackAndShipShippingPackage;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressEntity;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressViewModel;

public class cPackAndShipShippingPackage {

    private cPackAndShipShippingPackageEntity packAndShipShippingPackageEntity;

    public static List<cPackAndShipShippingPackage> allPackagesObl;
    //Region Public Properties

    public String sourceNoStr;
    public String getSourceNoStr() {
        return this.sourceNoStr;
    }

    public String shippingAgentCodeStr;
    public String getShippingAgentCodeStr() {
        return this.shippingAgentCodeStr;
    }

    public String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStr() {
        return this.shippingAgentServiceCodeStr;
    }

    public String packageTypeStr;
    public String getPackageTypeStr() {
        return this.packageTypeStr;
    }

    public int packageSequenceNumberInt;
    public int getPackageSequenceNumberInt() {
        return this.packageSequenceNumberInt;
    }

    public int packageItemCountInt;
    public int getPackageItemCountInt() {
        return this.packageItemCountInt;
    }

    public int packageWeightInGInt;
    public int getPackageWeightInGInt() {
        return this.packageWeightInGInt;
    }

    public String packageContainerTypeStr;
    public String getPackageContainerTypeStr() {
        return this.packageContainerTypeStr;
    }

    public String packageContainerStr;
    public String getPackageContainerStr() {
        return this.packageContainerStr;
    }

    private cPackAndShipShippingPackageViewModel getPackAndShipShippingPackageViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipShippingPackageViewModel.class);
    }

    //Region Constructor
    public cPackAndShipShippingPackage(JSONObject pvJsonObject) {
        this.packAndShipShippingPackageEntity = new cPackAndShipShippingPackageEntity(pvJsonObject);
        this.sourceNoStr = this.packAndShipShippingPackageEntity.getSourceNoStr();
        this.shippingAgentCodeStr = this.packAndShipShippingPackageEntity.getShippingAgentCodeStr();
        this.shippingAgentServiceCodeStr = this.packAndShipShippingPackageEntity.getShippingAgentServiceCodeStr();
        this.packageTypeStr = this.packAndShipShippingPackageEntity.getPackageTypeStr();
        this.packageSequenceNumberInt = this.packAndShipShippingPackageEntity.getPackageSequenceNumberInt();
        this.packageItemCountInt = this.packAndShipShippingPackageEntity.getPackageItemCountInt();
        this.packageWeightInGInt = this.packAndShipShippingPackageEntity.getPackageWeightInGInt();
        this.packageContainerTypeStr = this.packAndShipShippingPackageEntity.getPackageContainerTypeStr();
        this.packageContainerStr = this.packAndShipShippingPackageEntity.getPackageContainerStr();
    }
    //End Region Constructor


    public static boolean pTruncateTableBln(){

        cPackAndShipShippingPackageViewModel packAndShipShippingPackageViewModel=   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipShippingPackageViewModel.class);
        packAndShipShippingPackageViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getPackAndShipShippingPackageViewModel().insert(this.packAndShipShippingPackageEntity);

        if (cPackAndShipShippingPackage.allPackagesObl == null){
            cPackAndShipShippingPackage.allPackagesObl = new ArrayList<>();
        }
        cPackAndShipShippingPackage.allPackagesObl.add(this);
        return  true;
    }

}
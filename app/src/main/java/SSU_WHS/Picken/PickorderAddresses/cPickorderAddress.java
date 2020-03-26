package SSU_WHS.Picken.PickorderAddresses;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;

public class cPickorderAddress {

    private String addrescode;
    public String getAddrescodeStr() { return addrescode; }

    private String name;
    public String getNameStr() { return name; }

    private String nameAddition;
    public String getNameAdditionStr() { return nameAddition; }

    private String address;
    public String getAddressStr() { return address; }

    private String addressAddition;
    public String getAddressAdditionStr() { return addressAddition; }

    private String street;
    public String getStreetStr() { return street; }

    private String addressNumber;
    public String getAddressNumberStr() { return addressNumber; }

    private String zipcode;
    public String getZipcodeStr() { return zipcode; }

    private String city;
    public String getCityStr() { return city; }

    private String country;
    public String getCountryStr() { return country; }


    private cPickorderAddressViewModel getPickorderAddressViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderAddressViewModel.class);
    }

    private cPickorderAddressEntity pickorderAddressEntity;
    public static List<cPickorderAddress> allAdressesObl;

    //Region Constructor

    public cPickorderAddress(JSONObject pvJsonObject) {
        this.pickorderAddressEntity = new cPickorderAddressEntity(pvJsonObject);
        this.addrescode = this.pickorderAddressEntity.getAddrescodeStr();
        this.name = this.pickorderAddressEntity.getNameStr();
        this.nameAddition = this.pickorderAddressEntity.getNameAdditionStr();
        this.address = this.pickorderAddressEntity.getAddressStr();
        this.addressAddition = this.pickorderAddressEntity.getAddressAdditionStr();
        this.street = this.pickorderAddressEntity.getStreetStr();
        this.addressNumber = this.pickorderAddressEntity.getAddressNumberStr();
        this.addressAddition = this.pickorderAddressEntity.getAddressNumberAdditionStr();
        this.zipcode = this.pickorderAddressEntity.getZipcodeStr();
        this.city = this.pickorderAddressEntity.getCityStr();
        this.country = this.pickorderAddressEntity.getCountryStr();
    }
    //End Region Constructor

    public boolean pInsertInDatabaseBln() {

        this.getPickorderAddressViewModel().insert(this.pickorderAddressEntity);

        if (cPickorderAddress.allAdressesObl == null) {
            cPickorderAddress.allAdressesObl = new ArrayList<>();
        }

        cPickorderAddress.allAdressesObl.add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cPickorderAddressViewModel pickorderAddressViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderAddressViewModel.class);
        pickorderAddressViewModel.deleteAll();
        return true;
    }


}

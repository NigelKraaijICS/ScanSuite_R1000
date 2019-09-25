package SSU_WHS.Picken.PickorderAddresses;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.icsvertex.scansuite.cAppExtension;

public class cPickorderAddress {

public String addrescode;
public String getAddrescodeStr() { return addrescode; }

public String name;
public String getNameStr() { return name; }

public String nameAddition;
public String getNameAdditionStr() { return nameAddition; }

public String address;
public String getAddressStr() { return address; }

public String addressAddition;
public String getAddressAdditionStr() { return addressAddition; }

public String street;
public String getStreetStr() { return street; }

public String addressNumber;
public String getAddressNumberStr() { return addressNumber; }

public String addressNumberAddition;
public String getAddressNumberAdditionStr() { return addressNumberAddition; }

public String zipcode;
public String getZipcodeStr() { return zipcode; }

public String city;
public String getCityStr() { return city; }

public String country;
public String getCountryStr() { return country; }

public boolean inDatabaseBln;
public static Boolean pickOrderAdressAvailableBln;

public static cPickorderAddressViewModel pickorderAddressViewModel;
public static cPickorderAddressViewModel getPickorderAddressViewModel() {
        if (pickorderAddressViewModel == null){
        pickorderAddressViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cPickorderAddressViewModel.class);
        }
        return pickorderAddressViewModel;
        }

public cPickorderAddressEntity pickorderAddressEntity;
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
            cPickorderAddress.getPickorderAddressViewModel().insert(this.pickorderAddressEntity);
            this.inDatabaseBln = true;

            if (cPickorderAddress.allAdressesObl == null);
        {cPickorderAddress.allAdressesObl = new ArrayList<>();
        }
        cPickorderAddress.allAdressesObl.add(this);
            return true;
    }

    public static boolean pTruncateTableBln(){
        cPickorderAddress.getPickorderAddressViewModel().deleteAll();
        return true;
    }

     public static cPickorderAddress pCheckPickorderAdress(String pvAdresscodeStr){
            if (cPickorderAddress.allAdressesObl == null || cPickorderAddress.allAdressesObl.size()==0) {
                return null;
            }
            for (cPickorderAddress adressCode : cPickorderAddress.allAdressesObl)
            {
               if( adressCode.addrescode.equalsIgnoreCase(pvAdresscodeStr) == true)
               {return adressCode;}
            }
            return null;
    }
}

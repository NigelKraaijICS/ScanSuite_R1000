package SSU_WHS.Basics.PropertyGroup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ItemProperty.cItemPropertyViewModel;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupProperty;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupPropertyEntity;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPropertyGroup {

    private String propertyGroupStr;
    public String getPropertyGroupStr() { return propertyGroupStr; }

    private String descriptionStr;
    public String getDescriptionStr() { return descriptionStr; }

    private String shortNameStr;
    public String getShortNameStr() { return shortNameStr; }

    private String imageBase64Str;
    public String getImageBase64Str() { return imageBase64Str; }

    public ArrayList<cPropertyGroupProperty> propertyObl;

    public Bitmap imageBitmap(){
        byte[] decodedString = Base64.decode(this.getImageBase64Str(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Boolean propertyGroupsAvailableBln;

    private cPropertyGroupViewModel getPropertyGroupViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPropertyGroupViewModel.class);
    }

    private cPropertyGroupEntity propertyGroupEntity;
    public  static List<cPropertyGroup> allPropertyGroupsObl;

    //Region Constructor
    cPropertyGroup(JSONObject pvJsonObject) {
        this.propertyGroupEntity = new cPropertyGroupEntity(pvJsonObject);
        this.propertyGroupStr = this.propertyGroupEntity.getPropertyGroupStr();
        this.descriptionStr = this.propertyGroupEntity.getDescriptionStr();
        this.shortNameStr = this.propertyGroupEntity.getShortName();
        this.imageBase64Str = this.propertyGroupEntity.getImageBase64Str();
    }

    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        this.getPropertyGroupViewModel().insert(this.propertyGroupEntity);

        if (cPropertyGroup.allPropertyGroupsObl == null) {
            cPropertyGroup.allPropertyGroupsObl = new ArrayList<>();
        }
        cPropertyGroup.allPropertyGroupsObl.add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cPropertyGroupViewModel propertyGroupViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPropertyGroupViewModel.class);
        propertyGroupViewModel.deleteAll();
        return true;
    }

    public static boolean pGetPropertyGroupsViaWebserviceBln(Boolean pvRefreshBln) {


        if (pvRefreshBln) {
            cPropertyGroup.allPropertyGroupsObl = null;
            cPropertyGroup.pTruncateTableBln();
        }

        if (cPropertyGroup.allPropertyGroupsObl != null) {
            return  true;
        }

        cWebresult WebResult;
        cPropertyGroupViewModel propertyGroupViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPropertyGroupViewModel.class);
        WebResult =  propertyGroupViewModel.pGetPropertyGroupsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPropertyGroup propertyGroup = new cPropertyGroup(jsonObject);
                propertyGroup.pInsertInDatabaseBln();

                try {
                    JSONArray propertys    = jsonObject.getJSONArray(cDatabase.PROPERTYSDUTCH_NAMESTR);
                    propertyGroup.propertyObl = new ArrayList<>();

                    for (int i = 0, size = propertys.length(); i < size; i++)
                    {
                        JSONObject object =propertys.getJSONObject(i);
                        cPropertyGroupProperty propertyGroupProperty = new cPropertyGroupProperty(object);
                        propertyGroup.propertyObl.add(propertyGroupProperty);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            cPropertyGroup.propertyGroupsAvailableBln = true;
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPROPERTYGROUPS);
            return  false;
        }
    }


    //End Region Public Methods

    //Region Private Methods



    //End Region Private Methods
}

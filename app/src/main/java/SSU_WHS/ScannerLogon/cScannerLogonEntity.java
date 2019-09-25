package SSU_WHS.ScannerLogon;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SCANNERLOGON, primaryKeys = {"ScannerDescription"})
public class cScannerLogonEntity {

    //Region Public Properties
    @ColumnInfo(name = "FixedScannerBranch")
    public String fixedscannerbranch;
    public String getFixedscannerBranchStr() {return fixedscannerbranch;}

    @NonNull
    @ColumnInfo(name = "ScannerDescription")
    public String scannerdescription;
    public String getScannerDescriptionStr() {return  scannerdescription;}

    @ColumnInfo(name = "RequiredScannerVersion")
    public String requiredscannerversion;
    public String getRequiredScannerVersionStr() {return requiredscannerversion;}

    @ColumnInfo(name = "ApplicationEnvironment")
    public String applicationenvironment;
    public String getApplicationEnvironmentStr() {return applicationenvironment;}

    @ColumnInfo(name = "Languages")
    public String languages;
    public String getLanguagesStr() {return languages;}

    @ColumnInfo(name = "RequiredScannerConfiguration")
    public String requiredscannerconfiguration;
    public String getRequiredScannerConfigurationStr() {return requiredscannerconfiguration;}

    @ColumnInfo(name = "ReapplyScannerConfiguration")
    public String reapplyscannerconfiguration;
    public String getReapplyScannerConfigurationStr() {return reapplyscannerconfiguration;}

    @ColumnInfo(name = "VersionConfigAppCurrentScanner")
    public String versionconfigappcurrentscanner;
    public String getVersionConfigAppCurrentScannerStr() {return versionconfigappcurrentscanner;}

    @ColumnInfo(name = "VersionConfigAppRequiredScanner")
    public String versionconfigapprequiredscanner;
    public String getVersionConfigAppRequiredScannerStr() {return versionconfigapprequiredscanner;}

    @ColumnInfo(name = "ColorSet")
    public String colorset;
    public String getColorsetStr() {return colorset;}

    //End Region Public Properies

    //Region Constructor
    public cScannerLogonEntity() {

    }


    public cScannerLogonEntity(List<String> pvScannerStringObl) {
                this.fixedscannerbranch = pvScannerStringObl.get(0);
                this.scannerdescription = pvScannerStringObl.get(1);
                this.requiredscannerversion = pvScannerStringObl.get(2);
                this.applicationenvironment =  pvScannerStringObl.get(3);
                this.languages =   pvScannerStringObl.get(4);
                this.requiredscannerconfiguration =  pvScannerStringObl.get(5);
                this.reapplyscannerconfiguration =  pvScannerStringObl.get(6);
                this.versionconfigappcurrentscanner =  pvScannerStringObl.get(7);
                this.versionconfigapprequiredscanner = pvScannerStringObl.get(8);
                this.colorset =  pvScannerStringObl.get(9);
        }
    //End Region Constructor
    }




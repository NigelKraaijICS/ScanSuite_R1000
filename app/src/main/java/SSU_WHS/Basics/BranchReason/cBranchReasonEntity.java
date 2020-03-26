package SSU_WHS.Basics.BranchReason;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;
@Entity(tableName= cDatabase.TABLENAME_BRANCHREASON)
public class cBranchReasonEntity {
        //Region Public Properties

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = cDatabase.REASONNL_NAMESTR)
        private String reasonStr = "";
        @NonNull
        public String getReasonStr() {
            return this.reasonStr;
        }

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = cDatabase.DESCRIPTION_DUTCH_NAMESTR)
        private String descriptionStr = "";
        @NonNull
        public String getDescriptionStr() {
            return this.descriptionStr;
        }

        @ColumnInfo(name = cDatabase.RETURN_NAMESTR)
        private Boolean returnBln;
        public Boolean getReturnBln() {
            return this.returnBln;
        }


        //End Region Public Properies

    public cBranchReasonEntity(JSONObject jsonObject) {
            try {
                this.reasonStr = jsonObject.getString(cDatabase.REASONNL_NAMESTR);
                this.descriptionStr = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
                this.returnBln = cText.pStringToBooleanBln(jsonObject.getString(cDatabase.RETURN_NAMESTR), false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //End Region Constructor
    }


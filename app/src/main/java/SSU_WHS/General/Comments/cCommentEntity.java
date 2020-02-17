package SSU_WHS.General.Comments;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_COMMENT, primaryKeys = {cDatabase.COMMENTLINENO_NAMESTR})
public class cCommentEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.COMMENTLINENO_NAMESTR)
    public Long commentLineNoLng;
    public Long getCommentLineNoLng() {
        return commentLineNoLng;
    }

    @ColumnInfo(name = cDatabase.COMMENTCODE_NAMESTR)
    public String commentcodeStr;
    public String getCommentcodeStr() {
        return commentcodeStr;
    }

    @ColumnInfo(name = cDatabase.COMMENTTEXT_NAMESTR)
    public String commentTextStr;
    public String getCommentTextStr() {
        return commentTextStr;
    }

    //empty constructor
    public cCommentEntity() {
    }

    public cCommentEntity(JSONObject jsonObject) {
        try {
            commentLineNoLng = jsonObject.getLong(cDatabase.COMMENTLINENO_NAMESTR);
            commentcodeStr = jsonObject.getString(cDatabase.COMMENTCODE_NAMESTR);
            commentTextStr = jsonObject.getString(cDatabase.COMMENTTEXT_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

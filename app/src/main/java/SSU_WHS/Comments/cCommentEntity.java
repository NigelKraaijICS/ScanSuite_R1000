package SSU_WHS.Comments;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_COMMENT, primaryKeys = {cDatabase.COMMENTLINENO_NAMESTR})
public class cCommentEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.COMMENTLINENO_NAMESTR)
    public Integer commentlineno;
    @ColumnInfo(name = cDatabase.COMMENTCODE_NAMESTR)
    public String commentcode;
    @ColumnInfo(name = cDatabase.COMMENTTEXT_NAMESTR)
    public String commenttext;

    //empty constructor
    public cCommentEntity() {

    }

    public cCommentEntity(JSONObject jsonObject) {
        try {
            commentlineno = jsonObject.getInt(cDatabase.COMMENTLINENO_NAMESTR);
            commentcode = jsonObject.getString(cDatabase.COMMENTCODE_NAMESTR);
            commenttext = jsonObject.getString(cDatabase.COMMENTTEXT_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Integer getCommentlineno() {
        return commentlineno;
    }

    public void setCommentlineno(Integer commentlineno) {
        this.commentlineno = commentlineno;
    }

    public String getCommentcode() {
        return commentcode;
    }

    public void setCommentcode(String commentcode) {
        this.commentcode = commentcode;
    }

    public String getCommenttext() {
        return commenttext;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }
}

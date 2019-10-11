package SSU_WHS.Basics.ArticleImages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.List;

import ICS.cAppExtension;

public class cArticleImage {

    //Region Public Properties

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public String imageStr;
    public String getImageStr() {
        return imageStr;
    }

    public String dataTimeStampStr;
    public String getDataTimeStampStr() {
        return dataTimeStampStr;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessagesObl() {
        return errorMessagesObl;
    }

    public Bitmap imageBitmap(){
        byte[] decodedString = Base64.decode(this.getImageStr(), Base64.DEFAULT);
        Bitmap resultBmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
       return  resultBmp;
    }




    public cArticleImageEntity articleImageEntity;
    public boolean inDatabaseBln;

    public static cArticleImageViewModel gArticleImageViewModel;

    public static cArticleImageViewModel getArticleImageViewModel() {
        if (gArticleImageViewModel == null) {
            gArticleImageViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cArticleImageViewModel.class);
        }
        return gArticleImageViewModel;
    }

    public static List<cArticleImage> allImages;

    //End Region Public Properties

    //Region Constructor
    public cArticleImage(JSONObject pvJsonObject) {
        this.articleImageEntity = new cArticleImageEntity(pvJsonObject);
        this.itemNoStr = this.articleImageEntity.getItemnoStr();
        this.variantCodeStr =  this.articleImageEntity.getVariantCodeStr();
        this.imageStr = this.articleImageEntity.getImageStr();
        this.dataTimeStampStr = this.articleImageEntity.getDataTimeStampStr();
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cArticleImage.getArticleImageViewModel().insert(this.articleImageEntity);
        this.inDatabaseBln = true;
        return true;
    }

    public static boolean pTruncateTableBln(){
        cArticleImage.getArticleImageViewModel().deleteAll();
        return true;
    }
    public static cArticleImage pGetArticleImageByItemNoAndVariantCode(String pvItemNoStr, String pvVariantCodeStr){

        if (cArticleImage.allImages == null || cArticleImage.allImages.size() == 0) {
            return  null;
        }

        for (cArticleImage articleImage : cArticleImage.allImages) {

            if (articleImage.itemNoStr.equalsIgnoreCase(pvItemNoStr) && articleImage.variantCodeStr.equalsIgnoreCase(pvVariantCodeStr)) {
                return  articleImage;
            }

        }
        return  null;
    }


    //End Region Public Methods
}

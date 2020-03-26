package SSU_WHS.Basics.ArticleImages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.lifecycle.ViewModelProvider;

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

    private String imageStr;
    public String getImageStr() {
        return imageStr;
    }

    public Bitmap imageBitmap(){
        byte[] decodedString = Base64.decode(this.getImageStr(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private cArticleImageEntity articleImageEntity;

    private cArticleImageViewModel getArticleImageViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
    }

    public static List<cArticleImage> allImages;

    //End Region Public Properties

    //Region Constructor
    public cArticleImage(JSONObject pvJsonObject) {
        this.articleImageEntity = new cArticleImageEntity(pvJsonObject);
        this.itemNoStr = this.articleImageEntity.getItemnoStr();
        this.variantCodeStr =  this.articleImageEntity.getVariantCodeStr();
        this.imageStr = this.articleImageEntity.getImageStr();
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getArticleImageViewModel().insert(this.articleImageEntity);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cArticleImageViewModel articleImageViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
        articleImageViewModel.deleteAll();
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

package ICS;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ICS.Environments.cEnvironmentEntity;
import ICS.Environments.iEnvironmentDao;
import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.iWeberrorDao;

@Database(entities = {
        cEnvironmentEntity.class,
        cWeberrorEntity.class
        },version = 6)
public abstract class acICSDatabase extends RoomDatabase {
    public abstract iEnvironmentDao environmentDao();
    public abstract iWeberrorDao weberrorDao();

    private static acICSDatabase INSTANCE;

    public static acICSDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (acICSDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), acICSDatabase.class, "ICS").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}

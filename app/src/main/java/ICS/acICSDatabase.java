package ICS;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.iWeberrorDao;
import ICS.Environments.cEnvironmentEntity;
import ICS.Environments.iEnvironmentDao;

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

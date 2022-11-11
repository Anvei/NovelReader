package org.anvei.novelreader;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.anvei.novelreader.beans.NativeNovel;
import org.anvei.novelreader.beans.WebsiteNovel;
import org.anvei.novelreader.dao.NativeNovelDao;
import org.anvei.novelreader.dao.WebsiteNovelDao;
import org.anvei.novelreader.utils.Converters;

@Database(entities = {WebsiteNovel.class, NativeNovel.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "AppDatabase";

    private static volatile AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

    public abstract WebsiteNovelDao getWebsiteNovelDao();

    public abstract NativeNovelDao getNativeNovelDao();
}

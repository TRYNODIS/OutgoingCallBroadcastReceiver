package hu.nagyi.outgoingcallbroadcastreceiver

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    exportSchema = false,
    entities = arrayOf(OutgoingCallEntity::class),
    version = MyRoomDatabase.DATABASE_VERSION
)
abstract class MyRoomDatabase : RoomDatabase() {

    //region VARIABLES

    abstract fun callDao(): OutgoingCallDAO

    companion object {
        const val DATABASE_NAME = "MyDatabase.sqlite";
        const val DATABASE_VERSION = 1;

        @Volatile
        private var Instance: MyRoomDatabase? = null

        fun getInstance(context: Context): MyRoomDatabase {
            return Instance ?: synchronized(this) {
                Instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDatabase::class.java, this.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

    //endregion
}
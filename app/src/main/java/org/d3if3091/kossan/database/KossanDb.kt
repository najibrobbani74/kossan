package org.d3if3091.kossan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3091.kossan.model.Order

@Database(entities = [Order::class], version = 1, exportSchema = false)
abstract class KossanDb : RoomDatabase() {

    abstract val orderDao: OrderDao

    companion object {

        @Volatile
        private var INSTANCE: KossanDb? = null

        fun getInstance(context: Context): KossanDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        KossanDb::class.java,
                        "kossan.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
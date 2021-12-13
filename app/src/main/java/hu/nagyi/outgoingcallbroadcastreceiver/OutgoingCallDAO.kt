package hu.nagyi.outgoingcallbroadcastreceiver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OutgoingCallDAO {

    @Query("SELECT * FROM OutgoingCall")
    fun getAllCalls(): List<OutgoingCallEntity>

    @Insert
    fun insertCalls(vararg calls: OutgoingCallEntity)
}
package hu.nagyi.outgoingcallbroadcastreceiver

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OutgoingCall")
data class OutgoingCallEntity(

    //region VARIABLES

    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "PhoneNo") var phoneNo: String,
    @ColumnInfo(name = "Date") var date: String

    //endregion
)
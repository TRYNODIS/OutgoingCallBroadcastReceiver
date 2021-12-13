package hu.nagyi.outgoingcallbroadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.util.*

class OutgoingCallBroadcastReceiver : BroadcastReceiver() {

    //region METHODS

    override fun onReceive(context: Context, intent: Intent) {
        val out = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).toString()
        this.saveCalls(context, out)
        println(context.getString(R.string.dataSaved))
        Toast.makeText(
            context,
            context.getString(R.string.calledPhoneNo) + ": " + out,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun saveCalls(context: Context, phoneNo: String) {
        Thread {
            MyRoomDatabase.getInstance(context).callDao()
                .insertCalls(
                    OutgoingCallEntity(
                        null,
                        phoneNo,
                        Date(System.currentTimeMillis()).toString()
                    )
                )
        }.start()
    }

    //endregion
}
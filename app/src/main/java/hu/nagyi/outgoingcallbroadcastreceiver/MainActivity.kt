package hu.nagyi.outgoingcallbroadcastreceiver

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import hu.nagyi.outgoingcallbroadcastreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //region VARIABLES

    companion object {
        const val PERMISSION_REQUEST_CODE: Int = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var outgoingBroadcastReceiver: OutgoingCallBroadcastReceiver
    private var calls: List<OutgoingCallEntity> = ArrayList()


    //endregion

    //region METHODS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        val view = this.binding.root;
        this.setContentView(view)

        this.requestProcessOutgoingCallsPermission()

        this.outgoingBroadcastReceiver = OutgoingCallBroadcastReceiver()

        this.registerReceiver(this.outgoingBroadcastReceiver, IntentFilter(Intent.ACTION_CALL))
    }

    override fun onResume() {
        super.onResume()

        this.refreshRecyclerViewWithData()

        if (this.calls.isEmpty()) {
            Toast.makeText(
                this@MainActivity, getString(R.string.noRecordsToShow),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun refreshRecyclerViewWithData() {
        this.queryCalls()
        Thread.sleep(100)

        this.binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = CallsAdapter(this@MainActivity, this@MainActivity.calls)
        }
    }

    private fun queryCalls() {
        Thread {
            this.calls = MyRoomDatabase.getInstance(this@MainActivity).callDao().getAllCalls()
        }.start()
    }

    //region PERMISSIONS

    private fun requestProcessOutgoingCallsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                this.showRationaleDialog(
                    getString(R.string.requestPermission),
                    getString(R.string.itIsNecessary),
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    MainActivity.PERMISSION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS),
                    MainActivity.PERMISSION_REQUEST_CODE
                )
            }

        } else {
            //Permission is already granted
        }
    }

    fun requestCallPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (this.shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                this.showRationaleDialog(
                    getString(R.string.requestPermission),
                    getString(R.string.itIsNecessary),
                    Manifest.permission.CALL_PHONE,
                    MainActivity.PERMISSION_REQUEST_CODE
                )

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    MainActivity.PERMISSION_REQUEST_CODE
                )
            }

        } else {
            //Permission is already granted

            this.startActivity(
                Intent(
                    Intent.ACTION_CALL,
                    Uri.parse(
                        this.getString(R.string.tel) + this.calls.get((this.binding.mainRecyclerView.adapter as CallsAdapter).getPosition()).phoneNo
                    )
                )
            )
        }
    }

    private fun showRationaleDialog(
        title: String, message: String, permission: String,
        requestCode: Int
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                requestPermissions(arrayOf(permission), requestCode)
            }
        builder.create().show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MainActivity.PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        this@MainActivity, getString(R.string.permGranted),
                        Toast.LENGTH_SHORT
                    ).show()


                } else {
                    Toast.makeText(
                        this@MainActivity, getString(R.string.permNotGranted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    //endregion

    override fun onDestroy() {
        this.unregisterReceiver(this.outgoingBroadcastReceiver)

        super.onDestroy()
    }

    //endregion
}
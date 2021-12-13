package hu.nagyi.outgoingcallbroadcastreceiver

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.nagyi.outgoingcallbroadcastreceiver.databinding.RowDatabaseElementsBinding

class CallsAdapter(private val context: Context, private val calls: List<OutgoingCallEntity>) :
    RecyclerView.Adapter<CallsAdapter.ViewHolder>() {

    //region VARIABLES

    private lateinit var binding: RowDatabaseElementsBinding
    private var position: Int = 0

    //endregion

    //region METHODS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.binding =
            RowDatabaseElementsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(this.binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.phoneNoTV.text = this.calls.get(position).phoneNo;
        holder.dateTV.text = this.calls.get(position).date
        holder.callBtn.setOnClickListener {
            this.setPosition(holder.adapterPosition)
            (this.context as MainActivity).requestCallPhonePermission()
        }
    }

    override fun getItemCount() = this.calls.size

    fun getPosition(): Int {
        return this.position
    }

    private fun setPosition(position: Int) {
        this.position = position
    }

    //endregion

    //region VIEW HOLDER

    inner class ViewHolder(private val binding: RowDatabaseElementsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val phoneNoTV = this.binding.phoneNoTV
        val dateTV = this.binding.dateTV
        val callBtn = this.binding.callBtn
    }

    //endregion
}
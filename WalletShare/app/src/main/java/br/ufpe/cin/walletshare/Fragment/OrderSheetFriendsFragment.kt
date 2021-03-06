package br.ufpe.cin.walletshare.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.ufpe.cin.walletshare.Activity.OrderSheetActivity

import br.ufpe.cin.walletshare.R
import br.ufpe.cin.walletshare.entity.Friend
import br.ufpe.cin.walletshare.util.currencyFormatting
import br.ufpe.cin.walletshare.util.percent
import kotlinx.android.synthetic.main.fragment_order_sheet_friends.*
import kotlinx.android.synthetic.main.item_command_friends.view.*

class OrderSheetFriendsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_sheet_friends, container, false)
    }

    override fun onResume() {
        super.onResume()

        command_friends_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemAdapter(context, OrderSheetActivity.orderSheet.people)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        command_friends_action.hide()
        radio_equally.setOnClickListener {
            command_friends_recycler_view.adapter?.notifyDataSetChanged()
        }
        check_percent.setOnClickListener {
            command_friends_recycler_view.adapter?.notifyDataSetChanged()
        }
        radio_individually.setOnClickListener {
            command_friends_recycler_view.adapter?.notifyDataSetChanged()
        }
    }

    fun reload() {
        command_friends_recycler_view.adapter?.notifyDataSetChanged()
    }

    companion object Factory {
        fun newInstance(): OrderSheetFriendsFragment = OrderSheetFriendsFragment()
    }

    internal inner class ItemAdapter (
        var c: Context,
        var items: MutableList<Friend>) :  RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = LayoutInflater.from(c).inflate(R.layout.item_command_friends, parent, false)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.name

            val normal = OrderSheetActivity.orderSheet.valueFor(item)
            val divided = OrderSheetActivity.orderSheet.split()

            if (radio_equally.isChecked && check_percent.isChecked) {
                holder.price.text = divided.percent(0.1).currencyFormatting()
            }else if (radio_equally.isChecked) {
                holder.price.text = divided.currencyFormatting()
            }else if (radio_individually.isChecked && check_percent.isChecked) {
                holder.price.text = normal.percent(0.1).currencyFormatting()
            }else if (radio_individually.isChecked) {
                holder.price.text = normal.currencyFormatting()
            }

        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        internal inner class ItemHolder(val item: View) : RecyclerView.ViewHolder(item) {
            val title: TextView = item.item_command_friends_name
            val price: TextView = item.item_command_friends_price

            init {
                item.setOnClickListener {
//                    Toast.makeText(c, title.text, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

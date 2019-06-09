package br.ufpe.cin.walletshare.Activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import br.ufpe.cin.walletshare.R
import br.ufpe.cin.walletshare.entity.Friend
import br.ufpe.cin.walletshare.entity.Item
import br.ufpe.cin.walletshare.util.currencyFormatting

import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_item.toolbar
import kotlinx.android.synthetic.main.activity_participants.*
import kotlinx.android.synthetic.main.item_participants.view.*

class ItemActivity : AppCompatActivity() {

    var selected: MutableList<Boolean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(toolbar)

        item_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemAdapter(context, CommandActivity.command.people)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        val isNew = intent.getBooleanExtra("isNew", true)

        CommandActivity.command.people.forEach {
            if (isNew) {
                selected.add(false)
            }else{
                selected.add(item.people.contains(it))
            }
        }

        if (!isNew) {
            item_name_edit_text.setText(item.name)
            item_price_edit_text.setText(item.price.currencyFormatting())
        }

        item_action.setOnClickListener {

            val name = item_name_edit_text.text.toString()
            val price = item_price_edit_text.text.toString()

            val selectedFriend = selectedFriend()

            if (selectedFriend.isEmpty()) {
                Snackbar.make(it, "Selecione no minimo um amigo", Snackbar.LENGTH_LONG).show()
            }else if (name.isEmpty()) {
                Snackbar.make(it, "Preencha o nome do item", Snackbar.LENGTH_LONG).show()
            }else{
                if (isNew) {
                    val item = Item()
                    item.price = 20.0
                    item.name = name
                    item.people = selectedFriend
                    CommandActivity.command.items.add(item)
                }else{
                    item.price = 20.0
                    item.name = name
                    item.people = selectedFriend
                }
                finish()
            }
        }
    }

    companion object Factory {
        var item: Item = Item()
    }

    private fun selectedFriend(): MutableList<Friend> {
        var selectedFriend: MutableList<Friend> = mutableListOf()
        for (i in 0 until selected.count()) {
            if (selected[i]) {
                selectedFriend.add(CommandActivity.command.people[i])
            }
        }
        return  selectedFriend
    }

    internal inner class ItemAdapter (
        var c: Context,
        var items: MutableList<Friend>) :  RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = LayoutInflater.from(c).inflate(R.layout.item_participants, parent, false)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.name
            holder.index = position
            holder.checkBox.isChecked = selected[position]
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        internal inner class ItemHolder(val item: View) : RecyclerView.ViewHolder(item) {
            val title: TextView = item.item_participants_title
            val checkBox: CheckBox = item.item_participants_check_box
            var index: Int = 0

            init {
                item.setOnClickListener {
                    checkBox.isChecked = checkBox.isChecked.not()
                    buttonAction()
                }

                checkBox.setOnClickListener {
                    buttonAction()
                }
            }

            private fun buttonAction() {
                selected.set(index, checkBox.isChecked)
            }
        }
    }

}
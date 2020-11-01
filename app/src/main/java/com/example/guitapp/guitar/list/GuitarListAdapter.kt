package com.example.guitapp.guitar.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.guitapp.R
import com.example.guitapp.guitar.data.Guitar
import com.example.guitapp.guitar.edit.GuitarEditFragment
import kotlinx.android.synthetic.main.guitar_view.view.*

class GuitarListAdapter(
    private val fragment: Fragment
): RecyclerView.Adapter<GuitarListAdapter.ViewHolder>() {

    var items = emptyList<Guitar>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as Guitar
            fragment.findNavController().navigate(R.id.fragment_guitar_edit, Bundle().apply {
                putString(GuitarEditFragment.ITEM_ID, item._id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.guitar_view, parent, false)
        Log.v(javaClass.name, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(javaClass.name, "onBindViewHolder $position")
        val item = items[position]
        holder.itemView.tag = item
        holder.textView.text = item.toDisplay()
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}
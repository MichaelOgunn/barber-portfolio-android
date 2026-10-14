package org.setu.barberportfolio

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.barberportfolio.models.CutStyle
import java.util.Locale

class CutAdapter (
    private var cuts: List<CutStyle>,

    ): RecyclerView.Adapter<CutAdapter.CutViewHolder>() {
    class CutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val styleNameText: TextView = itemView.findViewById(R.id.styleNameText)
        private val notesText: TextView = itemView.findViewById(R.id.notesText)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val  dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fun bind(cut: CutStyle) {
            // Bind the data to the views in the item layout

            styleNameText.text = cut.styleName
            notesText.text = cut.notes
            dateText.text = dateFormat.format(cut.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CutViewHolder {
// Inflate the item layout and create a ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cut, parent, false)
        return CutViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CutViewHolder, position: Int) {
        // Bind the data to the ViewHolder
        val cut = cuts[position]
        holder.bind(cut)
    }

    override fun getItemCount(): Int {
        return cuts.size
    }

    fun updateCuts(newCuts: List<CutStyle>) {
        cuts = newCuts
        notifyDataSetChanged()
    }
}
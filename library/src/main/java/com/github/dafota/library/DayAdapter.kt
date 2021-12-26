package com.github.dafota.library

import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.dafota.library.databinding.ItemDayBinding
import com.github.dafota.library.model.DayOfWeek
import com.github.dafota.library.model.EnumDay
import com.github.dafota.library.utils.faName
import com.github.dafota.library.utils.inflater

class DayAdapter(
    private val items: List<DayOfWeek>
) : RecyclerView.Adapter<DayAdapter.DayOfWeekHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayOfWeekHolder {
        return DayOfWeekHolder(ItemDayBinding.inflate(parent.inflater, parent, false))
    }

    override fun onBindViewHolder(holder: DayOfWeekHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class DayOfWeekHolder(private val binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DayOfWeek) {
            binding.text.background = null
            binding.text.typeface = Typeface.DEFAULT_BOLD
            binding.text.text = item.name.faName

            if (item.name == EnumDay.Friday) {
                binding.text.setTextColor(Color.RED)
            }
        }
    }
}
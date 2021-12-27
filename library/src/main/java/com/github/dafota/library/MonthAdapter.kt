package com.github.dafota.library

import android.graphics.Color
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.dafota.library.databinding.ItemDayBinding
import com.github.dafota.library.model.Day
import com.github.dafota.library.model.EnumDay
import com.github.dafota.library.utils.dp
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class MonthAdapter(
    private val items: List<Day>,
    private val onDaySelected: (Day) -> Unit
) : RecyclerView.Adapter<MonthAdapter.DayHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DayHolder(ItemDayBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.bind(items[position], onDaySelected)
    }

    override fun getItemCount() = items.size

    class DayHolder(private val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Day, onDaySelected: (Day) -> Unit) {
            binding.text.isSelected = day.isSelected

            val context = binding.root.context

            val shape = MaterialShapeDrawable(
                ShapeAppearanceModel.Builder().setAllCorners(CornerFamily.ROUNDED, 18.dp).build()
            )

            val d = RippleDrawable(
                binding.text.textColors.withAlpha(30),
                binding.text.background,
                shape
            )

            ViewCompat.setBackground(binding.text, InsetDrawable(d, 8))

            if (day.isSelected) {
                val d = RippleDrawable(
                    binding.text.textColors.withAlpha(30),
                    ContextCompat.getDrawable(context, R.drawable.shape_circle),
                    shape
                )

                ViewCompat.setBackground(binding.text, InsetDrawable(d, 0))

                if (day.isCurrentDay) {
                    val d = RippleDrawable(
                        binding.text.textColors.withAlpha(30),
                        ContextCompat.getDrawable(context, R.drawable.shape_circle),
                        shape
                    )
                    ViewCompat.setBackground(binding.text, InsetDrawable(d, 0))
                }
            } else {
                if (day.isCurrentDay) {
                    val d = RippleDrawable(
                        binding.text.textColors.withAlpha(30),
                        ContextCompat.getDrawable(context, R.drawable.shape_stroke),
                        shape
                    )
                    ViewCompat.setBackground(binding.text, InsetDrawable(d, 0))
                } else {
                    val d = RippleDrawable(
                        binding.text.textColors.withAlpha(30),
                        null,
                        shape
                    )
                    ViewCompat.setBackground(binding.text, InsetDrawable(d, 0))
                }
            }

            if (day.isForNextMonth || day.isForPreviousMonth) {
                binding.text.text = ""
                binding.text.background = null
                binding.text.isEnabled = false
            } else {
                binding.text.isEnabled = true
                binding.text.text = day.number.toString()
            }

            if (day.name == EnumDay.Friday) {
                binding.text.setTextColor(Color.RED)
            }

            binding.text.setOnClickListener {
                onDaySelected(day)
            }
        }
    }

}
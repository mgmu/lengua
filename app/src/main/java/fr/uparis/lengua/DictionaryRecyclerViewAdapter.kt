package fr.uparis.lengua

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import fr.uparis.lengua.databinding.ItemLayoutBinding

class DictionaryRecyclerViewAdapter(private val model: TranslationViewModel):
    RecyclerView.Adapter<DictionaryRecyclerViewAdapter.VH>() {

    /* List of dictionaries to display */
    var dictionaries: List<Dictionary> = listOf()

    class VH(val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var dictionary: Dictionary? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        /* Binding for dictionary item */
        val itemBinding = ItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = VH(itemBinding)

        /* Selection listener */
        holder.binding.root.setOnClickListener {
            when (model.selectedDictionary.value) {
                null -> { /* No selected dictionary */
                    model.selectedDictionary.value = holder.dictionary
                    notifyItemChanged(dictionaries.indexOf(holder.dictionary))
                }

                holder.dictionary -> { /* Unselect dictionary */
                    model.selectedDictionary.value = null
                    notifyItemChanged(dictionaries.indexOf(holder.dictionary))
                }

                else -> { /* Change selected dictionary */
                    val previous = model.selectedDictionary.value
                    model.selectedDictionary.value = holder.dictionary
                    notifyItemChanged(dictionaries.indexOf(holder.dictionary))
                    notifyItemChanged(dictionaries.indexOf(previous))
                }
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        /* assign corresponding dictionary */
        holder.dictionary = dictionaries[position]

        /* assign values to corresponding fields and item background color */
        with (holder.binding) {
            dictionaryNameTextView.text = holder.dictionary!!.name
            dictionaryLinkTextView.text = holder.dictionary!!.link

            val color =
                if (holder.dictionary == model.selectedDictionary.value)
                    Color.RED   /* selected color */
                else if (position % 2 == 0)
                    Color.BLUE  /* even color */
                else
                    Color.GREEN /* odd color */

            holder.itemView.setBackgroundColor(color)
        }
    }

    override fun getItemCount(): Int = dictionaries.size
}

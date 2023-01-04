package fr.uparis.lengua

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import fr.uparis.lengua.databinding.ItemLayout2Binding

class WordRecyclerViewAdapter(private val model: TranslationViewModel):
    RecyclerView.Adapter<WordRecyclerViewAdapter.VH>() {

    /* List of dictionaries to display */
    var words: List<Word> = listOf()

    class VH(val binding: ItemLayout2Binding): RecyclerView.ViewHolder(binding.root) {
        var word: Word? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        /* Binding for Word item */
        val itemBinding = ItemLayout2Binding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = VH(itemBinding)

        /* Selection listener */
        holder.binding.root.setOnClickListener {
            when (model.selectedWord.value) {
                null -> { /* No selected Word */
                    model.selectedWord.value = holder.word
                    notifyItemChanged(words.indexOf(holder.word))
                }

                holder.word -> { /* Unselect Word */
                    model.selectedWord.value = null
                    notifyItemChanged(words.indexOf(holder.word))
                }

                else -> { /* Change selected Word */
                    val previous = model.selectedWord.value
                    model.selectedWord.value = holder.word
                    notifyItemChanged(words.indexOf(holder.word))
                    notifyItemChanged(words.indexOf(previous))
                }
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        /* assign corresponding Word */
        holder.word = words[position]

        /* assign values to corresponding fields and item background color */
        with (holder.binding) {
            wordWordTextView.text = holder.word!!.word
            wordSourceLanguageTextView.text = holder.word!!.sourceLanguage
            wordDestinationLanguageTextView.text = holder.word!!.destinationLanguage
            wordLinkTextView.text = holder.word!!.link
            wordSwipeTextView.text = holder.word!!.swiped.toString()

            val color =
                if (holder.word == model.selectedWord.value)
                    Color.RED   /* selected color */
                else if (position % 2 == 0)
                    Color.BLUE  /* even color */
                else
                    Color.GREEN /* odd color */

            holder.itemView.setBackgroundColor(color)
        }
    }

    override fun getItemCount(): Int = words.size
}
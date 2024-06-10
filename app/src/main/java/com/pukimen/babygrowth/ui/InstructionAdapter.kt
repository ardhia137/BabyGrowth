import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.data.model.Langkah

class InstructionAdapter(private val instructions: List<Langkah>) :
    RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    inner class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val instructionTextView: TextView = itemView.findViewById(R.id.instruction_text)

        fun bind(instruction: Langkah) {
            instructionTextView.text = "${instruction.step}. ${instruction.deskripsi}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instruction, parent, false)
        return InstructionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.bind(instructions[position])
    }

    override fun getItemCount(): Int {
        return instructions.size
    }
}

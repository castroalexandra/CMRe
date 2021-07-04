package com.example.cmre.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmre.R
import com.example.cmre.entities.Nota


const val TITULO = "TITULO"
const val DESCRICAO = "DESCRICAO"


class NotasAdapter(context: Context, private val intID:EnviarInformacao
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>()  {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()


    interface EnviarInformacao {
        fun sendID(id: Int?)
    }


    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloItemView: TextView = itemView.findViewById(R.id.tituloTV)
        val descricaoItemView: TextView = itemView.findViewById(R.id.descricaoTV)
        val delete : ImageButton = itemView.findViewById(R.id.apagarBtn)
        val edit : ImageButton = itemView.findViewById(R.id.editarBtn)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.linha_lista_notas, parent, false)
        return NotaViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.tituloItemView.text = current.titulo
        holder.descricaoItemView.text = current.descricao
        val id = current.id

        //chamar os botoes

        holder.edit.setOnClickListener{
            val context=holder.tituloItemView.context
            val titulo= holder.tituloItemView.text.toString()
            val descricao= holder.descricaoItemView.text.toString()

        }

        holder.delete.setOnClickListener{
            val titulo = holder.tituloItemView.text.toString()
            // ENVIA O ID
            intID.sendID(id)
        }

    }

    internal fun setNotas (notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }


    override fun getItemCount():Int {

        return notas.size
    }


}



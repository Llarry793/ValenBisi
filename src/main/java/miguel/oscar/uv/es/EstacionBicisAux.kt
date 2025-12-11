package miguel.oscar.uv.es

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstacionBicisAux(private var stations: List<EstacionBicis>, private val listener: (EstacionBicis) -> Unit) :
    RecyclerView.Adapter<EstacionBicisAux.BikeStationViewHolder>() {

    class BikeStationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val direccionEstacion: TextView = itemView.findViewById(R.id.direccionEstacion)
        val bicisDisponibles: TextView = itemView.findViewById(R.id.bicisDisponibles)
        val postesLibres: TextView = itemView.findViewById(R.id.postesLibres)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeStationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estacion, parent, false)
        return BikeStationViewHolder(view)
    }

    override fun onBindViewHolder(holder: BikeStationViewHolder, position: Int) {
        val station = stations[position]
        holder.direccionEstacion.text = station.direccionEstacion
        holder.bicisDisponibles.text = "Available: ${station.bicisDisponibles}"
        holder.postesLibres.text = "Free slots: ${station.postesLibres}"

        // Cambia el color según la cantidad de bicicletas disponibles
        val color = when {
            station.bicisDisponibles > 10 -> android.graphics.Color.GREEN
            station.bicisDisponibles in 5..10 -> android.graphics.Color.YELLOW // Amarillo
            else -> android.graphics.Color.RED
        }
        holder.bicisDisponibles.setTextColor(color)
        holder.itemView.setOnClickListener {
            Log.d("RecyclerView", "Clic en ${station.direccionEstacion}")
            listener(station)
        }
    }

    override fun getItemCount() = stations.size
}

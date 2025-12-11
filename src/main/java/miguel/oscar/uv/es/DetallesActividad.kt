package miguel.oscar.uv.es

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetallesActividad : AppCompatActivity() {
    private lateinit var estacionBicis: EstacionBicis  // Agregar esta variable

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        estacionBicis = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("station", EstacionBicis::class.java)
        } else {
            @Suppress("DEPRECATION") // Suprime la advertencia en versiones antiguas
            intent.getParcelableExtra("station")
        } ?: return

        // Cambiar el título de la actividad con el nombre de la estación
        title = estacionBicis.direccionEstacion

        findViewById<TextView>(R.id.txtAddress).text = "Dirección: ${estacionBicis.direccionEstacion}"
        findViewById<TextView>(R.id.txtAvailable).text = "Bicicletas disponibles: ${estacionBicis.bicisDisponibles}"
        findViewById<TextView>(R.id.txtFreeSlots).text = "Espacios libres: ${estacionBicis.postesLibres}"
        findViewById<TextView>(R.id.txtTotalSlots).text = "Capacidad total: ${estacionBicis.totalPostes}"
        findViewById<TextView>(R.id.txtUpdated).text = "Última actualización: ${estacionBicis.ultimaActualizacion}"

        // Botón para abrir en Google Maps
        findViewById<FloatingActionButton>(R.id.btnOpenMaps).setOnClickListener {
            val uri = Uri.parse("geo:${estacionBicis.latitud},${estacionBicis.longitud}?q=${Uri.encode(estacionBicis.direccionEstacion)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps") // Asegura que se abra en Google Maps
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)  // Asegúrate de haber creado `menu_detail.xml`
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_open_maps) {
            val uri = Uri.parse("geo:${estacionBicis.latitud},${estacionBicis.longitud}?q=${Uri.encode(estacionBicis.direccionEstacion)}")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
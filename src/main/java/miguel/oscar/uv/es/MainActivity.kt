package miguel.oscar.uv.es

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var estacionBicis: ArrayList<EstacionBicis>
    private lateinit var adapter: EstacionBicisAux  // Definir fuera de onCreate



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ✅ Configurar el Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        estacionBicis = getData()
        rv = findViewById(R.id.rv)
        adapter = EstacionBicisAux(estacionBicis) { station ->
            val intent = Intent(this, DetallesActividad::class.java)
            intent.putExtra("station", station)
            startActivity(intent)
        }

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        val btnOrdenarPorDireccion: Button = findViewById(R.id.ordenarPorDireccion)
        val btnOrdenarPorBicis: Button = findViewById(R.id.ordenarPorBicis)

        btnOrdenarPorDireccion.setOnClickListener {
            estacionBicis.sortBy { it.direccionEstacion }
            rv.adapter?.notifyDataSetChanged()
        }

        btnOrdenarPorBicis.setOnClickListener {
            estacionBicis.sortByDescending { it.bicisDisponibles }
            rv.adapter?.notifyDataSetChanged()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun readJsonFromRaw(resources: Resources, rawResourceId: Int): String {
        val inputStream: InputStream = resources.openRawResource(rawResourceId)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charsets.UTF_8)
    }

    private fun getData(): ArrayList<EstacionBicis> {
        val bikeStationsList = ArrayList<EstacionBicis>()
        val rawResourceId = R.raw.valenbisi_disponibilitat
        val jsonFileContent = readJsonFromRaw(resources, rawResourceId)

        val jsonArray = JSONArray(jsonFileContent)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            val number = jsonObject.getInt("number")
            val address = jsonObject.getString("address")
            val isOpen = jsonObject.getString("open") == "T"
            val availableBikes = jsonObject.getInt("available")
            val freeSlots = jsonObject.getInt("free")
            val totalSlots = jsonObject.getInt("total")
            val hasTicketMachine = jsonObject.getString("ticket") == "T"
            val lastUpdated = jsonObject.getString("updated_at")

            val geoPoint = jsonObject.getJSONObject("geo_point_2d")
            val latitude = geoPoint.getDouble("lat")
            val longitude = geoPoint.getDouble("lon")

            val bikeStation = EstacionBicis(
                address, number, isOpen, availableBikes, freeSlots, totalSlots,
                hasTicketMachine, lastUpdated, latitude, longitude
            )

            bikeStationsList.add(bikeStation)
        }
        return bikeStationsList
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_name -> {
                estacionBicis.sortBy { it.direccionEstacion }
                rv.adapter?.notifyDataSetChanged()
                return true
            }
            R.id.action_sort_bikes -> {
                estacionBicis.sortByDescending { it.bicisDisponibles }
                rv.adapter?.notifyDataSetChanged()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
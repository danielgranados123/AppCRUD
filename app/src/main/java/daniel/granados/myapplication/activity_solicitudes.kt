package daniel.granados.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassTickets
import recyclerViewHelper.Adaptador


class activity_solicitudes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitudes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)

        //Asignar layout al RecyclerView

        rcvTickets.layoutManager = LinearLayoutManager(this)

        //Función para obtener datos
        fun obtenerDatos(): List<DataClassTickets>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultset = statement?.executeQuery("select * from tbTickets")!!

            val tickets = mutableListOf<DataClassTickets>()
            while (resultset.next()){
                val numTicket = resultset.getString("numTicket")
                val titulo = resultset.getString("titulo")
                val descripcion = resultset.getString("descripcion")
                val autor = resultset.getString("autor")
                val emailAutor = resultset.getString("emailAutor")
                val fechaCreacion = resultset.getString("fechaCreacion")
                val estado = resultset.getString("estado")
                val fechaFinalizacion = resultset.getString("fechaFinalizacion")

                val infoTickets = DataClassTickets(numTicket, titulo, descripcion, autor, emailAutor, fechaCreacion, estado,fechaFinalizacion)
                tickets.add(infoTickets)
            }

            return tickets

        }

        CoroutineScope(Dispatchers.IO).launch {
            val newTickets = obtenerDatos()
            withContext(Dispatchers.IO){
                (rcvTickets.adapter as? Adaptador)?.actualizarListaTickets(newTickets)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val ticketsDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(ticketsDB)
                rcvTickets.adapter = adapter
            }
        }

    }
}
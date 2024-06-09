package daniel.granados.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import java.util.UUID
import java.util.Date
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVerSolicitudes = findViewById<Button>(R.id.btnVerSolicitudes)
        btnVerSolicitudes.setOnClickListener {
            val intent = Intent(this, activity_solicitudes::class.java)
            startActivity(intent)
        }

        val txtNombreAutor = findViewById<EditText>(R.id.txtNombreAutor)
        val txtEmailAutor = findViewById<EditText>(R.id.txtEmailAutor)
        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtFechaCreacion = findViewById<EditText>(R.id.txtFechaCreacion)
        val txtEstado = findViewById<EditText>(R.id.txtEstado)
        val txtFechaFinalizacion = findViewById<EditText>(R.id.txtFechaFinalizacion)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //Guardar datos
                //1-Crear objeto de la clase conexión

                val claseConexion = ClaseConexion().cadenaConexion()

                //2- Crear variable que contenga un PreparedStatement
                val addTicket = claseConexion?.prepareStatement("insert into tbTickets(numTicket, titulo, descripcion, autor, emailAutor, fechaCreacion, estado, fechaFinalizacion) values(?, ?, ?, ?, ?, ?, ?, ?)")!!

                addTicket.setString(1, UUID.randomUUID().toString())
                addTicket.setString(2, txtNombreAutor.text.toString())
                addTicket.setString(3, txtEmailAutor.text.toString())
                addTicket.setString( 4, txtTitulo.text.toString())
                addTicket.setString( 5, txtDescripcion.text.toString())
                addTicket.setString( 6, txtFechaCreacion.text.toString())
                addTicket.setString( 7, txtEstado.text.toString())
                addTicket.setString( 8, txtFechaFinalizacion.text.toString())
                addTicket.executeUpdate()

                runOnUiThread {
                    txtNombreAutor.text.clear()
                    txtEmailAutor.text.clear()
                    txtTitulo.text.clear()
                    txtDescripcion.text.clear()
                    txtFechaCreacion.text.clear()
                    txtEstado.text.clear()
                    txtFechaFinalizacion.text.clear()

                    Toast.makeText(this@MainActivity, "Ticket guardado con éxito.", Toast.LENGTH_LONG).show()
                }
                }
        }

        }
    }

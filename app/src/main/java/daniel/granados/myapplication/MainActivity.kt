package daniel.granados.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
                val addProducto = claseConexion?.prepareStatement("insert into tbTickets(numTicket, titulo, descripcion, autor, emailAutor, fechaCreacion, estado, fechaFinalizacion) values(?, ?, ?, ?, ?, ?, ?, ?)")!!

                addProducto.setString(1, UUID.randomUUID().toString())
                addProducto.setString(2, txtNombreAutor.text.toString())
                addProducto.setString(3, txtEmailAutor.text.toString())
                addProducto.setString( 4, txtTitulo.text.toString())
                addProducto.setString( 5, txtDescripcion.text.toString())
                addProducto.setString( 6, txtFechaCreacion.text.toString())
                addProducto.setString( 7, txtDescripcion.text.toString())
                addProducto.setString( 8, txtEstado.text.toString())
                addProducto.executeUpdate()
                }


                //val nuevosProductos = obtenerDatos()
                //creo una corrutina que actualice el listado

                //withContext(Dispatchers.Main){
                 //   (rcvTickets.adapter as? Adaptador)?.actualizarLista(nuevosProductos)

                }

        val btnVerSolicitudes = findViewById<Button>(R.id.btnVerSolicitudes)
        btnVerSolicitudes.setOnClickListener {

            val intent = Intent(this, activity_solicitudes::class.java)
            startActivity(intent)
            }
        }

    }

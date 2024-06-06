package daniel.granados.myapplication

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
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.util.UUID

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
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //Guardar datos
                //1-Crear objeto de la clase conexión

                val claseConexion = ClaseConexion().cadenaConexion()

                //2- Crear variable que contenga un PreparedStatement
                val addProducto = claseConexion?.prepareStatement("insert into tbTickets(uuid, nombreAutor, emailAutor, titulo, descripcion) values(?, ?, ?, ?)")!!

                addProducto.setString(1, UUID.randomUUID().toString())
                addProducto.setString(2, txtNombreAutor.text.toString())
                addProducto.setString(3, txtEmailAutor.text.toString())
                addProducto.setString( 4, txtTitulo.text.toString())
                addProducto.setString( 5, txtDescripcion.text.toString())
                addProducto.executeUpdate()

                //val nuevosProductos = obtenerDatos()
                //creo una corrutina que actualice el listado

                //withContext(Dispatchers.Main){
                 //   (rcvTickets.adapter as? Adaptador)?.actualizarLista(nuevosProductos)

                }

            }
        }

    }

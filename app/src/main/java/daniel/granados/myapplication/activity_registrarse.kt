package daniel.granados.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import java.util.UUID

class activity_registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgRegresar = findViewById<ImageView>(R.id.imgRegresarBienvenida)
        imgRegresar.setOnClickListener {
            val intent = Intent(this, activity_bienvenido::class.java)
            startActivity(intent)
        }

        val txtIngresar = findViewById<TextView>(R.id.txtIngresarRegistrarme)
        txtIngresar.setOnClickListener {
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
        }

        //Codigo para crear nuevo usuario
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrarme)
        btnRegistrar.setOnClickListener {

            val txtUsuario = findViewById<EditText>(R.id.txtUsuarioRegistrarse).text.toString()
            val txtContrasena = findViewById<EditText>(R.id.txtContrasenaRegistrarse).text.toString()
            val txtConfirmarContrasena = findViewById<EditText>(R.id.txtConfirmarContrasenaRegistrarse).text.toString()

            if (txtContrasena == txtConfirmarContrasena) {
            // Establecer la conexión con la base de datos
            val claseConexion = ClaseConexion().cadenaConexion()


            if (claseConexion != null) {
                try {
            //Crear variable que contenga un PreparedStatement
            val addUsuario = claseConexion?.prepareStatement("insert into tbUsuarios(uuidUsuario, idTipo, nombre, contrasena) values(?, ?, ?, ?)")!!

            addUsuario.setString(1, UUID.randomUUID().toString())
            addUsuario.setString(2, txtUsuario.text.toString())
            addUsuario.setString(3, txtContrasena.text.toString())
            addUsuario.setInt(4, 1)
            addUsuario.executeUpdate()

                    // Mostrar un mensaje de éxito
                    Toast.makeText(this, "¡Te has registrado con éxito!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    // Mostrar el error
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "No se pudo completar la acción.", Toast.LENGTH_LONG).show()
            }
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Las contraseñas no coinciden")
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        }
        }
    }

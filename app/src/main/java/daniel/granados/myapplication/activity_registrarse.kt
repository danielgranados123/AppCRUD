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
import kotlinx.coroutines.withContext
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
            val txtUsuario = findViewById<EditText>(R.id.txtUsuarioRegistrarse)
            val txtContrasena = findViewById<EditText>(R.id.txtContrasenaRegistrarse)
            val txtConfirmarContrasena = findViewById<EditText>(R.id.txtConfirmarContrasenaRegistrarse)
            val btnRegistrar = findViewById<Button>(R.id.btnRegistrarme)


        btnRegistrar.setOnClickListener {
            //Confirma que el usuario no se equivoque de contraseña, ya que compara ambos datos ingresados
            if (txtContrasena.text.toString() != txtConfirmarContrasena.text.toString()) {
                txtUsuario.setText("")
                txtContrasena.setText("")
                txtConfirmarContrasena.setText("")
                Toast.makeText(
                    this@activity_registrarse,
                    "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Si esta bien, deja crear el usuario
                GlobalScope.launch(Dispatchers.IO) {

                    val objConexion = ClaseConexion().cadenaConexion()


                    val crearUsuario =
                        objConexion?.prepareStatement("INSERT INTO tbUsuarios (UUID_usuario, correoElectronico, clave) VALUES (?, ?, ?)")!!
                    crearUsuario.setString(1, UUID.randomUUID().toString())
                    crearUsuario.setString(2, txtUsuario.text.toString())
                    crearUsuario.setString(3, txtContrasena.text.toString())
                    crearUsuario.executeUpdate()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@activity_registrarse,
                            "Usuario creado con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        txtUsuario.setText("")
                        txtContrasena.setText("")
                        txtConfirmarContrasena.setText("")
                    }
                }
            }
        }
    }
}


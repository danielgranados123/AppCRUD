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

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgRegresar = findViewById<ImageView>(R.id.imgRegresar)
        imgRegresar.setOnClickListener {
            val intent = Intent(this, activity_bienvenido::class.java)
            startActivity(intent)
        }

        val txtRegistrateLogin = findViewById<TextView>(R.id.txtRegistrateLogin)
        txtRegistrateLogin.setOnClickListener {
            val intent = Intent(this, activity_registrarse::class.java)
            startActivity(intent)
        }

        val txtCorreoLogin = findViewById<EditText>(R.id.txtNombreUsuarioLogin)
        val  txtPasswordLogin = findViewById<EditText>(R.id.txtContrasenaLogin)

        val btnIngresar = findViewById<Button>(R.id.btnIngresarLogin)
            btnIngresar.setOnClickListener{
            //preparo el intent para cambiar a la pantalla de bienvenida
            val pantallaPrincipal = Intent(this, MainActivity::class.java)
            //Dentro de una corrutina hago un select en la base de datos
            GlobalScope.launch(Dispatchers.IO) {

                //1-Creo un objeto de la clase conexion

                val objConexion = ClaseConexion().cadenaConexion()
                //2- Creo una variable que contenga un PrepareStatement

                val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuarios WHERE correoElectronico = ? AND clave = ?")!!
                comprobarUsuario.setString(1, txtCorreoLogin.text.toString())
                comprobarUsuario.setString(2, txtPasswordLogin.text.toString())
                val resultado = comprobarUsuario.executeQuery()

                //Si encuentra un resultado
                if (resultado.next()) {
                    startActivity(pantallaPrincipal)
                } else {

                    //Si el usuario no existe, hago que se limpien los campos y que aparezca la alerta de que las credenciales son inválidas.
                    withContext(Dispatchers.Main) {
                        txtCorreoLogin.text.clear()
                        txtPasswordLogin.text.clear()
                        Toast.makeText(this@activity_login, "El usuario o la contraseña son incorrectos.", Toast.LENGTH_LONG).show()
                }
                }
            }
        }

    }
}
package daniel.granados.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_detalle_tickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_tickets)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1-Mando a llamar a todos los elementos de la pantalla

        val txtNombreDetalle = findViewById<TextView>(R.id.txtAutorDetalle)
        val txtEmailDetalle = findViewById<TextView>(R.id.txtEmailDetalle)
        val txtTituloDetalle = findViewById<TextView>(R.id.txtTituloDetalle)
        val txtDescripcionDetalle = findViewById<TextView>(R.id.txtDescripcionDetalle)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)

        //2- Recibir los valores

        val nombreDetalle = intent.getStringExtra("nombreAutor")
        val emailDetalle = intent.getStringExtra("emailAutor")
        val tituloDetalle = intent.getStringExtra("titulo")
        val descripcionDetalle = intent.getStringExtra("descripcion")

        //3- Poner los valores recibidos en el textView

        txtNombreDetalle.text = nombreDetalle
        txtEmailDetalle.text = emailDetalle
        txtTituloDetalle.text = tituloDetalle
        txtDescripcionDetalle.text = descripcionDetalle

        imgAtras.setOnClickListener{
            val pantallaAtras = Intent(this, MainActivity::class.java)
            startActivity(pantallaAtras)
        }
    }
}
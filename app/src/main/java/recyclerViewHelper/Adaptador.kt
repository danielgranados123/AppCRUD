package recyclerViewHelper

import android.app.AlertDialog
import android.content.Intent
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import daniel.granados.myapplication.R
import daniel.granados.myapplication.activity_detalle_tickets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassTickets

class Adaptador(private var Datos: List<DataClassTickets>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(vista)
    }

    fun actualizarTickets(nuevaLista:List<DataClassTickets>){
        Datos=nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarTickets(numTicket: String, position: Int) {
        val listaDatos = Datos .toMutableList()
        listaDatos.removeAt(position)
        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()
            val borrarTicket = objConexion?.prepareStatement("delete from tbTickets where numTicket = ?")!!
            borrarTicket.setString(1, numTicket)
            borrarTicket.executeUpdate()
            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = Datos.size

    fun ActualizarListaDespuesDeActualizarDatos (uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.uuid == uuid }
        Datos[index].titulo = nuevoNombre
        notifyItemChanged(index)
    }

    fun actualizarTickets(titulo: String, numTicket: String){
        //1-Creo una corrutina
        GlobalScope.launch(Dispatchers.IO){
            //1- Crear objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Variable que contenga un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update tbTickets set nombreProducto = ? where numTicket = ?")!!
            updateProducto.setString(1, numTicket)
            updateProducto.setString(2, titulo)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                ActualizarListaDespuesDeActualizarDatos(numTicket, titulo)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.txtProblema.text = ticket.titulo
        holder.txtAutor.text = ticket.nombreAutor
        /*holder.textView.text = ticket.nombreAutor
        holder.textView.text = ticket.correoAutor
        holder.textView.text = ticket.fechaCreacion
        holder.textView.text = ticket.estado
        holder.textView.text = ticket.fechaFinalizacion*/

        val item = Datos[position]
        holder.imgBorrar.setOnClickListener {


            holder.imgBorrar.setOnClickListener {
                //Creamos una alerta
                //1-Invocamos el contexto

                val context = holder.itemView.context

                //Creo la alerta
                val builder = AlertDialog.Builder(context)

                //Le ponemos un titulo a la alerta

                builder.setTitle("¡Espera!")

                //Ponemos el mensaje
                builder.setMessage("¿Estás seguro de que deseas eliminar el ticket?")

                //Paso final, agregamos los botones
                builder.setPositiveButton("Si"){
                        dialog, wich ->
                    eliminarTickets(item.uuid, position)
                }

                builder.setNegativeButton("No"){
                        dialog, wich ->
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }

            holder.imgEditar.setOnClickListener {

                val context = holder.itemView.context

                // Crear el AlertDialog
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Editar la solicitud:")

                // Crear un LinearLayout para contener los campos de texto
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                layout.setPadding(16, 20, 16, 20)

                // Agregar campos de texto al LinearLayout
                val cuadritoNuevoAutor = EditText(context)
                cuadritoNuevoAutor.setHint(item.nombreAutor)
                layout.addView(cuadritoNuevoAutor)

                val cuadritoNuevoEmail = EditText(context)
                cuadritoNuevoEmail.setHint(item.correoAutor)
                layout.addView(cuadritoNuevoEmail)

                val cuadritoNuevoTitulo = EditText(context)
                cuadritoNuevoTitulo.setHint(item.titulo)
                layout.addView(cuadritoNuevoTitulo)

                val cuadritoNuevoDescripcion = EditText(context)
                cuadritoNuevoDescripcion.setHint(item.descripcion)
                layout.addView(cuadritoNuevoDescripcion)

                val cuadritoNuevoFechaCreacion = EditText(context)
                cuadritoNuevoFechaCreacion.setHint(item.fechaCreacion)
                layout.addView(cuadritoNuevoFechaCreacion)

                val cuadritoNuevoFechaFinalizacion = EditText(context)
                cuadritoNuevoFechaFinalizacion.setHint(item.fechaFinalizacion)
                layout.addView(cuadritoNuevoFechaFinalizacion)

                val cuadritoNuevoEstado = EditText(context)
                cuadritoNuevoEstado.setHint(item.estado)
                layout.addView(cuadritoNuevoEstado)

                builder.setView(layout)

                //Paso final, agregamos los botones
                builder.setPositiveButton("Actualizar") { dialog, _ ->
                    val nuevoAutor = cuadritoNuevoAutor.text.toString()
                    val nuevoEmail = cuadritoNuevoEmail.text.toString()
                    val nuevoTitulo = cuadritoNuevoTitulo.text.toString()
                    val nuevaDescripcion = cuadritoNuevoDescripcion.text.toString()
                    val nuevaFechaCreacion = cuadritoNuevoFechaCreacion.text.toString()
                    val nuevaFechaFinalizacion = cuadritoNuevoFechaFinalizacion.text.toString()
                    val nuevoEstado = cuadritoNuevoEstado.text.toString()

                    // Actualizar cada campo por separado
                    actualizarTickets(nuevoAutor, item.uuid)
                    actualizarTickets(nuevoEmail, item.uuid)
                    actualizarTickets(nuevoTitulo, item.uuid)
                    actualizarTickets(nuevaDescripcion, item.uuid)
                    actualizarTickets(nuevaFechaCreacion, item.uuid)
                    actualizarTickets(nuevaFechaFinalizacion, item.uuid)
                    actualizarTickets(nuevoEstado, item.uuid)
                }

                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        }

        //Darle clic a la card
        holder.itemView.setOnClickListener {
            //Invoco el contexto
            val context = holder.itemView.context

            //Cambiamos la pantalla
            //Abrimos la pantalla de productos

            val pantallaDetalles = Intent(context, activity_detalle_tickets::class.java)

            //Aqui, antes de abrir la nueva pantalla le mando los parametros
            pantallaDetalles.putExtra("nombreAutor", item.nombreAutor)
            pantallaDetalles.putExtra("emailAutor", item.correoAutor)
            pantallaDetalles.putExtra("titulo", item.titulo)
            pantallaDetalles.putExtra("descripcion", item.descripcion)
            pantallaDetalles.putExtra("fechaCreacion", item.fechaCreacion)
            pantallaDetalles.putExtra("fechaFinalizacion", item.fechaFinalizacion)
            pantallaDetalles.putExtra("Estado", item.estado)


            context.startActivity(pantallaDetalles)


        }
    }
}
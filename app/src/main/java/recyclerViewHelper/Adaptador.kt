package recyclerViewHelper

import android.app.AlertDialog
import android.content.Intent
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
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

    /*fun actualizarTickets(nuevaLista:List<DataClassTickets>){
        Datos=nuevaLista
        notifyDataSetChanged()
    }*/

    fun eliminarTickets(numTicket: String, posicion: Int) {
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)


        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().cadenaConexion()

            val eliminarTicket = objConexion?.prepareStatement("delete from tbTickets where numTicket = ?")!!
            eliminarTicket.setString(1, numTicket)
            eliminarTicket.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    override fun getItemCount() = Datos.size

    fun ActualizarListaDespuesDeActualizarDatos (uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.uuid == uuid }
        Datos[index].titulo = nuevoNombre
        notifyItemChanged(index)
    }

    fun actualizarTickets(numTicket: String, uuid: String){
        //1-Creo una corrutina
        GlobalScope.launch(Dispatchers.IO){
            //1- Crear objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Variable que contenga un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update tbProductos set nombreProducto = ? where uuid = ?")!!
            updateProducto.setString(1, numTicket)
            updateProducto.setString(2, uuid)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                ActualizarListaDespuesDeActualizarDatos(uuid, numTicket)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.textView.text = ticket.titulo

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
                    eliminarTickets(item.titulo, position)
                }

                builder.setNegativeButton("No"){
                        dialog, wich ->
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }

            holder.imgEditar.setOnClickListener {

                val context = holder.itemView.context

                //Crear alerta

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Editar título de la solicitud:")

                //Agregamos cuadro de texto para que el usuario escriba el nuevo nombre
                val cuadritoNuevoAutor = EditText(context)
                cuadritoNuevoAutor.setHint(item.nombreAutor)
                builder.setView(cuadritoNuevoAutor)

                val cuadritoNuevoEmail = EditText(context)
                cuadritoNuevoEmail.setHint(item.correoAutor)
                builder.setView(cuadritoNuevoEmail)

                val cuadritoNuevoTitulo = EditText(context)
                cuadritoNuevoTitulo.setHint(item.titulo)
                builder.setView(cuadritoNuevoTitulo)

                val cuadritoNuevoDescripcion = EditText(context)
                cuadritoNuevoDescripcion.setHint(item.descripcion)
                builder.setView(cuadritoNuevoDescripcion)

                val cuadritoNuevoFechaCreacion = EditText(context)
                cuadritoNuevoFechaCreacion.setHint(item.fechaCreacion)
                builder.setView(cuadritoNuevoFechaCreacion)

                val cuadritoNuevoFechaFinalizacion = EditText(context)
                cuadritoNuevoFechaFinalizacion.setHint(item.fechaFinalizacion)
                builder.setView(cuadritoNuevoFechaFinalizacion)

                val cuadritoNuevoEstado = EditText(context)
                cuadritoNuevoEstado.setHint(item.estado)
                builder.setView(cuadritoNuevoEstado)

                //Paso final, agregamos los botones
                builder.setPositiveButton("Actualizar"){
                        dialog, wich ->
                    actualizarTickets(cuadritoNuevoAutor.text.toString(), item.uuid)
                    actualizarTickets(cuadritoNuevoEmail.text.toString(), item.uuid)
                    actualizarTickets(cuadritoNuevoTitulo.text.toString(), item.uuid)
                    actualizarTickets(cuadritoNuevoDescripcion.text.toString(), item.uuid)
                    actualizarTickets(cuadritoNuevoFechaCreacion.text.toString(), item.uuid)
                    actualizarTickets(cuadritoNuevoFechaFinalizacion.text.toString(), item.uuid)
                    actualizarTickets(cuadritoNuevoEstado.text.toString(), item.uuid)
                }

                builder.setNegativeButton("Cancelar"){
                        dialog, wich ->
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
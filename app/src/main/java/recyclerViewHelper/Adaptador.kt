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

    fun actualizarListaTickets(nuevaLista:List<DataClassTickets>){
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
        Datos[index].estado = nuevoNombre
        notifyItemChanged(index)
    }

    fun actualizarTickets(estado: String, uuid: String){
        //1-Creo una corrutina
        GlobalScope.launch(Dispatchers.IO){
            //1- Crear objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Variable que contenga un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update tbTickets set estado = ? where numTicket = ?")!!
            updateProducto.setString(1, estado)
            updateProducto.setString(2, uuid)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                ActualizarListaDespuesDeActualizarDatos(uuid, estado)
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

                //Crear alerta

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Editar estado del ticket:")

                //Agregamos cuadro de texto para que el usuario escriba el nuevo nombre
                val cuadritoNuevoNombre = EditText(context)
                cuadritoNuevoNombre.setHint(item.estado)
                builder.setView(cuadritoNuevoNombre)

                //Paso final, agregamos los botones
                builder.setPositiveButton("Actualizar"){
                        dialog, wich ->
                    actualizarTickets(cuadritoNuevoNombre.text.toString(), item.uuid)
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
            pantallaDetalles.putExtra("numTicket", item.uuid)
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
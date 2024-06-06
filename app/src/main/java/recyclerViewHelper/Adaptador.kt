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

    override fun getItemCount() = Datos.size

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
                builder.setMessage("¿Estás seguro de que deseas elimar tu solicitud?")

                //Paso final, agregamos los botones
                builder.setPositiveButton("Si"){
                        dialog, wich ->
                    eliminarRegistro(item.titulo, position)
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
                val cuadritoNuevoTitulo = EditText(context)
                cuadritoNuevoTitulo.setHint(item.titulo)
                builder.setView(cuadritoNuevoTitulo)

                //Paso final, agregamos los botones
                builder.setPositiveButton("Actualizar"){
                        dialog, wich ->
                    actualizarProducto(cuadritoNuevoTitulo.text.toString(), item.uuid)
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


            context.startActivity(pantallaDetalles)


        }
    }

    fun actualizarLista(nuevaLista: List<DataClassTickets>){

        Datos = nuevaLista
        notifyDataSetChanged()

    }

    fun eliminarRegistro(titulo : String, posicion: Int){

        //Quitar el elemento de la lista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        //Quitar de la base de datos
        GlobalScope.launch(Dispatchers.IO){

            //1- Crear objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            val delProducto = objConexion?.prepareStatement("delete tbTickets where titulo = ?")!!
            delProducto.setString(1, titulo)
            delProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()

    }

    fun ActualizarListaDespuesDeActualizarDatos (uuid: String, nuevoTitulo: String){
        val index = Datos.indexOfFirst { it.uuid == uuid }
        Datos[index].titulo = nuevoTitulo
        notifyItemChanged(index)
    }

    fun actualizarProducto(titulo: String, uuid: String){
        //1-Creo una corrutina
        GlobalScope.launch(Dispatchers.IO){
            //1- Crear objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Variable que contenga un prepareStatement
            val updateTicket = objConexion?.prepareStatement("update tbProductos set nombreProducto = ? where uuid = ?")!!
            updateTicket.setString(1, titulo)
            updateTicket.setString(2, uuid)
            updateTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                ActualizarListaDespuesDeActualizarDatos(uuid, titulo)
            }
        }
    }
}
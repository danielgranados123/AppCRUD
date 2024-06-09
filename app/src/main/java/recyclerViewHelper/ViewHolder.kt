package recyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import daniel.granados.myapplication.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtProblema = view.findViewById<TextView>(R.id.txtProblemaCard)
    val txtAutor = view.findViewById<TextView>(R.id.txtAutorCard)

    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)
}
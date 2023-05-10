package com.example.shoppingapp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductListAdapter( private val fragment:Fragment,private val context: Context, private val list: ArrayList<Product> = ArrayList<Product>(), activity: FragmentActivity?, viewmodel: ViewModel,
                         listtype:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val viewmodel=viewmodel
    val activity=activity
    val listtype:String=listtype
    private lateinit var amountUpdate: AmountUpdateListener
    private lateinit var listener: Listener
    val  VIEW_TYPE_A:Int=1
    private val sharedPreferences=context.getSharedPreferences("likedcategory",MODE_PRIVATE)
    private val editor=sharedPreferences.edit()
    class ViewHolder1(Itemview: View):RecyclerView.ViewHolder(Itemview) {
        var title:TextView=Itemview.findViewById(R.id.title)
        val description:TextView=itemView.findViewById(R.id.descriptionOfProduct)
        val cartitem:TextView=itemView.findViewById(R.id.cartcount)
        val image:ImageView=itemView.findViewById(R.id.imageofProduct)
        val like:ImageView=itemView.findViewById(R.id.like1)
        val SingleproductLayout:LinearLayout=itemView.findViewById(R.id.SingleLayoutProdut)
    }
    class ViewHolder2(Itemview: View):RecyclerView.ViewHolder(Itemview) {
        var title:TextView=itemView.findViewById(R.id.title)
        val description:TextView=itemView.findViewById(R.id.descriptionOfProduct)
        val cartitem:TextView=itemView.findViewById(R.id.cartcount)
        val image:ImageView=itemView.findViewById(R.id.imageofProduct)
        val remove:Button=itemView.findViewById(R.id.remove)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType== VIEW_TYPE_A){
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.single_product_view, parent, false)
                return ViewHolder1(view)
            }
            else{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.single_checkout_product_view, parent, false)
                return ViewHolder2(view)
            }
    }

    override fun getItemViewType(position: Int): Int {
        if (listtype=="product list"){
            return  1
        }
        else{
            return 0
        }

    }
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        listener=activity as Listener
        if(holder.itemViewType==1){
                val viewHolder=holder as ViewHolder1
                viewHolder.title.text=list[position].name
                viewHolder.description.text=list[position].category.toString()
                viewHolder.cartitem.text=list[position].cart.toString()
                Picasso.get().load(list[position].imageUrl).into(holder.image)
                viewHolder.SingleproductLayout.setOnClickListener {
                    listener.SetProductDetails(list[position].name)
                }
                if(list[position].likeStatus==1){
                    viewHolder.like.setImageResource(R.drawable.baseline_thumb_up_alt_24)
                }
                else{
                    viewHolder.like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                }
                viewHolder.like.setOnClickListener {
                    if(list[position].likeStatus==0){
                        viewHolder.like.setImageResource(R.drawable.baseline_thumb_up_alt_24)
                        viewmodel.setLikeStatus(position,1)
                        editor.putString("liked",list[position].category).apply()
                    }
                    else{
                        viewHolder.like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                        viewmodel.setLikeStatus(position,0)
                    }
                }
            }
           else{
                val viewHolder=holder as ViewHolder2
                viewHolder.title.text=list[position].name
                viewHolder.description.text=list[position].category
                viewHolder.cartitem.text=list[position].cart.toString()
                Picasso.get().load(list[position].imageUrl).into(viewHolder.image)
                viewHolder.remove.setOnClickListener {
                    viewmodel.removeCartitems(list[position].name)
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    if (fragment is CheckoutList_fragment){
                        amountUpdate=fragment as AmountUpdateListener
                        amountUpdate.updateAmount()
                    }
                }
            }
    }
    override fun getItemCount(): Int {
        return if(list.isEmpty()) 0 else list.size
    }
}
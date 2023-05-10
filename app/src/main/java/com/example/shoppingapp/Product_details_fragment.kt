package com.example.shoppingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class Product_details_fragment : Fragment() {
    private lateinit var viewmodel:ViewModel
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var rate: TextView
    private lateinit var countInCart: TextView
    private lateinit var Availableitems: TextView
    private lateinit var image: ImageView
    private lateinit var noOfitem: EditText
    private lateinit var listener: Listener
    private lateinit var addToCart: Button
    private lateinit var Checkout: Button
    private lateinit var remove: Button
    private lateinit var dbhelper: MyDbhelper
    private  var post:Int=0
    private var noOfitems:String="0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_details_fragment, container, false)
        val productName= arguments?.getSerializable("productName") as String
        viewmodel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        dbhelper=MyDbhelper(requireActivity())
        var list=viewmodel.getProductLiveData()
        title=view.findViewById(R.id.title)
        description=view.findViewById(R.id.descriptionOfProduct)
        rate=view.findViewById(R.id.rate)
        countInCart=view.findViewById(R.id.cart)
        Availableitems=view.findViewById(R.id.rating)
        image=view.findViewById(R.id.imageView)
        noOfitem=view.findViewById(R.id.editTextNumber)
        addToCart=view.findViewById(R.id.addCart)
        Checkout=view.findViewById(R.id.checkout)
        remove=view.findViewById(R.id.remove_btn)
        listener=activity as Listener
        SetProductDetail(productName,list)
        addToCart.setOnClickListener {
            if (noOfitem.text.toString()!=""){
                noOfitems= noOfitem.text.toString()
            }
            viewmodel.addToCart(getPosition(productName,list),noOfitems.toInt())
            viewmodel.getProductLiveData().observe(requireActivity(), Observer {
                dbhelper.updateProduct(it.get(getPosition(productName,list)))
            })
            SetProductDetail(productName,list)
        }
        Checkout.setOnClickListener {
            listener.setcheckout()
        }
        remove.setOnClickListener {
            list.value?.get(getPosition(productName,list))?.name?.let { it1 ->
                viewmodel.removeCartitems(
                    it1
                )
            }
            SetProductDetail(productName,list)
        }

        return view
    }


    private fun getPosition(productName: String, list: MutableLiveData<MutableList<Product>>): Int {
        for (i in 0..((list.value?.size)?.minus(1)!!)){
            if (list.value?.get(i)?.name== productName){
                post=i
            }
        }
        return post
    }

    private fun SetProductDetail(position: String, list: MutableLiveData<MutableList<Product>>) {
        val positionOfProduct=getPosition(position, list)
        Picasso.get().load(list.value?.get(positionOfProduct)?.imageUrl).into(image)
        title.text= list.value?.get(positionOfProduct)?.name
        description.text= list.value?.get(positionOfProduct)?.Description
        rate.text= list.value?.get(positionOfProduct)?.price
        countInCart.text= list.value?.get(positionOfProduct)?.cart.toString()
        Availableitems.text=list.value?.get(positionOfProduct)?.rating.toString()
    }
}
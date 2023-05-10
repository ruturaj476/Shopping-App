package com.example.shoppingapp

import android .app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class ViewModel(application: Application): AndroidViewModel(application){
    //
    private val categories= mutableListOf<String>()
    //
    private val productlist=MutableLiveData<MutableList<Product>>()

    init {
        val requestQueue= Volley.newRequestQueue(getApplication<android.app.Application?>().applicationContext)
        val request= JsonArrayRequest(
            Request.Method.GET,"https://fakestoreapi.com/products",null,
            { respose ->
                val productList= mutableListOf<Product>()
                for (i in 0 until respose.length()){
                    val item=respose.getJSONObject(i)
                    //
                    val category=item.getString("category")
                    if(!categories.contains(category)){
                        categories.add(category)
                    }
                    //
                    val product=Product(item.getString("title"),item.getString("category"),item.getString("description"),item.getString("price"),item.getString("image"),item.getJSONObject("rating").getDouble("rate"),item.getJSONObject("rating").getInt("count"),0,0)
                    productList.add(product)
                }
                productlist.postValue(productList)
            },
            {error ->

            }
        )
       requestQueue.add(request)
   }
    //
    fun getCategories():List<String>{
        categories.add("All category")
        return categories
    }
    //
    fun getProductLiveData(): MutableLiveData<MutableList<Product>> {
        return productlist
    }
    fun setLikeStatus(position: Int, i: Int) {
        productlist.value?.get(position)?.likeStatus  =i
    }
    fun addToCart(position: Int, i: Int) {
        productlist.value?.get(position)?.cart  = productlist.value?.get(position)?.cart?.plus(i)!!
    }

    fun removeCartitems(position: String) {
        val position=getPosition(position)
        productlist.value?.get(position)?.cart=0
    }

    private fun getPosition(position: String): Int {
        var pos:Int=0
        for (i in 0..((productlist.value?.size)?.minus(1)!!)){
            if(productlist.value!!.get(i).name==position){
                pos=i
            }
        }
        return pos
    }

    fun getAmounttotal(): String {
        var Amount:String=""
        var doubl:Double=0.0
        for (i in 0..((productlist.value?.size)?.minus(1)!!)){
            doubl= (doubl+(productlist.value?.get(i)?.cart )?.times(productlist.value!!.get(i).price.toDouble())!!)

        }
        Amount=("%.2f".format(doubl)).toString()
        return Amount
    }

    fun removeProducts() {
        for (i in 0..((productlist.value?.size)?.minus(1)!!)){
            productlist.value!!.get(i).cart=0
        }
    }
}





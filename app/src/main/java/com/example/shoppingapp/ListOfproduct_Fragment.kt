package com.example.shoppingapp

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class ListOfproduct_Fragment : Fragment() {
    private lateinit var productList_recyclerview:RecyclerView
    private lateinit var viewmodel:ViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spin:Spinner
    private lateinit var Suggested:Button
    private lateinit var check:Button
    private lateinit var listener:Listener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_list_ofproduct, container, false)
        sharedPreferences=requireActivity().getSharedPreferences("likedcategory",MODE_PRIVATE)
        productList_recyclerview=view.findViewById(R.id.recycler_view_product_list)
        productList_recyclerview.layoutManager = LinearLayoutManager(context)
        productList_recyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        spin=view.findViewById(R.id.spinner)
        Suggested=view.findViewById(R.id.Suggested)
        check=view.findViewById(R.id.check)
        viewmodel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        val categories = viewmodel.getCategories()
        val adapter=ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter=adapter
        listener=activity as Listener
        setAaptor("All category")
        spin.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                setAaptor(categories[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        Suggested.setOnClickListener {
            val liked =sharedPreferences.getString("liked",null)
            viewmodel.getProductLiveData()
                .observe((this as LifecycleOwner)!!, object : Observer<List<Product>?> {
                    override fun onChanged(t: List<Product>?) {
                        val Likedlist = t?.filter { it.category == liked }
                        productList_recyclerview.adapter = context?.let { it1 ->
                            ProductListAdapter(this@ListOfproduct_Fragment,it1, Likedlist as ArrayList<Product>, activity, viewmodel,"product list")
                        }
                    }
                })
        }
        check.setOnClickListener {
            listener.setcheckout()
        }
        return view
    }
    fun setAaptor(categoryType: String) {
        viewmodel.getProductLiveData()
            .observe((this as LifecycleOwner)!!
            ) { t ->
                if (categoryType == "All category") {
                    productList_recyclerview.adapter = context?.let {
                        ProductListAdapter(this@ListOfproduct_Fragment, it, t as ArrayList<Product>, activity, viewmodel, "product list")
                    }
                } else {
                    val electronicslist = t?.filter { it.category == categoryType }
                    productList_recyclerview.adapter = context?.let {
                        ProductListAdapter(this@ListOfproduct_Fragment, it, electronicslist as ArrayList<Product>, activity, viewmodel, "product list")
                    }
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=requireActivity().getSharedPreferences("likedcategory",MODE_PRIVATE)
    }
}
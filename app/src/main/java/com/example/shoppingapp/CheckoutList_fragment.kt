package com.example.shoppingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CheckoutList_fragment : Fragment(),AmountUpdateListener{
    private lateinit var dbhelper:MyDbhelper
    private lateinit var checkoutList_recyclerview: RecyclerView
    private lateinit var viewmodel:ViewModel
    private lateinit var Amount_total:TextView
    private lateinit var buyButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_checkout_list_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkoutList_recyclerview=view.findViewById(R.id.Checkout_list_recyclerView)
        checkoutList_recyclerview.layoutManager = LinearLayoutManager(context)
        Amount_total=view.findViewById(R.id.totalamount)
        buyButton=view.findViewById(R.id.buy)
        dbhelper=MyDbhelper(requireActivity())
        checkoutList_recyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewmodel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        viewmodel.getProductLiveData()
            .observe((this as LifecycleOwner)!!, object : Observer<List<Product>?> {
                override fun onChanged(t: List<Product>?) {
                    val CheckoutList = t?.filter { it.cart>0 }
                    checkoutList_recyclerview.adapter = context?.let { it1 ->
                        ProductListAdapter(
                            this@CheckoutList_fragment,it1, CheckoutList as ArrayList<Product>, activity, viewmodel,"checkout list"
                        )
                    }
                }
            })
        Amount_total.text=viewmodel.getAmounttotal()
        buyButton.setOnClickListener {
            viewmodel.removeProducts()
            dbhelper.deleteCart()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun updateAmount(){
        Amount_total.text=viewmodel.getAmounttotal()
    }
}
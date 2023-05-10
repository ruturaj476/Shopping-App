package com.example.shoppingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

private const val CURRENT_FRAGMENT = "save_instance"
private const val INDEX = "index"
class MainActivity : AppCompatActivity() ,Listener{
    private lateinit var dbhelper:MyDbhelper
    private lateinit var viewModel: ViewModel
    private lateinit var currentPosition: String
    override fun SetProductDetails(position: String) {
        currentPosition = position
        val fragment=Product_details_fragment()
        val args=Bundle()
        args.putSerializable("productName",position)
        fragment.arguments=args
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.container,fragment).addToBackStack(null)
        transaction.commit()
    }

    override fun setcheckout() {
        val fragment=CheckoutList_fragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        transaction.replace(R.id.container,fragment).addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {

            when (savedInstanceState.getInt(CURRENT_FRAGMENT, 0)) {
                1 -> {
                    setcheckout()
                }
                2 -> {
                    SetProductDetails(currentPosition)
                }

            }
        }
        else {
            supportFragmentManager.beginTransaction().replace(R.id.container, ListOfproduct_Fragment())
                .commit()
        }
        dbhelper=MyDbhelper(this)
        dbhelper.deleteCart()
        dbhelper=MyDbhelper(this)
        viewModel=ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.getProductLiveData().observe(this, Observer {
            for (product:Product in it){
                dbhelper.addProduct(product)
            }
        })

    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        val manager = supportFragmentManager
        when(manager.findFragmentById(R.id.container)) {

            is CheckoutList_fragment -> {
                outState.putInt(CURRENT_FRAGMENT, 1)
            }

            is Product_details_fragment -> {
                outState.putSerializable(INDEX, currentPosition)
                outState.putInt(CURRENT_FRAGMENT, 2)
            }

        }
        super.onSaveInstanceState(outState, outPersistentState)
    }
}



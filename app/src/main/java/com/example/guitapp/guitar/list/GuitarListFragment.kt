package com.example.guitapp.guitar.list

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.guitapp.R
import com.example.guitapp.auth.data.AuthRepository
import kotlinx.android.synthetic.main.fragment_guitar_list.*

class GuitarListFragment : Fragment() {

    private lateinit var guitarModel: GuitarListViewModel
    private lateinit var guitarListAdapter: GuitarListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(javaClass.name, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(javaClass.name, "onDestroy")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guitar_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(javaClass.name, "onActivityCreated")
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        guitarModel = ViewModelProvider(this).get(GuitarListViewModel::class.java)
        AuthRepository.storedToken.observe(viewLifecycleOwner) {
            println(it)
            if (it?.token == null) {
                findNavController().navigate(R.id.fragment_login)
            } else {
                if (AuthRepository.user == null) {
                    AuthRepository.login(it)
                }
                setupGuitarList()
            }
        }
        fab.setOnClickListener {
            Log.v(javaClass.name, "navigate to add new guitar")
            findNavController().navigate(R.id.fragment_guitar_edit)
        }
        logoutFab.setOnClickListener {
            Log.v(javaClass.name, "LOGOUT")
            guitarModel.logout()
            findNavController().navigate(R.id.fragment_login)
        }
    }

    private fun setupGuitarList() {
        guitarListAdapter = GuitarListAdapter(this)
        guitar_list.adapter = guitarListAdapter
        guitarModel.items.observe(viewLifecycleOwner, { items ->
            Log.v(javaClass.name, "update items")
            guitarListAdapter.items = items
        })
        guitarModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(javaClass.name, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        guitarModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(javaClass.name, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        guitarModel.refresh()
    }
}
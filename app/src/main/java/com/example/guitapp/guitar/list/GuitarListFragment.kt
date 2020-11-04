package com.example.guitapp.guitar.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.guitapp.R
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
//        if (!AuthRepository.isLoggedIn) {
//            findNavController().navigate(R.id.fragment_login)
//            return;
//        }
        setupGuitarList()
        fab.setOnClickListener {
            Log.v(javaClass.name, "navigate to add new guitar")
            findNavController().navigate(R.id.fragment_guitar_edit)
        }
    }

    private fun setupGuitarList() {
        guitarListAdapter = GuitarListAdapter(this)
        guitar_list.adapter = guitarListAdapter
        guitarModel = ViewModelProvider(this).get(GuitarListViewModel::class.java)
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
package com.example.guitapp.guitar.edit

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
import com.example.guitapp.guitar.data.Guitar
import kotlinx.android.synthetic.main.fragment_guitar_edit.*
import java.time.LocalDateTime
import java.time.ZoneOffset

class GuitarEditFragment : Fragment() {

    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var viewModel: GuitarEditViewModel
    private var itemId: String? = null
    private var guitar: Guitar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(javaClass.name, "onCreate")
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(javaClass.name, "onCreateView")
        return inflater.inflate(R.layout.fragment_guitar_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(javaClass.name, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(javaClass.name, "save guitar")
            val i = guitar
            if (i != null) {
                i.model = model_input.text.toString()
                i.producedOn = date_input.text.toString()
                i.available = available_checkbox.isChecked
                i.price = price_input.text.toString().toDouble()
                viewModel.saveOrUpdate(i)
            }
        }
        delete_button.setOnClickListener {
            Log.v(javaClass.name, "delete guitar")
            val i = guitar
            if (i != null) {
                viewModel.delete(i)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(GuitarEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(javaClass.name, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(javaClass.name, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, { completed ->
            if (completed) {
                Log.v(javaClass.name, "completed, navigate back")
                findNavController().popBackStack()
            }
        })
        val id = itemId
        if (id == null) {
            guitar = Guitar("", "", 0.0, LocalDateTime.now().toInstant(ZoneOffset.UTC).toString(), false)
        } else {
            viewModel.getItemById(id).observe(viewLifecycleOwner, {
                Log.v(javaClass.name, "update guitar")
                if(it != null) {
                    guitar = it
                    model_input.setText(it.model)
                    price_input.setText(it.price.toString())
                    date_input.setText(it.producedOn)
                    available_checkbox.isChecked = it.available
                }
            })
        }
    }
}
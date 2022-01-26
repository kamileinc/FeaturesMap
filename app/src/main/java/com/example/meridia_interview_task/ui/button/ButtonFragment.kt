package com.example.meridia_interview_task.ui.button

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.meridia_interview_task.R
import com.example.meridia_interview_task.viewmodel.ButtonFragmentViewModel
import kotlinx.android.synthetic.main.fragment_button.*

class ButtonFragment : Fragment() {

    private lateinit var viewModel: ButtonFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        googleMapsButton.setOnClickListener {
            val action = ButtonFragmentDirections.actionButtonFragmentToMapsFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ButtonFragmentViewModel::class.java)
    }
}

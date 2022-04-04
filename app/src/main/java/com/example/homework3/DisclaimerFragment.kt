package com.example.homework3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation


class DisclaimerFragment : Fragment() {


    // using onCreateView to set up transition between fragments
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and store in view
        val view =  inflater.inflate(R.layout.fragment_disclaimer, container, false)


        //val disclaimerText = view.findViewById<TextView>(R.id.disclaimer)
        val nextButton = view.findViewById<Button>(R.id.agree)
        nextButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_disclaimerFragment_to_menuFragment)}

            return view

    }


}
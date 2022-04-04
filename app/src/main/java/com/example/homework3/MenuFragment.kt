package com.example.homework3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_menu, container, false)

        val tipsButton = view.findViewById<Button>(R.id.tipsButton)
        tipsButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_tipsFragment)}

        val returnToDisclaimer = view.findViewById<Button>(R.id.returnToDisclaimer)
        returnToDisclaimer.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_disclaimerFragment)}
        return view
    }

}
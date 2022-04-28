package com.dotexe.homework3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation


class CustomizeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_customize, container, false)

        // navigate to main screen
        val goToMain = view.findViewById<ImageButton>(R.id.goToMain)
        goToMain.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_customizeFragment_to_menuFragment)}

        return view
    }

}
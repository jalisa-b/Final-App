package com.example.homework3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation


class TipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tips, container, false)

        val returnToMain = view.findViewById<Button>(R.id.returnToMain)
        returnToMain.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_tipsFragment_to_menuFragment)}
        return view
    }


}
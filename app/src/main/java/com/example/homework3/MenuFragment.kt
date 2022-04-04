package com.example.homework3

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.Navigation


class MenuFragment : Fragment() {

    lateinit var tipsButton: Button
    lateinit var returnToDisclaimer: ImageButton
    lateinit var goToCustom: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_menu, container, false)

        // navigating to tips page
        tipsButton = view.findViewById<Button>(R.id.tipsButton)
        tipsButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_tipsFragment)}

        // navigating to disclaimer page
        returnToDisclaimer = view.findViewById<ImageButton>(R.id.returnToDisclaimer)
        returnToDisclaimer.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_disclaimerFragment)}

        // navigating to customize page
        goToCustom = view.findViewById<ImageButton>(R.id.goToCustom)
        goToCustom.setOnClickListener {
            scaler()
            Navigation.findNavController(view)
                .navigate(R.id.action_menuFragment_to_customizeFragment)
        }

        return view
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }
            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(goToCustom,scaleX,scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(goToCustom)
        animator.start()
    }

}
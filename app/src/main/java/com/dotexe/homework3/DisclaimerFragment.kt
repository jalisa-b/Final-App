package com.dotexe.homework3

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ms.square.android.expandabletextview.ExpandableTextView


class DisclaimerFragment : Fragment() {

    lateinit var nextButton: Button
    lateinit var expandableText: ExpandableTextView


    // using onCreateView to set up transition between fragments
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and store in view
        val view = inflater.inflate(R.layout.fragment_disclaimer, container, false)

        // getting reference of ExpandableTextView
        // getting reference of ExpandableTextView
        //expandableText= view.findViewById<ExpandableTextView>(R.id.answer1).findViewById(R.id.answer1) as ExpandableTextView

// calling setText on the ExpandableTextView so that
// text content will be displayed to the user

// calling setText on the ExpandableTextView so that
// text content will be displayed to the user
        //expTv.setText(getString(R.string.expandable_text))


        //val disclaimerText = view.findViewById<TextView>(R.id.disclaimer)
        nextButton = view.findViewById<Button>(R.id.agree)
        nextButton.setOnClickListener{

            scaler()
            //Navigation.findNavController(view).navigate(R.id.action_disclaimerFragment_to_menuFragment)
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
        val animator = ObjectAnimator.ofPropertyValuesHolder(nextButton,scaleX,scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(nextButton)
        animator.start()
    }


}
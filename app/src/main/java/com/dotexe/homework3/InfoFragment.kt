package com.dotexe.homework3
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class InfoFragment : Fragment() {
    lateinit var backButton: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and store in view
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
            scale(backButton)
            Navigation.findNavController(view).navigate(R.id.action_infoFragment_to_menuFragment)
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



    private fun scale(button: ImageButton) {
        // ObjectAnimator lets you do different kinds of animation
        // you could replace rotateButton with something else to animate it
        // doing a full circle rotation
        // you want 0 as the ending value
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(button,scaleX,scaleY)
        animator.duration = 100;
        animator.disableViewDuringAnimation(button)
        animator.start()
    }



}
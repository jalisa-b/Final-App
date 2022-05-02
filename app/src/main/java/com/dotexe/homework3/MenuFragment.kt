package com.dotexe.homework3

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation


class MenuFragment : Fragment() {

    lateinit var tipsButton: ImageButton
    lateinit var returnToDisclaimer: ImageButton
    lateinit var goToCustom: ImageButton
    lateinit var goToWeather: ImageButton

    var listPests = listOf("ants","bees","beetles", "cockroaches", "crickets", "flies", "mosquitos", "snakes", "spiders", "wasps")
    var  listClicks = mutableListOf<Int>()

    lateinit var ant: ImageButton
    lateinit var bee: ImageButton
    lateinit var beetle: ImageButton
    lateinit var cockroach: ImageButton
    lateinit var cricket: ImageButton
    lateinit var fly: ImageButton
    lateinit var mosquito: ImageButton
    lateinit var snake: ImageButton
    lateinit var spider: ImageButton
    lateinit var wasp: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_menu, container, false)

        ant = view.findViewById(R.id.ant)
        bee = view.findViewById(R.id.bee)
        beetle = view.findViewById(R.id.beetle)
        cockroach = view.findViewById(R.id.cockroach)
        cricket = view.findViewById(R.id.cricket)
        fly = view.findViewById(R.id.fly)
        mosquito = view.findViewById(R.id.mosquito)
        snake = view.findViewById(R.id.snake)
        spider = view.findViewById(R.id.spider)
        wasp = view.findViewById(R.id.wasp)

        var  listPestButtons = listOf(ant, bee, beetle, cockroach, cricket, fly, mosquito, snake, spider, wasp)
        for (pest in listPestButtons){
            listClicks.add(0) }

        for (i in listPestButtons.indices){
            var pest = listPestButtons[i]
            var pestString = listPests[i]
            var numClicks = listClicks[i]
            pest.setOnClickListener {
                val toast: Toast
                if (numClicks %2 == 0 ) {
                    it.setBackgroundResource(R.color.green_select)
                    toast = Toast.makeText(activity,"Playing sound to repel $pestString!",Toast.LENGTH_SHORT)
                } else {
                    it.setBackgroundResource(R.color.green_light)
                    toast = Toast.makeText(activity,"Turned off $pestString sound.",Toast.LENGTH_SHORT)
                }
                toast.show()


                numClicks += 1
                listClicks.set(i,numClicks)
            }

        }


        // navigating to tips page
        tipsButton = view.findViewById<ImageButton>(R.id.tipsButton)
        tipsButton.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_tipsFragment)}

        // navigating to disclaimer page
        returnToDisclaimer = view.findViewById<ImageButton>(R.id.goToInfo)
        returnToDisclaimer.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_infoFragment)}

        // navigating to customize page
        goToCustom = view.findViewById<ImageButton>(R.id.goToCustom)
        goToCustom.setOnClickListener {
            scaler()
            Navigation.findNavController(view)
                .navigate(R.id.action_menuFragment_to_customizeFragment)
        }

        goToWeather = view.findViewById<ImageButton>(R.id.goToWeather)
        goToWeather.setOnClickListener {
            scaler()
            Navigation.findNavController(view)
                .navigate(R.id.action_menuFragment_to_weatherFragment)
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
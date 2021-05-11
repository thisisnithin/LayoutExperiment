package com.example.layoutexperiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.layoutexperiment.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val first = binding.first
        val second = binding.second
        val third = binding.third
        val fourth = binding.fourth
        setContentView(binding.root)

        val firstLayoutParams = first.layoutParams as ConstraintLayout.LayoutParams
        val secondLayoutParams = second.layoutParams as ConstraintLayout.LayoutParams
        val thirdLayoutParams = third.layoutParams as ConstraintLayout.LayoutParams

        fun update(size: Int) {
            when (size) {
                1 -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        second.visibility = View.GONE
                        third.visibility = View.GONE
                        fourth.visibility = View.GONE
                        firstLayoutParams.matchConstraintPercentWidth = 1f
                        firstLayoutParams.matchConstraintPercentHeight = 1f
                        first.requestLayout()
                    }
                }
                2 -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        second.visibility = View.VISIBLE
                        third.visibility = View.GONE
                        fourth.visibility = View.GONE
                        firstLayoutParams.matchConstraintPercentWidth = 1f
                        firstLayoutParams.matchConstraintPercentHeight = 0.5f
                        secondLayoutParams.matchConstraintPercentWidth = 1f
                        secondLayoutParams.matchConstraintPercentHeight = 0.5f
                        secondLayoutParams.verticalBias = 1f
                        first.requestLayout()
                        second.requestLayout()
                    }
                }
                3 -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        second.visibility = View.VISIBLE
                        third.visibility = View.VISIBLE
                        fourth.visibility = View.GONE
                        firstLayoutParams.matchConstraintPercentWidth = 0.5f
                        firstLayoutParams.matchConstraintPercentHeight = 0.5f
                        secondLayoutParams.matchConstraintPercentWidth = 0.5f
                        secondLayoutParams.matchConstraintPercentHeight = 0.5f
                        thirdLayoutParams.matchConstraintPercentWidth = 1f
                        secondLayoutParams.verticalBias = 0f
                        first.requestLayout()
                        second.requestLayout()
                        third.requestLayout()
                    }
                }
                4 -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        second.visibility = View.VISIBLE
                        third.visibility = View.VISIBLE
                        fourth.visibility = View.GONE
                        firstLayoutParams.matchConstraintPercentWidth = 0.5f
                        firstLayoutParams.matchConstraintPercentHeight = 0.5f
                        secondLayoutParams.matchConstraintPercentWidth = 0.5f
                        secondLayoutParams.matchConstraintPercentHeight = 0.5f
                        thirdLayoutParams.matchConstraintPercentWidth = 0.5f
                        thirdLayoutParams.matchConstraintPercentHeight = 0.5f
                        first.requestLayout()
                        second.requestLayout()
                        third.requestLayout()
                    }
                }
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            // Joining
            for (i in 1 until 5) {
                delay(2000)
                //put remote stream to respective texture view
                update(i)
            }

            //leaving
            for (i in 3 downTo 1) {
                delay(2000)
                update(i)
            }
        }
    }
}
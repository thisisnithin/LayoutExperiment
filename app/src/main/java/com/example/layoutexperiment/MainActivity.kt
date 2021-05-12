package com.example.layoutexperiment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import com.example.layoutexperiment.databinding.ActivityMain1Binding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain1Binding.inflate(layoutInflater)

        // mapOf<uniqueID, isInCall>
        val viewList = mutableMapOf<Int, Boolean>()

        val flexBoxLayout = binding.flexboxLayout

        setContentView(binding.root)

        var mainWidth = flexBoxLayout.width
        var mainHeight = flexBoxLayout.height

        //Need to get the size of flexBox
        val viewTreeObserver: ViewTreeObserver = flexBoxLayout.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    flexBoxLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    mainWidth = flexBoxLayout.width
                    mainHeight = flexBoxLayout.height
                    Log.d("PARENT_HEIGHT_IN", mainHeight.toString())
                    Log.d("PARENT_WIDTH_IN", mainWidth.toString())
                }
            })
        }


        fun update(size: Int) {
            when (size) {
                1 -> {
                    for (i in viewList) {
                        if (i.value) {
                            val view = flexBoxLayout.getChildAt(i.key)
                            view.layoutParams.width = mainWidth
                            view.layoutParams.height = mainHeight
                            view.requestLayout()
                        }
                    }
                }
                2 -> {
                    for (i in viewList) {
                        if (i.value) {
                            val view = flexBoxLayout.getChildAt(i.key)
                            view.layoutParams.width = mainWidth
                            view.layoutParams.height = mainHeight / 2
                            view.requestLayout()
                        }
                    }
                }
                3 -> {
                    var lastElement = 0
                    for (i in viewList) {
                        if (i.value) {
                            lastElement = i.key
                            val view = flexBoxLayout.getChildAt(i.key)
                            view.layoutParams.width = mainWidth / 2
                            view.layoutParams.height = mainHeight / 2
                            view.requestLayout()
                        }
                    }
                    val view = flexBoxLayout.getChildAt(lastElement)
                    view.layoutParams.width = mainWidth
                }
                4 -> {
                    for (i in viewList) {
                        if (i.value) {
                            val view = flexBoxLayout.getChildAt(i.key)
                            view.layoutParams.width = mainWidth / 2
                            view.layoutParams.height = mainHeight / 2
                            view.requestLayout()
                        }
                    }
                }
            }
            //visibility toggles
            for (j in viewList) {
                val view = flexBoxLayout.getChildAt(j.key)
                if (j.value) view.visibility = View.VISIBLE
                else view.visibility = View.GONE
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            val job = coroutineScope {
                launch(Dispatchers.Main) {
                    //all four joining
                    //initially u can keep isInCall to false and when they join just set isInCall to true
                    //im putting it in for just to simulate
                    for (i in 1 until 5) {
                        viewList[i - 1] = true
                        /* launching this update in coroutine because the size of flexBox is calculated
                        a bit late and first view size will become (0,0) */
                        val job = coroutineScope {
                            launch (Dispatchers.Main){
                                //gets the count of people whose isInCall is true and updates
                                update(viewList.filterValues { it }.size)
                            }
                        }
                        job.join()
                        delay(1000)
                    }

                    //blue and white leaving
                    viewList[2] = false
                    viewList[0] = false
                    update(viewList.filterValues { it }.size)

                    delay(1000)

                    //white rejoining
                    viewList[2] = true
                    update(viewList.filterValues { it }.size)

                    delay(1000)

                    //blue rejoining
                    viewList[0] = true
                    update(viewList.filterValues { it }.size)
                }
            }
            job.join()
        }
    }
}
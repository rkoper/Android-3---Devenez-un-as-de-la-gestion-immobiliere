package com.sofianem.realestatemanager.controller.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HorizontalCarouselRecyclerView(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private var isScrolledBySystem = false

    fun <T : ViewHolder> initialize(newAdapter: Adapter<T>) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, true)
        adapter = newAdapter
    }



    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        post {
            if (state == SCROLL_STATE_IDLE) {
                var count = adapter?.itemCount ?: childCount

                val scrollPerChild = computeHorizontalScrollRange() / count

                var currentScroll = computeHorizontalScrollOffset() / scrollPerChild

                val scrollOffset=computeHorizontalScrollOffset() % scrollPerChild

                if(scrollOffset>=scrollPerChild){
                    currentScroll++
                }
                if (!isScrolledBySystem) {
                    smoothScrollToPosition(currentScroll)
                    isScrolledBySystem = !isScrolledBySystem
                }
            }else if(state== SCROLL_STATE_DRAGGING){
                isScrolledBySystem=false
            }
        }


    }

}
package com.sofianem.realestatemanager.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.adapter.MainAdapter
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.ImageV
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel


class MainFragment : Fragment(), LifecycleObserver {
    private val mMyViewModel by viewModel<MyViewModel>()
    private val mMyViewModelForImages by viewModel<MyViewModelForImages>()
    var mSearchlist: ArrayList<Int>? = arrayListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mSearchlist = arguments?.getIntegerArrayList("1111")
        println("--------------------mListId--------------" + mSearchlist )
        return inflater.inflate(R.layout.fragment_main, container, false) }

    private fun setupRecyclerView() {
        if (mSearchlist.isNullOrEmpty()) {
            mMyViewModel.mAllEstate.observeForever { dataEstate ->
                mMyViewModelForImages.allImageLive.observe(
                    viewLifecycleOwner,
                    Observer { dataImage ->
                        subscriber_recyclerView.adapter =
                            MainAdapter(dataEstate, dataImage, requireContext())
                    })
            }
        } else {
            val a =  arrayListOf<EstateR>()
            val b =  arrayListOf<ImageV>()
            mSearchlist!!.forEach { mId ->
                mMyViewModel.getById(mId).observe(viewLifecycleOwner, Observer {estate ->
                    mMyViewModelForImages.getById(mId).observe(viewLifecycleOwner, Observer { img ->
                       if (img == null) {}
                       else { b.add(img)}
                        a.add(estate)
                        subscriber_recyclerView.adapter = MainAdapter(a, b, requireContext())
                        }) }) } } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscriber_recyclerView.layoutManager = LinearLayoutManager(activity)
        setupRecyclerView()
    }

    companion object {
        fun newInstance(mListId: ArrayList<Int>?): MainFragment {
            val fragment = MainFragment()
            val bundle = Bundle().apply { putIntegerArrayList("1111", mListId) }
            fragment.arguments = bundle
            return fragment
        }
    }
}





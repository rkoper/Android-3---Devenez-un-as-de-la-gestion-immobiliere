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


class MainFragment : Fragment(), LifecycleObserver {
    private var mListId: ArrayList<Int>? = arrayListOf()
    private var mListData: List<EstateR>? = arrayListOf()
    private lateinit var mMyViewModel: MyViewModel
    private lateinit var mMyViewModelForImages: MyViewModelForImages

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListId = arguments?.getIntegerArrayList("1111")
        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        mMyViewModelForImages = ViewModelProviders.of(this).get(MyViewModelForImages::class.java)
        mMyViewModel.allWords.observe(this, Observer {
            mListData = it
//            println(" MAIN FRAGMENT -----1-----" + it[0].adress)
  //          println(" MAIN FRAGMENT ----2------" +it[1].adress)
            subscriber_recyclerView.adapter?.notifyDataSetChanged()
        })

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private fun setupRecyclerView(mListId: java.util.ArrayList<Int>?) {
        val t1 = arrayListOf<ImageV>()
        if (mListId != null) {
            val t = mMyViewModel.saveIdData(mListId)
            subscriber_recyclerView.adapter = MainAdapter(t, t1, requireContext())
        } else {
            mMyViewModel.allWords.observeForever { dataEstate ->
                mMyViewModelForImages.retrieveImageData().observe(this, Observer { dataImage ->
                    subscriber_recyclerView.adapter =
                        MainAdapter(dataEstate, dataImage, requireContext())
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscriber_recyclerView.layoutManager = LinearLayoutManager(activity)
        setupRecyclerView(mListId)
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

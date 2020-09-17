package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import com.sofianem.realestatemanager.viewmodel.MyViewModelForPlaces
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MyViewModel(mRepository = get()) }
    viewModel { MyViewModelForImages(mRepositoryImage = get()) }
    viewModel { MyViewModelForPlaces(mRepositoryPlace = get()) }
}
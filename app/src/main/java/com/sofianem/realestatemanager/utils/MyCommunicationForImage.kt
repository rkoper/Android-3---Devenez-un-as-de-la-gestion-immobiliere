package com.sofianem.realestatemanager.utils

interface MyCommunicationForImage {
    fun deleteImage(imageId: Int, mDesc: String, mPath: String)

    fun uploadImage(imageId: Int)

}
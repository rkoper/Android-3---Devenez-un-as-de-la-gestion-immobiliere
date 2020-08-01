package com.sofianem.realestatemanager.data.Dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.sofianem.realestatemanager.data.Model.EstateR


@Dao
interface EstateDao {

    @Query("SELECT * FROM Estate")
    fun getAll(): List<EstateR>

    @Query("SELECT * FROM Estate")
    fun getAllv2(): List<EstateR>

    @Insert
    fun insert(db: EstateR) : Long

    @Update
    fun update(db: EstateR)

    @Query("UPDATE Estate SET Estate_pharmacy = :pharmacy WHERE Estate_id = :id")
    fun updateProxPharma(pharmacy: String, id: Int)

    @Query("UPDATE Estate SET Estate_park = :park WHERE Estate_id = :id")
    fun updateProxPark(park: String, id: Int)

    @Query("UPDATE Estate SET Estate_school = :school WHERE Estate_id = :id")
    fun updateProxSchool(school: String, id: Int)

    @Query("UPDATE Estate SET Estate_market = :market WHERE Estate_id = :id")
    fun updateProxMarket(market: String, id: Int)

    @Query("UPDATE Estate SET Estate_nb_photo = :nb_photo WHERE Estate_id = :id")
    fun updateNbPhoto(nb_photo: Int, id: Int)

    @Query("UPDATE Estate SET Estate_location = :location WHERE Estate_id = :id")
    fun updateForLoc(location: String, id: Int)

    @Query("UPDATE Estate SET Estate_market = :Estate_market WHERE Estate_id = :id")
    fun updateForMarket(Estate_market: String, id: Int)
    @Query("UPDATE Estate SET Estate_park = :Estate_park WHERE Estate_id = :id")
    fun updateForPark(Estate_park: String, id: Int)
    @Query("UPDATE Estate SET Estate_school = :Estate_school WHERE Estate_id = :id")
    fun updateForSchool(Estate_school: String, id: Int)
    @Query("UPDATE Estate SET Estate_pharmacy = :Estate_pharmacy WHERE Estate_id = :id")
    fun updateForPharmacy(Estate_pharmacy: String, id: Int)

    @Query("SELECT * FROM Estate where Estate_surface like :surface")
    suspend fun getSurface(surface: String): List<EstateR>

    @Query("SELECT * FROM Estate where Estate_id like :id")
    suspend fun getById(id: Int): EstateR

    @Query("SELECT * FROM Estate ORDER BY Estate_id DESC")
    fun getAllTodoList(): LiveData<List<EstateR>>
    //
    @Query("SELECT Estate_id FROM Estate  WHERE Estate_personn  like :personn AND Estate_type  like :type AND Estate_surface BETWEEN :surfaceMini AND :surfaceMax AND Estate_price BETWEEN :priceMini AND :priceMax AND Estate_number_of_room BETWEEN :roomMini AND :roomMax AND Estate_date_begin BETWEEN :dateCreateBegin AND :dateCreateEnd AND Estate_nb_photo BETWEEN :nb_photo_mini AND :nb_photo_max AND Estate_date_end BETWEEN :dateSoldBegin AND :dateSoldBeginEnd AND Estate_status like :status  AND Estate_pharmacy like :pharmacy  AND Estate_school like :school  AND Estate_market like :market  AND Estate_park like :park ")
   suspend fun getSearchAll(personn:String?, type:String?, surfaceMini: Int?, surfaceMax: Int?, priceMini: Int?, priceMax: Int?, roomMini: Int?, roomMax: Int?, dateCreateBegin: Long?, dateCreateEnd: Long?, nb_photo_mini: Int?, nb_photo_max: Int?, dateSoldBegin: Long?, dateSoldBeginEnd: Long?,status: String?,pharmacy:String?,school:String?,market:String?,park:String? ): List<Int>

    @Query("SELECT * FROM Estate")
    fun getEstateWithCursor(): Cursor

    @Query("SELECT Estate_id FROM Estate")
    fun getEstateId(): List<Int>

    @Query("SELECT * FROM Estate WHERE Estate_id = :index")
    fun getItemsWithCursor(index:Int): Cursor

    @Query("SELECT Estate_location  FROM Estate WHERE Estate_id = :index")
    fun getEstateLocById(index:Int): String

    @Insert
    fun insertItem(estate: EstateR) : Long

    @Update
    fun updateItem(estate: EstateR) :Int
}


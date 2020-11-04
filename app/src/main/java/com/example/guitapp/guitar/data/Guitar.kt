package com.example.guitapp.guitar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guitars")
data class Guitar(
    @PrimaryKey @ColumnInfo(name = "_id") var _id: String,
    @ColumnInfo(name = "model") var model: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "produced_on") var producedOn: String,
    @ColumnInfo(name = "available") var available: Boolean
) {

    override fun toString(): String {
        return "Guitar(_id='$_id', model='$model', price=$price, producedOn='$producedOn', available=$available)"
    }

    fun toDisplay(): String {
        return "$model $price (${this.producedOn.split("T")[0]}) $available"
    }
}
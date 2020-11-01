package com.example.guitapp.guitar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDateTime

@Entity(tableName = "guitars")
//@TypeConverters(Converters::class)
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
//        2008-10-31T20:08:13+02:00
        return "$model $price (${this.producedOn.split("T")[0]}) $available"
    }
}

class Converters {
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(value) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date.toString()
    }
}
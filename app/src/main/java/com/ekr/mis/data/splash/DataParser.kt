package com.ekr.mis.data.splash

import android.util.Log
import com.google.gson.*
import java.lang.reflect.Type


class DataParser : JsonDeserializer<Penilaian> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Penilaian {
        val result = Penilaian()
        try {
            val map: HashMap<String, Penilaian>? = readServiceUrlMap(json?.asJsonObject)
            if (map != null) {
                result.dataValues = map
            }
        } catch (ex: JsonSyntaxException) {
            Log.e("Deserialize", "Message : " + ex.message)
        }

        return result

    }

    @Throws(JsonSyntaxException::class)
    private fun readServiceUrlMap(jsonObject: JsonObject?): HashMap<String, Penilaian>? {
        if (jsonObject == null) {
            return null
        }
        val products: HashMap<String, Penilaian> = HashMap()
        for ((key, value1) in jsonObject.entrySet()) {
            val value: Penilaian = Gson().fromJson(value1, Penilaian::class.java)
            products[key] = value
        }
        return products
    }
}
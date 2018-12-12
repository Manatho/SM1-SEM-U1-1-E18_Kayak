package kayaklers.sdu.dk.kayaklers.apollo

import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import java.util.*

class DateTypeAdapter : CustomTypeAdapter<Date> {
    override fun encode(value: Date): CustomTypeValue<*> {
        return CustomTypeValue.fromRawValue(value.time)
    }

    override fun decode(value: CustomTypeValue<*>): Date {
        return Date(value.value as Long)
    }

}
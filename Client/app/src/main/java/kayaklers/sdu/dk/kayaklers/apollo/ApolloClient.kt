package kayaklers.sdu.dk.kayaklers.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.response.CustomTypeAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ApolloClient {

    companion object {
        val BASE_URL: String = "https://kayaklers.localtunnel.me/graphql"
        fun setupApollo(): ApolloClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            val okHttp = OkHttpClient
                    .Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

            return ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(okHttp)
                    .addCustomTypeAdapter(type.CustomType.DATE, DateTypeAdapter())
                    .build()
        }
    }

}
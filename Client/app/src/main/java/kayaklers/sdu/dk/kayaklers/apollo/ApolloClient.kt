package kayaklers.sdu.dk.kayaklers.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.BuildConfig
import okhttp3.OkHttpClient

class ApolloClient {

    private val BASE_URL = "http://localhost:4000/graphql"

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .build()

        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttp)
                .build()
    }


}
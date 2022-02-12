package com.kitmak.network.remote

import com.kitmak.network.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import okhttp3.logging.HttpLoggingInterceptor

@Module(includes = [ApiModule::class])
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })
        }

        defaultRequest {
            header("Content-Type", "application/json;charset=UTF-8")
        }
        engine {
            config {
                followRedirects(true)
            }
            addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            )
            //addNetworkInterceptor(interceptor)
            //preconfigured = okHttpClientInstance
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    abstract fun bindApi(apisImpl: TruckApisImpl): TruckApis
}
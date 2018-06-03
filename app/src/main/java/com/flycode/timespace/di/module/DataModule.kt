package com.flycode.timespace.di.module

import android.content.Context
import com.flycode.timespace.data.models.User
import com.raizlabs.android.dbflow.config.DatabaseDefinition
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
open class DataModule{

    @Provides
    @Singleton
    fun provideDatabaseDefinition(): DatabaseDefinition {
        return FlowManager.getDatabase(com.flycode.timespace.data.db.Database::class.java)
    }

    @Named("default_user")
    @Provides
    @Singleton
    fun provideDefaultUser(): User {
        val user = SQLite.select()
                .from(User::class.java)
                .querySingle()

        return user ?: User()
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context): Cache {
        return Cache(context.cacheDir, (100 * 1024 * 1024).toLong())//100 MB cache
    }

    /*@Provides
    @Singleton
    fun provideApolloGraphqlCache(context: Context): ApolloHttpCache {

        //Size in bytes of the cache
        val size = 1024 * 1024

        //Create the http response cache store
        return ApolloHttpCache(DiskLruHttpCacheStore(context.cacheDir, size.toLong()))
    }*/

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                //.addInterceptor(interceptor)
                //.addInterceptor(httpLoggingInterceptor)
                //.addNetworkInterceptor(stethoInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRxJava2CallAdapterFactory(): RxJavaCallAdapterFactory {
        return RxJavaCallAdapterFactory.create()
    }

   /* @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient,
                            apolloHttpCache: ApolloHttpCache) : ApolloClient =
        ApolloClient.builder()
                .serverUrl(Config.GRAPHQL_ENDPOINT)
                .httpCache(apolloHttpCache)
                .okHttpClient(okHttpClient)
                .build()*/


}

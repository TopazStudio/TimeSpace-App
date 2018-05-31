package com.flycode.timespace.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.Config
import com.raizlabs.android.dbflow.config.DatabaseDefinition
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import javax.inject.Named

@Module
open class DataModule{

    @Provides
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
    fun provideConfig(context: Application): Config {
        return Config()
    }

    @Provides
    @Singleton
    fun provideCache(context: Application): Cache {
        return Cache(context.cacheDir, (100 * 1024 * 1024).toLong())//100 MB cache
    }

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

}

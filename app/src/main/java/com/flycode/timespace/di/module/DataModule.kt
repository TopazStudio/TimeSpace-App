package com.flycode.timespace.di.module

import android.content.Context
import android.content.SharedPreferences
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCache
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.flycode.timespace.data.Config
import com.flycode.timespace.data.models.User
import com.flycode.timespace.data.models.User_Table
import com.flycode.timespace.data.network.AppInvitesService
import com.flycode.timespace.data.network.AuthService
import com.flycode.timespace.data.network.GroupService
import com.flycode.timespace.data.network.TempService
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.util.HttpAuthorizer
import com.raizlabs.android.dbflow.config.DatabaseDefinition
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
                .where(User_Table._tag.eq("default_user"))
                .querySingle()

        return user ?: User()
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context): Cache {
        return Cache(context.cacheDir, (100 * 1024 * 1024).toLong())//100 MB cache
    }

    @Provides
    @Singleton
    fun provideApolloCacheKeyResolver(context: Context): CacheKeyResolver {
        return object : CacheKeyResolver() {
            @NotNull
            override fun fromFieldRecordSet(@NotNull field: ResponseField, @NotNull recordSet: Map<String, Any>): CacheKey {
//                val typeName = recordSet["__typename"] as String
//                if ("User" == typeName) {
//                    val userKey = typeName + "." + recordSet["login"]
//                    return CacheKey.from(userKey)
//                }
                if (recordSet.containsKey("id")) {
                    val typeNameAndIDKey = recordSet["__typename"].toString() + "." + recordSet["id"]
                    return formatCacheKey(typeNameAndIDKey)
                }
                return CacheKey.NO_KEY
            }

            // Use this resolver to customize the key for fields with variables: eg entry(repoFullName: $repoFullName).
            // This is useful if you want to make query to be able to resolved, even if it has never been run before.
            @NotNull
            override fun fromFieldArguments(@NotNull field: ResponseField, @NotNull variables: Operation.Variables): CacheKey {
                return CacheKey.NO_KEY
            }

            fun formatCacheKey(id: String?): CacheKey {
                return if (id == null || id.isEmpty()) {
                    CacheKey.NO_KEY
                } else {
                    CacheKey.from(id)
                }
            }
        }
    }


    @Provides
    @Singleton
    fun provideApolloNormailzedCache(context: Context): NormalizedCacheFactory<LruNormalizedCache> {
        val apolloSqlHelper = ApolloSqlHelper(context, "TimeSpaceCache")
        return LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)
                .chain(SqlNormalizedCacheFactory(apolloSqlHelper))
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("Main", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
            cache: Cache,
            @Named("authentication_interceptor") authenticationInterceptor: Interceptor,
            @Named("network_interceptor") networkInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(networkInterceptor)
                //.addInterceptor(httpLoggingInterceptor)
                //.addNetworkInterceptor(stethoInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
    }

    @Named("authentication_interceptor")
    @Provides
    @Singleton
    fun authenticationInterceptor(
            sharedPreferences: SharedPreferences
    ): Interceptor = Interceptor { chain ->
        var request = chain.request()

        if (!sharedPreferences.getString("token",null).isNullOrEmpty()) {

            val builder = request
                    .newBuilder()
                    .header("Authorization", "Bearer " +  sharedPreferences.getString("token", null))

            request = builder.build()
        }

        val response = chain.proceed(request)

        if (response.header("Authorization",null) != null){
            sharedPreferences.edit().putString(
                    "token",
                    response.header("Authorization",null)
            )
        }
        response
    }

    @Named("network_interceptor")
    @Provides
    @Singleton
    fun provideIntercepter(
            sharedPreferences: SharedPreferences
    ): Interceptor = Interceptor { chain ->
        var request = chain.request()
        val builder = request
                .newBuilder()
                .header("X-Requested-With","XMLHttpRequest")

        //Add socket id for push notifications
        if (!sharedPreferences.getString("socket-id",null).isNullOrEmpty()) {
            builder.header("X-Socket-ID",sharedPreferences.getString("socket-id", null))
        }

        request = builder.build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideApolloClient(
            okHttpClient: OkHttpClient,
            normalizedCacheFactory: NormalizedCacheFactory<LruNormalizedCache>,
            cacheKeyResolver: CacheKeyResolver
    ) : ApolloClient =
        ApolloClient.builder()
                .serverUrl(Config.GRAPHQL_ENDPOINT)
                .normalizedCache(normalizedCacheFactory, cacheKeyResolver)
                .okHttpClient(okHttpClient)
                .build()

    @Provides
    @Singleton
    fun provideRetrofit(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Config.BACKEND_ENDPOINT)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideTempService(
            retrofit: Retrofit
    ): TempService = retrofit.create(TempService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(
            retrofit: Retrofit
    ): AuthService = retrofit.create(AuthService::class.java)


    @Provides
    @Singleton
    fun provideGroupService(
            retrofit: Retrofit
    ): GroupService = retrofit.create(GroupService::class.java)


    @Provides
    @Singleton
    fun provideAppInvitesService(
            retrofit: Retrofit
    ): AppInvitesService = retrofit.create(AppInvitesService::class.java)

    @Provides
    @Singleton
    fun providePusher(
            sharedPreferences: SharedPreferences
    ) : Pusher{
        val options  = PusherOptions()

        val authorizer =  HttpAuthorizer("${Config.BACKEND_ENDPOINT}broadcasting/auth").apply {
            //SET JWT
            this.setHeaders(HashMap<String,String>().apply {
                if (!sharedPreferences.getString("token",null).isNullOrEmpty()) {
                    this["Authorization"] = "Bearer " + sharedPreferences.getString("token",null)
                }
                this["Accept"] = "application/json"
                this["Content-Type"] = "application/x-www-form-urlencoded"
            })

        }

        options.apply {
//            this.isEncrypted = true
            this.setCluster(Config.PUSHER_APP_CLUSTER)
            this.authorizer = authorizer
        }

        return Pusher(Config.PUSHER_APP_KEY, options)
    }

}

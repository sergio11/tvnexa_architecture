package com.dreamsoftware.api.data.cache.di

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.cache.datasource.impl.RedisCacheDatasourceImpl
import com.dreamsoftware.model.RedisClusterConfig
import com.google.gson.Gson
import org.koin.dsl.module
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.JedisCluster

val cacheModule = module {
    single {
        JedisCluster(hashSetOf(*get<RedisClusterConfig>().nodes.map {
            HostAndPort(it.host, it.port) }.toTypedArray()) )
    }
    factory<ICacheDatasource<String>> { RedisCacheDatasourceImpl(get(), get()) }
    single { Gson() }
}
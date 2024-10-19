package com.sunl888.rocket.registry;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistryCache {
    private static final Map<String, Class<?>> cache = new ConcurrentHashMap<>();

    public static void register(String service, Class<?> implClass) {
        cache.put(service, implClass);
    }

    public static Class<?> get(String service) {
        return cache.get(service);
    }

    public static void unregister(String service) {
        cache.remove(service);
    }
}

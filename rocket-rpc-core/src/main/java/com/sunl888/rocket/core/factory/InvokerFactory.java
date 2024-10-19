package com.sunl888.rocket.core.factory;

import com.sunl888.rocket.core.invoker.Invoker;
import com.sunl888.rocket.spi.ExtensionLoader;

public class InvokerFactory {
    public static Invoker getInvoker(String key) {
        return ExtensionLoader.getExtensionLoader(Invoker.class).getExtension(key);
    }
}

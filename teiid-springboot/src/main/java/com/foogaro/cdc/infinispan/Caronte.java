package com.foogaro.cdc.infinispan;

import com.foogaro.cdc.infinispan.model.Protable;
import org.infinispan.Cache;

public class Caronte {

    public void ship(Protable entry, Cache cache) {
        System.out.println("Protable: " + entry);
        System.out.println("Cache: " + cache);
        cache.put(entry.getKey(), entry);
    }

}

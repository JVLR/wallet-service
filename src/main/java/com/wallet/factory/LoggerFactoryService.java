package com.wallet.factory;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class LoggerFactoryService {

    public <T> Logger getLogger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}

package com.auto1.testdatastorage.utils;

import com.auto1.testdatastorage.exception.EmptyQueueException;
import com.auto1.testdatastorage.exception.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ExceptionSupplier {

    public Supplier<NotFoundException> notFoundException(String entity, String key) {
        return () -> new NotFoundException(String.format("%s [%s] not found", entity, key));
    }

    public Supplier<EmptyQueueException> emptyQueueException(String omniType) {
        return () -> new EmptyQueueException(String.format("Queue with [%s] omni type is empty ", omniType));
    }

}

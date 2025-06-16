package com.wallet.exception;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
public class HandleException {

    @Singleton
    public static class DataExceptionHandler implements ExceptionHandler<DataException, HttpResponse<?>> {
        @Override
        public HttpResponse<?> handle(io.micronaut.http.HttpRequest request, DataException exception) {
            return HttpResponse.unprocessableEntity().body(new JsonError(exception.getMessage()));
        }
    }
}
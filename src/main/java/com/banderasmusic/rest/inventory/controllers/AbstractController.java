package com.banderasmusic.rest.inventory.controllers;

import com.banderasmusic.rest.inventory.exceptions.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractController implements ApplicationEventPublisherAware {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected ApplicationEventPublisher eventPublisher;
    protected static final String DEFAULT_PAGE_SIZE = "20";
    protected static final String DEFAULT_PAGE_NUMBER = "0";

    Counter http400ExceptionCounter = Metrics.counter("com.banderasmusic.rest.inventory.InventoryController.HTTP400");
    Counter http404ExceptionCounter = Metrics.counter("com.banderasmusic.rest.inventory.InventoryController.HTTP404");
    Counter http500ExceptionCounter = Metrics.counter("com.banderasmusic.rest.inventory.InventoryController.HTTP500");
    Counter noSuchElementExceptionCounter = Metrics.counter("com.banderasmusic.rest.inventory.InventoryController.NoSuchElement");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HTTP400Exception.class)
    public @ResponseBody
    RestAPIExceptionInfo handleBadRequestException(HTTP400Exception ex, WebRequest request,
                                                   HttpServletResponse response) {
        log.info("Received Data Store Exception: " + ex.getLocalizedMessage());
        http400ExceptionCounter.increment();
        return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The request did not have the correct parameters...");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HTTP404Exception.class)
    public @ResponseBody
    RestAPIExceptionInfo handleResourceNotFoundException(HTTP404Exception ex, WebRequest request,
                                                         HttpServletResponse response) {
        log.info("Received Resource Not Found Exception: " + ex.getLocalizedMessage());
        http404ExceptionCounter.increment();
        return new RestAPIExceptionInfo(ex.getLocalizedMessage(), "The requested resource was not found...");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HTTP500Exception.class)
    public @ResponseBody
    RestAPIExceptionInfo handleInternalServerException(HTTP500Exception ex, WebRequest request,
                                                       HttpServletResponse response) {
        log.info("Received Internal Server Exception: " + ex.getLocalizedMessage());
        http500ExceptionCounter.increment();
        HTTP500Exception e = new HTTP500Exception("The request failed on the server...");
        return new RestAPIExceptionInfo(e.getMessage(), "The request failed on the server...");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoSuchElementException.class)
    public @ResponseBody
    RestAPIExceptionInfo handleNoSuchElementException(NoSuchElementException ex, WebRequest request,
                                                       HttpServletResponse response) {
        log.info("Received No Such Element Exception: " + ex.getLocalizedMessage());
        noSuchElementExceptionCounter.increment();
        NoSuchElementException e = new NoSuchElementException("No such element was found...");
        return new RestAPIExceptionInfo(e.getMessage(), "The request failed on the server...");
    }
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public static <T> T checkResourceFound(final T resource) {
        if (resource == null) {
            throw new HTTP400Exception("Resource Not Found");
        }
        return resource;
    }
}

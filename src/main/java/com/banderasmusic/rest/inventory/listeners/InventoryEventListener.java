package com.banderasmusic.ecommerceproductapi.listeners;

import com.banderasmusic.ecommerceproductapi.events.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Async
    @EventListener
    public void onApplicationEvent(ProductEvent productEvent) { // with @Async this is an asynchronous method
        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName() + " id: " + Thread.currentThread().getId());
        log.info("Received Product Event : " + productEvent.getEventType());
        log.info("Received Product From Product Event :" + productEvent.getProduct().toString());
    }
}

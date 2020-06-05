package com.banderasmusic.ecommerceproductapi.events;

import com.banderasmusic.ecommerceproductapi.model.Product;
import org.springframework.context.ApplicationEvent;

public class ProductEvent extends ApplicationEvent {

    private String eventType;
    private Product product;

    public ProductEvent(String eventType, Product product) {
        super(product);
        this.eventType = eventType;
        this.product = product;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

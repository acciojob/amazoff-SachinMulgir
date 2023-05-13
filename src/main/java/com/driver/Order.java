package com.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;



public class Order {

    private String id;
    private int deliveryTime;

    public Order(){
    }
    public Order(String id, String deliveryTime) {
        setDeliveryTime(deliveryTime);
        this.id = id;
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setDeliveryTime( String deliveryTime ){
        List<String> list = Arrays.asList(deliveryTime.split(":"));
        int HH = Integer.parseInt(list.get(0));
        int MM = Integer.parseInt(list.get(1));
        this.deliveryTime = HH*60 + MM;
    }
}

package com.driver;

import com.driver.DeliveryPartner;
import com.driver.Order;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@NoArgsConstructor
@Repository
public class OrderRepository {

    Map<String, Order> orderMap = new HashMap<>();
    Map<String, DeliveryPartner> partnerMap = new HashMap<>();
    Map<String, String> orderPartnerMap;
    Map<String, List<String>> partnerOrderMap;

    public void addOrder(Order order){
        orderMap.put(order.getId(), order);
    }

    public void addPartner(DeliveryPartner partner) {
        partnerMap.put(partner.getId(), partner);
    }

    public Optional<Order> getOrderById(String orderId) {
        if(orderMap.containsKey(orderId) ){
            return Optional.of(orderMap.get(orderId));
        }
        return Optional.empty();
    }

    public Optional<DeliveryPartner> getPartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId) ){
            return Optional.of(partnerMap.get(partnerId));
        }
        return Optional.empty();
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderPartnerMap.put(orderId, partnerId);
        List<String> list = new ArrayList<>();
        if( partnerOrderMap.containsKey(partnerId)){
            list = partnerOrderMap.get(partnerId);
            list.add(orderId);
        }
        partnerOrderMap.put(partnerId, list);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public boolean isAssignedOrNot(String orderId) {
        if( orderPartnerMap.containsKey(orderId)){
            return true;
        }
        return false;
    }


    public void deletePartnerById(String partnerId) {
        partnerMap.remove(partnerId);
        partnerOrderMap.remove(partnerId);
    }

    public void deleteOrderPartnerMapping(String orderId, String partnerId) {
        orderPartnerMap.remove(orderId);
    }

    public void deleteOrderById(String orderId) {
        orderMap.remove(orderId);
        orderPartnerMap.remove(orderId);
    }


    public String getPartnerForOrder(String orderId) {
        return orderPartnerMap.get(orderId);
    }

    public void deleteInPartnerOrderMap(String orderId, String partnerId) {
        List<String> orders = partnerOrderMap.get(partnerId);
        orders.remove(orderId);
        partnerOrderMap.put(partnerId, orders);
    }
}

package com.driver;

import com.driver.Order;
import com.driver.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;


    public void addOrder(Order order) {
        this.orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        this.orderRepository.addPartner(partner);
        this.orderRepository.addPartner(partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        Optional<Order> opt1 = this.orderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> opt2 = this.orderRepository.getPartnerById(partnerId);

        if( opt1.isPresent() && opt2.isPresent() ){
            DeliveryPartner partner = opt2.get();
            int orders = partner.getNumberOfOrders();
            orders++;
            partner.setNumberOfOrders(orders);
            this.orderRepository.addPartner(partner);

            this.orderRepository.addOrderPartnerPair(orderId, partnerId);
        }
    }

    public Order getOrderById(String orderId) throws RuntimeException{
        Optional<Order> opt = this.orderRepository.getOrderById(orderId);
        if( opt.isPresent() )return opt.get();
        throw new RuntimeException("Order not found");
    }

    public DeliveryPartner getPartnerById(String partnerId) throws RuntimeException{
        Optional<DeliveryPartner> opt = this.orderRepository.getPartnerById(partnerId);
        if( opt.isPresent() ){
            return opt.get();
        }
        throw new RuntimeException("DeliveryPartner not found");
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner partner = getPartnerById(partnerId);
        return partner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return this.orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return this.orderRepository.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        int cnt = 0;
        List<String> orders = getAllOrders();
        for( String orderId : orders){
            if( this.orderRepository.isAssignedOrNot(orderId) == false ){
                cnt++;
            }
        }
        return cnt;
    }

    public Integer getOrderAfterTime(String time, String partnerId) {
        int cnt = 0;
        int t = convertTimeToInt(time);
        List<String> orders = this.orderRepository.getOrdersByPartnerId(partnerId);

        for( String orderId : orders){
            Optional<Order> opt = this.orderRepository.getOrderById(orderId);
            Order order = opt.get();
            if( order.getDeliveryTime() > t )cnt++;
        }

        return cnt;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orders = getOrdersByPartnerId(partnerId);
        int lastTime = 0;
        for( String orderId : orders){
            Order order = this.orderRepository.getOrderById(orderId).get();
            if( order.getDeliveryTime() > lastTime )lastTime = order.getDeliveryTime();
        }
        return convertTimeToString(lastTime);
    }





    public int convertTimeToInt(String time){
        List<String> list = Arrays.asList(time.split(":"));
        int hh = Integer.parseInt(list.get(0));
        int mm = Integer.parseInt(list.get(1));
        return (hh * 60 + mm);
    }

    public String convertTimeToString(int time){
        int HH = time/60;
        int MM = time%60;
        String hh = String.valueOf(HH);
        if( hh.length() == 0 ){
            hh = '0' + hh;
        }
        String mm = String.valueOf(MM);
        if( mm.length() == 0){
            mm = '0' + mm;
        }

        return  hh + ":" + mm;
    }

    public void deletePartnerById(String partnerId) {
        this.orderRepository.deletePartnerById(partnerId);
        List<String> orders = this.orderRepository.getAllOrders();
        for( String orderId : orders ){
            this.orderRepository.deleteOrderPartnerMapping(orderId, partnerId);
        }
    }

    public void deleteOrderById(String orderId) {
        String partnerId = this.orderRepository.getPartnerForOrder(orderId);
        this.orderRepository.deleteOrderById(orderId);
        if( partnerId != null ){
            DeliveryPartner partner = this.orderRepository.getPartnerById(partnerId).get();
            int orders = partner.getNumberOfOrders();
            orders--;
            partner.setNumberOfOrders(orders);
            this.orderRepository.addPartner(partner);
            this.orderRepository.deleteInPartnerOrderMap(orderId, partnerId);
        }
    }
}

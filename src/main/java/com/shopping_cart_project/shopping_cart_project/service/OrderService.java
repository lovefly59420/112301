package com.shopping_cart_project.shopping_cart_project.service;

import com.shopping_cart_project.shopping_cart_project.entity.Order;
import com.shopping_cart_project.shopping_cart_project.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public OrderService(){
        Dotenv dotenv = Dotenv.load();
        Stripe.apiKey = dotenv.get("STRIPE_PRIVATE_KEY");
    }


    public Session createCheckoutSession(int amount) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/checkout/success")
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("twd")
                                .setUnitAmount(amount*100L)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("96gen shopping cart product").build())
                                .build()
                ).build()).build();
        return Session.create(params);
    }


    public Order createOrder(String sessionId, Integer totalPrice, String status, String url, Long userId) throws Exception {
        Order order = new Order();
        order.setSessionId(sessionId);
        order.setAmount(totalPrice);
        order.setStatus(status);
        order.setUrl(url);
        order.setUserId(userId);
        return orderRepository.save(order);
    }

    public List<Order> findOrderByUserId(Long userId) throws Exception {
        List<Order> orders = orderRepository.findOrderByUserId(userId);
        List<Order> updated_orders = new ArrayList<>();
        for(Order order: orders){
            this.updateOrder(order.getId());
            updated_orders.add(order);
        }
        return updated_orders;
    }

    public void updateOrder(Long id) throws Exception {
        Optional<Order> opt = orderRepository.findById(id);
        if(opt.isPresent()){
            Order updated = opt.get();
            Session session =
                    //根據SessionId從Stripe API取得Session資料
                    Session.retrieve(
                            opt.get().getSessionId()
                    );
            //更新付款狀態
            updated.setStatus(session.getPaymentStatus());
            orderRepository.save(updated);
            return;
        }
        throw new Exception("Error: Order not found with id: " + id);
    }
}

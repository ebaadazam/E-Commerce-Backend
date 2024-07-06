package com.ebaad.ecommerce.controller;

import com.ebaad.ecommerce.exception.OrderException;
import com.ebaad.ecommerce.model.Order;
import com.ebaad.ecommerce.repository.OrderRespository;
import com.ebaad.ecommerce.response.ApiResponse;
import com.ebaad.ecommerce.response.PaymentLinkResponse;
import com.ebaad.ecommerce.service.OrderService;
import com.ebaad.ecommerce.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Value("{razorpay.api.key}")
    String apiKey;

    @Value("{razorpay.api.secret}")
    String apiSecret;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRespository orderRespository;

    // Method to create the payment link
    @PostMapping("/payments/{orderId}") // it is that orderId for which we want to generate the payment link
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
                                                                 @RequestHeader("Authorization")String jwt) throws OrderException, RazorpayException {
        Order order = orderService.findOrderById(orderId);

        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecret);

            // Create JSON Objects
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",order.getTotalPrice()*100);
            paymentLinkRequest.put("currency","INR");

            JSONObject customer = new JSONObject();
            customer.put("name",order.getUser().getUserName());
            customer.put("email",order.getUser().getEmail());
            paymentLinkRequest.put("customer",customer);

            JSONObject notify = new JSONObject();
            customer.put("sms",true);
            customer.put("email",true);
            paymentLinkRequest.put("notify",notify);

            // When payment is successful then it should redirect us to the page:
            paymentLinkRequest.put("callback_url","http://localhost:3000/payment/"+orderId); // if the application s deployed then give the deployed link instead of localhost
            paymentLinkRequest.put("callback_method","get");

            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId=payment.get("id");
            String paymentLinkUrl=payment.get("short_url");

            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPayment_link_id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);

            return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.CREATED);
        }
        catch (Exception e){
            throw new RazorpayException(e.getMessage());
        }
    }

    // Now after successful payment we need to change the status of order from PENDING to PLACED and also update the payment details
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name = "payment_id") String paymentId,
                                                @RequestParam(name = "order_id") Long orderId) throws RazorpayException, OrderException {
        Order order=orderService.findOrderById(orderId);
        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
        try {
            Payment payment = razorpay.payments.fetch(paymentId);

            // If payment is already captured then change the database details
            if (payment.get("status").equals("captured")){
                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                orderRespository.save(order);
            }

            ApiResponse res = new ApiResponse();
            res.setMessage("Yay! Your Order gets placed");
            res.setStatus(true);

            return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            throw new RazorpayException(e.getMessage());
        }
    }
}

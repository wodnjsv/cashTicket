package com.cashticket.controller;

import com.cashticket.config.CurrentUser;
import com.cashticket.entity.User;
import com.cashticket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${toss.client-key:test_ck_5OWRapdA8dd9zMMqkWbY8o1zEqZK}")
    private String clientKey;

    /* ■ 사전결제 위젯 페이지 (auctionId, amount 는 모델에 담긴 상태) */
    @GetMapping("/auction/{auctionId}")
    public String paymentPage(@PathVariable Long auctionId,
                              @RequestParam Long amount,
                              @CurrentUser User user,
                              Model model) {

        model.addAttribute("auctionId", auctionId);
        model.addAttribute("amount",    amount);
        model.addAttribute("user",      user);
        model.addAttribute("clientKey", clientKey);
        return "payment/index";
    }

    /* ■ Toss 위젯 successUrl */
    @GetMapping("/success")
    public String success(@RequestParam String paymentKey,
                          @RequestParam String orderId,
                          @RequestParam Long amount,
                          @RequestParam Long auctionId,
                          @CurrentUser User user,
                          Model model) {
        try {
            var payment = paymentService.approveAndSave(paymentKey, orderId, amount, auctionId, user);
            model.addAttribute("payment", payment);
            model.addAttribute("auctionId", auctionId);
            return "payment/success";
        } catch (Exception e) {
            model.addAttribute("code", "APPROVE_FAILED");
            model.addAttribute("message", e.getMessage());
            return "payment/fail";
        }
    }

    /* ■ Toss 위젯 failUrl */
    @GetMapping("/fail")
    public String fail(@RequestParam(required = false) String code,
                       @RequestParam(required = false) String message,
                       Model model) {

        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "payment/fail";
    }

}
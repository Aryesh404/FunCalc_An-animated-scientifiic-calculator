package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    private final String[] jokes = {
            "Why do Java developers wear glasses? Because they don't C#!",
            "I told my computer I needed a break, and now it won't stop sending me Kit Kat bars.",
            "Why don't programmers like nature? It has too many bugs.",
            "How many programmers does it take to change a light bulb? None, it's a hardware problem.",
            "Programming is 10% writing code and 90% understanding why it’s not working",
            "Programmers don't need money; they need advice"
    };

    @GetMapping("/calculate")
    public String calculate(@RequestParam double num1,
                            @RequestParam double num2,
                            @RequestParam String bt1,
                            HttpSession session,
                            Model model) {
        double ans = 0;

        try {
            switch (bt1) {
                case "1":
                    ans = num1 + num2;
                    break;
                case "2":
                    ans = num1 - num2;
                    break;
                case "3":
                    ans = num1 * num2;
                    break;
                case "4":
                    if (num2 != 0) {
                        ans = num1 / num2;
                    } else {
                        throw new ArithmeticException("Division by zero is not allowed.");
                    }
                    break;
                case "5": // Power
                    ans = Math.pow(num1, num2);
                    break;
                case "6": // Logarithm
                    if (num1 > 0) {
                        ans = Math.log(num1);
                    } else {
                        throw new ArithmeticException("Logarithm of non-positive number is not allowed.");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation.");
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        int randomIndex = (int) (Math.random() * jokes.length);
        String randomJoke = jokes[randomIndex];

        @SuppressWarnings("unchecked")
        List<String> history = (List<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<>();
        }

        history.add("Calculation: " + getOperationSymbol(bt1, num1, num2) + " = " + ans);
        session.setAttribute("history", history);

        model.addAttribute("ans", ans);
        model.addAttribute("joke", randomJoke);

        return "result";
    }

    private String getOperationSymbol(String operation, double a, double b) {
        switch (operation) {
            case "1":
                return a + " + " + b;
            case "2":
                return a + " - " + b;
            case "3":
                return a + " * " + b;
            case "4":
                return a + " / " + b;
            case "5":
                return a + "^" + b;
            case "6":
                return "log(" + a + ")";
            default:
                return "";
        }
    }

    @GetMapping("/history")
    public String showHistory(Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> history = (List<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<>();
        }
        model.addAttribute("history", history);
        return "history";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}

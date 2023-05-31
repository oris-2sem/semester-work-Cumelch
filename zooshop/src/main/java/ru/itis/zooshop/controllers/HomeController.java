package ru.itis.zooshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.zooshop.service.impl.OfferServiceImpl;

@Controller
public class HomeController {
    private final static int HOME_RECENT_OFFERS_COUNT = 3;

    private final OfferServiceImpl offerServiceImpl;
    public HomeController(OfferServiceImpl offerServiceImpl) {
        this.offerServiceImpl = offerServiceImpl;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recentOffers",
                offerServiceImpl.getRecentOffers(HOME_RECENT_OFFERS_COUNT));
        return "index";
    }

}

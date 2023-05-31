package ru.itis.zooshop.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.zooshop.exceptions.CategoryNotFoundException;
import ru.itis.zooshop.exceptions.OfferNotFoundException;
import ru.itis.zooshop.exceptions.UserNotFoundException;
import ru.itis.zooshop.model.dto.offer.CreateOfferDTO;
import ru.itis.zooshop.model.dto.offer.SearchOfferDTO;
import ru.itis.zooshop.model.dto.offer.UpdateOfferDTO;
import ru.itis.zooshop.model.user.UserDetails;
import ru.itis.zooshop.model.view.OfferDetailsView;
import ru.itis.zooshop.service.impl.CategoryServiceImpl;
import ru.itis.zooshop.service.impl.OfferServiceImpl;
import ru.itis.zooshop.util.CurrencyConverter;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/offers")
public class OfferController {
    private final OfferServiceImpl offerServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;

    public OfferController(OfferServiceImpl offerServiceImpl, CategoryServiceImpl categoryServiceImpl) {
        this.offerServiceImpl = offerServiceImpl;
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @GetMapping("/all")
    public String allOffers(
            Model model,
            @PageableDefault(
                    sort = "createdOn",
                    direction = Sort.Direction.DESC,
                    page = 0,
                    size = 8
            )Pageable pageable){

        model.addAttribute("offers", offerServiceImpl.getAllOffers(pageable));
        return "dashboard";
    }

    @GetMapping("/{username}")
    public String myOffers(
            @PathVariable String username,
            Model model,
            @PageableDefault(
                    sort = "createdOn",
                    direction = Sort.Direction.DESC,
                    page = 0,
                    size = 8
            ) Pageable pageable) {

        model.addAttribute("sellerUsername", username);
        model.addAttribute("offers", offerServiceImpl.getAllUserOffers(username, pageable));
        return "user-offers";
    }

    @GetMapping("/create")
    public String createOffer(Model model) {
        if (!model.containsAttribute("addOfferModel")) {
            model.addAttribute("addOfferModel", new CreateOfferDTO());
        }
        model.addAttribute("categories", categoryServiceImpl.getAllCategories());
        return "create-offer";
    }

    @PostMapping("/create")
    public String createOffers(@Valid CreateOfferDTO addOfferModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addOfferModel", addOfferModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addOfferModel",
                    bindingResult);
            return "redirect:/offers/create";
        }

        offerServiceImpl.addOffer(addOfferModel, userDetails);
        redirectAttributes.addFlashAttribute("waitForApproval",true);
        return "redirect:/offers/all";
    }

    @GetMapping("/{id}/details")
    public String detailOffer(
            Model model,
            @PathVariable("id") Long id
    ) {
        OfferDetailsView offerById = offerServiceImpl.getOfferById(id);
        BigDecimal offerPrice = offerById.getPrice();

        if (!offerPrice.equals(new BigDecimal(0))) {
            BigDecimal convertedPrice = CurrencyConverter.convert(offerPrice);
            model.addAttribute("convertedPrice", convertedPrice);
        }

        model.addAttribute("offer", offerById);
        model.addAttribute("offerSellerUsername", offerById.getSellerUsername());
        return "details-offer";
    }

    @PreAuthorize("@offerServiceImpl.isOwner(#principal.name,#id)")
    @GetMapping("/{id}/edit")
    public String editOffer(
            Model model,
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (!model.containsAttribute("editOffer")) {
            UpdateOfferDTO editOfferById = offerServiceImpl.getEditOfferById(id);
            model.addAttribute("editOffer",editOfferById);
        }
        model.addAttribute("offerId", id);

        return "edit-offer";
    }

    @PatchMapping("/{id}/edit")
    public String editOffer(
                            @PathVariable("id") Long id,
                            @Valid UpdateOfferDTO editOffer,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editOffer", editOffer);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editOffer",
                    bindingResult);
            return "redirect:/offers/{id}/edit";
        }

        offerServiceImpl.editOffer(id,editOffer);

        return "redirect:/offers/{id}/details";
    }

    @GetMapping("/search")
    public String searchQuery(@Valid SearchOfferDTO searchOfferDTO,
                              BindingResult bindingResult,
                              Model model,
                              @PageableDefault(
                                      sort = "createdOn",
                                      direction = Sort.Direction.DESC,
                                      page = 0,
                                      size = 8
                              )Pageable pageable) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("searchOfferModel", searchOfferDTO);
            model.addAttribute(
                    "org.springframework.validation.BindingResult.searchOfferModel",
                    bindingResult);
            return "search-offer";
        }

        if (!model.containsAttribute("searchOfferModel")) {
            model.addAttribute("searchOfferModel", searchOfferDTO);
        }

        if (    searchOfferDTO.getName() != null
                || searchOfferDTO.getCategory() != null
                || searchOfferDTO.getMaxPrice() != null
                || searchOfferDTO.getMinPrice() != null) {

            model.addAttribute("offers", offerServiceImpl.searchOffer(searchOfferDTO, pageable));
        }

        return "search-offer";
    }

    @PreAuthorize("@offerServiceImpl.isOwner(#principal.name,#id)")
    @DeleteMapping("/{id}/delete")
    public String deleteOffer(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        offerServiceImpl.deleteOfferById(id);

        return "redirect:/offers/all";
    }

    @PreAuthorize("@offerServiceImpl.isOwner(#principal.name,#id)")
    @PatchMapping("/{id}/approve")
    public String approveOffer(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        offerServiceImpl.approveOfferById(id);

        return "redirect:/offers/all";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({OfferNotFoundException.class})
    public String onOfferNotFound(OfferNotFoundException exception,Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String onUserNotFound(UserNotFoundException exception,Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler({CategoryNotFoundException.class})
    public String onCategoryNotFound(CategoryNotFoundException exception,Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }


}

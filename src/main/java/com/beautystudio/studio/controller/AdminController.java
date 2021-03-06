package com.beautystudio.studio.controller;

import com.beautystudio.studio.model.Category;
import com.beautystudio.studio.model.SubCategory;
import com.beautystudio.studio.service.ICategoryService;
import com.beautystudio.studio.service.IReservationService;
import com.beautystudio.studio.service.ISubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ISubCategoryService iSubCategoryService;

    @Autowired
    private IReservationService iReservationService;

    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/main")
    public String showAdminPanel(Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/homePage";
    }

    @GetMapping(value = "/addCategory")
    public String showAddCategoryForm(Model model){
        model.addAttribute("category", new Category());
        return "admin/addcategory";
    }

    @GetMapping(value = "/addSubCategory")
    public String showAddSubcategoryForm(Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategory", new SubCategory());
        return "admin/addsubcategory";
    }

    @PostMapping(value = "/addCategory")
    public String addCategory(@ModelAttribute Category category){
        categoryService.add(category);
        return "redirect:/admin/main";
    }

    @PostMapping(value = "/addSubCategory")
    public String addSubCategory(@ModelAttribute SubCategory subCategory){
        iSubCategoryService.add(subCategory);
        return "redirect:/admin/main";
    }

    @GetMapping(value = "/reservation")
    public String showReservation(Model model){
        model.addAttribute("reservations", iReservationService.showAllReservationWithStatusFalse());
        return "admin/reservationListForm";
    }

    @GetMapping(value = "/reservation/{reservationId}")
    public String confirmReservation(@PathVariable("reservationId") Long id){
        iReservationService.confirmReservation(id);
        return "redirect:/admin/reservation";
    }


}

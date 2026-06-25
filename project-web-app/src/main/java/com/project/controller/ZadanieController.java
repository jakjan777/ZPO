package com.project.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import com.project.exception.HttpException;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.service.ProjektService;
import com.project.service.ZadanieService;

@Controller
public class ZadanieController {

    private static final Logger logger = LoggerFactory.getLogger(ZadanieController.class);

    private ZadanieService zadanieService;
    private ProjektService projektService;

    public ZadanieController(ZadanieService zadanieService, ProjektService projektService) {
        this.zadanieService = zadanieService;
        this.projektService = projektService;
    }

    private void addProjektyToModel(Model model) {
        model.addAttribute("projekty",
                projektService.getProjekty(PageRequest.of(0, 100, Sort.by("nazwa"))).getContent());
    }

    @GetMapping("/zadanieList")
    public String zadanieList(Model model, Pageable pageable) {
        model.addAttribute("zadania", zadanieService.getZadania(pageable).getContent());
        return "zadanieList";
    }

    @GetMapping("/zadanieEdit")
    public String zadanieEdit(@RequestParam(name = "zadanieId", required = false) Integer zadanieId, Model model) {
        if (zadanieId != null) {
            model.addAttribute("zadanie", zadanieService.getZadanie(zadanieId).get());
        } else {
            Zadanie zadanie = new Zadanie();
            zadanie.setProjekt(new Projekt());
            model.addAttribute("zadanie", zadanie);
        }
        addProjektyToModel(model);
        return "zadanieEdit";
    }

    @PostMapping(path = "/zadanieEdit")
    public String zadanieEditSave(@ModelAttribute @Valid Zadanie zadanie, BindingResult bindingResult, Model model) {
        if (zadanie.getProjekt() == null || zadanie.getProjekt().getProjektId() == null) {
            bindingResult.rejectValue("projekt.projektId", "NotNull", "Projekt musi być wybrany!");
        }
        if (bindingResult.hasErrors()) {
            addProjektyToModel(model);
            return "zadanieEdit";
        }
        try {
            zadanie = zadanieService.setZadanie(zadanie);
        } catch (HttpStatusCodeException e) {
            bindingResult.rejectValue("", String.valueOf(e.getStatusCode().value()),
                    e.getStatusCode().toString());
            addProjektyToModel(model);
            return "zadanieEdit";
        } catch (HttpException e) {
            bindingResult.reject("", e.getMessage());
            addProjektyToModel(model);
            return "zadanieEdit";
        }
        return "redirect:/zadanieList";
    }

    @PostMapping(params = "cancel", path = "/zadanieEdit")
    public String zadanieEditCancel() {
        return "redirect:/zadanieList";
    }

    @PostMapping(params = "delete", path = "/zadanieEdit")
    public String zadanieEditDelete(@RequestParam(name = "zadanieId") Integer zadanieId) {
        zadanieService.deleteZadanie(zadanieId);
        return "redirect:/zadanieList";
    }
}

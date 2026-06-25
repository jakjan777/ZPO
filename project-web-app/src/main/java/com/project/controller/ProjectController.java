package com.project.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
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
import com.project.service.ProjektService;

@Controller
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private ProjektService projektService;

    public ProjectController(ProjektService projektService) {
        this.projektService = projektService;
    }

    @GetMapping("/projektList")
    public String projektList(Model model, Pageable pageable) {
        model.addAttribute("projekty", projektService.getProjekty(pageable).getContent());
        return "projektList";
    }

    @GetMapping("/projektEdit")
    public String projektEdit(@RequestParam(name = "projektId", required = false) Integer projektId, Model model) {
        if (projektId != null) {
            model.addAttribute("projekt", projektService.getProjekt(projektId).get());
        } else {
            Projekt projekt = new Projekt();
            model.addAttribute("projekt", projekt);
        }
        return "projektEdit";
    }

    @PostMapping(path = "/projektEdit")
    public String projektEditSave(@ModelAttribute @Valid Projekt projekt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "projektEdit";
        }
        try {
            projekt = projektService.setProjekt(projekt);
        } catch (HttpStatusCodeException e) {
            bindingResult.rejectValue("", String.valueOf(e.getStatusCode().value()),
                    e.getStatusCode().toString());
            return "projektEdit";
        } catch (HttpException e) {
            bindingResult.reject("", e.getMessage());
            return "projektEdit";
        }
        return "redirect:/projektList";
    }

    @PostMapping(params = "cancel", path = "/projektEdit")
    public String projektEditCancel() {
        return "redirect:/projektList";
    }

    @PostMapping(params = "delete", path = "/projektEdit")
    public String projektEditDelete(@RequestParam(name = "projektId") Integer projektId) {
        projektService.deleteProjekt(projektId);
        return "redirect:/projektList";
    }
}

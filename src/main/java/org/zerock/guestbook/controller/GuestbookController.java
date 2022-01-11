package org.zerock.guestbook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.guestbook.repository.GuestbookRepository;

@Controller
@Slf4j
@RequestMapping("/guestbook")
public class GuestbookController {

    @Autowired
    public GuestbookRepository guestbookRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(Model model) {
        log.info("list...........");

        model.addAttribute("result", guestbookRepository.findAll());
    }
}

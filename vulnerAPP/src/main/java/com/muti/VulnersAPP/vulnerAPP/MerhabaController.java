package com.muti.VulnersAPP.vulnerAPP;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MerhabaController {

    @RequestMapping("/merhaba")
    public void merhaba(Model model) {
        String mesajicerigi="Merhaba Dünya";
        String mesajicerigi2="Merhaba Dünya2";
        model.addAttribute("mesaj", mesajicerigi);
        model.addAttribute("mesaj2", mesajicerigi2);
    }
}
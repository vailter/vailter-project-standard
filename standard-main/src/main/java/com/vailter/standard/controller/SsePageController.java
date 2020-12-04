package com.vailter.standard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sse")
public class SsePageController {

    @GetMapping("sse1")
    public String sse1Page() {
        return "sse1";
    }

    @GetMapping("sse2")
    public String sse2Page() {
        return "sse2";
    }
}

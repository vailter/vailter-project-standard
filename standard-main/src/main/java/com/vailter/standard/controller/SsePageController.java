package com.vailter.standard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping
    @ResponseBody
    public String test() {
        System.out.println(1/0);
        return "123";
    }
}

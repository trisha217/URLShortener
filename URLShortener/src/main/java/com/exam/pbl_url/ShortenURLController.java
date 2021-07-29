package com.exam.pbl_url;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ShortenURLController
{

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String loadIndex() {
        return "index";
    }

}
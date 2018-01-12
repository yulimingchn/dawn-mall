package com.dawn.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dawn.web.service.IndexService;

@RequestMapping
@Controller
public class IndexController {
    
    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        // 首页大广告
        String indexAD1 = this.indexService.queryIndexAD1();
        mv.addObject("indexAD1", indexAD1);
        return mv;
    }

}

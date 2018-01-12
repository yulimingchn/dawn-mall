package com.dawn.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dawn.search.bean.SearchResult;
import com.dawn.search.service.SearchService;

@RequestMapping("search")
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    public static final Integer ROWS = 32;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWords,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        ModelAndView mv = new ModelAndView("search");
        
        try {
            keyWords = new String(keyWords.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SearchResult searchResult = this.searchService.search(keyWords, page, ROWS);
        
        // 搜索关键字
        mv.addObject("query", keyWords);
        // 商品列表
        mv.addObject("itemList", searchResult.getList());
        // 当前页数
        mv.addObject("page", page);
        // 总页数
        int total = searchResult.getTotal().intValue();
        int pages = total % ROWS == 0 ? total / ROWS : total / ROWS + 1;
        mv.addObject("pages", pages);

        return mv;
    }

}

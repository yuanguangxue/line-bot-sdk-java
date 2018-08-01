package com.hp.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.enumerate.TagEnum;
import com.hp.model.Result;
import com.hp.model.Suggestion;
import com.hp.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping
public class SuggestionController {

    @Autowired
    SuggestionService suggestionService;

    @RequestMapping
    public ModelAndView index(ModelAndView model){
        model.setViewName("index");
        return model;
    }

    @PostMapping("/saveSuggestion")
    @ResponseBody
    public String saveSuggestion(@Valid Suggestion suggestion) throws JsonProcessingException {
        suggestion.setCreatedAt(new Date());
        suggestionService.save(suggestion);
        return Result.success("保存成功！").toJson();
    }

    @GetMapping("/getTags.json")
    @ResponseBody
    public String getTags(){
        return TagEnum.toJsonString();
    }


    @RequestMapping(value = "/listSuggestion",produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public List<Suggestion> listSuggestion(Suggestion suggestion,
                                           @RequestParam(name = "page",defaultValue = "0") int page,
                                           @RequestParam(name = "size",defaultValue = "5") int size) {
        PageRequest pageRequest = new PageRequest(page,size,Sort.Direction.DESC,"createdAt");
        return suggestionService.findSuggestionPage(suggestion,pageRequest);
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel(HttpServletResponse response,
                      Suggestion suggestion,
                      @RequestParam(name = "page",defaultValue = "0") int page,
                      @RequestParam(name = "size",defaultValue = "5") int size) throws Exception {
        PageRequest pageRequest = new PageRequest(page,size,Sort.Direction.DESC,"createdAt");
        suggestionService.excel(response,suggestion,pageRequest);
    }

}

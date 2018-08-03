package com.hp.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hp.enumerate.ResultCode;
import com.hp.enumerate.TagEnum;
import com.hp.model.BootstrapTable;
import com.hp.model.Result;
import com.hp.model.Suggestion;
import com.hp.service.SuggestionService;
import com.hp.util.DateTimePropertyEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;


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

    @RequestMapping("/admin")
    public ModelAndView admin(ModelAndView model){
        model.setViewName("admin");
        return model;
    }

    @GetMapping("/getSuggestion")
    @ResponseBody
    public String getSuggestion(String id) throws JsonProcessingException {
        try {
            Suggestion suggestion = suggestionService.get(id);
            return Result.success(suggestion).toJson();
        }catch (Exception e){
            return Result.failure(ResultCode.DATA_IS_WRONG,"获取id=【"+id+"】的建议内容失败！"+ e.getMessage()).toJson();
        }
    }

    @PostMapping("/saveSuggestion")
    @ResponseBody
    public String saveSuggestion(@Valid Suggestion suggestion) throws JsonProcessingException {
        suggestion.setCreatedAt(new Date());
        try {
            suggestionService.save(suggestion);
            return Result.success("保存成功！").toJson();
        }catch (Exception e){
            return Result.failure(ResultCode.DATA_IS_WRONG,"保存失败！"+ e.getMessage()).toJson();
        }
    }


    @GetMapping("/delSuggestion")
    @ResponseBody
    public String delSuggestion(String id) throws JsonProcessingException {
        try {
            suggestionService.delete(id);
            return Result.success("删除成功！").toJson();
        }catch (Exception e){
            return Result.failure(ResultCode.DATA_IS_WRONG,"删除失败！"+ e.getMessage()).toJson();
        }
    }

    @GetMapping("/getTags.json")
    @ResponseBody
    public String getTags(){
        return TagEnum.toJsonString();
    }


    @RequestMapping(value = "/listSuggestion",produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public BootstrapTable<Suggestion> listSuggestion(Suggestion suggestion,
                                                     @RequestParam(name = "offset",defaultValue = "0") int offset,
                                                     @RequestParam(name = "limit",defaultValue = "5") int size) {
        int page = offset / size;
        PageRequest pageRequest = new PageRequest(page,size,Sort.Direction.DESC,"createdAt");
        return suggestionService.findSuggestionPage(suggestion,pageRequest);
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel(HttpServletResponse response,
                      Suggestion suggestion,
                      @RequestParam(name = "offset",defaultValue = "0") int offset,
                      @RequestParam(name = "limit",defaultValue = "5") int size) throws Exception {
        int page = offset / size;
        PageRequest pageRequest = new PageRequest(page,size,Sort.Direction.DESC,"createdAt");
        suggestionService.excel(response,suggestion,pageRequest);
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(Date.class,new DateTimePropertyEditor());
    }

}

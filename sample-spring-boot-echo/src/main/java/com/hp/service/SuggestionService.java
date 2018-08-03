package com.hp.service;

import com.hp.model.BootstrapTable;
import com.hp.model.ExcelData;
import com.hp.model.Suggestion;
import com.hp.repository.SuggestionRepository;

import com.hp.util.ExportExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionService {

    @Autowired
    private SuggestionRepository suggestionRepository;

    public void save(Suggestion suggestion){
        suggestionRepository.save(suggestion);
    }

    public void delete(String id){
        suggestionRepository.delete(id);
    }


    public Suggestion get(String id){
        return suggestionRepository.findOne(id);
    }

    public BootstrapTable<Suggestion> findSuggestionPage(final Suggestion suggestion, Pageable pageable){
        Page<Suggestion> page = suggestionRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if(StringUtils.isNotBlank(suggestion.getTitle())){
                predicate = cb.and(predicate,cb.like(root.get("title"),"%"+suggestion.getTitle()+"%"));
            }
            if(StringUtils.isNotBlank(suggestion.getContent())){
                predicate = cb.and(predicate,cb.like(root.get("content"),"%"+suggestion.getContent()+"%"));
            }
            if(StringUtils.isNotBlank(suggestion.getTag())){
                predicate = cb.and(predicate,cb.like(root.get("tag"),"%"+suggestion.getTag()+"%"));
            }
            if(suggestion.getCreatedAt_start()!=null){
                predicate = cb.and(predicate,cb.greaterThanOrEqualTo(root.get("createdAt"),suggestion.getCreatedAt_start()));
            }
            if(suggestion.getCreatedAt_end()!=null){
                predicate = cb.and(predicate,cb.lessThanOrEqualTo(root.get("createdAt"),suggestion.getCreatedAt_end()));
            }
            return predicate;
        }, pageable);
        return BootstrapTable.to(page);
    }


    public void excel(HttpServletResponse response,final Suggestion suggestion,Pageable pageable) throws Exception {
        final ExcelData data = new ExcelData();
        data.setName("Suggestion");
        BootstrapTable<Suggestion> bootstrapTable = findSuggestionPage(suggestion,pageable);
        List<ExcelData.Head> heads = new ArrayList<>();
        ExcelData.Head headTitle = ExcelData.Head.builder().title("标题").length(25).build();
        ExcelData.Head headTag = ExcelData.Head.builder().title("分类").length(25).build();
        ExcelData.Head headContent = ExcelData.Head.builder().title("内容").length(150).build();
        ExcelData.Head headCreatedAt = ExcelData.Head.builder().title("创建时间").length(60).build();
        heads.add(headTitle);
        heads.add(headTag);
        heads.add(headContent);
        heads.add(headCreatedAt);
        data.setHeads(heads);
        List<List<Object>> rows = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bootstrapTable.getRows().forEach(sug -> {
            List<Object> row = new ArrayList<>();
            row.add(sug.getTitle());
            row.add(sug.displayTag());
            row.add(sug.getContent());
            row.add(dateFormat.format(sug.getCreatedAt()));
            rows.add(row);
        });
        data.setRows(rows);
        ExportExcelUtils.exportExcel(response,"CSS-WeChat-Team-Suggestion.xlsx",data);
    }
}

package com.apec.systemconfig.service.impl;

import com.apec.framework.common.enumtype.EnableFlag;
import com.apec.framework.common.util.BeanUtil;
import com.apec.systemconfig.dao.WordBookDAO;
import com.apec.systemconfig.model.WordBook;
import com.apec.systemconfig.service.WordBookService;
import com.apec.systemconfig.vo.WordBookViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hmy on 2017/8/2.
 * @author hmy
 */
@Service
public class WordBookServiceImpl implements WordBookService {

    @Autowired
    private WordBookDAO wordBookDAO;


    /**
     * 根据code获取相应所需的字典表数据
     */
    @Override
    public List<WordBookViewVO> listNeedWordBook(WordBookViewVO vo) {
        List<WordBookViewVO> result = new ArrayList<>();
        Iterable<WordBook> wordBooks = wordBookDAO.findByCodeAndEnableFlagOrderBySort(vo.getCode(), EnableFlag.Y);
        Iterator<WordBook> it = wordBooks.iterator();
        while(it.hasNext()){
            WordBookViewVO viewVO = new WordBookViewVO();
            WordBook wordBook = it.next();
            if(wordBook != null){
                BeanUtil.copyPropertiesIgnoreNullFilds(wordBook,viewVO);
                result.add(viewVO);
            }
        }
        return result;
    }



}

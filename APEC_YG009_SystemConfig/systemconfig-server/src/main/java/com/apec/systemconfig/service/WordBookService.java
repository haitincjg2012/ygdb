package com.apec.systemconfig.service;

import com.apec.systemconfig.vo.WordBookViewVO;

import java.util.List;

/**
 * Created by hmy on 2017/8/2.
 * @author hmy
 */
public interface WordBookService {

    /**
     * 查询字典表信息
     * @param vo vo
     * @return 查询字典表信息
     */
    List<WordBookViewVO> listNeedWordBook(WordBookViewVO vo);

}

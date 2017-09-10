package com.apec.systemconfig.service;

import com.apec.systemconfig.vo.WordBookViewVO;

import java.util.List;

/**
 * Created by hmy on 2017/8/2.
 */
public interface WordBookService {

    List<WordBookViewVO> listNeedWordBook(WordBookViewVO vo);

}

package com.apec.systemuser.service;

import com.apec.framework.common.PageDTO;
import com.apec.systemuser.vo.SysResourceVO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SysResourceService
{

    PageDTO<SysResourceVO> searchPage(SysResourceVO vo, PageRequest pageRequest);

    void insert(SysResourceVO vo, String userId);

    void delete(Long id, Long userId);

    void update(SysResourceVO vo, String userId);

    SysResourceVO findOne(Long id);

    List<SysResourceVO> selectAll(SysResourceVO vo);

}

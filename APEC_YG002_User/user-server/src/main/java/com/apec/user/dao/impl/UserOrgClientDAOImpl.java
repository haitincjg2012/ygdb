package com.apec.user.dao.impl;

import com.apec.framework.common.PageDTO;
import com.apec.user.dao.FocusUserOrgClientDAO;
import com.apec.user.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by hmy on 2017/9/19.
 */
@Repository
public class UserOrgClientDAOImpl implements FocusUserOrgClientDAO {

    @Autowired
    private EntityManager em;

    @Override
    public PageDTO<Object[]> findMyFocusF(List<Long> userOrgIds, Pageable pageable, UserDTO userDTO) {

        String dataSql = " SELECT o.id as orgId,o.org_name,o.address,o.org_first_banner_url,u.id as userId,u.user_type,u.user_real_auth FROM (select * from user_org_client where id in (:userOrgIds) and enable_flag = 'Y' ) o "
                + " left join (select * from user where user_account_type != 'ORG_CHILD_ACCOUNT' and enable_flag = 'Y' ) u  on u.user_org_id = o.id  ";
        String countSql = " SELECT count(*) FROM ( SELECT * from user_org_client where id in (:userOrgIds) ) o "
                + " left join (select * from user where user_account_type != 'ORG_CHILD_ACCOUNT') u  on u.user_org_id = o.id  ";

        if(userDTO != null && (StringUtils.isNotBlank(userDTO.getAddress()) || userDTO.getUserType() != null)) {
            dataSql += "  where  ";
            countSql += "  where ";
            if(StringUtils.isNotBlank(userDTO.getAddress())){
                dataSql += "  o.address like :address ";
                countSql += " o.address like :address ";
            }
            if(userDTO.getUserType() != null){
                if(StringUtils.isNotBlank(userDTO.getAddress())){
                    dataSql += "  and ";
                    countSql += " and ";
                }
                dataSql += "  u.user_type = :userType ";
                countSql += "  u.user_type = :userType ";
            }
        }

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if(pageNumber < 0){
            pageNumber = 0;
        }
        if(pageSize <= 0){
            pageSize = 10;
        }
        int star = pageNumber * pageSize;
        int end = (pageNumber + 1) * pageSize;

        dataSql += " limit " + star + "," + end;

        Query dataQuery = em.createNativeQuery(dataSql);
        Query countQuery = em.createNativeQuery(countSql);

        dataQuery.setParameter("userOrgIds", userOrgIds);
        countQuery.setParameter("userOrgIds", userOrgIds);
        if(userDTO != null){
            if(StringUtils.isNotBlank(userDTO.getAddress())){
                dataQuery.setParameter("address", "%" + userDTO.getAddress() + "%");
                countQuery.setParameter("address", "%" + userDTO.getAddress() + "%");
            }
            if(userDTO.getUserType() != null){
                dataQuery.setParameter("userType", userDTO.getUserType().name());
                countQuery.setParameter("userType", userDTO.getUserType().name());
            }
        }
        BigInteger totalSize = (BigInteger)countQuery.getSingleResult();
        PageDTO<Object[]> page = new PageDTO<>();
        page.setTotalElements(totalSize.longValue());
        List<Object[]> data = dataQuery.getResultList();
        page.setRows(data);
        page.setNumber(pageNumber + 1);
        page.setTotalPages(new Long(page.getTotalElements()%pageSize == 0?page.getTotalElements()/pageSize:page.getTotalElements()/pageSize+1).intValue());
        return page;
    }

}

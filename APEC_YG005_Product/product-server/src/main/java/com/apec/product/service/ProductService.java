package com.apec.product.service;

import com.apec.framework.common.ResultData;
import com.apec.framework.dto.UserInfoVO;
import com.apec.product.vo.ProductInfoVO;
import com.apec.product.vo.ProductSkuInfoVO;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.List;
import java.util.Map;

/**
 * 产品相关服务
 */
public interface ProductService {


    String testESInfo();

    /**
     * 添加新的供求
     * @param userInfoVO 用户信息VO
     * @param productSkuInfoVO　SkuInfo
     * @return returnCode 返回码
     */
    String addProductAttr(UserInfoVO userInfoVO, ProductSkuInfoVO productSkuInfoVO,  List<Map<String,String>> resultMap);

    /**
     * 添加新的供求
     * @param userInfoVO 用户信息VO
     * @param productInfoVO　产品Info
     * @return returnCode 返回码
     */
    String addNewProduct(UserInfoVO userInfoVO, ProductInfoVO productInfoVO);

    /**
     * 保存供求信息
     * @param productInfoVO 供求信息VO
     * @return returnCode 返回码
     */
    String saveProduct(ProductInfoVO productInfoVO);

    /**
     * 下架供求job
     * @return
     */
    void offSellProductJob();

    /**
     * 根据ES ID下架供求
     * @param userInfoVO
     * @param productInfoVO
     * @return
     */
    String offSellProduct(UserInfoVO userInfoVO, ProductInfoVO productInfoVO);

    /**
     * 修改ES中供求信息
     * @param userInfoVO
     * @param productInfoVO
     * @return
     */
    String updateProductByES(UserInfoVO userInfoVO, ProductInfoVO productInfoVO);

    /**
     * 修改数据库中的供求信息
     * @param productInfoVO
     * @return
     */
    String updateProduct(ProductInfoVO productInfoVO);
}

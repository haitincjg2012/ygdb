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
     * 代发 新的供求
     * @param userNo 用户的ID
     * @param productSkuInfoVO　SkuInfo
     * @return returnCode 返回码
     */
    String addProductAttrByInstead(Long userNo, ProductSkuInfoVO productSkuInfoVO,  List<Map<String,String>> resultMap);


    /**
     * 代发添加新的供求
     * @param userNo 用户信息VO
     * @param productInfoVO　产品Info
     * @return returnCode 返回码
     */
    String addNewProductByInstead(Long userNo,ProductInfoVO productInfoVO);

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
     * 根据ES ID下架供求
     * @param productInfoVO
     * @return
     */
    String offSellProductByManager(ProductInfoVO productInfoVO);

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

    /**
     * 根据indexUrl推送供求信息到ES中
     * @param indexUrl
     * @return
     */
    String pushProductToEs(String indexUrl);

    /**
     * 根据indexUrl推送下架信息到ES中 取最新的10条数据
     * @param indexUrl
     * @return
     */
    String pushOffSellProductToEs(String indexUrl);
}

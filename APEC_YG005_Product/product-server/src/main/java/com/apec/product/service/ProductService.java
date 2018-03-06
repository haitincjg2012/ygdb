package com.apec.product.service;

import com.apec.framework.common.PageDTO;
import com.apec.framework.dto.UserInfoVO;
import com.apec.product.dto.ProductInfoDTO;
import com.apec.product.vo.ProductInfoVO;
import com.apec.product.vo.ProductSkuInfoVO;
import com.apec.product.vo.WeekBest;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 产品相关服务
 * @author yido
 */
public interface ProductService {

    /**
     * 添加新的供求
     * @param userInfoVO 用户信息VO
     * @param productSkuInfoVO　SkuInfo
     * @param resultMap　有改动的字段
     * @return returnCode 返回码
     */
    String addProductAttr(UserInfoVO userInfoVO, ProductSkuInfoVO productSkuInfoVO,  List<Map<String,String>> resultMap);

    /**
     * 代发 新的供求
     * @param userNo 用户的ID
     * @param productSkuInfoVO　SkuInfo
     * @param resultMap　有改动的字段
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
     */
    void offSellProductJob();

    /**
     * 根据ES ID下架供求
     * @param userInfoVO 用户信息
     * @param productInfoVO 供求信息
     * @return returnCode 返回码
     */
    String offSellProduct(UserInfoVO userInfoVO, ProductInfoVO productInfoVO);

    /**
     * 根据ES ID下架供求
     * @param productInfoVO 供求信息
     * @return returnCode 返回码
     */
    String offSellProductByManager(ProductInfoVO productInfoVO);

    /**
     * 修改ES中供求信息
     * @param userInfoVO 用户信息
     * @param productInfoVO 供求信息
     * @return returnCode 返回码
     */
    String updateProductByES(UserInfoVO userInfoVO, ProductInfoVO productInfoVO);

    /**
     * 修改数据库中的供求信息
     * @param productInfoVO 供求信息
     * @return returnCode 返回码
     */
    String updateProduct(ProductInfoVO productInfoVO);

    /**
     * 根据indexUrl推送供求信息到ES中
     * @param indexUrl 索引
     * @return returnCode 返回码
     */
    String pushProductToEs(String indexUrl);

    /**
     * 根据indexUrl推送下架信息到ES中 取最新的10条数据
     * @param indexUrl 索引
     * @return returnCode 返回码
     */
    String pushOffSellProductToEs(String indexUrl);

    /**
     * 调果达人
     * @return 调果达人信息
     */
    List<WeekBest> maxProduct();

    /**
     * 查询符合条件的用户信息
     * @param num 供求条数
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return
     */
    List<Object[]> queryUserForRanking(int num, Date startTime, Date endTime);

    /**
     * 查询用户上传的供求条数
     * @param userIds 用户id
     * @return 上传供求信息
     */
    List<Object[]> queryUserInfoForRanking(List<Long> userIds);

    /**
     * 分页查询供求信息
     * @param productInfoDTO 查询条件
     * @param pageRequest 分页条件
     * @return 分页结果
     */
    PageDTO<ProductInfoVO> queryProductPage(ProductInfoDTO productInfoDTO, PageRequest pageRequest);

    /**
     * 查询要导出的供求信息
     * @param dto 查询条件
     * @return 供求信息
     */
    List<Object[]> selectProductInfoForExcel(ProductInfoDTO dto);

    /**
     * 通过esId查询供求详情
     * @param productInfoVO 供求信息
     * @return 供求详情
     */
    ProductInfoVO queryProductInfoByEsId(ProductInfoVO productInfoVO);

    /**
     * 供求加标签
     * @param productInfoVO 供求详情
     * @param userId 操作人id
     * @return 操作是否成功
     */
    String setProductTags(ProductInfoVO productInfoVO, String userId);

    /**
     * 置顶供求信息
     * @param productInfoVO 要置顶的供求
     * @param userId 操作员id
     * @return 操作结果码
     */
    String stickProductInfo(ProductInfoVO productInfoVO, String userId);

    /**
     * 取消置顶供求信息
     * @param productInfoVO 要置顶的供求
     * @param userId 操作员id
     * @return 操作结果码
     */
    String closeStickProductInfo(ProductInfoVO productInfoVO, String userId);

}

package com.apec.society.web;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.PageDTO;
import com.apec.framework.common.ResultData;
import com.apec.framework.common.enums.Realm;
import com.apec.framework.common.excel.ExcelExportUtils;
import com.apec.framework.common.excel.XlsVO;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.util.BaseJsonUtil;
import com.apec.framework.common.util.DateTimeUtils;
import com.apec.framework.common.util.FileUtils;
import com.apec.framework.ftp.service.FtpService;
import com.apec.framework.log.InjectLogger;
import com.apec.framework.springcloud.SpringCloudClient;
import com.apec.society.dto.SocietyPostDTO;
import com.apec.society.service.SocietyPostService;
import com.apec.society.view.QuotationView;
import com.apec.society.view.SocietyPostViewVO;
import com.apec.society.vo.SocietyPostAggreVO;
import com.apec.society.vo.SocietyPostVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Created by hmy on 2017/10/30.
 * 帖子
 * @author hmy
 */
@RestController
@RequestMapping(value = "/societyPost")
public class SocietyPostController extends MyBaseController {

    @Autowired
    private SocietyPostService societyPostService;

    @InjectLogger
    private Logger log;

    @Autowired
    private SpringCloudClient springCloudClient;

    @Autowired
    private FtpService ftpService;

    @Value("${EXCELFILEPATH}")
    String excelPath = "";

    @Value("${EXCELFILE_URL}")
    String excelUrl = "";

    /**
     * 添加帖子
     */
    @RequestMapping(value = "/addSocietyPost", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addSocietyPost(@RequestBody String json){
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || StringUtils.isBlank(societyPostVO.getContent());
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            if(!CollectionUtils.isEmpty(societyPostVO.getSocietyPostImagesVOS()) && societyPostVO.getSocietyPostImagesVOS().size() > Integer.valueOf(Constants.POST_IMAGES_LENGTH)){
                return super.getResultJSONStr(false,null, ErrorCodeConst.IMAGES_OUT_LENGTH);
            }
            societyPostVO.setUserId(getUserId(json));
            String returnCode = societyPostService.addSocietyPost(societyPostVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPost.addSocietyPost] Add SocietyPost  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPost.addSocietyPost] Add SocietyPost Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 添加行情竞猜
     */
    @RequestMapping(value = "/addQuotation", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addQuotation(@RequestBody String json){
        try {
            QuotationView quotationView = getFormJSON(json, QuotationView.class);
            boolean flag = quotationView == null || StringUtils.isBlank(quotationView.getTitle())
                    || quotationView.getStartTime() == null || quotationView.getEndTime() == null;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            if(DateTimeUtils.getDifferTime(quotationView.getStartTime(),quotationView.getEndTime(),DateTimeUtils.SECOND) >= 0){
                return super.getResultJSONStr(false,null, ErrorCodeConst.ERROR_EDIT_TIME);
            }
            String returnCode = societyPostService.addQuotation(quotationView,String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPost.addQuotation] Add addQuotation  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPost.addQuotation] Add addQuotation Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 修改行情竞猜信息
     */
    @RequestMapping(value = "/updateQuotation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateQuotation(@RequestBody String json) {
        try {
            QuotationView quotationView = getFormJSON(json, QuotationView.class);
            boolean flag = quotationView == null || quotationView.getId() == null || quotationView.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.updateQuotation(quotationView);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        } catch (BusinessException e) {
            log.error("[societyPost][updateQuotation] updateQuotation BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost][updateQuotation]  updateQuotation Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 行情竞猜信息下线
     */
    @RequestMapping(value = "/outLineQuotation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String outLineQuotation(@RequestBody String json) {
        try {
            QuotationView quotationView = getFormJSON(json, QuotationView.class);
            boolean flag = quotationView == null || quotationView.getId() == null || quotationView.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.outLineQuotation(quotationView);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        } catch (BusinessException e) {
            log.error("[societyPost][outLineQuotation] outLineQuotation BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost][outLineQuotation]  outLineQuotation Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询帖子
     */
    @RequestMapping(value = "/societyPostPage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String societyPostPage(@RequestBody String json){
        try {
            SocietyPostDTO societyPostDTO = getFormJSON(json, SocietyPostDTO.class);
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.ASC, "auditState"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "priv"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
            int pageNumber = 1;
            int pageSize = 10;
            if (societyPostDTO.getPageNumber() > 0) {
                pageNumber = societyPostDTO.getPageNumber();
            }
            if (societyPostDTO.getPageSize() > 0 && societyPostDTO.getPageSize() < Integer.valueOf(Constants.MAX_FETCHSIZE)) {
                pageSize = societyPostDTO.getPageSize();
            }
            PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, new Sort(orders));
            if(StringUtils.isNotBlank(societyPostDTO.getAuthor())){
                //通过作者查询信息
                //发送client请求用户系统，查询相关的用户id信息
                String server = "yg-user-service";
                String method = "user/listUserId";
                Map<String,String> map = new HashMap<>(16);
                map.put("name",societyPostDTO.getAuthor());
                ResultData<List<Long>> userIds = callServer(server,method,map);
                if(userIds.isSucceed()){
                    List<Long> userList = societyPostDTO.getUserIds();
                    if(userList == null){
                        userList = new ArrayList<>();
                    }
                    userList.addAll(userIds.getData());
                    societyPostDTO.setUserIds(userList);
                }
            }
            PageDTO<SocietyPostViewVO> result = societyPostService.societyPostSerachPage(societyPostDTO,pageRequest,String.valueOf(getUserIdFormToken(json)));
            return super.getResultJSONStr(true,result,null);

        } catch (BusinessException e) {
            log.error("[societyPost.societyPostPage] societyPostPage  BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost.societyPostPage]societyPostPage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 导出果有圈信息
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String exportExcel(@RequestBody String json) {
        try {
            SocietyPostDTO societyPostDTO = getParamJSON(json, SocietyPostDTO.class);
            if(StringUtils.isNotBlank(societyPostDTO.getAuthor())){
                //通过作者查询信息
                //发送client请求用户系统，查询相关的用户id信息
                String server = "yg-user-service";
                String method = "user/listUserId";
                Map<String,String> map = new HashMap<>(16);
                map.put("name",societyPostDTO.getAuthor());
                ResultData<List<Long>> userIds = callServer(server,method,map);
                if(userIds.isSucceed()){
                    List<Long> userList = societyPostDTO.getUserIds();
                    if(userList == null){
                        userList = new ArrayList<>();
                    }
                    userList.addAll(userIds.getData());
                    societyPostDTO.setUserIds(userList);
                }
            }
            String[] excelHeader = new String[] {"发布时间", "发布内容", "阅读数","点赞数", "评价数", "转发数","发布人"};
            String fileName = ExcelExportUtils.getExcelFileName("society");
            List<Object[]> results = societyPostService.selectSocietyInfoForExcel(societyPostDTO);
            String filePath = FileUtils.getFileRelativePath(excelPath);
            ByteArrayOutputStream os = ExcelExportUtils.exportExcel(excelHeader, results, false);
            byte[] b = os.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            ftpService.uploadFile(filePath, fileName, in);
            XlsVO xlsVO = new XlsVO();
            xlsVO.setFileName(fileName);
            xlsVO.setUrl(excelUrl +filePath + fileName);
            return super.getResultJSONStr(true, xlsVO, null);
        } catch (Exception e) {
            log.error("[user][exportExcel] Exception：{}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }

    }

    /**
     * 增加帖子阅读量
     */
    @RequestMapping(value = "/addSocietyPostViewCount", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addSocietyPostViewCount(@RequestBody String json){
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.addSocietyPostViewCount(societyPostVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPost.addSocietyPostViewCount] Add addSocietyPostViewCount  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPost.addSocietyPostViewCount] Add addSocietyPostViewCount Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 增加帖子分享数量
     */
    @RequestMapping(value = "/addSocietyPostTransCount", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String addSocietyPostTransCount(@RequestBody String json){
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.addSocietyPostTransCount(societyPostVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPost.addSocietyPostTransCount] Add addSocietyPostTransCount  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPost.addSocietyPostTransCount] Add addSocietyPostTransCount Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 点赞/取消点赞
     */
    @RequestMapping(value = "/likeSocietyPostOrNot", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String likeSocietyPostOrNot(@RequestBody String json){
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L || societyPostVO.getLikeSocietyPost() == null ;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.likeSocietyPostOrNot(societyPostVO, String.valueOf(getUserId(json)));
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }

        } catch (BusinessException e) {
            log.error("[societyPost.likeSocietyPostOrNot] likeSocietyPostOrNot  BusinessException", e.getErrorCode());
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }catch (Exception e) {
            log.error("[societyPost.likeSocietyPostOrNot] likeSocietyPostOrNot Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 查询帖子具体信息
     */
    @RequestMapping(value = "/findSocietyPostById", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String findSocietyPostById(@RequestBody String json){
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            SocietyPostViewVO societyPostById = societyPostService.findSocietyPostById(societyPostVO,String.valueOf(getUserIdFormToken(json)));
            return super.getResultJSONStr(true,societyPostById,null);

        } catch (BusinessException e) {
            log.error("[societyPost.findSocietyPostById] findSocietyPostById  BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost.findSocietyPostById]findSocietyPostById Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询帖子
     */
    @RequestMapping(value = "/mySocietyPostPage", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String mySocietyPostPage(@RequestBody String json){
        try {
            SocietyPostDTO societyPostDTO = getFormJSON(json, SocietyPostDTO.class);
            PageRequest pageRequest = genPageRequest(societyPostDTO);
            PageDTO<SocietyPostViewVO> result = societyPostService.mySocietyPostPage(societyPostDTO,pageRequest,String.valueOf(getUserId(json)));
            return super.getResultJSONStr(true,result,null);

        } catch (BusinessException e) {
            log.error("[societyPost.societyPostPage] societyPostPage  BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost.societyPostPage]societyPostPage Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 置顶/取消置顶
     */
    @RequestMapping(value = "/stickSocietyPost", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String stickSocietyPost(@RequestBody String json) {
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String result = societyPostService.stickSocietyPost(societyPostVO);
            if (StringUtils.equals(result, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true, null, null);
            }else{
                return super.getResultJSONStr(false, null, result);
            }

        }catch (Exception e) {
            log.error("[societyPost][stickSocietyPost] Exception {} ", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 修改信息
     */
    @RequestMapping(value = "/updateSocietyPostById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateSocietyPostById(@RequestBody String json) {
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.updateSocietyPostInfo(societyPostVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        } catch (BusinessException e) {
            log.error("[societyPost][updateSocietyPostById] updateSocietyPostById BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost][updateSocietyPostById]  updateSocietyPostById Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 删除信息
     */
    @RequestMapping(value = "/deleteSocietyPostById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteSocietyPostById(@RequestBody String json) {
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.deleteSocietyPostById(societyPostVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        } catch (BusinessException e) {
            log.error("[societyPost][deleteSocietyPostById] deleteSocietyPostById BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost][deleteSocietyPostById]  deleteSocietyPostById Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 文章审核
     */
    @RequestMapping(value = "/articleReview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String articleReview(@RequestBody String json) {
        try {
            SocietyPostVO societyPostVO = getFormJSON(json, SocietyPostVO.class);
            boolean flag = societyPostVO == null || societyPostVO.getId() == null || societyPostVO.getId() == 0L;
            if(flag){
                return super.getResultJSONStr(false,null,Constants.ERROR_100003);
            }
            String returnCode = societyPostService.articleReview(societyPostVO);
            if (StringUtils.equals(returnCode, Constants.RETURN_SUCESS)) {
                return super.getResultJSONStr(true,null,null);
            } else {
                return super.getResultJSONStr(false,null,returnCode);
            }
        }catch (BusinessException e) {
            log.error("[societyPost][articleReview] articleReview BusinessException", e);
            return super.getResultJSONStr(false,null,e.getErrorCode());
        }catch (Exception e) {
            log.error("[societyPost][articleReview]  articleReview Exception", e);
            return super.getResultJSONStr(false,null,Constants.SYS_ERROR);
        }
    }

    /**
     * 请求其他服务
     */
    private ResultData callServer(String server, String method, Map<String,String> reqMap){
        ResultData resultData;
        String url = Constants.HTTP_COLON + Constants.DOUBLE_SLASH + server + Constants.SINGLE_SLASH + method;
        try{
            String res = springCloudClient.post(url, BaseJsonUtil.toJSONString(reqMap));
            resultData = BaseJsonUtil.parseObject(res, ResultData.class);
        }catch (Exception e){
            log.error("调用后台服务异常 " + url, e);
            resultData = getResultData(false, null, Constants.SYS_ERROR);
        }
        return resultData;
    }

    /**
     * 获取栏目下所有的行情
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/newsBannerList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String newsBannerList(@RequestBody String jsonStr) {
        try {
            SocietyPostDTO societyPostDTO = getFormJSON(jsonStr,SocietyPostDTO.class);
            PageRequest pageRequest = genPageRequest(societyPostDTO);
            PageDTO<SocietyPostViewVO> newsList = societyPostService.queryNewsListByTopPic(societyPostDTO, pageRequest);
            return super.getResultJSONStr(true, newsList, null);
        } catch (Exception e) {
            log.error("[societyPost][newsBannerList]  get Article list top n Exception", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 生成行情竞猜信息（定时任务）
     * @return ResultData
     */
    @RequestMapping(value = "/newQuotation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String newQuotation() {
        try {
            String result = societyPostService.newQuotation();
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true, null, null);
            }
            return super.getResultJSONStr(false, null, result);
        } catch (Exception e) {
            log.error("[societyPost][newQuotation]  task exception {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 获取最新的行情竞猜信息
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/newsQuo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String newsQuo(@RequestBody String jsonStr) {
        try {
            QuotationView quotationView = societyPostService.newsQuo(String.valueOf(getUserIdFormToken(jsonStr)));
            return super.getResultJSONStr(true, quotationView, null);
        } catch (Exception e) {
            log.error("[societyPost][newsQuo]  Get the latest quizzes exception {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 判定结果
     * @param jsonStr 字符
     * @return ResultData
     */
    @RequestMapping(value = "/quotationReview", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String quotationReview(@RequestBody String jsonStr) {
        try {
            QuotationView quotationView = getFormJSON(jsonStr,QuotationView.class);
            if(quotationView == null || quotationView.getId() == null || quotationView.getId() == 0L){
                return super.getResultJSONStr(false, null, Constants.ERROR_100003);
            }
            String result = societyPostService.quotationReview(quotationView,String.valueOf(getUserId(jsonStr)));
            if(StringUtils.equals(Constants.RETURN_SUCESS,result)){
                return super.getResultJSONStr(true, null, null);
            }
            return super.getResultJSONStr(false, null, result);
        } catch (Exception e) {
            log.error("[societyPost][newsQuo]  Get the latest quizzes exception {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 获取最新的已出结果的行情竞猜信息
     */
    @RequestMapping(value = "/newsReviewQuo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String newsReviewQuo() {
        try {
            return super.getResultJSONStr(true, societyPostService.newsReviewQuo(), null);
        } catch (Exception e) {
            log.error("[societyPost][newsReviewQuo]  Get the latest quizzes exception {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 分页查询行情竞猜结果
     * @param json
     * @return
     */
    @RequestMapping(value = "/newsQuoPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String newsQuoPage(@RequestBody String json) {
        try {
            SocietyPostDTO societyPostDTO = getFormJSON(json, SocietyPostDTO.class);
            societyPostDTO.setRealm(Realm.QUOTATION);
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.DESC, "startTime"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
            int pageNumber = 1;
            int pageSize = 10;
            if (societyPostDTO.getPageNumber() > 0) {
                pageNumber = societyPostDTO.getPageNumber();
            }
            if (societyPostDTO.getPageSize() > 0 && societyPostDTO.getPageSize() < Integer.valueOf(Constants.MAX_FETCHSIZE)) {
                pageSize = societyPostDTO.getPageSize();
            }
            PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, new Sort(orders));
            return super.getResultJSONStr(true, societyPostService.newsQuoPage(societyPostDTO,pageRequest,String.valueOf(getUserId(json))), null);
        } catch (Exception e) {
            log.error("[societyPost][newsQuoPage]  Get the latest quizzes exception {}", e);
            return super.getResultJSONStr(false, null, Constants.SYS_ERROR);
        }
    }

    /**
     * 查询符合条件的用户信息
     */
    @RequestMapping(value = "/queryUserForRanking", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryUserForRanking(@RequestParam int num, @RequestParam String startTime , @RequestParam String endTime){
        try{
            List<Object[]> list = new ArrayList<>();
            List<SocietyPostAggreVO> result = societyPostService.queryUserForRanking(num,
                    DateTimeUtils.transferStrToDate(startTime), DateTimeUtils.transferStrToDate(endTime));
            result.forEach(vo->{
                if(vo.getUserId() != null && vo.getUserId() != 0L){
                    Object[] objects = new Object[2];
                    objects[0] = vo.getUserId();
                    objects[1] = vo.getTotalNum();
                    list.add(objects);
                }
            });
            return super.getResultJSONStr(true,list ,null);
        }catch (Exception e){
            log.error("[PrductController] [queryUserForRanking] Exception", e);
            return super.getResultJSONStr(false,null,null);
        }
    }

    /**
     * 查询符合条件的用户信息,给人评论的条件
     */
    @RequestMapping(value = "/queryUserForRankingForReply", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String queryUserForRankingForReply(@RequestParam int num, @RequestParam String startTime , @RequestParam String endTime){
        try{
            List<Object[]> list = new ArrayList<>();
            List<SocietyPostAggreVO> result = societyPostService.queryUserForRankingForReply(num,
                    DateTimeUtils.transferStrToDate(startTime), DateTimeUtils.transferStrToDate(endTime));
            result.forEach(vo->{
                if(vo.getUserId() != null && vo.getUserId() != 0L){
                    Object[] objects = new Object[2];
                    objects[0] = vo.getUserId();
                    objects[1] = vo.getTotalNum();
                    list.add(objects);
                }

            });
            return super.getResultJSONStr(true,list ,null);
        }catch (Exception e){
            log.error("[PrductController] [queryUserForRankingForReply] Exception", e);
            return super.getResultJSONStr(false,null,null);
        }
    }





}

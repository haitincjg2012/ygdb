package com.apec.framework.controller;

import com.apec.framework.base.BaseController;
import com.apec.framework.base.IJsonService;
import com.apec.framework.common.Constants;
import com.apec.framework.common.exception.ApecRuntimeException;
import com.apec.framework.common.exception.DispatchException;
import com.apec.framework.common.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 类 编 号：
 * 类 名 称：FileUploadController
 * 内容摘要：公海数据请求信息
 * 创建日期：2016/9/29
 * 编码作者：
 * @author xxx
 */
@RestController
public class FileUploadController extends BaseController
{

    @Autowired
    private IJsonService dispatchJSONService;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 请求图片上传
     * @param serverName 服务名称
     * @param methodName 调用方法名
     * @param request 请求信息
     * @return 请求返回结果
     */
    @RequestMapping(value = "/{serverName}/{methodName}/uploadPicture.apec", produces = "application/json;charset=UTF-8")
    public String imageUpload(@PathVariable("serverName") String serverName,
        @PathVariable("methodName") String methodName, HttpServletRequest request)
    {
        Map<String, MultipartFile> multipartFileMap = ((MultipartHttpServletRequest)request).getFileMap();

        try
        {
            fileUploadService.uploadImage( multipartFileMap, request );
        }
        catch (ApecRuntimeException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }

        String ret;
        try
        {
            ret = dispatchJSONService.service( serverName, methodName, request );
        }
        catch (DispatchException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }
        return ret;
    }

    @RequestMapping(value = "/{serverName}/{fileName}/{methodName}/uploadPicture.apec", produces = "application/json;charset=UTF-8")
    public String imageUpload(@PathVariable("serverName") String serverName,
        @PathVariable("fileName") String fileName,
        @PathVariable("methodName") String methodName, HttpServletRequest request)
    {
        Map<String, MultipartFile> multipartFileMap = ((MultipartHttpServletRequest)request).getFileMap();

        try
        {
            fileUploadService.uploadImage( multipartFileMap, request );
        }
        catch (ApecRuntimeException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }

        String ret;
        try
        {
            ret = dispatchJSONService.service( serverName, fileName + Constants.SINGLE_SLASH + methodName, request );
        }
        catch (DispatchException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }
        return ret;
    }

    /**
     * 二级目录
     * 上传文件到FTP
     * @param serverName 服务名称
     * @param methodName 方法名称
     * @param request 请求信息
     * @return 上传结果
     */
    @RequestMapping(value = "/{serverName}/{methodName}/uploadFile.apec", produces = "application/json;charset=UTF-8")
    public String uploadFile(@PathVariable("serverName") String serverName,
        @PathVariable("methodName") String methodName, HttpServletRequest request)
    {
        Map<String, MultipartFile> multipartFileMap = ((MultipartHttpServletRequest)request).getFileMap();

        try
        {
            fileUploadService.uploadFileByFTP( multipartFileMap, request );
        }
        catch (ApecRuntimeException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }

        String ret;
        try
        {
            ret = dispatchJSONService.service( serverName, methodName, request );
        }
        catch (DispatchException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }
        return ret;
    }

    /**
     * 三级目录
     * 上传文件到FTP
     * @param serverName 服务名称
     * @param fileName 文件名称
     * @param methodName 方法名称
     * @param request 请求信息
     * @return 上传结果
     */
    @RequestMapping(value = "/{serverName}/{fileName}/{methodName}/uploadFile.apec", produces = "application/json;charset=UTF-8")
    public String uploadFile(@PathVariable("serverName") String serverName,
        @PathVariable("fileName") String fileName,
        @PathVariable("methodName") String methodName, HttpServletRequest request)
    {
        Map<String, MultipartFile> multipartFileMap = ((MultipartHttpServletRequest)request).getFileMap();

        try
        {
            fileUploadService.uploadFileByFTP( multipartFileMap, request );
        }
        catch (ApecRuntimeException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }

        String ret;
        try
        {
            ret = dispatchJSONService.service( serverName, fileName + Constants.SINGLE_SLASH + methodName, request );
        }
        catch (DispatchException e)
        {
            return super.getResultJSONStr( false, "", e.getErrorCode() );
        }
        return ret;
    }

    /**
     * 通用上传图片
     * @return String
     */
    @RequestMapping(value = "/common/uploadImg.apec", produces = "application/json;charset=UTF-8")
    public String imageUpload(  HttpServletRequest request) {
        Map<String, MultipartFile> multipartFileMap = ((MultipartHttpServletRequest) request).getFileMap();

        try {
            fileUploadService.uploadImage(multipartFileMap, request);
        } catch (ApecRuntimeException e) {
            return super.getResultJSONStr(false, "", e.getErrorCode());
        }
        return super.getResultJSONStr(true,   request.getAttribute( Constants.IMAGE_ITEMS),null);
    }
}
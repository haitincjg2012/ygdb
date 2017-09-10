package com.apec.framework.common.service;

import com.apec.framework.common.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface FileUploadService
{

    /**
     * 处理图片上传
     * @param multipartFileMap 所有文件信息
     * @param request 请求信息
     */
    void uploadImage(Map<String, MultipartFile> multipartFileMap, HttpServletRequest request);

    /**
     * 上传文件到FTP
     * @param multipartFileMap 上传文件流
     * @param request 请求信息
     */
    void uploadFileByFTP(Map<String, MultipartFile> multipartFileMap, HttpServletRequest request);

    /**
     * 从ftp下载文件
     * @param ftpFileName ftp文件名称
     * @throws BusinessException 业务异常
     */
    byte[] download(String ftpFileName) throws BusinessException;
}
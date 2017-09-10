
package com.apec.framework.ftp.service;

import com.apec.framework.common.exception.BusinessException;

import java.io.InputStream;

/**
 * 基本描述：FtpService
 *
 */
public interface FtpService
{

    /**
     * 向FTP上傳文件
     * @param ftpFileName 文件名称
     * @param is 文件流
     * @throws BusinessException 业务异常
     */
    void uploadFile(String filePath, String ftpFileName, InputStream is) throws BusinessException;


    /**
     * 從FTP下載文件
     * @param ftpFileName 文件名称
     * @return 字节数组
     * @throws BusinessException 业务异常
     */
    byte[] download(String ftpFileName) throws BusinessException;

}

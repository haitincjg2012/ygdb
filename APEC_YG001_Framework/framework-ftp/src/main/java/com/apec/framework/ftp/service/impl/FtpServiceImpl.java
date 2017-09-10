package com.apec.framework.ftp.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.ftp.service.FtpService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基本描述：FtpServiceImpl FTP业务逻辑处理
 *
 */
@Service
public class FtpServiceImpl implements FtpService
{
    private static final Log log = LogFactory.getLog( FtpServiceImpl.class );

    private static final int DEFAULT_TIMEOUT = 20000;

    private static final int CONNECT_TIMEOUT = 50000;

    private static final int DATA_TIMEOUT = 100000;

    private static final String SLASH = "/";

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.workingDir}")
    private String workingDir;

    private FTPClient createNewConnect() throws BusinessException
    {
        FTPClient ftp = new FTPClient();

        ftp.setDefaultTimeout( DEFAULT_TIMEOUT );
        ftp.setConnectTimeout( CONNECT_TIMEOUT );
        ftp.setDataTimeout( DATA_TIMEOUT );
        ftp.setControlEncoding( Constants.SYSTEM_ENCODING );
        // 连接ftp
        try
        {
            ftp.connect( host, port );
        }
        catch (IOException e)
        {
            throw new BusinessException( ErrorCodeConst.ERROR_FTP_CONNECT_FAILD );
        }

        try
        {
            if(!ftp.login( username, password ))
            {
                disconnect( ftp );
                throw new BusinessException( ErrorCodeConst.ERROR_FTP_LOGIN_FAILD );
            }
        }
        catch (IOException e)
        {
            throw new BusinessException( ErrorCodeConst.ERROR_FTP_LOGIN_FAILD );
        }

        if(!FTPReply.isPositiveCompletion( ftp.getReplyCode() ))
        {
            disconnect( ftp );
            throw new BusinessException( ErrorCodeConst.ERROR_FTP_CONNECT_FAILD );
        }


        try
        {
            ftp.setFileType( FTP.BINARY_FILE_TYPE );
        }
        catch (IOException e)
        {
            throw new BusinessException( ErrorCodeConst.ERROR_FTP_FILETYPE_FAILD );
        }
        //设置工作目录
        try
        {
            if(!ftp.changeWorkingDirectory( workingDir ))
            {
                disconnect( ftp );
                throw new BusinessException( ErrorCodeConst.ERROR_FTP_WORKINGDIR_FAILD );
            }
        }
        catch (IOException e)
        {
            throw new BusinessException( ErrorCodeConst.ERROR_FTP_WORKINGDIR_FAILD );
        }
//        ftp.enterLocalPassiveMode(); //使用被动模式通过防火墙
        return ftp;
    }

    private void disconnect(FTPClient ftp)
    {
        if(ftp.isConnected())
        {
            try
            {
                ftp.logout();
                ftp.disconnect();
            }
            catch (IOException e)
            {
                log.error( "It occured error in excuting FtpServiceImpl.disconnect IOException case: " + e );
            }
        }
    }

    @Override
    public void uploadFile(String filePath, String ftpFileName, InputStream is) throws BusinessException
    {
        FTPClient ftpClient = createNewConnect();
        try
        {
            String[] folderList = filePath.split( SLASH );
            String currWkDir = workingDir;
            for(String folder : folderList)
            {
                if(StringUtils.isNotEmpty( folder ))
                {
                    currWkDir = currWkDir + SLASH + folder;
                    if(!ftpClient.changeWorkingDirectory( currWkDir ))
                    {
                        ftpClient.makeDirectory( folder );
                        ftpClient.changeWorkingDirectory( currWkDir );
                    }
                }
            }

            if(ftpClient.storeFile( ftpFileName, is ))
            {
                is.close();
                return;
            }
            log.info("================FTP Exception======================");
            log.info("====Reply Info:  "+ ftpClient.getReplyString());
            log.info("================FTP Exception end======================");
        }
        catch (Exception e)
        {
           log.error( "It occured error in excuting FtpServiceImpl.uploadFile IOException case: "  ,e );
           try{
               is.close();
           }catch (Exception ex){
               log.error("Can't close InputStream.");
               log.error( "It occured error in excuting FtpServiceImpl.uploadFile IOException case: "  , ex );
           }
        }
        throw new BusinessException( ErrorCodeConst.ERROR_FTP_UPLOAD_FAILD );
    }


    @Override
    public byte[] download(String ftpFileName) throws BusinessException
    {
        FTPClient ftpClient = createNewConnect();
        try
        {
            FTPFile[] fileInfoArray = ftpClient.listFiles( ftpFileName );//列出指定路径所有文件（包括文件夹）
            if(fileInfoArray != null && fileInfoArray.length > 0)
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                if(ftpClient.retrieveFile( ftpFileName, out ))
                {
                    return out.toByteArray();
                }
            }
        }
        catch (IOException e)
        {
            log.error( "It occured error in excuting FtpServiceImpl.download IOException case: " + e );
        }
        throw new BusinessException( ErrorCodeConst.ERROR_FTP_DOWNLOAD_FAILD );
    }
}

package com.apec.framework.common.service.impl;

import com.apec.framework.common.Constants;
import com.apec.framework.common.ErrorCodeConst;
import com.apec.framework.common.exception.BusinessException;
import com.apec.framework.common.service.FileUploadService;
import com.apec.framework.common.util.DateUtil;
import com.apec.framework.common.util.FileUtils;
import com.apec.framework.common.util.ImageUtils;
import com.apec.framework.common.util.UploadFTPUtils;
import com.apec.framework.dto.ImageUploadVO;
import com.apec.framework.ftp.service.FtpService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 类 编 号：
 * 类 名 称：FileUploadService
 * 内容摘要：文件上传业务逻辑处理
 * 创建日期：2016/10/10
 * 编码作者：
 */
@Service
public class FileUploadServiceImpl implements FileUploadService
{
    private static final Log log = LogFactory.getLog( FileUploadService.class );

    /**
     * 图片上传路径
     */
    @Value("${imageUploadPath}")
    private String imageUploadPath;

    /**
     * 可以上传图片的格式
     */
    @Value("${imageSuffix}")
    private String imageSuffix;

    //图片服务器
    @Value("${imageServerUrl}")
    private String imageServerUrl;

    /**
     * 缩略图规格数组
     */
    @Value("${thumbnailSizes}")
    private String[] thumbnailSizes;

    /**
     * 高度缩略图
     */
    @Value("${thumbnailSizesHeight}")
    private String[] thumbnailSizesHeight;

    @Autowired
    private FtpService ftpService;

    @Override
    public void uploadImage(Map<String, MultipartFile> multipartFileMap, HttpServletRequest request)
    {
        String userNo = (String)request.getAttribute( Constants.USER_NO );
        List<List<ImageUploadVO>> imageItems = new ArrayList<>();
        if(CollectionUtils.isNotEmpty( multipartFileMap.keySet() ))
        {
            for(Object key : multipartFileMap.keySet())
            {
                MultipartFile multipartFile = multipartFileMap.get( key );
                String imageName = multipartFile.getOriginalFilename();//获取图片的名字
                String suffix = StringUtils.substringAfterLast( imageName, "." );//获取图片后缀名
                String seq = DateUtil.formatDate( new Date(), "yyyyMMddHHmmssSSS" );//当前时间
                if(UploadFTPUtils.validateIsPicture( suffix, imageSuffix ) && StringUtils.isEmpty( suffix ))
                {
                    throw new BusinessException( ErrorCodeConst.ERROR_600002 );
                }

                String filePath = FileUtils.getFileRelativePath( imageUploadPath );
                //上传图片名称
                String upladImageName = userNo + seq + "." + suffix;
                try
                {
                    ftpService.uploadFile(filePath, upladImageName, multipartFile.getInputStream() );

                    List<ImageUploadVO> imageUploadVOs = new ArrayList<>();
                    BufferedImage bufferedImage = ImageIO.read( multipartFile.getInputStream() );
                    // 添加原图信息
                    ImageUploadVO srcimageUploadVO = new ImageUploadVO();
                    srcimageUploadVO.setImageName( upladImageName );
                    srcimageUploadVO.setImagePath( imageServerUrl + filePath + upladImageName );
                    imageUploadVOs.add( srcimageUploadVO );
                    // 循环添加缩略图信息
                    String compression = request.getParameter( "whetherCompression" );
                    // 循环添加缩略图信息
                    if(StringUtils.equals( "1", compression )) {
                        if (ArrayUtils.isNotEmpty(thumbnailSizes)) {
                            for (int i = 1; i <= thumbnailSizes.length; i++) {
                                //缩略图文件夹
                                String tinyPath =  i + "/";
                                //创建缩略图文件夹
//                            File tinyFile = UploadFTPUtils.createFolder( file.getAbsolutePath() + tinyPath );
                                //上传缩略图名称
                                String tinyUploadImageName = userNo + seq + "_" + i + "." + suffix;
                                String tinyUploadPath = FileUtils.getFileRelativePath(imageUploadPath) + i;
                                BufferedImage tinyImage = ImageUtils
                                        .compressImage(bufferedImage,suffix, Integer.parseInt(thumbnailSizes[i - 1]),Integer.parseInt(thumbnailSizesHeight[i - 1]));
                                InputStream is = UploadFTPUtils.getImageStream(tinyImage, suffix);

                                ftpService.uploadFile(tinyUploadPath, tinyUploadImageName, is);
                                ImageUploadVO tinyImageUploadVO = new ImageUploadVO();
                                tinyImageUploadVO.setImageName(tinyUploadImageName);
                                tinyImageUploadVO.setImagePath(imageServerUrl + filePath + tinyPath + tinyUploadImageName);
                                tinyImageUploadVO.setSizeLevel(i);
                                imageUploadVOs.add(tinyImageUploadVO);
                            }
                        }
                    }
                    imageItems.add( imageUploadVOs );
                }
                catch (Exception e)
                {
                    log.error(
                        "It occurred error in executing FileUploadServiceImpl imageUpload :" , e);
                    throw new BusinessException( ErrorCodeConst.ERROR_600003 );
                }
            }
            log.info( "images upload success" );
            request.setAttribute( Constants.IMAGE_ITEMS, imageItems );
        }
    }

    @Override
    public void uploadFileByFTP(Map<String, MultipartFile> multipartFileMap, HttpServletRequest request)
    {
        String userNo = (String)request.getAttribute( Constants.USER_NO );
        List<List<ImageUploadVO>> imageItems = new ArrayList();
        if(CollectionUtils.isNotEmpty( multipartFileMap.keySet() ))
        {
            for(Object key : multipartFileMap.keySet())
            {
                MultipartFile multipartFile = multipartFileMap.get( key );
                String fileName = multipartFile.getOriginalFilename();//获取文件的名字
                String fileSuffix = StringUtils.substringAfterLast( fileName, "." );//获取文件后缀名
                String seq = DateUtil.formatDate( new Date(), "yyyyMMddHHmmssSSS" );//当前时间
                //是否为上传图片名称
                if(StringUtils.isNotBlank( fileSuffix ) && !UploadFTPUtils.validateIsPicture( fileSuffix, imageSuffix ))
                {
                    List<ImageUploadVO> imageUploadVOs = new ArrayList<>();
                    String upladImageName = userNo + seq + "." + fileSuffix;
                    String uploadImagePath = FileUtils.getFileRelativePath( imageUploadPath );
                    try
                    {
                        ftpService.uploadFile( uploadImagePath, upladImageName, multipartFile.getInputStream() );

                        log.info( "upload file to FTP success!!!" );
                        // 添加原图信息
                        ImageUploadVO srcimageUploadVO = new ImageUploadVO();
                        srcimageUploadVO.setImageName( upladImageName );
                        srcimageUploadVO.setImagePath( imageServerUrl + uploadImagePath + upladImageName );
                        imageUploadVOs.add( srcimageUploadVO );
                        //是否压缩图片
                        String compression = request.getParameter( "whetherCompression" );
                        // 循环添加缩略图信息
                        if(StringUtils.equals( "1", compression ))
                        {
                            BufferedImage bufferedImage = ImageIO.read( multipartFile.getInputStream() );
                            if(ArrayUtils.isNotEmpty( thumbnailSizes ))
                            {
                                for(int i = 1; i <= thumbnailSizes.length; i++)
                                {
                                    //上传缩略图名称
                                    String tinyUploadImageName = userNo + seq + "_" + i + "." + fileSuffix;
                                    String tinyUploadPath = FileUtils.getFileRelativePath( imageUploadPath ) + i + "/";
                                    //压缩后的图片
                                    BufferedImage tinyImage = ImageUtils
                                        .compressImage( bufferedImage, fileSuffix, Integer
                                            .parseInt( thumbnailSizes[i - 1] ) ,Integer.parseInt(thumbnailSizesHeight[i - 1]));
                                    InputStream is = UploadFTPUtils.getImageStream( tinyImage, fileSuffix );
                                    ftpService.uploadFile( tinyUploadPath, tinyUploadImageName, is );
                                    log.info( "upload tiny file to FTP success!!!" );
                                    ImageUploadVO tinyImageUploadVO = new ImageUploadVO();
                                    tinyImageUploadVO.setImageName( tinyUploadImageName );
                                    tinyImageUploadVO.setImagePath( imageServerUrl + tinyUploadPath + tinyUploadImageName );
                                    tinyImageUploadVO.setSizeLevel( i );
                                    imageUploadVOs.add( tinyImageUploadVO );
                                }
                            }
                        }
                        imageItems.add( imageUploadVOs );
                    }
                    catch (IOException e)
                    {
                        log.error( "It occured error in excuting FileUploadServiceImpl.uploadFileByFTP case" , e );
                        throw new BusinessException( ErrorCodeConst.ERROR_FTP_UPLOAD_FAILD, e.getMessage() );
                    }
                }
            }
            request.setAttribute( Constants.IMAGE_ITEMS, imageItems );
        }

    }

    @Override
    public byte[] download(String ftpFileName) throws BusinessException
    {
        return new byte[0];
    }
}

package com.apec.framework.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Calendar;

/**
 * 类 编 号：
 * 类 名 称：处理文件傻昂出
 * 内容摘要：
 * 创建日期：2017/3/10
 * 编码作者：
 */
public abstract class UploadFTPUtils
{
    private static final Log log = LogFactory.getLog( UploadFTPUtils.class );

    private static final String SLASH = "/";

    /**
     * 验证是否为图片
     * @param fileExt 文件后缀名
     * @param fileSuffix 文件后缀名
     */
    public static boolean validateIsPicture(String fileExt, String fileSuffix)
    {
        int index = fileSuffix.indexOf( fileExt.toLowerCase() );
        return -1 == index;
    }

    /**
     * 创建文件夹
     * @param path 文件路径
     * @return 创建的文件夹
     */
    public static File createFolder(String path)
    {
        File file = new File( path );
        if(!file.exists())
        {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 根据文件类型创建文件夹
     * @param uploadPath 文件路径
     * @param fileType 文件类型
     * @return 创建的文件夹
     */
    public static File createFolder(String uploadPath, String fileType)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH ) + 1;
        int date = calendar.get( Calendar.DATE );

        String relativePath = fileType + File.separator + year + File.separator + month + File.separator + date;
        String createPath = uploadPath + relativePath;
        File file = new File( uploadPath );
        if(!file.exists())
        {
            file.mkdirs();

        }
        return file;
    }

    /**
     * 获取图片的相对路径
     * @param fileType 文件类型
     * @return 图片的相对路径
     */
    public static String getFileRelativePath(String fileType)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH ) + 1;
        int date = calendar.get( Calendar.DATE );
        return  fileType + SLASH + year + SLASH + month + SLASH + date + SLASH;
    }

    /**
     * bufferedImage转换InputStream
     * @param bufferedImage 图片对象
     * @param imgSuffix 后缀名
     * @return InputStream
     */
    public static InputStream getImageStream(BufferedImage bufferedImage, String imgSuffix)
    {
        InputStream is = null;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();

        ImageOutputStream imOut;
        try
        {
            imOut = ImageIO.createImageOutputStream( bs );
            ImageIO.write( bufferedImage, imgSuffix, imOut );
            is = new ByteArrayInputStream( bs.toByteArray() );
        }
        catch (IOException e)
        {
            log.error( "It occured error in excuting UploadFTPUtils.getImageStream case:" + e );
        }
        return is;
    }

}


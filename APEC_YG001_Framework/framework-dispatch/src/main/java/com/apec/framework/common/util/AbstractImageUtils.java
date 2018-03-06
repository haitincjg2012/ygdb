package com.apec.framework.common.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 静态图片工具处理类
 * @author  xxx
 */
public abstract class AbstractImageUtils
{
    /**
     * 按宽度，高度压缩
     * @param bufferImage 需要压缩的图片
     * @param width 压缩的宽度
     * @param height 压缩的高度
     * @return 压缩的图片
     */
    public static BufferedImage compressImage(BufferedImage bufferImage, int width, int height)
    {
        ImageScale imageScale = new ImageScale();
        return imageScale.imageZoomOut( bufferImage, width, height );

    }

    /**
     * 根据宽度进行比例压缩
     * @param bufferedImage 需要压缩的图片
     * @param suffix 图片的后缀名
     * @param newWidth 压缩的宽度
     * @return 压缩后的图片
     * @throws IOException I
     */
    public static BufferedImage compressImage(BufferedImage bufferedImage, String suffix, int newWidth)
        throws IOException
    {
        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        // 缩放的比例
        double scale = (double)originalWidth / (double)newWidth;
        int newHeight = (int)(originalHeight / scale);
        return zoomImageUtils( suffix, bufferedImage, newWidth, newHeight );
    }

    /**
     * 按照固定宽高缩放
     * @param bufferedImage bufferedImage
     * @param suffix suffix
     * @param newWidth newWidth
     * @param newHeight newHeight
     * @return BufferedImage
     * @throws IOException IOException
     */
    public static BufferedImage compressImage(BufferedImage bufferedImage, String suffix, int newWidth,int newHeight)
            throws IOException
    {
        return zoomImageUtils( suffix, bufferedImage, newWidth, newHeight );
    }


    /**
     * 生成图片的高度，然后宽度按照原始图的 高宽比 计算出新图片的宽度
     * @param suffix 图片的后缀名
     * @param bufferedImage 需要压缩的图片
     * @param width 压缩的宽度
     * @param height 压缩的高度
     * @return 压缩后的图片
     * @throws IOException IOException
     */
    private static BufferedImage zoomImageUtils(String suffix, BufferedImage bufferedImage, int width, int height)
        throws IOException
    {
        // 处理 png 背景变黑的问题
        boolean flag = suffix != null && (suffix.trim().toLowerCase().endsWith( "png" ) || suffix.trim().toLowerCase()
                .endsWith( "gif" ));
        if(flag)
        {
            BufferedImage to = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage( width, height, Transparency.TRANSLUCENT );
            g2d.dispose();
            g2d = to.createGraphics();
            Image from = bufferedImage.getScaledInstance( width, height, Image.SCALE_AREA_AVERAGING );
            g2d.drawImage( from, 0, 0, null );
            g2d.dispose();
            return to;
        }
        else
        {
            BufferedImage newImage = new BufferedImage( width, height, bufferedImage.getType() );
            Graphics g = newImage.getGraphics();
            g.drawImage( bufferedImage, 0, 0, width, height, null );
            g.dispose();
            return newImage;
        }
    }
}

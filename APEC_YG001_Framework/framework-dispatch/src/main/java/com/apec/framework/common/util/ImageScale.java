package com.apec.framework.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.image.BufferedImage;

/**
 * @author xx
 */
public class ImageScale
{
    private static final Log log = LogFactory.getLog( ImageScale.class );

    private static final int LENGTH = 255;

    private Integer width;

    private Integer height;

    private Integer scaleWidth;

    private Double support = (double)3.0;

    private Double pi = (double)3.14159265358978;

    private double[] contrib;

    private double[] normContrib;

    private double[] tmpContrib;

    private Integer nDots;

    private Integer nHalfDots;

    public BufferedImage imageZoomOut(BufferedImage srcBufferImage, int w, int h)
    {
        width = srcBufferImage.getWidth();
        height = srcBufferImage.getHeight();
        scaleWidth = w;

        if(determineResultSize( w, h ) == 1)
        {
            return srcBufferImage;
        }
        calContrib();
        BufferedImage pbOut = horizontalFiltering( srcBufferImage, w );
        return verticalFiltering( pbOut, h );
    }

    /**
     * 决定图像尺寸
     */
    private int determineResultSize(int w, int h)
    {
        double scaleH, scaleV;
        scaleH = (double)w / (double)width;
        scaleV = (double)h / (double)height;
        // 需要判断一下scaleH，scaleV，不做放大操作
        if(scaleH >= 1.0 && scaleV >= 1.0)
        {
            return 1;
        }
        return 0;

    }

    private double lanczos(int i, int inWidth, int outWidth, double support)
    {
        double x;

        x = (double)i * (double)outWidth / (double)inWidth;

        return Math.sin( x * pi ) / (x * pi) * Math.sin( x * pi / support )
               / (x * pi / support);

    } // end of Lanczos()

    private void calContrib()
    {
        nHalfDots = (int)((double)width * support / (double)scaleWidth);
        nDots = nHalfDots * 2 + 1;
        try
        {
            contrib = new double[nDots];
            normContrib = new double[nDots];
            tmpContrib = new double[nDots];
        }
        catch (Exception e)
        {
            log.error( "init contrib,normContrib,tmpContrib" + e );
        }

        int center = nHalfDots;
        contrib[center] = 1.0;

        double weight = 0.0;
        int i;
        for(i = 1; i <= center; i++)
        {
            contrib[center + i] = lanczos( i, width, scaleWidth, support );
            weight += contrib[center + i];
        }

        for(i = center - 1; i >= 0; i--)
        {
            contrib[i] = contrib[center * 2 - i];
        }

        weight = weight * 2 + 1.0;

        for(i = 0; i <= center; i++)
        {
            normContrib[i] = contrib[i] / weight;
        }

        for(i = center + 1; i < nDots; i++)
        {
            normContrib[i] = normContrib[center * 2 - i];
        }
    } // end of CalContrib()

    /**
     * // 处理边缘
     * @param start start
     * @param stop stop
     */
    private void calTempContrib(int start, int stop)
    {
        double weight = 0;

        int i;
        for(i = start; i <= stop; i++)
        {
            weight += contrib[i];
        }

        for(i = start; i <= stop; i++)
        {
            tmpContrib[i] = contrib[i] / weight;
        }

    } // end of CalTempContrib()

    private int getRedValue(int rgbValue)
    {
        int temp = rgbValue & 0x00ff0000;
        return temp >> 16;
    }

    private int getGreenValue(int rgbValue)
    {
        int temp = rgbValue & 0x0000ff00;
        return temp >> 8;
    }

    private int getBlueValue(int rgbValue)
    {
        return rgbValue & 0x000000ff;
    }

    private int comRGB(int redValue, int greenValue, int blueValue)
    {

        return (redValue << 16) + (greenValue << 8) + blueValue;
    }

    /**
     * // 行水平滤波
     * @param bufImg bufImg
     * @param startX startX
     * @param stopX stopX
     * @param start start
     * @param stop stop
     * @param y y
     * @param pContrib pContrib
     * @return int
     */
    private int horizontalFilter(BufferedImage bufImg, int startX, int stopX,
        int start, int stop, int y, double[] pContrib)
    {
        double valueRed = 0.0;
        double valueGreen = 0.0;
        double valueBlue = 0.0;
        int valueRGB;
        int i, j;

        for(i = startX, j = start; i <= stopX; i++, j++)
        {
            valueRGB = bufImg.getRGB( i, y );

            valueRed += getRedValue( valueRGB ) * pContrib[j];
            valueGreen += getGreenValue( valueRGB ) * pContrib[j];
            valueBlue += getBlueValue( valueRGB ) * pContrib[j];
        }

        valueRGB = comRGB( clip( (int)valueRed ), clip( (int)valueGreen ),
                clip( (int)valueBlue ) );
        return valueRGB;

    }

    /**
     * // 图片水平滤波
     * @param bufImage bufImage
     * @param iOutW OutW
     * @return BufferedImage
     */
    private BufferedImage horizontalFiltering(BufferedImage bufImage, int iOutW)
    {
        int dwInW = bufImage.getWidth();
        int dwInH = bufImage.getHeight();
        int value;
        BufferedImage pbOut = new BufferedImage( iOutW, dwInH,
                                                 BufferedImage.TYPE_INT_RGB );

        for(int x = 0; x < iOutW; x++)
        {

            int startX;
            int start;
            int xxX = (int)(((double)x) * ((double)dwInW) / ((double)iOutW) + 0.5);
            int y;

            startX = xxX- nHalfDots;
            if(startX < 0)
            {
                startX = 0;
                start = nHalfDots - xxX;
            }
            else
            {
                start = 0;
            }

            int stop;
            int stopX = xxX + nHalfDots;
            if(stopX > (dwInW - 1))
            {
                stopX = dwInW - 1;
                stop = nHalfDots + (dwInW - 1 - xxX);
            }
            else
            {
                stop = nHalfDots * 2;
            }

            if(start > 0 || stop < nDots - 1)
            {
                calTempContrib( start, stop );
                for(y = 0; y < dwInH; y++)
                {
                    value = horizontalFilter( bufImage, startX, stopX, start,
                                              stop, y, tmpContrib );
                    pbOut.setRGB( x, y, value );
                }
            }
            else
            {
                for(y = 0; y < dwInH; y++)
                {
                    value = horizontalFilter( bufImage, startX, stopX, start,
                                              stop, y, normContrib );
                    pbOut.setRGB( x, y, value );
                }
            }
        }

        return pbOut;

    }

    private int verticalFilter(BufferedImage pbInImage, int startY, int stopY,
        int start, int stop, int x, double[] pContrib)
    {
        double valueRed = 0.0d;
        double valueGreen = 0.0d;
        double valueBlue = 0.0d;
        int valueRGB;
        int i, j;

        for(i = startY, j = start; i <= stopY; i++, j++)
        {
            valueRGB = pbInImage.getRGB( x, i );

            valueRed += getRedValue( valueRGB ) * pContrib[j];
            valueGreen += getGreenValue( valueRGB ) * pContrib[j];
            valueBlue += getBlueValue( valueRGB ) * pContrib[j];
        }

        valueRGB = comRGB( clip( (int)valueRed ), clip( (int)valueGreen ),
                clip( (int)valueBlue ) );
        return valueRGB;

    }

    private BufferedImage verticalFiltering(BufferedImage pbImage, int iOutH)
    {
        int iW = pbImage.getWidth();
        int iH = pbImage.getHeight();
        int value;
        BufferedImage pbOut = new BufferedImage( iW, iOutH,
                                                 BufferedImage.TYPE_INT_RGB );

        for(int y = 0; y < iOutH; y++)
        {

            int startY;
            int start;
            int xxY = (int)(((double)y) * ((double)iH) / ((double)iOutH) + 0.5);

            startY = xxY - nHalfDots;
            if(startY < 0)
            {
                startY = 0;
                start = nHalfDots - xxY;
            }
            else
            {
                start = 0;
            }

            int stop;
            int stopY = xxY + nHalfDots;
            if(stopY > (iH - 1))
            {
                stopY = iH - 1;
                stop = nHalfDots + (iH - 1 - xxY);
            }
            else
            {
                stop = nHalfDots * 2;
            }

            if(start > 0 || stop < nDots - 1)
            {
                calTempContrib( start, stop );
                for(int x = 0; x < iW; x++)
                {
                    value = verticalFilter( pbImage, startY, stopY, start, stop,
                                            x, tmpContrib );
                    pbOut.setRGB( x, y, value );
                }
            }
            else
            {
                for(int x = 0; x < iW; x++)
                {
                    value = verticalFilter( pbImage, startY, stopY, start, stop,
                                            x, normContrib );
                    pbOut.setRGB( x, y, value );
                }
            }

        }

        return pbOut;

    }

    private int clip(int x)
    {
        if(x < 0){
            return 0;
        }
        if(x > LENGTH){
            return LENGTH;
        }
        return x;
    }

}

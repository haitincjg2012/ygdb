package com.apec.framework.dto;

/**
 * 类 编 号：
 * 类 名 称：ImageUploadVO
 * 内容摘要：文件上传DTO
 * 创建日期：2016/10/18
 * 编码作者：
 */
public class ImageUploadVO
{
    /**
     * 图片名称
     */
    private String imageName;

    /**
     * 图片路径
     */
    private String imagePath;

    /**
     * 图片尺寸
     */
    private int sizeLevel;

    /**
     * 获取 图片名称
     */
    public String getImageName()
    {
        return this.imageName;
    }

    /**
     * 设置 图片名称
     */
    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    /**
     * 获取 图片路径
     */
    public String getImagePath()
    {
        return this.imagePath;
    }

    /**
     * 设置 图片路径
     */
    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    /**
     * 获取 图片尺寸
     */
    public int getSizeLevel()
    {
        return this.sizeLevel;
    }

    /**
     * 设置 图片尺寸
     */
    public void setSizeLevel(int sizeLevel)
    {
        this.sizeLevel = sizeLevel;
    }
}

package com.apec.framework.dto;

/**
 * @author xxx
 */
public class MenuVO
{
    private Long id;
    
    private String menuNo;

    private String menuName;

    private String menuLinkUrl;

    private String upMenuNo;
    
    private String sortPos;
    
    private String menuImgUrl;
    
    private String menuLevel;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getMenuNo()
    {
        return menuNo;
    }

    public void setMenuNo(String menuNo)
    {
        this.menuNo = menuNo;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuLinkUrl()
    {
        return menuLinkUrl;
    }

    public void setMenuLinkUrl(String menuLinkUrl)
    {
        this.menuLinkUrl = menuLinkUrl;
    }

    public String getUpMenuNo()
    {
        return upMenuNo;
    }

    public void setUpMenuNo(String upMenuNo)
    {
        this.upMenuNo = upMenuNo;
    }

    public String getSortPos()
    {
        return sortPos;
    }

    public void setSortpos(String sortPos)
    {
        this.sortPos = sortPos;
    }

    public String getMenuImgUrl()
    {
        return menuImgUrl;
    }

    public void setMenuImgUrl(String menuImgUrl)
    {
        this.menuImgUrl = menuImgUrl;
    }

    public String getMenuLevel()
    {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel)
    {
        this.menuLevel = menuLevel;
    }

    @Override
    public String toString(){
        return super.toString();
    }
    
}

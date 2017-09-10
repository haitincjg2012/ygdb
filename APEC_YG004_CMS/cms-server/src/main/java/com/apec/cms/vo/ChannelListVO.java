package com.apec.cms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Title:
 *
 * @author yirde  2017/7/28.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelListVO {

    /**
     * 栏目Key
     */
    private String channelKey;

    /**
     * 栏目
     */
    private String channelName;

    /**
     * 栏目
     */
    private List<ChannelVO> list ;

}

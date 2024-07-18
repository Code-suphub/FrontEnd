package com.li.entity.enumE;

import java.util.HashMap;
import java.util.Map;

public class ValueMapping {
    // 充值月份映射
    public final static Map<Integer,Integer> VIP_MAP = Map.of(
            2,1,
            3,6,
            4,12);

    public final static Map<String,String> ACTION_2_KEY = Map.of(
            "article_like","article_like_key"
            ,"article_hits","article_hits_key"
            ,"comment_like","comment_like_key"
            ,"comment_browse","comment_browse_key"
            ,"square_like","square_like_key"
            ,"square_browse","square_browse_key"
            ,"square_comment_like","square_comment_like_key"
            ,"square_comment_browse","square_comment_browse_key"
            ,"square_class_like","square_class_like_key"
    );
}

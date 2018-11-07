package com.winnie.videoplayer;

/**
 * @author : winnie
 * @date : 2018/11/7
 * @desc
 */
public class StringUtils {
    public static int parseInt(String s){
        try {
            return Integer.parseInt(s);
        }catch (NumberFormatException e){
            return 0;
        }
    }
}

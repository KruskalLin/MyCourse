package mycourse.utils;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/3/6
 * @Todo:
 */
public class RandomUtils {

    public static int generateRondom() {
        return (int)((Math.random()*9+1)*100000);
    }
}

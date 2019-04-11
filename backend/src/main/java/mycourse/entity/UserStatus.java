package mycourse.entity;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/4
 * @Todo:
 */
public enum UserStatus {

    INACTIVATE,
    /**
     * @Description: Once user is created, this state would be attached.
     * @author Popping Lim
     * @date 2019/2/4
     */
    CREATE,

    /**
     * @Description: Once user is cancelled, this state would be attached.
     * @author Popping Lim
     * @date 2019/2/4
     */
    CANCEL;
}

package com.pengsoft.support.aspect;

/**
 * The constants of join points.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public class JoinPoints {

    public static final String ALL_SERVICE = "execution(public * com.pengsoft..service.*Service.*(..))";

    public static final String ALL_FACADE = "execution(public * com.pengsoft..facade.*Facade.*(..))";

    public static final String ALL_API = "execution(public * com.pengsoft..api.*Api.*(..))";

    private JoinPoints() {
    }

}

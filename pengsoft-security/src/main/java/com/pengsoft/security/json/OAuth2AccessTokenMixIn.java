package com.pengsoft.security.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * To replace the original JSON serializer.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@JsonSerialize(using = OAuth2AccessTokenSerializer.class)
public class OAuth2AccessTokenMixIn {

}

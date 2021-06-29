package com.pengsoft.system.json;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pengsoft.system.domain.Region;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This wrapper just for JSON serialization.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@JsonSerialize(using = RegionWrapperJsonSerializer.class)
@Getter
@RequiredArgsConstructor
public class RegionWrapper implements Serializable {

    private static final long serialVersionUID = 1105803452474874888L;
    private final Region region;

}

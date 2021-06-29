package com.pengsoft.system.service;

import com.pengsoft.support.service.EntityServiceImpl;
import com.pengsoft.system.domain.Asset;
import com.pengsoft.system.repository.AssetRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * The implementer of {@link AssetService} based on JPA.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Primary
@Service
public class AssetServiceImpl extends EntityServiceImpl<AssetRepository, Asset, String> implements AssetService {

}

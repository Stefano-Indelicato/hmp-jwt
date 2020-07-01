package com.hmp.jwt.configuration.manager;

import com.hmp.jwt.dao.SettingDAO;
import com.hmp.jwt.entity.Setting;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class GlobalConfigurationManager {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Inject
    SettingDAO settingDAO;

    private static final Long KEY = 1L;

    private final ConcurrentMap<Long, Setting> cache = new ConcurrentHashMap<Long, Setting>();

    void init(@Observes StartupEvent ev)  {
        initializeCache();
    }

    /**
     * Initializes configurations and checks mandatory properties
     */
    public void initializeCache() {
        LOG.info("=============================================");
        LOG.info("Initializing global configuration cache");
        cache.clear();
        try {
            Setting setting = settingDAO.getSetting();
            cache.put(KEY, setting);
            checkMandatoryProperties();
        } catch (Exception e) {
            LOG.error("No Setting Found", e);
        }

        LOG.info("Global configuration cache initialized with {} values", cache.size());
        LOG.info("=============================================");
    }

    private void checkMandatoryProperties() {
        List<String> missingProperties = new ArrayList<String>();

        // TODO check parameters actually needed, not just the emptiness
        if (cache.isEmpty()) {
            missingProperties.add("DB CONFIGURATION PARAMETERS");
        }

        if(missingProperties.size() > 0) {
            throw new RuntimeException("APPLICATION CANNOT BE INITIALIZED, MISSING: " + missingProperties.toString());
        }
    }

    public Setting getGlobalConfigByGroupAndKey() {
        return cache.get(KEY);
    }

    public String getSecretMDM() {
        return cache.get(KEY).getSecretMDM();
    }

}

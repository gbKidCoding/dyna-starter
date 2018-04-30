package com.github.yuebo.elfinder;

import com.github.yuebo.dyna.config.QiniuApiConfig;
import com.github.yuebo.dyna.config.QiniuConfigProperties;
import com.github.yuebo.elfinder.controller.ConnectorController;
import com.github.yuebo.elfinder.controller.executor.CommandExecutorFactory;
import com.github.yuebo.elfinder.controller.executor.DefaultCommandExecutorFactory;
import com.github.yuebo.elfinder.controller.executors.MissingCommandExecutor;
import com.github.yuebo.elfinder.impl.*;
import com.github.yuebo.elfinder.localfs.LocalFsVolume;
import com.github.yuebo.elfinder.service.FsSecurityChecker;
import com.github.yuebo.elfinder.service.FsService;
import com.github.yuebo.elfinder.service.FsServiceFactory;
import com.github.yuebo.elfinder.service.FsVolume;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

@Configuration
@ConditionalOnClass(ConnectorController.class)
public class ELfinderConfiguration {
    @Autowired
    ELfinderConfigProperties eLfinderConfigProperties;

    @Bean
    MissingCommandExecutor missingCommandExecutor(){
        return new MissingCommandExecutor();
    }

    @Bean
    CommandExecutorFactory commandExecutorFactory(@Autowired MissingCommandExecutor missingCommandExecutor){
        DefaultCommandExecutorFactory defaultCommandExecutorFactory=new DefaultCommandExecutorFactory();
        defaultCommandExecutorFactory.setClassNamePattern("com.github.yuebo.elfinder.controller.executors.%sCommandExecutor");
        defaultCommandExecutorFactory.setFallbackCommand(missingCommandExecutor);
        defaultCommandExecutorFactory.setMap(Collections.emptyMap());
        return defaultCommandExecutorFactory;
    }

    @Bean
    @ConditionalOnProperty(prefix = "elfinder",value = "provider",havingValue = "local",matchIfMissing = true)
    FsVolume localFsVolume(){
        LocalFsVolume localFsVolume=new LocalFsVolume();
        localFsVolume.setName("MyFiles");
        localFsVolume.setRootDir(new File(eLfinderConfigProperties.getRootPath()));
        return localFsVolume;
    }

    @Bean
    FsSecurityCheckFilterMapping fsSecurityCheckFilterMapping(@Autowired FsSecurityChecker fsSecurityChecker){
        FsSecurityCheckFilterMapping fsSecurityCheckFilterMapping=new FsSecurityCheckFilterMapping();
        fsSecurityCheckFilterMapping.setPattern("A_*");
        fsSecurityCheckFilterMapping.setChecker(fsSecurityChecker);
        return fsSecurityCheckFilterMapping;
    }
    @Bean FsSecurityChecker fsSecurityChecker(){
        FsSecurityCheckForAll fsSecurityCheckForAll=new FsSecurityCheckForAll();
        fsSecurityCheckForAll.setReadable(true);
        fsSecurityCheckForAll.setWritable(true);
        return fsSecurityCheckForAll;
    }
    @Bean
    QiniuApiConfig qiniuApiConfig(@Autowired QiniuConfigProperties qiniuConfigProperties){
        QiniuApiConfig apiConfig=new QiniuApiConfig();
        BeanUtils.copyProperties(qiniuConfigProperties,apiConfig);
        return apiConfig;
    }

    @Bean
    FsServiceFactory fsServiceFactory(@Autowired FsService fsService){
        StaticFsServiceFactory staticFsServiceFactory =new StaticFsServiceFactory();
        staticFsServiceFactory.setFsService(fsService);
        return staticFsServiceFactory;
    }

    @Bean FsService fsService(@Autowired final FsVolume fsVolume){
        DefaultFsService defaultFsService=new DefaultFsService();
        defaultFsService.setSecurityChecker(fsSecurityChecker());
        DefaultFsServiceConfig defaultFsServiceConfig=new DefaultFsServiceConfig();
        defaultFsServiceConfig.setTmbWidth(80);
        defaultFsService.setServiceConfig(defaultFsServiceConfig);
        defaultFsService.setVolumeMap(new HashMap(){{
            this.put("A",fsVolume);
        }});
        return defaultFsService;
    }

}

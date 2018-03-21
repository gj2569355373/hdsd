package com.zchd.hdsd.business.component;


import com.zchd.hdsd.business.model.ActivityModule;
import com.zchd.hdsd.business.model.AppModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by GJ on 16/10/31.
 * //@Singletion代表各个注入对象为单例
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    ActivityComponent plus(ActivityModule activityModule);
}

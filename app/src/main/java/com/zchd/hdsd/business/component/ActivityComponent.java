package com.zchd.hdsd.business.component;

import com.zchd.hdsd.ActivityScope;
import com.zchd.hdsd.business.model.ActivityModule;

import dagger.Subcomponent;


/**
 * Created by GJ on 2016/10/31.
 * 新建Component桥梁。他是一个子Component,依赖于一个全局的父Component。
 * Subcomponent注解与Component依赖另一个Component有点像，他们区别在于：
 * Subcomponent可以获取到父Component的所有可以产生出的对象。
 * Component依赖则只能获取到被依赖的Component所暴露出来的可以生产的对象
 */
@ActivityScope
@Subcomponent (modules = ActivityModule.class)
public interface ActivityComponent {
//    MainComponent plus(MainModel mainModel);
//    WebC plus(WebM webM);
//    WeblistComponent plus(WeblistModel weblistModel);
//    LoginComponent plus(LoginModule loginModule);
//    RegisterConmponent plus(RegisterModule registerModule);
//    TopupComponent plus(TopupModel topupModel);
//    ImportMailListComponent plus(ImportMailListModel importMailListModel);
//    MessageComponent plus(MessageModel messageModel);
//    TopupTooComponent plus(TopupTooModule topupTooModule);
//    CommissioComponent plus(CommissioModel commissioModel);
//    DownloadComponent plus(DownloadModel downloadModel);
//    AllapplicationComponent plus(AllapplicationModel allapplicationModel);
//    LocalClassComponent plus(LocalClassificationModel localClassificationModel);
//    IpMonitorComponent plus(IpMonitorModel currencyModel);
//    AddReturnVisitComponent plus(AddReturnVisitRecordModel addReturnVisitRecordModel);
//    AddVisitRecordComponent plus(AddVisitRecordModel addVisitRecordModel);
//    ImportMailListComponent plus(ImportMailListModel importMailListModel);
//    FuzzyQueryComponent plus(FuzzyQueryModel fuzzyQueryModel);
//    SetComponent plus(SetModel setModel);
//    PersonalInformationComponent plus(PersonalInformationModel personalInformationModel);
}

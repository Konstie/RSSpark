package com.app.rsspark.injection.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by konstie on 10.12.16.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ForApplication {}

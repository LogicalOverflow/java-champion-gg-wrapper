package com.lvack.championggwrapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PageNumberClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PageNumber {
}

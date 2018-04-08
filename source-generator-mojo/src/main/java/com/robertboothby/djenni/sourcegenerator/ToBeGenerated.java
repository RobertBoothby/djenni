package com.robertboothby.djenni.sourcegenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation may be applied in new code bases to classes that are to have Generators and GeneratorBuilders created
 * as part of source code generation. This annotation is primarily intended to be used in conjunction with the Source
 * Generator maven plugin.
 * <p>&#169; 2013 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ToBeGenerated {
    boolean generate() default true;
}

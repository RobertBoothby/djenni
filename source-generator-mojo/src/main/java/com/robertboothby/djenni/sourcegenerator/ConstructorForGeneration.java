package com.robertboothby.djenni.sourcegenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to indicate which constructor should be used by the source code generator in the
 * Generator and GeneratorBuilder source files. This annotation should only be applied to <em>one</em> constructor
 * The behaviour if applied to more than one is undefined...</p>
 *
 * @author robertboothby
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.CONSTRUCTOR)
public @interface ConstructorForGeneration {
}

<#if javaSource.packageName?has_content>package ${javaSource.packageName};

</#if>
import com.robertboothby.djenni.core.StreamableSupplier;
import com.robertboothby.djenni.SupplierBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.robertboothby.djenni.core.SupplierHelper.fix;
import static java.util.Collections.emptyList;

/**
 * <p>
 * Concrete implementations of this class must be created that provide reasonable default Suppliers and provide the
 * builder methods for overriding them.
 * </p>
 * <p>
 *    <em>
 *        This class has been autogenerated by the Djenni code generation framework and as such should never be checked
 *        in or edited.
 *    </em>
 * </p>
 */
public class ${javaClass.name}SupplierBuilder implements SupplierBuilder<${javaClass.name}> {
<#if constructor.parameters?has_content>

    //Constructor parameter suppliers.
<#list constructor.parameters as constructorParameter>
    protected Supplier<${constructorParameter.type.genericFullyQualifiedName}> ${constructorParameter.name}Constructor = fix(null);
</#list>
</#if>
<#if setterMethods?has_content>

    //Setter method suppliers.
<#list setterMethods as setterMethod>
    protected Supplier<${setterMethod.propertyType.genericFullyQualifiedName}> ${setterMethod.propertyName}Setter = fix(null);
</#list>
</#if>
<#if collectionGetters?has_content>

    //Collection getter method suppliers.
<#list collectionGetters as collectionGetter>
    protected Supplier<${collectionGetter.propertyType.genericFullyQualifiedName}> ${collectionGetter.propertyName}Collection = fix(emptyList());
</#list>
</#if>

    public StreamableSupplier<${javaClass.name}> build() {
        return new ${javaClass.name}Supplier(
<#if constructor.parameters?has_content>
<#list constructor.parameters as constructorParameter>
                ${constructorParameter.name}Constructor<#sep>,
</#list>
<#if setterMethods?has_content || collectionGetters?has_content>,</#if>
</#if>
<#if setterMethods?has_content>
<#list setterMethods as setterMethod>
                ${setterMethod.propertyName}Setter<#sep>,
</#list>
<#if collectionGetters?has_content>,</#if>
</#if>
<#list collectionGetters as collectionGetter>
                ${collectionGetter.propertyName}Collection<#sep>,
</#list>

        );
    }
<#list constructor.parameters as constructorParameter>

    public ${javaClass.name}SupplierBuilder with${constructorParameter.name?cap_first}Constructor(Supplier<${constructorParameter.type.genericFullyQualifiedName}> supplier) {
        this.${constructorParameter.name}Constructor = supplier;
        return this;
    }

    public ${javaClass.name}SupplierBuilder with${constructorParameter.name?cap_first}Constructor(SupplierBuilder<${constructorParameter.type.genericFullyQualifiedName}> builder) {
        this.${constructorParameter.name}Constructor = builder.build();
        return this;
    }
</#list>
<#list setterMethods as setterMethod>

    public ${javaClass.name}SupplierBuilder with${setterMethod.propertyName?cap_first}Setter(Supplier<${setterMethod.propertyType.genericFullyQualifiedName}> supplier) {
        this.${setterMethod.propertyName}Setter = supplier;
        return this;
    }

    public ${javaClass.name}SupplierBuilder with${setterMethod.propertyName?cap_first}Setter(SupplierBuilder<${setterMethod.propertyType.genericFullyQualifiedName}> builder) {
        this.${setterMethod.propertyName}Setter = builder.build();
        return this;
    }
</#list>
<#list collectionGetters as collectionGetter>

    public ${javaClass.name}SupplierBuilder with${collectionGetter.propertyName?cap_first}Collection(Supplier<${collectionGetter.propertyType.genericFullyQualifiedName}> supplier) {
        this.${collectionGetter.propertyName}Collection = supplier;
        return this;
    }

    public ${javaClass.name}SupplierBuilder with${collectionGetter.propertyName?cap_first}Collection(SupplierBuilder<${collectionGetter.propertyType.genericFullyQualifiedName}> builder) {
        this.${collectionGetter.propertyName}Collection = builder.build();
        return this;
    }
</#list>

    public static Supplier<${javaClass.name}> ${javaClass.name?uncap_first}Supplier(Consumer<${javaClass.name}SupplierBuilder> configuration) {
        ${javaClass.name}SupplierBuilder builder = new ${javaClass.name}SupplierBuilder();
        configuration.accept(builder);
        return builder.build();
    }

    public static ${javaClass.name}SupplierBuilder ${javaClass.name?uncap_first}Supplier() {
        return new ${javaClass.name}SupplierBuilder();
    }
}
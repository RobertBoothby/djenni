<#if javaSource.packageName?has_content>package ${javaSource.packageName};

</#if>
import com.robertboothby.djenni.Generator;
import com.robertboothby.djenni.GeneratorBuilder;
import java.util.function.Consumer;


import static com.robertboothby.djenni.core.GeneratorHelper.$;
import static java.util.Collections.emptyList;

/**
 * <p>
 * Concrete implementations of this class must be created that provide reasonable default Generators and provide the
 * builder methods for overriding them.
 * </p>
 * <p>
 *    <em>
 *        This class has been autogenerated by the Djenni code generation framework and as such should never be checked
 *        in or edited.
 *    </em>
 * </p>
 */
public class ${javaClass.name}GeneratorBuilder implements GeneratorBuilder<${javaClass.name}> {
<#if constructor.parameters?has_content>

    //Constructor parameter generators.
<#list constructor.parameters as constructorParameter>
    protected Generator<${constructorParameter.type.genericFullyQualifiedName}> ${constructorParameter.name}Constructor = $(null);
</#list>
</#if>
<#if setterMethods?has_content>

    //Setter method generators.
<#list setterMethods as setterMethod>
    protected Generator<${setterMethod.propertyType.genericFullyQualifiedName}> ${setterMethod.propertyName}Setter = $(null);
</#list>
</#if>
<#if collectionGetters?has_content>

    //Collection getter method generators.
<#list collectionGetters as collectionGetter>
    protected Generator<${collectionGetter.propertyType.genericFullyQualifiedName}> ${collectionGetter.propertyName}Collection = $(emptyList());
</#list>
</#if>

    public Generator<${javaClass.name}> build() {
        return new ${javaClass.name}Generator(
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

    public ${javaClass.name}GeneratorBuilder with${constructorParameter.name?cap_first}Constructor(Generator<${constructorParameter.type.genericFullyQualifiedName}> generator) {
        this.${constructorParameter.name}Constructor = generator;
        return this;
    }

    public ${javaClass.name}GeneratorBuilder with${constructorParameter.name?cap_first}Constructor(GeneratorBuilder<${constructorParameter.type.genericFullyQualifiedName}> builder) {
        this.${constructorParameter.name}Constructor = builder.build();
        return this;
    }
</#list>
<#list setterMethods as setterMethod>

    public ${javaClass.name}GeneratorBuilder with${setterMethod.propertyName?cap_first}Setter(Generator<${setterMethod.propertyType.genericFullyQualifiedName}> generator) {
        this.${setterMethod.propertyName}Setter = generator;
        return this;
    }

    public ${javaClass.name}GeneratorBuilder with${setterMethod.propertyName?cap_first}Setter(GeneratorBuilder<${setterMethod.propertyType.genericFullyQualifiedName}> builder) {
        this.${setterMethod.propertyName}Setter = builder.build();
        return this;
    }
</#list>
<#list collectionGetters as collectionGetter>

    public ${javaClass.name}GeneratorBuilder with${collectionGetter.propertyName?cap_first}Collection(Generator<${collectionGetter.propertyType.genericFullyQualifiedName}> generator) {
        this.${collectionGetter.propertyName}Collection = generator;
        return this;
    }

    public ${javaClass.name}GeneratorBuilder with${collectionGetter.propertyName?cap_first}Collection(GeneratorBuilder<${collectionGetter.propertyType.genericFullyQualifiedName}> builder) {
        this.${collectionGetter.propertyName}Collection = builder.build();
        return this;
    }
</#list>

    public static ${javaClass.name}GeneratorBuilder ${javaClass.name?uncap_first}Generator(Consumer<${javaClass.name}GeneratorBuilder> consumer) {
        ${javaClass.name}GeneratorBuilder result = new ${javaClass.name}GeneratorBuilder();
        consumer.accept(result);
        return result;
    }

    public static ${javaClass.name}GeneratorBuilder ${javaClass.name?uncap_first}Generator() {
        return new ${javaClass.name}GeneratorBuilder();
    }

}
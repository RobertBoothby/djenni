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
    protected Generator<${constructorParameter.type.genericFullyQualifiedName}> ${constructorParameter.name}ConstructorGenerator = $(null);
</#list>
</#if>
<#if setterMethods?has_content>

    //Setter method generators.
<#list setterMethods as setterMethod>
    protected Generator<${setterMethod.propertyType.genericFullyQualifiedName}> ${setterMethod.propertyName}SetterGenerator = $(null);
</#list>
</#if>
<#if collectionGetters?has_content>

    //Collection getter method generators.
<#list collectionGetters as collectionGetter>
    protected Generator<${collectionGetter.propertyType.genericFullyQualifiedName}> ${collectionGetter.propertyName}CollectionGenerator = $(emptyList());
</#list>
</#if>

    public Generator<${javaClass.name}> build() {
        return new ${javaClass.name}Generator(
<#if constructor.parameters?has_content>
<#list constructor.parameters as constructorParameter>
                ${constructorParameter.name}ConstructorGenerator<#sep>,
</#list>
<#if setterMethods?has_content || collectionGetters?has_content>,</#if>
</#if>
<#if setterMethods?has_content>
<#list setterMethods as setterMethod>
                ${setterMethod.propertyName}SetterGenerator<#sep>,
</#list>
<#if collectionGetters?has_content>,</#if>
</#if>
<#list collectionGetters as collectionGetter>
                ${collectionGetter.propertyName}CollectionGenerator<#sep>,
</#list>

        );
    }
<#list constructor.parameters as constructorParameter>

    public ${javaClass.name}GeneratorBuilder with${constructorParameter.name?cap_first}ConstructorGenerator(Generator<${constructorParameter.type.genericFullyQualifiedName}> generator) {
        this.${constructorParameter.name}ConstructorGenerator = generator;
        return this;
    }

    public ${javaClass.name}GeneratorBuilder with${constructorParameter.name?cap_first}ConstructorGenerator(GeneratorBuilder<${constructorParameter.type.genericFullyQualifiedName}> builder) {
        this.${constructorParameter.name}ConstructorGenerator = builder.build();
        return this;
    }
</#list>
<#list setterMethods as setterMethod>

    public ${javaClass.name}GeneratorBuilder with${setterMethod.propertyName?cap_first}SetterGenerator(Generator<${setterMethod.propertyType.genericFullyQualifiedName}> generator) {
        this.${setterMethod.propertyName}SetterGenerator = generator;
        return this;
    }

    public ${javaClass.name}GeneratorBuilder with${setterMethod.propertyName?cap_first}SetterGenerator(GeneratorBuilder<${setterMethod.propertyType.genericFullyQualifiedName}> builder) {
        this.${setterMethod.propertyName}SetterGenerator = builder.build();
        return this;
    }
</#list>
<#list collectionGetters as collectionGetter>

    public ${javaClass.name}GeneratorBuilder with${collectionGetter.propertyName?cap_first}CollectionGenerator(Generator<${collectionGetter.propertyType.genericFullyQualifiedName}> generator) {
        this.${collectionGetter.propertyName}CollectionGenerator = generator;
        return this;
    }

    public ${javaClass.name}GeneratorBuilder with${collectionGetter.propertyName?cap_first}CollectionGenerator(GeneratorBuilder<${collectionGetter.propertyType.genericFullyQualifiedName}> builder) {
        this.${collectionGetter.propertyName}CollectionGenerator = builder.build();
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
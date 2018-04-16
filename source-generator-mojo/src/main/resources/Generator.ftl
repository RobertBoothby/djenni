<#if javaSource.packageName?has_content>package ${javaSource.packageName};

</#if>
import com.robertboothby.djenni.Generator;
import java.util.Optional;
import org.hamcrest.Description;

import static java.util.Optional.ofNullable;

/**
 * <p>
 *     This class is intended to be used to generate instances of ${javaClass.name}. It is intended to be used with concrete
 *     implementations of {@link Abstract${javaClass.name}GeneratorBuilder.
 * </p>
 * <p>
 *    <em>
 *        This class has been autogenerated by the Djenni code generation framework and as such should never be checked
 *        in or edited.
 *    </em>
 * </p>
 */
public class ${javaClass.name}Generator implements Generator<${javaClass.name}> {
<#--#if(${constructor.parameters.isEmpty()} == false)-->

    <#--//Constructor parameter generators.-->
<#--#end-->
<#--#foreach(${constructorParameter} in ${constructor.parameters})-->
    <#--protected final Generator<${constructorParameter.type.genericValue.replace('$', '.')}> ${constructorParameter.name}ConstructorGenerator;-->
<#--#end-->
<#--#if(${setterMethods.isEmpty()} == false)-->

    <#--//Setter method generators.-->
<#--#end-->
<#--#foreach(${setterMethod} in ${setterMethods})-->
    <#--protected final Nullable<Generator<${setterMethod.propertyType.genericValue.replace('$', '.')}>> ${setterMethod.propertyName}SetterGenerator;-->
<#--#end-->
<#--#if(${collectionGetters.isEmpty()} == false)-->

    <#--//Collection getter method generators.-->
<#--#end-->
<#--#foreach(${collectionGetter} in ${collectionGetters})-->
    <#--protected final Nullable<Generator<${collectionGetter.propertyType.genericValue.replace('$', '.')}>> ${collectionGetter.propertyName}CollectionGenerator;-->
<#--#end-->

    <#--//Constructor-->
    <#--public ${class.name}Generator (#foreach(${constructorParameter} in ${constructor.parameters})-->

        <#--Generator<${constructorParameter.type.genericValue.replace('$', '.')}> ${constructorParameter.name}ConstructorGenerator#if(${velocityCount} != ${constructor.parameters.size()}-->
            <#--||  !${setterMethods.isEmpty()} || !${collectionGetters.isEmpty()}),#else) { #end-->
<#--#end-->
<#--#foreach(${setterMethod} in ${setterMethods})-->
        <#--Nullable<Generator<${setterMethod.propertyType.genericValue.replace('$', '.')}>> ${setterMethod.propertyName}SetterGenerator#if(${velocityCount} != ${setterMethods.size()}-->
            <#--|| !${collectionGetters.isEmpty()}),#else) { #end-->
<#--#end-->
<#--#foreach(${collectionGetter} in ${collectionGetters})-->
        <#--Nullable<Generator<${collectionGetter.propertyType.genericValue.replace('$', '.')}>> ${collectionGetter.propertyName}CollectionGenerator#if(${velocityCount} != ${collectionGetters.size()}),#else) { #end-->
<#--#end-->

<#--#foreach(${constructorParameter} in ${constructor.parameters})-->
        <#--this.${constructorParameter.name}ConstructorGenerator = ${constructorParameter.name}ConstructorGenerator;-->
<#--#end-->
<#--#foreach(${setterMethod} in ${setterMethods})-->
        <#--this.${setterMethod.propertyName}SetterGenerator = ${setterMethod.propertyName}SetterGenerator;-->
<#--#end-->
<#--#foreach(${collectionGetter} in ${collectionGetters})-->
        <#--this.${collectionGetter.propertyName}CollectionGenerator = ${collectionGetter.propertyName}CollectionGenerator;-->
<#--#end-->
    <#--}-->

    <#--public ${class.name} generate() {-->
<#--#if(${constructor.parameters.size()} == 0)-->
        <#--${class.name} generated${class.name} = new ${class.name}();-->
<#--#else-->
        <#--${class.name} generated${class.name} = new ${class.name}(#foreach(${constructorParameter} in ${constructor.parameters})-->

            <#--${constructorParameter.name}ConstructorGenerator.generate()#if(${velocityCount} != ${constructor.parameters.size()}),#else);#end-->
<#--#end-->
<#--#end-->
<#--#foreach(${setterMethod} in ${setterMethods})-->
        <#--if(${setterMethod.propertyName}SetterGenerator.hasValue()){-->
            <#--generated${class.name}.${setterMethod.name}(${setterMethod.propertyName}SetterGenerator.value().generate());-->
        <#--}-->
<#--#end-->
<#--#foreach(${collectionGetter} in ${collectionGetters})-->
        <#--if(${collectionGetter.propertyName}CollectionGenerator.hasValue()){-->
            <#--generated${class.name}.${collectionGetter.name}().addAll(${collectionGetter.propertyName}CollectionGenerator.value().generate());-->
        <#--}-->
<#--#end-->

        <#--return generated${class.name};-->
    <#--}-->

    <#--public void describeTo(Description description) {-->
        <#--description.appendText("{  ${class.name}Generator ");-->
<#--#if(${constructor.parameters.isEmpty()} && ${setterMethods.isEmpty()} && ${collectionGetters.isEmpty()})#else-->
<#--#if(!${constructor.parameters.isEmpty()})-->
        <#--description.appendText("Constructor Generators : { ");-->
<#--#foreach(${constructorParameter} in ${constructor.parameters})-->
        <#--description.appendText("${constructorParameter.name}ConstructorGenerator : ");-->
        <#--${constructorParameter.name}ConstructorGenerator.describeTo(description);-->
<#--#if(${velocityCount} != ${constructor.parameters.size()})-->
        <#--description.appendText(", ");-->
<#--#end-->
<#--#end-->
        <#--description.appendText("} ");-->
<#--#end-->

<#--#if(!${setterMethods.isEmpty()})-->
        <#--description.appendText("Setter Generators : { ");-->
<#--#foreach(${setterMethod} in ${setterMethods})-->
        <#--description.appendText("${setterMethod.propertyName}SetterGenerator : ");-->
        <#--if(${setterMethod.propertyName}SetterGenerator.hasValue()){-->
            <#--${setterMethod.propertyName}SetterGenerator.value().describeTo(description);-->
        <#--} else {-->
            <#--description.appendText(" null ");-->
        <#--}-->
        <#--${setterMethod.propertyName}SetterGenerator.value().describeTo(description);-->
<#--#if(${velocityCount} != ${setterMethods.size()})-->
        <#--description.appendText(", ");-->
<#--#end-->
<#--#end-->
        <#--description.appendText("} ");-->
<#--#end-->

<#--#if(!${collectionMethods.isEmpty()})-->
        <#--description.appendText("Collection Generators : { ");-->
<#--#foreach(${collectionMethod} in ${collectionMethods})-->
        <#--description.appendText("${collectionMethod.propertyName}CollectiorGenerator : ");-->
        <#--if(${collectionMethod.propertyName}CollectiorGenerator.hasValue()){-->
            <#--${collectionMethod.propertyName}CollectiorGenerator.value().describeTo(description);-->
        <#--} else {-->
            <#--description.appendText(" null ");-->
        <#--}-->
<#--#if(${velocityCount} != ${collectionMethods.size()})-->
        <#--description.appendText(", ");-->
<#--#end-->
<#--#end-->
        <#--description.appendText("} ");-->
<#--#end-->
<#--#end-->
        <#--description.appendText("}");-->
    <#--}-->
}
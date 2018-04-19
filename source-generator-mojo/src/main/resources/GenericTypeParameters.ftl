<#macro removeAngleBrackets genericValue>${genericValue?trim?remove_beginning("<")?remove_ending(">")}</#macro>
<#macro genericParameters typeParameters=[]><#if typeParameters?has_content><#assign typeNames><#list typeParameters as typeParameter><@removeAngleBrackets genericValue=typeParameter.genericValue/><#sep>|</#list></#assign><${typeNames?split('|')?join(", ")}> </#if></#macro>
<#macro genericNames typeParameters=[]><#if typeParameters?has_content><#assign typeNames><#list typeParameters as typeParameter>${typeParameter.name}<#sep>|</#list></#assign><${typeNames?split('|')?join(", ")}> </#if></#macro>

<#--This template is expected to be included in others and will look for a 'genericTypeParameters' variable-->




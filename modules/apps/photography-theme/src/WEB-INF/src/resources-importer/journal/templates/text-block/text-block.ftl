<#--
Web content templates are used to lay out the fields defined in a web
content structure.

Please use the left panel to quickly add commonly used variables.
Autocomplete is also available and can be invoked by typing "${".
-->
<div class="text-block" style="background-color:${backgroundColor.getData()}; height:${blockHeight.getData()}${HeightType.getData()};">
    <h2 style="color:${textColor.getData()};">${heading.getData()}</h2>
    <#if htmlInput.getSiblings()?has_content>
    	<#list htmlInput.getSiblings() as cur_htmlInput>
    		${cur_htmlInput.getData()}
    	</#list>
    </#if>
    <ul>
        <#if link.getSiblings()?has_content>
            <#list link.getSiblings() as cur_link>
                <li><a href="${cur_link.getData()}" style="color:${linkColor.getData()};">${cur_link.text.getData()}</a></li>
            </#list>
        </#if>
    </ul>
    <a href="${linkToPage.getFriendlyUrl()}" style="color:${linkColor.getData()};">
	    ${linkToPage.linkText.getData()}
	</a>
</div>

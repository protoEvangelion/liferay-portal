<#--
Web content templates are used to lay out the fields defined in a web
content structure.

Please use the left panel to quickly add commonly used variables.
Autocomplete is also available and can be invoked by typing "${".
-->
<#if image.getSiblings()?has_content>
    <div class="carousel" style="height:${Height.getData()}${HeightUnits.getData()};">
    	<#list image.getSiblings() as cur_image>
    		<#if cur_image.getData()?? && cur_image.getData() != "">
    			<div class="carousel-cell" style="background: url(${cur_image.getData()})" >
    			    <div class="heading-container">
        			    <h1 style="color:${cur_image.textColors.getData()};">${cur_image.heading1.getData()}</h1>
        			    <h2 style="color:${cur_image.textColors.getData()};">${cur_image.heading2.getData()}</h2>
    			    </div>
    			</div>
    		</#if>
    	</#list>
    </div>
</#if>

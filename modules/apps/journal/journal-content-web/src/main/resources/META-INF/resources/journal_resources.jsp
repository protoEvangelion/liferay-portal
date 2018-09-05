<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
String refererPortletName = ParamUtil.getString(request, "refererPortletName");

JournalArticle article = journalContentDisplayContext.getArticle();
%>

<aui:input id='<%= refererPortletName + "ddmTemplateKey" %>' name='<%= refererPortletName + "preferences--ddmTemplateKey--" %>' type="hidden" useNamespace="<%= false %>" value="<%= journalContentDisplayContext.getDDMTemplateKey() %>" />

<p class="text-muted">
	<liferay-ui:message key="layout.types.article" />
</p>

<div class="row">
	<div class="col-md-4">
		<c:if test="<%= article != null %>">
			<liferay-util:include page="/journal_article_resources.jsp" servletContext="<%= application %>" />
		</c:if>
	</div>
</div>

<div class="button-holder">
	<aui:button cssClass="web-content-selector" name="webContentSelector" value='<%= Validator.isNull(article) ? "select" : "change" %>' />

	<c:if test="<%= article != null %>">
		<aui:button cssClass="selector-button" name="removeWebContent" value="remove" />
	</c:if>
</div>

<c:if test="<%= article != null %>">
	<liferay-util:include page="/journal_template.jsp" servletContext="<%= application %>" />

	<h4>
		<liferay-ui:message key="user-tools" />
	</h4>

	<liferay-asset:asset-addon-entry-selector
		assetAddonEntries="<%= (List<AssetAddonEntry>)(List<?>)journalContentDisplayContext.getEnabledUserToolAssetAddonEntries() %>"
		hiddenInput="preferences--userToolAssetAddonEntryKeys--"
		id="userToolsAssetAddonEntriesSelector"
		selectedAssetAddonEntries="<%= (List<AssetAddonEntry>)(List<?>)journalContentDisplayContext.getSelectedUserToolAssetAddonEntries() %>"
		title='<%= LanguageUtil.get(request, "select-user-tools") %>'
	/>

	<h4>
		<liferay-ui:message key="content-metadata" />
	</h4>

	<liferay-asset:asset-addon-entry-selector
		assetAddonEntries="<%= (List<AssetAddonEntry>)(List<?>)journalContentDisplayContext.getEnabledContentMetadataAssetAddonEntries() %>"
		hiddenInput="preferences--contentMetadataAssetAddonEntryKeys--"
		id="contentMetadataAssetAddonEntriesSelector"
		selectedAssetAddonEntries="<%= (List<AssetAddonEntry>)(List<?>)journalContentDisplayContext.getSelectedContentMetadataAssetAddonEntries() %>"
		title='<%= LanguageUtil.get(request, "select-content-metadata") %>'
	/>

	<aui:input name="preferences--enableViewCountIncrement--" type="toggle-switch" value="<%= journalContentDisplayContext.isEnableViewCountIncrement() %>" />
</c:if>
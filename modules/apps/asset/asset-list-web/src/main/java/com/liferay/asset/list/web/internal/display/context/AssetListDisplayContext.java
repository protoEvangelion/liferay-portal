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

package com.liferay.asset.list.web.internal.display.context;

import com.liferay.asset.list.constants.AssetListActionKeys;
import com.liferay.asset.list.constants.AssetListEntryTypeConstants;
import com.liferay.asset.list.constants.AssetListFormConstants;
import com.liferay.asset.list.constants.AssetListPortletKeys;
import com.liferay.asset.list.constants.AssetListWebKeys;
import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.service.AssetListEntryServiceUtil;
import com.liferay.asset.list.web.internal.security.permission.resource.AssetListPermission;
import com.liferay.asset.list.web.util.AssetListPortletUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationRegistry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class AssetListDisplayContext {

	public AssetListDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;

		_request = PortalUtil.getHttpServletRequest(renderRequest);

		_portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			_request);
		_themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public ScreenNavigationEntry getActiveScreenNavigationEntry()
		throws Exception {

		String screenNavigationEntryKey = ParamUtil.getString(
			_request, "screenNavigationEntryKey",
			AssetListFormConstants.ENTRY_KEY_DETAILS);

		List<ScreenNavigationEntry> screenNavigationEntries =
			getScreenNavigationEntries();

		screenNavigationEntries = ListUtil.filter(
			screenNavigationEntries,
			screenNavigationEntry -> Objects.equals(
				screenNavigationEntryKey, screenNavigationEntry.getEntryKey()));

		if (screenNavigationEntries.isEmpty()) {
			return null;
		}

		return screenNavigationEntries.get(0);
	}

	public List<DropdownItem> getAddAssetListEntryDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setHref(
							_addAssetListEntryURL(
								AssetListEntryTypeConstants.TYPE_MANUAL));
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "manual-selection"));
					});

				add(
					dropdownItem -> {
						dropdownItem.setHref(
							_addAssetListEntryURL(
								AssetListEntryTypeConstants.TYPE_DYNAMIC));
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "dynamic-selection"));
					});
			}
		};
	}

	public int getAssetListEntriesCount() {
		if (_assetListEntriesCount != null) {
			return _assetListEntriesCount;
		}

		int assetListEntriesCount =
			AssetListEntryServiceUtil.getAssetListEntriesCount(
				_themeDisplay.getScopeGroupId());

		_assetListEntriesCount = assetListEntriesCount;

		return _assetListEntriesCount;
	}

	public SearchContainer getAssetListEntriesSearchContainer() {
		if (_assetListEntriesSearchContainer != null) {
			return _assetListEntriesSearchContainer;
		}

		SearchContainer assetListEntriesSearchContainer = new SearchContainer(
			_renderRequest, _renderResponse.createRenderURL(), null,
			"there-are-no-asset-lists");

		assetListEntriesSearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		OrderByComparator<AssetListEntry> orderByComparator =
			AssetListPortletUtil.getAssetListEntryOrderByComparator(
				_getOrderByCol(), getOrderByType());

		assetListEntriesSearchContainer.setOrderByCol(_getOrderByCol());
		assetListEntriesSearchContainer.setOrderByComparator(orderByComparator);
		assetListEntriesSearchContainer.setOrderByType(getOrderByType());

		List<AssetListEntry> assetListEntries = null;

		int assetListEntriesCount = 0;

		if (_isSearch()) {
			assetListEntries = AssetListEntryServiceUtil.getAssetListEntries(
				_themeDisplay.getScopeGroupId(), _getKeywords(),
				assetListEntriesSearchContainer.getStart(),
				assetListEntriesSearchContainer.getEnd(), orderByComparator);

			assetListEntriesCount =
				AssetListEntryServiceUtil.getAssetListEntriesCount(
					_themeDisplay.getScopeGroupId(), _getKeywords());
		}
		else {
			assetListEntries = AssetListEntryServiceUtil.getAssetListEntries(
				_themeDisplay.getScopeGroupId(),
				assetListEntriesSearchContainer.getStart(),
				assetListEntriesSearchContainer.getEnd(), orderByComparator);

			assetListEntriesCount = getAssetListEntriesCount();
		}

		assetListEntriesSearchContainer.setResults(assetListEntries);

		assetListEntriesSearchContainer.setTotal(assetListEntriesCount);

		_assetListEntriesSearchContainer = assetListEntriesSearchContainer;

		return _assetListEntriesSearchContainer;
	}

	public AssetListEntry getAssetListEntry() throws PortalException {
		if (_assetListEntry != null) {
			return _assetListEntry;
		}

		long assetListEntryId = getAssetListEntryId();

		_assetListEntry = AssetListEntryServiceUtil.fetchAssetListEntry(
			assetListEntryId);

		return _assetListEntry;
	}

	public List<DropdownItem> getAssetListEntryActionItemsDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData(
							"action", "deleteSelectedAssetListEntries");
						dropdownItem.setIcon("times-circle");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "delete"));
						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	public String getAssetListEntryClearResultsURL() {
		PortletURL clearResultsURL = getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);

		return clearResultsURL.toString();
	}

	public List<DropdownItem> getAssetListEntryFilterItemsDropdownItems() {
		return new DropdownItemList() {
			{
				addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItems(
							_getAssetListEntryOrderByDropdownItems());
						dropdownGroupItem.setLabel(
							LanguageUtil.get(_request, "order-by"));
					});
			}
		};
	}

	public long getAssetListEntryId() {
		if (_assetListEntryId != null) {
			return _assetListEntryId;
		}

		_assetListEntryId = ParamUtil.getLong(_request, "assetListEntryId");

		return _assetListEntryId;
	}

	public String getAssetListEntrySearchActionURL() {
		PortletURL searchActionURL = _renderResponse.createRenderURL();

		searchActionURL.setParameter("mvcPath", "/view.jsp");
		searchActionURL.setParameter("redirect", _themeDisplay.getURLCurrent());

		return searchActionURL.toString();
	}

	public String getAssetListEntryTitle() throws PortalException {
		String title = StringPool.BLANK;

		int assetListEntryType = getAssetListEntryType();

		AssetListEntry assetListEntry = getAssetListEntry();

		if (assetListEntry != null) {
			title = HtmlUtil.escape(assetListEntry.getTitle());
		}
		else if (assetListEntryType ==
					 AssetListEntryTypeConstants.TYPE_DYNAMIC) {

			title = "new-dynamic-asset-list";
		}
		else if (assetListEntryType ==
					 AssetListEntryTypeConstants.TYPE_MANUAL) {

			title = "new-manual-asset-list";
		}

		return LanguageUtil.get(_request, title);
	}

	public int getAssetListEntryTotalItems() {
		SearchContainer assetListEntriesSearchContainer =
			getAssetListEntriesSearchContainer();

		return assetListEntriesSearchContainer.getTotal();
	}

	public int getAssetListEntryType() throws PortalException {
		if (_assetListEntryType != null) {
			return _assetListEntryType;
		}

		AssetListEntry assetListEntry = getAssetListEntry();

		int assetListEntryType = ParamUtil.getInteger(
			_request, "assetListEntryType");

		if (assetListEntry != null) {
			assetListEntryType = assetListEntry.getType();
		}

		_assetListEntryType = assetListEntryType;

		return _assetListEntryType;
	}

	public CreationMenu getCreationMenu() {
		return new CreationMenu() {
			{
				addPrimaryDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref(
							_addAssetListEntryURL(
								AssetListEntryTypeConstants.TYPE_MANUAL));
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "manual-selection"));
					});

				addPrimaryDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref(
							_addAssetListEntryURL(
								AssetListEntryTypeConstants.TYPE_DYNAMIC));
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "dynamic-selection"));
					});
			}
		};
	}

	public String getEmptyResultMessageDescription() {
		if (isShowAddAssetListEntryAction()) {
			return LanguageUtil.get(
				_request, "fortunately-it-is-very-easy-to-add-new-ones");
		}

		return StringPool.BLANK;
	}

	public String getOrderByCol() {
		if (_orderByCol != null) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(_request, "orderByCol");

		if (Validator.isNull(_orderByCol)) {
			_orderByCol = _portalPreferences.getValue(
				AssetListPortletKeys.ASSET_LIST, "order-by-col", "create-date");
		}
		else {
			boolean saveOrderBy = ParamUtil.getBoolean(_request, "saveOrderBy");

			if (saveOrderBy) {
				_portalPreferences.setValue(
					AssetListPortletKeys.ASSET_LIST, "order-by-col",
					_orderByCol);
			}
		}

		return _orderByCol;
	}

	public String getOrderByType() {
		if (_orderByType != null) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(_request, "orderByType");

		if (Validator.isNull(_orderByType)) {
			_orderByType = _portalPreferences.getValue(
				AssetListPortletKeys.ASSET_LIST, "order-by-type", "asc");
		}
		else {
			boolean saveOrderBy = ParamUtil.getBoolean(_request, "saveOrderBy");

			if (saveOrderBy) {
				_portalPreferences.setValue(
					AssetListPortletKeys.ASSET_LIST, "order-by-type",
					_orderByType);
			}
		}

		return _orderByType;
	}

	public PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		String keywords = _getKeywords();

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		String orderByCol = getOrderByCol();

		if (Validator.isNotNull(orderByCol)) {
			portletURL.setParameter("orderByCol", orderByCol);
		}

		String orderByType = getOrderByType();

		if (Validator.isNotNull(orderByType)) {
			portletURL.setParameter("orderByType", orderByType);
		}

		return portletURL;
	}

	public List<ScreenNavigationEntry> getScreenNavigationEntries()
		throws Exception {

		AssetListEntry assetListEntry = getAssetListEntry();

		ScreenNavigationRegistry screenNavigationRegistry =
			(ScreenNavigationRegistry)_renderRequest.getAttribute(
				AssetListWebKeys.SCREEN_NAVIGATION_REGISTRY);

		List<ScreenNavigationCategory> screenNavigationCategories =
			screenNavigationRegistry.getScreenNavigationCategories(
				AssetListFormConstants.SCREEN_NAVIGATION_KEY_ASSET_LIST,
				_themeDisplay.getUser(), assetListEntry);

		List<ScreenNavigationEntry> screenNavigationEntries = new ArrayList<>();

		for (ScreenNavigationCategory screenNavigationCategory :
				screenNavigationCategories) {

			screenNavigationEntries.addAll(
				screenNavigationRegistry.getScreenNavigationEntries(
					screenNavigationCategory, _themeDisplay.getUser(),
					assetListEntry));
		}

		return screenNavigationEntries;
	}

	public String getSortingURL() {
		PortletURL sortingURL = getPortletURL();

		sortingURL.setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc");

		return sortingURL.toString();
	}

	public boolean isShowAddAssetListEntryAction() {
		return AssetListPermission.contains(
			_themeDisplay.getPermissionChecker(),
			_themeDisplay.getScopeGroupId(),
			AssetListActionKeys.ADD_ASSET_LIST_ENTRY);
	}

	private String _addAssetListEntryURL(int type) {
		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletURL addAssetListEntry = _renderResponse.createRenderURL();

		addAssetListEntry.setParameter("mvcPath", "/edit_asset_list_entry.jsp");
		addAssetListEntry.setParameter(
			"redirect", themeDisplay.getURLCurrent());
		addAssetListEntry.setParameter(
			"assetListEntryType", String.valueOf(type));

		return addAssetListEntry.toString();
	}

	private List<DropdownItem> _getAssetListEntryOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(_getOrderByCol(), "title"));
						dropdownItem.setHref(
							getPortletURL(), "orderByCol", "title");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "title"));
					});

				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(_getOrderByCol(), "create-date"));
						dropdownItem.setHref(
							getPortletURL(), "orderByCol", "create-date");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "create-date"));
					});
			}
		};
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_request, "keywords");

		return _keywords;
	}

	private String _getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_request, "orderByCol", "create-date");

		return _orderByCol;
	}

	private boolean _isSearch() {
		if (Validator.isNotNull(_getKeywords())) {
			return true;
		}

		return false;
	}

	private Integer _assetListEntriesCount;
	private SearchContainer _assetListEntriesSearchContainer;
	private AssetListEntry _assetListEntry;
	private Long _assetListEntryId;
	private Integer _assetListEntryType;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final PortalPreferences _portalPreferences;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;
	private final ThemeDisplay _themeDisplay;

}
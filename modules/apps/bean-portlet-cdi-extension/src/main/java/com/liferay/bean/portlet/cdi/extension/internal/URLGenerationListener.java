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

package com.liferay.bean.portlet.cdi.extension.internal;

import com.liferay.petra.string.StringPool;

/**
 * @author Neil Griffin
 */
public class URLGenerationListener {

	public URLGenerationListener() {
	}

	public URLGenerationListener(int ordinal, String listenerClassName) {
		_ordinal = ordinal;
		_listenerClassName = listenerClassName;
	}

	public String getListenerClassName() {
		return _listenerClassName;
	}

	public int getOrdinal() {
		return _ordinal;
	}

	public void setListenerClassName(String listenerClassName) {
		_listenerClassName = listenerClassName;
	}

	public void setOrdinal(int ordinal) {
		_ordinal = ordinal;
	}

	@Override
	public String toString() {
		if (_listenerClassName == null) {
			return null;
		}

		return _listenerClassName.concat(
			StringPool.SEMICOLON).concat(String.valueOf(_ordinal));
	}

	private String _listenerClassName;
	private int _ordinal;

}
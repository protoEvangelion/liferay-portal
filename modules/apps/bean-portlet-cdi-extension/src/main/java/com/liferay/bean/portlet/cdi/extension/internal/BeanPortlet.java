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

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

/**
 * @author Neil Griffin
 */
public interface BeanPortlet {

	public void addBeanMethod(BeanMethod beanMethod);

	public void addLiferayConfiguration(
		Map<String, String> liferayConfiguration);

	public void addLiferayConfiguration(String name, String value);

	public void addPortletDependency(PortletDependency portletDependency);

	public BeanApp getBeanApp();

	public List<BeanMethod> getBeanMethods(MethodType methodType);

	public String getPortletClassName();

	public String getPortletName();

	public String getResourceBundle();

	public Dictionary<String, Object> toDictionary(String portletId);

}
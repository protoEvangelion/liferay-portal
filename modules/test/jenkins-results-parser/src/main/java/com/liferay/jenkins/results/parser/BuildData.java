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

package com.liferay.jenkins.results.parser;

import java.io.File;

import java.util.List;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public interface BuildData {

	public static final String DIST_ROOT_PATH = "/tmp/dist";

	public static final String JENKINS_BUILD_DATA_FILE_NAME =
		"jenkins-build-data.json";

	public Integer getBuildNumber();

	public String getBuildURL();

	public String getCohortName();

	public List<String> getDistNodes();

	public String getDistPath();

	public String getHostname();

	public String getJenkinsGitHubURL();

	public String getJobName();

	public String getMasterHostname();

	public String getRunID();

	public File getWorkspaceDir();

	public JSONObject toJSONObject();

}
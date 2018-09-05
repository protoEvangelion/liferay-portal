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

package com.liferay.sharing.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.service.test.ServiceTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.sharing.constants.SharingEntryActionKey;
import com.liferay.sharing.exception.NoSuchEntryException;
import com.liferay.sharing.model.SharingEntry;
import com.liferay.sharing.security.permission.SharingPermissionChecker;
import com.liferay.sharing.service.SharingEntryLocalService;
import com.liferay.sharing.service.SharingEntryService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Sergio González
 */
@RunWith(Arquillian.class)
public class SharingEntryServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
		_fromUser = UserTestUtil.addUser();
		_toUser = UserTestUtil.addUser();
		_user = UserTestUtil.addUser();

		ServiceTestUtil.setUser(_fromUser);

		Bundle bundle = FrameworkUtil.getBundle(SharingEntryServiceTest.class);

		_bundleContext = bundle.getBundleContext();

		_testSharingPermissionCheckerClassName =
			_classNameLocalService.addClassName("TestSharingPermissionChecker");
	}

	@After
	public void tearDown() throws Exception {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	@Test(expected = PrincipalException.class)
	public void testAddSharingEntryWithInvalidClassNameIdThrowsException()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		ClassName invalidClassName = _classNameLocalService.addClassName(
			"InvalidClassName");

		long classNameId = invalidClassName.getClassNameId();

		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);
	}

	@Test
	public void testAddSharingEntryWithUpdateAndViewPermission()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(
					SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		SharingEntry sharingEntry = _sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);

		Assert.assertEquals(_group.getCompanyId(), sharingEntry.getCompanyId());
		Assert.assertEquals(_group.getGroupId(), sharingEntry.getGroupId());
		Assert.assertEquals(
			_fromUser.getUserId(), sharingEntry.getFromUserId());
		Assert.assertEquals(_toUser.getUserId(), sharingEntry.getToUserId());
		Assert.assertEquals(classNameId, sharingEntry.getClassNameId());
		Assert.assertEquals(classPK, sharingEntry.getClassPK());
		Assert.assertTrue(sharingEntry.isShareable());
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testAddSharingEntryWithUpdateAndViewPermissionWhenUserHasShareableAddDiscussionAndViewSharingEntryActionKey()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _fromUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), true,
			Arrays.asList(
				SharingEntryActionKey.ADD_DISCUSSION,
				SharingEntryActionKey.VIEW),
			serviceContext);

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);
	}

	@Test
	public void testAddSharingEntryWithUpdateAndViewPermissionWhenUserHasShareableUpdateAndViewSharingEntryActionKey()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _fromUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testAddSharingEntryWithUpdateAndViewPermissionWhenUserHasUnshareableUpdateAndViewSharingEntryActionKey()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _fromUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), false,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testAddSharingEntryWithUpdateAndViewPermissionWhenUserHasUpdatePermissionAndShareableViewSharingEntryActionKey()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.UPDATE)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _fromUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), true,
			Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testAddSharingEntryWithUpdateAndViewPermissionWhenUserHasViewPermission()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testAddSharingEntryWithUpdatePermissionWhenUserHasViewPermissionThrowsException()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.UPDATE), serviceContext);
	}

	@Test
	public void testAddSharingEntryWithViewPermission() throws Exception {
		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		SharingEntry sharingEntry = _sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);

		Assert.assertEquals(_group.getCompanyId(), sharingEntry.getCompanyId());
		Assert.assertEquals(_group.getGroupId(), sharingEntry.getGroupId());
		Assert.assertEquals(
			_fromUser.getUserId(), sharingEntry.getFromUserId());
		Assert.assertEquals(_toUser.getUserId(), sharingEntry.getToUserId());
		Assert.assertEquals(classNameId, sharingEntry.getClassNameId());
		Assert.assertEquals(classPK, sharingEntry.getClassPK());
		Assert.assertTrue(sharingEntry.isShareable());
	}

	@Test
	public void testAddSharingEntryWithViewPermissionWhenUserHasShareableUpdateAndViewSharingEntryActionKey()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _fromUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), true,
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW),
			serviceContext);

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);
	}

	@Test
	public void testAddSharingEntryWithViewPermissionWhenUserHasViewPermissionAndShareableViewSharingEntryActionKey()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _fromUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), true,
			Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);
	}

	@Test
	public void testAddSharingEntryWithViewPermissionWhenUserHasViewPermissionAndUnshareableViewSharingEntryActionKey()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		_sharingEntryLocalService.addSharingEntry(
			_user.getUserId(), _fromUser.getUserId(), classNameId, classPK,
			_group.getGroupId(), false,
			Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);

		_sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);
	}

	@Test(expected = NoSuchEntryException.class)
	public void testDeleteNonexistingSharingEntry() throws Exception {
		_sharingEntryService.updateSharingEntry(
			RandomTestUtil.randomLong(),
			Arrays.asList(
				SharingEntryActionKey.ADD_DISCUSSION,
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW));
	}

	@Test
	public void testUpdateSharingEntryWithUpdateAndViewPermission()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(
					SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		SharingEntry sharingEntry = _sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);

		sharingEntry = _sharingEntryService.updateSharingEntry(
			sharingEntry.getSharingEntryId(),
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW));

		Assert.assertEquals(3, sharingEntry.getActionIds());
		Assert.assertEquals(_group.getCompanyId(), sharingEntry.getCompanyId());
		Assert.assertEquals(_group.getGroupId(), sharingEntry.getGroupId());
		Assert.assertEquals(
			_fromUser.getUserId(), sharingEntry.getFromUserId());
		Assert.assertEquals(_toUser.getUserId(), sharingEntry.getToUserId());
		Assert.assertEquals(classNameId, sharingEntry.getClassNameId());
		Assert.assertEquals(classPK, sharingEntry.getClassPK());
		Assert.assertTrue(sharingEntry.isShareable());
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testUpdateSharingEntryWithUpdateAndViewPermissionWhenUserHasViewPermission()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		SharingEntry sharingEntry = _sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);

		_sharingEntryService.updateSharingEntry(
			sharingEntry.getSharingEntryId(),
			Arrays.asList(
				SharingEntryActionKey.UPDATE, SharingEntryActionKey.VIEW));
	}

	@Test(expected = PrincipalException.MustHavePermission.class)
	public void testUpdateSharingEntryWithUpdatePermissionWhenUserHasViewPermission()
		throws Exception {

		_registerSharingPermissionChecker(
			new TestSharingPermissionChecker(
				Arrays.asList(SharingEntryActionKey.VIEW)));

		long classNameId =
			_testSharingPermissionCheckerClassName.getClassNameId();
		long classPK = RandomTestUtil.randomLong();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		SharingEntry sharingEntry = _sharingEntryService.addSharingEntry(
			_toUser.getUserId(), classNameId, classPK, _group.getGroupId(),
			true, Arrays.asList(SharingEntryActionKey.VIEW), serviceContext);

		_sharingEntryService.updateSharingEntry(
			sharingEntry.getSharingEntryId(),
			Arrays.asList(SharingEntryActionKey.UPDATE));
	}

	private void _registerSharingPermissionChecker(
		SharingPermissionChecker sharingPermissionChecker) {

		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put(
			"model.class.name",
			_testSharingPermissionCheckerClassName.getValue());

		_serviceRegistration = _bundleContext.registerService(
			SharingPermissionChecker.class, sharingPermissionChecker,
			properties);
	}

	private BundleContext _bundleContext;

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@DeleteAfterTestRun
	private User _fromUser;

	@DeleteAfterTestRun
	private Group _group;

	private ServiceRegistration<SharingPermissionChecker> _serviceRegistration;

	@Inject
	private SharingEntryLocalService _sharingEntryLocalService;

	@Inject
	private SharingEntryService _sharingEntryService;

	private ClassName _testSharingPermissionCheckerClassName;

	@DeleteAfterTestRun
	private User _toUser;

	@DeleteAfterTestRun
	private User _user;

	private class TestSharingPermissionChecker
		implements SharingPermissionChecker {

		public TestSharingPermissionChecker(
			List<SharingEntryActionKey> sharingEntryActionKeys) {

			_sharingEntryActionKeys = sharingEntryActionKeys;
		}

		@Override
		public boolean hasPermission(
			PermissionChecker permissionChecker, long classPK, long groupId,
			Collection<SharingEntryActionKey> sharingEntryActionKeys) {

			for (SharingEntryActionKey sharingEntryActionKey :
					sharingEntryActionKeys) {

				if (!_sharingEntryActionKeys.contains(sharingEntryActionKey)) {
					return false;
				}
			}

			return true;
		}

		private final List<SharingEntryActionKey> _sharingEntryActionKeys;

	}

}
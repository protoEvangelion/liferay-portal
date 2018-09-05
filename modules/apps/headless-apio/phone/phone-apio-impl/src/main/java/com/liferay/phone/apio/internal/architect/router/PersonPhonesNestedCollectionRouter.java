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

package com.liferay.phone.apio.internal.architect.router;

import com.liferay.apio.architect.router.NestedCollectionRouter;
import com.liferay.person.apio.architect.identifier.PersonIdentifier;
import com.liferay.phone.apio.architect.identifier.PhoneIdentifier;
import com.liferay.phone.apio.internal.architect.router.base.BaseUserAccountPhonesNestedCollectionRouter;
import com.liferay.portal.kernel.model.Phone;

import org.osgi.service.component.annotations.Component;

/**
 * Provides the information necessary to expose the <a
 * href="http://schema.org/Telephone">Telephone</a> resources contained inside
 * an <a href="http://schema.org/Person">Person</a> through a web API. The
 * resources are mapped from the internal model {@link Phone}.
 *
 * @author Javier Gamarra
 * @review
 */
@Component(immediate = true)
public class PersonPhonesNestedCollectionRouter
	extends BaseUserAccountPhonesNestedCollectionRouter<PersonIdentifier>
	implements NestedCollectionRouter
		<Phone, Long, PhoneIdentifier, Long, PersonIdentifier> {
}
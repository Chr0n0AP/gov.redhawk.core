/**
 * This file is protected by Copyright.
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package gov.redhawk.sca.model.provider.refresh.preferences;

/**
 * @since 4.0
 */
public final class RefreshPreferenceConstants {

	public static final String REFRESH_INTERVAL = "refreshInterval";

	/**
	 * @since 5.0
	 */
	public static final String REFRESH_TIMEOUT = "refreshTimeout";

	/**
	 * @since 5.0
	 * @deprecated Do not use.
	 */
	@Deprecated
	public static final String REFRESH_OVERRIDE_DEPTH = "refreshOverrideDepth";

	private RefreshPreferenceConstants() {
	}

}

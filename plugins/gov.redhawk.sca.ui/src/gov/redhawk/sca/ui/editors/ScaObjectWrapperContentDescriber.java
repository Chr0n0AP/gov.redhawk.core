/**
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.sca.ui.editors;

import gov.redhawk.model.sca.CorbaObjWrapper;
import gov.redhawk.model.sca.ProfileObjectWrapper;
import gov.redhawk.model.sca.util.ScaFileSystemUtil;
import gov.redhawk.sca.ui.ScaFileStoreEditorInput;
import gov.redhawk.sca.ui.ScaUI;
import gov.redhawk.sca.ui.ScaUiPlugin;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorInput;

/**
 * @since 2.2
 */
public class ScaObjectWrapperContentDescriber implements IScaContentDescriber, IExecutableExtension {
	public static final String PARAM_PROFILE_FILENAME = "profileFilename";
	private Map<String, String> params;

	@Override
	public int describe(final Object contents) throws IOException {
		if (this.params == null) {
			return IScaContentDescriber.INDETERMINATE;
		}

		if (!(contents instanceof ProfileObjectWrapper< ? >)) {
			return IScaContentDescriber.INVALID;
		}

		final ProfileObjectWrapper< ? > obj = (ProfileObjectWrapper< ? >) contents;

		final String profileFileName = this.params.get(ScaObjectWrapperContentDescriber.PARAM_PROFILE_FILENAME);
		try {
			if (profileFileName != null) {
				final Object scaObj = obj.getProfileObj();
				if (scaObj instanceof EObject) {
					final EObject eObj = (EObject) scaObj;
					final Resource resource = eObj.eResource();
					if (resource != null && resource.getURI() != null && resource.getURI().lastSegment() != null
						&& resource.getURI().lastSegment().matches(profileFileName)) {
						return IScaContentDescriber.VALID;
					}
				}
			}
		} catch (final Exception e) { // SUPPRESS CHECKSTYLE Logged
			ScaUiPlugin.logError("Unable to describe content", e);
		}
		return IScaContentDescriber.INVALID;
	}

	@Override
	public IEditorInput getEditorInput(final Object contents) {
		if (!(contents instanceof ProfileObjectWrapper)) {
			return null;
		}
		IFileStore store;
		try {
			store = ScaFileSystemUtil.getFileStore((ProfileObjectWrapper< ? >) contents);
		} catch (CoreException e) {
			return null;
		}
		if (contents instanceof CorbaObjWrapper< ? >) {
			return new ScaFileStoreEditorInput((CorbaObjWrapper< ? >) contents, store);
		}
		return ScaUI.getEditorInput(store);
	}

	/**
	 * @since 9.3
	 * @deprecated Use {@link ScaFileSystemUtil#getFileStore(ProfileObjectWrapper)}
	 */
	@Deprecated
	public static IFileStore getFileStore(ProfileObjectWrapper< ? > obj) throws CoreException {
		return ScaFileSystemUtil.getFileStore(obj);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data) throws CoreException {
		if (data instanceof Map) {
			this.params = (Map<String, String>) data;
		}
	}
}

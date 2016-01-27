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
package gov.redhawk.core.internal.filemanager;

import CF.DataType;
import CF.FileSystemPackage.FileInformationType;

/**
 * An entry in the IDE's file manager file system. See derived classes for details.
 */
public interface Node {
	public DataType[] createDataTypeArray();

	FileInformationType createFileInformationType();

}

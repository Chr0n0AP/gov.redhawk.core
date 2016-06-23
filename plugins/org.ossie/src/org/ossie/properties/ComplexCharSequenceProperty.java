/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file 
 * distributed with this source distribution.
 * 
 * This file is part of REDHAWK core.
 * 
 * REDHAWK core is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * REDHAWK core is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

/*
 * WARNING: This file is generated from ComplexSequenceProperty.template.
 *          Do not modify directly.
 */

package org.ossie.properties;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.omg.CORBA.Any;

import CF.complexChar;
import CF.complexCharSeqHelper;

public class ComplexCharSequenceProperty extends AbstractSequenceProperty<complexChar> {
    public ComplexCharSequenceProperty(String id, String name, List<complexChar> value, Mode mode,
                                         Action action, Kind[] kinds) {
        super(id, name, "char", value, mode, action, kinds);
    }

    public static List<complexChar> asList(complexChar... array) {
        return new ArrayList<complexChar>(Arrays.asList(array));
    }

    protected List<complexChar> extract(Any any) {
        complexChar[] array = complexCharSeqHelper.extract(any);
        List<complexChar> list = new ArrayList<complexChar>(array.length);
        for (complexChar item : array) {
            list.add(item);
        }
        return list;
    }

    protected void insert(Any any, List<complexChar> value) {
        complexChar[] array = value.toArray(new complexChar[value.size()]);
        complexCharSeqHelper.insert(any, array);
    }
}

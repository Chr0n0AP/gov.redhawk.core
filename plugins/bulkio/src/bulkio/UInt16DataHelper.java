/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK bulkioInterfaces.
 *
 * REDHAWK bulkioInterfaces is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK bulkioInterfaces is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
/*
 * WARNING: This file is generated from NumericDataHelper.java.template.
 *          Do not modify directly.
 */
package bulkio;

import java.util.Arrays;

class UInt16DataHelper implements DataHelper<short[]> {
    public int elementSize() {
        return 2;
    }

    public int bitSize() {
        return 2 * 8;
    }

    public int arraySize(short[] data) {
        return data.length;
    }

    public short[] emptyArray() {
        return new short[0];
    }

    public short[] slice(short[] data, int start, int end) {
        return Arrays.copyOfRange(data, start, end);
    }
}

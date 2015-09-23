/**
 *  Copyright 2012, 2014 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: onl.netfishers.blt.bgp.netty.protocol.update.AttomicAggregatePathAttribute.java 
 */
package onl.netfishers.blt.bgp.net.attributes;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AtomicAggregatePathAttribute extends PathAttribute {
	
	public AtomicAggregatePathAttribute() {
		super(Category.WELL_KNOWN_DISCRETIONARY);
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.ATOMIC_AGGREGATE;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		return true;
	}

	@Override
	protected int subclassHashCode() {
		return 0;
	}

	@Override
	protected int subclassCompareTo(PathAttribute o) {
		return 0;
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return new ToStringBuilder(this);
	}
}

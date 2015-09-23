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
 */
package onl.netfishers.blt.bgp.net.capabilities;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AutonomousSystem4Capability extends Capability {

	private int autonomousSystem;

	public AutonomousSystem4Capability() {
	}

	public AutonomousSystem4Capability(int autonomousSystem) {
		this.autonomousSystem = autonomousSystem;
	}
	
	/**
	 * @return the autonomousSystem
	 */
	public int getAutonomousSystem() {
		return autonomousSystem;
	}

	/**
	 * @param autonomousSystem the autonomousSystem to set
	 */
	public void setAutonomousSystem(int autonomousSystem) {
		this.autonomousSystem = autonomousSystem;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.net.Capability#orderNumber()
	 */
	@Override
	protected int orderNumber() {
		return ORDER_NUMBER_AS4_CAPABILITY;
	}

	@Override
	protected boolean equalsSubclass(Capability other) {
		return (getAutonomousSystem() == ((AutonomousSystem4Capability)other).getAutonomousSystem());
	}

	@Override
	protected int hashCodeSubclass() {
		return (new HashCodeBuilder()).append(getAutonomousSystem()).toHashCode();
	}

	@Override
	protected int compareToSubclass(Capability other) {
		return (new CompareToBuilder())
				.append(getAutonomousSystem(), ((AutonomousSystem4Capability)other).getAutonomousSystem())
				.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

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
 * File: onl.netfishers.blt.bgp.netty.protocol.UpdateAttributeChecker.java 
 */
package onl.netfishers.blt.bgp.netty.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import onl.netfishers.blt.bgp.net.ASTypeAware;
import onl.netfishers.blt.bgp.net.attributes.ASPathAttribute;
import onl.netfishers.blt.bgp.net.attributes.LocalPrefPathAttribute;
import onl.netfishers.blt.bgp.net.attributes.NextHopPathAttribute;
import onl.netfishers.blt.bgp.net.attributes.OriginPathAttribute;
import onl.netfishers.blt.bgp.net.attributes.PathAttribute;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;
import onl.netfishers.blt.bgp.netty.PeerConnectionInformation;
import onl.netfishers.blt.bgp.netty.PeerConnectionInformationAware;
import onl.netfishers.blt.bgp.netty.protocol.NotificationPacket;
import onl.netfishers.blt.bgp.netty.protocol.update.AttributeFlagsNotificationPacket;
import onl.netfishers.blt.bgp.netty.protocol.update.MalformedAttributeListNotificationPacket;
import onl.netfishers.blt.bgp.netty.protocol.update.MissingWellKnownAttributeNotificationPacket;
import onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodec;
import onl.netfishers.blt.bgp.netty.protocol.update.UpdatePacket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rainer Bieniek (rainer@bgp4j.org)
 *
 */
@PeerConnectionInformationAware
public class UpdateAttributeChecker extends SimpleChannelUpstreamHandler {	
	private static final Logger log = LoggerFactory.getLogger(UpdateAttributeChecker.class);

	
	private Set<Class<? extends PathAttribute>> mandatoryIBGPAttributes = new HashSet<Class<? extends PathAttribute>>();
	private Set<Class<? extends PathAttribute>> mandatoryEBGPAttributes = new HashSet<Class<? extends PathAttribute>>();
	private Map<Class<? extends PathAttribute>, Integer> as2ClazzCodeMap = new HashMap<Class<? extends PathAttribute>, Integer>();
	private Map<Class<? extends PathAttribute>, Integer> as4ClazzCodeMap = new HashMap<Class<? extends PathAttribute>, Integer>();
	
  
	public UpdateAttributeChecker() {
		mandatoryEBGPAttributes.add(OriginPathAttribute.class);
		mandatoryEBGPAttributes.add(ASPathAttribute.class);
		mandatoryEBGPAttributes.add(NextHopPathAttribute.class);

		mandatoryIBGPAttributes.add(OriginPathAttribute.class);
		mandatoryIBGPAttributes.add(ASPathAttribute.class);
		mandatoryIBGPAttributes.add(NextHopPathAttribute.class);
		mandatoryIBGPAttributes.add(LocalPrefPathAttribute.class);
		
		as2ClazzCodeMap.put(ASPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH);
		as2ClazzCodeMap.put(LocalPrefPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF);
		as2ClazzCodeMap.put(NextHopPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP);
		as2ClazzCodeMap.put(OriginPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN);

		as4ClazzCodeMap.put(ASPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH);
		as4ClazzCodeMap.put(LocalPrefPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF);
		as4ClazzCodeMap.put(NextHopPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP);
		as4ClazzCodeMap.put(OriginPathAttribute.class, BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		boolean sentUpstream = false;
		
		if(e.getMessage() instanceof UpdatePacket) {
			PeerConnectionInformation connInfo = (PeerConnectionInformation)ctx.getAttachment();
			UpdatePacket update = (UpdatePacket)e.getMessage();
			List<PathAttribute> attributeFlagsErrorList = new LinkedList<PathAttribute>();
			List<Class<? extends PathAttribute>> missingWellKnownList = new LinkedList<Class<? extends PathAttribute>>();
			Set<Class<? extends PathAttribute>> givenAttributes = new HashSet<Class<? extends PathAttribute>>();
			
			// check if passed optional / transitive bits match the presettings of the attribute type
			for(PathAttribute attribute : update.getPathAttributes()) {
				boolean badAttr = false;

				givenAttributes.add(attribute.getClass());
				
				switch(attribute.getCategory()) {
				case WELL_KNOWN_MANDATORY:
				case WELL_KNOWN_DISCRETIONARY:
					badAttr = attribute.isOptional() || !attribute.isTransitive();
					break;
				case OPTIONAL_NON_TRANSITIVE:
					badAttr = !attribute.isOptional() || attribute.isTransitive();
					break;
				case OPTIONAL_TRANSITIVE:
					badAttr = !attribute.isOptional() || !attribute.isTransitive();
					break;
				}
				
				if(badAttr) {
					log.info("detected attribute " + attribute + " with invalid flags");
					
					attributeFlagsErrorList.add(attribute);
				}
			}
			
			// if we have any bad attribute, generate notification message and leave
			if(attributeFlagsErrorList.size() > 0) {
				NotificationHelper.sendNotification(ctx, 
						new AttributeFlagsNotificationPacket(serializeAttributes(attributeFlagsErrorList)), 
						new BgpEventFireChannelFutureListener(ctx));
			} else {
				// check presence of mandatory attributes
				Set<Class<? extends PathAttribute>> mandatoryAttributes;

				if (connInfo.isIBGPConnection())
					mandatoryAttributes = mandatoryIBGPAttributes;
				else
					mandatoryAttributes = mandatoryEBGPAttributes;

				for (Class<? extends PathAttribute> attrClass : mandatoryAttributes) {
					if (!givenAttributes.contains(attrClass)) {
						missingWellKnownList.add(attrClass);
					}
				}

				if (missingWellKnownList.size() > 0) {
					Map<Class<? extends PathAttribute>, Integer> codeMap;
					List<NotificationPacket> notifications = new LinkedList<NotificationPacket>();

					if(connInfo.isAS4OctetsInUse())
						codeMap = as4ClazzCodeMap;
					else
						codeMap = as2ClazzCodeMap;

					
					if(connInfo.isAS4OctetsInUse())
						codeMap = as4ClazzCodeMap;
					else
						codeMap = as2ClazzCodeMap;
						
					for(Class<? extends PathAttribute> attrClass : missingWellKnownList) {
						int code = codeMap.get(attrClass);
						
						log.info("detected missing well-known atribute, type " + code);
						notifications.add(new MissingWellKnownAttributeNotificationPacket(code));
					}
					
					NotificationHelper.sendNotifications(ctx, 
							notifications, 
							new BgpEventFireChannelFutureListener(ctx));
				} else {
					boolean haveBougsWidth = false;
					
					// check path attributes for AS number width (2 or 4) settings which mismatch the connection configuration
					for(PathAttribute attribute : update.getPathAttributes()) {
						if(attribute instanceof ASTypeAware) {
							if(((ASTypeAware)attribute).getAsType() != connInfo.getAsTypeInUse()) {
								haveBougsWidth = true;
							}
						}
					}
					
					if(haveBougsWidth) {
						NotificationHelper.sendNotification(ctx, 
								new MalformedAttributeListNotificationPacket(), 
								new BgpEventFireChannelFutureListener(ctx));
					} else
						sentUpstream = true;
				}
			}
		} else
			sentUpstream = true;
		
		if(sentUpstream)
	        ctx.sendUpstream(e);
	}

	private byte[] serializeAttributes(List<PathAttribute> attrs) {
		int size = 0;
		
		for(PathAttribute attr : attrs)
			size += PathAttributeCodec.calculateEncodedPathAttributeLength(attr);
		
		ChannelBuffer buffer = ChannelBuffers.buffer(size);
		
		for(PathAttribute attr : attrs)
			buffer.writeBytes(PathAttributeCodec.encodePathAttribute(attr));
		
		byte[] b = new byte[size];
		
		buffer.readBytes(b);
		
		return b;
	}
}

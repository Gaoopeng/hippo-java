package com.pinganfu.hippo.broker.transport.command.handle;

import com.pinganfu.hippo.broker.Broker;
import com.pinganfu.hippo.client.HippoResult;
import com.pinganfu.hippo.network.CommandResult;
import com.pinganfu.hippo.network.command.Command;
import com.pinganfu.hippo.network.transport.nio.CommandHandle;

/**
 * 
 * @author saitxuc
 *
 */
public class UpdateCommandHandle implements CommandHandle{
	
	private Broker broker;
	
	public UpdateCommandHandle(Broker broker) {
		this.broker = broker;
	}
	
	@Override
	public CommandResult doCommand(Command command) throws Exception {
		HippoResult result = broker.processCommand(command);
		return result;
	}
}

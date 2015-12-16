package com.pinganfu.hippo.broker.cluster.simple.master;

import com.pinganfu.hippo.broker.cluster.simple.MSClusterReplicatedFactory;
import com.pinganfu.hippo.broker.cluster.simple.ClusterReplicatedFactoryFinder;
import com.pinganfu.hippo.broker.cluster.simple.ZkRegisterService;
import com.pinganfu.hippo.broker.cluster.zk.StateListener;
import com.pinganfu.hippo.broker.cluster.zk.StateListenerImpl;
import com.pinganfu.hippo.common.lifecycle.LifeCycleSupport;
import com.pinganfu.hippo.store.MigrationEngine;

/**
 * 
 * @author saitxuc
 *
 */
public class MasterService extends LifeCycleSupport {

    private IMasterReplicatedServer masterReplicatedServer;
    
    private ZkRegisterService registerService;
    
    private StateListener stateListener;
    
    public MasterService(MigrationEngine migrationEngine,ZkRegisterService registerService, String replicatedPort) {
        MSClusterReplicatedFactory factory = ClusterReplicatedFactoryFinder.getClusterReplicatedFactory(migrationEngine.getName());
        this.masterReplicatedServer = factory.getReplicatedServe(migrationEngine,replicatedPort);
        this.registerService = registerService;
    }

    @Override
    public void doInit() {
    	this.stateListener = new StateListenerImpl(registerService);
        subscribe();
    	masterReplicatedServer.init();
    }

    @Override
    public void doStart() {
        masterReplicatedServer.start();
    }

    @Override
    public void doStop() {
    	unsubscribe();
    	masterReplicatedServer.stop();
    }
    
    private void subscribe() {
        registerService.subscribeStateChanges(this.stateListener);

    }

    private void unsubscribe() {
        registerService.unsubscribeStateChanges(this.stateListener);
    }
    
}

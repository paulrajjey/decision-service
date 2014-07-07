package com.disney.decision.service.utils;

import javax.enterprise.context.ApplicationScoped;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

@ApplicationScoped
public class BRMSUtil {

    private KieContainer kContainer = null;
    private KieServices kServices ;
    private KieScanner kScanner ;
    public BRMSUtil() {	    
    	
    	System.out.println("BRMSUtil()");
    	
        kServices = KieServices.Factory.get();
    	
    	

		ReleaseId releaseId = kServices.newReleaseId( "com.disney.sales", "sales", "LATEST" );
		
		kContainer = kServices.newKieContainer( releaseId );

		//KieScanner 
		kScanner = kServices.newKieScanner( kContainer );

		// Start the KieScanner polling the maven repository every 10 seconds

		kScanner.start( 10000L );
    }

    
    public StatelessKieSession getStatelessSession() {

        return kContainer.newStatelessKieSession();

    }

    /*
     * KieSession is the new StatefulKnowledgeSession from BRMS 5.3.
     */
    public KieSession getStatefulSession() {

        return kContainer.newKieSession();

    }


	public KieContainer getkContainer() {
		return kContainer;
	}


	public void setkContainer(KieContainer kContainer) {
		this.kContainer = kContainer;
	}


	public KieServices getkServices() {
		return kServices;
	}


	public void setkServices(KieServices kServices) {
		this.kServices = kServices;
	}

}

package com.rule.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.core.command.assertion.AssertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import com.disney.business.domain.Accommodation;
import com.disney.business.domain.Bundle;
import com.disney.business.domain.CommunicationChannel;
import com.disney.business.domain.Component;
import com.disney.business.domain.Dine;
import com.disney.business.domain.Rate;
import com.disney.business.domain.Rates;
import com.disney.business.domain.SalesRequest;
import com.disney.decision.service.utils.AgendaListener;
import com.disney.decision.service.utils.BRMSUtil;

public class RuleTests {

	private BRMSUtil brmsUtil ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		brmsUtil = new BRMSUtil();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testcase1() {
		
		SalesRequest request = new SalesRequest();
		
		//input data
		CommunicationChannel communicationChannel = new CommunicationChannel();
		communicationChannel.setType("test");
		Rates rates = new Rates();
		Rate rate = new Rate();
		rate.setDay(6);
		Rate rate1 = new Rate();
		rate1.setDay(7);
		Accommodation cc= new Accommodation();
		cc.setName("WDW Onsite Resort");
		cc.setLengthOfStay(7);
		request.setAccomodation(cc);
		List<Rate> ratess = new ArrayList<Rate>();
		ratess.add(rate);
		ratess.add(rate1);
		rates.setRates(ratess);;
		request.setRates(rates);
		request.setCommunicationChannel(communicationChannel);
		
		AgendaListener listener = new AgendaListener();
		
		KieRuntimeLogger logger = null;
		
		if ( request != null ) {
			
			KieSession ksession = null;
			try {
							
				ksession = brmsUtil.getStatefulSession();
				KieServices  kieServices = brmsUtil.getkServices();
				 logger =
						  kieServices.getLoggers().newFileLogger(ksession, "/Users/JeyPaulraj/audit");
				 kieServices.getLoggers().newConsoleLogger(ksession);
				 //ewFileLogger(ksession, "/User/JeyPaulraj/
				
				System.out.println(ksession.getKieBase().getKiePackages());
				//String final ruleName= null;
				ksession.addEventListener(listener);
				
				CommunicationChannel channel = request.getCommunicationChannel()	;
				
				List<Rate> ratelist = request.getRates().getRates();
				Accommodation ac = request.getAccomodation();
				//com.dg.validation.spatial.Order factOrder = new com.dg.validation.spatial.Order();
				
				//factOrder.setInvalid(order.isInvalid());
				
				FactHandle handle = ksession.insert(channel);
				ksession.insert(ac);
				System.out.print("size" + ratelist.size());
				for(Rate rt :ratelist){
					ksession.insert(rt);
				}
				ksession.insert(request);
				
				/*for (SpatialAttribute sa : order.getSpatialAttributeList()) {
					
					//com.dg.validation.spatial.SpatialAttribute factSpatialAttribute = new com.dg.validation.spatial.SpatialAttribute();
					//factSpatialAttribute.setAttribute(sa.getAttribute());
					//factSpatialAttribute.setValue(sa.getValue());
					//factSpatialAttribute.setOrder(factOrder);
					
 					//System.out.println("Value: " + factSpatialAttribute.getValue());
					
					//ksession.insert(factSpatialAttribute);
					
				}*/
									
				ksession.fireAllRules();	
				
				/*Collection cl = ksession.getObjects();
				
				for(Object obj : cl){
					
					if ( obj instanceof SalesRequest){
						req= (SalesRequest)obj;
					}
				}*/
				
				//System.out.println("Order Invalid? " + factOrder.getInvalid());
				
				/*if ( factOrder.getInvalid() == true ) {
				
					order.setInvalid(factOrder.getInvalid());
					order.setValidationError(factOrder.getInvalidReason());
					
				}*/
				
			} finally {
				
				if ( ksession != null ) {
					
					ksession.dispose();
					logger.close();
				}
			}	

			
		//fail("Not yet implemented");
	    assertTrue(listener.getRuleName().equals("businessRule1"));
	}
	}

	@Test
	public void testcase2() {
		
		SalesRequest request = new SalesRequest();
		
		CommunicationChannel communicationChannel = new CommunicationChannel();
		communicationChannel.setType("CC");
		Component com = new Component();
		com.setName("DLROR");
		Component com2 = new Component();
		com2.setName("TPT");
		Component com3 = new Component();
		com3.setName("QSDINE");
		Component com4 = new Component();
		com4.setName("CARR");
		
		Dine dine = new Dine();
		request.setDine(dine);
		request.setCommunicationChannel(communicationChannel);
		
		Bundle bundle = new Bundle();
		List<Component> cps = new ArrayList<Component>();
		cps.add(com);
		cps.add(com2);
		cps.add(com3);
		cps.add(com4);
		bundle.setComponents(cps);
		request.setBundle(bundle);
		request.setCommunicationChannel(communicationChannel);
		
		AgendaListener listener = new AgendaListener();
		KieRuntimeLogger logger = null;
		if ( request != null ) {
			
			KieSession ksession = null;
			try {
							
				ksession = brmsUtil.getStatefulSession();
				KieServices  kieServices = brmsUtil.getkServices();
				 logger =
						  kieServices.getLoggers().newFileLogger(ksession, "/User/JeyPaulraj/audit.log");
						  //newFileLogger(ksession, "logdir/mylogfile");

				
				System.out.println(ksession.getKieBase().getKiePackages());
				//String final ruleName= null;
				ksession.addEventListener(listener);
				
				CommunicationChannel channel = request.getCommunicationChannel()	;
				
				//List<Rate> ratelist = request.getRates().getRates();
				Accommodation ac = request.getAccomodation();
				//com.dg.validation.spatial.Order factOrder = new com.dg.validation.spatial.Order();
				
				//factOrder.setInvalid(order.isInvalid());
				
				FactHandle handle = ksession.insert(channel);
				//ksession.insert(ac);
				List<Component> cpms = request.getBundle().getComponents();
				for(Component ls : cpms){
					ksession.insert(ls);
				}
				ksession.insert(dine);
				
				//ksession.insert(request);
									
				ksession.fireAllRules();	
				
				/*Collection cl = ksession.getObjects();
				
				for(Object obj : cl){
					
					if ( obj instanceof SalesRequest){
						req= (SalesRequest)obj;
					}
				}*/
				
				//System.out.println("Order Invalid? " + factOrder.getInvalid());
				
				/*if ( factOrder.getInvalid() == true ) {
				
					order.setInvalid(factOrder.getInvalid());
					order.setValidationError(factOrder.getInvalidReason());
					
				}*/
				
			} finally {
				
				if ( ksession != null ) {
					
					ksession.dispose();
					logger.close();
				}
			}	

			
		//fail("Not yet implemented");
	    assertTrue(listener.getRuleName().equals("businessRule2"));
	}
	}
}

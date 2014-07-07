package com.disney.decision.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import com.disney.business.domain.Accommodation;
import com.disney.business.domain.Booking;
import com.disney.business.domain.Bundle;
import com.disney.business.domain.CommunicationChannel;
import com.disney.business.domain.Component;
import com.disney.business.domain.Dine;
import com.disney.business.domain.Discount;
import com.disney.business.domain.Discounts;
import com.disney.business.domain.Golf;
import com.disney.business.domain.Rate;
import com.disney.business.domain.Rates;
import com.disney.business.domain.SalesRequest;
import com.disney.business.domain.ThemeParkTicket;
import com.disney.business.domain.util.Helper;
import com.disney.decision.service.utils.BRMSUtil;

@WebService(serviceName="DecisionService")
public class DisneySalesService implements DecisionService {

	private BRMSUtil brmsUtil = new BRMSUtil();
	
	@SuppressWarnings("null")
	public SalesRequest processRequest(@WebParam(name="request") SalesRequest request) {
		SalesRequest req = null;
		KieRuntimeLogger logger = null;
		if ( request != null ) {
			
			KieSession ksession = null;
			try {
							
				// get new session from KnowledgeBase
				ksession = brmsUtil.getStatefulSession();
				
				//enable audit logging
				KieServices  kieServices = brmsUtil.getkServices();
				logger = kieServices.getLoggers().newFileLogger(ksession, "/Users/JeyPaulraj/audit");
			
				//Global service that will be used in rule consequence
				Helper helper =new Helper();
				
				ksession.setGlobal("helper", helper);
				
				//Agenda Listener to trace  rule activation and execution  
				ksession.addEventListener(new DefaultAgendaEventListener() {
					public void afterMatchFired(AfterMatchFiredEvent event) {
						System.out.println("###### Fired: "
								+ event.getMatch().getRule().getName());
					}
					
				});
				
				//prepareFact(ksession, request);
				//Prepare fact data 
				
				CommunicationChannel channel = request.getCommunicationChannel()	;
				
				Accommodation accommodation = request.getAccomodation();
				
				Booking booking = request.getBooking();
				Calendar dt = Calendar.getInstance();
				dt.set(2014, 8, 1);
				booking.setBookingdate(dt.getTime());
				
				
				Dine dine =request.getDine();
				
				ThemeParkTicket ticket = request.getThemeParkTicket();
				
				List<Rate> rates = request.getRates().getRates();
				
				
				Golf golf = request.getGolf();
				
				Bundle bundle = request.getBundle();
			
				//insert all data to ksession
				
				ksession.insert(request);
				
				ksession.insert(channel);
				
				ksession.insert(accommodation);
				
				ksession.insert(booking);
				
				ksession.insert(dine)	;	
			
				
				//
				if(ticket!=null){
					ksession.insert(ticket);
				}				
				//insert golf 
				if(golf!=null){
					ksession.insert(golf);
				}	
				//insert rates into sesssion
				for(Rate rate :rates){
					ksession.insert(rate);
				}
				
				//Insert all component to session
				if ( bundle != null){
					List<Component> componentlist = bundle.getComponents();
					for(Component compone :componentlist){
						ksession.insert(compone);
					}
				}
				ksession.fireAllRules();
				
				QueryResults requestresult = ksession.getQueryResults ("getRequest" );
				System.out.println ("Number of discount objects: " + requestresult.size());
				
				for (QueryResultsRow re : requestresult) {
					req = (SalesRequest) re.get("request");
					System.out.println("discounts is: " +  req.toString());
					  
				}
				
				
				List<Discount> dics = new ArrayList<Discount>();
				QueryResults results = ksession.getQueryResults ("get discounts" );
				System.out.println ("Number of discount objects: " + results.size());
				for (QueryResultsRow qresult : results) {
					Discount dis = (Discount) qresult.get("discount");
					System.out.println("discounts is: " +  dis.toString());
					dics.add(dis);
						  
				}
				
				if(dics.size() > 0){
					Discounts ds = req.getDiscoiunts();
					if(ds == null){
						ds = new Discounts();
					}
				
					ds.setDiscounts(dics);
					req.setDiscoiunts(ds);
				}		
				
				//getting results from session
				//getResultsFromEngine(ksession, req);
				//Prepare fact data 
				
			
				
			//session clean up at end of the execution	
			} finally {
				
				if ( ksession != null ) {
					
					ksession.dispose();
					logger.close();
				}
			}	

		}
		System.out.println(req.toString());
		return req;
	}
	
	/*public void prepareFact(KieSession ksession,SalesRequest request){
		//Prepare fact data 
		
		CommunicationChannel channel = request.getCommunicationChannel()	;
		
		Accommodation accommodation = request.getAccomodation();
		
		Booking booking = request.getBooking();
		Calendar dt = Calendar.getInstance();
		dt.set(2014, 8, 1);
		booking.setBookingdate(dt.getTime());
		
		
		Dine dine =request.getDine();
		
		ThemeParkTicket ticket = request.getThemeParkTicket();
		
		List<Rate> rates = request.getRates().getRates();
		
		
		Golf golf = request.getGolf();
		
		Bundle bundle = request.getBundle();
	
		//insert all data to ksession
		
		ksession.insert(request);
		
		ksession.insert(channel);
		
		ksession.insert(accommodation);
		
		ksession.insert(booking);
		
		ksession.insert(dine)	;	
	
		
		//
		if(ticket!=null){
			ksession.insert(ticket);
		}				
		//insert golf 
		if(golf!=null){
			ksession.insert(golf);
		}	
		//insert rates into sesssion
		for(Rate rate :rates){
			ksession.insert(rate);
		}
		
		//Insert all component to session
		if ( bundle != null){
			List<Component> componentlist = bundle.getComponents();
			for(Component compone :componentlist){
				ksession.insert(compone);
			}
		}
	}
	
	public void getResultsFromEngine(KieSession ksession , SalesRequest req){
		QueryResults requestresult = ksession.getQueryResults ("getRequest" );
		System.out.println ("Number of discount objects: " + requestresult.size());
		
		for (QueryResultsRow re : requestresult) {
			req = (SalesRequest) re.get("request");
			System.out.println("discounts is: " +  req.toString());
			  
		}
		
		
		List<Discount> dics = new ArrayList<Discount>();
		QueryResults results = ksession.getQueryResults ("get discounts" );
		System.out.println ("Number of discount objects: " + results.size());
		for (QueryResultsRow qresult : results) {
			Discount dis = (Discount) qresult.get("discount");
			System.out.println("discounts is: " +  dis.toString());
			dics.add(dis);
				  
		}
		
		if(dics.size() > 0){
			Discounts ds = req.getDiscoiunts();
			if(ds == null){
				ds = new Discounts();
			}
		
			ds.setDiscounts(dics);
			req.setDiscoiunts(ds);
		}
		
	}*/
	public static void main(String [] req){
	
		DisneySalesService service = new DisneySalesService();
		SalesRequest request = new SalesRequest();
		CommunicationChannel communicationChannel = new CommunicationChannel();
		communicationChannel.setType("INTER");
		Rates rates = new Rates();
		Rate rate = new Rate();
		rate.setDay(6);
		Rate rate1 = new Rate();
		rate1.setDay(7);
		Accommodation cc= new Accommodation();
		//cc.setName("WDW Onsite Resort");
		cc.setName("California Area Hotel");
		
		cc.setLengthOfStay(7);
		request.setAccomodation(cc);
		List<Rate> ratess = new ArrayList<Rate>();
		ratess.add(rate);
		ratess.add(rate1);
		rates.setRates(ratess);;
		request.setRates(rates);
		request.setCommunicationChannel(communicationChannel);
		Booking booking = new Booking();
		Calendar dt = Calendar.getInstance();
		dt.set(2014, 8, 1);
		booking.setBookingdate(dt.getTime());
		
		request.setBooking(booking);
		
		ThemeParkTicket tick = new ThemeParkTicket();
		tick.setDestination("California");
		Golf golf = new Golf();
		request.setGolf(golf);
		request.setThemeParkTicket(tick);
		service.processRequest(request);
		
		System.out.println("has errors" + request.hasErrors() + "" + rate1.getRate());
		
	}

}

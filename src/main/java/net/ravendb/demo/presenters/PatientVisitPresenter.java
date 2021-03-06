package net.ravendb.demo.presenters;

import java.util.Collection;

import net.ravendb.client.documents.session.IDocumentQuery;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.demo.command.PatientVisit;
import net.ravendb.demo.db.RavenDBDocumentStore;
import net.ravendb.demo.model.Condition;
import net.ravendb.demo.model.Doctor;
import net.ravendb.demo.model.Patient;
import net.ravendb.demo.model.Visit;
import net.ravendb.demo.presenters.PatientVisitViewable.PatientVisitViewListener;

public class PatientVisitPresenter implements PatientVisitViewListener {

	private IDocumentSession session;
	public PatientVisitPresenter() {

	}

//	@Override
//	public Collection<PatientVisit> getVisistsList(boolean order) {
//		try (IDocumentSession session = RavenDBDocumentStore.INSTANCE.getStore().openSession()) {
//			
//			IDocumentQuery<PatientVisit> visits = session.advanced().documentQuery(Patient.class).
//		    		groupBy("visits[].doctorName","visits[].date","visits[].conditionSummery")
//		    		.selectKey("visits[].doctorName", "doctorName")
//		    		.selectKey("visits[].date", "date")
//		    		.selectKey("visits[].conditionSummery","conditionSummery")		    	
//		    		.selectCount()
//		    		.ofType(PatientVisit.class)
//		    		.whereNotEquals("date",null);
//		    
//			if(order){
//	    		return visits.orderByDescending("date").toList();
//		    }else{
//		    	return visits.orderBy("date").toList();
//		    }
//
//		}
//	}

	@Override
	public Collection<PatientVisit> getVisistsList(String patientId,String term,boolean order) {
			session.advanced().eagerly().executeAllPendingLazyOperations();
			Patient patient=session.load(Patient.class, patientId);

			IDocumentQuery<PatientVisit> visits = session.query(Patient.class).waitForNonStaleResults()							    		
					.groupBy("visits[].doctorName","visits[].date","visits[].type","visits[].conditionId","firstName","lastName","visits[].visitSummery")
		    		.selectKey("visits[].doctorName", "doctorName")
		    		.selectKey("visits[].date", "date")
		    		.selectKey("visits[].visitSummery","visitSummery")
		    		.selectKey("firstName", "firstName")
		    		.selectKey("lastName", "lastName")	
		    		.selectKey("visits[].type", "type")
		    		.selectKey("visits[].conditionId", "conditionId")
		    		.selectCount()		    		
		    		.ofType(PatientVisit.class)
		    		.whereNotEquals("date",null)
		    		.whereEquals("firstName",patient.getFirstName())
		    		.whereEquals("lastName", patient.getLastName());	
		    		
		    	
		    if(term!=null){
		    	visits.whereStartsWith("doctorName", term);
		    }
		    if(order){
	    		return visits.orderByDescending("date").toList();
		    }else{
		    	return visits.orderBy("date").toList();
		    }
			
			
			
		 	
//			IDocumentQuery<Patient> q=session.query(Patient.class).include("visits[].doctorId")
//					.whereEquals("id", patientId);
//			
//			if(term!=null){
//				q.search("visits[].doctorName", term);
//			}else{
//				
//			}
//			Patient patient=q.first();
			
//			IDocumentQuery<Visit> visits = session.query(Patient.class).whereEquals("Id",patientId).
//		    		groupBy("visits[].doctorName","visits[].date","visits[].doctorId","visits[].type","visits[].conditionSummery","id")
//		    		.selectKey("visits[].doctorName", "doctorName")
//		    		.selectKey("visits[].date", "date")
//		    		.selectKey("visits[].conditionSummery","conditionSummery")
//		    		.selectKey("visits[].doctorId", "doctorId")
//		    		.selectKey("id", "id")
//		    		.selectCount()
//		    		//.whereEquals("id", patientId)
//		    		.ofType(Visit.class)		    		
//		    		.include("doctorId");		    		
//		    		
//		    if(order){
//	    		return visits.orderByDescending("date").toList();
//		    }else{
//		    	return visits.orderBy("date").toList();
//		    }
		    
			//fetch doctors by batch  
//	 	    Set<String> doctorIds=patient.getVisits().stream().map(v->v.getDoctorId()).collect(Collectors.toSet());
//			Map<String,Doctor> map= session.load(Doctor.class,doctorIds);
//			   
//            for (Visit visit : patient.getVisits()) {			
//				visit.setDoctor(map.get(visit.getDoctorId()));				
//			}            
//            
//			return patient.getVisits();

	}

	@Override
	public void save(String patientId, Visit visit) {
		

			 Patient patient=session.load(Patient.class, patientId);			
             patient.getVisits().add(visit);
             session.store(patient);
			 session.saveChanges();
			 session.advanced().eagerly().executeAllPendingLazyOperations();
			
		
	}

	@Override
	public Patient getPatientById(String id) {

			Patient patient = session.load(Patient.class, id);
			return patient;
		

	}

	@Override
	public Collection<Doctor> getDoctorsList() {

			return session.query(Doctor.class).distinct().toList();
		

	}
	@Override
	public Collection<Condition> getConditionsList() {

			return session.query(Condition.class).distinct().toList();
		
	}
	@Override
	public Condition getConditionById(String conditionId) {

			return session.load(Condition.class,conditionId);
		
	}
	
	@Override
	public void openSession() {
		if(session==null){
			  session = RavenDBDocumentStore.INSTANCE.getStore().openSession();
		}
	}

	@Override
	public void releaseSession() {
		session.close();
	}
}

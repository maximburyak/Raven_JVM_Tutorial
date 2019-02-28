package net.ravendb.demo.presenters;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.ravendb.client.documents.queries.IndexQuery;
import net.ravendb.client.documents.session.IDocumentQuery;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.demo.command.PatientVisit;
import net.ravendb.demo.db.RavenDBDocumentStore;
import net.ravendb.demo.model.Condition;
import net.ravendb.demo.model.Patient;
import net.ravendb.demo.model.Visit;
import net.ravendb.demo.model.Visit.Type;
import net.ravendb.demo.presenters.ConditionViewable.ConditionViewListener;

public class ConditionPresenter implements ConditionViewListener {
	private final ConditionViewable view;

	public ConditionPresenter(ConditionViewable view) {
		this.view = view;
	}

	@Override
	public void delete(Condition condition) {
			 try (IDocumentSession session = RavenDBDocumentStore.INSTANCE.getStore().openSession()) {
		           session.delete(condition.getId());
		           session.saveChanges();
		     }			
	}
	@Override
	public Patient getPatientById(String id) {
		try (IDocumentSession session = RavenDBDocumentStore.INSTANCE.getStore().openSession()) {
			Patient patient = session.load(Patient.class, id);
			return patient;
		}

	}

	@Override
	public void save(Condition condition) {
		try (IDocumentSession session = RavenDBDocumentStore.INSTANCE.getStore().openSession()) {

			session.advanced().clear(); //todo: we don't need tho clear in the session
			session.store(condition);
			session.saveChanges();
		}
	}

	//todo: do we need this?
	@Override
	public Condition getConditionById(String id) {	
		return null;
	}

	@Override
	public Collection<Condition> getConditionsList(int offset, int limit, String term) {
		try (IDocumentSession session = RavenDBDocumentStore.INSTANCE.getStore().openSession()) {
			IDocumentQuery<Condition> conditions = session.advanced().documentQuery(Condition.class).include("patientId");
				
			if(term!=null){
				conditions.whereStartsWith("description", term);
			}
    		conditions.skip(offset)
    		.take(limit);
		
    		return conditions.toList(); 
		}
	}

	// todo: as in other places, let's avoid having a seperate call for count
	@Override
	public int getConditionsCount(String term) {
		try (IDocumentSession session = RavenDBDocumentStore.INSTANCE.getStore().openSession()) {
			IDocumentQuery<Condition> conditions = session.advanced().documentQuery(Condition.class);
				
			if(term!=null){
				conditions.whereStartsWith("description", term);
			}
    		
			
    		return conditions.count();

		}
	}
}

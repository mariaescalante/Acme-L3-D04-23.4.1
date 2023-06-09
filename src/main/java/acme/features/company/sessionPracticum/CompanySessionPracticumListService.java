
package acme.features.company.sessionPracticum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.SessionPracticum;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanySessionPracticumListService extends AbstractService<Company, SessionPracticum> {

	@Autowired
	protected CompanySessionPracticumRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Practicum practicum;

		masterId = super.getRequest().getData("id", int.class);
		practicum = this.repository.findPracticumById(masterId);
		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<SessionPracticum> objects;
		int practicumId;

		practicumId = super.getRequest().getData("id", int.class);
		objects = this.repository.findPracticumSessionsByPracticumId(practicumId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final SessionPracticum object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "startDate", "endDate", "correction");

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<SessionPracticum> objects) {
		assert objects != null;

		int practicumId;
		Practicum practicum;
		final boolean showCreate;
		final boolean exceptional;

		practicumId = super.getRequest().getData("id", int.class);
		practicum = this.repository.findPracticumById(practicumId);
		showCreate = super.getRequest().getPrincipal().hasRole(practicum.getCompany());
		exceptional = practicum.isDraftMode();

		super.getResponse().setGlobal("id", practicumId);

		super.getResponse().setGlobal("exceptional", exceptional);
		super.getResponse().setGlobal("showCreate", showCreate);
	}

}

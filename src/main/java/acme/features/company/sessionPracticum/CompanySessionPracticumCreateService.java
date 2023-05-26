
package acme.features.company.sessionPracticum;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.SessionPracticum;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanySessionPracticumCreateService extends AbstractService<Company, SessionPracticum> {

	@Autowired
	protected CompanySessionPracticumRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(practicumId);
		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SessionPracticum object;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(practicumId);

		object = new SessionPracticum();
		object.setPracticum(practicum);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final SessionPracticum object) {
		assert object != null;

		super.bind(object, "title", "abstract$", "startDate", "endDate", "furtherInformationLink");
	}

	@Override
	public void validate(final SessionPracticum object) {
		assert object != null;
		Date date;
		boolean confirmation;

		confirmation = object.getPracticum().isDraftMode() ? true : super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
		if (object.getEndDate() != null && object.getStartDate() != null) {
			if (!super.getBuffer().getErrors().hasErrors("endDate"))
				super.state(object.getStartDate().before(object.getEndDate()), "endDate", "company.practicum.form.error.start-before-end");

			if (!super.getBuffer().getErrors().hasErrors("startDate")) {
				date = CompanySessionPracticumCreateService.sumarDiasAFecha(MomentHelper.getCurrentMoment(), 7);
				super.state(object.getStartDate().after(date) || object.getStartDate().equals(date), "startDate", "company.practicum.form.error.least-one-week-ahead");
			}

			if (!super.getBuffer().getErrors().hasErrors("endDate")) {
				date = CompanySessionPracticumCreateService.sumarDiasAFecha(object.getStartDate(), 7);
				super.state(object.getEndDate().after(date) || object.getEndDate().equals(date), "endDate", "company.practicum.form.error.least-one-week-long");
			}
		}
	}

	@Override
	public void perform(final SessionPracticum object) {
		assert object != null;
		object.setCorrection(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final SessionPracticum object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstract$", "startDate", "endDate", "furtherInformationLink");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("draftMode", object.getPracticum().isDraftMode());
		tuple.put("confirmation", false);

		super.getResponse().setData(tuple);
		tuple.put("confirmation", false);
	}

	public static Date sumarDiasAFecha(final Date fecha, final int dias) {
		if (dias == 0)
			return fecha;
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}

}

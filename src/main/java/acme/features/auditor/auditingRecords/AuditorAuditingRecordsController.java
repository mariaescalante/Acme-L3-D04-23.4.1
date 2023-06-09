
package acme.features.auditor.auditingRecords;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.AuditingRecords;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditorAuditingRecordsController extends AbstractController<Auditor, AuditingRecords> {

	@Autowired
	protected AuditorAuditingRecordsListService				listService;

	@Autowired
	protected AuditorAuditingRecordsShowService				showService;

	@Autowired
	protected AuditorAuditingRecordsCreateService			createService;

	@Autowired
	protected AuditorAuditingRecordsCreateCorrectionService	createCorrectionService;

	@Autowired
	protected AuditorAuditingRecordsUpdateService			updateService;

	@Autowired
	protected AuditorAuditingRecordsDeleteService			deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("create-correction", "create", this.createCorrectionService);
	}

}

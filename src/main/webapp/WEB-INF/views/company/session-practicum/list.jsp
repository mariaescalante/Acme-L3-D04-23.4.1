<%--
- list.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="company.session-practicum.list.label.exceptional" path="correction" width="10%"/>
	<acme:list-column code="company.session-practicum.list.label.title" path="title" width="50%"/>	
	<acme:list-column code="company.session-practicum.list.label.start-date" path="startDate" width="20%"/>
	<acme:list-column code="company.session-practicum.list.label.end-date" path="endDate" width="20%"/>
</acme:list>

<acme:button test="${showCreate && exceptional}" code="company.session-practicum.list.button.create" action="/company/session-practicum/create?masterId=${id}"/>
<acme:button test="${showCreate && !exceptional}" code="company.session-practicum.list.button.create-exceptional" action="/company/session-practicum/create-correction?masterId=${id}"/>
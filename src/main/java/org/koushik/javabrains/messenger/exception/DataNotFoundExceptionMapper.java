package org.koushik.javabrains.messenger.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.koushik.javabrains.messenger.model.ErrorMessage;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException ex) {
		ErrorMessage errormMssage = new ErrorMessage(ex.getMessage(), 404, "https://en.wikipedia.org/wiki/HTTP_404");
		return Response.status(Status.NOT_FOUND).entity(errormMssage).build();
	}

}

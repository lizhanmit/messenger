package org.koushik.javabrains.messenger.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.koushik.javabrains.messenger.model.Message;
import org.koushik.javabrains.messenger.resources.beans.MessageFilterBean;
import org.koushik.javabrains.messenger.service.MessageService;

@Path("/messages") // gate annotation
@Consumes(MediaType.APPLICATION_JSON) // use @Consumes to specify the expected request body format
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class MessageResource {

	private MessageService messageService = new MessageService();
	
	@GET
	public List<Message> getAllMessages(@BeanParam MessageFilterBean filterBean) { // if there isn't "?year=" in the request url, the year parameter equals zero. So you can getAllMessagesForYear and getAllMessages in one method
		if (filterBean.getYear() > 0) {
			return messageService.getAllMessagesForYear(filterBean.getYear());
		}
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		return messageService.getAllMessages();
	}
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(id);
		message.addLinks(getUriForSelf(uriInfo, message), "self");
		message.addLinks(getUriForProfile(uriInfo, message), "profile");
		message.addLinks(getUriForComments(uriInfo, message), "comments");
		return message;
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()  // http://localhost:8080/messenger/webapi
				.path(MessageResource.class)  // /messages
				.path(MessageResource.class, "getCommentResource")  // /{messageId}/comments
				.path(CommentResource.class)  // /
				.resolveTemplate("messageId", message.getId())  // replace {messageId} template to real pathparam messageId 
				.build()
				.toString();
		return uri;
	}

	private String getUriForProfile(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()  // http://localhost:8080/messenger/webapi
				.path(ProfileResource.class)  // /profiles
				.path(message.getAuthor())  // /{authorName}
				.build()
				.toString();
		return uri;
	}

	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()  // http://localhost:8080/messenger/webapi
							.path(MessageResource.class)  // /messages
							.path(String.valueOf(message.getId()))  // /{messageId}
							.build()
							.toString();
		return uri;
	}
	
	@POST
	public Response addMessage(Message message, @Context UriInfo uriInfo) {
		Message newMessage = messageService.addMessage(message);
		String newMessageId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newMessageId).build(); // build the location header
		return Response.created(uri).entity(newMessage).build(); // send status code 201 instead of 200 for created, and the location header
		// return messageService.addMessage(message);
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long id, Message message) {
		message.setId(id);
		return messageService.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long id) {
		messageService.removeMessage(id);
	}
	
	// if all http methods call the same method here, you do not need to write http method annotations
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() { // return the sub resource
		return new CommentResource();
	}
}

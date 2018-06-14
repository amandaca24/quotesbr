package main.java.com.amanda.quotesbr.resources;

import main.java.com.amanda.quotesbr.model.domain.Quotes;
import main.java.com.amanda.quotesbr.model.domain.Tema;
import main.java.com.amanda.quotesbr.resources.beans.QuoteFilterBean;
import main.java.com.amanda.quotesbr.services.QuotesService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/quotes")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class QuotesResource {

    private QuotesService service = new QuotesService();

    @GET
    public List<Quotes> getQuotes(@BeanParam QuoteFilterBean quoteFilter) {
        if ((quoteFilter.getOffset() >= 0) && (quoteFilter.getLimit() > 0)) {
            return service.getQuotesByPagination(quoteFilter.getOffset(), quoteFilter.getLimit());
        }
        if (quoteFilter.getName() != null) {
            return service.getQuoteByAuthor(quoteFilter.getName());
        }

        return service.getQuotes();
    }

    @GET
    @Path("{quoteId}")
    public Quotes getQuotes(@PathParam("quoteId") long id) {
        return service.getQuote(id);
    }

    @POST
    public Response save(Quotes quote) {
        quote = service.saveQuote(quote);
        return Response.status(Response.Status.CREATED)
                .entity(quote)
                .build();
    }

    @GET
    @Path("{name}")
    public List<Quotes> getQuoteByQuote(@PathParam("name") String name){
        return service.getQuoteByQuote(name);

    }

    @GET
    @Path("{tema}")
    public List<Quotes> getQuoteByQuote(@PathParam("tema") Tema tema){
        return service.getQuoteByTheme(tema);

    }

    @PUT
    @Path("{quoteId}")
    public void update(@PathParam("quoteId") long id, Quotes quote) {
        quote.setId(id);
        service.updateQuote(quote);
    }

    @DELETE
    @Path("{quoteId}")
    public void delete(@PathParam("quoteId") long id) {
        service.deleteQuote(id);
    }


}

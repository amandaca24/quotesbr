package main.java.com.amanda.quotesbr.services;

import main.java.com.amanda.quotesbr.model.dao.QuotesDAO;
import main.java.com.amanda.quotesbr.model.domain.Quotes;
import main.java.com.amanda.quotesbr.model.domain.Tema;

import java.util.List;

public class QuotesService {

    private QuotesDAO dao = new QuotesDAO();

    public List<Quotes> getQuotes(){
        return dao.getAll();
    }

    public Quotes getQuote(Long id){
        return dao.getById(id);
    }

    public Quotes saveQuote(Quotes quote){
        return dao.save(quote);
    }

    public Quotes updateQuote(Quotes quote){
        return dao.update(quote);
    }

    public Quotes deleteQuote(Long id){
        return dao.delete(id);
    }

    public List<Quotes> getQuotesByPagination(int firstResult, int maxResults){
        return dao.getPagination(firstResult, maxResults);
    }

    public List<Quotes> getQuoteByAuthor(String name){
        return dao.getByAuthor(name);
    }

    public List<Quotes> getQuoteByQuote(String name){
        return dao.getByQuote(name);
    }

    public List<Quotes> getQuoteByTheme(Tema tema){
        return dao.getByTheme(tema);
    }

}

package main.java.com.amanda.quotesbr.model.dao;

import main.java.com.amanda.quotesbr.exceptions.DAOException;
import main.java.com.amanda.quotesbr.exceptions.ErrorCode;
import main.java.com.amanda.quotesbr.model.domain.Quotes;
import main.java.com.amanda.quotesbr.model.domain.Tema;

import javax.persistence.EntityManager;
import java.util.List;

public class QuotesDAO {

    //Listar todas as citações

    public List<Quotes> getAll(){
        EntityManager em = JPAUtil.getEntityManager();
        List<Quotes> quotes = null;

        try{
            quotes = em.createQuery("SELECT q FROM Quotes q", Quotes.class).getResultList();
        }catch (RuntimeException ex) {
            throw new DAOException("Erro ao recuperar as citações do banco: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return quotes;
    }

    public Quotes getById(Long id){
        EntityManager em = JPAUtil.getEntityManager();
        Quotes quote = null;

        if (id <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }
        try {
            quote = em.find(Quotes.class, id);
        }catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar citação por id no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        if(quote == null){
            throw new DAOException("Citação de id " + id + " não existe.", ErrorCode.NOT_FOUND.getCode());
        }

        return quote;
    }

    public Quotes save(Quotes quote){
        EntityManager em = JPAUtil.getEntityManager();

        if(!quoteIsValid(quote)){
            throw new DAOException("Citação com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            em.getTransaction().begin();
            em.persist(quote);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao salvar citação no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return quote;
    }

    public Quotes update(Quotes quote){
        EntityManager em = JPAUtil.getEntityManager();
        Quotes quoteManaged = null;

        if(quote.getId()<=0){
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }
        if(!quoteIsValid(quote)){
            throw new DAOException("Citação com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        try{
            em.getTransaction().begin();
            quoteManaged = em.find(Quotes.class, quote.getId());
            quoteManaged.setAuthor(quote.getAuthor());
            quoteManaged.setQuote(quote.getSource());
            quoteManaged.setSource(quote.getSource());
            quoteManaged.setTema(quote.getTema());
            em.getTransaction().commit();
        }catch (NullPointerException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Citação informada para atualização não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao atualizar citação no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return quoteManaged;
    }

    public Quotes delete(Long id){
        EntityManager em = JPAUtil.getEntityManager();
        Quotes quote = null;

        if (id <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }

        try{
            em.getTransaction().begin();
            quote = em.find(Quotes.class, id);
            em.remove(quote);
            em.getTransaction().commit();
        }catch (IllegalArgumentException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Citação informada para remoção não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao remover citação do banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }

        return quote;

    }

    public List<Quotes> getPagination(int firstResult, int maxResults){
        List<Quotes> quotes;
        EntityManager em = JPAUtil.getEntityManager();

        try{
            quotes = em.createQuery("select q from Quotes q", Quotes.class)
                    .setFirstResult(firstResult - 1)
                    .setMaxResults(maxResults)
                    .getResultList();
        }catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar produtos no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        if(quotes.isEmpty()){
            throw new DAOException("Página com produtos vazia.", ErrorCode.NOT_FOUND.getCode());
        }

        return quotes;

    }

    public List<Quotes> getByAuthor(String name){
        EntityManager em = JPAUtil.getEntityManager();
        List<Quotes> quotes = null;

        try {
            quotes = em.createQuery("select q from Quotes q where q.author like :name", Quotes.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar autores por nome no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        if(quotes.isEmpty()){
            throw new DAOException("Página com autores vazia.", ErrorCode.NOT_FOUND.getCode());
        }

        return quotes;
    }

    public List<Quotes> getByQuote(String name){
        EntityManager em = JPAUtil.getEntityManager();
        List<Quotes> quotes;

        try {
            quotes = em.createQuery("select q from Quotes q where q.quote like :name", Quotes.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar citações por palavra-chave no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        if(quotes.isEmpty()){
            throw new DAOException("Página com citações vazia.", ErrorCode.NOT_FOUND.getCode());
        }

        return quotes;
    }

    public List<Quotes> getByTheme(Tema tema){
        EntityManager em = JPAUtil.getEntityManager();
        List<Quotes> quotes;

        try {
            quotes = em.createQuery("select q from Quotes q where q.tema like :tema", Quotes.class)
                    .setParameter("tema", "%" + tema + "%")
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao buscar citações por tema no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        } finally {
            em.close();
        }
        if(quotes.isEmpty()){
            throw new DAOException("Página com citações por tema vazia.", ErrorCode.NOT_FOUND.getCode());
        }

        return quotes;
    }

    private boolean quoteIsValid(Quotes quote) {
        try {
            if ((quote.getAuthor().isEmpty()) || (quote.getQuote().isEmpty() || (quote.getSource().isEmpty()) || (quote.getTema() == null) ))
                return false;
        } catch (NullPointerException ex) {
            throw new DAOException("Citação com dados incompletos.", ErrorCode.BAD_REQUEST.getCode());
        }

        return true;
    }
}

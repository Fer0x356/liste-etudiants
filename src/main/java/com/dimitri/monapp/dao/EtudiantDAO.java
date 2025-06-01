package com.dimitri.monapp.dao;

import com.dimitri.monapp.models.Etudiant;
import com.dimitri.monapp.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EtudiantDAO {
    public List<Etudiant> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Simple requête HQL (Hibernate Query Language)
            return session.createQuery("from Etudiant", Etudiant.class).list();
        }
    }

    // On pourra ajouter plus tard des méthodes pour ajouter, modifier FAIT, supprimer...
    public void update(Etudiant etudiant){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(etudiant);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void save(Etudiant etudiant){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(etudiant);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Etudiant etudiant){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(etudiant);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Etudiant findByNomPrenom(String nom, String prenom) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Etudiant where nom = :nom and prenom = :prenom", Etudiant.class)
                    .setParameter("nom", nom)
                    .setParameter("prenom", prenom)
                    .uniqueResult();
        }
    }
}

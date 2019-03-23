package de.eschwank.backend.database

import org.hibernate.HibernateException
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.io.Serializable


object HibernateUtil {

    private val sessionFactory: SessionFactory

    init {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = Configuration().configure().buildSessionFactory()
        } catch (ex: Throwable) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed.$ex")
            throw ExceptionInInitializerError(ex)
        }
    }

    private fun exec(f: (Session) -> Any): Any? {
        val session = sessionFactory.openSession()
        var ret: Any? = null
        try {
            val tx = session.beginTransaction()
            ret = f(session)
            session.transaction.commit()
            if (tx.isActive) {
                tx.commit()
            }
        } catch (e: HibernateException) {
        } finally {
            session.close()
        }
        return ret
    }

    fun save(obj: Any) {
        exec { session -> session.saveOrUpdate(obj) }
    }

    fun <T> query(query: String, clazz: Class<T>, offset: Int = -1, limit: Int = -1): List<*>? {
        return exec { session ->
            val q = session.createQuery(query, clazz)
            if (offset >= 0) {
                q.firstResult = offset
            }
            if (limit >= 0) {
                q.maxResults = limit
            }
            q.list()
        } as List<*>?
    }

    fun delete(obj: Any) {
        exec { session -> session.delete(obj) }
    }
}
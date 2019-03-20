package de.eschwank.backend.entities

import de.eschwank.backend.database.HibernateUtil
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractEntity : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    fun save() {
        HibernateUtil.save(this)
    }

    fun delete() {
        HibernateUtil.delete(this)
    }

    companion object {
        fun <T: Any?> query(query: String) : List<AbstractEntity>? {
            return HibernateUtil.query(query, AbstractEntity::class.java) as List<AbstractEntity>
        }

        fun <T: Any?> all() : List<AbstractEntity>? {
            return HibernateUtil.query("SELECT * FROM ${AbstractEntity::class.java};", AbstractEntity::class.java) as List<AbstractEntity>
        }
    }
}
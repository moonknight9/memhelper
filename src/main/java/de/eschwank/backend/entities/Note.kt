package de.eschwank.backend.entities

import de.eschwank.backend.database.HibernateUtil
import java.time.LocalDateTime
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType

@Entity
class Note() : AbstractEntity() {
    var title: String = ""
    var content: String = ""

    var isArchived = false

    var creationTimeStamp: LocalDateTime = LocalDateTime.now()

    @ElementCollection(fetch = FetchType.EAGER)
    var tags: List<String> = ArrayList()

    constructor(title: String, content:String) : this() {
        this.title = title
        this.content = content
    }

    companion object {
        fun all() : List<Note>? {
            // TODO: find a better solution here
            return HibernateUtil.query("FROM Note", Note::class.java) as List<Note>
        }
    }
}

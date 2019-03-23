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

    var creationTimeStamp: LocalDateTime = LocalDateTime.now()
    var lastUpdate: LocalDateTime = creationTimeStamp

    @ElementCollection(fetch = FetchType.EAGER)
    var tags: List<String> = ArrayList()

    constructor(title: String, content:String) : this() {
        this.title = title
        this.content = content
    }

    companion object {
        fun all(offset:Int = -1, limit:Int = -1) : List<Note>? {
            // TODO: find a better solution here
            return HibernateUtil.query("FROM Note", Note::class.java, offset, limit) as List<Note>
        }
    }
}

package de.eschwank.backend.entities

import java.time.LocalDateTime
import java.util.Arrays

class NoteService private constructor() {

    companion object {
        val INSTANCE: NoteService
            get() = SingletonHolder.INSTANCE
    }

    val all: List<Note>
        get() {
            val n1 = Note("Title 1", "n1")
            val n2 = Note("Title 2", "n2")
            val n3 = Note("Title 3", "n3")
            return Arrays.asList(n1, n2, n3)
        }

    /**
     * Helper class to initialize the singleton Service in a thread-safe way and
     * to keep the initialization ordering clear between the two services. See
     * also: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
     */
    private object SingletonHolder {
        internal val INSTANCE = createNoteService()

        private fun createNoteService(): NoteService {
            return NoteService()
        }
    }
    /** This class is not meant to be instantiated.  */
}

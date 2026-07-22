// -------------------- Entity --------------------

data class Note(
    val id: Int,
    val title: String,
    val description: String
)


// -------------------- DAO --------------------

interface NoteDao {

    fun insert(note: Note)

    fun getAllNotes(): List<Note>

    fun searchNotes(query: String): List<Note>
}


// -------------------- DAO Implementation --------------------

class NoteDaoImpl : NoteDao {

    private val notes = mutableListOf<Note>()


    override fun insert(note: Note) {
        notes.add(note)
    }


    override fun getAllNotes(): List<Note> {
        return notes
    }


    override fun searchNotes(query: String): List<Note> {

        return notes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
    }
}


// -------------------- Repository --------------------

class NoteRepository(private val dao: NoteDao) {


    fun addNote(note: Note) {
        dao.insert(note)
    }


    fun getNotes(): List<Note> {
        return dao.getAllNotes()
    }


    fun searchNotes(query: String): List<Note> {
        return dao.searchNotes(query)
    }
}


// -------------------- ViewModel --------------------

class NoteViewModel(private val repository: NoteRepository) {


    private var notes: List<Note> = emptyList()


    fun loadNotes() {
        notes = repository.getNotes()
    }


    fun addNote(note: Note) {
        repository.addNote(note)
        loadNotes()
    }


    fun getAllNotes(): List<Note> {
        return notes
    }


    fun search(query: String): List<Note> {
        return repository.searchNotes(query)
    }
}


// -------------------- Main Function --------------------

fun main() {


    // Creating layers
    val dao = NoteDaoImpl()

    val repository = NoteRepository(dao)

    val viewModel = NoteViewModel(repository)



    // Adding notes
    viewModel.addNote(
        Note(
            1,
            "Kotlin Basics",
            "Variables, Functions and Classes"
        )
    )


    viewModel.addNote(
        Note(
            2,
            "Room Database",
            "Entity, DAO and Repository"
        )
    )


    viewModel.addNote(
        Note(
            3,
            "MVVM Architecture",
            "ViewModel and Data Flow"
        )
    )



    // Display all notes
    println("========== ALL NOTES ==========")

    viewModel.getAllNotes().forEach { note ->

        println(
            "${note.id}. ${note.title} - ${note.description}"
        )
    }



    // Search notes
    println("\n========== SEARCH RESULT: Room ==========")

    val searchResults = viewModel.search("Room")


    if (searchResults.isEmpty()) {

        println("No notes found.")

    } else {

        searchResults.forEach { note ->

            println(
                "${note.id}. ${note.title} - ${note.description}"
            )
        }
    }



    println("\n========== SEARCH RESULT: Kotlin ==========")

    viewModel.search("Kotlin").forEach { note ->

        println(
            "${note.id}. ${note.title} - ${note.description}"
        )
    }
}
package com.example.ms1.note;

import com.example.ms1.note.note.NoteService;
import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookRepository;
import com.example.ms1.note.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final NotebookService notebookService;
    private final NoteService noteService;

    public Notebook getNotebook(Long notebookId) {
        return notebookService.getNotebook(notebookId);
    }

    public List<Notebook> getNotebookList() {
        return notebookService.getNotebookList();
    }

    public void saveDefault() {
        Notebook notebook = new Notebook();
        notebook.setName("새노트북");

        notebookService.save(notebook);
        noteService.saveDefault(notebook);

    }
}

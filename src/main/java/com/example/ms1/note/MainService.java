package com.example.ms1.note;

import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteService;
import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookRepository;
import com.example.ms1.note.notebook.NotebookService;
import com.sun.tools.javac.Main;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final NotebookService notebookService;
    private final NoteService noteService;

    public MainDataDto getDefaultMainData() {
        List<Notebook> notebookList = notebookService.getNotebookList();

        if (notebookList.isEmpty()) {
            Notebook notebook = this.saveDefaultNotebook();
            notebookList.add(notebook);
        }

        Notebook targetNotebook = notebookList.get(0);
        List<Note> noteList = targetNotebook.getNoteList();
        Note targetNote = noteList.get(0);

        MainDataDto mainDataDto = new MainDataDto(notebookList, targetNotebook, noteList, targetNote);
        return mainDataDto;
    }

    public MainDataDto getMainData(Long notebookId, Long noteId) {

        MainDataDto mainDataDto = this.getDefaultMainData();
        Notebook targetNotebook = this.getNotebook(notebookId);
        Note targetNote = noteService.getNote(noteId);

        mainDataDto.setTargetNotebook(targetNotebook);
        mainDataDto.setTargetNote(targetNote);

        return mainDataDto;
    }

    public Notebook getNotebook(Long notebookId) {
        return notebookService.getNotebook(notebookId);
    }

    public List<Notebook> getNotebookList() {
        return notebookService.getNotebookList();
    }

    public Notebook saveDefaultNotebook() {
        Notebook notebook = new Notebook();
        notebook.setName("새노트북");

        notebookService.save(notebook);
        noteService.saveDefault(notebook);

        return notebook;
    }
}

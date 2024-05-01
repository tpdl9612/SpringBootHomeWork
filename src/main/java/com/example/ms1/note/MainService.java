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
        mainDataDto.setNoteList(targetNotebook.getNoteList());

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

        Note note = noteService.saveDefault();
        notebook.addNote(note);

        return notebookService.save(notebook);
    }

    public void saveGroupNotebook(Long notebookId) {
        Notebook parent = this.getNotebook(notebookId);
        Notebook child = this.saveDefaultNotebook();
        parent.addChild(child);

        notebookService.save(parent);
    }

    public Notebook addToNotebook(Long notebookId) {
        Notebook notebook = this.getNotebook(notebookId);
        Note note = noteService.saveDefault();
        notebook.addNote(note);

        return notebookService.save(notebook);
    }

    public void delete(Long id) {

        Notebook notebook = this.getNotebook(id);

        if(notebook.getChildren().isEmpty()) {
            deleteBasic(notebook);
        }
        else {
            deleteGroup(notebook);
        }
    }

    public void deleteGroup(Notebook notebook) {

        // 먼저 자식 노트북 삭제
        List<Notebook> children = notebook.getChildren();
        for (Notebook child : children) {
            // 자식 노트북의 노트 먼저 삭제
            deleteBasic(child);
        }
        // 본인 삭제
        // 노트 먼저 삭제
        // 본인 노트북 삭제
        deleteBasic(notebook);
    }

    public void deleteBasic(Notebook notebook) {

        List<Note> noteList = notebook.getNoteList();
        // 노트 먼저 삭제
        for (Note note : noteList) {
            noteService.delete(note.getId());
        }
        // 노트북 삭제
        notebookService.delete(notebook.getId());
    }
}

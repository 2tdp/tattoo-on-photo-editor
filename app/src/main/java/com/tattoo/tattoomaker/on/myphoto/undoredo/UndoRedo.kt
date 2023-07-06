package com.tattoo.tattoomaker.on.myphoto.undoredo

import com.tattoo.tattoomaker.on.myphoto.model.UndoRedoModel

class UndoRedo(private var onUndoRedoEventListener: OnUndoRedoEventListener?) {
    interface OnUndoRedoEventListener {
        fun onUndoFinished(projectOriginator: UndoRedoModel?)
        fun onRedoFinished(projectOriginator: UndoRedoModel?)
        fun onStateSaveFinished()
    }

    private val projectCareTaker = ProjectCareTaker()
    var projectOriginator: UndoRedoModel? = null

    fun setState() {
        if (projectOriginator == null) return
        val memento = projectOriginator!!.createMemento()
        projectCareTaker.addMementoToStack(memento)
        onUndoRedoEventListener?.onStateSaveFinished()
    }

    fun undo() {
        val memento = projectCareTaker.getUndoMemento()
        memento?.let { projectOriginator!!.restoreMemento(it) }

        onUndoRedoEventListener?.onUndoFinished(projectOriginator)
    }

    fun redo() {
        val memento = projectCareTaker.getRedoMemento()
        memento?.let { projectOriginator!!.restoreMemento(it) }

        onUndoRedoEventListener?.onRedoFinished(projectOriginator)
    }

    val isUndoIsPossible: Boolean get() = projectCareTaker.isUndoPossible()
    val isRedoIsPossible: Boolean get() = projectCareTaker.isRedoPossible()
}
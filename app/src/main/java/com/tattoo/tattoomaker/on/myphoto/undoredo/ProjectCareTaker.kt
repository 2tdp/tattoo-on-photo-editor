package com.tattoo.tattoomaker.on.myphoto.undoredo

import java.util.*

class ProjectCareTaker {
    private val undoStack = Stack<ProjectMemento>()
    private val redoStack = Stack<ProjectMemento>()

    fun getUndoMemento(): ProjectMemento? {
        if (undoStack.size >= 1) {
            redoStack.push(undoStack.pop())
            if (undoStack.size > 0) return undoStack.peek()
        }
        return null
    }

    fun getRedoMemento(): ProjectMemento? {
        if (redoStack.size != 0) {
            val m = redoStack.pop()
            undoStack.push(m)
            return m
        }
        return null
    }

    fun addMementoToStack(projectMemento: ProjectMemento?) {
        if (projectMemento != null) {
            undoStack.push(projectMemento)
            redoStack.clear()
        }
    }

    fun isUndoPossible(): Boolean {
        return undoStack.size >= 1
    }

    fun isRedoPossible(): Boolean {
        return redoStack.size >= 1
    }
}
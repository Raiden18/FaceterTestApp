package com.rv1den.facetertest.presentation.screens.camera.view.commands

//В данном случае паттерн команда нужна для того, чтобы разгрузить активити и для повышения связности кода (cohesion)
interface Command {
    fun execute()
}
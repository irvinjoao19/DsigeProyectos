package com.dsige.dsigeproyectos.data.local.model

open class MenuPrincipal {
    var id: Int = 0
    var title: String = ""
    var image: Int = 0
    var position: Int = 0

    constructor()

    constructor(id: Int, title: String, image: Int) {
        this.id = id
        this.title = title
        this.image = image
    }

    constructor(id: Int, title: String, image: Int, position: Int) {
        this.id = id
        this.title = title
        this.image = image
        this.position = position
    }
}
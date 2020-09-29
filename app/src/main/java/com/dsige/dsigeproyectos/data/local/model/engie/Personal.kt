package com.dsige.dsigeproyectos.data.local.model.engie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Personal  {
    
    @PrimaryKey(autoGenerate = true)
    var personalId: Int = 0
    var empresaId: Int = 0
    var nroDocumento: String = ""
    var apellido: String = ""
    var nombre: String = ""

}
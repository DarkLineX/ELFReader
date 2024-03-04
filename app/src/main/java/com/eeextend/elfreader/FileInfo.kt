package com.eeextend.elfreader

class FileInfo {
    var typeName:String =""
    var typeValue:String =""

    constructor(typeName: String?, typeValue: String?) {
        if (typeName != null) {
            this.typeName = typeName
        }
        if (typeValue != null) {
            this.typeValue = typeValue
        }
    }
}
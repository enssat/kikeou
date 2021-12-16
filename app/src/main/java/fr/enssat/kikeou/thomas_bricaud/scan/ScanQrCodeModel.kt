package fr.enssat.kikeou.thomas_bricaud.scan

import androidx.lifecycle.ViewModel

class ScanQrCodeModel(
    name: String,
    email: String,
    phone: String,
    week: String,
    monday: String,
    tuesday: String,
    wednesday: String,
    thursday: String,
    friday: String,
    saturday: String,
) : ViewModel() {
    // personnal information
    var name    = String()
    var email   = String()
    var phone   = String()

    // actual week information
    var week        = String()
    var monday      = String()
    var tuesday     = String()
    var wednesday   = String()
    var thursday    = String()
    var friday      = String()
    var saturday    = String()

    // init view
    init {
        this.name       = name
        this.email      = email
        this.phone      = phone
        this.week       = week
        this.monday     = monday
        this.tuesday    = tuesday
        this.wednesday  = wednesday
        this.thursday   = thursday
        this.friday     = friday
        this.saturday   = saturday
    }

    override fun onCleared() {
        super.onCleared()
    }
}
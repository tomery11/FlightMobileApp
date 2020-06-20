package com.example.flightmobileapp

import kotlin.properties.Delegates

class Command {


    // property (data member)
    private var aileron by Delegates.notNull<Double>()
    private var rudder by Delegates.notNull<Double>()
    private var elevator by Delegates.notNull<Double>()
    private var throttle by Delegates.notNull<Double>()

    constructor(aileron: Double, rudder: Double, elevator: Double, throttle: Double) {
        this.aileron = aileron
        this.rudder = rudder
        this.elevator = elevator
        this.throttle = throttle
    }
}
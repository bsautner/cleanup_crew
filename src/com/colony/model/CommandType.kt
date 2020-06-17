package com.colony.model

enum class CommandType(val id: Int) {


    PING(1), SEEK(2), CALIBRATE(3), SONAR(4);

    companion object {
        val map =  HashMap<Int, CommandType>()

        fun lookup(id : Int) : CommandType? {
            if (map.isEmpty()) {
                for (c in values()) {
                    map.put(c.id, c)
                }
            }
            return map[id]
        }
    }
}
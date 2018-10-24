package kayaklers.sdu.dk.kayaklers

import kayaklers.sdu.dk.kayaklers.services.ServerFacade


class apiTest {

    private val serverService = ServerFacade()


/*
    @Test
    fun testAddandGetLog() {
        val log = Log(1,2,3.0,true,1, mutableListOf(
                GPSPoint(1.0,2.0,3.0),
                GPSPoint(3.0,2.0,1.0),
                GPSPoint(2.0,1.0,3.0)
        ))
        serverService.addLog(log)
        assertEquals(log, serverService.getLog(0))
    }
    */
}

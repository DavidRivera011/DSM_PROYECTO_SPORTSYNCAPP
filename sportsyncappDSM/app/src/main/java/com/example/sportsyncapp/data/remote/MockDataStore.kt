package com.example.sportsyncapp.data.remote

import com.example.sportsyncapp.data.model.*

object MockDataStore {

    val sports = mutableListOf(
        Sport(1, "FÃºtbol", "ic_nav_sports", "Disciplina tradicional de 11 vs 11 jugadores en canchas oficiales."),
        Sport(2, "Basketball", "ic_nav_sports", "Torneos 5 vs 5 y relÃ¡mpagos 3x3 en duela techada."),
        Sport(3, "Voleyball", "ic_nav_sports", "Voleibol de sala y arena institucional."),
        Sport(4, "NataciÃ³n", "ic_nav_sports", "Competencias acuÃ¡ticas en estilo libre, mariposa y dorso.")
    )

    val institutions = mutableListOf(
        Institution(
            1,
            "Universidad de El Salvador",
            null,
            "Formamos profesionales Ã­ntegros y comprometidos con la sociedad.",
            listOf("FÃºtbol", "Basketball", "Voleyball")
        ),
        Institution(
            2,
            "C.D. Ãguilas",
            null,
            "Club deportivo representativo con equipos formativos juveniles.",
            listOf("FÃºtbol", "Basketball")
        ),
        Institution(
            3,
            "Instituto Nacional de los Deportes (INDES)",
            null,
            "Ente rector del deporte nacional promoviendo ligas colegiales.",
            listOf("FÃºtbol", "Basketball", "Voleyball", "NataciÃ³n")
        ),
        Institution(
            4,
            "Colegio San JosÃ©",
            null,
            "InstituciÃ³n educativa participante en copas intercolegiales.",
            listOf("FÃºtbol", "Voleyball")
        )
    )

    val teams = mutableListOf(
        Team(1, 1, "Universidad de El Salvador", 1, "FÃºtbol", "UES FC"),
        Team(2, 2, "C.D. Ãguilas", 1, "FÃºtbol", "Ãguilas FC"),
        Team(3, 4, "Colegio San JosÃ©", 1, "FÃºtbol", "San JosÃ© FC"),
        Team(4, 1, "Universidad de El Salvador", 2, "Basketball", "UES Basket"),
        Team(5, 2, "C.D. Ãguilas", 2, "Basketball", "Ãguilas Basket"),
        Team(6, 4, "Colegio San JosÃ©", 3, "Voleyball", "San JosÃ© Voley")
    )

    val events = mutableListOf(
        SportEvent(
            1,
            1,
            "FÃºtbol",
            1,
            "UES - Sede Central",
            "Torneo Apertura 2024",
            "01/06/2024",
            2,
            "En curso",
            "Torneo oficial de apertura 2024 categorÃ­a libre interuniversitaria."
        ),
        SportEvent(
            2,
            2,
            "Basketball",
            1,
            "Gimnasio UES",
            "Liga Universitaria",
            "15/06/2024",
            2,
            "En curso",
            "Liga de baloncesto universitario con 8 equipos clasificados."
        ),
        SportEvent(
            3,
            3,
            "Voleyball",
            4,
            "Colegio San JosÃ©",
            "Copa Intercolegial",
            "20/06/2024",
            1,
            "Pendiente",
            "Copa anual de voleibol colegial para estudiantes de bachillerato."
        ),
        SportEvent(
            4,
            1,
            "FÃºtbol",
            3,
            "Estadio MÃ¡gico GonzÃ¡lez",
            "Torneo RelÃ¡mpago",
            "05/07/2024",
            1,
            "Pendiente",
            "Torneo eliminatorio de un solo dÃ­a con eliminaciÃ³n directa."
        )
    )

    val matches = mutableListOf(
        Match(
            1,
            1,
            "Torneo Apertura 2024",
            1,
            "UES FC",
            2,
            "Ãguilas FC",
            2,
            1,
            "01/06/2024",
            "14:00",
            3,
            "Finalizado"
        ),
        Match(
            2,
            1,
            "Torneo Apertura 2024",
            3,
            "San JosÃ© FC",
            1,
            "UES FC",
            0,
            0,
            "15/06/2024",
            "16:30",
            2,
            "En curso"
        ),
        Match(
            3,
            2,
            "Liga Universitaria",
            4,
            "UES Basket",
            5,
            "Ãguilas Basket",
            48,
            42,
            "18/06/2024",
            "10:00",
            1,
            "Programado"
        )
    )

    val participants = mutableListOf(
        // Jugadores UES FC
        MatchParticipant(1, 1, 1, "Luis Fernando GÃ³mez", "UES FC", true, "Delantero"),
        MatchParticipant(2, 1, 2, "Carlos HenrÃ­quez", "UES FC", true, "Mediocampista"),
        MatchParticipant(3, 1, 3, "Mateo Rivas", "UES FC", true, "Defensa Central"),
        MatchParticipant(4, 1, 4, "David Linares", "UES FC", true, "Portero"),
        MatchParticipant(5, 1, 5, "Bryan HernÃ¡ndez", "UES FC", false, "Suplente Delantero"),
        // Jugadores Ãguilas FC
        MatchParticipant(6, 1, 6, "Justin RodrÃ­guez", "Ãguilas FC", true, "Delantero"),
        MatchParticipant(7, 1, 7, "CÃ©sar Chevez", "Ãguilas FC", true, "Mediocampista"),
        MatchParticipant(8, 1, 8, "AndrÃ©s Palacios", "Ãguilas FC", true, "Defensa"),
        MatchParticipant(9, 1, 9, "Roberto Ramos", "Ãguilas FC", false, "Suplente")
    )

    val inscriptions = mutableListOf(
        Inscription(
            idInscripcion = 1,
            idEvento = 1,
            eventoNombre = "Torneo Apertura 2024",
            tipo = "Equipo",
            idEquipo = 2,
            equipoNombre = "Ãguilas FC",
            idPerfil = null,
            perfilNombre = null,
            idUsuarioSolicitante = 1,
            nombreSolicitante = "Luis Calvillo",
            fechaInscripcion = "01/05/2024",
            idEstado = 1,
            estadoNombre = "Pendiente"
        ),
        Inscription(
            idInscripcion = 2,
            idEvento = 2,
            eventoNombre = "Liga Universitaria",
            tipo = "Individual",
            idEquipo = null,
            equipoNombre = null,
            idPerfil = 1,
            perfilNombre = "Luis Fernando",
            idUsuarioSolicitante = 1,
            nombreSolicitante = "Luis Fernando",
            fechaInscripcion = "15/05/2024",
            idEstado = 2,
            estadoNombre = "Aprobado"
        ),
        Inscription(
            idInscripcion = 3,
            idEvento = 3,
            eventoNombre = "Copa Intercolegial",
            tipo = "Individual",
            idEquipo = null,
            equipoNombre = null,
            idPerfil = 2,
            perfilNombre = "Carlos HenrÃ­quez",
            idUsuarioSolicitante = 2,
            nombreSolicitante = "Carlos HenrÃ­quez",
            fechaInscripcion = "18/05/2024",
            idEstado = 3,
            estadoNombre = "Rechazado"
        )
    )

    val athleteProfile = AthleteProfile(
        idPerfil = 1,
        idUsuario = 1,
        nombreCompleto = "Luis Fernando GÃ³mez",
        nombreDeportivo = "El Rayo",
        institucionNombre = "Universidad de El Salvador",
        equipoNombre = "UES FC",
        deporteNombre = "FÃºtbol",
        tipoPerfilNombre = "Jugador",
        puntosTotales = 18,
        partidosJugados = 7,
        fotoDeportiva = null
    )

    val rankings = mutableListOf(
        RankingItem(1, "Luis Fernando GÃ³mez", "UES FC", "FÃºtbol", 18),
        RankingItem(2, "Justin RodrÃ­guez", "Ãguilas FC", "FÃºtbol", 15),
        RankingItem(3, "Mateo Rivas", "San JosÃ© FC", "FÃºtbol", 12),
        RankingItem(4, "Carlos HenrÃ­quez", "UES Basket", "Basketball", 11),
        RankingItem(5, "David Linares", "UES FC", "FÃºtbol", 9)
    )
}


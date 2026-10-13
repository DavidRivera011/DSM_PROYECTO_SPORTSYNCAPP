package com.example.sportsyncapp.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sportsyncapp.data.model.Inscription
import com.example.sportsyncapp.data.model.Match
import com.example.sportsyncapp.data.model.Sport
import com.example.sportsyncapp.data.model.SportEvent

class SportSyncDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "sportsync_local.db"
        // Increment version to trigger onUpgrade and create new tables
        const val DATABASE_VERSION = 3

        // Tabla Deportes
        const val TABLE_DEPORTES = "deportes"
        const val COL_DEP_ID = "id_deporte"
        const val COL_DEP_NOMBRE = "nombre_deporte"
        const val COL_DEP_FOTO = "foto_deporte"
        const val COL_DEP_DESC = "descripcion"

        // Tabla Eventos
        const val TABLE_EVENTOS = "eventos"
        const val COL_EVT_ID = "id_evento"
        const val COL_EVT_ID_DEP = "id_deporte"
        const val COL_EVT_DEP_NOMBRE = "deporte_nombre"
        const val COL_EVT_ID_INST = "id_institucion"
        const val COL_EVT_INST_NOMBRE = "institucion_nombre"
        const val COL_EVT_NOMBRE = "nombre"
        const val COL_EVT_FECHA = "fecha"
        const val COL_EVT_ID_ESTADO = "id_estado"
        const val COL_EVT_ESTADO_NOMBRE = "estado_nombre"
        const val COL_EVT_DESC = "descripcion"

        // Tabla Inscripciones
        const val TABLE_INSCRIPCIONES = "inscripciones"
        const val COL_INS_ID = "id_inscripcion"
        const val COL_INS_ID_EVT = "id_evento"
        const val COL_INS_EVT_NOMBRE = "evento_nombre"
        const val COL_INS_TIPO = "tipo"
        const val COL_INS_ID_EQUIPO = "id_equipo"
        const val COL_INS_EQUIPO_NOMBRE = "equipo_nombre"
        const val COL_INS_ID_PERFIL = "id_perfil"
        const val COL_INS_PERFIL_NOMBRE = "perfil_nombre"
        const val COL_INS_ID_USER = "id_usuario_solicitante"
        const val COL_INS_SOLICITANTE = "nombre_solicitante"
        const val COL_INS_FECHA = "fecha_inscripcion"
        const val COL_INS_ID_ESTADO = "id_estado"
        const val COL_INS_ESTADO_NOMBRE = "estado_nombre"

        // Tabla Partidos
        const val TABLE_PARTIDOS = "partidos"
        const val COL_PAR_ID = "id_partido"
        const val COL_PAR_ID_EVT = "id_evento"
        const val COL_PAR_EVT_NOMBRE = "evento_nombre"
        const val COL_PAR_ID_LOCAL = "id_equipo_local"
        const val COL_PAR_LOCAL_NOMBRE = "equipo_local_nombre"
        const val COL_PAR_ID_VISIT = "id_equipo_visitante"
        const val COL_PAR_VISIT_NOMBRE = "equipo_visitante_nombre"
        const val COL_PAR_MARCADOR_L = "marcador_local"
        const val COL_PAR_MARCADOR_V = "marcador_visitante"
        const val COL_PAR_FECHA = "fecha"
        const val COL_PAR_HORA = "hora"
        const val COL_PAR_ID_ESTADO = "id_estado"
        const val COL_PAR_ESTADO_NOMBRE = "estado_nombre"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // ---- Deportes ----
        db.execSQL(
            """CREATE TABLE $TABLE_DEPORTES (
                $COL_DEP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DEP_NOMBRE TEXT NOT NULL,
                $COL_DEP_FOTO TEXT,
                $COL_DEP_DESC TEXT
            )"""
        )
        // Datos iniciales en SQLite
        insertInitialSport(db, "FÃºtbol", "ic_nav_sports", "Disciplina tradicional de 11 vs 11 jugadores.")
        insertInitialSport(db, "Basketball", "ic_nav_sports", "Torneos 5 vs 5 y relÃ¡mpagos 3x3 en duela.")
        insertInitialSport(db, "Voleyball", "ic_nav_sports", "Voleibol institucional.")

        // ---- Eventos ----
        db.execSQL(
            """CREATE TABLE $TABLE_EVENTOS (
                $COL_EVT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EVT_ID_DEP INTEGER,
                $COL_EVT_DEP_NOMBRE TEXT,
                $COL_EVT_ID_INST INTEGER,
                $COL_EVT_INST_NOMBRE TEXT,
                $COL_EVT_NOMBRE TEXT NOT NULL,
                $COL_EVT_FECHA TEXT,
                $COL_EVT_ID_ESTADO INTEGER DEFAULT 1,
                $COL_EVT_ESTADO_NOMBRE TEXT DEFAULT 'Pendiente',
                $COL_EVT_DESC TEXT
            )"""
        )
        // Eventos demo
        insertInitialEvent(db, 1, "FÃºtbol", 1, "UES - Sede Central", "Torneo Apertura 2024", "01/06/2024", 2, "En curso", "Torneo oficial de apertura 2024.")
        insertInitialEvent(db, 2, "Basketball", 1, "Gimnasio UES", "Liga Universitaria", "15/06/2024", 2, "En curso", "Liga de baloncesto universitario con 8 equipos.")
        insertInitialEvent(db, 3, "Voleyball", 4, "Colegio San JosÃ©", "Copa Intercolegial", "20/06/2024", 1, "Pendiente", "Copa anual de voleibol colegial.")

        // ---- Inscripciones ----
        db.execSQL(
            """CREATE TABLE $TABLE_INSCRIPCIONES (
                $COL_INS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_INS_ID_EVT INTEGER NOT NULL,
                $COL_INS_EVT_NOMBRE TEXT,
                $COL_INS_TIPO TEXT DEFAULT 'Equipo',
                $COL_INS_ID_EQUIPO INTEGER,
                $COL_INS_EQUIPO_NOMBRE TEXT,
                $COL_INS_ID_PERFIL INTEGER,
                $COL_INS_PERFIL_NOMBRE TEXT,
                $COL_INS_ID_USER INTEGER,
                $COL_INS_SOLICITANTE TEXT,
                $COL_INS_FECHA TEXT,
                $COL_INS_ID_ESTADO INTEGER DEFAULT 1,
                $COL_INS_ESTADO_NOMBRE TEXT DEFAULT 'Pendiente'
            )"""
        )
        // Inscripciones demo
        insertInitialInscription(db, 1, "Torneo Apertura 2024", "Equipo", 2, "Ãguilas FC", null, null, 1, "Luis Calvillo", "01/05/2024", 1, "Pendiente")
        insertInitialInscription(db, 2, "Liga Universitaria", "Individual", null, null, 1, "Luis Fernando", 1, "Luis Fernando", "15/05/2024", 2, "Aprobado")

        // ---- Partidos ----
        db.execSQL(
            """CREATE TABLE $TABLE_PARTIDOS (
                $COL_PAR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PAR_ID_EVT INTEGER,
                $COL_PAR_EVT_NOMBRE TEXT,
                $COL_PAR_ID_LOCAL INTEGER,
                $COL_PAR_LOCAL_NOMBRE TEXT,
                $COL_PAR_ID_VISIT INTEGER,
                $COL_PAR_VISIT_NOMBRE TEXT,
                $COL_PAR_MARCADOR_L INTEGER DEFAULT 0,
                $COL_PAR_MARCADOR_V INTEGER DEFAULT 0,
                $COL_PAR_FECHA TEXT,
                $COL_PAR_HORA TEXT,
                $COL_PAR_ID_ESTADO INTEGER DEFAULT 1,
                $COL_PAR_ESTADO_NOMBRE TEXT DEFAULT 'Programado'
            )"""
        )
        // Partidos demo
        insertInitialMatch(db, 1, "Torneo Apertura 2024", 1, "UES FC", 2, "Ãguilas FC", 2, 1, "01/06/2024", "14:00", 3, "Finalizado")
        insertInitialMatch(db, 1, "Torneo Apertura 2024", 3, "San JosÃ© FC", 1, "UES FC", 0, 0, "15/06/2024", "16:30", 2, "En curso")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARTIDOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INSCRIPCIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DEPORTES")
        onCreate(db)
    }

    // ============================================================
    // Deportes
    // ============================================================

    private fun insertInitialSport(db: SQLiteDatabase, nombre: String, foto: String, desc: String) {
        val v = ContentValues().apply {
            put(COL_DEP_NOMBRE, nombre)
            put(COL_DEP_FOTO, foto)
            put(COL_DEP_DESC, desc)
        }
        db.insert(TABLE_DEPORTES, null, v)
    }

    fun getAllSports(): List<Sport> {
        val list = mutableListOf<Sport>()
        val db = readableDatabase
        val cursor = db.query(TABLE_DEPORTES, null, null, null, null, null, "$COL_DEP_ID ASC")
        while (cursor.moveToNext()) {
            list.add(
                Sport(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_DEP_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DEP_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DEP_FOTO)) ?: "",
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DEP_DESC))
                )
            )
        }
        cursor.close()
        return list
    }

    fun insertSport(sport: Sport): Long {
        val db = writableDatabase
        val v = ContentValues().apply {
            put(COL_DEP_NOMBRE, sport.nombreDeporte)
            put(COL_DEP_FOTO, sport.fotoDeporte)
            put(COL_DEP_DESC, sport.descripcion)
        }
        return db.insert(TABLE_DEPORTES, null, v)
    }

    fun deleteSport(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_DEPORTES, "$COL_DEP_ID = ?", arrayOf(id.toString()))
    }

    // ============================================================
    // Eventos
    // ============================================================

    private fun insertInitialEvent(
        db: SQLiteDatabase, idDep: Int, depNombre: String, idInst: Int, instNombre: String,
        nombre: String, fecha: String, idEstado: Int, estadoNombre: String, desc: String
    ) {
        val v = ContentValues().apply {
            put(COL_EVT_ID_DEP, idDep); put(COL_EVT_DEP_NOMBRE, depNombre)
            put(COL_EVT_ID_INST, idInst); put(COL_EVT_INST_NOMBRE, instNombre)
            put(COL_EVT_NOMBRE, nombre); put(COL_EVT_FECHA, fecha)
            put(COL_EVT_ID_ESTADO, idEstado); put(COL_EVT_ESTADO_NOMBRE, estadoNombre)
            put(COL_EVT_DESC, desc)
        }
        db.insert(TABLE_EVENTOS, null, v)
    }

    fun getAllEvents(): List<SportEvent> {
        val list = mutableListOf<SportEvent>()
        val db = readableDatabase
        val cursor = db.query(TABLE_EVENTOS, null, null, null, null, null, "$COL_EVT_ID ASC")
        while (cursor.moveToNext()) {
            list.add(
                SportEvent(
                    idEvento = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EVT_ID)),
                    idDeporte = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EVT_ID_DEP)),
                    deporteNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_EVT_DEP_NOMBRE)) ?: "",
                    idInstitucion = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EVT_ID_INST)),
                    institucionNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_EVT_INST_NOMBRE)) ?: "",
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_EVT_NOMBRE)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(COL_EVT_FECHA)) ?: "",
                    idEstado = cursor.getInt(cursor.getColumnIndexOrThrow(COL_EVT_ID_ESTADO)),
                    estadoNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_EVT_ESTADO_NOMBRE)) ?: "Pendiente",
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COL_EVT_DESC))
                )
            )
        }
        cursor.close()
        return list
    }

    fun insertEvent(event: SportEvent): Long {
        val db = writableDatabase
        val v = ContentValues().apply {
            put(COL_EVT_ID_DEP, event.idDeporte)
            put(COL_EVT_DEP_NOMBRE, event.deporteNombre)
            put(COL_EVT_ID_INST, event.idInstitucion)
            put(COL_EVT_INST_NOMBRE, event.institucionNombre)
            put(COL_EVT_NOMBRE, event.nombre)
            put(COL_EVT_FECHA, event.fecha)
            put(COL_EVT_ID_ESTADO, event.idEstado)
            put(COL_EVT_ESTADO_NOMBRE, event.estadoNombre)
            put(COL_EVT_DESC, event.descripcion)
        }
        return db.insert(TABLE_EVENTOS, null, v)
    }

    fun deleteEvent(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_EVENTOS, "$COL_EVT_ID = ?", arrayOf(id.toString()))
    }

    // ============================================================
    // Inscripciones
    // ============================================================

    private fun insertInitialInscription(
        db: SQLiteDatabase, idEvt: Int, evtNombre: String, tipo: String,
        idEquipo: Int?, equipoNombre: String?, idPerfil: Int?, perfilNombre: String?,
        idUser: Int, solicitante: String, fecha: String, idEstado: Int, estadoNombre: String
    ) {
        val v = ContentValues().apply {
            put(COL_INS_ID_EVT, idEvt); put(COL_INS_EVT_NOMBRE, evtNombre)
            put(COL_INS_TIPO, tipo)
            if (idEquipo != null) put(COL_INS_ID_EQUIPO, idEquipo) else putNull(COL_INS_ID_EQUIPO)
            if (equipoNombre != null) put(COL_INS_EQUIPO_NOMBRE, equipoNombre) else putNull(COL_INS_EQUIPO_NOMBRE)
            if (idPerfil != null) put(COL_INS_ID_PERFIL, idPerfil) else putNull(COL_INS_ID_PERFIL)
            if (perfilNombre != null) put(COL_INS_PERFIL_NOMBRE, perfilNombre) else putNull(COL_INS_PERFIL_NOMBRE)
            put(COL_INS_ID_USER, idUser); put(COL_INS_SOLICITANTE, solicitante)
            put(COL_INS_FECHA, fecha); put(COL_INS_ID_ESTADO, idEstado); put(COL_INS_ESTADO_NOMBRE, estadoNombre)
        }
        db.insert(TABLE_INSCRIPCIONES, null, v)
    }

    fun getAllInscriptions(): List<Inscription> {
        val list = mutableListOf<Inscription>()
        val db = readableDatabase
        val cursor = db.query(TABLE_INSCRIPCIONES, null, null, null, null, null, "$COL_INS_ID DESC")
        while (cursor.moveToNext()) {
            list.add(
                Inscription(
                    idInscripcion = cursor.getInt(cursor.getColumnIndexOrThrow(COL_INS_ID)),
                    idEvento = cursor.getInt(cursor.getColumnIndexOrThrow(COL_INS_ID_EVT)),
                    eventoNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_INS_EVT_NOMBRE)) ?: "",
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_INS_TIPO)) ?: "Equipo",
                    idEquipo = cursor.getInt(cursor.getColumnIndexOrThrow(COL_INS_ID_EQUIPO)).takeIf { cursor.getColumnIndexOrThrow(COL_INS_ID_EQUIPO) >= 0 && !cursor.isNull(cursor.getColumnIndexOrThrow(COL_INS_ID_EQUIPO)) },
                    equipoNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_INS_EQUIPO_NOMBRE)),
                    idPerfil = cursor.getInt(cursor.getColumnIndexOrThrow(COL_INS_ID_PERFIL)).takeIf { !cursor.isNull(cursor.getColumnIndexOrThrow(COL_INS_ID_PERFIL)) },
                    perfilNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_INS_PERFIL_NOMBRE)),
                    idUsuarioSolicitante = cursor.getInt(cursor.getColumnIndexOrThrow(COL_INS_ID_USER)),
                    nombreSolicitante = cursor.getString(cursor.getColumnIndexOrThrow(COL_INS_SOLICITANTE)) ?: "",
                    fechaInscripcion = cursor.getString(cursor.getColumnIndexOrThrow(COL_INS_FECHA)) ?: "",
                    idEstado = cursor.getInt(cursor.getColumnIndexOrThrow(COL_INS_ID_ESTADO)),
                    estadoNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_INS_ESTADO_NOMBRE)) ?: "Pendiente"
                )
            )
        }
        cursor.close()
        return list
    }

    fun insertInscription(ins: Inscription): Long {
        val db = writableDatabase
        val v = ContentValues().apply {
            put(COL_INS_ID_EVT, ins.idEvento)
            put(COL_INS_EVT_NOMBRE, ins.eventoNombre)
            put(COL_INS_TIPO, ins.tipo)
            if (ins.idEquipo != null) put(COL_INS_ID_EQUIPO, ins.idEquipo) else putNull(COL_INS_ID_EQUIPO)
            if (ins.equipoNombre != null) put(COL_INS_EQUIPO_NOMBRE, ins.equipoNombre) else putNull(COL_INS_EQUIPO_NOMBRE)
            if (ins.idPerfil != null) put(COL_INS_ID_PERFIL, ins.idPerfil) else putNull(COL_INS_ID_PERFIL)
            if (ins.perfilNombre != null) put(COL_INS_PERFIL_NOMBRE, ins.perfilNombre) else putNull(COL_INS_PERFIL_NOMBRE)
            put(COL_INS_ID_USER, ins.idUsuarioSolicitante)
            put(COL_INS_SOLICITANTE, ins.nombreSolicitante)
            put(COL_INS_FECHA, ins.fechaInscripcion)
            put(COL_INS_ID_ESTADO, ins.idEstado)
            put(COL_INS_ESTADO_NOMBRE, ins.estadoNombre)
        }
        return db.insert(TABLE_INSCRIPCIONES, null, v)
    }

    fun updateInscriptionStatus(id: Int, idEstado: Int, estadoNombre: String): Int {
        val db = writableDatabase
        val v = ContentValues().apply {
            put(COL_INS_ID_ESTADO, idEstado)
            put(COL_INS_ESTADO_NOMBRE, estadoNombre)
        }
        return db.update(TABLE_INSCRIPCIONES, v, "$COL_INS_ID = ?", arrayOf(id.toString()))
    }

    fun deleteInscription(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_INSCRIPCIONES, "$COL_INS_ID = ?", arrayOf(id.toString()))
    }

    // ============================================================
    // Partidos
    // ============================================================

    private fun insertInitialMatch(
        db: SQLiteDatabase, idEvt: Int, evtNombre: String,
        idLocal: Int, localNombre: String, idVisit: Int, visitNombre: String,
        marcL: Int, marcV: Int, fecha: String, hora: String, idEstado: Int, estadoNombre: String
    ) {
        val v = ContentValues().apply {
            put(COL_PAR_ID_EVT, idEvt); put(COL_PAR_EVT_NOMBRE, evtNombre)
            put(COL_PAR_ID_LOCAL, idLocal); put(COL_PAR_LOCAL_NOMBRE, localNombre)
            put(COL_PAR_ID_VISIT, idVisit); put(COL_PAR_VISIT_NOMBRE, visitNombre)
            put(COL_PAR_MARCADOR_L, marcL); put(COL_PAR_MARCADOR_V, marcV)
            put(COL_PAR_FECHA, fecha); put(COL_PAR_HORA, hora)
            put(COL_PAR_ID_ESTADO, idEstado); put(COL_PAR_ESTADO_NOMBRE, estadoNombre)
        }
        db.insert(TABLE_PARTIDOS, null, v)
    }

    fun getAllMatches(): List<Match> {
        val list = mutableListOf<Match>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PARTIDOS, null, null, null, null, null, "$COL_PAR_ID ASC")
        while (cursor.moveToNext()) {
            list.add(
                Match(
                    idPartido = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAR_ID)),
                    idEvento = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAR_ID_EVT)),
                    eventoNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAR_EVT_NOMBRE)),
                    idEquipoLocal = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAR_ID_LOCAL)),
                    equipoLocalNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAR_LOCAL_NOMBRE)) ?: "",
                    idEquipoVisitante = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAR_ID_VISIT)),
                    equipoVisitanteNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAR_VISIT_NOMBRE)) ?: "",
                    marcadorLocal = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAR_MARCADOR_L)),
                    marcadorVisitante = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAR_MARCADOR_V)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAR_FECHA)) ?: "",
                    hora = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAR_HORA)) ?: "",
                    idEstado = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PAR_ID_ESTADO)),
                    estadoNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_PAR_ESTADO_NOMBRE)) ?: "Programado"
                )
            )
        }
        cursor.close()
        return list
    }

    fun updateMatchScore(id: Int, local: Int, visitante: Int): Int {
        val db = writableDatabase
        val v = ContentValues().apply {
            put(COL_PAR_MARCADOR_L, local)
            put(COL_PAR_MARCADOR_V, visitante)
        }
        return db.update(TABLE_PARTIDOS, v, "$COL_PAR_ID = ?", arrayOf(id.toString()))
    }

    fun insertMatch(match: Match): Long {
        val db = writableDatabase
        val v = ContentValues().apply {
            put(COL_PAR_ID_EVT, match.idEvento)
            put(COL_PAR_EVT_NOMBRE, match.eventoNombre)
            put(COL_PAR_ID_LOCAL, match.idEquipoLocal)
            put(COL_PAR_LOCAL_NOMBRE, match.equipoLocalNombre)
            put(COL_PAR_ID_VISIT, match.idEquipoVisitante)
            put(COL_PAR_VISIT_NOMBRE, match.equipoVisitanteNombre)
            put(COL_PAR_MARCADOR_L, match.marcadorLocal)
            put(COL_PAR_MARCADOR_V, match.marcadorVisitante)
            put(COL_PAR_FECHA, match.fecha)
            put(COL_PAR_HORA, match.hora)
            put(COL_PAR_ID_ESTADO, match.idEstado)
            put(COL_PAR_ESTADO_NOMBRE, match.estadoNombre)
        }
        return db.insert(TABLE_PARTIDOS, null, v)
    }
}


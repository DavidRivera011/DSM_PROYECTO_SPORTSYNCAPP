package com.example.sportsyncapp.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException

/**
 * MockInterceptor â€” tries the real HTTP call first.
 * If the server is unreachable (IOException), falls back to an in-memory mock
 * that reads the REAL request body so created/updated data reflects what the user typed.
 */
class MockInterceptor : Interceptor {

    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        try {
            // Attempt real network call first
            val realResponse = chain.proceed(request)
            if (realResponse.isSuccessful) {
                return realResponse
            }
            // Server returned an error code (4xx/5xx) but we are reachable;
            // fall through to mock so the demo always works.
            realResponse.close()
        } catch (_: IOException) {
            // Server not running or no network â†’ use mock
        }

        val url = request.url.toString()
        val method = request.method
        val path = request.url.encodedPath

        // Read request body as JSON (safe â€” body may be null for GET/DELETE)
        val bodyJson: JsonObject? = runCatching {
            request.body?.let { body ->
                val buffer = Buffer()
                body.writeTo(buffer)
                gson.fromJson(buffer.readUtf8(), JsonObject::class.java)
            }
        }.getOrNull()

        val (statusCode, responseBody) = handleMockRequest(path, method, request, bodyJson)

        return Response.Builder()
            .code(statusCode)
            .message(if (statusCode == 200) "OK" else "Error")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(responseBody.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun handleMockRequest(
        path: String,
        method: String,
        request: Request,
        body: JsonObject?
    ): Pair<Int, String> {

        return when {

            // â”€â”€ Deportes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.endsWith("/api/deportes") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.sports)
            }
            path.endsWith("/api/deportes") && method == "POST" -> {
                val nombre = body?.get("nombre_deporte")?.asString ?: "Nuevo Deporte"
                val foto = body?.get("foto_deporte")?.asString ?: "ic_nav_sports"
                val desc = body?.get("descripcion")?.asString ?: ""
                val newId = (MockDataStore.sports.maxOfOrNull { it.idDeporte } ?: 0) + 1
                val created = com.example.sportsyncapp.data.model.Sport(
                    idDeporte = newId,
                    nombreDeporte = nombre,
                    fotoDeporte = foto,
                    descripcion = desc
                )
                MockDataStore.sports.add(created)
                200 to gson.toJson(created)
            }
            path.contains("/api/deportes/") && method == "DELETE" -> {
                val id = path.substringAfterLast("/").toIntOrNull() ?: 0
                MockDataStore.sports.removeAll { it.idDeporte == id }
                200 to "{\"success\":true,\"message\":\"Deporte eliminado\"}"
            }

            // â”€â”€ Instituciones â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.endsWith("/api/instituciones") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.institutions)
            }

            // â”€â”€ Equipos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.endsWith("/api/equipos") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.teams)
            }

            // â”€â”€ Eventos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.endsWith("/api/eventos") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.events)
            }
            path.endsWith("/api/eventos") && method == "POST" -> {
                val nombre = body?.get("nombre")?.asString ?: "Evento Sin Nombre"
                val fecha = body?.get("fecha")?.asString ?: ""
                val instNombre = body?.get("institucion_nombre")?.asString ?: "UES"
                val depNombre = body?.get("deporte_nombre")?.asString ?: "FÃºtbol"
                val desc = body?.get("descripcion")?.asString
                val newId = (MockDataStore.events.maxOfOrNull { it.idEvento } ?: 0) + 1
                val newEvent = com.example.sportsyncapp.data.model.SportEvent(
                    idEvento = newId,
                    nombre = nombre,
                    fecha = fecha,
                    estadoNombre = "Pendiente",
                    idEstado = 1,
                    deporteNombre = depNombre,
                    institucionNombre = instNombre,
                    descripcion = desc
                )
                MockDataStore.events.add(newEvent)
                200 to gson.toJson(newEvent)
            }

            // â”€â”€ Partidos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.endsWith("/api/partidos") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.matches)
            }
            path.contains("/api/partidos/") && path.contains("/marcador") && method == "PUT" -> {
                val id = path.substringAfter("/api/partidos/").substringBefore("/").toIntOrNull() ?: 0
                val local = request.url.queryParameter("local")?.toIntOrNull() ?: 0
                val visitante = request.url.queryParameter("visitante")?.toIntOrNull() ?: 0
                val match = MockDataStore.matches.find { it.idPartido == id }
                if (match != null) {
                    match.marcadorLocal = local
                    match.marcadorVisitante = visitante
                    200 to gson.toJson(match)
                } else {
                    404 to "{\"error\":\"Partido no encontrado\"}"
                }
            }

            // â”€â”€ Participantes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.contains("/api/partidos/") && path.contains("/participantes") && method == "GET" -> {
                val id = path.substringAfter("/api/partidos/").substringBefore("/").toIntOrNull() ?: 0
                200 to gson.toJson(MockDataStore.participants.filter { it.idPartido == id })
            }

            // â”€â”€ Inscripciones â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.endsWith("/api/inscripciones") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.inscriptions)
            }
            path.endsWith("/api/inscripciones") && method == "POST" -> {
                val eventoNombre = body?.get("evento_nombre")?.asString ?: "Torneo Deportivo"
                val tipo = body?.get("tipo")?.asString ?: "Equipo"
                val equipoNombre = body?.get("equipo_nombre")?.asString
                val solicitante = body?.get("nombre_solicitante")?.asString ?: "Usuario"
                val fecha = body?.get("fecha_inscripcion")?.asString ?: "Hoy"
                val newId = (MockDataStore.inscriptions.maxOfOrNull { it.idInscripcion } ?: 0) + 1
                val newIns = com.example.sportsyncapp.data.model.Inscription(
                    idInscripcion = newId,
                    idEvento = body?.get("id_evento")?.asInt ?: 1,
                    eventoNombre = eventoNombre,
                    tipo = tipo,
                    equipoNombre = equipoNombre,
                    nombreSolicitante = solicitante,
                    fechaInscripcion = fecha,
                    idEstado = 1,
                    estadoNombre = "Pendiente"
                )
                MockDataStore.inscriptions.add(0, newIns)
                200 to gson.toJson(newIns)
            }
            path.contains("/api/inscripciones/") && path.contains("/estado") && method == "PUT" -> {
                val id = path.substringAfter("/api/inscripciones/").substringBefore("/").toIntOrNull() ?: 0
                val idEstado = request.url.queryParameter("id_estado")?.toIntOrNull() ?: 1
                val ins = MockDataStore.inscriptions.find { it.idInscripcion == id }
                if (ins != null) {
                    ins.idEstado = idEstado
                    ins.estadoNombre = when (idEstado) {
                        2 -> "Aprobado"
                        3 -> "Rechazado"
                        else -> "Pendiente"
                    }
                    200 to gson.toJson(ins)
                } else {
                    404 to "{\"error\":\"Inscripcion no encontrada\"}"
                }
            }
            path.contains("/api/inscripciones/") && method == "DELETE" -> {
                val id = path.substringAfterLast("/").toIntOrNull() ?: 0
                MockDataStore.inscriptions.removeAll { it.idInscripcion == id }
                200 to "{\"success\":true,\"message\":\"InscripciÃ³n cancelada\"}"
            }

            // â”€â”€ Perfiles â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.contains("/api/perfiles/") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.athleteProfile)
            }

            // â”€â”€ Ranking â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
            path.endsWith("/api/ranking") && method == "GET" -> {
                200 to gson.toJson(MockDataStore.rankings)
            }

            else -> {
                200 to "{}"
            }
        }
    }
}

